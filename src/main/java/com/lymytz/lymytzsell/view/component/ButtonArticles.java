/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.view.component;

import com.lymytz.lymytzsell.dao.entity.YvsBaseConditionnement;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;
import javafx.application.Platform;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * @author LYMYTZ
 */
public class ButtonArticles extends VBox {

    @Setter
    @Getter
    private YvsBaseConditionnement conditionnement;
    private final HomeCaisseController page;

    public ButtonArticles(YvsBaseConditionnement conditionnement, HomeCaisseController home) {
        super();
        this.conditionnement = conditionnement;
        this.page = home;
        this.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                Onglets tab = (Onglets) page.TAB_FACTURES.getSelectionModel().getSelectedItem();
                if (tab != null) {
                    tab.addArticleOnFacture(conditionnement, 1, false, conditionnement.getPrix());
                } else {
                    this.page.initFactureVenteClientDivers();
                    Platform.runLater(() -> {
                        var newTab = (Onglets) page.TAB_FACTURES.getSelectionModel().getSelectedItem();
                        Optional.ofNullable(newTab).ifPresent(t -> t.addArticleOnFacture(conditionnement, 1, false, conditionnement.getPrix()));
                    });
                }
            } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                page.displayPropertyArticle(conditionnement, true);
            }
        });

    }

}
