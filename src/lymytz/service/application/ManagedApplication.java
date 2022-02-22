/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import lymytz.service.application.service.ListenServersRemote;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import lymytz.dao.Options;
import lymytz.dao.ParamConnection;
import lymytz.dao.entity.YvsBaseArticles;
import lymytz.dao.entity.YvsComCreneauHoraireUsers;
import lymytz.dao.entity.YvsComEnteteDocVente;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import lymytz.synchro.ws.ResultatAction;
import lymytz.synchro.ws.WsSynchro;
import lymytz.view.LocalLoader;
import lymytz.view.main.HomeCaisseController;
import lymytzsell.LymytzSell;

/**
 * FXML Controller class
 *
 * @author LENOVO les fonctionnalités metiers partagé de l'application
 */
public class ManagedApplication {

    public LQueryFactories dao = new LQueryFactories();
    private HomeCaisseController mainPage;
    public ListenServersRemote serviceListen;
    private List<Long> idDepots;
    List<String> categories;

    public ManagedApplication() {
        categories = new ArrayList<>();
        categories.add(Constantes.CAT_MP);
        categories.add(Constantes.CAT_PSF);
    }

    public HomeCaisseController getMainPage() {
        return mainPage;
    }

    public void setMainPage(HomeCaisseController mainPage) {
        this.mainPage = mainPage;
    }

    public List<Long> getIdDepots() {
        return idDepots;
    }

    public void setIdDepots(List<Long> idDepots) {
        this.idDepots = idDepots;
    }

    public YvsComEnteteDocVente createNewFicheFromCreneaux(long idCreno, Date date) {
        //Vérifie si le vendeur a déjà ouvert une fiche ce jour
        //récupère les paramètres commerciale
        YvsComCreneauHoraireUsers creno = (YvsComCreneauHoraireUsers) dao.findOneByNQ("YvsComCreneauHoraireUsers.findById", new String[]{"id"}, new Object[]{idCreno});
        date = (date == null && creno != null) ? creno.getDateTravail() : (date == null) ? new Date() : date;
        if (!UtilsProject.verifyDateVente(date)) {
            return null;
        }
        if (creno != null) {
            YvsComEnteteDocVente header = (YvsComEnteteDocVente) dao.findOneByNQ("YvsComEnteteDocVente.findOneFiche", new String[]{"date", "creno"}, new Object[]{date, creno});
            if (header != null) {
                //vérifie que la fiche ne soit pas déjà clôturé
                if (header.getCloturer() || header.getEtat().equals(Constantes.ETAT_CLOTURE)) {
                    LymytzService.openAlertDialog("La fiche de ce planning est déjà clôturé", "information", "Fiche non editable", Alert.AlertType.WARNING);
                    UtilsProject.headerDoc = null;
                } else {
                    LymytzService.openAlertDialog("Vous avez déjà une fiche pour cette journée", "information", "La fiche existe", Alert.AlertType.INFORMATION);
                    UtilsProject.headerDoc = header;
                    LymytzService.success();
                }
            } else {
                header = createHeader(creno, ((date != null) ? date : creno.getDateTravail()));
                UtilsProject.headerDoc = header;
                LymytzService.success();
            }
//            displayPropertiesFiche(header);
            return header;
        } else {
            LymytzService.openAlertDialog("Aucun créno actif n'a été trouvé !", "Objet non trouvé", "Erreur", Alert.AlertType.ERROR);
        }
        return null;
    }

    private YvsComEnteteDocVente createHeader(YvsComCreneauHoraireUsers creno, Date date) {
        YvsComEnteteDocVente head = new YvsComEnteteDocVente();
        head.setAgence(UtilsProject.currentAgence);
        head.setAuthor(UtilsProject.currentUser);
        head.setCloturer(false);
        head.setCreneau(creno);
        head.setDateEntete(date);
        head.setDateSave(new Date());
        head.setDateUpdate(new Date());
        head.setEtat(Constantes.ETAT_EDITABLE);
        head.setStatutLivre(Constantes.ETAT_ATTENTE);
        head.setStatutRegle(Constantes.ETAT_ATTENTE);
        head = (YvsComEnteteDocVente) dao.save1(head);
        return head;
    }

//    public TabPane tabView = new TabPane();
//    Requete rq = new Requete();
    KeyCode code;

//    List<YvsComClient> clients;
//    List<YvsDictionnaire> villes;
    final Tooltip info_quit = new Tooltip("Quitter");
    final Tooltip info_compte = new Tooltip("Voir mon espace");
    final Tooltip info_param = new Tooltip("Paramétrer");

    public Pagination pagination;

    private CheckBox memoireDlg = new CheckBox("Ne plus me rappeler...");

    @FXML
    private BorderPane ROOT_PAN;
    @FXML
    private VBox ZONE_IMG;
    @FXML
    private VBox PAN_ART;
    @FXML
    public Button BTN_CMDE;
    @FXML
    private Button BTN_LIST;
    @FXML
    public Button BTN_CATALOGUE;
    @FXML
    private Button BTN_TRASH;
    @FXML
    private Button BTN_COMPTE;
    @FXML
    private Button BTN_LOG_OOUT;
    @FXML
    private AnchorPane PANE;
    @FXML
    private TextField TEXT_FIND;
    @FXML
    public Label LABEL_NB_TRANSFERT;
    @FXML
    public Label LAB_RESULT;

    @FXML
    private Label LAB_VOL;
    @FXML
    private Label LAB_POID;
//    @FXML
//    private VBox PAN_COND;
    @FXML
    private Button BTN_PARAM;

    @FXML
    private Label LAB_TOTAL;
    @FXML
    private Label LAB_T_HT;
//    @FXML
//    public Button BTN_SAVE;
//    @FXML
//    public Button BTN_REGLER;
//    @FXML
//    public Button BTN_LIVRER;

    @FXML
    private Label L_CURRENT;

    @FXML
    public VBox PAN_STOCK;
    @FXML
    public ImageView IMG_WIFI;
    @FXML
    private TextArea ART_DESCRIPTION;

    @FXML
    private Label SESS_SOCIETE;
    @FXML
    private Label SESS_DUREE;

    public void initialize_() {
        initComponent_();
        initContent_();

        BTN_PARAM.setTooltip(info_param);
        BTN_LOG_OOUT.setTooltip(info_quit);
        BTN_COMPTE.setTooltip(info_compte);
//        if (UtilsProject.depotLivraison != null) {
//            if (serviceListen == null) {
//                serviceListen = new ListenServersRemote(10, UtilsProject.depotLivraison.getId(), this);
//                serviceListen.start();
//            } else {
//                serviceListen.setDepot(UtilsProject.depotLivraison.getId());
//                serviceListen.restart();
//            }
//        }
        LAB_RESULT.setVisible(false);
//        if (LymytzService.autoriserPage("gescom_paramG")) {
////        if (LymytzService.autoriserPage(1501L)) {
//            System.err.println(" Vous êtes autorisé...");
//        } else {
//            BTN_PARAM.setVisible(false);
//        }
//        SESS_SOCIETE.setText(UtilsProject.currentSociete.getName());
//        paramConnection = new ParamConnection();
//        paramConnection.readFile(LymytzSell.getFileInputStream());
//        time.addListener(new ChangeListener<Number>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                Platform.runLater(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        SESS_DUREE.setText(Constantes.HMS.format(new Date(getTime())));
//                    }
//                });
//            }
//        });
//        Thread t = new Thread(new Clock());
//        t.start();
    }

