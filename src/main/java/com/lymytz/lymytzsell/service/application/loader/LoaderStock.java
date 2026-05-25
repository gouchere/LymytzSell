/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application.loader;

import com.lymytz.lymytzsell.dao.entity.YvsBaseConditionnement;
import com.lymytz.lymytzsell.dao.entity.YvsBaseDepots;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author LYMYTZ
 */
public class LoaderStock extends Task<VBox> {

    HomeCaisseController page;
    List<YvsBaseDepots> depots;
    YvsBaseConditionnement cond;

    public LoaderStock(HomeCaisseController page, List<YvsBaseDepots> depots, YvsBaseConditionnement cond) {
        this.page = page;
        this.depots = depots;
        this.cond = cond;
    }

    @Override
    public VBox call() throws Exception {
        VBox box = new VBox(2);
        HBox lineStock;
        double stock;
        try {
            for (YvsBaseDepots d : depots) {
                lineStock = new HBox(2);
                stock = UtilsProject.getStocks(cond, d.getId());
                if (stock > 0) {
                    Label lstock = new Label(d.getDesignation() + " : ");
                    Label lqte = new Label(Constantes.nbf.format(stock));
                    lineStock.getChildren().addAll(lstock, lqte, new Label("  "), new Label(cond.getUnite().getLibelle()));
                    box.getChildren().addAll(lineStock);
                }
                if (d.equals(UtilsProject.depotLivraison)) {
                    cond.setStock(stock);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(LoaderStock.class.getSimpleName()).log(Level.SEVERE, null, e);
        }
        return box;
    }

}
