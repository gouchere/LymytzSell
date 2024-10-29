package com.lymytz.lymytzsell.business.helpers;

import com.lymytz.lymytzsell.service.utils.LymytzService;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.lymytz.lymytzsell.service.utils.Constantes.TYPE_FV;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HelperFactureVente {

    public static EtatMontantPayer isValideMontantPaye(String typeDoc, double netApayer, double montantPaye) {
        if (TYPE_FV.equals(typeDoc)) {
            if (netApayer != montantPaye) {
                // erreur paiement insuffisant
                //Platform.runLater(() -> LymytzService.openAlertDialog("Incohérence des montants !", "Erreur", "Le montant payé de la facture est différent du TTC !", Alert.AlertType.ERROR));
                return EtatMontantPayer.KO_NET_FACTURE;
            }
        } else {
            // contrôle le montant d'avance
            if (montantPaye > netApayer) {
                //Platform.runLater(() -> LymytzService.openAlertDialog("Incohérence des montants !", "Erreur", "Le montant d'avance de la commande est suppérieure au TTC !", Alert.AlertType.ERROR));
                return EtatMontantPayer.KO_NET_COMMANDE;
            }
        }
        return EtatMontantPayer.OK;
    }

}