    private void initComponent_() {
        // Bouton cmde
        final Image img = new Image(LocalLoader.class.getResource("icones/icone_caddie.png").toExternalForm());
        final ImageView icon = new ImageView(img);
        BTN_CMDE.setGraphic(icon);
        final Image img1 = new Image(LocalLoader.class.getResource("icones/list-fact.png").toExternalForm());
        final ImageView icon1 = new ImageView(img1);
        BTN_LIST.setGraphic(icon1);
        final Image img2 = new Image(LocalLoader.class.getResource("icones/catalogue.png").toExternalForm());
        final ImageView icon2 = new ImageView(img2);
        BTN_CATALOGUE.setGraphic(icon2);
        final Image img3 = new Image(LocalLoader.class.getResource("icones/compte.png").toExternalForm());
        final ImageView icon3 = new ImageView(img3);
        BTN_COMPTE.setGraphic(icon3);
        final Image img4 = new Image(LocalLoader.class.getResource("icones/log_out.png").toExternalForm());
        final ImageView icon4 = new ImageView(img4);
        BTN_LOG_OOUT.setGraphic(icon4);
        final Image img5 = new Image(LocalLoader.class.getResource("icones/trash.png").toExternalForm());
        final ImageView icon5 = new ImageView(img5);
        BTN_TRASH.setGraphic(icon5);
        final Image img6 = new Image(LocalLoader.class.getResource("icones/param.png").toExternalForm());
        final ImageView icon6 = new ImageView(img6);
        BTN_PARAM.setGraphic(icon6);

        //zone facture
//        ZONE_FACTURE.getChildren().add(tabView);
        final ImageView btn_save = new ImageView(new Image(LocalLoader.class.getResource("icones/save.png").toExternalForm()));
        final ImageView btn_print = new ImageView(new Image(LocalLoader.class.getResource("icones/print.png").toExternalForm()));
        final ImageView btn_reg = new ImageView(new Image(LocalLoader.class.getResource("icones/money.png").toExternalForm()));
        final ImageView btn_liv = new ImageView(new Image(LocalLoader.class.getResource("icones/cdcopy.png").toExternalForm()));
//        BTN_SAVE.setGraphic(btn_save);
//        BTN_REGLER.setGraphic(btn_reg);
//        BTN_LIVRER.setGraphic(btn_liv);
//        BTN_SAVE.getStyleClass().add("mes_boutons");
//        BTN_REGLER.getStyleClass().add("mes_boutons");
//        BTN_LIVRER.getStyleClass().add("mes_boutons");
//        BTN_LIVRER.setVisible(false);
//        BTN_REGLER.setVisible(false);
    }

    private void initContent_() {
//        Thread t1 = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                clients = rq.loadNameQueries("YvsComClient.findAll_", new String[]{"societe"}, new Object[]{UtilsProject.currentSociete});
//                villes = rq.loadNameQueries("YvsDictionnaire.findVilles", new String[]{}, new Object[]{});
//            }
//        });
//        t1.start();
    }

//    private String getRefFournisseur(YvsBaseArticles art) {
//        String ref = "";
//        if (art.getFournisseurs() != null ? !art.getFournisseurs().isEmpty() : false) {
//            for (YvsBaseArticleFournisseur af : art.getFournisseurs()) {
//                ref += ", " + af.getDesArtExterne();
//            }
//        }
//        return ref;
//    }
    @FXML
    private void openViewParam(ActionEvent event) {
//        ParametresController p = new ParametresController();
        openViewParam(true);

    }

