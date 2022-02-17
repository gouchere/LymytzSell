///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package lymytz.view.component;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.geometry.Pos;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import lymytz.service.application.ApplicationController;
//import lymytz.service.utils.UtilsProject;
//import lymytz.view.LocalLoader;
//import yvs.dao.salaire.service.Constantes;
//import yvs.entity.base.YvsBaseConditionnement;
//import yvs.entity.produits.YvsBaseArticles;
//
///**
// * FXML Controller class
// *
// * @author LENOVO
// */
//public class ButtonArticles extends VBox {
//
//    ApplicationController source;
//    @FXML
//    private ImageView IMG;
//    @FXML
//    private Label PRIX;
//    @FXML
//    private Label REFART;
//    @FXML
//    private Label DESART;
//    private Label COND;
//    private ImageView BUT_PPTE = new ImageView();
//    private Button BUT_ADD = new Button();
//    VBox boxButton;
//    FXMLLoader fxmlLoader;
//    Image imge;
//
//    public ButtonArticles() {
//    }
//
//    public ImageView getIMG() {
//        return IMG;
//    }
//
//    public void setIMG(ImageView IMG) {
//        this.IMG = IMG;
//    }
//
//    public Label getPRIX() {
//        return PRIX;
//    }
//
//    public void setPRIX(Label PRIX) {
//        this.PRIX = PRIX;
//    }
//
//    public Label getREFART() {
//        return REFART;
//    }
//
//    public void setREFART(Label REFART) {
//        this.REFART = REFART;
//    }
//
//    public Label getDESART() {
//        return DESART;
//    }
//
//    public void setDESART(Label DESART) {
//        this.DESART = DESART;
//    }
//
//    AnchorPane ligne;
//    HBox art;
//    HBox boxConds;
//
//    public ButtonArticles(YvsBaseArticles article) {
//        this.getStyleClass().add("line_bar");
//        decorateButton();
//        initEvent(article);
////        IMG = new ImageView();
////        imge = displayPhotos(article.getPhoto());
////        if (imge != null) {
////            IMG.setImage(imge);
////        }
//        REFART = new Label(article.getRefArt());
//        DESART = new Label(article.getDesignation());
//        REFART.setStyle("-fx-font-size:1.2em;-fx-font-weight: bold;");
//        DESART.setStyle("-fx-font-size:0.9em;");
////        COND = new Label();
////        PRIX = new Label();
////        if (article.getConditionnements() != null ? article.getConditionnements().size() == 1 : false) {
//////            COND.setText("[" + article.getConditionnements().get(0).getUnite().getLibelle() + "]");
//////            PRIX.setText(Constantes.nbF.format(article.getConditionnements().get(0).getPrix()));
//////            COND.setStyle("-fx-font-size:0.9em;-fx-font-weight: bold;");
////        } else if (article.getConditionnements() != null ? article.getConditionnements().size() == 0 : false) {
//////            PRIX.setText(Constantes.nbF.format(conditionnements.get(0).getPrix()));
////        }
//        boxButton = new VBox(0, BUT_PPTE);
//        ligne = new AnchorPane();
//        art = new HBox(new VBox(REFART, DESART));
//        ligne.getChildren().add(art);
//        ligne.getChildren().add(boxButton);
//        AnchorPane.setRightAnchor(boxButton, 0d);
//        this.getChildren().add(ligne);
//        if (article.getConditionnements() != null) {
//            if (article.getConditionnements().size() > 0) {
//                 boxConds = new HBox(7);
//                VBox box;
//                Label des, price;
////                ImageView imCond;
//                String prix;
//                BUT_ADD.setVisible(false); //s'il y a plusieurs cond. l'ajout au panier deviens ambigu
//                for (YvsBaseConditionnement c : article.getConditionnements()) {
//                    box = new VBox(3);
//                    box.setAlignment(Pos.CENTER);
//                    prix = Constantes.nbF.format(c.getPrix());
//                    price = new Label("[" + prix + " Fcfa]");
//                    price.setStyle("-fx-font-weight: bold;");
//                    box.getChildren().add(price);
//                    des = new Label(c.getUnite().getLibelle());
//                    des.setWrapText(true);
//                    box.getChildren().add(des);
//                    box.setPrefSize(200, 300);
////                    cond.setGraphic(imCond);
//                    box.getStyleClass().add("background_art");
//                    box.setOnMouseClicked(new EventHandler<MouseEvent>() {
//
//                        @Override
//                        public void handle(MouseEvent event) {
//                            source.addArticleOnFacture(c, 1, false, 0);
//                        }
//                    });
//                    boxConds.getChildren().add(box);
//                }
//                this.getChildren().add(boxConds);
//            }
//        }
//    }
//    private void initEvent(YvsBaseArticles article){
//        BUT_PPTE.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                source.displayPropertyArticleById(article.getId());
//            }
//        });
//        BUT_ADD.setOnAction(new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent event) {
//                source.addArticleOnFacture(article);
//            }
//        });
//    }
//
//    public ButtonArticles(YvsBaseArticles article, ApplicationController target) {
//        this(article);
//        source = target;
////        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
////
////            @Override
////            public void handle(MouseEvent event) {
//////                source.actionOnClickOneArt(((ButtonArticles) event.getSource()).getREFART().getText());
////            }
////        });
//////        BUT_PPTE.setOnAction(new EventHandler<ActionEvent>() {
//////
//////            @Override
//////            public void handle(ActionEvent event) {
////////                source.displayPropertiesArt(code, displayPhotos(photo));
////////                source.displayPropertiesArt(code);
//////            }
//////        });
//
//    }
//
//    private Image displayPhotos(String photo) {
//        try {
//            File f = new File(UtilsProject.readProperty().getCheminPhotos() + "\\" + photo);
//            Image img = new Image(new FileInputStream(f));
//            return img;
//        } catch (FileNotFoundException ex) {
//            File f = new File(LocalLoader.class.getResource("icones/produits.png").toExternalForm());
//            if (f.exists()) {
//                try {
//                    Image img = new Image(new FileInputStream(f));
//                    return img;
//                } catch (FileNotFoundException ex1) {
//                    Logger.getLogger(ButtonArticles.class.getName()).log(Level.SEVERE, null, ex1);
//                }
//            }
//        }
//        return null;
//    }
//
//    private Image displayPhotosCond(String photo) {
////        try {
////            File f = new File(LymytzCaisseAuto.session.paramG.getCheminPhotos() + "\\" + photo);
////            Image img = new Image(new FileInputStream(f));
////            return img;
////        } catch (FileNotFoundException ex) {
////            File f = new File(LocalLoader.class.getResource("view/icones/produits.png").toExternalForm());
////            if (f.exists()) {
////                try {
////                    Image img = new Image(new FileInputStream(f));
////                    return img;
////                } catch (FileNotFoundException ex1) {
////                    Logger.getLogger(VenteATableController.class.getName()).log(Level.SEVERE, null, ex1);
////                }
////            }
////        }
//        return null;
//    }
//    ImageView i1;
//
//    private void decorateButton() {
//        i1 = new ImageView(new Image(LocalLoader.class.getResource("icones/add1.png").toExternalForm()));
//        BUT_PPTE = new ImageView(new Image(LocalLoader.class.getResource("icones/info.png").toExternalForm()));
//        BUT_ADD.setGraphic(i1);
//        BUT_ADD.getStyleClass().add("mes_boutons_op");
////        BUT_PPTE.getStyleClass().add("mes_boutons_op");
//        BUT_ADD.setWrapText(true);
//    }
//}
