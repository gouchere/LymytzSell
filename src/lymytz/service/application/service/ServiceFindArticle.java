///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package lymytz.service.application.service;
//
//import java.util.ArrayList;
//import java.util.List;
//import javafx.collections.ObservableList;
//import javafx.concurrent.WorkerStateEvent;
//import javafx.event.Event;
//import javafx.event.EventHandler;
//import javafx.scene.layout.GridPane;
//import lymytz.dao.entity.YvsBaseArticles;
//import lymytz.dao.entity.YvsBaseConditionnement;
//import lymytz.dao.query.LQueryFactories;
//import lymytz.service.application.composant.ButtonArticles;
//import lymytz.service.application.loader.LoaderConditionnement;
//import lymytz.service.utils.Constantes;
//import lymytz.view.main.HomeCaisseController;
//
///**
// *
// * @author LYMYTZ
// */
//public class ServiceFindAricle implements Runnable {
//
//    LQueryFactories dao = new LQueryFactories();
//    private HomeCaisseController page;
//
//    public ServiceFindAricle(HomeCaisseController page) {
//        this.page = page;
//    }
//
//    public void displayListArticlesInPoint(List<YvsBaseConditionnement> l) {
//        ButtonArticles gp;
//        page.BOX_ARTICLES.getChildren().clear();
//        for (YvsBaseConditionnement fa : l) {
//            gp = new ButtonArticles(fa, page);
//            gp.setPrefSize(60, 20);
////            gp.getIMG().setFitHeight(100);
////            gp.getIMG().setFitWidth(100);
////            gp.getIMG().setPreserveRatio(true);
//            page.BOX_ARTICLES.getChildren().add(gp);
//        }
//
//    }
//
//    public void displayPropertyArticle(String refArticle) {
//        if (refArticle != null ? !refArticle.trim().isEmpty() : false) {
//            if (page.getIdDepots() == null) {
//                page.setIdDepots(new ArrayList<>());
//            }
//            if (page.getIdDepots().isEmpty()) {
//                page.getIdDepots().add(-1L);
//            }
//            List<YvsBaseConditionnement> articles ;
//            LoaderConditionnement service=new LoaderConditionnement(page, refArticle);
//            service.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
//
//                @Override
//                public void handle(WorkerStateEvent event) {
//                    ObservableList<GridPane> value = tache.getValue();
//                BOX_ARTICLES.getChildren().addAll(value);
//                PROGRESS_LABEL.textProperty().unbind();
//                PROGRESS_LABEL.setText("terminé !");
//                }
//            });
//            displayListArticlesInPoint(articles);
//           
//            if (articles.size() == 1) {
//                displayPropertyArticle(articles.get(0), false);
//            }
//        }
//    }
//
////    public void displayPropertyArticleById(Long idArticle) {
////        if (idArticle != null ? idArticle > 0 : false) {
////            YvsBaseArticles articles = dao.findOneByNQ("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{idArticle});
////            displayListArticlesInPoint(articles);
////            if (articles.size() == 1) {
////                displayPropertyArticle(articles.get(0), true);
////            }
////        }
////    }
//
//    public void displayPropertyArticle(YvsBaseConditionnement art, boolean allppte) {
//        if (art != null) {
//            page.LAB_REF.setText(art.getArticle().getRefArt());
//            page.LAB_DES.setText(art.getArticle().getDesignation());
////            page.LAB_DES2.setText(getRefFournisseur(art));
//            page.LAB_FAM.setText(art.getArticle().getFamille().getDesignation());
////            page.LAB_FABRICANT.setText(art.getArticle().getFabriquant() != null ? art.getFabriquant().getNom_prenom() : "");
//            page.LAB_GROUP.setText(art.getArticle().getGroupe() != null ? art.getArticle().getGroupe().getDesignation() : "");
//            page.LAB_PUV.setText(Constantes.nbf.format(art.getPrix()));
//            page.LAB_REM.setText(Constantes.nbf.format(art.getRemise()));
//            page.LAB_PUV_TTC.setText(art.getArticle().getPuvTtc() ? "Oui" : "Non");
//            page.LAB_PUV_V.setText(art.getArticle().getChangePrix() ? "Oui" : "Non");
////            ZONE_IMG.getChildren().clear();
////            createImageProduit(art);
////            ZONE_IMG.getChildren().add(pagination);
////            List<YvsBaseDepots> depots = null;
////            if (allppte) {
////                depots = rq.loadNameQueries("YvsBaseArticleDepot.findDepotActifByArt", new String[]{"article"}, new Object[]{art});
////                depots.remove(UtilsProject.depotLivraison);
////                depots.add(0, UtilsProject.depotLivraison);
////            } else {
////                depots = new ArrayList<>();
////                depots.add(UtilsProject.depotLivraison);
////            }
////            PAN_STOCK.getChildren().clear();
////            VBox lineStock;
////            HBox lineStockC;
////            double stock;
////            Label lstock, lqte, lcond;
////            if (UtilsProject.headerDoc != null ? UtilsProject.headerDoc.getCreneau() != null : false) {
////                for (YvsBaseDepots d : depots) {
////                    lstock = new Label(d.getDesignation());
////                    lstock.getStyleClass().add("label_info_stock1");
////                    lstock.setPrefWidth(800);
////                    lineStock = new VBox(3, lstock);
////                    for (YvsBaseConditionnement c : art.getConditionnements()) {
////                        stock = getStocks(c, d.getId());
////                        lqte = new Label(Constantes.nbF.format(stock));
////                        lcond = new Label(c.getUnite().getLibelle());
////                        lcond.getStyleClass().add("label_info_cond");
////                        lqte.getStyleClass().add("label_info_cond");
////                        lineStockC = new HBox(10);
////                        lineStockC.setPadding(new Insets(0, 0, 0, 15));
////                        lineStockC.getChildren().addAll(lqte, lcond);
////                        lineStock.getChildren().add(lineStockC);
////                        if (d.equals(UtilsProject.depotLivraison)) {
////                            c.setStock(stock);
////                            if (c.equals(currentCond)) {
////                                currentCond.setStock(stock);
////                            }
////                        }
////                    }
////                    PAN_STOCK.getChildren().add(lineStock);
////                }
////            }
////                //Afficher la description de l'article
////            }
//        }
//    }
//
//    @Override
//    public void run() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//}
