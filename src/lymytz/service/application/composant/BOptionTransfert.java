///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package lymytz.service.application.composant;
//
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.scene.control.Button;
//import javafx.scene.control.TreeTableCell;
//import lymytz.service.application.ApplicationController;
//import lymytz.service.application.MyComptesController;
//import lymytz.service.application.bean.ContenuDocStock;
//import yvs.dao.salaire.service.Constantes;
//
///**
// *
// * @author LENOVO
// */
//public class BOptionTransfert extends TreeTableCell<ContenuDocStock, Long> {
//
//    ApplicationController mainControler;
//    MyComptesController compteController;
//    ContenuDocStock item;
//
//    final Button cellButon = new Button();
//
//    public void setMainContoler(ApplicationController mainContoler) {
//        this.mainControler = mainContoler;
//    }
//
//    public void setCompteController(MyComptesController compteController) {
//        this.compteController = compteController;
//    }
//
//    public BOptionTransfert(String text) {
//        this.cellButon.setText(text);
//        if (getTreeTableRow() != null) {
//            item = getTreeTableRow().getItem();
//        }
//        cellButon.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                item = getTreeTableRow().getItem();
//                if (item != null) {
//                    if(compteController.valideReceptionTransfert(getTreeTableRow().getItem().getId(), item.isDisplayButtonOption())){
//                        cellButon.setText("RENVOYER");
//                        cellButon.setDisable(true);
//                    }
//                    
//                }
//            }
//        });
//    }
//
//    public BOptionTransfert(String text, ApplicationController appsc) {
//        this(text);
//        mainControler = appsc;
//    }
//
//    @Override
//    protected void updateItem(Long item_, boolean empty) {
//        super.updateItem(item_, empty);
//        if (getTreeTableRow() != null) {
//            item = getTreeTableRow().getItem();
//            if (!empty && item != null ? item.isDisplayButtonOption() : false) {
//                setGraphic(cellButon);
//                cellButon.setVisible(true);
//                this.cellButon.setText("ACCEPTER TOUT");
//            } else {
//                if (item != null) {
//                    if (item.getStatut().equals(Constantes.ETAT_VALIDE)) {
//                        this.cellButon.setText("RENVOYER");
//                        this.cellButon.setDisable(true);
//                    } else {
//                        this.cellButon.setText("ACCEPTER");
//                    }
//                    setGraphic(cellButon);
//                    cellButon.setVisible(true);
//                } else {
//                    cellButon.setVisible(false);
//                }
//            }
//        }
//    }
//
//}
