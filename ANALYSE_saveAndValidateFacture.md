# Rapport d'analyse approfondie — `saveAndValidateFacture`

> **Contexte** : Le contenu de la facture enregistrée en base ne correspond pas toujours à ce qui est imprimé sur le ticket.
> Fichiers analysés : `ClaviersController.java`, `HomeCaisseController.java`, `Onglets.java`, `PrintTiket.java`, `YvsComDocVentes.java`, `ManagedFactureVente.java`

---

## Flux global d'exécution

```
[FX Thread] valideFrappe()
  │
  ├── selectOnglet.getFacture().setContenus(buildInfoTableToContentFacure(...))
  │     → Construit de NOUVEAUX YvsComContenuDocVente depuis le ContentPanier
  │
  └── saveAndValidateFacture()
        │
        ├── getDocVenteFromOnglet(selectOnglet)
        │     → new YvsComDocVentes(onglet.getFacture())  [shallow copy !]
        │     → génère numDoc   ← AFFECTÉ UNIQUEMENT SUR LA COPIE
        │
        ├── new Thread(() -> confirmValideFacture(facture, ...)).start() [Thread-A]
        │
        ├── printTicketFacture(selectOnglet.getFacture(), ...)  ← ≠ facture !
        │     → Platform.runLater() [P1]
        │
        └── page.closeOngletFacture(selectOnglet)
              → Platform.runLater() [P2]

[Thread-A] confirmValideFacture(facture, ...)
  │── contenuDuPanier = new ArrayList<>(facture.getContenus())
  │── facture.getContenus().clear()
  └── saveFactureAndContent(...)
        └── saveContentFacture(c, doc)
              ├── c.setDocVente(doc)    ← mutation objet PARTAGÉ
              ├── c.setParent(null)     ← mutation objet PARTAGÉ
              └── c.setId(null)        ← mutation objet PARTAGÉ
                  c = dao.save1(c)

[FX Thread - P1] PrintTiket → new Thread(pt).start() [Thread-B]
[FX Thread - P2] Fermeture onglet

[Thread-B] PrintTiket.run() → dataPrint() → Platform.runLater(print)
  └── FOOTER_TICKET_ itère sur facture.getContenus()
        → lit c.getQuantite(), c.getPrix(), c.getRemise()...  ← objets potentiellement mutés par Thread-A
```

---

## BUG #1 — CRITIQUE : Numéro de facture différent entre le ticket et la base de données

### Fichiers concernés
- `ClaviersController.java` lignes ~280–288
- `PrintTiket.java` ligne 116

### Code source

**Dans `getDocVenteFromOnglet` (ClaviersController.java, ~l.280) :**
```java
private YvsComDocVentes getDocVenteFromOnglet(Onglets onglet) {
    YvsComDocVentes docVente = new YvsComDocVentes(onglet.getFacture()); // copie
    String numFacture = UtilsProject.generatedNumDoc(
        (docVente.getTypeDoc().equals(TYPE_FV)) ? Constantes.TYPE_FV_NAME : Constantes.TYPE_BCV_NAME
    );
    docVente.setNumDoc(numFacture);   // ← affecté sur la COPIE seulement
    docVente.setEnteteDoc(UtilsProject.headerDoc);
    return docVente;
}
```

**Dans `saveAndValidateFacture` :**
```java
var facture = getDocVenteFromOnglet(selectOnglet);  // facture.numDoc = VRAI numéro généré
// ...
printTicketFacture(selectOnglet.getFacture(), ...); // ← utilise l'OBJET ORIGINAL
                                                    //   (pas la copie avec le bon numDoc)
```

**Dans `printTicketFacture` :**
```java
pt.setFacture(new YvsComDocVentes(facture)); // facture = selectOnglet.getFacture()
                                              // contient l'ANCIEN numDoc temporaire
```

**Dans `PrintTiket.FOOTER_TICKET_` (ligne 116) :**
```java
Text numDoc = getText("Num: \t " + facture.getNumDoc() + "\n", 9, false); // ← MAUVAIS NUMÉRO
```

### Explication technique