    public void openViewParam(boolean establish) {
        try {
            FXMLLoader loader = new FXMLLoader(LocalLoader.class.getResource("start/form_parametres.fxml"));
            VBox root = loader.load();
            Screen sc = Screen.getPrimary();
            Scene scene = new Scene(root, 500, 280);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initOwner(UtilsProject.primaryStage);
            stage.centerOnScreen();
            stage.show();
            //récupère le contrôleur
//            ParametresController controller = loader.getController();
//            controller.displayParametres(establish);
        } catch (IOException ex) {
            Logger.getLogger(LymytzSell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openViewComptes(ActionEvent event) {
        try {
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource("main/form_comptes.fxml"));
            VBox root = load.load();
            Screen sc = Screen.getPrimary();
            Rectangle2D bounds = sc.getVisualBounds();
            Scene scene = new Scene(root, 1000, 500);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Lymytz:extended caisse apps");
            stage.centerOnScreen();
            stage.setIconified(false);
            stage.initOwner(UtilsProject.primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            MyComptesController controler = (MyComptesController) load.getController();
//            controler.setMainController(this);
//            displayStatHeader(UtilsProject.headerDoc, controler);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ManagedApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openListContent(ActionEvent event) {
        try {
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource("main/report/form_listing.fxml"));
            VBox root = load.load();
            Screen sc = Screen.getPrimary();
            Rectangle2D bounds = sc.getVisualBounds();
            Scene scene = new Scene(root, 1000, 565);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Lymytz:extended caisse apps");
            stage.centerOnScreen();
            stage.setIconified(false);
            stage.initOwner(UtilsProject.primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
//            FormListingController controler = (FormListingController) load.getController();
//            controler.initController(this, UtilsProject.headerDoc);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ManagedApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void initContent_(ActionEvent event) {
        initContent_();
    }

    public void cleanVente(Long idHeader) {
//        Requete<YvsComEnteteDocVente> rq = new Requete<>();
//        try {
//            String req = "delete from yvs_com_doc_ventes WHERE entete_doc=? AND id not in (select c.doc_vente from yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d "
//                    + " ON d.id=c.doc_vente WHERE d.entete_doc=?)";
//            rq.requeteLibre(req, new yvs.dao.Options[]{new Options(idHeader, 1), new Options(idHeader, 2)});
//        } catch (Exception ex) {
//            LymytzService.openExceptionDialog(ex.getMessage(), "Erreur ", "Erreur transaction bd", Alert.AlertType.ERROR, ex);
//        }
    }

//
//    public boolean verifyDateVente(Date date) {
//        int ecart = -1;
//        int nbFiches = -1;
//        if (date != null ? date.after(new Date()) : true) {
//            LymytzService.openAlertDialog("Vous ne pouvez enregistrer une fiche dans le future !", "Date fiche incorrecte", "Date fiche incorrecte !", Alert.AlertType.ERROR);
//            return false;
//        }
//        List<YvsComParametreVente> lp = rq.loadNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{UtilsProject.currentAgence}, 0, 1);
//        if ((lp != null) ? !lp.isEmpty() : false) {
//            ecart = lp.get(0).getJourAnterieur();
//            nbFiches = lp.get(0).getNbFicheMax();
//        }
//        // Vérifie le nombre de fiche non cloturé
//        if (nbFiches > 0) {
//            Long nb = (Long) rq.loadObjectByNameQueries("YvsComEnteteDocVente.countFicheOpenByUsers", new String[]{"users"}, new Object[]{UtilsProject.currentUser.getUsers()});
//            nb = nb != null ? nb : 0;
//            if (nb >= nbFiches) {
//                LymytzService.openAlertDialog("Vous avez trop de fiches non clôturées !", "Trop de fiches non clôturées", "Erreur d'ouverture de fiche", Alert.AlertType.ERROR);
//                return false;
//            }
//        }
//        return verifyDate(date, ecart);
//    }
//    public boolean verifyDate(Date date, int ecart) {
////        if (autoriser("com_save_hors_limit")) {
////            return true;
////        }
//        String[] champ = new String[]{"dateJour", "societe"};
//        Object[] val = new Object[]{date, UtilsProject.currentSociete};
//        YvsBaseExercice exo = (YvsBaseExercice) rq.findOneEntity("YvsBaseExercice.findActifByDate", champ, val);
//        System.err.println(" Exo -- " + exo + " Société " + UtilsProject.currentSociete + UtilsProject.currentSociete.getName());
//        if (exo != null ? exo.getId() < 1 : true) {
//            LymytzService.openAlertDialog("Le document doit etre enregistré dans un exercice actif", "Erreur facture", "Aucun exercice actif trouvé", Alert.AlertType.ERROR);
//            return false;
//        }
//        if (exo.getCloturer()) {
//            LymytzService.openAlertDialog("Le document ne peut pas etre enregistré dans un exercice cloturé", "Erreur facture", "Exercice clôturé", Alert.AlertType.ERROR);
//            return false;
//        }
//        if (ecart > 0) {
//            Calendar c = Calendar.getInstance();
//            c.setTime(new Date());
//            c.set(Calendar.HOUR_OF_DAY, 0);
//            c.set(Calendar.MINUTE, 0);
//            c.set(Calendar.SECOND, 0);
//            c.set(Calendar.MILLISECOND, 0);
//
//            Calendar d = Calendar.getInstance();
//            d.setTime(date);
//            d.set(Calendar.HOUR_OF_DAY, 0);
//            d.set(Calendar.MINUTE, 0);
//            d.set(Calendar.SECOND, 0);
//            d.set(Calendar.MILLISECOND, 0);
//            if (d.after(c)) {
//                LymytzService.openAlertDialog("La date ne doit pas superieur à la date du jour", "Erreur facture", "Date de a fiche incorrecte", Alert.AlertType.ERROR);
//                return false;
//            }
//            if (ecart > 0) {
//                c.add(Calendar.DATE, -ecart);
//                if (d.before(c)) {
//                    LymytzService.openAlertDialog("La date ne doit pas excedé le nombre de jour de retrait prévu", "Erreur facture", "Date de a fiche incorrecte", Alert.AlertType.ERROR);
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    public YvsComEnteteDocVente createNewFicheFromCreneaux(long idCreno, Date date) {
//        //Vérifie si le vendeur a déjà ouvert une fiche ce jour
//        Requete rq = new Requete<>();
//        //récupère les paramètres commerciale
//        YvsComCreneauHoraireUsers creno = (YvsComCreneauHoraireUsers) rq.findOneEntity("YvsComCreneauHoraireUsers.findById", new String[]{"id"}, new Object[]{idCreno});
//        date = (date == null && creno != null) ? creno.getDateTravail() : new Date();
//        if (!verifyDateVente(date)) {
//            return null;
//        }
//        if (creno != null) {
//            Requete<YvsComEnteteDocVente> rqh = new Requete<>();
//            YvsComEnteteDocVente header = (YvsComEnteteDocVente) rqh.findOneEntity("YvsComEnteteDocVente.findOneFiche", new String[]{"date", "creno"}, new Object[]{date, creno});
//            if (header != null) {
//                //vérifie que la fiche ne soit pas déjà clôturé
//                if (header.getCloturer() || header.getEtat().equals(Constantes.ETAT_CLOTURE)) {
//                    LymytzService.openAlertDialog("La fiche de ce planning est déjà clôturé", "Fiche non editable", "nformation", Alert.AlertType.WARNING);
//                    UtilsProject.headerDoc = null;
//                } else {
//                    UtilsProject.headerDoc = header;
//                }
//            } else {
//                header = createHeder(creno, ((date != null) ? date : creno.getDateTravail()), rq);
//                UtilsProject.headerDoc = header;
//            }
//            displayPropertiesFiche(header);
//            return header;
//        } else {
//            LymytzService.openAlertDialog("Aucun créno actif n'a été trouvé !", "Objet non trouvé", "Erreur", Alert.AlertType.ERROR);
//        }
//        return null;
//    }
//
//    private YvsComEnteteDocVente createHeder(YvsComCreneauHoraireUsers creno, Date date, Requete rq) {
//        YvsComEnteteDocVente head = new YvsComEnteteDocVente();
//        head.setAgence(UtilsProject.currentAgence);
//        head.setAuthor(UtilsProject.currentUser);
//        head.setCloturer(false);
//        head.setCreneau(creno);
//        head.setDateEntete(date);
//        head.setDateSave(new Date());
//        head.setDateUpdate(new Date());
//        head.setEtat(Constantes.ETAT_EDITABLE);
//        head = (YvsComEnteteDocVente) rq.save1(head);
//        return head;
//    }
    @FXML
    private void createNewFacture(ActionEvent event) {
        try {
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource("main/form_create_facture.fxml"));
            VBox root = load.load();
            Scene scene = new Scene(root, 500, 320);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Lymytz:extended caisse apps");
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(UtilsProject.primaryStage);
            stage.show();
            FactureController controler = (FactureController) load.getController();
//            controler.initDataForm(this, stage);
            scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode().equals(KeyCode.ESCAPE)) {
                        stage.close();
                    }
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(ManagedApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openViewCatalogue(ActionEvent event) {
        try {
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource("main/form_catalogue.fxml"));
            BorderPane root = load.load();
            Scene scene = new Scene(root, 900, 700);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Lymytz:Catalogue articles");
            stage.centerOnScreen();
//            stage.initOwner(UtilsProject.primaryStage);
            stage.show();
//            CatalogueController controler = (CatalogueController) load.getController();
        } catch (IOException ex) {
            Logger.getLogger(ManagedApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openViewFacture(ActionEvent event) {
        try {
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource("main/form_factures.fxml"));
            AnchorPane root = load.load();
            Scene scene = new Scene(root, 955, 580);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Lymytz:Factures");
            stage.centerOnScreen();
            stage.initOwner(UtilsProject.primaryStage);
            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setResizable(false);
            stage.show();
//            ListFacturesController controler = (ListFacturesController) load.getController();
//            controler.initPage(this);
        } catch (IOException ex) {
            Logger.getLogger(ManagedApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public synchronized Long createFactureVente(YvsComClient client, String typeDoc, YvsDictionnaire adresse, String nameClient, Date dateLiv, String telephone) {
//        Facture f = new Facture(client, typeDoc, adresse, nameClient, dateLiv, telephone);
//        Thread t = new Thread(f);
//        t.run();
//        return f.getResult();
//    }
//
//    public void deleteLineContenu(int numLine) {
//        Onglets tab = (Onglets) tabView.getSelectionModel().getSelectedItem();
//        if (tab != null ? tab.getFacture().getStatut().equals(Constantes.ETAT_EDITABLE) : false) {
//            tab.getContentsFacture().remove(numLine);
//            displayMontantsBean(tab);
//        } else {
//            if (tab == null) {
//                LymytzService.openAlertDialog("Modification de la facture impossible !", "Erreur ", "Aucune facture n'a été initié !", Alert.AlertType.ERROR);
//            } else {
//                LymytzService.openAlertDialog("Modification de la facture impossible !", "Erreur ", "Cette facture n'est plus éditable", Alert.AlertType.ERROR);
//            }
//        }
//    }
//
    public void incrementLinePanier(int numLine, int qte) {
//        Onglets tab = (Onglets) tabView.getSelectionModel().getSelectedItem();
//        ContentPanier c = tab.getContentsFacture().get(numLine);
//        c.getConditionnement().setArticle(c.getArticle());
//        if (addArticleOnFacture(c.getConditionnement(), qte, false, (c.getArticle().getChangePrix() ? c.getPrix() : 0))) {
////            displayMontants(tab);
//        }
    }
//
//    public void displayListArticlesInPoint(List<YvsBaseArticles> l) {
//        ButtonArticles gp;
//        PAN_ART.getChildren().clear();
//        for (YvsBaseArticles fa : l) {
//            gp = new ButtonArticles(fa, this);
//            gp.setPrefSize(60, 20);
////            gp.getIMG().setFitHeight(100);
////            gp.getIMG().setFitWidth(100);
////            gp.getIMG().setPreserveRatio(true);
//            PAN_ART.getChildren().add(gp);
//        }
//
//    }
//
//    public void displayPropertyArticle(String refArticle) {
//        if (refArticle != null ? !refArticle.trim().isEmpty() : false) {
//            Requete<YvsBaseArticles> rq = new Requete<>();
//            if (idDepots == null) {
//                idDepots = new ArrayList<>();
//            }
//            if (idDepots.isEmpty()) {
//                idDepots.add(-1L);
//            }
//            List<YvsBaseArticles> articles = rq.loadNameQueries("YvsBaseArticleDepot.findArticleByDepots", new String[]{"ids", "categories", "code"}, new Object[]{idDepots, categories, "%" + refArticle + "%"}, 0, 20);
//            displayListArticlesInPoint(articles);
//            if (articles.size() == 1) {
//                displayPropertyArticle(articles.get(0), null, false);
//            }
//        }
//    }
//
//    public void displayPropertyArticleById(Long idArticle) {
//        if (idArticle != null ? idArticle > 0 : false) {
//            Requete<YvsBaseArticles> rq = new Requete<>();
//            if (idDepots == null) {
//                idDepots = new ArrayList<>();
//            }
//            if (idDepots.isEmpty()) {
//                idDepots.add(-1L);
//            }
//            List<YvsBaseArticles> articles = rq.loadNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{idArticle});
//            displayListArticlesInPoint(articles);
//            if (articles.size() == 1) {
//                displayPropertyArticle(articles.get(0), null, true);
//            }
//        }
//    }
//

//
    @FXML
    private void findArticleByRef(KeyEvent event) {
//        //Exécute cette methode seulement si l'option display detail est actif
//        if (CHK_DISPLAY.isSelected()) {
//            if (TEXT_FIND.getText() != null ? TEXT_FIND.getText().trim().length() > 1 : false) {
//                displayPropertyArticle(TEXT_FIND.getText());
//            }
//        } else {
//            if (event.getCode().equals(KeyCode.ENTER)) {
//                if (!addArticleOnFacture(TEXT_FIND.getText())) {
//                    return;
//                }
//
//            }
//        }
    }
//
//    public boolean addArticleOnFacture(String refArt) {
//        if (refArt != null ? !refArt.trim().isEmpty() : false) {
//            Requete rq = new Requete<>();
//            String query = "SELECT a.id,c.id FROM yvs_base_article_code_barre ac RIGHT OUTER JOIN yvs_base_conditionnement c ON c.id=ac.conditionnement "
//                    + " INNER JOIN yvs_base_articles a ON a.id=c.article"
//                    + " INNER JOIN yvs_base_famille_article f ON f.id=a.famille WHERE f.societe=? AND a.actif IS true AND"
//                    + " (UPPER(a.ref_art)=? OR UPPER(a.designation)=? OR UPPER(ac.code_barre)=?)";
//            List<Object[]> re = rq.loadDataBySqlQuerie(query, new Options[]{new Options(UtilsProject.currentSociete.getId(), 1), new Options(refArt.trim().toUpperCase(), 2), new Options(refArt.trim().toUpperCase(), 3), new Options(refArt.trim().toUpperCase(), 4)});
//            if (re.size() == 1) {
//                Object[] line = re.get(0);
//                YvsBaseArticles article = null;
//                YvsBaseConditionnement cond = null;
//                if (line[1] != null) { // id conditionnement
//                    Long idcond = (Long) line[1];
//                    cond = (YvsBaseConditionnement) rq.findOneEntity("YvsBaseConditionnement.findById", new String[]{"id"}, new Object[]{idcond});
//                    article = (cond != null) ? cond.getArticle() : null;
//                } else {
//                    article = (YvsBaseArticles) rq.findOneEntity("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{re.get(0)});
//                }
//                if (article != null) {
//                    ZONE_IMG.getChildren().clear();
//                    createImageProduit(article);
//                    ZONE_IMG.getChildren().add(pagination);
//                    if (cond != null) {
//                        if (!addArticleOnFacture(cond, 1, false, 0)) {
//                            return false;
//                        }
//                    } else {
//                        addArticleOnFacture(article);
//                    }
//                } else {
//                    LAB_RESULT.setVisible(true);
//                    LAB_RESULT.setText("Aucune référence trouvé !");
//                }
//                LAB_RESULT.setVisible(false);
//                return true;
//            } else {
//                LAB_RESULT.setVisible(true);
//                LAB_RESULT.setText("Aucune ou plus d'une référence trouvé !");
////                LymytzService.openAlertDialog("Aucune ou plus d'une référence trouvé !", "Erreur !!", "Aucune ou plus d'une référence trouvé !", Alert.AlertType.ERROR);
//                TEXT_FIND.setText("");
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public void addArticleOnFacture(YvsBaseArticles art) {
//        if (art.getConditionnements() != null) {
//            if (art.getConditionnements().size() == 1) {
//                if (!addArticleOnFacture(art.getConditionnements().get(0), 1, false, 0)) {
//                    return;
//                }
//            } else {
//                LymytzService.openAlertDialog("Modification de la facture impossible !", "Erreur ", "La précision du conditionnement est requise !", Alert.AlertType.ERROR);
//            }
//        } else {
//            LymytzService.openAlertDialog("Modification de la facture impossible !", "Erreur ", "Aucun conditionnement trouvé pour cet article !", Alert.AlertType.ERROR);
//        }
//    }
//    ButtonType re;
//
//    public boolean addArticleOnFacture(YvsBaseConditionnement cond, double q, boolean resetQte, double prixV) {
//        Onglets tab = (Onglets) tabView.getSelectionModel().getSelectedItem();
//        if (tab != null ? (tab.getFacture().getStatut().equals(Constantes.ETAT_EDITABLE) && !tab.getFacture().isSynchroniser()) : false) {
//            displayPropertyArticle(cond.getArticle(), cond, false);
//            ContentPanier line = buildContentPanier(cond, tab);
//            //contrôle de stock
//            if (tab.getFacture().getTypeDoc().equals(Constantes.TYPE_FV)) {
//                if (cond.getStock() - q < 0) {
//                    Boolean sellWithoutStock = (Boolean) rq.loadObjectByNameQueries("YvsBaseArticleDepot.findIfSellWithOutStock", new String[]{"article", "depot"}, new Object[]{cond.getArticle(), UtilsProject.depotLivraison});
//                    sellWithoutStock = (sellWithoutStock != null) ? sellWithoutStock : true;
//                    if (!sellWithoutStock) {
//                        LymytzService.openAlertDialog("Insertion impossible!", "Erreur ", "Le stock de cet article est insuffisant dans le dépôt planifié", Alert.AlertType.ERROR);
//                        return false;
//                    } else {
//                        if (!memoireDlg.isSelected()) {
//                            re = LymytzService.openCustumAlertDialogChoice("Stock insuffisant voulez-vous continuer?", "Stock insuffisant", "L'article vendu est insuffisant en stock", Alert.AlertType.CONFIRMATION, memoireDlg);
//                        } else {
//
//                        }
//                        if (re != null ? !re.equals(ButtonType.OK) : false) {
//                            tab.getContentsFacture().remove(line);
//                            return false;
//                        }
//                    }
//                }
//            }
//            double qte = q;
//            boolean lineIn = false;
//            if (!tab.getContentsFacture().contains(line)) {
//            } else {
//                line = tab.getContentsFacture().get(tab.getContentsFacture().indexOf(line));
//                if (!resetQte) {
//                    qte = line.getQuantite() + q;
//                } else {
//                    qte = q;
//                }
//                lineIn = true;
//            }
//            PrixArticles prix = getPrixArticle(cond, qte, tab.getFacture(), prixV);
//            if (prix.getTotalTTC() > 0) {
//                line.setPr(prix.getPr());
//                line.setPrix(prix.getPrixUnite());
//                line.setRemise(prix.getRemise());
//                line.setRistourne(prix.getRistourne());
//                line.setTaxe(prix.getTaxe());
//                line.setQuantite(qte);
//                line.setMontantTotal(prix.getNetApayer());
//                line.setMontantTotalHT(prix.getTotalHt());
//                line.setMontantTotalTTC(prix.getTotalTTC());
//                if (!lineIn) {
//                    tab.getContentsFacture().add(0, line);
//                } else {
//                    int idx = tab.getContentsFacture().indexOf(line);
//                    if (line.getQuantite() > 0) {
//                        tab.getContentsFacture().set(idx, line);
//                    } else {
//                        tab.getContentsFacture().remove(idx);
//                    }
//                }
//                int i = 1;
//                for (ContentPanier c : tab.getContentsFacture()) {
//                    c.setNumLine(i);
//                    c.setId(-i);
//                    i++;
//                }
//                displayMontantsBean(tab);
//                //Redonne le curseur
//                giveFocusAtTxtFind();
//            } else {
////                LymytzService.openAlertDialog("Modification de la facture impossible !", "Erreur ", "Le prix de l'article est incorrecte !", Alert.AlertType.ERROR);
//                LogFiles.addLogInFile("Le prix de l'article " + cond.getArticle().getDesignation() + " est incorrecte !");
//            }
//        } else {
//            if (tab == null) {
//                LymytzService.openAlertDialog("Modification de la facture impossible !", "Erreur ", "Aucune facture n'a été initié !", Alert.AlertType.ERROR);
//                return false;
//            } else {
//                LymytzService.openAlertDialog("Modification de la facture impossible !", "Erreur ", "Cette facture n'est plus éditable", Alert.AlertType.ERROR);
//            }
//            return false;
//        }
//        return true;
//    }
//
////    private ContentPanier buildContentPanier(YvsBaseConditionnement cond, Onglets tab) {
////        ContentPanier re = new ContentPanier();
////        re.setArticle(cond.getArticle());
////        re.setConditionnement(cond);
////        re.setNumLine(tab.getContentsFacture().size());
////        re.setQuantite(1);
////        //calcul détermine les stocks
////        //Détermine le prix de vente
////        return re;
////    }
//
////    private PrixArticles getPrixArticle(YvsBaseConditionnement c, double qte, YvsComDocVentes facture, double prix) {
////        Requete rq = new Requete();
////        PrixArticles re = new PrixArticles();
////        re.setArticles(c.getArticle());
////        if (prix <= 0 || !c.getArticle().getChangePrix()) {
////            re.setPrixUnite(rq.getByrequeteLibre("select public.get_puv(?,?,?,?,?,?,?,?,?)", new Options[]{
////                new Options(c.getArticle().getId(), 1), new Options(qte, 2), new Options(c.getPrix(), 3), new Options(facture.getClient().getId(), 4), new Options(0, 5),
////                new Options(UtilsProject.headerDoc.getCreneau().getCreneauPoint().getPoint().getId(), 6), new Options(UtilsProject.headerDoc.getDateEntete(), 7), new Options(c.getId(), 8), new Options(false, 9)
////            }));
////        } else {
////            re.setPrixUnite(prix);
////        }
////        re.setRemise(rq.getByrequeteLibre("select public.get_remise_vente(?,?,?,?,?,?,?)", new Options[]{
////            new Options(c.getArticle().getId(), 1), new Options(qte, 2), new Options(re.getPrixUnite(), 3), new Options(facture.getClient().getId(), 4), new Options(UtilsProject.headerDoc.getCreneau().getCreneauPoint().getPoint().getId(), 5),
////            new Options(UtilsProject.headerDoc.getDateEntete(), 6), new Options(c.getId(), 7)
////        }));
////        re.setTaxe(rq.getByrequeteLibre("select public.get_taxe(?,?,?,?,?,?,?)", new Options[]{
////            new Options(c.getArticle().getId(), 1), new Options(facture.getCategorieComptable().getId(), 2), new Options(0, 3), new Options(re.getRemise(), 4), new Options(qte, 5),
////            new Options(re.getPrixUnite(), 6), new Options(true, 7)
////        }));
////        re.setRistourne(rq.getByrequeteLibre("select public.get_ristourne(?,?,?,?,?)", new Options[]{
////            new Options(c.getId(), 1), new Options(qte, 2), new Options(re.getPrixUnite(), 3), new Options(facture.getClient().getId(), 4),
////            new Options(facture.getEnteteDoc().getDateEntete(), 5)
////        }));
////        re.setPr(rq.getByrequeteLibre("select public.get_pr(?,?,?,?,?)", new Options[]{
////            new Options(c.getArticle().getId(), 1), new Options(UtilsProject.depotLivraison.getId(), 2), new Options(0, 3), new Options(facture.getEnteteDoc().getDateEntete(), 4),
////            new Options(c.getId(), 5)
////        }));
////        double total = (re.getPrixUnite() * qte) - re.getRemise();
////        total = (c.getArticle().getPuvTtc()) ? total : total + re.getTaxe();
////        re.setTotalHt(total - re.getTaxe());
////        re.setTotalTTC((c.getArticle().getPuvTtc()) ? total : (re.getTotalHt() + re.getTaxe()));
////        re.setNetApayer(total);
////        return re;
////    }
//

//    public void openDlgCalculatrice(Onglets onglet, String source) {
//        try {
////            if (onglet != null ? onglet.getContentsFacture().isEmpty() : true) {
////                LymytzService.openAlertDialog("Votre panier est vide", "erreur contenu", "Erreur !", Alert.AlertType.ERROR);
////                return;
////            }
//            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource("component/claviers.fxml"));
//            VBox root = load.load();
//            Scene scene = new Scene(root, 485, 566);
//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.setTitle("Lymytz:Claviers");
//            stage.centerOnScreen();
//            stage.initModality(Modality.WINDOW_MODAL);
//            stage.initOwner(UtilsProject.primaryStage);
//            stage.setResizable(false);
//            stage.show();
//            ClaviersController controler = (ClaviersController) load.getController();
//            controler.initController(mainPage, onglet, stage, source);
//        } catch (IOException ex) {
//            Logger.getLogger(ManagedApplication.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public void comptabilise(Long id, String numDoc) {
        String squery = "SELECT y.comptabilisation_auto FROM yvs_com_parametre_vente y WHERE y.agence=?";
        Boolean be = (Boolean) dao.findOneObjectBySQLQ(squery, new Options[]{new Options(UtilsProject.currentAgence.getId(), 1)});
        if (be) {
            WsSynchro ws = new WsSynchro();
            ResultatAction re = ws.comptabiliseVente(id, UtilsProject.currentUser.getId(), numDoc);
        }
    }

////    private boolean saveContentFacture(List<ContentPanier> contents, YvsComDocVentes doc) {
//    private YvsComptaAcompteClient saveAcompteClient(YvsComDocVentes facture, double montant, Date date) {
//        YvsComptaAcompteClient bean = new YvsComptaAcompteClient();
//        String numDoc = UtilsProject.generatedNumDoc(Constantes.TYPE_PT_AVANCE_VENTE);
//        if (numDoc != null ? !numDoc.isEmpty() : false) {
//            Requete rq = new Requete();
//            bean.setAuthor(UtilsProject.currentUser);
//            bean.setCaisse(UtilsProject.caisse);
//            bean.setClient(facture.getClient());
//            bean.setCommentaire("Avance pour la commande " + facture.getNumDoc());
//            bean.setDateAcompte(date);
//            bean.setDateSave(new Date());
//            bean.setDateUpdate(new Date());
//            bean.setModel(UtilsProject.modeReg);
//            bean.setMontant(montant);
//            bean.setNumRefrence(numDoc);
//            bean = (YvsComptaAcompteClient) rq.save1(bean);
//            return bean;
//        } else {
//            LogFiles.addLogInFile("Le numéro de pièce d'avance client n'a pu être généré ");
//            return null;
//        }
//    }
//
//    private boolean bindNotifReglement(YvsComptaAcompteClient avance, YvsComptaCaissePieceVente piece) {
//        if (avance != null && piece != null) {
//            if (avance.getId() > 0 && piece.getId() > 0) {
//                // contrôle que le solde d'avance du client permt de créer la liason
//                if (piece.getMontant() <= avance.getMontant()) {
//                    Requete rq = new Requete();
//                    YvsComptaNotifReglementVente notif = new YvsComptaNotifReglementVente();
//                    notif.setAcompte(avance);
//                    notif.setPieceVente(piece);
//                    notif.setAuthor(UtilsProject.currentUser);
//                    notif.setDateSave(new Date());
//                    notif.setDateUpdate(new Date());
//                    rq.save(notif);
//                } else {
//                    LogFiles.addLogInFile("Le montant de l'acompte est inférieure au total de la pièce !");
//                }
//            }
//        }
//        return true;
//    }
//
//    private boolean verifyOperation(String type, String operation) {
//        if (UtilsProject.depotLivraison != null ? UtilsProject.depotLivraison.getId() > 0 : false) {
//            if (!UtilsBean.checkOperationDepot(UtilsProject.depotLivraison.getId(), type)) {
//                LogFiles.addLogInFile("Le depot '" + UtilsProject.depotLivraison.getDesignation() + "' n'est pas paramètré pour les opérations '" + type + "'");
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private boolean controleLivraison(YvsComDocVentes facture) {
//        double s;
//        Requete rq = new Requete();
//        for (YvsComContenuDocVente c : facture.getContenus()) {
//            s = getStocks(c.getConditionnement(), UtilsProject.depotLivraison.getId());
//            Double qteLivre = (Double) rq.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", new String[]{"docVente", "statut", "typeDoc", "article", "unite"}, new Object[]{c.getDocVente().getDocumentLie(), Constantes.ETAT_VALIDE, Constantes.TYPE_BLV, c.getArticle(), c.getConditionnement()});
//            qteLivre = (qteLivre != null) ? qteLivre : 0;
//            //trouve la quantité d'article facturé 
//            Double qteFacture = (Double) rq.loadObjectByNameQueries("YvsComContenuDocVente.findQteByArticle", new String[]{"docVente", "article", "unite"}, new Object[]{c.getDocVente().getDocumentLie(), c.getArticle(), c.getConditionnement()});
//            qteFacture = (qteFacture != null) ? qteFacture : 0;
//            //trouve la quantité d'article facturé 
//            Double qteBonusFacture = (Double) rq.loadObjectByNameQueries("YvsComContenuDocVente.findQteBonusByFacture", new String[]{"docVente", "article", "unite"}, new Object[]{c.getDocVente().getDocumentLie(), c.getArticle(), c.getConditionnement()});
//            qteBonusFacture = (qteBonusFacture != null) ? qteBonusFacture : 0;
//
//            if (c.getDocVente().getDocumentLie() != null ? !c.getDocVente().getDocumentLie().getStatutRegle().equals(Constantes.ETAT_REGLE) : true) {
//                //si la facture n'est pas encore réglé, on ne dois pas inclure la quantité bonus dans la quantité à livrer
//                if (c.getQuantite() > (qteFacture - qteLivre)) {
//                    LogFiles.addLogInFile("Vous ne pouvez livrer l'article " + c.getArticle().getRefArt() + " au delà de la quantité facturée !");
//                    return false;
//                }
//            } else {
//                if (c.getQuantite() > ((qteFacture + qteBonusFacture) - qteLivre)) {
//                    LogFiles.addLogInFile("Vous ne pouvez livrer l'article " + c.getArticle().getRefArt() + " au delà de la quantité facturée !");
//                    return false;
//                }
//            }
//            if (s < c.getQuantite()) {
//                if (c.getArticle().getMethodeVal() != null) {
//                    if (!c.getArticle().getMethodeVal().equals(Constantes.CMP2)) {
//                        LogFiles.addLogInFile("Impossible d'effectuer cette action... Car l'article " + c.getArticle().getDesignation() + " n'a plus un stock suffisant");
//                        return false;
//                    }
//                } else {
//                    LogFiles.addLogInFile("Impossible d'effectuer cette action... Car l'article " + c.getArticle().getDesignation() + " n'a plus un stock suffisant");
//                    return false;
//                }
//            }
//            YvsBaseArticleDepot y = (YvsBaseArticleDepot) rq.findOneEntity("YvsBaseArticleDepot.findByArticleDepot", new String[]{"article", "depot"}, new Object[]{c.getArticle(), UtilsProject.depotLivraison});
//            if (y != null ? y.getId() < 1 : true) {
//                LogFiles.addLogInFile("Impossible d'effectuer cette action... Car le depot " + UtilsProject.depotLivraison.getDesignation() + " ne possède pas l'article " + c.getArticle().getDesignation());
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private String crateDocLivraison(YvsComDocVentes facture) {
//        if (!verifyOperation(Constantes.SORTIE, Constantes.VENTE)) {
//            return Constantes.ETAT_ATTENTE;
//        }
//        if (controleLivraison(facture)) {
//            Requete rq = new Requete();
//            YvsComDocVentes docL = new YvsComDocVentes(facture);
//            String numDoc = UtilsProject.generatedNumDoc(Constantes.TYPE_BLV_NAME);
//            if (numDoc != null ? !numDoc.isEmpty() : false) {
//                docL.setId(null);
//                docL.setDocumentLie(facture);
//                String ref = UtilsProject.generatedNumDocBL();
//                docL.setNumDoc(ref);
//                docL.setNumPiece("BL N° " + facture.getNumDoc());
//                docL.setTypeDoc(Constantes.TYPE_BLV);
//                docL.setTrancheLivrer(UtilsProject.headerDoc.getCreneau().getCreneauPoint().getTranche());
//                docL.setDateLivraison(new Date());
//                docL.setDateLivraisonPrevu(facture.getDateLivraisonPrevu());
//                docL.setStatut(Constantes.ETAT_VALIDE);
//                docL = (YvsComDocVentes) rq.save1(docL);
//                if (docL != null) {
//                    YvsComContenuDocVente cv;
//                    for (YvsComContenuDocVente c : facture.getContenus()) {
//                        //Contrôle de stocks
//                        cv = new YvsComContenuDocVente(c);
//                        cv.setId(null);
//                        cv.setDocVente(docL);
//                        cv.setStatutLivree(Constantes.STATUT_DOC_LIVRER);
//                        rq.save(cv);
//                    }
//                }
//                return Constantes.ETAT_LIVRE;
//            } else {
//                LogFiles.addLogInFile("Le BL n'a pas été créer car le numéro de pièce n'a pue être généré !");
//                return Constantes.ETAT_ATTENTE;
//            }
//        } else {
//            return Constantes.ETAT_ATTENTE;
//        }
//    }
//
//
   
//

    @FXML
    private void saveOrGenerateBl(ActionEvent event) {
//        if (UtilsProject.depotLivraison == null) {
//            LymytzService.openAlertDialog("Aucun dépôt de livraison n'a été trouvé pour ce point de vente", "Erreur ", "Impossible de terminer cette action", Alert.AlertType.ERROR);
//            return;
//        }
//        Onglets tab = (Onglets) tabView.getSelectionModel().getSelectedItem();
//        saveOrGenerateBl_(tab);
    }
//
//    private void saveOrGenerateBl_(Onglets onglet) {
//        if (onglet != null) {
//            ServiceLivraison service = new ServiceLivraison(this);
//            if (!onglet.getFacture().getTypeDoc().equals(Constantes.TYPE_BCV)) {
//                service.termineLivraison(onglet.getFacture(), false);
//            } else {
//                Alert dlg = new Alert(Alert.AlertType.CONFIRMATION, "Confirmez vous la livraison de cette commande ?", new ButtonType("Oui"), new ButtonType("Non"));
//                Optional<ButtonType> re = dlg.showAndWait();
//                if (re.get().getText().equals("Oui")) {
//                    if (service.transmisOrder(onglet.getFacture())) {
//                        BTN_LIVRER.setVisible(false);
//                    }
//                } else {
////                System.err.println(" Livre pas");
//                }
//            }
//        }
//    }

//
    private Image[] images;

    public void createImageProduit(YvsBaseArticles art) {
        int n = art.getPhotos().size();
        pagination = new Pagination((n <= 0) ? 1 : n);
        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        images = new Image[art.getPhotos().size()];
        int idx = 0;
        for (String str : art.getPhotos()) {
            images[idx] = displayPhotos(str);
            idx++;
        }
        pagination.setPageFactory(new Callback<Integer, Node>() {

            @Override
            public Node call(Integer param) {
                return displayPagesArticles(param);
            }
        });
    }

    private VBox displayPagesArticles(int pageIndex) {
        VBox box = new VBox();
        if (images.length > pageIndex) {
            ImageView iv = new ImageView(images[pageIndex]);
            iv.setFitWidth(200);
            iv.setFitHeight(200);
            iv.setPreserveRatio(true);
            box.setAlignment(Pos.CENTER);
            Label desc = new Label("PAGE " + (pageIndex + 1));
            box.getChildren().addAll(iv, desc);
        }
        return box;
    }

    private Image displayPhotos(String photo) {
        try {
            String path = UtilsProject.properties.getProperty(Constantes.KEY_PATH);
            if (Constantes.asString(path)) {
                File f = new File(path + "\\" + photo);
                if (f.exists()) {
                    return new Image(new FileInputStream(f));
                } else {
                    return new Image(LocalLoader.class.getResource("icones/coffee.png").toExternalForm());
                }
            } else {
                return new Image(LocalLoader.class.getResource("icones/coffee.png").toExternalForm());
            }
        } catch (FileNotFoundException ex) {
            return new Image(LocalLoader.class.getResource("icones/coffee.png").toExternalForm());
        }
    }
//
//    public void clearTabFacture(ActionEvent ev) {
//        if (!tabView.getTabs().isEmpty()) {
//            Alert al = new Alert(Alert.AlertType.CONFIRMATION);
//            al.setTitle("Fermer les factures en cours");
//            al.setContentText("Si vous confirmez cette action, les factures non sauvegardé seront perdues");
//            Optional<ButtonType> result = al.showAndWait();
//            if (result.get().equals(ButtonType.OK)) {
//                tabView.getTabs().clear();
//                displayDetailFacture(null);
//            }
//        }
//    }
//

    @FXML
    public void deleteFacture(ActionEvent ev) {
//        if (!tabView.getTabs().isEmpty()) {
//            Onglets tab = (Onglets) tabView.getSelectionModel().getSelectedItem();
//            if (tab != null) {
//                if (!tab.getFacture().getStatut().equals(Constantes.ETAT_VALIDE)) {
//                    Alert al = new Alert(Alert.AlertType.CONFIRMATION);
//                    al.setTitle("Confrimation");
//                    al.setHeaderText("Supprimer la facture en cours");
//                    al.setContentText("Si vous confirmez cette action, la facture et son contenu sera perdu définitivement");
//                    Optional<ButtonType> result = al.showAndWait();
//                    if (result.get().equals(ButtonType.OK)) {
//                        Requete rq = new Requete();
//                        rq.delete(tab.getFacture());
//                        tabView.getTabs().remove(tab);
//                        afterCreateFacture();
//                    }
//                } else {
//                    LymytzService.openAlertDialog("Vous ne disposez pas de privillège pour supprimer cette facture", "Erreur !!!", "Action non autorisé", Alert.AlertType.ERROR);
//                }
//            }
//        }
    }
//

    @FXML
    private void navigueLeft(MouseEvent event) {
        navigueInHeader(false);
    }

    @FXML
    private void navigueRight(MouseEvent event) {
        navigueInHeader(true);
    }

//    private int current = -1;
//    private List<YvsComDocVentes> listeFactures;
//    Onglets tab = null;
//
    private void navigueInHeader(boolean next) {
//        if (current == -1) {
//            listeFactures = rq.loadNameQueries("YvsComDocVentes.findByHeader", new String[]{"entete", "typeDoc", "typeDoc2"}, new Object[]{UtilsProject.headerDoc, Constantes.TYPE_FV, Constantes.TYPE_BCV});
//            if (tab == null) {
//                tab = new Onglets((!listeFactures.isEmpty() ? listeFactures.get(0) : null), this);
//                tabView.getTabs().add(0, tab);
////                tabView.getSelectionModel().select(tab);
//                tabView.getSelectionModel().select(0);
//            } else {
//                if (!tabView.getTabs().contains(tab)) {
//                    tabView.getTabs().add(0, tab);
//                    tabView.getSelectionModel().select(0);
//                }
//            }
//            loadContentOnView(tab.getFacture(), tab);
//            L_CURRENT.setText("" + (1));
//            current = 0;
//        } else if (!listeFactures.isEmpty()) {
//            if (tab != null && !tabView.getTabs().contains(tab)) {
//                tabView.getTabs().add(0, tab);
//                tabView.getSelectionModel().select(0);
//            }
//            int total = listeFactures.size();
//            if (total > 0) {
//                current = current + (next ? (1) : (-1));
//                if (current < 0 || current >= total) {
//                    current = -1;
//                    navigueInHeader(next);
//                    return;
//                }
//                tab.setFacture(listeFactures.get(current));
//                tab.setText(tab.getFacture().getNumDoc());
//                loadContentOnView(tab.getFacture(), tab);
//                L_CURRENT.setText("" + (current + 1));
//            }
//        }
    }

//    public void saveCurrentCommercial(YvsComDocVentes facture) {
//        if (UtilsProject.headerDoc != null) {
//            if (UtilsProject.headerDoc.getCreneau() != null ? (UtilsProject.headerDoc.getCreneau().getId_() != null ? UtilsProject.headerDoc.getCreneau().getId_() > 0 : false) : false) {
//                char commissionFor = 'C';
//                YvsBasePointVente pv = null;
//                YvsComCreneauPoint cr = UtilsProject.headerDoc.getCreneau().getCreneauPoint();
//                if (cr != null ? cr.getId() > 0 : false) {
//                    pv = cr.getPoint();
//                    if (pv != null ? pv.getId() > 0 : false) {
//                        pv = (YvsBasePointVente) rq.findOneEntity("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{pv.getId()});
//                        if (pv != null ? pv.getId() > 0 : false) {
//                            commissionFor = pv.getCommissionFor();
//                        }
//                    }
//                }
//                YvsComComerciale y = (YvsComComerciale) rq.findOneEntity("YvsComComerciale.findByUser", new String[]{"user"}, new Object[]{UtilsProject.headerDoc.getCreneau().getUsers()});
//                if (y == null) { //Commerciale est celui rattaché au user en cours
//                    YvsComCommercialVente bean;
//                    double taux = pv.getCommerciaux().size() > 0 ? (100 / pv.getCommerciaux().size()) : 0;
//                    for (YvsComCommercialPoint cp : pv.getCommerciaux()) {
//                        bean = new YvsComCommercialVente();
//                        bean.setFacture(facture);
//                        bean.setTaux(taux);
//                        bean.setResponsable(false);
//                        bean.setCommercial(cp.getCommercial());
//                        saveNewCommercial(bean);
//                    }
//                } else {
//                    YvsComCommercialVente bean = new YvsComCommercialVente();
//                    bean.setCommercial(y);
//                    bean.setFacture(facture);
//                    bean.setResponsable(true);
//                    bean.setTaux(100d);
//                    saveNewCommercial(bean);
//                }
//            }
//        }
//    }
//
//    public void saveNewCommercial(YvsComCommercialVente y) {
//        try {
//            if (y != null ? y.getCommercial() != null : false) {
//                y.setAuthor(UtilsProject.currentUser);
//                y.setDateSave(new Date());
//                y.setDateUpdate(new Date());
//                if (y.getId() != null ? y.getId() < 1 : true) {
//                    y.setId(null);
//                    y = (YvsComCommercialVente) rq.save1(y);
//                } else {
//                    rq.update(y);
//                }
//                if (y.getResponsable()) {
//                    if (y.getFacture() != null ? y.getCommercial().getTiers() != null : false) {
//                        if (y.getFacture().getTiers() != null ? !y.getFacture().getTiers().getId().equals(y.getCommercial().getTiers().getId()) : true) {
//                            YvsComClient tiers = null;
//                            if (y.getCommercial().getTiers().getId() > 0 ? y.getCommercial().getTiers().getClients() != null ? !y.getCommercial().getTiers().getClients().isEmpty() : false : false) {
//                                tiers = y.getCommercial().getTiers().getClients().get(0);
//                            }
//                            Options[] param = new Options[]{new Options(y.getFacture().getId(), 1), new Options(tiers.getId(), 2)};
//                            String query = "update yvs_com_doc_ventes set tiers = null where id = ?";
//                            if (y.getCommercial().getTiers().getId() > 0) {
//                                param = new Options[]{new Options(y.getCommercial().getTiers().getId(), 1), new Options(y.getFacture().getId(), 2)};
//                            }
//                            rq.requeteLibre(query, param);
//                        }
//                    }
//                }
//            }
//        } catch (Exception ex) {
//
//        }
//    }
//
//
//    private LongProperty time = new SimpleLongProperty();
//
//    public Long getTime() {
//        return time.get();
//    }
//
//    public void setTime(Long time) {
//        this.time.set(time);
//    }
//
//    public LongProperty getTimeProperty() {
//        return time;
//    }
//
//    public class Clock extends Task<Long> {
//
//        long time = System.currentTimeMillis();
//        Date d;
//
//        @Override
//        protected Long call() throws Exception {
//            while (true) {
//                long duree = System.currentTimeMillis() - time - 3600000;
//////                duree = System.currentTimeMillis() - time;
//                d = new Date(duree);
////                Platform.runLater(new Runnable() {
////                    @Override
////                    public void run() {
////                        SESS_DUREE.setText(Constantes.HMS.format(d));
////                    }
////                });
//                setTime(duree);
//                Thread.sleep(1000);
//            }
//        }
//    }
//
    @FXML
    private void testPrint(ActionEvent ev) {
////        System.err.println(" ---- Print ticket test....");
////        PrintTiket pt = new PrintTiket(this, (Onglets) tabView.getSelectionModel().getSelectedItem(), 0, "XX");
////        Thread t = new Thread(pt);
//////                PrintTiket print = new PrintTiket(page, selectOnglet, montantAvance, "XX");
//////                print.run();
////        t.start();
    }
//
//    public class Facture implements Runnable {
//
//        YvsComClient client;
//        String typeDoc;
//        YvsDictionnaire adresse;
//        String nameClient;
//        Date dateLiv;
//        String telephone;
//        private Long result;
//        private YvsComDocVentes facture;
//
//        public Facture(YvsComClient client, String typeDoc, YvsDictionnaire adresse, String nameClient, Date dateLiv, String telephone) {
//            this.client = client;
//            this.typeDoc = typeDoc;
//            this.adresse = adresse;
//            this.nameClient = nameClient;
//            this.dateLiv = dateLiv;
//            this.telephone = telephone;
//        }
//
//        public Long getResult() {
//            return result;
//        }
//
//        public void setResult(Long result) {
//            this.result = result;
//        }
//
//        public YvsComDocVentes getFacture() {
//            return facture;
//        }
//
//        public void setFacture(YvsComDocVentes facture) {
//            this.facture = facture;
////        }
//
//        @Override
//        public void run() {
//            if (UtilsProject.headerDoc != null) {
//                if (!UtilsProject.headerDoc.getCloturer()) {
//                    String numDoc = UtilsProject.generatedNumDoc((typeDoc.equals(Constantes.TYPE_FV)) ? Constantes.TYPE_FV_NAME : Constantes.TYPE_BCV_NAME);
//                    if (client != null && numDoc != null ? !numDoc.isEmpty() : false) {
//                        Requete rq = new Requete();
//                        final YvsComDocVentes bean = new YvsComDocVentes();
//                        bean.setNumDoc(numDoc);
//                        bean.setLivraisonAuto(true);
//                        bean.setActif(true);
//                        bean.setAdresse(adresse);
//                        bean.setAuthor(UtilsProject.currentUser);
//                        bean.setCategorieComptable(client.getCategorieComptable());
//                        bean.setClient(client);
//                        bean.setCloturer(Boolean.FALSE);
//                        bean.setConsigner(Boolean.FALSE);
//                        bean.setCommision(0d);
//                        bean.setDateSave(new Date());
//                        bean.setDateUpdate(new Date());
//                        bean.setDateSolder(new Date());
//                        bean.setDepotLivrer(UtilsProject.depotLivraison);
//                        bean.setEnteteDoc(UtilsProject.headerDoc);
//                        bean.setEtapeTotal(1);
//                        bean.setHeureDoc(new Date());
//                        bean.setModelReglement(UtilsProject.modelReg);
//                        bean.setMouvStock(Boolean.TRUE);
//                        bean.setNomClient(nameClient);
//                        bean.setStatut(Constantes.ETAT_EDITABLE);
//                        bean.setStatutLivre(Constantes.ETAT_ATTENTE);
//                        bean.setStatutRegle(Constantes.ETAT_ATTENTE);
//                        bean.setTypeDoc(typeDoc);
//                        bean.setDateLivraisonPrevu(dateLiv);
//                        bean.setTelephone(telephone);
//                        bean.setOperateur(UtilsProject.currentUser.getUsers());
//                        if (client.getSuiviComptable()) {
//                            bean.setTiers(client);
//                        }
//                        if (bean.getTiers() == null) {
//                            //ne pas enregistrer
//                            LymytzService.openAlertDialog("Le tiers rattaché à ce client n'existe pas !", "Action abandonné !", "Erreur ", Alert.AlertType.ERROR);
//                            result = -1L;
//                        }
//                        bean.setSupp(false);
//                        YvsComDocVentes d = (YvsComDocVentes) rq.save1(bean);
//                        bean.setId(d.getId());
//                        setFacture(bean);
//                        Thread t = new Thread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                saveCurrentCommercial(bean);
//                            }
//                        });
//                        t.start();
//                        BTN_REGLER.setVisible(bean.getStatutRegle().equals(Constantes.ETAT_REGLE));
//                        BTN_LIVRER.setVisible(bean.getStatutLivre().equals(Constantes.ETAT_LIVRE));
//                        result = bean.getId();
//                    } else {
//                        if (client == null) {
//                            result = -2L;
//                        } else {
//                            result = -3L;
//                        }
//                    }
//                } else {
//                    result = -4L;
//                }
//            } else {
//                result = -5L;
//            }
//            result = -10L;
//             //
//
//            //display result
//            switch (result.intValue()) {
//                case -1:
//                    Platform.runLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            LymytzService.openAlertDialog("Le tiers rattaché à ce client n'existe pas !", "Action abandonné !", "Erreur ", Alert.AlertType.ERROR);
//                        }
//                    });
//                    break;
//                case -2:
//                    Platform.runLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            LymytzService.openAlertDialog("Génération de la facture non réussi !", "Erreur ", "Action abandonné !", Alert.AlertType.ERROR);
//                        }
//                    });
//                    break;
//                case -3:
//                    Platform.runLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            LymytzService.openAlertDialog("Génération de a facture non réussi !", "Erreur ", "Le numéro de référence n'a pas pu être généré !", Alert.AlertType.ERROR);
//                        }
//                    });
//                    break;
//                case -4:
//                    Platform.runLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            LymytzService.openAlertDialog("Génération de a facture non réussi !", "Erreur ", "Votre fiche de vente est déjà clôturé !", Alert.AlertType.ERROR);
//                        }
//                    });
//                    break;
//                case -5:
//                    Platform.runLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            LymytzService.openAlertDialog("Génération de a facture non réussi !", "Erreur ", "Aucun entête n'a été trouvé !", Alert.AlertType.ERROR);
//                        }
//                    });
//                    break;
//                default:
//                    Platform.runLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            displayDetailFacture(null);
//                            initTabPane(facture);
//                            TEXT_FIND.setText("");
//                            BTN_REGLER.setVisible(getFacture().getStatutRegle().equals(Constantes.ETAT_REGLE));
//                            BTN_LIVRER.setVisible(getFacture().getStatutLivre().equals(Constantes.ETAT_LIVRE));
//                            TEXT_FIND.requestFocus();
////                            afterCreateFacture();
//                        }
//                    });
//                    break;
//            }
//        }
//    }
}
