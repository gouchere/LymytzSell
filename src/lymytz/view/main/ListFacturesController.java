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
import javafx.scene.control.CheckBox;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lymytz.dao.Options;
import lymytz.dao.ParamOption;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.dao.query.LQueryFactories;
import lymytz.dao.query.RQueryFactories;
import lymytz.service.application.Controller;
import lymytz.service.application.bean.Factures;
import lymytz.service.application.loader.LoaderFacture;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.EtatDoc;
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
    private String type;
    List<ParamOption> paramsOptions;

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
    private ComboBox<EtatDoc> CB_STATUT_DOC;
    @FXML
    private ComboBox<EtatDoc> CB_STATUT_LIV;
    @FXML
    private ComboBox<EtatDoc> CB_STATUT_REG;

    @FXML
    private CheckBox CHK_CMDE_SERVIE;

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
        paramsOptions = new ArrayList<>();
        initColumnData();
    }

    MenuItem itemLivrer;

    public void initPage(HomeCaisseController page, String type) {
        this.page = page;
        this.type = type;
        //context menu
        MenuItem itemSynchro = new MenuItem("Synchroniser ");
        MenuItem itemActualiser = new MenuItem("Actualiser ");
        CTM_TV.getItems().add(itemSynchro);
        CTM_TV.getItems().add(itemActualiser);
        initParamsQueries(type);
        loadDataFactures(type);
        initInfoSearch();
        if (UtilsProject.REPLICATION) {
            TABLE_FACTURE.setContextMenu(CTM_TV);
        }
        TABLE_FACTURE.setItems(items);
        TABLE_FACTURE.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Factures>() {

            @Override
            public void changed(ObservableValue<? extends Factures> observable, Factures oldValue, Factures newValue) {
                YvsComDocVentes doc = (YvsComDocVentes) rq.findOneByNQ("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{newValue.getId()});
                doc.setContenus(rq.loadByNamedQuery("YvsComContenuDocVente.findByDocVente", new String[]{"docVente"}, new Object[]{doc}));
                page.displayFactureOnView(doc);
            }
        });
        CB_STATUT_DOC.getItems().addAll(new EtatDoc(null, "Tout"), new EtatDoc(Constantes.ETAT_VALIDE, "Non Validé "));
        CB_STATUT_LIV.getItems().addAll(new EtatDoc(null, "Tout"), new EtatDoc(Constantes.ETAT_LIVRE, "Non Livré "));
        CB_STATUT_REG.getItems().addAll(new EtatDoc(null, "Tout"), new EtatDoc(Constantes.ETAT_REGLE, "Non Réglé "));
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
                    LymytzService.openAlertDialog("Le serveur distant n'est pas connecté", "Serveur distant déconnecté", "", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void initColumnData() {
        COL_CLIENT.setCellValueFactory((TableColumn.CellDataFeatures<Factures, String> param) -> new SimpleObjectProperty(param.getValue().getNomClient()));
        COL_DATE.setCellValueFactory((TableColumn.CellDataFeatures<Factures, String> param) -> new SimpleObjectProperty((param.getValue().getDate() != null) ? Constantes.dfD.format(param.getValue().getDate()) : ""));
        COL_TOTAL.setCellValueFactory((TableColumn.CellDataFeatures<Factures, String> param) -> new SimpleObjectProperty((param.getValue().getTotal() != null) ? Constantes.nbf.format(param.getValue().getTotal()) : ""));
        COL_HEURE.setCellValueFactory((TableColumn.CellDataFeatures<Factures, String> param) -> new SimpleObjectProperty((param.getValue().getHeure() != null) ? Constantes.dfh.format(param.getValue().getHeure()) : ""));
        COL_D_LIV.setCellValueFactory((TableColumn.CellDataFeatures<Factures, String> param) -> new SimpleObjectProperty((param.getValue().getDateLiv() != null) ? Constantes.dfh.format(param.getValue().getDateLiv()) : ""));
        COL_ID.setCellValueFactory((TableColumn.CellDataFeatures<Factures, Long> param) -> new SimpleObjectProperty(param.getValue().getId()));
        COL_N.setCellValueFactory((TableColumn.CellDataFeatures<Factures, Integer> param) -> new SimpleObjectProperty(param.getValue().getNumLine()));
        COL_NUM.setCellValueFactory((TableColumn.CellDataFeatures<Factures, String> param) -> new SimpleObjectProperty(param.getValue().getNumDoc()));
        COL_TYPE.setCellValueFactory((TableColumn.CellDataFeatures<Factures, String> param) -> new SimpleObjectProperty(param.getValue().getType()));
        COL_LIV.setCellValueFactory((TableColumn.CellDataFeatures<Factures, Boolean> param) -> new SimpleBooleanProperty(param.getValue().getStatutLivraison().equals(Constantes.ETAT_LIVRE)));
        COL_REG.setCellValueFactory((TableColumn.CellDataFeatures<Factures, Boolean> param) -> new SimpleBooleanProperty(param.getValue().getStatutReglement().equals(Constantes.ETAT_REGLE)));
        COL_VALIDE.setCellValueFactory((TableColumn.CellDataFeatures<Factures, Boolean> param) -> {
            if (param.getValue().getStatutDoc() != null) {
                return new SimpleBooleanProperty(param.getValue().getStatutDoc().equals(Constantes.ETAT_VALIDE));
            } else {
                return new SimpleBooleanProperty(false);
            }
        });

        COL_REG.setCellFactory((TableColumn<Factures, Boolean> param) -> {
            CheckBoxTableCell cell = new CheckBoxTableCell();
            cell.setDisable(true);
            return cell;
        });
        COL_VALIDE.setCellFactory((TableColumn<Factures, Boolean> param) -> {
            CheckBoxTableCell cell = new CheckBoxTableCell();
            cell.setDisable(true);
            return cell;
        });
        COL_LIV.setCellFactory((TableColumn<Factures, Boolean> param) -> {
            CheckBoxTableCell cell = new CheckBoxTableCell();
            cell.setDisable(true);
            return cell;
        });
        COL_SYNC.setCellValueFactory((TableColumn.CellDataFeatures<Factures, Long> param) -> new SimpleObjectProperty(param.getValue().getIdDistant()));
        COL_LIV.getStyleClass().add("colsOptions");
        COL_SYNC.getStyleClass().add("colsOptions");
        COL_VALIDE.getStyleClass().add("colsOptions");
        COL_REG.getStyleClass().add("colsOptions");

    }

    private void initInfoSearch() {

    }

    public void initParamsQueries(String type) {
        paramsOptions.clear();
        paramsOptions.add(new ParamOption("d.type_doc", type, "="));
        paramsOptions.add(new ParamOption("d.statut", Constantes.ETAT_ANNULE, "!="));
        if (UtilsProject.headerDoc != null) {
            if (type.equals(Constantes.TYPE_FV)) {
                paramsOptions.add(new ParamOption("e.id", UtilsProject.headerDoc.getId(), "="));
            } else {
                paramsOptions.add(new ParamOption("d.statut_livre", Constantes.ETAT_LIVRE, "!="));
                paramsOptions.add(new ParamOption("e.agence", UtilsProject.currentAgence.getId(), "="));
            }
        }
    }

    private void loadDataFactures(String type) {
        try {
            if (UtilsProject.headerDoc != null) {
                LoaderFacture task = new LoaderFacture(type, paramsOptions);
                PROGRESS.progressProperty().unbind();
                PROGRESS.progressProperty().bind(task.progressProperty());
                PROGRESS_LABEL.textProperty().unbind();
                PROGRESS_LABEL.textProperty().bind(task.messageProperty());
                task.setOnSucceeded((WorkerStateEvent event) -> {
                    Platform.runLater(() -> {
                        items.clear();
                        items.addAll(task.getValue());
                        TABLE_FACTURE.setItems(items);
                        PROGRESS_LABEL.textProperty().unbind();
                        PROGRESS_LABEL.setText("terminé !");
                    });
                });
                Thread t = new Thread(task);
                t.setName("Loader facture");
                t.start();
            } else {
                LymytzService.openAlertDialog("Veuillez enregistrer l'entête de votre journal", "Aucune entête trouvé", "Aucune entête trouvé", Alert.AlertType.ERROR);
            }
        } catch (Exception ex) {
            Logger.getLogger(ListFacturesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
            LymytzService.openAlertDialog("", "", "Aucune donnée mise à jour", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void filterByStatutLiv(ActionEvent ev) {
        EtatDoc etat = CB_STATUT_LIV.getValue();
        ParamOption o = new ParamOption("d.statut_livre", Constantes.ETAT_LIVRE, "!=");
        if (etat.getCodeEtat() != null) {
            if (!paramsOptions.contains(o)) {
                paramsOptions.add(o);
            } else {
                int idx = paramsOptions.indexOf(o);
                paramsOptions.remove(idx);
                paramsOptions.add(o);
            }
        } else {
            if (type.equals(Constantes.TYPE_FV)) {
                paramsOptions.remove(o);
            }
        }
        loadDataFactures("");
    }

    @FXML
    public void filterByStatutReg(ActionEvent ev) {
        EtatDoc etat = CB_STATUT_REG.getValue();
        ParamOption o = new ParamOption("d.statut_regle", Constantes.ETAT_REGLE, "!=");
        if (etat.getCodeEtat() != null) {
            if (!paramsOptions.contains(o)) {
                paramsOptions.add(o);
            } else {
                int idx = paramsOptions.indexOf(o);
                paramsOptions.remove(idx);
                paramsOptions.add(o);
            }
        } else {
            paramsOptions.remove(o);
        }
        loadDataFactures("");
    }

    @FXML
    public void filterByStatutDoc(ActionEvent ev) {
        EtatDoc etat = CB_STATUT_DOC.getValue();
        ParamOption o = new ParamOption("d.statut", Constantes.ETAT_VALIDE, "!=");
        if (etat.getCodeEtat() != null) {
            if (!paramsOptions.contains(o)) {
                paramsOptions.add(o);
            } else {
                int idx = paramsOptions.indexOf(o);
                paramsOptions.remove(idx);
                paramsOptions.add(o);
            }
        } else {
            if (!paramsOptions.contains(o)) {
                paramsOptions.add(o);
            } else {
                int idx = paramsOptions.indexOf(o);
                paramsOptions.remove(idx);
                paramsOptions.add(new ParamOption("d.statut", Constantes.ETAT_ANNULE, "!="));
            }
        }
        loadDataFactures("");
    }

    @FXML
    public void findByNumDoc(KeyEvent ev) {
        ParamOption o = new ParamOption("d.num_doc", "%" + TXT_REF.getText() + "%", " LIKE ");
        if (Constantes.asString(TXT_REF.getText())) {
            if (TXT_REF.getText().length() > 2) {
                if (!paramsOptions.contains(o)) {
                    paramsOptions.add(o);
                } else {
                    int idx = paramsOptions.indexOf(o);
                    paramsOptions.remove(idx);
                    paramsOptions.add(o);
                }
                loadDataFactures("");
            } else if (TXT_REF.getText().length() == 0) {
                if (paramsOptions.contains(o)) {
                    paramsOptions.remove(o);
                    loadDataFactures("");
                }
            }
        } else {
            if (paramsOptions.contains(o)) {
                paramsOptions.remove(o);
                loadDataFactures("");
            }
        }
    }

    @FXML
    public void findByCustomer(KeyEvent ev) {
        ParamOption o = new ParamOption("cl.nom", "%" + TXT_CLIENT.getText() + "%", " LIKE ");
        if (Constantes.asString(TXT_CLIENT.getText())) {
            if (TXT_CLIENT.getText().length() > 2) {
                if (!paramsOptions.contains(o)) {
                    paramsOptions.add(o);
                } else {
                    int idx = paramsOptions.indexOf(o);
                    paramsOptions.remove(idx);
                    paramsOptions.add(o);
                }
                loadDataFactures("");
            } else if (TXT_CLIENT.getText().length() == 0) {
                if (paramsOptions.contains(o)) {
                    paramsOptions.remove(o);
                    loadDataFactures("");
                }
            }
        } else {
            if (paramsOptions.contains(o)) {
                paramsOptions.remove(o);
                loadDataFactures("");
            }
        }
    }

    @FXML
    public void findCommandeServi(ActionEvent ev) {
        ParamOption o = new ParamOption("d.document_lie", null, " IS NOT NULL ");
        if (CHK_CMDE_SERVIE.isSelected()) {
            paramsOptions.add(o);
        } else {
            int idx = paramsOptions.indexOf(o);
            paramsOptions.remove(idx);
        }
        loadDataFactures("");
    }
}
