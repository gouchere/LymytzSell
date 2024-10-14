///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package lymytz.service.application.service.reglement;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.ResourceBundle;
//import javafx.beans.property.SimpleObjectProperty;
//import javafx.beans.value.ObservableValue;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.util.Callback;
//import lymytz.view.main.ListFacturesController;
//import yvs.dao.salaire.service.Constantes;
//import yvs.entity.commercial.vente.YvsComContenuDocVente;
//import yvs.entity.commercial.vente.YvsComDocVentes;
//import yvs.entity.commercial.vente.YvsComContenuDocVente;
//import yvs.entity.compta.YvsComptaCaissePieceVente;
//
///**
// * FXML Controller class
// *
// * @author LENOVO
// */
//public class ListDetailFactController implements Initializable, Controller {
//
//    private YvsComDocVentes facture;
//    @FXML
//    private TableView<YvsComptaCaissePieceVente> TABLE_REG;
//    @FXML
//    private TableColumn<YvsComptaCaissePieceVente, String> CR_FAC;
//    @FXML
//    private TableColumn<YvsComptaCaissePieceVente, String> CR_NUM;
//    @FXML
//    private TableColumn<YvsComptaCaissePieceVente, String> CR_MONTANT;
//    @FXML
//    private TableColumn<YvsComptaCaissePieceVente, String> CR_CAISSE;
//    @FXML
//    private TableColumn<YvsComptaCaissePieceVente, String> CR_DATE;
//    @FXML
//    private TableColumn<YvsComptaCaissePieceVente, String> CR_STATUT;
//    @FXML
//    private TableColumn<YvsComptaCaissePieceVente, String> CR_AUTEUR;
//    @FXML
//    private TableView<YvsComContenuDocVente> TABLE_BL;
//    @FXML
//    private TableColumn<YvsComContenuDocVente, String> CL_FAC;
//    @FXML
//    private TableColumn<YvsComContenuDocVente, String> CL_NUM;
//    @FXML
//    private TableColumn<YvsComContenuDocVente, String> CL_DEPOT;
//    @FXML
//    private TableColumn<YvsComContenuDocVente, String> CL_DATE;
//    @FXML
//    private TableColumn<YvsComContenuDocVente, String> CL_STATUT;
//    @FXML
//    private TableColumn<YvsComContenuDocVente, String> CL_AUTEUR;
//    @FXML
//    private TableColumn<YvsComContenuDocVente, String> CL_REF;
//    @FXML
//    private TableColumn<YvsComContenuDocVente, String> CL_DES;
//    @FXML
//    private TableColumn<YvsComContenuDocVente, String> CL_QUANTITE;
//
//    /**
//     * Initializes the controller class.
//     *
//     * @param url
//     */
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//
//    }
//
//    public void initController(ListFacturesController page, YvsComDocVentes facture) {
//        this.facture = facture;
//        initTableReglement();
//        initTableLivraison();
//    }
//
//    private void initTableReglement() {
//        ObservableList<YvsComptaCaissePieceVente> items = FXCollections.observableArrayList(facture.getReglements());
//        TABLE_REG.setItems(items);
//        CR_FAC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComptaCaissePieceVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComptaCaissePieceVente, String> param) {
//                if (param.getValue() != null) {
//                    return new SimpleObjectProperty<>(param.getValue().getVente().getNumDoc());
//                }
//                return new SimpleObjectProperty<>();
//            }
//        });
//        CR_NUM.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComptaCaissePieceVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComptaCaissePieceVente, String> param) {
//                return new SimpleObjectProperty<String>(param.getValue().getNumeroPiece());
//            }
//        });
//        CR_MONTANT.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComptaCaissePieceVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComptaCaissePieceVente, String> param) {
//                return new SimpleObjectProperty<String>(Constantes.nbF.format(param.getValue().getMontant()));
//            }
//        });
//        CR_AUTEUR.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComptaCaissePieceVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComptaCaissePieceVente, String> param) {
//                return new SimpleObjectProperty<String>(param.getValue().getAuthor().getUsers().getNomUsers());
//            }
//        });
//        CR_CAISSE.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComptaCaissePieceVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComptaCaissePieceVente, String> param) {
//                return new SimpleObjectProperty<String>(param.getValue().getCaisse().getIntitule());
//            }
//        });
//        CR_DATE.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComptaCaissePieceVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComptaCaissePieceVente, String> param) {
//                return new SimpleObjectProperty<String>(Constantes.df.format(param.getValue().getDatePaiement()));
//            }
//        });
//        CR_STATUT.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComptaCaissePieceVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComptaCaissePieceVente, String> param) {
//                return new SimpleObjectProperty<String>(param.getValue().getStatutPiece() + "");
//            }
//        });
//    }
//
//    private void initTableLivraison() {
//        ObservableList<YvsComContenuDocVente> items = FXCollections.observableArrayList(listeLivraison(facture.getDocuments()));
//        TABLE_BL.setItems(items);
//        CL_FAC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComContenuDocVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComContenuDocVente, String> param) {
//                if (param.getValue() != null) {
//                    return new SimpleObjectProperty<>(param.getValue().getDocVente().getNumeroExterne());
//                }
//                return new SimpleObjectProperty<>();
//            }
//        });
//        CL_NUM.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComContenuDocVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComContenuDocVente, String> param) {
//                return new SimpleObjectProperty<String>(param.getValue().getDocVente().getNumDoc());
//            }
//        });
//        CL_DATE.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComContenuDocVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComContenuDocVente, String> param) {
//                return new SimpleObjectProperty<String>(Constantes.df.format(param.getValue().getDocVente().getDateLivraison()));
//            }
//        });
//        CL_AUTEUR.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComContenuDocVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComContenuDocVente, String> param) {
//                return new SimpleObjectProperty<String>(param.getValue().getAuthor().getUsers().getNomUsers());
//            }
//        });
//        CL_DEPOT.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComContenuDocVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComContenuDocVente, String> param) {
//                return new SimpleObjectProperty<String>(param.getValue().getDocVente().getDepotLivrer().getDesignation());
//            }
//        });
//        CL_DES.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComContenuDocVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComContenuDocVente, String> param) {
//                return new SimpleObjectProperty<String>(param.getValue().getArticle().getDesignation());
//            }
//        });
//        CL_QUANTITE.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComContenuDocVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComContenuDocVente, String> param) {
//                return new SimpleObjectProperty<String>(Constantes.nbF.format(param.getValue().getQuantite()));
//            }
//        });
//        CL_REF.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComContenuDocVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComContenuDocVente, String> param) {
//                return new SimpleObjectProperty<String>(param.getValue().getArticle().getRefArt());
//            }
//        });
//        CL_STATUT.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<YvsComContenuDocVente, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<YvsComContenuDocVente, String> param) {
//                return new SimpleObjectProperty<String>(param.getValue().getStatut());
//            }
//        });
//    }
//
//    private List<YvsComContenuDocVente> listeLivraison(List<YvsComDocVentes> l) {
//        List<YvsComContenuDocVente> re = new ArrayList<>();
//        if (l != null) {
//            for (YvsComDocVentes dv : l) {
//                if (dv.getTypeDoc().equals(Constantes.TYPE_BLV)) {
//                    for (YvsComContenuDocVente c : dv.getContenus()) {
//                        re.add(c);
//                    }
//                }
//            }
//        }
//        return re;
//    }
//}
