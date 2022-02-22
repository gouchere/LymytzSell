/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.view.main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.print.attribute.standard.Severity;
import lymytz.dao.Options;
import lymytz.dao.ParamConnection;
import lymytz.dao.UtilsBean;
import lymytz.dao.entity.YvsBaseArticleCategorieComptable;
import lymytz.dao.entity.YvsBaseArticleCategorieComptableTaxe;
import lymytz.dao.entity.YvsBaseCategorieComptable;
import lymytz.dao.entity.YvsBaseConditionnement;
import lymytz.dao.entity.YvsBaseDepots;
import lymytz.dao.entity.YvsComContenuDocVente;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.dao.entity.YvsComEnteteDocVente;
import lymytz.dao.entity.YvsComTaxeContenuVente;
import lymytz.dao.query.LQueryFactories;
import lymytz.dao.query.RQueryFactories;
import lymytz.service.ClientMessage;
import lymytz.service.ServeurMessage;
import lymytz.service.application.ManagedApplication;
import lymytz.service.application.FactureController;
import lymytz.service.application.FormCatalogueController;
import lymytz.service.application.MyComptesController;
import lymytz.service.application.PreferenceController;
import lymytz.service.application.bean.ContentPanier;
import lymytz.service.application.composant.ClaviersController;
import lymytz.service.application.composant.Onglets;
import lymytz.service.application.loader.LoaderConditionnement;
import lymytz.service.application.loader.LoaderStock;
import lymytz.service.application.service.ListenServersLocal;
import lymytz.service.application.service.ListenServersRemote;
import lymytz.service.application.service.ServiceCreateFacture;
import lymytz.service.application.service.ServiceLivraison;
import lymytz.service.application.service.mail.SendMail;
import lymytz.service.application.service.reglement.FormVirementController;
import lymytz.service.application.service.reglement.ServiceReglement;
import lymytz.service.application.synchro.ControlServiceController;
import lymytz.service.application.synchro.export.ExportDataController;
import lymytz.service.application.synchro.impor.ImportDataController;
import lymytz.service.application.synchro.impor.ListenRemoteTableController;
import lymytz.service.application.synchro.ListenTableController;
import lymytz.service.application.synchro.SynchronizeDataIn;
import lymytz.service.application.synchro.SynchronizeDataOut;
import lymytz.service.application.synchro.SynchronizeDeleteFacture;
import lymytz.service.application.synchro.export.UtilExport;
import lymytz.service.start.StartController;
import lymytz.service.utils.ConsUtil;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.CustomWindow;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.PrintTiket;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;
import lymytz.synchro.ws.WsSynchro;
import lymytz.view.LocalLoader;
import lymytz.view.component.CustomComponents;
import lymytz.view.data.ReadLogController;
import lymytz.view.main.report.PrintFacture;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class HomeCaisseController extends ManagedApplication implements Initializable {

    private BooleanProperty connectRemoteServer;
    ClientMessage clientSocket;

    public SynchronizeDataOut myServiceOut;
    public SynchronizeDataIn myServiceIn;
    public SynchronizeDeleteFacture myServiceDel;
    public Stage stageCreateFacture;
    @FXML
    private MenuBar HOMEMENU;
    @FXML
    public ToolBar TOOLBAR;
    @FXML
    public Button BTN_NEW_CMDE;
    @FXML
    public Button BTN_LIST_FAC;
    @FXML
    public Button BTN_COMPTE;
    @FXML
    public Button BTN_QUIT;
    @FXML
    private VBox MAIN_PAGE;
    @FXML
    public SplitPane SPLIT_CENTER;
    @FXML
    public VBox RIGHT_BOX;
    @FXML
    public Label ECRAN;
    @FXML
    public Label LAB_TITLE_PANIER;

    @FXML
    public Label LAB_NB_DATA_IN;
    @FXML
    public Label LAB_NB_DATA_OUT;
    @FXML
    public ImageView IMG_ETAT_SERVEUR_L;
    @FXML
    public ImageView IMG_ETAT_SERVEUR_R;

    //MENU
    @FXML
    public Menu MEN_SYNCHRO;
    //MENUITEM
    @FXML
    public MenuItem ITEM_NEW;
    @FXML
    public MenuItem ITEM_PREF;
    @FXML
    public MenuItem ITEM_COMPTE;
    @FXML
    public MenuItem ITEM_QUIT;
    @FXML
    public MenuItem ITEM_ABOUT;
    @FXML
    public MenuItem ITEM_DOC;
    @FXML
    public MenuItem ITEM_PING_L;
    @FXML
    public MenuItem ITEM_PING_R;
    @FXML
    public MenuItem ITEM_FACTURE;
    @FXML
    public MenuItem ITEM_COMANDE;
    @FXML
    public MenuItem ITEM_CATALOG;
    @FXML
    public MenuItem ITEM_IMPORT;
    @FXML
    public MenuItem ITEM_EXPORT;
    @FXML
    public MenuItem ITEM_SERVICE;
    @FXML
    public MenuItem ITEM_RELOAD_PPTE;
    @FXML
    public MenuItem ITEM_CLEAN;

    //
    @FXML
    public CheckMenuItem ITEM_DISPLAY_TOOLS;

    //Contrôles facture 
    @FXML
    public Button BTN_PRINT;
    @FXML
    public Button BTN_REGLER;
    @FXML
    public Button BTN_LIVRER;
    @FXML
    public Button BTN_SAVE;
    @FXML
    public TextField TEXT_FIND;
    @FXML
    public CheckBox CHK_DISPLAY;

    @FXML
    public VBox BOX_ARTICLES;
    @FXML
    public ProgressBar PROGRESS;
    @FXML
    public Label PROGRESS_LABEL;
//
    @FXML
    public TabPane TAB_FACTURES;
    DoubleProperty EcranWidth, RigthBoxWidth;

    //Header page vente
    @FXML
    public Label LAB_DATE;
    @FXML
    public Label LAB_TRANCHE;
    @FXML
    public Label LAB_PV;
    @FXML
    public Label LAB_VEND;

    //Facture
    @FXML
    public Label LAB_REF_FACTURE;
    @FXML
    public Label LAB_CLIENT;
    @FXML
    private Label LAB_ADRESSSE;
    @FXML
    private CheckBox CHK_LIVRE;
    @FXML
    private CheckBox CHK_REGLE;
    @FXML
    private Label LAB_NAME_CLT;
    @FXML
    private Label L_TOTAL;
    //Le montants
    @FXML
    public Label LAB_T_REM;
    @FXML
    public Label LAB_T_AVANCE;
    @FXML
    public Label LAB_TTC;
    @FXML
    public Label LAB_NET_A_PAYER;
    @FXML
    public Label LAB_T_RIST;
//contôleur visuel de synchro sortant (export des données)
    @FXML
    public ImageView ICO_RUN_ON;
    @FXML
    public ImageView ICO_RUN_OFF;
    @FXML
    public ImageView ICO_ALERT_EX;
    @FXML
    public ImageView ICO_ALERT_IM;
    @FXML
    public Label LAB_TITLE_SYNC_T;
    @FXML
    public Label LAB_SYNC_EX;
    @FXML
    public Label LAB_SYNC_IM;

    //contôleur visuel de synchro entrant (import des données de base)
    @FXML
    public ImageView ICO_RUN_ON_IN;
    @FXML
    public ImageView ICO_RUN_OFF_IN;

    @FXML
    public Label LAB_SYNC_MSG;
    //display articles
    @FXML
    public Label LAB_REF;
    @FXML
    public Label LAB_DES;
    @FXML
    public Label QTE_FACTURE;
    public Label LAB_DES2;
    public Label LAB_FAM;
    public Label LAB_GROUP;
    public Label LAB_FABRICANT;
    public Label LAB_PUV;
    public Label LAB_REM;
    public Label LAB_PUV_TTC;
    public Label LAB_PUV_V;
    //
    @FXML
    private VBox ZONE_IMG;

    @FXML
    private Label SESS_DUREE;

    //Footer
    @FXML
    private Label TEXT_SOCIETE;

    public HomeCaisseController() {
    }

    public Boolean getConnectRemoteServer() {
        return connectRemoteServer.get();
    }

    public void setConnect(Boolean connect) {
        this.connectRemoteServer.setValue(connect);
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connectRemoteServer = new SimpleBooleanProperty();
        initComponent();
        setMainPage(this);
        //Attacher un listener au text find
        TEXT_FIND.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if (CHK_DISPLAY.isSelected()) {
                    if (Constantes.asString(TEXT_FIND.getText())) {
                        loadCatalogue(TEXT_FIND.getText());
                    }
                } else {
                    Onglets tab = (Onglets) TAB_FACTURES.getSelectionModel().getSelectedItem();
                    if (tab != null) {
                        if (Constantes.asString(TEXT_FIND.getText())) {
                            LoaderConditionnement tache1 = new LoaderConditionnement(HomeCaisseController.this, TEXT_FIND.getText());
                            YvsBaseConditionnement art = tache1.findOneArticle(TEXT_FIND.getText());
                            if (art != null) {
                                tab.addArticleOnFacture(art, 1, false, art.getPrix());
                            } else {

                            }
                        }
                    } else {
                        LymytzService.openAlertDialog("Aucune facture n'a été trouvé !", "Erreur", "Vous devez enregistrer la facture !", Alert.AlertType.ERROR);
                    }
                }
            }
        });
        TEXT_FIND.setOnKeyReleased((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (CHK_DISPLAY.isSelected()) {
                    if (Constantes.asString(TEXT_FIND.getText())) {
                        loadCatalogue(TEXT_FIND.getText());
                    }
                } else {
                    Onglets tab = (Onglets) TAB_FACTURES.getSelectionModel().getSelectedItem();
                    if (tab != null) {
                        if (Constantes.asString(TEXT_FIND.getText())) {
                            LoaderConditionnement tache1 = new LoaderConditionnement(HomeCaisseController.this, TEXT_FIND.getText());
                            YvsBaseConditionnement art = tache1.findOneArticle(TEXT_FIND.getText());
                            if (art != null) {
                                tab.addArticleOnFacture(art, 1, false, art.getPrix());
                            } else {
                                
                            }
                        }
                    } else {
                        LymytzService.openAlertDialog("Aucune facture n'a été trouvé !", "Erreur", "Vous devez enregistrer la facture !", Alert.AlertType.ERROR);
                    }
                }
            }
        });
    }

    public void initComponent() {
        //Mettre le menubar à la dimension de la fenêtre.
//        Scale scale = new Scale(1, 1, 0, 0);
        HOMEMENU.setPrefWidth(StartController.SCREENWIDTH);
        MAIN_PAGE.setPrefWidth(StartController.SCREENWIDTH - 5);
        MAIN_PAGE.setPrefHeight(StartController.SCREENHEIGHT);
        BOX_ARTICLES.setPrefHeight(StartController.SCREENHEIGHT - 315);
        TAB_FACTURES.setPrefHeight(StartController.SCREENHEIGHT - 345);
        RigthBoxWidth = new SimpleDoubleProperty(RIGHT_BOX.getPrefWidth());
        CustomComponents.custumMenuAndToolBar(this);
        CustomComponents.initEventComponents(this);
        if (UtilsProject.currentAgence != null && UtilsProject.currentSociete != null) {
            TEXT_SOCIETE.setText(UtilsProject.currentSociete.getName() + "[" + UtilsProject.currentAgence.getDesignation() + "]");
        }
        loadCatalogue(" ");
        //Lance l'horloge d'écoulement du temps
        time.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            Platform.runLater(() -> {
                SESS_DUREE.setText(Constantes.HMS.format(new Date(getTime())));
            });
        });
        connectRemoteServer.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            //ecrit sur la socket: seul le serveur peut écrire 
            if (newValue != null) {
                if (UtilsProject.properties.getProperty(Constantes.KEY_MODE).equals(Constantes.APPS_MODE_BOTH)) {
                    ServeurMessage.writeMessage(newValue);
                    LogFiles.addLogInFile(null, Severity.REPORT, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, null);
                }
            }
        });
        String name=(UtilsProject.currentUser!=null)?UtilsProject.currentUser.getUsers().getNomUsers():(UtilsProject.MODE_ADMIN?"ADMINISTRATEUR":"---");
        LAB_VEND.setText(name);
        Thread ttemp = new Thread(new Clock());
        ttemp.setName("Horloge locale");
        ttemp.start();
        loadProperties();
        //Code temporaire...
        if (UtilsProject.currentUser != null) {
            if (UtilsProject.currentUser.getUsers().getCodeUsers().equals("ADMINGLP")) {
                ITEM_RELOAD_PPTE.setVisible(true);
                ITEM_PREF.setVisible(true);
            } else {
                ITEM_RELOAD_PPTE.setVisible(false);
                ITEM_PREF.setVisible(false);
                ITEM_CLEAN.setVisible(false);
            }
        }
    }

    public GridPane displayCatalogue() {
        GridPane gp = new GridPane();
        gp.getStylesheets().add("background_art");
        gp.setPrefHeight(140);
        gp.setPrefWidth(640);
        gp.setHgap(3);
        gp.setGridLinesVisible(true);
        return gp;
    }

    LoaderConditionnement tache;

    private void loadCatalogue(String ref) {
        tache = new LoaderConditionnement(this, ref);
        try {
            if (UtilsProject.depotLivraison != null && ref != null) {
                BOX_ARTICLES.getChildren().clear();
                PROGRESS.progressProperty().unbind();
                PROGRESS_LABEL.textProperty().unbind();
                PROGRESS.progressProperty().bind(tache.progressProperty());
                PROGRESS_LABEL.textProperty().bind(tache.messageProperty());
                tache.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        ObservableList<GridPane> value = tache.getValue();
                        if (value != null) {
                            BOX_ARTICLES.getChildren().addAll(value);
                            PROGRESS_LABEL.textProperty().unbind();
                        }
                        if (value != null ? value.size() > 0 : false) {
                            PROGRESS_LABEL.setText("terminé !");
                        } else {
                            PROGRESS_LABEL.setText("Aucun résultat trouvé !");
                        }
                    }
                });
                new Thread(tache).start();
            }
