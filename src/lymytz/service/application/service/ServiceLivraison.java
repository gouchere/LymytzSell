/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.service;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import lymytz.dao.Options;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.service.application.synchro.export.UtilExport;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import lymytz.synchro.ws.ResultatAction;
import lymytz.synchro.ws.WsSynchro;
import lymytz.view.main.HomeCaisseController;
import org.json.JSONObject;

/**
 *
 * @author LENOVO
 */
public class ServiceLivraison {

    HomeCaisseController mainPage;

    public ServiceLivraison() {
    }

    public ServiceLivraison(HomeCaisseController mainPage) {
        this.mainPage = mainPage;
    }

    public boolean saveLivraison(YvsComDocVentes facture, boolean message) {
        WsSynchro ws = new WsSynchro();
        //Construction de l'objet avec ses liaisons sur le serveur distant
        JSONObject entityJson = UtilExport.exportDocVente(facture, false, 0L);
        ResultatAction<YvsComDocVentes> result = ws.livraisonDocVente(entityJson, "livrer_facture_vente_caisse");
        if (result.isResult()) {
            //met à jour le statut livré de la facture
            String query = "UPDATE yvs_com_doc_ventes SET statut_livre='L' WHERE id=? ";
            mainPage.dao.executeSqlQuery(query, new Options[]{new Options(facture.getId(), 1)});
            if (message) {
                Platform.runLater(() -> {
                    LymytzService.success();
                });
            }
        } else {
            Platform.runLater(() -> {
                LymytzService.openAlertDialog("", "", result.getMessage(), Alert.AlertType.ERROR);
            });
        }
        return result != null ? result.isResult() : false;
    }

}
