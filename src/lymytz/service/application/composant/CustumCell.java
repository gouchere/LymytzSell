///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package lymytz.service.application.composant;
//
//import javafx.scene.control.Label;
//import javafx.scene.control.ListCell;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import lymytz.view.LocalLoader;
//
///**
// *
// * @author LENOVO
// */
//public class CustumCell extends ListCell<YvsBaseArticles> {
//
//        ImageView image;
//        HBox hNode ;
//        VBox node = new VBox(5);
//
//    public CustumCell() {
////        
//    }
//
//    @Override
//    public void updateItem(YvsBaseArticles art, boolean empty) {
//        super.updateItem(art, empty);
//        hNode=new HBox(5);
//        if (!empty) {
//            if (art != null) {
//                    image = new ImageView(new Image(LocalLoader.class.getResource("icones/produits.png").toExternalForm()));
//    //                image.setFitWidth(1);
//    //                image.setFitHeight(1);                
//                    node.getChildren().addAll(new Label(art.getRefArt()), new Label(art.getDesignation()));
//                    hNode.getChildren().addAll(image, node);    
//            }
//            setGraphic(hNode);
//        }
//    }
//
//}
