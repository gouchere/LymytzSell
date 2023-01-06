/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.loader;

import java.util.List;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lymytz.dao.entity.YvsBaseConditionnement;
import lymytz.dao.entity.YvsBaseDepots;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.UtilsProject;
import lymytz.view.main.HomeCaisseController;

/**
 *
 * @author LYMYTZ
 */
public class LoaderStock extends Task<VBox> {

    LQueryFactories Ldao = new LQueryFactories();
    HomeCaisseController page;
    String reference;
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
        HBox lineStock ;
        double stock;
//        Label lstock, lqte;
//        boolean verifiStock;
//        if (!UtilsProject.REPLICATION) {
//            verifiStock = true;
//        } else {
//            if (page.getConnectRemoteServer() != null ? page.getConnectRemoteServer() : false) {
//                verifiStock = true;
//            } else {
//                verifiStock = false;
//                Label lstock = new Label("Impossible d'afficher le stock de cet article car le serveur distant est inaccessible !");
//                lstock.setWrapText(true);
//                lstock.setStyle("-fx-text-fill: red; -fx-font-size:0.9em; ");
//                box.getChildren().addAll(lstock);
//            }
//        }
//        if (verifiStock) {
            try {
                for (YvsBaseDepots d : depots) {
                    lineStock = new HBox(2);
                    Label lstock = new Label(d.getDesignation() + " : ");
                    stock = UtilsProject.getStocks(cond, d.getId());
                    Label lqte = new Label(Constantes.nbf.format(stock));
                    lineStock.getChildren().addAll(lstock, lqte, new Label("  "), new Label(cond.getUnite().getLibelle() + " en stock"));
                    box.getChildren().addAll(lineStock);
                    if (d.equals(UtilsProject.depotLivraison)) {
                        cond.setStock(stock);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
        return box;
    }

}
