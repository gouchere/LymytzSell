package com.lymytz.lymytzsell.service.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessagesConstants {

    public static final String KEY_VALUE = "value";

    public static final String ERREUR = "Erreur";
    public static final String GENERATION_FACTURE_NON_REUSSI = "Génération de la facture non réussi !";
    public static final String MODIFICATION_FACTURE_IMPOSSIBLE = "Modification de la facture impossible !";
    public static final String IMPOSSIBE_DE_DEMARRER_L_APPLICATION = "Impossible de démarrer l'application. \n\nAstuce: Ouvrir l'application avec le Login ``ADMIN`` sans mot de passe pour paramétrer vos préférences";
    public static final String FICHIER_PROPERTIE_MAL_CONFIGURE = "Le fichier application.properties est mal configuré";
    public static final String VOUS_DEVEZ_INITIALISER_LA_PROPRIETE = "Vous devez initialiser la propriété ";
}