//            tache.call();
        } catch (Exception ex) {
            Logger.getLogger(HomeCaisseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void displayPropertiesFiche(YvsComEnteteDocVente head) {
        if (head != null) {
            LAB_DATE.setText(Constantes.dfD.format(head.getDateEntete()));
            LAB_PV.setText(head.getCreneau().getCreneauPoint().getPoint().getLibelle());
            LAB_VEND.setText(head.getCreneau().getUsers().getNomUsers());
            UtilsProject.depotLivraison = (head.getCreneau().getCreneauDepot() != null) ? head.getCreneau().getCreneauDepot().getDepot() : null;
            UtilsProject.trancheLivraison = (head.getCreneau().getCreneauDepot() != null) ? head.getCreneau().getCreneauDepot().getTranche() : null;
            if (UtilsProject.depotLivraison == null) {
                UtilsProject.depotLivraison = head.getCreneau().getCreneauDepot().getDepot();
            }
            if (UtilsProject.trancheLivraison == null) {
                UtilsProject.trancheLivraison = head.getCreneau().getCreneauPoint().getTranche();
            }
            LAB_TRANCHE.setText(UtilsProject.trancheLivraison.getTitre());
            //Récupère les ids des dépôts lié au point de vente courant
            setIdDepots(dao.loadByNamedQuery("YvsBasePointVenteDepot.findIdDepotByPoint", new String[]{"pointVente"}, new Object[]{head.getCreneau().getCreneauPoint().getPoint()}));
        }
    }

    public void resetAllView(YvsComEnteteDocVente head) {
        if (head != null && UtilsProject.headerDoc != null) {
            if (UtilsProject.headerDoc.equals(head)) {
                displayDetailFacture(null);
            }
        }
    }

    public void displayDetailFacture(YvsComDocVentes facture) {
        if (facture != null) {
            if (Constantes.asLong(facture.getId())) {
                LAB_REF_FACTURE.setText(facture.getNumDoc());
            } else {
                LAB_REF_FACTURE.setText(facture.getNumDoc() + ":" + facture.getClient().getCodeClient() + "" + facture.getId());
            }
            LAB_CLIENT.setText(facture.getClient().getCodeClient());
            CHK_LIVRE.setSelected(facture.getStatutLivre().equals(Constantes.ETAT_LIVRE));
            CHK_REGLE.setSelected(facture.getStatutRegle().equals(Constantes.ETAT_REGLE));
            LAB_ADRESSSE.setText(facture.getAdresse() != null ? facture.getAdresse().getLibele() : "");
            LAB_NAME_CLT.setText(facture.getNomClient());
            double avance = facture.getMontantAvance();

            Onglets on = (Onglets) TAB_FACTURES.getSelectionModel().getSelectedItem();
            if (on != null) {
                on.setNetAPayer(facture.getMontantResteApayer());
                LAB_T_AVANCE.setText(Constantes.nbf.format(avance));
                LAB_NET_A_PAYER.setText(Constantes.nbf.format(on.getNetAPayer()));
            }
            if (facture.getStatut().equals(Constantes.ETAT_VALIDE) || facture.getStatut().equals(Constantes.ETAT_CLOTURE)) {
                BTN_SAVE.setVisible(false);
                BTN_PRINT.setVisible(true);
            } else {
                BTN_SAVE.setVisible(true);
                BTN_PRINT.setVisible(false);
            }
            BTN_REGLER.setVisible(!facture.getStatutRegle().equals(Constantes.ETAT_REGLE));
            BTN_LIVRER.setVisible(!facture.getStatutLivre().equals(Constantes.ETAT_LIVRE));
        } else {
            BTN_LIVRER.setVisible(false);
            BTN_REGLER.setVisible(false);
            LAB_REF_FACTURE.setText(null);
            LAB_CLIENT.setText(null);
            CHK_LIVRE.setSelected(false);
            CHK_REGLE.setSelected(false);
            LAB_ADRESSSE.setText(null);
            LAB_NAME_CLT.setText(null);
            BTN_SAVE.setVisible(TAB_FACTURES.getTabs().size() > 0);
            BTN_PRINT.setVisible(false);
            ECRAN.setText("0");
        }
    }

    public void displayPropertyArticle(YvsBaseConditionnement art, boolean allppte) {
        if (art != null) {
            LAB_REF.setText(art.getArticle().getRefArt());
            LAB_DES.setText(art.getArticle().getDesignation());
            ZONE_IMG.getChildren().clear();
            createImageProduit(art.getArticle());
            ZONE_IMG.getChildren().add(pagination);
            List<YvsBaseDepots> depots = null;
            if (allppte) {
                depots = dao.loadByNamedQuery("YvsBaseArticleDepot.findDepotActifByArt", new String[]{"article"}, new Object[]{art.getArticle()});
                depots.remove(UtilsProject.depotLivraison);
                depots.add(0, UtilsProject.depotLivraison);
            } else {
                depots = new ArrayList<>();
                depots.add(UtilsProject.depotLivraison);
            }
            PAN_STOCK.getChildren().clear();
            if (UtilsProject.headerDoc != null ? UtilsProject.headerDoc.getCreneau() != null : false) {
                //compte la quantité de l'article facturé
                Double qte = (Double) dao.findOneObjectByNQ("YvsComContenuDocVente.countQteVendu", new String[]{"conditionnement", "header"}, new Object[]{art, UtilsProject.headerDoc});
                if (qte != null ? qte > 0 : false) {
                    QTE_FACTURE.setText(Constantes.nbf.format(qte));
                } else {
                    QTE_FACTURE.setText(Constantes.nbf.format(0));
                }
                if (!UtilsProject.REPLICATION && UtilsProject.depotLivraison != null) {
                    //si on est pas en mode replication, calcul immédiatement le stock
                    Double stock = UtilsProject.getStocks(art, UtilsProject.depotLivraison.getId());
                    stock = (stock != null) ? stock : 0d;
                    art.setStock(stock);
                }
                LoaderStock service = new LoaderStock(this, depots, art);
                service.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        VBox re = service.getValue();
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                PAN_STOCK.getChildren().clear();
                                PAN_STOCK.getChildren().add(re);
                            }
                        });
                    }
                });
                new Thread(service).start();
            }
        }
        //Afficher la description de l'article
    }

    public void createFactureDivers() {
// créée une nouvelle facture     
        try {
            if (UtilsProject.headerDoc != null) {
                ServiceCreateFacture f = new ServiceCreateFacture(UtilsProject.clientDivers, Constantes.TYPE_FV, UtilsProject.defaultAdresse, "Client Divers", UtilsProject.headerDoc.getDateEntete(), null, this);
                Thread t = new Thread(f);
                t.start();
            } else {
                LymytzService.openAlertDialog("Génération de a facture non réussi !", "Erreur ", "Aucune entête n'a été trouvé !", Alert.AlertType.ERROR);
            }
        } catch (Exception ex) {
            LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
        }
    }

    public void initTabPane(YvsComDocVentes facture) {
        if (facture != null) {
            TAB_FACTURES.getTabs().add(0, new Onglets(facture, this));
            TAB_FACTURES.getSelectionModel().select(0);
        }
    }

    public void afterCreateFacture() {
        Long nb = (Long) dao.findOneObjectByNQ("YvsComDocVentes.countDocByHeaderAndType", new String[]{"type1", "type2", "header"}, new Object[]{Constantes.TYPE_FV, Constantes.TYPE_BCV, UtilsProject.headerDoc});
        L_TOTAL.setText(nb != null ? nb.toString() : "0");
    }
    ButtonType re;

    public void giveFocusAtTxtFind() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                TEXT_FIND.setText("");
                TEXT_FIND.requestFocus();
            }
        });
    }

    public List<YvsComContenuDocVente> buildInfoTableToContentFacure(List<ContentPanier> contents) {
        List<YvsComContenuDocVente> lc = new ArrayList<>();
        YvsComContenuDocVente bean;
        for (ContentPanier c : contents) {
            bean = new YvsComContenuDocVente(c.getIdContent());
            bean.setActif(Boolean.TRUE);
            bean.setArticle(c.getConditionnement().getArticle());
            bean.setAuthor(UtilsProject.currentUser);
            bean.setConditionnement(c.getConditionnement());
            bean.setDateContenu(new Date());
            bean.setDateSave(new Date());
            bean.setDateUpdate(new Date());
            bean.setDepotLivraisonPrevu(UtilsProject.depotLivraison);
            bean.setMouvStock(Boolean.FALSE);
            bean.setPr(c.getPr());
            bean.setPrix(c.getPrix());
            bean.setPrixTotal(c.getMontantTotal());
            bean.setPuvMin(c.getPrixMin());
            bean.setQuantite(c.getQuantite());
            bean.setRemise(c.getRemise());
            bean.setRabais(c.getRabais());
            bean.setRistourne(c.getRistourne());
            bean.setStatut(Constantes.ETAT_VALIDE);
            bean.setStatutLivree(Constantes.STATUT_DOC_ATTENTE);
            lc.add(bean);
        }
        return lc;
    }

    public boolean confirmValideFacture(Onglets fac, double montantPaye, double montantRecu, String source) {
        String etatLivre = Constantes.ETAT_ATTENTE;
        String etatRegle = Constantes.ETAT_ATTENTE;
        //5. contrôle les valeurs de paiement
        if (fac.getFacture().getTypeDoc().equals(Constantes.TYPE_FV)) {
            if (fac.getNetAPayer() != montantPaye) {
                // erreur paiement insuffisant
                Platform.runLater(() -> {
                    LymytzService.openAlertDialog("Incohérence des montants !", "Erreur", "Le montant payé de la facture est différent du TTC !", Alert.AlertType.ERROR);
                });
                return false;
            }
        } else {
            // contrôle le montant d'avance
            if (montantPaye > fac.getNetAPayer()) {
                Platform.runLater(() -> {
                    LymytzService.openAlertDialog("Incohérence des montants !", "Erreur", "Le montant d'avance de la commande est suppérieure au TTC !", Alert.AlertType.ERROR);
                });
                return false;
            }
        }
        List<YvsComContenuDocVente> l = new ArrayList<>(fac.getFacture().getContenus());
        fac.getFacture().getContenus().clear();
        YvsComDocVentes d = saveFacture(fac.getFacture());
        if (d != null) {
            fac.setFacture(d);
            fac.getFacture().setContenus(l);
            if (saveContentFacture(fac.getFacture().getContenus(), fac.getFacture())) {
//                fac.getFacture().setStatut((fac.getFacture().getTypeDoc().equals(Constantes.TYPE_FV)) ? Constantes.ETAT_VALIDE : Constantes.ETAT_EDITABLE);
                fac.getFacture().setStatut(Constantes.ETAT_VALIDE);
                fac.getFacture().setEtapeValide(1);
                fac.getFacture().setStatutLivre(etatLivre);
                fac.getFacture().setStatutRegle(etatRegle);
                fac.getFacture().setMontantAvance(montantPaye);
                List<YvsComContenuDocVente> temp = new ArrayList<>(fac.getFacture().getContenus());
                fac.getFacture().getContenus().clear();
                if (fac.getFacture().getTypeDoc().equals(Constantes.TYPE_BCV)) {
                    fac.getFacture().setLivraisonAuto(Boolean.FALSE);
                }
                dao.update(fac.getFacture());
                fac.getFacture().getContenus().addAll(temp);
                Thread tcompta = new Thread(() -> {
                    saveLivraisonAndreglement(new YvsComDocVentes(fac.getFacture()), montantPaye, montantRecu);
                    if (!UtilsProject.REPLICATION && fac.getFacture().getTypeDoc().equals(Constantes.TYPE_FV)) {
                        comptabilise(fac.getFacture().getId(), fac.getFacture().getNumDoc());
                    }
                });
                tcompta.start();
                if (!source.equals("A")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            TAB_FACTURES.getTabs().remove(fac);
                            if (!TAB_FACTURES.getTabs().isEmpty()) {
                                TAB_FACTURES.getSelectionModel().select(0);
                            } else {

                            }
                        }
                    });
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private YvsComDocVentes saveFacture(YvsComDocVentes doc) {
        if (UtilsProject.headerDoc != null) {
            String numDoc = UtilsProject.generatedNumDoc((doc.getTypeDoc().equals(Constantes.TYPE_FV)) ? Constantes.TYPE_FV_NAME : Constantes.TYPE_BCV_NAME);
            if (Constantes.asString(numDoc)) {
                doc.setId(null);
                doc.setNumDoc(numDoc);
                doc.setNumPiece(numDoc);
                doc.setNumeroExterne(numDoc);
                doc.setEnteteDoc(UtilsProject.headerDoc);
                System.err.println(" ... Date Liv"+Constantes.dfD.format(doc.getDateLivraisonPrevu()));
                doc = (YvsComDocVentes) dao.save1(doc);
                new ServiceCreateFacture(this).saveCurrentCommercial(doc);
            } else {
                Platform.runLater(() -> {
                    LymytzService.openAlertDialog("Génération de a facture non réussi !", "Erreur ", "Le numéro de référence n'a pas pu être généré !", Alert.AlertType.ERROR);
                });
                return null;
            }
        } else {
            Platform.runLater(() -> {
                LymytzService.openAlertDialog("Génération de a facture non réussi !", "Erreur ", "L'en-tête de la facture n'a pas été trouvé!", Alert.AlertType.ERROR);
            });
            return null;
        }
        return doc;
    }

    private boolean saveContentFacture(List<YvsComContenuDocVente> contents, YvsComDocVentes doc) {
        if (!contents.stream().noneMatch((c) -> (!saveContentFacture(c, doc)))) {
            return false;
        }
        return true;
    }

    private boolean saveContentFacture(YvsComContenuDocVente c, YvsComDocVentes doc) {
        c.setDocVente(doc);
        c.setParent(null);
        if (c.getId() <= 0) {
            c.setId(null);
            c = (YvsComContenuDocVente) dao.save1(c);
            c.setId(c.getId());
            //Save les taxes
            saveAllTaxe(c);
        } else {
            dao.update(c);
        }
        return true;
    }

    public void saveAllTaxe(YvsComContenuDocVente y) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            double prix = y.getPrix() - y.getRabais();
            double qte = y.getQuantite();
            double remise = y.getRemise();
            double taxe = 0;
            double valeur = 0;

            long categorie = y.getDocVente().getCategorieComptable().getId();

            String nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticle";
            YvsBaseArticleCategorieComptable acc = (YvsBaseArticleCategorieComptable) dao.findOneByNQ(nameQueri, new String[]{"categorie", "article"}, new Object[]{new YvsBaseCategorieComptable(categorie), y.getArticle()});
            if (acc != null ? (acc.getId() != null ? acc.getId() > 0 : false) : false) {
                if (y.getArticle().getPuvTtc()) {
                    for (YvsBaseArticleCategorieComptableTaxe t : acc.getTaxes()) {
                        taxe += t.getTaxe().getTaux();
                    }
                    prix = prix / (1 + (taxe / 100));
                }
                valeur = qte * prix;
                for (YvsBaseArticleCategorieComptableTaxe t : acc.getTaxes()) {
                    taxe = 0;
                    if (t.getAppRemise()) {
                        taxe = (((valeur - remise) * t.getTaxe().getTaux()) / 100);
                    } else {
                        taxe = ((valeur * t.getTaxe().getTaux()) / 100);
                    }
                    taxe = dao.arrondi(UtilsProject.currentSociete.getId(), taxe);

                    YvsComTaxeContenuVente ct = (YvsComTaxeContenuVente) dao.findOneByNQ("YvsComTaxeContenuVente.findOne", new String[]{"contenu", "taxe"}, new Object[]{y, t.getTaxe()});
                    if (ct != null ? (ct.getId() != null ? ct.getId() > 0 : false) : false) {
                        ct.setMontant(taxe);
                        ct.setAuthor(UtilsProject.currentUser);
                        dao.update(ct);
                    } else {
                        ct = new YvsComTaxeContenuVente();
                        ct.setContenu(y);
                        ct.setMontant(taxe);
                        ct.setTaxe(t.getTaxe());
                        ct.setAuthor(UtilsProject.currentUser);
                        dao.save1(ct);
                    }
                    int idx = y.getTaxes().indexOf(ct);
                    if (idx > -1) {
                        y.getTaxes().set(idx, ct);
                    } else {
                        y.getTaxes().add(0, ct);
                    }
                }
            }
        }
    }

    private void saveLivraisonAndreglement(YvsComDocVentes facture, double montantPaye, double montantRecu) {
        //2. Enregistrer la pièce de règlement       
        new UtilsBean().setMontantTotalDoc(facture, facture.getContenus());
        ServiceReglement service = new ServiceReglement(this);
        String etatRegle = service.saveReglementFacture(facture, montantPaye, montantRecu);
        //3. Enregistrer le document de livraison. 
        //on enregistre directement que si on n'est pas en mode replication (car si on est en mode replication, le BL sera géré par le serveur d'application dès la validation de la facture)
        if (!UtilsProject.REPLICATION) {
            if (!facture.getTypeDoc().equals(Constantes.TYPE_BCV)) {
                ServiceLivraison serviceL = new ServiceLivraison(this);
                serviceL.saveLivraison(facture, false);
            } else {
                String etatLivre = Constantes.ETAT_ATTENTE;
            }
        }
        dao.findOneObjectBySQLQ("SELECT equilibre_vente_regle(?,?)", new Options[]{new Options(facture.getId(), 1), new Options(true, 2)});
    }

    public void saveReglement(YvsComDocVentes facture, double montant, double montantRecu) {
        ServiceReglement service = new ServiceReglement(this);
        String etat = service.saveReglementAvance(facture, montant, montantRecu);
        facture.setStatutRegle(etat);
        facture.setMontantAvance(facture.getMontantAvance() + montant);
        facture.setDateUpdate(new Date());
        facture.setAuthor(UtilsProject.currentUser);
        facture.setContenus(null);
        facture.setReglements(null);
        dao.update(facture);
    }

    public void displayFactureOnView(YvsComDocVentes doc) {
        //Crée ou récupère l'onglet
        Onglets ong = new Onglets(doc, this);
        int idx = TAB_FACTURES.getTabs().indexOf(ong);
        if (idx < 0) {
            TAB_FACTURES.getTabs().add(0, ong);
            TAB_FACTURES.getSelectionModel().select(0);
        } else {
            TAB_FACTURES.getSelectionModel().select(ong);
        }
        //charge le contenu
        ong.loadContentOnView(doc);
        // trouve le montant reçu
        Double recu = (Double) dao.findOneObjectByNQ("YvsComptaCaissePieceVente.findMontantRecuByFacture", new String[]{"vente"}, new Object[]{doc});
        ong.setMontantRecu(recu != null ? recu : 0);
    }
