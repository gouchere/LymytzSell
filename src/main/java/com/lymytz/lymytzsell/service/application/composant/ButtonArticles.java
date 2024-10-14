/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application.composant;

import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import com.lymytz.lymytzsell.dao.entity.YvsBaseConditionnement;
import com.lymytz.lymytzsell.service.utils.LymytzService;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;

/**
 *
 * @author LYMYTZ
 */
public class ButtonArticles extends VBox {

    private YvsBaseConditionnement conditionnement;
    private HomeCaisseController page;
    private ButtonArticles current;

    public ButtonArticles(YvsBaseConditionnement conditionnement, HomeCaisseController home) {
        super();
        current = this;
        this.conditionnement = conditionnement;
        this.page = home;
        this.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                Onglets tab = (Onglets) page.TAB_FACTURES.getSelectionModel().getSelectedItem();
                if (tab != null) {
                    current.getStylesheets().add("");
                    tab.addArticleOnFacture(conditionnement, 1, false, conditionnement.getPrix());
                } else {
                    LymytzService.openAlertDialog("Aucune facture n'a été trouvé !", "Erreur", "Vous devez enregistrer la facture !", Alert.AlertType.ERROR);
                }
            } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                page.displayPropertyArticle(conditionnement, true);
            }
        });

    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

}
