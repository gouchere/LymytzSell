package com.lymytz.lymytzsell.business.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.lymytz.lymytzsell.service.utils.Constantes.TYPE_FV;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HelperFactureVente {

    public static EtatMontantPayer isValideMontantPaye(String typeDoc, double netApayer, double montantPaye) {
        if (TYPE_FV.equals(typeDoc)) {
            if (netApayer != montantPaye) {
                // erreur paiement insuffisant
                return EtatMontantPayer.KO_NET_FACTURE;
            }
        } else {
            // contrôle le montant d'avance
            if (montantPaye < netApayer) {
                return EtatMontantPayer.KO_NET_COMMANDE;
            }
        }
        return EtatMontantPayer.OK;
    }

}
