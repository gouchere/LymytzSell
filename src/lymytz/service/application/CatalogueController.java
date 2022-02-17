///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package lymytz.service.application;
//
//import java.net.URL;
//import java.util.List;
//import java.util.ResourceBundle;
//import javafx.application.Platform;
//import javafx.concurrent.Task;
//import javafx.concurrent.WorkerStateEvent;
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.geometry.Pos;
//import javafx.scene.Node;
//import javafx.scene.control.Label;
//import javafx.scene.control.Pagination;
//import javafx.scene.control.ProgressIndicator;
//import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.util.Callback;
//import lymytz.service.utils.UtilsProject;
//import static lymytz.service.utils.UtilsProject.getStocks;
//import lymytz.view.LocalLoader;
//import yvs.dao.local.Requete;
//import yvs.dao.salaire.service.Constantes;
//import yvs.entity.base.YvsBaseArticleDepot;
//import yvs.entity.base.YvsBaseConditionnement;
//import yvs.entity.produits.YvsBaseArticles;
//
///**
// * FXML Controller class
// *
// * @author LENOVO
// */
//public class CatalogueController implements Initializable {
//
////    private List<YvsBaseArticles> items;
//    private String action = "ALL";
//
//    Requete rq = new Requete();
//
//    @FXML
//    private VBox PAN_ART;
//    @FXML
//    private TextField TXT_REF;
//    @FXML
//    private TextField TXT_FAM;
//    @FXML
//    private VBox PAN_COND;
//    @FXML
//    private VBox PAN_STOCK;
//    @FXML
//    private VBox PAN_FSEUR;
////    @FXML
////    private ProgressBar PROGRESS;
//    @FXML
//    private VBox ZONE_LEFT;
//    @FXML
//    private VBox ZONE_BOTTOM;
//    @FXML
//    private VBox ZONE_CENTER;
//    @FXML
//    private ProgressIndicator INDICATOR;
//    @FXML
//    private Pagination PAGINATOR;
//
//    /**
//     * Initializes the controller class.
//     *
//     * @param url
//     * @param rb
//     */
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
////        loadDataArticles();
//        PAGINATOR.setPageFactory(new Callback<Integer, Node>() {
//
//            @Override
//            public Node call(Integer param) {
//                System.err.println(" begin crete page...");
//                return createPage(param);
//            }
//        });        
//    }
//
//    private void loadDataArticles() {
//        TacheFind task = new TacheFind("ALL", 0, true);
//        INDICATOR.progressProperty().unbind();
//        INDICATOR.progressProperty().bind(task.progressProperty());
//        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent event) {
////                stop = true;
//                VBox re = task.getValue();
//                Platform.runLater(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        PAN_ART.getChildren().add(re);
//                        ZONE_BOTTOM.setVisible(true);
//                        ZONE_LEFT.setVisible(true);
//                        ZONE_CENTER.setVisible(true);
//                        INDICATOR.setVisible(false);
//
//                    }
//                });
//            }
//        });
//        Thread t = new Thread(task);
//        t.start();
//    }
//
//    public HBox buildZoneValeur(YvsBaseArticles art) {
//        HBox box = new HBox(5);
//        Label lprix = new Label("Prix :"), lremise = new Label("Remise:"), lprixMin = new Label("Prix Min. :");
//        Label prix, remise, prixMin;
//        prix = new Label(Constantes.nbF.format(art.getPuv()));
//        prixMin = new Label(Constantes.nbF.format(art.getPrixMin()));
//        remise = new Label(Constantes.nbF.format(art.getRemise()));
//        prix.getStyleClass().add("label_info");
//        remise.getStyleClass().add("label_info");
//        prixMin.getStyleClass().add("label_info");
//        prix.setPrefWidth(100);
//        prixMin.setPrefWidth(100);
//        remise.setPrefWidth(100);
//        box.getChildren().addAll(lprix, prix, lremise, remise, lprixMin, prixMin);
//        box.getStyleClass().add("border");
//        box.setAlignment(Pos.CENTER);
//        return box;
//    }
//
//    public VBox buildZoneRef(YvsBaseArticles art) {
//        VBox box = new VBox(5);
//        Label lref = new Label("Référence:"), ldesignation = new Label("Désignation:"), lcategorie = new Label("Catégorie:"), lfamille = new Label("Famille:"),
//                lgroupe = new Label("Groupe:"), lclasse1 = new Label("Classe 1:"), lclasse2 = new Label("Classe 2:");
//        Label ref, designation, categorie, famille, groupe, classe1, classe2;
//        ref = new Label(art.getRefArt());
//        designation = new Label(art.getDesignation());
//        categorie = new Label(art.getCategorie());
//        famille = new Label(art.getFamille().getDesignation());
//        groupe = new Label(art.getGroupe() != null ? art.getGroupe().getDesignation() : "");
//        classe1 = new Label((art.getClasse1() != null) ? art.getClasse1().getDesignation() : "");
//        classe2 = new Label((art.getClasse2() != null) ? art.getClasse2().getDesignation() : "");
//        ref.getStyleClass().add("label_info");
//        designation.getStyleClass().add("label_info");
//        categorie.getStyleClass().add("label_info");
//        famille.getStyleClass().add("label_info");
//        groupe.getStyleClass().add("label_info");
//        classe1.getStyleClass().add("label_info");
//        classe2.getStyleClass().add("label_info");
//        box.getChildren().add(new HBox(lref, ref));
//        box.getChildren().add(new HBox(ldesignation, designation));
//        box.getChildren().add(new HBox(lcategorie, categorie));
//        box.getChildren().add(new HBox(lfamille, famille));
//        box.getChildren().add(new HBox(lgroupe, groupe));
//        box.getChildren().add(new HBox(lclasse1, classe1));
//        box.getChildren().add(new HBox(lclasse2, classe2));
//        return box;
//    }
//
//    public void displayPropertiesArticles(YvsBaseArticles art) {
//        PAN_COND.getChildren().clear();
//        PAN_STOCK.getChildren().clear();
//        PAN_FSEUR.getChildren().clear();
//        if (art != null) {
//            // display cond
//            for (YvsBaseConditionnement cond : art.getConditionnements()) {
//                PAN_COND.getChildren().add(buildDataCond(cond));
//            }
//            //display stocks
//            for (YvsBaseArticleDepot artd : art.getYvsBaseArticleDepotList()) {
//                PAN_STOCK.getChildren().add(buildDataStocks(artd));
//            }
//
//        }
//    }
//
//    public VBox buildDataCond(YvsBaseConditionnement art) {
//        VBox box = new VBox();
//        HBox b1 = new HBox(new Label(art.getUnite().getLibelle() + " [" + art.getUnite().getReference() + "]"));
//        HBox b2 = new HBox();
//        b1.getStyleClass().add("line_bar2");
//        b2.getStyleClass().add("line_bar3");
//        Label lprix = new Label("Prix :"), lremise = new Label("Remise:"), lprixMin = new Label("Prix Min. :");
//        Label prix, remise, prixMin;
//        prix = new Label(Constantes.nbF.format(art.getPrix()));
//        prixMin = new Label(Constantes.nbF.format(art.getPrixMin()));
//        remise = new Label(Constantes.nbF.format(art.getRemise()));
//        prix.getStyleClass().add("label_info");
//        remise.getStyleClass().add("label_info");
//        prixMin.getStyleClass().add("label_info");
//        prix.setPrefWidth(100);
//        prixMin.setPrefWidth(100);
//        remise.setPrefWidth(100);
//        b2.getChildren().addAll(lprix, prix, lremise, remise, lprixMin, prixMin);
//        box.getChildren().addAll(b1, b2);
//        box.setAlignment(Pos.CENTER);
//        return box;
//    }
//
//    public VBox buildDataStocks(YvsBaseArticleDepot art) {
//        VBox box = new VBox();
//        HBox b1 = new HBox(new Label(art.getDepot().getDesignation()));
//        b1.getStyleClass().add("line_bar2");
//        HBox b2 = new HBox();
//        b2.getStyleClass().add("line_bar3");
//        Label labCond, labStock;
//        for (YvsBaseConditionnement cond : art.getArticle().getConditionnements()) {
//            labStock = new Label(Constantes.nbF.format(getStocks(cond, art.getDepot().getId())));
//            labCond = new Label(cond.getUnite().getLibelle());
//            labStock.setPrefWidth(100d);
//            b2.getChildren().addAll(labStock, labCond);
//        }
//        box.getChildren().addAll(b1, b2);
//        box.setAlignment(Pos.CENTER);
//        return box;
//    }
//
//    @FXML
//    private void findByReference(KeyEvent event) {
//        TacheFind task = new TacheFind("ARTICLE", 0, true);
//        INDICATOR.setVisible(true);
//        INDICATOR.progressProperty().unbind();
//        INDICATOR.progressProperty().bind(task.progressProperty());
//        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent event) {
////                stop = true;
//                VBox re = task.getValue();
//                Platform.runLater(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        PAN_ART.getChildren().clear();
//                        PAN_ART.getChildren().add(re);
//                        INDICATOR.setVisible(false);
////                        ZONE_LEFT.setVisible(true);
////                        ZONE_CENTER.setVisible(true);
////                        PROGRESS.setPrefHeight(0d);
////                        PROGRESS.setVisible(false);
//
//                    }
//                });
//            }
//        });
//        Thread t = new Thread(task);
//        t.start();
//    }
//
//    @FXML
//    private void findByFamille(KeyEvent event) {
//        TacheFind task = new TacheFind("FAMILLE", 0, true);
//        INDICATOR.setVisible(true);
//        INDICATOR.progressProperty().unbind();
//        INDICATOR.progressProperty().bind(task.progressProperty());
//        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent event) {
////                stop = true;
//                VBox re = task.getValue();
//                Platform.runLater(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        PAN_ART.getChildren().clear();
//                        PAN_ART.getChildren().add(re);
//                        INDICATOR.setVisible(false);
//
//                    }
//                });
//            }
//        });
//        Thread t = new Thread(task);
//        t.start();
//    }
//
//    private long countNumberArt(String query) {
//        Long re;
//        switch (query) {
//            case "ARTICLE":
//                if (TXT_REF.getText() != null ? !TXT_REF.getText().trim().isEmpty() : false) {
//                    re = (Long) rq.loadObjectByNameQueries("YvsBaseArticles.countByCodeLActif", new String[]{"societe", "code"}, new Object[]{UtilsProject.currentSociete, "%" + TXT_REF.getText() + "%"});
//                } else {
//                    re = (Long) rq.loadObjectByNameQueries("YvsBaseArticles.findCountAll", new String[]{"societe"}, new Object[]{UtilsProject.currentSociete});
//                }
//                break;
//            case "FAMILLE":
//                if (TXT_FAM.getText() != null ? !TXT_FAM.getText().trim().isEmpty() : false) {
//                    re = (Long) rq.loadObjectByNameQueries("YvsBaseArticles.countyCodeFamilleActif", new String[]{"societe", "code"}, new Object[]{UtilsProject.currentSociete, "%" + TXT_FAM.getText() + "%"});
//                } else {
//                    re = (Long) rq.loadObjectByNameQueries("YvsBaseArticles.findCountAll", new String[]{"societe"}, new Object[]{UtilsProject.currentSociete});
//                }
//                break;
//            default:
//                re = (Long) rq.loadObjectByNameQueries("YvsBaseArticles.findCountAll", new String[]{"societe"}, new Object[]{UtilsProject.currentSociete});
//                break;
//        }
//        return re;
//    }
//
//    public class TacheFind extends Task<VBox> {
//
//        List<YvsBaseArticles> items;
//
//        public TacheFind(String query, int beginIndex, boolean calculNbPage) {
//            action = query;
//            switch (query) {
//                case "ARTICLE":
//                    if (TXT_REF.getText() != null ? !TXT_REF.getText().trim().isEmpty() : false) {
//                        items = rq.loadNameQueries("YvsBaseArticles.findByCodeLActif", new String[]{"societe", "code"}, new Object[]{UtilsProject.currentSociete, "%" + TXT_REF.getText() + "%"}, beginIndex * 50, 50);
//                        System.err.println(" -- -- résultat de la  requête "+items.size()); 
//                    } else {
//                        items = (rq.loadNameQueries("YvsBaseArticles.findAll", new String[]{"societe"}, new Object[]{UtilsProject.currentSociete}));
//                        System.err.println(" -- -- résultat de la  requête "+items.size());
//                    }
//                    break;
//                case "FAMILLE":
//                    if (TXT_FAM.getText() != null ? !TXT_FAM.getText().trim().isEmpty() : false) {
//                        items = rq.loadNameQueries("YvsBaseArticles.findByCodeFamilleActif", new String[]{"societe", "code"}, new Object[]{UtilsProject.currentSociete, "%" + TXT_FAM.getText() + "%"}, beginIndex * 50, 50);
//                    } else {
//                        items = (rq.loadNameQueries("YvsBaseArticles.findAll", new String[]{"societe"}, new Object[]{UtilsProject.currentSociete}, beginIndex * 50, 50));
//                    }
//                    break;
//                default:
//                    items = (rq.loadNameQueries("YvsBaseArticles.findAll", new String[]{"societe"}, new Object[]{UtilsProject.currentSociete}, beginIndex * 50, 50));
//                     System.err.println(" -- -- résultat de la  requête ALL "+items.size());
//                    break;
//            }
//            if (calculNbPage) {
//                PAGINATOR.setCurrentPageIndex(beginIndex);
//                PAGINATOR.setPageCount((int) countNumberArt(query) / 50);
//            }
//        }
//
//        @Override
//        protected VBox call() throws Exception {
//            ImageView image;
//            VBox result = new VBox();
//            HBox hNode;
//            HBox bottom;
//            VBox node;
//            AnchorPane right;
//            int line = 0;
//            int numero = 0;
//            for (YvsBaseArticles art : items) {
//                right = new AnchorPane();
//                hNode = new HBox(5);
//                bottom = buildZoneValeur(art);
//                node = buildZoneRef(art);
//                image = new ImageView(new Image(LocalLoader.class.getResource("icones/produits.png").toExternalForm()));
//                image.setPreserveRatio(true);
//                image.setFitHeight(150);
//                image.setFitWidth(150);
//                right.setPrefWidth(500);
//                bottom.setPrefWidth(500);
//                bottom.setPrefHeight(35);
//                right.getChildren().addAll(node, bottom);
//                AnchorPane.setTopAnchor(node, 1d);
//                AnchorPane.setBottomAnchor(bottom, 1d);
//                hNode.getChildren().addAll(image, right);
//                if (UtilsProject.isNumbrePair(line)) {
//                    hNode.getStyleClass().add("line_catalogue_pair");
//                } else {
//                    hNode.getStyleClass().add("line_catalogue_impair");
//                }
//                node.setStyle("-fx-width:100%;");
//                hNode.setStyle("-fx-width:100%;");
//                hNode.setPrefHeight(180);
//                result.getChildren().add(hNode);
//                line++;
//                hNode.setOnMouseClicked(new EventHandler<MouseEvent>() {
//
//                    @Override
//                    public void handle(MouseEvent event) {
//                        displayPropertiesArticles(art);
//                    }
//                });
//                numero++;
//                this.updateProgress(numero, items.size());
//            }
//            return result;
//        }
//    }
//
//    public Node createPage(int pageIndex) {
//        TacheFind task = new TacheFind(action, pageIndex, false);
//        PAN_ART.getChildren().clear();
//        INDICATOR.setVisible(true);
//        INDICATOR.progressProperty().unbind();
//        INDICATOR.progressProperty().bind(task.progressProperty());
//        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent event) {
////                stop = true;
//                VBox re = task.getValue();
//                Platform.runLater(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        PAN_ART.getChildren().add(re);
//                        INDICATOR.setVisible(false);
//
//                    }
//                });
//            }
//        });
//        Thread t = new Thread(task);
//        t.start();
//        return null;
//    }
//}
