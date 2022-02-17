    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lymytz.dao.Options;
import lymytz.dao.UtilsBean;
import lymytz.dao.entity.YvsComCreneauHoraireUsers;
import lymytz.dao.entity.YvsComEnteteDocVente;
import lymytz.service.application.bean.ContenuDocStock;
//import lymytz.service.application.bean.ContenuDocStock;
import lymytz.service.application.bean.HeaderDoc;
import lymytz.service.application.bean.Planning;
import lymytz.service.application.composant.factory.BCellActive;
import lymytz.service.application.composant.factory.BCellClose;
import lymytz.service.application.composant.factory.BCellInit;
import lymytz.service.application.composant.factory.ButtonCellFactory;
import lymytz.service.application.composant.factory.IButtonCellFactory;
import lymytz.service.application.loader.LoaderFiches;
import lymytz.service.application.loader.LoaderPlanning;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import lymytz.view.main.HomeCaisseController;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class MyComptesController extends ManagedApplication implements Initializable, Controller {

    public HomeCaisseController mainControler;
    IButtonCellFactory factoryButtonCell = new ButtonCellFactory();

    private ObservableList listeCreneaux;

    @FXML
    private TableView<Planning> TAB_PLANING;
    @FXML
    private TableColumn<Planning, String> COL_DATE;
    @FXML
    private TableColumn<Planning, String> COL_PV;
    @FXML
    private TableColumn<Planning, String> COL_TRANCHE;
    @FXML
    private TableColumn<Planning, Boolean> COL_ACTIF;
    @FXML
    private TableColumn<Planning, Long> COL_OP;

    private ObservableList dataPlaning;
    @FXML
    private TableView<HeaderDoc> TAB_FICHES;
    @FXML
    private TableColumn<HeaderDoc, String> DATE_FICHE;
    @FXML
    private TableColumn<HeaderDoc, String> PV_FICHE;
    @FXML
    private TableColumn<HeaderDoc, String> TRANCHE_FICHE;
    @FXML
    private TableColumn<HeaderDoc, String> DATE_EDIT;
    @FXML
    private TableColumn<HeaderDoc, Boolean> STATUT_FICHE;
    @FXML
    private TableColumn<HeaderDoc, Long> COL_CLOSE; // Bouton option pour clôturer une fiche
    @FXML
    private TableColumn<HeaderDoc, Long> COL_ACTIVE;// Bouton option pour changer la fiche active

//    private ObservableList<HeaderDoc> dataHeaders;
    @FXML
    protected Label F_POINT_VENTE;
    @FXML
    protected Label F_NB_FACT_SAVE;
    @FXML
    protected Label F_VST_ATTENDU;
    @FXML
    protected Label F_NUM_BEST_VENTE;
    @FXML
    protected Label F_NB_CMDE_SAVE;
    @FXML
    protected Label F_NB_TICKET_RECU;
    @FXML
    protected Label F_SOLDE_CAISSE;
    @FXML
    protected Label F_CLIENT_BEST_VENTE;
    @FXML
    protected Label F_NB_FAC_ATTENTE;
    @FXML
    protected Label F_NB_CMDE_NON_LIVRE;
    @FXML
    public Label F_FACT_VALIDE;
    @FXML
    public Label F_FACT_LIVRE;
    @FXML
    public Label F_FACT_REGLE;
    @FXML
    public Label F_CMDE_VALIDE;
    @FXML
    public Label F_CMDE_LIVRE;
    @FXML
    public Label F_CMDE_REGLE;
    @FXML
    public Label F_NB_VALIDE;
    @FXML
    public Label F_NB_LIVRE;
    @FXML
    public Label F_NB_REGLE;
    @FXML
    public Label C_NB_VALIDE;
    @FXML
    public Label C_NB_LIVRE;
    @FXML
    public Label C_NB_REGLE;
    @FXML
    protected VBox ZONE_NEW;
    @FXML
    protected ComboBox<Planning> CB_CRENO;
    @FXML
    public DatePicker DATE;
    @FXML
    private TreeTableView<ContenuDocStock> TAB_APPRO;
    @FXML
    private TreeTableColumn<ContenuDocStock, String> COL_REF_ART;
    @FXML
    private TreeTableColumn<ContenuDocStock, String> COL_DESIGNATION;
    @FXML
    private TreeTableColumn<ContenuDocStock, Double> COL_QUANTITE_E;
    @FXML
    private TreeTableColumn<ContenuDocStock, String> COL_UNITE_E;
    @FXML
    private TreeTableColumn<ContenuDocStock, Date> COL_DATE_T;
    @FXML
    private TreeTableColumn<ContenuDocStock, String> COL_TRANCHE_S;
    @FXML
    private TreeTableColumn<ContenuDocStock, Double> COL_PUV;
    @FXML
    private TreeTableColumn<ContenuDocStock, String> COL_DEPOT;
    @FXML
    private TreeTableColumn<ContenuDocStock, String> COL_NUM_DOC;
    @FXML
    private TreeTableColumn<ContenuDocStock, String> COL_STATUT;
    @FXML
    private TreeTableColumn<ContenuDocStock, Long> COL_ACTION;

    private ObservableList dataTransfert;

    public void setMainController(HomeCaisseController appc) {
        mainControler = appc;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (UtilsProject.currentUser != null) {
            initDataPlanning();
            initDataFiches();
            initColumnTablePlanning();
            initColumnTableHeader();
            // Si l'utilisateur est en planning permanent, lui donner la possibilité d'initialiser une nouvelle fiche
            List<YvsComCreneauHoraireUsers> l = dao.loadByNamedQuery("YvsComCreneauHoraireUsers.findPointPermanentByUser", new String[]{"users"}, new Object[]{UtilsProject.currentUser.getUsers()});
            if (l != null ? !l.isEmpty() : false) {
                listeCreneaux = FXCollections.observableList(buildPlanning(l));
                CB_CRENO.setItems(listeCreneaux);
                ZONE_NEW.setVisible(true);
            } else {
                ZONE_NEW.setVisible(false);
            }
            displayStatHeader(UtilsProject.headerDoc);
        }else{
         LymytzService.openAlertDialog("Vous devez être connecté avec un compte utilisateur valide", "Impossible d'ouvrir la page", "Erreur", Alert.AlertType.WARNING);
        }
    }

    MyComptesController current;

    private void initColumnTablePlanning() {
        current = this;
        COL_PV.setCellValueFactory(new PropertyValueFactory("pointVente"));
        COL_TRANCHE.setCellValueFactory(new PropertyValueFactory("tranche"));
//        COL_ACTIF.setCellValueFactory(new PropertyValueFactory("actif"));
        COL_OP.setCellValueFactory(new PropertyValueFactory("id"));
        COL_DATE.setCellValueFactory(new PropertyValueFactory("date"));
        COL_ACTIF.setCellValueFactory((TableColumn.CellDataFeatures<Planning, Boolean> param) -> {
            Planning p = param.getValue();
            SimpleBooleanProperty val = new SimpleBooleanProperty(p.getActif());
            val.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                p.setActif(newValue);
            });
            return val;
        });
        COL_ACTIF.setCellFactory((TableColumn<Planning, Boolean> param) -> {
            CheckBoxTableCell<Planning, Boolean> cell = new CheckBoxTableCell();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        COL_OP.setCellFactory((TableColumn<Planning, Long> param) -> {
            BCellInit cell = factoryButtonCell.getBCellInit("Initier une Fiche");
            cell.setMainContoler(current);
            cell.setDate(Constantes.localDateToDate(DATE.getValue()));
            return cell;
        });
    }

    private void initColumnTableHeader() {
        current = this;
        PV_FICHE.setCellValueFactory(new PropertyValueFactory("pointVente"));
        TRANCHE_FICHE.setCellValueFactory(new PropertyValueFactory("tranche"));
        COL_CLOSE.setCellValueFactory(new PropertyValueFactory("id"));
        COL_ACTIVE.setCellValueFactory(new PropertyValueFactory("idPlus"));
        DATE_FICHE.setCellValueFactory(new PropertyValueFactory("date"));
        DATE_EDIT.setCellValueFactory(new PropertyValueFactory("dateSave"));
        STATUT_FICHE.setCellValueFactory((TableColumn.CellDataFeatures<HeaderDoc, Boolean> param) -> {
            HeaderDoc he = param.getValue();
            SimpleBooleanProperty val = new SimpleBooleanProperty(he.getCloturer());
            val.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                he.setCloturer(newValue);
            });
            return val;
        });
        STATUT_FICHE.setCellFactory((TableColumn<HeaderDoc, Boolean> param) -> {
            CheckBoxTableCell<HeaderDoc, Boolean> cell = new CheckBoxTableCell();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        COL_CLOSE.setCellFactory((TableColumn<HeaderDoc, Long> param) -> {
            BCellClose cell = factoryButtonCell.getBCellClose("Clôturer");
            cell.setMainControler(mainControler);
            return cell;
        });
        COL_ACTIVE.setCellFactory((TableColumn<HeaderDoc, Long> param) -> {
            BCellActive cell = factoryButtonCell.getBCellActive("Activer");
            cell.setMainControler(mainControler);
            cell.setCompteController(current);
            return cell;
        });
    }

    public void initTableTransfert() {
    }

    private void initDataPlanning() {
        LoaderPlanning task = new LoaderPlanning();
        TAB_PLANING.getItems().clear();
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (Event event) -> {
            ObservableList<Planning> result = task.getValue();
            TAB_PLANING.setItems(result);
        });
        Thread tLoaderR = new Thread(task);
        tLoaderR.setName("Loader Planning");
        tLoaderR.start();
    }

    private void initDataFiches() {

        LoaderFiches task = new LoaderFiches();
        TAB_FICHES.getItems().clear();
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (Event event) -> {
            ObservableList<HeaderDoc> result = task.getValue();
            TAB_FICHES.setItems(result);
        });
        Thread tLoaderR = new Thread(task);
        tLoaderR.setName("Loader Fiches vente");
        tLoaderR.start();
    }

    @FXML
    protected void createNewFiche(ActionEvent ev) {
        if (DATE.getValue() != null && CB_CRENO.getValue() != null) {
            YvsComEnteteDocVente header = createNewFicheFromCreneaux(CB_CRENO.getValue().getId(), Constantes.localDateToDate(DATE.getValue()));
            if (header != null) {
                HeaderDoc p = new HeaderDoc();
                p.setCloturer(header.getCloturer());
                p.setDate(Constantes.dfD.format(header.getDateEntete()));
                p.setId(header.getId());
                p.setIdPlus(header.getId());
                p.setPointVente(header.getCreneau().getCreneauPoint().getPoint().getLibelle());
                p.setTranche(header.getCreneau().getCreneauPoint().getTranche().getTitre());
                p.setDateSave(Constantes.dfh.format(header.getDateSave()));
                if (!TAB_FICHES.getItems().isEmpty()) {
                    TAB_FICHES.getItems().add(0, p);
                }
                mainControler.displayPropertiesFiche(header);
            }
        }
    }

    public void changeHeader(Long idHeader, MyComptesController page) {
        YvsComEnteteDocVente header = (YvsComEnteteDocVente) dao.findOneByNQ("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{idHeader});
        if (header != null) {
            UtilsProject.headerDoc = header;
            // display statistique
            mainControler.displayPropertiesFiche(header);
            displayStatHeader(header);
        } else {
            LymytzService.openAlertDialog("Aucune lignne selectionné", "Ereur selection", "Erreur", Alert.AlertType.WARNING);
        }
    }

    private List<Planning> buildPlanning(List<YvsComCreneauHoraireUsers> l) {
        List<Planning> re = new ArrayList<>();
        Planning p;
        for (YvsComCreneauHoraireUsers c : l) {
            p = new Planning();
            p.setId(c.getId());
            p.setActif(Boolean.TRUE);
            p.setDate(Constantes.dfD.format(new Date()));
            p.setPointVente(c.getCreneauPoint().getPoint().getLibelle());
            p.setTranche(c.getCreneauPoint().getTranche().getTitre());
            re.add(p);
        }
        return re;
    }
    TreeItem<ContenuDocStock> rootitem;

    public void displayStatHeader(YvsComEnteteDocVente header) {
        if (header != null) {
            String query;
            Long re;
            List<Object[]> result;
            List<String> types = new ArrayList<>();
            types.add(Constantes.TYPE_FV);
            types.add(Constantes.TYPE_BCV);
            //2. CA facture
            Double totalFacture = UtilsBean.getTotalFacturesHeader(header.getId());
            Double totalCmde = UtilsBean.getTotalCommandeHeader(header.getId());
            F_NB_FACT_SAVE.setText(Constantes.nbf.format(totalFacture));
            //2. Nombre de commandes
            F_NB_CMDE_SAVE.setText(Constantes.nbf.format(totalCmde));
            //2. Nombre de clients reçu
            re = (Long) dao.findOneObjectByNQ("YvsComDocVentes.countAllFacturesByHeader", new String[]{"typesDoc", "typesDoc1", "header"}, new Object[]{Constantes.TYPE_FV, Constantes.TYPE_BCV, header});
            re = (re != null) ? re : 0;
            F_NB_TICKET_RECU.setText(Constantes.nbf.format(re));
            //2. Meilleur vente 
            query = "SELECT SUM(c.prix_total) AS somme, d.num_doc, d.nom_client from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on d.id=c.doc_vente "
                    + "WHERE d.type_doc IN ('FV','BCV') AND d.entete_doc=? "
                    + "GROUP BY d.num_doc, d.nom_client ORDER BY somme desc LIMIT 1 ";
            result = dao.loadBySQLQuery(query, new Options[]{new Options(header.getId(), 1)});
            if (result != null ? !result.isEmpty() : false) {
                F_NUM_BEST_VENTE.setText("( " + Constantes.nbf.format(result.get(0)[0]) + " ) -> " + result.get(0)[1]);
                //2. Client ayant réalisé la Meilleur vente 
                F_CLIENT_BEST_VENTE.setText("" + result.get(0)[2]);
            }
            if (UtilsProject.caisse != null) {
                double vestAttendu = UtilsBean.getVersementAttenduHeader(header.getId());
                F_VST_ATTENDU.setText(Constantes.nbf.format(vestAttendu));
            } else {
                F_VST_ATTENDU.setText("Aucune caisse trouvéé!");
            }
            Double val = null;
            //Total Facture validé
            val = (Double) dao.findOneObjectByNQ("YvsComContenuDocVente.findTotalByTypeDocAndHeader", new String[]{"header", "typeDoc"}, new Object[]{header, Constantes.TYPE_FV});
            F_FACT_VALIDE.setText(Constantes.nbf.format(val != null ? val : 0));
            re = (Long) dao.findOneObjectByNQ("YvsComDocVentes.countAllFacturesByHeader1", new String[]{"typesDoc", "header"}, new Object[]{Constantes.TYPE_FV, header});
            re = (re != null) ? re : 0;
            F_NB_VALIDE.setText(" (" + Constantes.nbf.format(re) + ")");
            //Total Facture Livré
            val = (Double) dao.findOneObjectByNQ("YvsComContenuDocVente.findTotalByTypeDocAndLivre", new String[]{"header", "typeDoc", "statutL"}, new Object[]{header, Constantes.TYPE_FV, Constantes.ETAT_LIVRE});
            F_FACT_LIVRE.setText(Constantes.nbf.format(val != null ? val : 0));
            re = (Long) dao.findOneObjectByNQ("YvsComDocVentes.countAllFacturesByHeaderL", new String[]{"typesDoc", "header", "statutL"}, new Object[]{Constantes.TYPE_FV, header, Constantes.ETAT_LIVRE});
            re = (re != null) ? re : 0;
            F_NB_LIVRE.setText(" (" + Constantes.nbf.format(re) + ")");
            //Total Facture Régle
            val = (Double) dao.findOneObjectByNQ("YvsComContenuDocVente.findTotalByTypeDocAndRegle", new String[]{"header", "typeDoc", "statutR"}, new Object[]{header, Constantes.TYPE_FV, Constantes.ETAT_REGLE});
            F_FACT_REGLE.setText(Constantes.nbf.format(val != null ? val : 0));
            re = (Long) dao.findOneObjectByNQ("YvsComDocVentes.countAllFacturesByHeaderR", new String[]{"typesDoc", "header", "statutR"}, new Object[]{Constantes.TYPE_FV, header, Constantes.ETAT_REGLE});
            re = (re != null) ? re : 0;
            F_NB_REGLE.setText(" (" + Constantes.nbf.format(re) + ")");
            //Total cmde validé
            val = (Double) dao.findOneObjectByNQ("YvsComContenuDocVente.findTotalByTypeDocAndHeader", new String[]{"header", "typeDoc"}, new Object[]{header, Constantes.TYPE_BCV});
            F_CMDE_VALIDE.setText(Constantes.nbf.format(val != null ? val : 0));
            re = (Long) dao.findOneObjectByNQ("YvsComDocVentes.countAllFacturesByHeader1", new String[]{"typesDoc", "header"}, new Object[]{Constantes.TYPE_BCV, header});
            re = (re != null) ? re : 0;
            C_NB_VALIDE.setText(" (" + Constantes.nbf.format(re) + ")");
            //Total cmde Livré
            val = (Double) dao.findOneObjectByNQ("YvsComContenuDocVente.findTotalByTypeDocAndLivre", new String[]{"header", "typeDoc", "statutL"}, new Object[]{header, Constantes.TYPE_BCV, Constantes.ETAT_LIVRE});
            F_CMDE_LIVRE.setText(Constantes.nbf.format(val != null ? val : 0));
            re = (Long) dao.findOneObjectByNQ("YvsComDocVentes.countAllFacturesByHeaderL", new String[]{"typesDoc", "header", "statutL"}, new Object[]{Constantes.TYPE_BCV, header, Constantes.ETAT_LIVRE});
            re = (re != null) ? re : 0;
            C_NB_LIVRE.setText(" (" + Constantes.nbf.format(re) + ")");
            //Total cmde Régle
            val = (Double) dao.findOneObjectByNQ("YvsComContenuDocVente.findTotalByTypeDocAndRegle", new String[]{"header", "typeDoc", "statutR"}, new Object[]{header, Constantes.TYPE_BCV, Constantes.ETAT_REGLE});
            F_CMDE_REGLE.setText(Constantes.nbf.format(val != null ? val : 0));
            re = (Long) dao.findOneObjectByNQ("YvsComDocVentes.countAllFacturesByHeaderR", new String[]{"typesDoc", "header", "statutR"}, new Object[]{Constantes.TYPE_BCV, header, Constantes.ETAT_REGLE});
            re = (re != null) ? re : 0;
            C_NB_REGLE.setText(" (" + Constantes.nbf.format(re) + ")");
        }
    }

    @Override
    public void freeMemoryController() {
        TAB_PLANING = null;
        TAB_APPRO = null;
        TAB_FICHES = null;
    }

}