`getDocVenteFromOnglet` génère le vrai numéro de facture (ex : `FV-2025-0001`) et l'affecte sur **`facture`** (la copie) — c'est cet objet qui sera enregistré en base. Mais `printTicketFacture` reçoit **`selectOnglet.getFacture()`** (l'objet original) dont le `numDoc` est resté à sa valeur temporaire (`FV:-99XX` ou le format `TYPE:CLIENT-ID`).

Le ticket imprimé affichera donc un numéro **différent** de celui présent dans la base de données.

### Comment reproduire

1. Lancer l'application sur une session valide.
2. Créer une nouvelle facture (client divers ou autre).
3. Ajouter 1 ou plusieurs articles au panier.
4. Cliquer sur Valider → ouvrir le clavier.
5. Saisir un montant puis valider.
6. Comparer :
   - Le numéro **sur le ticket imprimé** (ou le log PrintTiket)
   - Le numéro **en base** (`SELECT num_doc FROM yvs_com_doc_ventes ORDER BY id DESC LIMIT 1`)
7. ➜ Les deux numéros seront **différents**.

### Correction suggérée

Dans `saveAndValidateFacture`, propager le numDoc généré sur `selectOnglet.getFacture()` avant l'impression :

```java
private void saveAndValidateFacture() {
    var facture = getDocVenteFromOnglet(selectOnglet);
    // Propager le numéro généré sur la facture de l'onglet (utilisée pour l'impression)
    selectOnglet.getFacture().setNumDoc(facture.getNumDoc());

    var statutMontantPaye = isValideMontantPaye(...);
    var statut = ManagedFactureVente.controleBeforeSaveFacture.apply(facture);
    if (OK.equals(statutMontantPaye) && StatutResponse.OK.equals(statut)) {
        new Thread(() -> page.confirmValideFacture(facture, montantAvance, getMontantAffiche())).start();
        printTicketFacture(selectOnglet.getFacture(), selectOnglet.getMontantRecu());
        page.closeOngletFacture(selectOnglet);
    } else {
        processResponseIfError(statut, statutMontantPaye);
    }
}
```

---

## BUG #2 — CRITIQUE : Race condition — mutations d'objets partagés entre thread de sauvegarde et thread d'impression

### Fichiers concernés
- `YvsComDocVentes.java` constructeur de copie, ligne ~285
- `HomeCaisseController.java` `saveContentFacture`, lignes ~885–898
- `PrintTiket.java` `FOOTER_TICKET_`, lignes ~133–151

### Code source

**Constructeur de copie de `YvsComDocVentes` (l. 285) :**
```java
this.contenus = new ArrayList<>(y.contenus); // ← SHALLOW COPY
```
Les éléments `YvsComContenuDocVente` sont des **références partagées** entre la copie (`facture`) et l'original (`selectOnglet.getFacture()`).

**`saveContentFacture` — Thread-A (thread background) :**
```java
private boolean saveContentFacture(YvsComContenuDocVente c, YvsComDocVentes doc) {
    c.setDocVente(doc);     // ← mutation sur objet partagé (visible par Thread-B)
    c.setParent(null);      // ← mutation sur objet partagé
    if (c.getId() <= 0) {
        c.setId(null);      // ← mutation critique sur objet partagé
        c = dao.save1(c);   // si retourne un NOUVEL objet → original reste avec id=null
        // ...
    }
}
```

**`FOOTER_TICKET_` — Thread-B (thread d'impression) :**
```java
for (YvsComContenuDocVente c : facture.getContenus()) { // ← itère sur objets partagés
    tf.getChildren().add(getText(c.getArticle().getDesignation() + " \n", 8, false));
    tf.getChildren().add(getText(
        Constantes.nbf.format(c.getQuantite()) + "\t\t"
        + Constantes.nbf.format(c.getPrix()) + ...
    ));
}
```

### Explication technique

`new YvsComDocVentes(facture)` effectue une **shallow copy** : les `YvsComContenuDocVente` de la liste sont les **mêmes objets** en mémoire entre `facture` et `selectOnglet.getFacture()`. Le thread de sauvegarde (Thread-A) et le thread d'impression (Thread-B) lisent/écrivent **simultanément** sur ces objets sans aucune synchronisation.

Les mutations les plus dangereuses :
- `c.setId(null)` : l'objet partagé se retrouve avec `id = null` pendant que Thread-B peut être en train de le lire.
- Si `dao.save1(c)` retourne un **nouvel objet** (comportement JPA `persist`), l'assignation `c = dao.save1(c)` est **locale** : l'objet original partagé garde `id = null` et `docVente` muté.

### Comment reproduire (intermittent)

1. Utiliser l'application sur une machine lente ou avec une base de données lente (simule le décalage des threads).
2. Créer une facture avec de nombreux articles (≥ 10 lignes) afin d'allonger la durée de `saveContentFacture`.
3. Valider la facture. Dans certaines conditions de timing, le ticket imprimé peut afficher des données corrompues ou tronquées.

> **Note** : Ce bug est **non-déterministe** (race condition) et difficile à reproduire systématiquement. Il est plus fréquent en conditions de charge ou de latence DB.

### Correction suggérée

Imprimer à partir d'une **deep copy** faite AVANT le lancement du thread de sauvegarde, ou après la fin de la sauvegarde (approche recommandée) :

```java
private void saveAndValidateFacture() {
    var facture = getDocVenteFromOnglet(selectOnglet);
    selectOnglet.getFacture().setNumDoc(facture.getNumDoc()); // Fix bug #1

    // Capturer les données du ticket AVANT tout thread concurrent
    final YvsComDocVentes factureForPrint = new YvsComDocVentes(selectOnglet.getFacture());
    final double montantRecuSnapshot = selectOnglet.getMontantRecu();

    var statutMontantPaye = isValideMontantPaye(...);
    var statut = ManagedFactureVente.controleBeforeSaveFacture.apply(facture);
    if (OK.equals(statutMontantPaye) && StatutResponse.OK.equals(statut)) {
        new Thread(() -> {
            page.confirmValideFacture(facture, montantAvance, getMontantAffiche());
            // Imprimer APRÈS la sauvegarde (dans le même thread ou via callback)
            printTicketFacture(factureForPrint, montantRecuSnapshot);
        }).start();
        page.closeOngletFacture(selectOnglet);
    } else {
        processResponseIfError(statut, statutMontantPaye);
    }
}
```

---

## BUG #3 — IMPORTANT : Le ticket est imprimé AVANT la fin de la sauvegarde en base

### Code source

```java
// ClaviersController.java — saveAndValidateFacture()
new Thread(() -> page.confirmValideFacture(facture, ...)).start(); // sauvegarde async
printTicketFacture(selectOnglet.getFacture(), ...);                // impression lancée aussitôt
page.closeOngletFacture(selectOnglet);                             // fermeture aussitôt
```

### Explication technique

Le thread de sauvegarde (`confirmValideFacture`) et l'impression (`printTicketFacture`) sont lancés **quasi-simultanément** sans aucune coordination. Cela signifie que le ticket peut être imprimé avec des données qui n'existent **pas encore** en base de données (par exemple, si la sauvegarde échoue silencieusement après l'impression, un ticket a été remis au client pour une facture non enregistrée).

De plus, `closeOngletFacture` est planifié immédiatement via `Platform.runLater`. Cela ferme l'onglet avant même que l'impression (planifiée via un autre `Platform.runLater` en imbriqué) ait pu accéder à toutes les données de l'onglet.

### Séquençage problématique

```
T+0ms  : Thread-A (sauvegarde) − démarré
T+0ms  : Platform.runLater P1 − planifié (impression)
T+0ms  : Platform.runLater P2 − planifié (fermeture onglet)
T+X ms : FX thread exécute P1 − crée PrintTiket, lance Thread-B
T+X ms : FX thread exécute P2 − ferme et détruit l'onglet
T+Y ms : Thread-B imprime − accède à des données qui peuvent être GC'd ou nulles
T+Z ms : Thread-A finit de sauvegarder (ou échoue)
```

### Comment reproduire

1. Créer une facture.
2. Valider. Observer l'ordre d'impression vs le moment où l'entrée apparaît en base.
3. Forcer un échec de sauvegarde (ex : couper la connexion DB juste avant validation) → le ticket sera imprimé mais la facture n'existera pas en base.

### Correction suggérée

Déclencher l'impression uniquement en cas de succès de la sauvegarde, depuis le callback du thread de sauvegarde :

```java
new Thread(() -> {
    page.confirmValideFacture(facture, montantAvance, getMontantAffiche());
    // La sauvegarde est terminée ici — imprimer maintenant
    printTicketFacture(factureForPrint, montantRecuSnapshot);
}).start();
page.closeOngletFacture(selectOnglet);
```

---

## BUG #4 — IMPORTANT : `netAPayer` incorrect sur le ticket pour les FV avec avance

### Fichiers concernés
- `ClaviersController.java` `printTicketFacture`, ligne ~293
- `PrintTiket.java` `FOOTER_TICKET_`, ligne 154

### Code source

```java
// ClaviersController.java — printTicketFacture()
pt.setNetAPayer(facture.getMontantTotal());  // ← getMontantTotal() pas getMontantResteApayer()
```

**Dans `PrintTiket.FOOTER_TICKET_` (l. 154) :**
```java
Text tNet = getText("Net A payer: \t\t " + Constantes.nbf.format(netAPayer) + "\n", 9, false);
```

### Explication technique

Pour une **facture de vente (FV)** qui a une avance partielle, le montant "net à payer" affiché sur le ticket est `getMontantTotal()` (TTC brut) alors qu'il devrait être `getMontantResteApayer()` = `getMontantTotal() - getMontantAvance()`.

Résultat : le ticket affiche un montant supérieur à ce que le client doit réellement payer.

`getMontantTotal()` dans `YvsComDocVentes` :
```java
public double getMontantTotal() {
    montantTotal = getMontantTTC() + getMontantCS() - getMontantRemises() - getMontantAvoir() + getMontantAvanceAvoir();
    return montantTotal;
}
```
vs `getMontantResteApayer()` :
```java
public double getMontantResteApayer() {
    montantResteApayer = getMontantNetAPayer() - getMontantAvance(); // soustrait l'avance
    return montantResteApayer;
}
```

### Correction suggérée

```java
// Dans printTicketFacture()
pt.setNetAPayer(facture.getMontantResteApayer()); // Utiliser le montant restant à payer
```

---

## BUG #5 — MINEUR : Potentiel `NullPointerException` dans `FOOTER_TICKET_` pour les BCV

### Fichier concerné
- `PrintTiket.java` ligne 124

### Code source

```java
if (facture.getTypeDoc().equals(Constantes.TYPE_BCV)) {
    dateL = getText("Liv. le: \t " + Constantes.dfh.format(facture.getDateLivraisonPrevu()) + "\n", 9, false);
    // ...
}
```

### Explication technique

`facture.getDateLivraisonPrevu()` peut retourner `null` si non renseigné pour un bon de commande (BCV). `Constantes.dfh.format(null)` lancera une `NullPointerException`. Le ticket ne s'imprimera pas (silencieusement dans le thread).

---

## BUG #6 — MINEUR : `contenus` vidé avant d'être sauvegardé en cas d'exception

### Fichier concerné
- `HomeCaisseController.java` `confirmValideFacture`, lignes ~819–820

### Code source

```java
public void confirmValideFacture(YvsComDocVentes facture, ...) {
    List<YvsComContenuDocVente> contenuDuPanier = new ArrayList<>(facture.getContenus());
    facture.getContenus().clear();  // ← clear AVANT la sauvegarde
    YvsComDocVentes entityFacture = saveFactureAndContent(facture, contenuDuPanier, ...);
    if (entityFacture != null) {
        // ...
    } else {
        ToastService.show(..., "Votre facture n'a pas été enregistré ...");
        // Les contenus du panier en mémoire sont définitivement perdus
    }
}
```

### Explication technique

Si `saveFactureAndContent` échoue (exception, DB down), l'objet `facture.contenus` est **déjà vide** (du fait du `clear()`). La copie `contenuDuPanier` est dans le scope local et sera garbage-collectée. Il est impossible de ré-essayer la sauvegarde.

De plus, l'exception en `saveFactureAndContent` est silencieusement swallowed : la méthode retourne `null` sans relancer, et seul un toast apparaît.

---

## Récapitulatif et priorités de correction

| # | Sévérité | Impact | Description |
|---|----------|--------|-------------|
| 1 | 🔴 CRITIQUE | Le numéro sur le ticket ≠ numéro en BDD | `numDoc` généré dans `getDocVenteFromOnglet` non propagé à l'objet d'impression |
| 2 | 🔴 CRITIQUE | Données corrompues de manière intermittente | Race condition : objets `YvsComContenuDocVente` partagés et mutés concurremment |
| 3 | 🟠 IMPORTANT | Ticket imprimé pour une facture non enregistrée | Impression lancée avant la fin de la sauvegarde |
| 4 | 🟠 IMPORTANT | Montant "Net à payer" incorrect sur le ticket (FV avec avance) | `getMontantTotal()` au lieu de `getMontantResteApayer()` |
| 5 | 🟡 MINEUR | NPE silencieuse — ticket BCV jamais imprimé si date de livraison null | `getDateLivraisonPrevu()` non protégé |
| 6 | 🟡 MINEUR | Impossible de ré-essayer après échec de sauvegarde | `facture.getContenus().clear()` avant la sauvegarde |

---

## Plan de correction recommandé

### Étape 1 : Corriger le numDoc (Bug #1)

Dans `saveAndValidateFacture`, ajouter :
```java
selectOnglet.getFacture().setNumDoc(facture.getNumDoc());
```
**juste après** `var facture = getDocVenteFromOnglet(selectOnglet)`.

### Étape 2 : Supprimer la race condition (Bugs #2 et #3)

Réorganiser `saveAndValidateFacture` pour imprimer **après** la sauvegarde :

```java
private void saveAndValidateFacture() {
    var facture = getDocVenteFromOnglet(selectOnglet);
    selectOnglet.getFacture().setNumDoc(facture.getNumDoc()); // Fix #1

    // Snapshot IMMUTABLE des données d'impression, capturé AVANT tout thread concurrent
    final YvsComDocVentes factureSnapshot = new YvsComDocVentes(selectOnglet.getFacture());
    // Deep-copy des contenus pour éviter la mutation concurrente (Fix #2)
    factureSnapshot.setContenus(new ArrayList<>(selectOnglet.getFacture().getContenus().stream()
        .map(c -> new YvsComContenuDocVente(c)) // nécessite un constructeur de copie dans YvsComContenuDocVente
        .collect(Collectors.toList())));
    final double montantRecuSnapshot = selectOnglet.getMontantRecu();

    var statutMontantPaye = isValideMontantPaye(...);
    var statut = ManagedFactureVente.controleBeforeSaveFacture.apply(facture);

    if (OK.equals(statutMontantPaye) && StatutResponse.OK.equals(statut)) {
        new Thread(() -> {
            page.confirmValideFacture(facture, montantAvance, getMontantAffiche()); // sauvegarde
            printTicketFacture(factureSnapshot, montantRecuSnapshot);              // impression APRÈS sauvegarde (Fix #3)
        }).start();
        page.closeOngletFacture(selectOnglet);
    } else {
        processResponseIfError(statut, statutMontantPaye);
    }
}
```

### Étape 3 : Corriger le montant net (Bug #4)

Dans `printTicketFacture`, remplacer :
```java
pt.setNetAPayer(facture.getMontantTotal());
```
par :
```java
pt.setNetAPayer(facture.getMontantResteApayer());
```

### Étape 4 : Protéger la date de livraison (Bug #5)

Dans `PrintTiket.FOOTER_TICKET_` :
```java
if (facture.getTypeDoc().equals(Constantes.TYPE_BCV) && facture.getDateLivraisonPrevu() != null) {
    dateL = getText("Liv. le: \t " + Constantes.dfh.format(facture.getDateLivraisonPrevu()) + "\n", 9, false);
    // ...
}
```

---

*Rapport généré le 24/05/2026 — Analyse statique du code source.*

