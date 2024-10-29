# Liste d'améliorations
- [ ] **Examiner le code de l'application pour tenter de reproduire le problème de duplication de contenu**
- [ ] plutôt que d'avoir une modal qui dis aucune facture n'a été trouvé au moment de l'ajout d'un article au panier, proposer un enregistrement implicite
- [ ] Pour la société ACROPOL, Ne pas faire effectuer de contôrole de stock
- [ ] Revoir l'affichage de la liste des article selon les modes choisi
  - Nombre de colonnes (2 ou 3)
  - Nombre de ligne par page
  - type d'affichage
- [ ] La vue Préférence a besoin d'être retouché
- [ ] La vue d'import peut être visible même si on est pas en mode synchro
- [ ] Construire un installeur

# Approche optimiste de l'enregistrement des factures:
Il s'agit de lancer l'impression du ticket en parallèle de l'enregistrement de la facture
Si l'enregistrement de la facture échoue, il faut être capable de la reprendre.
