/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application;

import lymytz.service.application.loader.LoaderCatalogue;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lymytz.service.application.bean.Articles;

/**
 * FXML Controller class
 *
 * @author LYMYTZ
 */
public class FormCatalogueController implements Initializable, Controller {

    @FXML
    private TableView<Articles> TV_ARTICLE;
    @FXML
    private TableColumn<Articles, Integer> C_NUM;
    @FXML
    private TableColumn<Articles, String> C_REF;
    @FXML
    private TableColumn<Articles, String> C_DESIGNATION;
    @FXML
    private TableColumn<Articles, String> C_UNITE;
    @FXML
    private TableColumn<Articles, Double> C_PUA;
    @FXML
    private TableColumn<Articles, Double> C_PUV;
    @FXML
    private TableColumn<Articles, Double> C_PMIN;
    @FXML
    private TableColumn<Articles, String> C_CATEGORIE;
    @FXML
    private TableColumn<Articles, String> C_FAMILLE;
    @FXML
    private TableColumn<Articles, String> C_GROUP;
    @FXML
    private TableColumn<Articles, String> C_CLASSE;
    @FXML
    private TableColumn<Articles, Boolean> C_STATUT;
    @FXML
    private Label PROGRESS_LABEL;
    @FXML
    private ProgressBar PROGRESS;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initComponent();
        LoaderCatalogue task = new LoaderCatalogue();
        TV_ARTICLE.getItems().clear();
        PROGRESS.progressProperty().unbind();
        PROGRESS_LABEL.textProperty().unbind();
        PROGRESS.progressProperty().bind(task.progressProperty());
        PROGRESS_LABEL.textProperty().bind(task.messageProperty());
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent event) {
                ObservableList<Articles> result = task.getValue();
                TV_ARTICLE.setItems(result);
                PROGRESS_LABEL.textProperty().unbind();
                PROGRESS_LABEL.setText("terminé !");
            }
        });
        new Thread(task).start();
    }

    public void initComponent() {
        C_REF.setCellValueFactory(
                new PropertyValueFactory<>("reference")
        );
        C_NUM.setCellValueFactory(
                new PropertyValueFactory<>("num")
        );
        C_DESIGNATION.setCellValueFactory(
                new PropertyValueFactory<>("designation")
        );
        C_UNITE.setCellValueFactory(
                new PropertyValueFactory<>("unite")
        );
        C_CATEGORIE.setCellValueFactory(
                new PropertyValueFactory<>("categorie")
        );
        C_FAMILLE.setCellValueFactory(
                new PropertyValueFactory<>("famille")
        );
        C_GROUP.setCellValueFactory(
                new PropertyValueFactory<>("groupe")
        );
        C_CLASSE.setCellValueFactory(
                new PropertyValueFactory<>("classe")
        );
        C_PMIN.setCellValueFactory(
                new PropertyValueFactory<>("prixMin")
        );
        C_PUA.setCellValueFactory(
                new PropertyValueFactory<>("pua")
        );
        C_PUV.setCellValueFactory(
                new PropertyValueFactory<>("puv")
        );
        C_STATUT.setCellValueFactory(
                new PropertyValueFactory<>("actif")
        );
    }

    @Override
    public void freeMemoryController() {
    }

}
