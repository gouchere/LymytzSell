/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.service.report;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.application.Controller;
import lymytz.service.application.bean.ContentPanier;
import lymytz.service.application.bean.Factures;
import lymytz.service.application.loader.LoaderListing;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import lymytz.view.LocalLoader;
import lymytz.view.main.HomeCaisseController;
import lymytz.view.main.ListFacturesController;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class ListingController implements Initializable, Controller {

    HomeCaisseController page;
    LQueryFactories rq = new LQueryFactories();
    ObservableList<ContentPanier> items = FXCollections.observableArrayList();

    ContextMenu CTM_TV = new ContextMenu();

    private List<String> types;

    @FXML
    private TableView<ContentPanier> TABLE_LISTING;
    @FXML
    private TableColumn<ContentPanier, Integer> COL_N;
    @FXML
    private TableColumn<ContentPanier, String> COL_REF;
    @FXML
    private TableColumn<ContentPanier, String> COL_ART;
    @FXML
    private TableColumn<ContentPanier, String> COL_UNITE;
    @FXML
    private TableColumn<ContentPanier, String> COL_CLIENT;
    @FXML
    private TableColumn<ContentPanier, String> COL_TIME;
    @FXML
    private TableColumn<ContentPanier, String> COL_TOTAL;
    @FXML
    private TableColumn<ContentPanier, String> COL_QTE;
    @FXML
    private TableColumn<ContentPanier, String> COL_NUM_DOC;
    @FXML
    private TableColumn<ContentPanier, String> COL_PRIX;
    @FXML
    private TableColumn<ContentPanier, String> COL_TYPE;
    @FXML
    private TableColumn<ContentPanier, Boolean> COL_LIV;
    @FXML
    private TextField F_FACTURE;
    @FXML
    private TextField F_CLIENT;
    @FXML
    private TextField F_ARTICLE;
    @FXML
    private ProgressBar PROGRESS;
    
    public ObservableList<ContentPanier> getItems() {
        return items;
    }

    public void setItems(ObservableList<ContentPanier> items) {
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

    public void initPage(HomeCaisseController page) {
        this.page = page;
        loadDataFactures();
        initInfoSearch();
    }

    private void initColumnData() {
        COL_CLIENT.setCellValueFactory((TableColumn.CellDataFeatures<ContentPanier, String> param) -> new SimpleObjectProperty(param.getValue().getFacture().getNomClient()));
        COL_TIME.setCellValueFactory((TableColumn.CellDataFeatures<ContentPanier, String> param) -> new SimpleObjectProperty((param.getValue().getDateSave()!= null) ? Constantes.dfh.format(param.getValue().getDateSave()) : ""));
        COL_TOTAL.setCellValueFactory((TableColumn.CellDataFeatures<ContentPanier, String> param) -> new SimpleObjectProperty(Constantes.nbf.format(param.getValue().getMontantTotalTTC())));
        COL_QTE.setCellValueFactory((TableColumn.CellDataFeatures<ContentPanier, String> param) -> new SimpleObjectProperty(Constantes.nbf.format(param.getValue().getQuantite())));
        COL_PRIX.setCellValueFactory((TableColumn.CellDataFeatures<ContentPanier, String> param) -> new SimpleObjectProperty(Constantes.nbf.format(param.getValue().getPrix())));
        //COL_N.setCellValueFactory((TableColumn.CellDataFeatures<ContentPanier, Integer> param) -> new SimpleObjectProperty(param.getValue().getNumLine()));
        COL_REF.setCellValueFactory((TableColumn.CellDataFeatures<ContentPanier, String> param) -> new SimpleObjectProperty(param.getValue().getConditionnement().getArticle().getRefArt()));
        COL_ART.setCellValueFactory((TableColumn.CellDataFeatures<ContentPanier, String> param) -> new SimpleObjectProperty(param.getValue().getConditionnement().getArticle().getDesignation()));
        COL_UNITE.setCellValueFactory((TableColumn.CellDataFeatures<ContentPanier, String> param) -> new SimpleObjectProperty(param.getValue().getConditionnement().getUnite().getReference()));
        COL_TYPE.setCellValueFactory((TableColumn.CellDataFeatures<ContentPanier, String> param) -> new SimpleObjectProperty(param.getValue().getFacture().getTypeDoc()));
        COL_NUM_DOC.setCellValueFactory((TableColumn.CellDataFeatures<ContentPanier, String> param) -> new SimpleObjectProperty(param.getValue().getFacture().getNumDoc()));
        COL_LIV.setCellValueFactory((TableColumn.CellDataFeatures<ContentPanier, Boolean> param) -> new SimpleBooleanProperty(param.getValue().getFacture().getStatutLivre().equals(Constantes.ETAT_LIVRE)));

    }

    private void initInfoSearch() {

    }

    private void loadDataFactures() {
        try {
            if (UtilsProject.headerDoc != null) {
                LoaderListing task = new LoaderListing(true);
                PROGRESS.progressProperty().unbind();
                PROGRESS.progressProperty().bind(task.progressProperty());
                task.setOnSucceeded((WorkerStateEvent event) -> {
                    Platform.runLater(() -> {
                        items.clear();
                        items.addAll(task.getValue());
                        TABLE_LISTING.setItems(items);
                    });
                });
                Thread t = new Thread(task);
                t.setName("Loader listing");
                t.start();
            } else {
                LymytzService.openAlertDialog("Veuillez enregistrer l'entête de votre journal", "Aucune entête trouvé", "Aucune entête trouvé", Alert.AlertType.ERROR);
            }
        } catch (Exception ex) {
            Logger.getLogger(ListFacturesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addParamClient(KeyEvent event) {
       
    }

    @FXML
    private void addParamNumDoc(KeyEvent event) {
        
    }

    @FXML
    private void addParamRefArticle(KeyEvent event) {
       
    }

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
            long id = TABLE_LISTING.getSelectionModel().getSelectedItem().getIdContent();
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
        TABLE_LISTING = null;
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
}