//
    /*Barre d'outils*/
    int num = 5;

    @FXML
    private void valideFacture(ActionEvent event) {
        ServiceCreateFacture service = new ServiceCreateFacture(this);
        service.valideFacture();
    }

    /*Actions sur les menus*/
    @FXML
    public void openViewImport(ActionEvent ev) {
        //Ouvre la fenêtre de gestion des imports
        BorderPane root = null;
        long memoire = Runtime.getRuntime().freeMemory();
        CustomWindow w = LymytzService.openWindowNew("main/synchro/import_data.fxml", "Lymytz /Importation", root, 1000.0, 500.0, true);
        ImportDataController controler = (ImportDataController) w.getController();
        this.stageCreateFacture = w.getStage();
        if (controler != null) {
            controler.initComponents(this);
        }
        long memoire2 = Runtime.getRuntime().freeMemory();
    }

    @FXML
    public void openViewExport(ActionEvent ev) {
        //Ouvre la fenêtre de gestion des imports
        BorderPane root = null;
        ExportDataController controler = LymytzService.openWindow("main/synchro/export_data.fxml", "Lymytz /Exportation", root, 1000.0, 600.0);
    }

    @FXML
    public void openViewControlService(ActionEvent ev) {
        //Ouvre la fenêtre de gestion des imports
        VBox root = null;
        ControlServiceController controler = LymytzService.openWindow("main/synchro/form_control_service.fxml", "Etat Service", root, 460d, 300d);
        controler.initPage(this, this.myServiceOut, this.myServiceIn);
    }

    @FXML
    public void openViewLog(ActionEvent ev) {
        //Ouvre la fenêtre de gestion des imports
        VBox root = null;
        ReadLogController controler = LymytzService.openWindow("data/read_log.fxml", "Log_", root, 630d, 500d);
//        controler.initPage(this, this.myServiceOut, this.myServiceIn);
    }

    @FXML
    public void openViewPreference(ActionEvent ev) {
        //Ouvre la fenêtre de gestion des imports
        VBox root = null;
        PreferenceController controler = LymytzService.openWindow("main/preference.fxml", "Lymytz /Préférence", root, 550.0, 600.0);
    }

    @FXML
    public void openViewCatalogue(ActionEvent ev) {
        //Ouvre la fenêtre de gestion des imports
        VBox root = null;
        FormCatalogueController controler = LymytzService.openWindow("data/form_catalogue.fxml", "Lymytz /Catalogue", root, 1000.0, 550.0);
    }

    @FXML
    private void testRemoteConnect(ActionEvent event) {
        if (!RQueryFactories.pingServer()) {
            LymytzService.openAlertDialog("Impossible d'ouvrir une connexion à la base de données distante, veuillez consulter le fichier de log pour en connaître la cause", "Echec de connexion", "Echec de connexion à la source de données distante", Alert.AlertType.ERROR);
        } else {
            LymytzService.success();
        }

    }

    @FXML
    private void testLocalConnect(ActionEvent event) {
        if (!LQueryFactories.pingServer()) {
            LymytzService.openAlertDialog("Impossible d'ouvrir une connexion à la base de données locale, veuillez consulter le fichier de log pour en connaître la cause", "Echec de connexion", "Echec de connexion à la source de données locale", Alert.AlertType.ERROR);
        } else {
            LymytzService.success();
        }
    }

    @FXML
    private void openViewAbout(ActionEvent event) {
        VBox root = null;
        LymytzService.openWindow("component/form_about.fxml", "Lymytz /A propos", root, 400.0, 305.0, false);
    }

    @FXML
    private void openViewRaccorcis(ActionEvent event) {
        VBox root = null;
        LymytzService.openWindow("component/racourcis_clavier.fxml", "Lymytz /A propos", root, 400.0, 450.0, false);
    }

    @FXML
    private void openViewComptes(ActionEvent event) {
        openViewComptes();
    }

    public void openViewComptes() {
        VBox root = null;
        MyComptesController controler = LymytzService.openWindow("main/form_comptes.fxml", "Lymytz /Mon compte", root, 850.0, 505.0, true);
        controler.setMainController(this);
    }

    @FXML
    public void openTocreateNewFacture(ActionEvent ev) {
        try {
            VBox root = null;
            FactureController controler = LymytzService.openWindow("main/form_create_facture.fxml", "Lymytz /Nouvelle Facture", root, 600.0, 305.0, false, this);
            if (controler != null) {
                controler.initDataForm(this);
            }
        } catch (Exception ex) {
            LogFiles.addLogInFile("Erreur à l'ouverture de la page form_create_facture.fxml", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
            LymytzService.openAlertDialog("Impossible d'ouvrir la page de création de la facture. Consultez votre fichier de log pour en savoir plus sur la cause", "Ouverture Impossible", "Ouverture de la page", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void openViewListFacture(ActionEvent ev) {
        VBox root = null;
        ListFacturesController controler = LymytzService.openWindow("data/form_facture.fxml", "Lymytz /Liste de Factures", root, 950.0, 605.0, true, this);
        if (controler != null) {
            controler.initPage(this, Constantes.TYPE_FV);
        }
    }

    @FXML
    public void openViewListCommandes(ActionEvent ev) {
        VBox root = null;
        ListFacturesController controler = LymytzService.openWindow("data/form_facture.fxml", "Lymytz /Liste de Factures", root, 900.0, 605.0, true, this);
        if (controler != null) {
            controler.initPage(this, Constantes.TYPE_BCV);
        }
    }

    @FXML
    public void openDlgStatusSync(ActionEvent ev) {
        VBox root = null;
        ListenTableController controler = LymytzService.openWindow("main/synchro/listen_table.fxml", "Lymytz /Etat Synchronisation", root, 900.0, 505.0, true, this);
        if (controler != null) {
            controler.initPage(this);
        }
    }

    @FXML
    public void openDlgStatusSyncImp(ActionEvent ev) {
        VBox root = null;
        ListenRemoteTableController controler = LymytzService.openWindow("main/synchro/listen_table_remote.fxml", "Lymytz /Etat Synchronisation", root, 900.0, 505.0, true, this);
        if (controler != null) {
            controler.initPage(this);
        }
    }

    @FXML
    private void printFacture(ActionEvent event) {
        if (!TAB_FACTURES.getTabs().isEmpty()) {
            Onglets ong = (Onglets) TAB_FACTURES.getSelectionModel().getSelectedItem();
            ParamConnection param = new ParamConnection();
            ParamConnection.readFile(LymytzService.getFileInputStream());
            if (ong != null) {
                ong.setNetAPayer(ong.getFacture().getMontantTTC());
                if (param.getTypeRapport().equals(UtilsProject.TYPE_RAPPORT_TICKET)) {
                    PrintTiket pt = new PrintTiket(this, ong, 0, "");
                    pt.setFacture(ong.getFacture());
                    pt.setMontantAvance(ong.getFacture().getMontantAvance());
                    pt.setMontantRecu(ong.getMontantRecu());
                    pt.setMontantTotal(ong.getFacture().getMontantTotal());
                    pt.setNetAPayer(ong.getNetAPayer());
                    new Thread(pt).start();
                } else {
                    PrintFacture preview = new PrintFacture(this, ong);
                    preview.loadFactureToPrint(ong.getFacture());
                }
            }
        }
    }

    @FXML
    private void saveOrGeneratedPaiement(ActionEvent event) {
        //1. Controle la caisse et le mode de paiement  
        Onglets tab = (Onglets) TAB_FACTURES.getSelectionModel().getSelectedItem();
        if (tab != null) {
            ServiceCreateFacture service = new ServiceCreateFacture(this);
            service.saveOrGeneratedPaiement_(tab);
        }
    }

    @FXML
    private void generatedBlFromDoc(ActionEvent event) {
        //1. Controle la caisse et le mode de paiement  
        Onglets tab = (Onglets) TAB_FACTURES.getSelectionModel().getSelectedItem();
        if (tab != null) {
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    YvsComDocVentes dv = tab.getFacture();
                    JSONObject entityJson = UtilExport.exportDocVente(tab.getFacture(), false, null);
                    if (entityJson != null) {
                        WsSynchro ws = new WsSynchro();
                        if (dv.getTypeDoc().equals(Constantes.TYPE_FV)) {
                            ws.livraisonDocVente(entityJson, "livrer_facture_vente");
                        } else if (dv.getTypeDoc().equals(Constantes.TYPE_BCV)) {

                        }
                    }
                }
            });
        }
    }

    @FXML
    private void clearDocVenteWithoutContent(ActionEvent event) {
        //1. Controle la caisse et le mode de paiement  
        if (UtilsProject.headerDoc != null) {
            dao.cleanDocWithoutContent(UtilsProject.headerDoc.getId());
        }
    }

    public void openDlgCalculatrice(Onglets onglet, String source, String action, ContentPanier content) {
        try {
            if (onglet != null ? onglet.getContentFacture().isEmpty() : true) {
                LymytzService.openAlertDialog("Votre panier est vide", "erreur contenu", "Erreur !", Alert.AlertType.ERROR);
                return;

            }
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource("component/claviers.fxml"));
            VBox root = load.load();
            Scene scene = new Scene(root, 485, 566);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Lymytz:Claviers");
            stage.centerOnScreen();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(UtilsProject.primaryStage);
            stage.setResizable(false);
            stage.show();
            ClaviersController controler = (ClaviersController) load.getController();
            controler.initController(this, onglet, stage, source, action, content);
            scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode().equals(KeyCode.ESCAPE)) {
                        stage.close();
                    }
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(ManagedApplication.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void openDlgCalculatrice(Onglets onglet, String source, String action) {
        openDlgCalculatrice(onglet, source, action, null);
    }

    public void opnClotureFiche(Long idHeader) {
        try {
            FXMLLoader load = new FXMLLoader(LocalLoader.class
                    .getResource("main/form_virement_recette.fxml"));
            VBox root = load.load();
            Scene scene = new Scene(root, 600, 250);
            Stage stage = new Stage();

            stage.setScene(scene);

            stage.setTitle("Lymytz:extended caisse apps");
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(UtilsProject.primaryStage);
            stage.show();
            FormVirementController controller = (FormVirementController) load.getController();
            controller.initFormVirement(idHeader, UtilsProject.caisse, this, stage);
            scene.setOnKeyReleased(
                    (KeyEvent event) -> {
                        if (event.getCode().equals(KeyCode.ESCAPE)) {
                            stage.close();
                        }
                    });
        } catch (IOException ex) {
            Logger.getLogger(ManagedApplication.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    public void goingOut(ActionEvent ev) {
        goingOutApplication();
    }

    public boolean goingOutApplication() {
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setTitle("Fermeture de session!");
        al.setContentText("Souhaitez vous terminer cette session ?");
        Optional<ButtonType> result = al.showAndWait();
        if (result.get().equals(ButtonType.OK)) {
            UtilsProject.primaryStage.close();
            LymytzService.openApps(UtilsProject.primaryStage);
            return true;
        } else {
            return false;
        }
    }

    public boolean closeApplication() {
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setTitle("Fermeture de session!");
        al.setContentText("Souhaitez vous terminer cette session et Arrêter l'application?");
        Optional<ButtonType> result = al.showAndWait();
        if (result.get().equals(ButtonType.OK)) {
            Platform.exit();
            System.exit(0);
            return true;
        } else {
            return false;
        }
    }

    @FXML
    public void reloadPropertieFile(ActionEvent ev) {
        UtilsProject.loadFilePropertie();
        if (UtilsProject.properties != null) {
            UtilsProject.loadInitData();
            loadProperties();
            LymytzService.success();
        }
    }

    @FXML
    public void testLoadData(ActionEvent ev) {
        WsSynchro.runningIn = false;
    }

    @FXML
    public void testSendMail(ActionEvent ev) {
        SendMail service = new SendMail("", "");
        service.sendMessage();
    }

    @FXML
    public void livrerFacture(ActionEvent ev) {
        Onglets fac = (Onglets) TAB_FACTURES.getSelectionModel().getSelectedItem();
        saveOrGenerateBl(fac);
    }

    public void saveOrGenerateBl(Onglets onglet) {
        if (onglet != null) {
            ServiceLivraison service = new ServiceLivraison(this);
            if (!onglet.getFacture().getTypeDoc().equals(Constantes.TYPE_BCV)) {
                service.saveLivraison(onglet.getFacture(), true);
            } else {
                Alert dlg = new Alert(Alert.AlertType.CONFIRMATION, "Confirmez vous la livraison de ce bon de commande ?", new ButtonType("Oui"), new ButtonType("Non"));
                Optional<ButtonType> resp = dlg.showAndWait();
                if (resp.get().getText().equals("Oui")) {
                    if (service.transmisOrder(onglet.getFacture())) {
                        BTN_LIVRER.setVisible(false);
                    }
                } else {
//                System.err.println(" Livre pas");
                }
            }
        }
    }

    private LongProperty time = new SimpleLongProperty();

    public Long getTime() {
        return time.get();
    }

    public void setTime(Long time) {
        this.time.set(time);
    }

    public LongProperty getTimeProperty() {
        return time;
    }

    public class Clock extends Task<Long> {

        long time = System.currentTimeMillis();
        Date d;

        @Override
        protected Long call() throws Exception {
            while (true) {
                long duree = System.currentTimeMillis() - time - 3600000;
                d = new Date(duree);
                setTime(duree);
                Thread.sleep(1000);
            }
        }
    }

    public void loadProperties() {
        if (UtilsProject.properties.containsKey(Constantes.KEY_ENVIRONNEMENT)) {
            if (UtilsProject.properties.getProperty(Constantes.KEY_ENVIRONNEMENT).equals("PRODUCTION")) {
                //Lance des ping sur le serveur distant pour savoir s'il est toujours connecté
                //cette methode est lancé seulement lorsque le serveur est en mode BOTH
                if (UtilsProject.properties.getProperty(Constantes.KEY_MODE).equals(Constantes.APPS_MODE_BOTH)) {
                    new ListenServersRemote(10, this).start();
                } else {
                    //lance la socket d'écoute client... (si on est en mode replication)
                    if (UtilsProject.REPLICATION) {
                        Thread t = new Thread(() -> {
                            clientSocket = new ClientMessage("", this);
                            clientSocket.initClient();
                        });
                        t.start();
                        verifySocketIsConnected();
                    } else {

                    }
                }
                //Lance des ping sur le serveur local pour savoir s'il est toujours connecté
                new ListenServersLocal(10, this).start();
            }
        }
        ICO_ALERT_EX.setVisible(false);
        ICO_ALERT_IM.setVisible(false);
        if (UtilsProject.REPLICATION && Constantes.APPS_MODE_BOTH.equals(UtilsProject.properties.getProperty(Constantes.KEY_MODE))) {
            LAB_TITLE_SYNC_T.setVisible(true);
            //Notifie sur la vue utilisateur si des opérations de synchronisation s'exécutent ou pas
            listenSynchro();
            //Synchronise les données du serveur local vers le serveur distant
            if (myServiceOut != null ? !myServiceOut.isRunning() : true) {
                myServiceOut = new SynchronizeDataOut(20, this);
                myServiceOut.start();
            }
            //Synchronise les données du serveur distant vers le serveur local
            if (myServiceIn != null ? !myServiceIn.isRunning() : true) {
                myServiceIn = new SynchronizeDataIn(25, this);
                myServiceIn.start();
            }
//            //Synchronise les données de suppresion
//            if (myServiceDel != null ? !myServiceDel.isRunning() : true) {
//                myServiceDel = new SynchronizeDeleteFacture(20, this);
//                myServiceDel.start();
//            }
        } else {
            ICO_RUN_OFF.setVisible(false);
            ICO_RUN_ON.setVisible(false);
            ICO_RUN_OFF_IN.setVisible(false);
            ICO_RUN_ON_IN.setVisible(false);
            LAB_TITLE_SYNC_T.setVisible(false);
            ITEM_IMPORT.setVisible(false);
            ITEM_EXPORT.setVisible(false);
            ITEM_SERVICE.setVisible(false);
            ITEM_PING_R.setVisible(false);
            MEN_SYNCHRO.setVisible(false);
            LAB_SYNC_EX.setVisible(false);
            LAB_SYNC_IM.setVisible(false);
        }
    }

    public void listenSynchro() {
        Thread t = new Thread(() -> {
            while (!UtilsProject.STOP_LISTEN) {
                try {
                    // si le statut du service est cancelled ou le service n'est pas accéssible;
                    if (WsSynchro.runningOut && SynchronizeDataOut.running) {
                        Platform.runLater(() -> {
                            ICO_RUN_OFF.setVisible(false);
                            ICO_RUN_ON.setVisible(true);
                        });
                    } else {
                        Platform.runLater(() -> {
                            ICO_RUN_OFF.setVisible(true);
                            ICO_RUN_ON.setVisible(false);
                        });
                    }
                    if (WsSynchro.runningIn && SynchronizeDataIn.running) {
                        Platform.runLater(() -> {
                            ICO_RUN_OFF_IN.setVisible(false);
                            ICO_RUN_ON_IN.setVisible(true);
                        });
                    } else {
                        Platform.runLater(() -> {
                            ICO_RUN_OFF_IN.setVisible(true);
                            ICO_RUN_ON_IN.setVisible(false);
                        });
                    }
                    Thread.sleep(2000);

                } catch (InterruptedException ex) {
                    Logger.getLogger(HomeCaisseController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t.start();
    }

    //méthode qui se rassure que la socket client est toujours connecté au serveur
    public void verifySocketIsConnected() {
        if (UtilsProject.properties.getProperty(Constantes.KEY_MODE).equals(Constantes.APPS_MODE_SINGLE)) {
            HomeCaisseController p = this;
            new Thread(() -> {
                while (true) {
                    if (clientSocket != null ? clientSocket.socketClient != null : false) {
                        if (clientSocket.socketClient.isClosed()) {
                            Thread t = new Thread(() -> {
                                clientSocket.initClient();
                            });
                            t.start();
                        } else {
                            p.setConnect(true);
                        }
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HomeCaisseController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
        }
    }

}
