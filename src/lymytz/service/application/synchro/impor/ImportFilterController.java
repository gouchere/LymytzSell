/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.synchro.impor;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lymytz.dao.entity.service.EntityColumn;
import lymytz.service.application.Controller;
import lymytz.service.application.synchro.ParamQuery;
import lymytz.service.utils.LymytzService;

/**
 * FXML Controller class
 *
 * @author LYMYTZ
 */
public class ImportFilterController implements Initializable, Controller {

    Stage fen;
    ImportDataController page;
    @FXML
    public ComboBox<String> CB_COL;
    @FXML
    public TextField TXT_VAL;
    @FXML
    public ListView<ParamQuery> LIST_PARAM;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void initComposant(ImportDataController page, List<EntityColumn> colonnes, Stage fen) {
        this.page = page;
        this.fen = fen;
        LIST_PARAM.getItems().clear();
        colonnes.stream().forEach((c) -> {
            CB_COL.getItems().add(c.getColumnName());
        });
    }

    @FXML
    public void openFilter(ActionEvent event) {
        try {
            LIST_PARAM.getItems().add(new ParamQuery("y." + CB_COL.getValue(), Long.valueOf(TXT_VAL.getText()), null));
        } catch (NumberFormatException e) {
            LymytzService.openAlertDialog("Valeur Incorrect !", "Erreur valeur", "La valeur entrée est incorrect", Alert.AlertType.ERROR);

        }
    }

    @FXML
    public void importer(ActionEvent event) {
        page.fillDataRemote(LIST_PARAM.getItems());
//        while(page.TV_REMOTE.getItems().isEmpty()){
//            System.err.println("Statut R :"+page.PROGRESS_MESSAGE_R.getText());
//        }
//        page.importData();
        fen.close();
    }

    @Override
    public void freeMemoryController() {
    }

}
