/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.view.main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lymytz.dao.Options;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.dao.query.LQueryFactories;
import lymytz.dao.query.RQueryFactories;
import lymytz.service.application.Controller;
import lymytz.service.application.bean.Factures;
import lymytz.service.application.loader.LoaderFacture;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import lymytz.view.LocalLoader;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class ListFacturesController implements Initializable, Controller {

    HomeCaisseController page;
    LQueryFactories rq = new LQueryFactories();
    ObservableList<Factures> items = FXCollections.observableArrayList();

    ContextMenu CTM_TV = new ContextMenu();

    private List<String> types;

    @FXML
    private TableView<Factures> TABLE_FACTURE;
    @FXML
    private TableColumn<Factures, Integer> COL_N;
    @FXML
    private TableColumn<Factures, String> COL_TYPE;
    @FXML
    private TableColumn<Factures, String> COL_NUM;
    @FXML
    private TableColumn<Factures, String> COL_CLIENT;
    @FXML
    private TableColumn<Factures, String> COL_DATE;
    @FXML
    private TableColumn<Factures, String> COL_TOTAL;
    @FXML
    private TableColumn<Factures, String> COL_HEURE;
    @FXML
    private TableColumn<Factures, String> COL_D_LIV;
    @FXML
    private TableColumn<Factures, Boolean> COL_VALIDE;
    @FXML
    private TableColumn<Factures, Boolean> COL_LIV;
    @FXML
    private TableColumn<Factures, Boolean> COL_REG;
    @FXML
    private TableColumn<Factures, Long> COL_ID;
    @FXML
    private TableColumn<Factures, Long> COL_SYNC;
    @FXML
    private TextField TXT_REF;
    @FXML
    private TextField TXT_CLIENT;
    @FXML
    private ProgressBar PROGRESS;
    @FXML
    private Label PROGRESS_LABEL;
    @FXML
    private ComboBox<String> CB_TYPE;
    @FXML
    private ComboBox<String> CB_STATUT_DOC;
    @FXML
    private ComboBox<String> CB_STATUT_LIV;
    @FXML
    private ComboBox<String> CB_STATUT_REG;

    public ObservableList<Factures> getItems() {
        return items;
    }

    public void setItems(ObservableList<Factures> items) {
        this.items = items;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initColumnData();
    }

    MenuItem itemLivrer;

    public void initPage(HomeCaisseController page, String type) {
        this.page = page;
        //context menu
        MenuItem itemSynchro = new MenuItem("Synchroniser ");
        MenuItem itemActualiser = new MenuItem("Actualiser ");
        CTM_TV.getItems().add(itemSynchro);
        CTM_TV.getItems().add(itemActualiser);
        loadDataFactures(type);
        initInfoSearch();
        TABLE_FACTURE.setContextMenu(CTM_TV);
        TABLE_FACTURE.setItems(items);
        TABLE_FACTURE.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Factures>() {

            @Override
            public void changed(ObservableValue<? extends Factures> observable, Factures oldValue, Factures newValue) {
                YvsComDocVentes doc = (YvsComDocVentes) rq.findOneByNQ("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{newValue.getId()});
                doc.setContenus(rq.loadByNamedQuery("YvsComContenuDocVente.findByDocVente", new String[]{"docVente"}, new Object[]{doc}));
                page.displayFactureOnView(doc);
            }
        });
        CB_TYPE.getItems().addAll(Constantes.TYPE_FV, Constantes.TYPE_BCV);
        CB_STATUT_DOC.getItems().addAll(Constantes.ETAT_ATTENTE, Constantes.ETAT_VALIDE, Constantes.ETAT_ENCOURS);
        CB_STATUT_LIV.getItems().addAll(Constantes.ETAT_ATTENTE, Constantes.ETAT_LIVRE, Constantes.ETAT_ENCOURS);
        CB_STATUT_REG.getItems().addAll(Constantes.ETAT_ATTENTE, Constantes.ETAT_REGLE, Constantes.ETAT_ENCOURS);
        types = new ArrayList<>();
//        types.add(Constantes.TYPE_FV);
        types.add(Constantes.TYPE_BCV);
        itemSynchro.setOnAction((ActionEvent event) -> {
            Factures select = TABLE_FACTURE.getSelectionModel().getSelectedItem();
            changeStatutSynchronize(select);
        });
        itemActualiser.setOnAction((ActionEvent event) -> {
            Factures select = TABLE_FACTURE.getSelectionModel().getSelectedItem();
            if (page.getConnectRemoteServer() && Constantes.asLong(select.getIdDistant())) {
                if (page.getConnectRemoteServer()) {
                    select = new RQueryFactories().actualiseStatut(select);
                    TABLE_FACTURE.refresh();
                    String query = "UPDATE yvs_com_doc_ventes SET statut_livre=?, statut_regle=? WHERE id=?";
                    rq.executeSqlQuery(query, new Options[]{new Options(select.getStatutLivraison(), 1),
                        new Options(select.getStatutReglement(), 2), new Options(select.getId(), 3)});
                } else {
                    LymytzService.openAlertDialog("Le serveur distant n'est pas connect??", "Serveur distant d??connect??", "", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void initColumnData() {
        COL_CLIENT.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factures, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Factures, String> param) {
                return new SimpleObjectProperty(param.getValue().getNomClient());
            }
        });
        COL_DATE.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factures, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Factures, String> param) {
                return new SimpleObjectProperty((param.getValue().getDate() != null) ? Constantes.dfD.format(param.getValue().getDate()) : "");
            }
        });
        COL_TOTAL.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factures, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Factures, String> param) {
                return new SimpleObjectProperty((param.getValue().getTotal() != null) ? Constantes.nbf.format(param.getValue().getTotal()) : "");
            }
        });
        COL_HEURE.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factures, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Factures, String> param) {
                return new SimpleObjectProperty((param.getValue().getHeure() != null) ? Constantes.dfh.format(param.getValue().getHeure()) : "");
            }
        });
        COL_D_LIV.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factures, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Factures, String> param) {
                return new SimpleObjectProperty((param.getValue().getDateLiv() != null) ? Constantes.dfh.format(param.getValue().getDateLiv()) : "");
            }
        });
        COL_ID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factures, Long>, ObservableValue<Long>>() {
            @Override
            public ObservableValue<Long> call(TableColumn.CellDataFeatures<Factures, Long> param) {
                return new SimpleObjectProperty(param.getValue().getId());
            }
        });
        COL_N.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factures, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Factures, Integer> param) {
                return new SimpleObjectProperty(param.getValue().getNumLine());
            }
        });
        COL_NUM.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factures, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Factures, String> param) {
                return new SimpleObjectProperty(param.getValue().getNumDoc());
            }
        });
        COL_TYPE.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factures, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Factures, String> param) {
                return new SimpleObjectProperty(param.getValue().getType());
            }
        });
        COL_LIV.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factures, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Factures, Boolean> param) {
                return new SimpleBooleanProperty(param.getValue().getStatutLivraison().equals(Constantes.ETAT_LIVRE));
            }
        });
        COL_REG.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factures, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Factures, Boolean> param) {
                return new SimpleBooleanProperty(param.getValue().getStatutReglement().equals(Constantes.ETAT_REGLE));
            }
        });
        COL_VALIDE.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factures, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Factures, Boolean> param) {
                if (param.getValue().getStatutDoc() != null) {
                    return new SimpleBooleanProperty(param.getValue().getStatutDoc().equals(Constantes.ETAT_VALIDE));
                } else {
                    return new SimpleBooleanProperty(false);
                }
            }
        });

        COL_REG.setCellFactory(new Callback<TableColumn<Factures, Boolean>, TableCell<Factures, Boolean>>() {
            @Override
            public TableCell<Factures, Boolean> call(TableColumn<Factures, Boolean> param) {
                CheckBoxTableCell cell = new CheckBoxTableCell();
                cell.setDisable(true);
                return cell;
            }
        });
        COL_VALIDE.setCellFactory(new Callback<TableColumn<Factures, Boolean>, TableCell<Factures, Boolean>>() {
            @Override
            public TableCell<Factures, Boolean> call(TableColumn<Factures, Boolean> param) {
                CheckBoxTableCell cell = new CheckBoxTableCell();
                cell.setDisable(true);
                return cell;
            }
        });
        COL_LIV.setCellFactory(new Callback<TableColumn<Factures, Boolean>, TableCell<Factures, Boolean>>() {
            @Override
            public TableCell<Factures, Boolean> call(TableColumn<Factures, Boolean> param) {
                CheckBoxTableCell cell = new CheckBoxTableCell();
                cell.setDisable(true);
                return cell;
            }
        });
        COL_SYNC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factures, Long>, ObservableValue<Long>>() {
            @Override
            public ObservableValue<Long> call(TableColumn.CellDataFeatures<Factures, Long> param) {
                return new SimpleObjectProperty(param.getValue().getIdDistant());
            }
        });
        COL_LIV.getStyleClass().add("colsOptions");
        COL_SYNC.getStyleClass().add("colsOptions");
        COL_VALIDE.getStyleClass().add("colsOptions");
        COL_REG.getStyleClass().add("colsOptions");

    }

    private void initInfoSearch() {

    }

    private void loadDataFactures(String type) {
        try {
            if (UtilsProject.headerDoc != null) {
                LoaderFacture task = new LoaderFacture(type);

                PROGRESS.progressProperty().unbind();
                PROGRESS.progressProperty().bind(task.progressProperty());
                PROGRESS_LABEL.textProperty().unbind();
                PROGRESS_LABEL.textProperty().bind(task.messageProperty());
                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                items.clear();
                                items.addAll(task.getValue());
                                TABLE_FACTURE.setItems(items);
                                PROGRESS_LABEL.textProperty().unbind();
                                PROGRESS_LABEL.setText("termin?? !");
                            }
                        });
                    }
                });
                Thread t = new Thread(task);
                t.setName("Loader facture");
                t.start();
            } else {
                LymytzService.openAlertDialog("Veuillez enregistrer l'ent??te de votre journal", "Aucune ent??te trouv??", "Aucune ent??te trouv??", Alert.AlertType.ERROR);
            }
        } catch (Exception ex) {
            Logger.getLogger(ListFacturesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//
//    @FXML
//    private void findByClient(KeyEvent event) {
//        findByClient();
//    }
//
//    @FXML
//    private void findByNumDoc(KeyEvent event) {
//        findByNumDoc();
//    }
//
//    @FXML
//    private void findByTypeDoc(ActionEvent event) {
//        findByTypeDoc();
//    }
////
////    private void findByNumDoc() {
//        String operateur = "LIKE";
//        ParametreRequete p = new ParametreRequete("y.numDoc", "numDoc", null, operateur, "AND");
//        if (TXT_REF.getText() != null ? !TXT_REF.getText().isEmpty() : false) {
//            p.setObjet("%" + TXT_REF.getText().toUpperCase() + "%");
//        }
//        paginator.addParam(p);
//        loadDataFactures();
//    }
//
//    private void findByClient() {
//        ParametreRequete p = new ParametreRequete(null, "client", null, "=", "AND");
//        String operateuClt = "LIKE";
//        String predicat = (operateuClt.trim().equals("LIKE")) ? "OR" : "OR";
//        if (TXT_CLIENT.getText() != null ? !TXT_CLIENT.getText().isEmpty() : false) {
//            p = new ParametreRequete(null, "client", (opClient(false) + TXT_CLIENT.getText().toUpperCase() + opClient(true)), operateuClt, "AND");
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nomClient)", "client", TXT_CLIENT.getText().toUpperCase() + "%", operateuClt, predicat));
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.codeClient)", "client", TXT_CLIENT.getText().toUpperCase() + "%", operateuClt, predicat));
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.nom)", "client", TXT_CLIENT.getText().toUpperCase() + "%", operateuClt, predicat));
//        }
//        paginator.addParam(p);
//        loadDataFactures();
//    }
//
//    private void findByTypeDoc() {
//        ParametreRequete p = new ParametreRequete("y.typeDoc", "typeDoc", null, "=", "AND");
//        if (CB_TYPE.getValue() != null ? !CB_TYPE.getValue().isEmpty() : false) {
//            p.setObjet(CB_TYPE.getValue());
//        }
//        paginator.addParam(p);
//        loadDataFactures();
//    }

    List<YvsComDocVentes> executeQuery() {
        List<YvsComDocVentes> re = null;
//        ParametreRequete p0 = new ParametreRequete(null, "type", "XXXX", "=", "AND");
//        p0.getOtherExpression().add(new ParametreRequete("y.typeDoc", "type0", Constantes.TYPE_FV, "=", "OR"));
//        p0.getOtherExpression().add(new ParametreRequete("y.typeDoc", "type1", Constantes.TYPE_BCV, "=", "OR"));
//        paginator.addParam(new ParametreRequete("y.enteteDoc", "header", UtilsProject.headerDoc, "=", "AND"));
//        paginator.addParam(p0);
//        String query = "YvsComDocVentes y JOIN FETCH y.client JOIN y.enteteDoc E INNER JOIN E.creneau C JOIN FETCH C.users "
//                + " INNER JOIN C.creneauPoint CP JOIN FETCH CP.point";
//
//        String nameQueri = paginator.getQueryToExecute("y", "y", query, "y.numDoc");
//        re = rq.loadEntity(nameQueri, paginator.getChamp(), paginator.getVal());
//        // Ajoute l'ensemble des commandes non servi du point de vente
//        List<YvsComDocVentes> l = rq.loadNameQueries("YvsComDocVentes.findCmdeNonServie", new String[]{"point", "typesDocs", "header", "statut", "agence", "statutLivre", "statutRegle"},
//                new Object[]{UtilsProject.headerDoc.getCreneau().getCreneauPoint().getPoint(), types, UtilsProject.headerDoc, Constantes.ETAT_VALIDE, UtilsProject.currentAgence, Constantes.ETAT_LIVRE, Constantes.ETAT_REGLE});
//        re.addAll(l);
        return re;
    }

    private String opClient(boolean g) {
//        RadioButton type = (RadioButton) G_CLIENT.getSelectedToggle();
//        if (type.getText().equals("All")) {
//            if (g) {
//                return (type.getText().equals("debut")) ? "%" : "";
//            } else {
//                return (type.getText().equals("fin")) ? "%" : "";
//            }
//        } else {
        return "";
//        }
    }

    private String opNumdoc(boolean g) {
//        RadioButton type = (RadioButton) G_REF.getSelectedToggle();
//        if (!type.getText().equals("All")) {
//            if (g) {
//                return (type.getText().equals("debut")) ? "%" : "";
//            } else {
//                return (type.getText().equals("fin")) ? "%" : "";
//            }
//        } else {
        return "";
//        }
    }

    private String getOperateur(RadioButton type) {
        String op = "=";
        if (!type.getText().equals("All")) {
            op = "LIKE";
        }
        return op;
    }

    @FXML
    private void openDlgDetails(ActionEvent event) {
        try {
            long id = TABLE_FACTURE.getSelectionModel().getSelectedItem().getId();
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource("main/reglement/list_details_facture.fxml"));
            AnchorPane root = load.load();
            Scene scene = new Scene(root, 820, 600);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Lymytz:Claviers");
            stage.centerOnScreen();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(UtilsProject.primaryStage);
            stage.setResizable(false);
            stage.show();
//            ListDetailFactController pageImportController = (ListDetailFactController) load.getController();
//            YvsComDocVentes dv = (YvsComDocVentes) rq.findOneEntity("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{id});
// pageImportController  controler.initController(this, dv);
        } catch (IOException ex) {
            Logger.getLogger(HomeCaisseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void freeMemoryController() {
        TABLE_FACTURE = null;
    }

    public class TacheFind extends Task<List<Factures>> {

        List<YvsComDocVentes> listFact;

        public TacheFind() {
            listFact = new ArrayList<>();
        }

        @Override
        protected List<Factures> call() throws Exception {
            List<Factures> result = new ArrayList<>();
            if (UtilsProject.headerDoc != null) {
                listFact = executeQuery();
                this.updateProgress(10, (listFact.size() + 10));
            }
            int line = 0;
            int numero = 0;
            Factures fac;
            if (!listFact.isEmpty()) {
                for (YvsComDocVentes f : listFact) {
                    fac = new Factures();
                    if (f.getAdresse() != null) {
                        fac.setAdresse(f.getAdresse().getLibele());
                    }
//                    fac.setCategorieClient(f.getClient().getCategorie());
                    fac.setHeader(f.getEnteteDoc());
                    fac.setId(f.getId());
                    fac.setNomClient(f.getClient().getNom_prenom());
                    fac.setNumDoc(f.getNumDoc());
                    fac.setNumPiece(f.getNumPiece());
                    fac.setStatutDoc(f.getStatut());
                    fac.setStatutLivraison(f.getStatutLivre());
                    fac.setStatutReglement(f.getStatutRegle());
                    fac.setHeure(f.getDateSave());
                    fac.setDateLiv(f.getDateLivraisonPrevu());
                    fac.setDate(f.getEnteteDoc().getDateEntete());
                    fac.setType(f.getTypeDoc());
                    fac.setNumLine(line++);
                    result.add(fac);
                    numero++;
                    this.updateProgress(numero, listFact.size() + 10);
                }
            } else {
                this.updateProgress(100, 100);
            }
            return result;
        }
    }

    private void changeStatutSynchronize(Factures fac) {
        String query = "UPDATE yvs_synchro_listen_table SET to_listen=true, message=null WHERE name_table=? AND id_source=? AND action_name='UPDATE'";
        Long re = rq.executeSqlQuery(query, new Options[]{new Options("yvs_com_doc_ventes", 1), new Options(fac.getId(), 2)});
        if (Constantes.asLong(re)) {
            LymytzService.success();
        } else {
            LymytzService.openAlertDialog("", "", "Aucune donn??e mise ?? jour", Alert.AlertType.WARNING);
        }
    }
}
