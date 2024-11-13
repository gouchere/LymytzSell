package com.lymytz.lymytzsell.service.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessagesConstants {

    public static final String KEY_VALUE = "value";

    public static final String ERREUR = "Erreur";
    public static final String GENERATION_FACTURE_NON_REUSSI = "Génération de la facture non réussi !";
    public static final String ERREUR_A_LA_GENERATION_FACTURE = "Erreur lors de la génération de la facture";
    public static final String MODIFICATION_FACTURE_IMPOSSIBLE = "Modification de la facture impossible !";
    public static final String GENERATION_DE_LA_FACTURE_NON_REUSSI = "Génération de la facture non réussi !";
    public static final String IMPOSSIBE_DE_DEMARRER_L_APPLICATION = "Impossible de démarrer l'application. \n\nAstuce: Ouvrir l'application avec le Login ``ADMIN`` sans mot de passe pour paramétrer vos préférences";
    public static final String FICHIER_PROPERTIE_MAL_CONFIGURE = "Le fichier application.properties est mal configuré";
    public static final String VOUS_DEVEZ_INITIALISER_LA_PROPRIETE = "Vous devez initialiser la propriété ";
    public static final String ECHEC_DE_LEXEECUTION_DE_LA_REQUETE = "Echec de l'execution de la requete ";
    public static final String VEUILLEZ_ENREGISTRER_UN_JOURNAL = "Veuillez enregistrer votre journal de vente";
    public static final String AUCUN_JOURNAL_DE_VENTE_TROUVE = "Aucun journal de vente n'a été trouvé";
}
