/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.synchro.impor;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import lymytz.dao.Options;
import lymytz.dao.entity.service.EntityColumn;
import lymytz.dao.entity.service.LymytzData;
import lymytz.dao.entity.service.LymytzEntityClass;
import lymytz.dao.entity.service.LymytzLoaderEntity;
import lymytz.dao.query.LQueryFactories;
import lymytz.dao.query.RQueryFactories;
import lymytz.service.application.Controller;
import lymytz.service.application.bean.TableBean;
import lymytz.service.application.synchro.ParamQuery;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.CustomWindow;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.log.LogFiles;
import lymytz.synchro.ws.WsSynchro;
import lymytz.view.main.HomeCaisseController;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class ImportDataController implements Initializable, Controller {

    RQueryFactories Rdao = new RQueryFactories();   //dao sur la base de donnée distante
    LQueryFactories dao = new LQueryFactories();   //dao sur la base de donnée distante
    public List<LymytzData> listRemoteData;
    public List<LymytzData> listDataLocal;
    Thread tLoaderR, tLoaderL;

    HomeCaisseController page;
    public Stage fenetre;

    //
    ContextMenu CTM_TV_LOCAL = new ContextMenu();

    /**/
    @FXML
    private Label LAB_NAME_TABLE;
//    public ObservableList tables=FXCollections.
    @FXML
    private TableView<TableBean> TV_TABLE;
    @FXML
    private TableColumn<TableBean, String> C_TABLE;
    @FXML
    private TableColumn<TableBean, Long> C_MAJ;

    @FXML
    public ProgressBar PROGRESS;
    @FXML
    public Label PROGRESS_LABEL;

    @FXML
    public ProgressBar PROGRESS_LOCAL;
    @FXML
    public Label PROGRESS_MESSAGE_L;

    @FXML
    public Label LAB_RUN_IMPORT;

    @FXML
    public ProgressBar PROGRESS_REMOTE;
    @FXML
    public Label PROGRESS_MESSAGE_R;

    /**/
    public TableView TV_LOCALE;
    public TableView TV_REMOTE;

    /**/
    @FXML
    private VBox BOX_TAB_LOCALE;
    @FXML
    private VBox BOX_TAB_REMOTE;

    public ImportDataController() {
    }

    long memoire = 0;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // charge la liste
        new LymytzLoaderEntity<>(true);
        ObservableList items = FXCollections.observableArrayList(getListTable(LymytzLoaderEntity.ALLENTITY));
        TV_TABLE.setItems(items);
        initComponents();
        memoire = Runtime.getRuntime().freeMemory();
    }

    public void initComponents(HomeCaisseController page) {
        this.page = page;
    }

    public void initComponents() {
        MenuItem filter = new MenuItem("Filtrer...");
        MenuItem loadDataL = new MenuItem("Afficher les données locales...");
        MenuItem loadDataR = new MenuItem("Afficher les données distantes...");
        ContextMenu context = new ContextMenu(filter, loadDataL, loadDataR);
        TV_TABLE.setEditable(true);
        C_TABLE.setCellValueFactory(
                new PropertyValueFactory<>("simpleName")
        );
        C_TABLE.setEditable(true);
        C_MAJ.setCellValueFactory(new PropertyValueFactory<>("nbLocal"));
        //selection d'une table
        TV_LOCALE = new TableView<>();
        TV_REMOTE = new TableView<>();
        BOX_TAB_LOCALE.getChildren().clear();
        BOX_TAB_REMOTE.getChildren().clear();
        /*ajout du menu contextuel*/
        final MenuItem itemLink = new MenuItem("Lier à ");
        final MenuItem itemPropriete = new MenuItem("Voir la donnée lié");
        CTM_TV_LOCAL.getItems().addAll(itemLink, itemPropriete);
        TV_LOCALE.setContextMenu(CTM_TV_LOCAL);
        BOX_TAB_LOCALE.getChildren().add(TV_LOCALE);
        BOX_TAB_REMOTE.getChildren().add(TV_REMOTE);
        TV_TABLE.setContextMenu(context);
        TV_TABLE.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends TableBean> c) -> {

            //instancie le thread de chargement des données
//            loadRemoteData(colonnes);
//            loadLocalDataLocal(colonnes);
        });
        itemLink.setOnAction((ActionEvent event) -> {
            //ouvre une boite de dialogue attendant l'id distant de l'élément
            ObservableList line = (ObservableList) TV_LOCALE.getSelectionModel().getSelectedItem();
            line.size();
            line.get(2);
            Long idDistant = 0L;
            Long id = Long.valueOf(line.get(2).toString());
            if (Constantes.asString(line.get(1).toString())) {
                idDistant = Long.valueOf(line.get(1).toString());
            }
            openDlgToLinkData(id, TV_TABLE.getSelectionModel().getSelectedItem().getNameTable());
        });
        itemPropriete.setOnAction((ActionEvent event) -> {
            //ouvre une boite de dialogue avec les propriété de la ligne
        });
        filter.setOnAction((ActionEvent event) -> {
            List<EntityColumn> colonnes = LymytzLoaderEntity.loadEntityColumn(TV_TABLE.getSelectionModel().getSelectedItem().getNameEntity());
            openViewFilter(colonnes);
        });
        loadDataL.setOnAction((ActionEvent event) -> {
            fillDataLocal();
        });
        loadDataR.setOnAction((ActionEvent event) -> {
            fillDataRemote(null);
        });

        countDataTable();
    }

    private void fillDataLocal() {
        LAB_NAME_TABLE.setText(TV_TABLE.getSelectionModel().getSelectedItem().getNameEntity());
        TV_LOCALE.getColumns().clear();
        TV_LOCALE.getItems().clear();
        //récupération des colonne à partir du nom de la Classe Entity
        List<EntityColumn> colonnes = LymytzLoaderEntity.loadEntityColumn(TV_TABLE.getSelectionModel().getSelectedItem().getNameEntity());
        TableColumn col;
        //initialise la première colonne
        col = new TableColumn("N°");
        col.setPrefWidth(50);
        col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(0).toString());
            }
        });
        TV_LOCALE.getColumns().add(col);
        col = new TableColumn("Id Distant");
        col.setPrefWidth(65);
        col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(1).toString());
            }
        });
        TV_LOCALE.getColumns().add(col);
        int idx;
        try {
            for (int i = 2; i < colonnes.size() + 2; i++) {
                final int j = i;
                col = new TableColumn(colonnes.get(i - 2).getName());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                TV_LOCALE.getColumns().add(col);
            }
        } catch (Exception e) {
            System.err.println("********** Patienter.... ");
        }
        //instancie le thread de chargement des données
        loadLocalDataLocal(colonnes);
    }

    public void fillDataRemote(List<ParamQuery> params) {
        LAB_NAME_TABLE.setText(TV_TABLE.getSelectionModel().getSelectedItem().getNameEntity());
        TV_REMOTE.getColumns().clear();
        TV_REMOTE.getItems().clear();
        //récupération des colonne à partir du nom de la Classe Entity
        List<EntityColumn> colonnes = LymytzLoaderEntity.loadEntityColumn(TV_TABLE.getSelectionModel().getSelectedItem().getNameEntity());
        TableColumn col;
        //initialise la première colonne
        col = new TableColumn("N°");
        col.setPrefWidth(50);
        col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(0).toString());
            }
        });
        TV_REMOTE.getColumns().add(col);
        int idx;
        try {
            for (int i = 0; i < colonnes.size(); i++) {
                final int j = i;
                col = new TableColumn(colonnes.get(i).getName());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j + 1).toString());
                    }
                });
                TV_REMOTE.getColumns().add(col);
            }
        } catch (Exception e) {
            System.err.println("********** Patienter.... ");
        }
        //instancie le thread de chargement des données
        loadRemoteData(colonnes, params);
    }

    public void loadRemoteData(List<EntityColumn> colonnes, List<ParamQuery> params) {
        try {
            LoaderRemotelImportData task = new LoaderRemotelImportData();
            TV_REMOTE.getItems().clear();
            PROGRESS_REMOTE.progressProperty().unbind();
            PROGRESS_REMOTE.progressProperty().bind(task.progressProperty());
            PROGRESS_MESSAGE_R.textProperty().unbind();
            PROGRESS_MESSAGE_R.setText("Attente de chargement...");
            PROGRESS_MESSAGE_R.textProperty().bind(task.messageProperty());
            task.setTable(TV_TABLE.getSelectionModel().getSelectedItem().getNameTable());
            task.setColonnes(colonnes);
            task.setColFilter(extractColFilter(params));
            task.setValueFilters(extractValColFilter(params));
            task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (Event event) -> {
                ObservableList<ObservableList> result = task.getValue();
                TV_REMOTE.setItems(result);
                listRemoteData = new ArrayList<>(task.listData);
                PROGRESS_REMOTE.progressProperty().unbind();
                PROGRESS_MESSAGE_R.textProperty().unbind();
                PROGRESS_MESSAGE_R.setText("Récupération terminé !");

            });
            if (tLoaderR != null) {
                tLoaderR.interrupt();
            }
            tLoaderR = new Thread(task);
            tLoaderR.setName("Loader R Data");
            tLoaderR.start();
        } catch (Exception ex) {
            LogFiles.addLogInFile("", new Exception(ex));
            Logger.getLogger(ImportDataController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String[] extractColFilter(List<ParamQuery> params) {
        if (params != null) {
            String[] re = new String[params.size()];
            int i = 0;
            params.stream().forEach((p) -> {
                re[i] = p.getColone();
            });
            return re;
        } else {
            return null;
        }
    }

    private Object[] extractValColFilter(List<ParamQuery> params) {
        if (params != null) {
            Object[] re = new Object[params.size()];
            int i = 0;
            params.stream().forEach((p) -> {
                re[i] = p.getValue();
            });
            return re;
        } else {
            return null;
        }
    }

    private void loadLocalDataLocal(List<EntityColumn> colonnes) {
        LoaderLocalImportData task = new LoaderLocalImportData();
        TV_REMOTE.getItems().clear();
        PROGRESS_LOCAL.progressProperty().unbind();
        PROGRESS_LOCAL.progressProperty().bind(task.progressProperty());
        PROGRESS_MESSAGE_L.textProperty().unbind();
        PROGRESS_MESSAGE_L.setText("Attente de chargement...");
        PROGRESS_MESSAGE_L.textProperty().bind(task.messageProperty());
        task.setTable(TV_TABLE.getSelectionModel().getSelectedItem().getNameTable());
        task.setColonnes(colonnes);
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                ObservableList<ObservableList> result = task.getValue();
                TV_LOCALE.setItems(result);
                listDataLocal = new ArrayList<>(task.listData);
                PROGRESS_LOCAL.progressProperty().unbind();
                PROGRESS_MESSAGE_L.textProperty().unbind();
                PROGRESS_MESSAGE_L.setText("Récupération terminé !");
            }
        }
        );
        if (tLoaderL != null) {
            tLoaderL.interrupt();
        }
        tLoaderL = new Thread(task);
        tLoaderL.setName("Loader L Data");
        tLoaderL.start();
    }

    @FXML
    private void importRemoteData(ActionEvent event) {
        importData();
    }

    public void importData() {
        //avant de demarrer l'importation explicite, mettre une pause sur le service d'importation implicite
        WsSynchro.runningIn = true;
        //méthode de récupération des données distantes
        ImportService service = new ImportService(listRemoteData, TV_TABLE.getSelectionModel().getSelectedItem().getNameTable(), this, false);
        PROGRESS_LABEL.textProperty().unbind();
        PROGRESS_LABEL.setText("");
        PROGRESS.progressProperty().unbind();
        PROGRESS.progressProperty().bind(service.progressProperty());
        PROGRESS_LABEL.textProperty().unbind();
        PROGRESS_LABEL.textProperty().bind(service.messageProperty());
        service.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                Boolean result = service.getValue();
                PROGRESS_LABEL.textProperty().unbind();
                PROGRESS_LABEL.setText("Importation terminé !");
                LAB_RUN_IMPORT.setText("");
                //relance le service  de synchro implicite
                WsSynchro.runningIn = false;
                LymytzEntityClass c = LymytzLoaderEntity.ALLENTITY.get(LymytzLoaderEntity.ALLENTITY.indexOf(new LymytzEntityClass(null, null, TV_TABLE.getSelectionModel().getSelectedItem().getNameTable())));
                countTable(c);
                TV_TABLE.refresh();
            }
        }
        );
        new Thread(service).start();
    }

    public void actualiseMessageImport(String msg) {
        Platform.runLater(() -> {
            LAB_RUN_IMPORT.setText(msg);
        });

    }

    private List<TableBean> getListTable(List<LymytzEntityClass> classes) {
        List<TableBean> re = new ArrayList<>();
        TableBean t;
        for (LymytzEntityClass c : classes) {
            t = new TableBean();
            t.setNameEntity(c.getEntity());
            t.setNameTable(c.getAnotationTable());
            t.setSimpleName(c.getSimpleName());
            t.setNbLocal(0L);
            re.add(t);
        }
        return re;
    }

    private void countDataTable() {

        Thread t;
        t = new Thread(new Runnable() {
            @Override
            public void run() {
//                Long re = 0L;
                String query;
                TableBean item;
                List<EntityColumn> colonnes;
                LymytzLoaderEntity.ALLENTITY.stream().forEach((c) -> {
                    countTable(c);
                });
            }
        });
        t.setName("Count Entity On server...");
        t.start();
    }

    private void countTable(LymytzEntityClass c) {
        List<EntityColumn> colonnes;
        String query;
        TableBean item;
        colonnes = LymytzLoaderEntity.loadEntityColumn(c.getEntity());
        query = "SELECT COUNT(*) FROM " + c.getAnotationTable();
        final Long re = (Long) dao.findOneObjectBySQLQ(query, new Options[]{});
        final Long Rre = Rdao.countElement(c.getAnotationTable(), colonnes);
        //met à jour le modèle
        item = new TableBean();
        item.setNameTable(c.getAnotationTable());
        final int idx = TV_TABLE.getItems().indexOf(item);
        if (idx >= 0) {
            Platform.runLater(() -> {
                long N = (Rre != null ? Rre : 0L) - (re != null ? re : 0L);
                TV_TABLE.getItems().get(idx).setNbLocal(N < 0 ? 0 : N);
            });
        }
    }

    private void openDlgToLinkData(Long idLocal, String table) {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Liason de données");
        dlg.setHeaderText("Entrez l'identifiant distant de la ligne de liaison");
        dlg.setContentText("Identifiant distant");
        Optional re = dlg.showAndWait();
        re.ifPresent((Object t) -> {
            if (Constantes.asString(t.toString())) {
                Long idDistant = Long.valueOf(t.toString());
                System.err.println("Résultat " + idDistant);
                //vérifie que la valeur entrez correspond bien à une ligne sur le serveur distant
                WsSynchro ws = new WsSynchro();
                if (ws.pingElementOnserver(idDistant, table)) {
                    Long id = dao.insertListenData(idLocal, table, null, idDistant, false);
                    LymytzService.success();
                } else {
                    LymytzService.openAlertDialog("Erreur ", "Liaison impossible!", "Aucune entité distante n'a été trouvé avec cet identifiant", Alert.AlertType.ERROR);
                }
            }
        });
    }

    public void openViewFilter(List<EntityColumn> colonnes) {
        //Ouvre la fenêtre de gestion des imports
        VBox root = null;
        CustomWindow w = LymytzService.openWindowNew("main/synchro/import_filter.fxml", "Lymytz /Importation/Paramètres", root, 400.0, 350.0, false);
        ImportFilterController controler = (ImportFilterController) w.getController();
        if (controler != null) {
            fenetre = w.getStage();
            controler.initComposant(this, colonnes, fenetre);
        }
    }

    public void freeMemoryController() {
        if (listDataLocal != null) {
            listDataLocal.clear();
        }
        if (listRemoteData != null) {
            listRemoteData.clear();
        }
        TV_LOCALE = null;
        TV_REMOTE = null;
        if (tLoaderL != null) {
            tLoaderL.interrupt();
        }
        if (tLoaderR != null) {
            tLoaderR.interrupt();
        }
        System.gc();
        long memoire2 = Runtime.getRuntime().freeMemory();
    }
}
