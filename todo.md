# Liste d'améliorations

- [x] **Examiner le code de l'application pour tenter de reproduire le problème de duplication de contenu**
- [ ] plutôt que d'avoir une modal qui dit aucune facture n'a été trouvé au moment de l'ajout d'un article au panier,
  proposer un enregistrement implicite
- [ ] Pour la société ACROPOL, Ne pas faire effectuer de contôrole de stock
- [x] Revoir l'affichage de la liste des article selon les modes choisi
    - Nombre de colonnes (2 ou 3)
    - Nombre de ligne par page
    - type d'affichage
- [ ] La vue Préférence a besoin d'être retouché
- [ ] La vue d'import peut être visible même si on est pas en mode synchro
- [ ] Construire un installeur
- [ ] Afficher des notifications pour les factures non livré
- [x] Cacher le loader après le chargement des articles
- [ ] Revoir le temps d'enregistrement d'un article dans le panier
- [ ] Modifier le logger pour utiliser le RollingFile four    ni par apache logging
- [ ] Virer du code l'utilisation des package com.sun, pour régler le pb d'introspection
- [ ] Gérer la caisse de manière autonome sans dépendre de l'ERP, sauf pour la récupération des données de bases
- [ ] Configurer les logs dans un fichier
- [ ] A l'initialisation, filtrer les sociétés et les agences en foncion d'un identifiant fourni.

# Approche optimiste de l'enregistrement des factures:

Il s'agit de lancer l'impression du ticket en parallèle de l'enregistrement de la facture
Si l'enregistrement de la facture échoue, il faut être capable de la reprendre.
