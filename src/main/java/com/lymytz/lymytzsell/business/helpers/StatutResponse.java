package com.lymytz.lymytzsell.business.helpers;

public enum StatutResponse {
    OK,
    TIERS_INNEXISTANT,
    CLIENT_INNEXISTANT,
    NUMERO_DOC_NON_GENERE,
    FICHE_DEJA_CLOTURE,
    ENTETE_FACTURE_NON_TROUVE,
    ENTETE_NON_TROUVE,
    DATE_FICHE_INCORRECT,
    CAISSE_CIBLE_NON_TROUVE,
    MAUVAIS_MONTANT;

    public String getMessage() {
        return switch (this) {
            case FICHE_DEJA_CLOTURE -> "La fiche en cours est déjà clôturé";
            case ENTETE_FACTURE_NON_TROUVE -> "Aucune fiche n'a été trouvé pour cette session";
            case ENTETE_NON_TROUVE -> "La fiche de vente n'a pas pu être chargé";
            case NUMERO_DOC_NON_GENERE -> "";
            case TIERS_INNEXISTANT -> "";
            case CLIENT_INNEXISTANT -> "";
            case DATE_FICHE_INCORRECT -> "";
            case CAISSE_CIBLE_NON_TROUVE -> "Aucune caisse cible du versement n'a été trouvé";
            case MAUVAIS_MONTANT -> "Vous devez entrer un montant supérieur à 0";
            case OK -> "";
        };
    }
}
