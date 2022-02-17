/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.synchro.export;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lymytz.dao.entity.service.EntityColumn;
import lymytz.dao.entity.service.LymytzData;
import lymytz.dao.entity.service.LymytzEntityClass;
import lymytz.dao.entity.service.LymytzLoaderEntity;
import lymytz.dao.query.LQueryFactories;
import lymytz.dao.query.RQueryFactories;
import lymytz.service.application.Controller;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class ExportDataController implements Initializable, Controller {

    RQueryFactories dao = new RQueryFactories();
    public List<LymytzData> listDataLocal;

    /**/
    @FXML
    private Label LAB_NAME_TABLE;
    @FXML
    private TableView<LymytzEntityClass> TV_TABLE;
    @FXML
    private TableColumn<LymytzEntityClass, String> C_TABLE;
    @FXML
    private TableColumn<LymytzEntityClass, Double> C_MAJ;

    @FXML
    public ProgressBar PROGRESS_LOCAL;
    @FXML
    public Label PROGRESS_MESSAGE_L;
    @FXML
    public ProgressBar PROGRESS;
    @FXML
    public Label PROGRESS_LABEL;

    /**/
    public TableView TV_LOCALE;

    /**/
    @FXML
    private VBox BOX_TAB_LOCALE;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // charge la liste
        new LymytzLoaderEntity<>(false);
        ObservableList items = FXCollections.observableArrayList(LymytzLoaderEntity.ALLENTITY);
        TV_TABLE.setItems(items);
        initComponents();
    }

    public void initComponents() {
        MenuItem addToListen = new MenuItem("Mettre dans la file...");
        ContextMenu context = new ContextMenu(addToListen);
        TV_TABLE.setEditable(true);
        C_TABLE.setCellValueFactory(
                new PropertyValueFactory<>("simpleName")
        );
        C_TABLE.setEditable(true);
        C_MAJ.setCellValueFactory(new PropertyValueFactory<>("NbAction"));
        //selection d'une table
        TV_TABLE.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<LymytzEntityClass>() {

            public void onChanged(ListChangeListener.Change<? extends LymytzEntityClass> c) {
                LAB_NAME_TABLE.setText(TV_TABLE.getSelectionModel().getSelectedItem().getSimpleName());
                TV_LOCALE = new TableView<>();
                TV_LOCALE.getColumns().clear();
                //récupération des colonne à partir du nom de la Classe Entity
                List<EntityColumn> colonnes = LymytzLoaderEntity.loadEntityColumn(TV_TABLE.getSelectionModel().getSelectedItem().getEntity());
                //initialise la première colonne
                TableColumn col = new TableColumn("N°");
                col.setPrefWidth(50);
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(0).toString());
                    }
                });
                TV_LOCALE.getColumns().add(col);
                TableColumn col1 = new TableColumn("Id Synchro");
                col1.setPrefWidth(50);
                col1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(1).toString());
                    }
                });
                TV_LOCALE.getColumns().add(col1);
                TableColumn col2 = new TableColumn("Id Distant");
                col2.setPrefWidth(50);
                col2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(2).toString());
                    }
                });

                TV_LOCALE.getColumns().add(col2);
                col2 = new TableColumn("Id Listen");
                col2.setPrefWidth(50);
                col2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(3).toString());
                    }
                });

                TV_LOCALE.getColumns().add(col2);
                col2 = new TableColumn("Action");
                col2.setPrefWidth(50);
                col2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(4).toString());
                    }
                });

                TV_LOCALE.getColumns().add(col2);
                for (int i = 5; i < colonnes.size() + 4; i++) {
                    final int j = i;
                    col2 = new TableColumn(colonnes.get(i - 5).getName());
                    col2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                    TV_LOCALE.getColumns().add(col2);
                }
                //instancie le thread de chargement des données 
                TV_LOCALE.setContextMenu(context);
                loadLocalDataLocal(colonnes);
                BOX_TAB_LOCALE.getChildren().clear();
                BOX_TAB_LOCALE.getChildren().add(TV_LOCALE);
            }
        });
        addToListen.setOnAction((ActionEvent ev) -> {
            ObservableList o = (ObservableList) TV_LOCALE.getSelectionModel().getSelectedItem();
            if (o.get(3) != null ? !((String) o.get(3)).trim().isEmpty() : false) {
                LymytzService.openAlertDialog("La ligne est déjà présente dans le listen " + o.get(3), "Insertion non nécessaire", "Warning... ", Alert.AlertType.WARNING);
            } else {
                //ajouter listen
                LQueryFactories daol = new LQueryFactories();
                if (Constantes.asLong(daol.insertListenData(Long.valueOf((String) o.get(5)), TV_TABLE.getSelectionModel().getSelectedItem().getAnotationTable(), UtilsProject.currentUser.getId(), 0L, true))) {
                    LymytzService.success();
                }
            }
        });
    }

    private void loadLocalDataLocal(List<EntityColumn> colonnes) {
        LoaderLocalExport task = new LoaderLocalExport();
        PROGRESS_LOCAL.progressProperty().unbind();
        PROGRESS_LOCAL.progressProperty().bind(task.progressProperty());
        PROGRESS_MESSAGE_L.textProperty().unbind();
        PROGRESS_MESSAGE_L.setText("Attente de chargement...");
        PROGRESS_MESSAGE_L.textProperty().bind(task.messageProperty());
        task.setTable(TV_TABLE.getSelectionModel().getSelectedItem().getAnotationTable());
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
        new Thread(task).start();
//        try {
//            task.call();
//        } catch (Exception ex) {
//            Logger.getLogger(ExportDataController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @FXML
    private void exportRemoteData(ActionEvent event) {
        //méthode de récupération des données distantes
        //Lance la requête dans une tread
        ExportService service = new ExportService("IHM", listDataLocal, this);
        try {
            PROGRESS.setProgress(0);
            PROGRESS_LABEL.textProperty().unbind();
            PROGRESS_LABEL.setText("");
            PROGRESS.progressProperty().unbind();
            PROGRESS.progressProperty().bind(service.progressProperty());
            PROGRESS_LABEL.textProperty().unbind();
            PROGRESS_LABEL.textProperty().bind(service.messageProperty());
            service.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    Object result = service.getValue();
                    PROGRESS_LABEL.textProperty().unbind();
                    PROGRESS_LABEL.setText(service.getListData().size() + " /Synchronisation terminé ! ");
                }
            }
            );
            new Thread(service).start();
        } catch (Exception ex) {
            Logger.getLogger(ExportDataController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void freeMemoryController() {
        if (listDataLocal != null) {
            listDataLocal.clear();
        }
        TV_LOCALE = null;
    }

}
