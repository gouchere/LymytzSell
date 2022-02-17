/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.synchro;

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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import lymytz.dao.Options;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.dao.query.LQueryFactories;
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
public class ListenTableController implements Initializable, Controller {

    HomeCaisseController page;
    ObservableList<ListenTableBean> items = FXCollections.observableArrayList();

    @FXML
    private ProgressBar PROGRESS;
    @FXML
    private Label PROGRESS_LABEL;
    @FXML
    private Hyperlink LINK_CONTROL;

    ContextMenu CONTEXT;
    @FXML
    private Label NB_WAIT;
    @FXML
    private Label NB_ERROR;
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
        loadDataListen(false);
        LINK_CONTROL.setVisible(false);
    }

    public void initPage(HomeCaisseController page) {
        this.page = page;
        CONTEXT = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh ");
        MenuItem view = new MenuItem("Afficher ");
        MenuItem itemCancel = new MenuItem("Ne plus synchroniser ");
        MenuItem itemSynchro = new MenuItem("Remettre dans la file");
        MenuItem idisplayAll = new MenuItem("Tout Afficher");
        MenuItem iCancelAll = new MenuItem("Ne plus synchroniser tout");
        MenuItem iResynchroAll = new MenuItem("Tout Remettre dans la file");
        CONTEXT.getItems().add(refresh);
        CONTEXT.getItems().add(view);
        CONTEXT.getItems().add(new SeparatorMenuItem());
        CONTEXT.getItems().add(itemSynchro);
        CONTEXT.getItems().add(itemCancel);
        CONTEXT.getItems().add(new SeparatorMenuItem());
        CONTEXT.getItems().add(idisplayAll);
        CONTEXT.getItems().add(iResynchroAll);
        CONTEXT.getItems().add(iCancelAll);

        TAB_LISTEN.setContextMenu(CONTEXT);
        count();
        itemSynchro.setOnAction((ActionEvent ev) -> {
            ListenTableBean row = TAB_LISTEN.getSelectionModel().getSelectedItem();
            try {
                if (row != null) {
                    String query = "UPDATE yvs_synchro_listen_table SET to_listen=TRUE, message=NULL, nb_failed=0 WHERE id= ? ";
                    if (Constantes.asLong(page.dao.executeSqlQuery(query, new Options[]{new Options(row.getId(), 1)}))) {
                        row.setMessage(null);
                        TAB_LISTEN.getItems().get(items.indexOf(row)).setMessage(null);
                        TAB_LISTEN.refresh();
                    }
                }
            } catch (Exception ex) {
                LogFiles.addLogInFile("", ex);
                Logger.getLogger(ListenTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        iResynchroAll.setOnAction((ActionEvent ev) -> {
            ListenTableBean row = TAB_LISTEN.getSelectionModel().getSelectedItem();
            try {
                if (row != null) {
                    String query = "UPDATE yvs_synchro_listen_table SET to_listen=TRUE, message=NULL, nb_failed=0 WHERE (message!='OK' AND message IS NOT NULL AND to_listen IS TRUE) "
                            + "AND name_table IN ( " + Constantes.TABLES_TO_SYNCHRO + " )";
                    page.dao.executeSqlQuery(query, new Options[]{});
                    TAB_LISTEN.getItems().stream().forEach((_item) -> {
                        row.setMessage(null);
                    });
                    TAB_LISTEN.getItems().stream().filter((line) -> (Constantes.asString(line.getMessage()))).forEach((line) -> {
                        line.setMessage(null);
                    });
                    TAB_LISTEN.refresh();
                }
            } catch (Exception ex) {
                LogFiles.addLogInFile("", ex);
                Logger.getLogger(ListenTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        itemCancel.setOnAction((ActionEvent ev) -> {
            ListenTableBean row = TAB_LISTEN.getSelectionModel().getSelectedItem();
            try {
                if (row != null) {
                    String query = "UPDATE yvs_synchro_listen_table SET to_listen=FALSE, message='Annulé par un utilisateur' WHERE id= ?";
                    if (Constantes.asLong(page.dao.executeSqlQuery(query, new Options[]{new Options(row.getId(), 1)}))) {
                        row.setMessage(null);
                        TAB_LISTEN.getItems().remove(row);
                        TAB_LISTEN.refresh();
                    }
                }
            } catch (Exception ex) {
                LogFiles.addLogInFile("", ex);
                Logger.getLogger(ListenTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        iCancelAll.setOnAction((ActionEvent ev) -> {
            ListenTableBean row = TAB_LISTEN.getSelectionModel().getSelectedItem();
            try {
                String query = "UPDATE yvs_synchro_listen_table SET to_listen=FALSE, message='Annulé par un utilisateur' WHERE (to_listen IS TRUE AND (message!='OK' AND message IS NOT NULL)) "
                        + "AND name_table IN ( " + Constantes.TABLES_TO_SYNCHRO + " )";
                page.dao.executeSqlQuery(query, new Options[]{});
                TAB_LISTEN.getItems().clear();
                TAB_LISTEN.refresh();
            } catch (Exception ex) {
                LogFiles.addLogInFile("", ex);
                Logger.getLogger(ListenTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        refresh.setOnAction((ActionEvent ev) -> {
            loadDataListen(false);
            TAB_LISTEN.refresh();
        });
        idisplayAll.setOnAction((ActionEvent ev) -> {
            loadDataListen(true);
            TAB_LISTEN.refresh();
        });
        LINK_CONTROL.setOnAction((ActionEvent ev) -> {
            String query = "UPDATE yvs_synchro_listen_table SET to_listen=TRUE, message=NULL WHERE to_listen=true AND message IS NOT NULL";
            page.dao.executeSqlQuery(query, new Options[]{});
            TAB_LISTEN.getItems().stream().forEach((r) -> {
                r.setMessage(null);
            });
            TAB_LISTEN.refresh();
        });
        view.setOnAction((ActionEvent ev) -> {
            ListenTableBean row = TAB_LISTEN.getSelectionModel().getSelectedItem();
            try {
                if (row != null) {
                    switch (row.getTable()) {
                        case Constantes.TABLE_DOC_VENTE_CODE:
                            {
                                LQueryFactories dao=new LQueryFactories();
                                YvsComDocVentes d=(YvsComDocVentes) dao.findOneByNQ("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{row.getIdSource()});
                                page.displayFactureOnView(d);
                                break;
                            }
                        case Constantes.TABLE_CONTENT_DOC_VENTE_CODE:
                            {
                                LQueryFactories dao=new LQueryFactories();
                                YvsComDocVentes d=(YvsComDocVentes) dao.findOneByNQ("YvsComContenuDocVente.findDocById", new String[]{"id"}, new Object[]{row.getIdSource()});
                                page.displayFactureOnView(d);
                                break;
                            }
                        case Constantes.TABLE_PIECE_CAISSE_VENTE_CODE:
                            {
                                LQueryFactories dao=new LQueryFactories();
                                YvsComDocVentes d=(YvsComDocVentes) dao.findOneByNQ("YvsComptaCaissePieceVente.findDocById", new String[]{"id"}, new Object[]{row.getIdSource()});
                                page.displayFactureOnView(d);
                                break;
                            }
                    }

                }
            } catch (Exception ex) {
                LogFiles.addLogInFile("", ex);
                Logger.getLogger(ListenTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        LINK_CONTROL.setTooltip(new Tooltip("Remettre toutes les ligne dans la file de synchronization"));
    }

    private void initColumn() {
        C_ID.setCellValueFactory((TableColumn.CellDataFeatures<ListenTableBean, Long> param) -> new SimpleObjectProperty(param.getValue().getId()));
        C_ID_SOURCE.setCellValueFactory((TableColumn.CellDataFeatures<ListenTableBean, Long> param) -> new SimpleObjectProperty(param.getValue().getIdSource()));
        C_TABLE.setCellValueFactory((TableColumn.CellDataFeatures<ListenTableBean, String> param) -> new SimpleObjectProperty(param.getValue().getTable()));
        C_ACTION.setCellValueFactory((TableColumn.CellDataFeatures<ListenTableBean, String> param) -> new SimpleObjectProperty(param.getValue().getAction()));
        C_MESSAGE.setCellValueFactory((TableColumn.CellDataFeatures<ListenTableBean, String> param) -> new SimpleObjectProperty(param.getValue().getMessage()));
        C_DATE_SAVE.setCellValueFactory((TableColumn.CellDataFeatures<ListenTableBean, String> param) -> new SimpleObjectProperty(param.getValue().getDateSave()));

    }

    private void loadDataListen(boolean all) {
        try {
            LoaderListenTable task = new LoaderListenTable(true, all);

            PROGRESS.progressProperty().unbind();
            PROGRESS.progressProperty().bind(task.progressProperty());
            PROGRESS_LABEL.textProperty().unbind();
            PROGRESS_LABEL.textProperty().bind(task.messageProperty());
            task.setOnSucceeded((WorkerStateEvent event) -> {
                Platform.runLater(() -> {
                    items.clear();
                    items.addAll(task.getValue());
                    TAB_LISTEN.setItems(items);
                    PROGRESS_LABEL.textProperty().unbind();
                    PROGRESS_LABEL.setText("terminé !");
                    LINK_CONTROL.setVisible(!items.isEmpty());
                });
            });
            Thread t = new Thread(task);
            t.start();
        } catch (Exception ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(ListenTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void count() {
        String query1 = "SELECT COUNT(id) FROM yvs_synchro_listen_table WHERE to_listen=true ";
        String query2 = "SELECT COUNT(id) FROM yvs_synchro_listen_table WHERE to_listen=true AND message IS NOT NULL";
        //Syncrho en attente
        Long W = (Long) page.dao.findOneObjectBySQLQ(query1, new Options[]{});
        //Syncrho echoué
        Long F = (Long) page.dao.findOneObjectBySQLQ(query2, new Options[]{});
        NB_ERROR.setText(Constantes.nbf.format(F));
        NB_WAIT.setText(Constantes.nbf.format(W));
    }

    @Override
    public void freeMemoryController() {
    }

}
