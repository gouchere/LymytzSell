/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.synchro.impor;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lymytz.dao.Options;
import lymytz.service.application.Controller;
import lymytz.service.application.bean.ListenTableBean;
import lymytz.service.application.loader.LoaderListenTable;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.log.LogFiles;
import lymytz.view.main.HomeCaisseController;

/**
 * FXML Controller class
 *
 * @author LYMYTZ
 */
public class ListenRemoteTableController implements Initializable, Controller {

    HomeCaisseController page;
    ObservableList<ListenTableBean> items = FXCollections.observableArrayList();

    @FXML
    private ProgressBar PROGRESS;
    @FXML
    private Label PROGRESS_LABEL;

    ContextMenu CONTEXT;
    @FXML
    private Label NB_WAIT;
    @FXML
    private TableView<ListenTableBean> TAB_LISTEN;
    @FXML
    private TableColumn<ListenTableBean, Long> C_ID;
    @FXML
    private TableColumn<ListenTableBean, Long> C_ID_SOURCE;
    @FXML
    private TableColumn<ListenTableBean, String> C_TABLE;
    @FXML
    private TableColumn<ListenTableBean, String> C_ACTION;
    @FXML
    private TableColumn<ListenTableBean, String> C_MESSAGE;
    @FXML
    private TableColumn<ListenTableBean, String> C_DATE_SAVE;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initColumn();
        loadDataListen();
    }

    public void initPage(HomeCaisseController page) {
        this.page = page;
        CONTEXT = new ContextMenu();
        MenuItem itemSynchro = new MenuItem("Mettre dans la file ");
        MenuItem itemCancel = new MenuItem("Ne plus synchronizer ");
        CONTEXT.getItems().add(itemSynchro);
        CONTEXT.getItems().add(itemCancel);
        TAB_LISTEN.setContextMenu(CONTEXT);
        count();
        itemSynchro.setOnAction((ActionEvent ev) -> {
            ListenTableBean row = TAB_LISTEN.getSelectionModel().getSelectedItem();
            try {
                if (row != null) {
                    String query = "UPDATE yvs_synchro_listen_table SET to_listen=TRUE, message=NULL WHERE id= ?";
                    if (Constantes.asLong(page.dao.executeSqlQuery(query, new Options[]{new Options(row.getId(), 1)}))) {
                        row.setMessage(null);
                        TAB_LISTEN.getItems().get(items.indexOf(row)).setMessage(null);
                        TAB_LISTEN.refresh();
                    }
                }
            } catch (Exception ex) {
                LogFiles.addLogInFile("", ex);
                Logger.getLogger(ListenRemoteTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        itemCancel.setOnAction((ActionEvent ev) -> {
            ListenTableBean row = TAB_LISTEN.getSelectionModel().getSelectedItem();
            try {
                if (row != null) {
                    String query = "UPDATE yvs_synchro_listen_table SET to_listen=FALSE, message='Annulé par un utilisateur' WHERE id= ?";
                    if (Constantes.asLong(page.dao.executeSqlQuery(query, new Options[]{new Options(row.getId(), 1)}))) {
                        row.setMessage(null);
                        TAB_LISTEN.getItems().get(items.indexOf(row)).setMessage(null);
                        TAB_LISTEN.refresh();
                    }
                }
            } catch (Exception ex) {
                LogFiles.addLogInFile("", ex);
                Logger.getLogger(ListenRemoteTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });}

    private void initColumn() {
        C_ID.setCellValueFactory((TableColumn.CellDataFeatures<ListenTableBean, Long> param) -> new SimpleObjectProperty(param.getValue().getId()));
        C_ID_SOURCE.setCellValueFactory((TableColumn.CellDataFeatures<ListenTableBean, Long> param) -> new SimpleObjectProperty(param.getValue().getIdSource()));
        C_TABLE.setCellValueFactory((TableColumn.CellDataFeatures<ListenTableBean, String> param) -> new SimpleObjectProperty(param.getValue().getTable()));
        C_ACTION.setCellValueFactory((TableColumn.CellDataFeatures<ListenTableBean, String> param) -> new SimpleObjectProperty(param.getValue().getAction()));
        C_MESSAGE.setCellValueFactory((TableColumn.CellDataFeatures<ListenTableBean, String> param) -> new SimpleObjectProperty(param.getValue().getMessage()));
        C_DATE_SAVE.setCellValueFactory((TableColumn.CellDataFeatures<ListenTableBean, String> param) -> new SimpleObjectProperty(param.getValue().getDateSave()));

    }

    private void loadDataListen() {
        try {
            LoaderListenTable task = new LoaderListenTable(false,false);
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
                            TAB_LISTEN.setItems(items);
                            PROGRESS_LABEL.textProperty().unbind();
                            PROGRESS_LABEL.setText("terminé !");
                        }
                    });
                }
            });
            Thread t = new Thread(task);
            t.start();
        } catch (Exception ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(ListenRemoteTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void count() {
//        RQueryFactories dao=new RQueryFactories();
//        String query1 = "SELECT COUNT(id) FROM yvs_synchro_listen_table WHERE to_listen=true ";
//        //Syncrho en attente
//        Long W = (Long) dao.loadOneBySQLQuery(query1, new Options[]{});
//        NB_WAIT.setText(Constantes.nbf.format(W));
    }

    @Override
    public void freeMemoryController() {
    }

}
