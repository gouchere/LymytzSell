/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.view.main;

import com.lymytz.lymytzsell.service.application.service.ManagedFactureVente;
import com.lymytz.lymytzsell.business.helpers.Helpers;
import com.lymytz.lymytzsell.business.helpers.KeyBoardAction;
import com.lymytz.lymytzsell.business.helpers.ResponseAction;
import com.lymytz.lymytzsell.persistence.dao.Options;
import com.lymytz.lymytzsell.service.application.config.Properties;
import com.lymytz.lymytzsell.persistence.dao.util.UtilsBean;
import com.lymytz.lymytzsell.persistence.entity.YvsBaseArticleCategorieComptable;
import com.lymytz.lymytzsell.persistence.entity.YvsBaseArticleCategorieComptableTaxe;
import com.lymytz.lymytzsell.persistence.entity.YvsBaseCategorieComptable;
import com.lymytz.lymytzsell.persistence.entity.YvsBaseConditionnement;
import com.lymytz.lymytzsell.persistence.entity.YvsBaseDepots;
import com.lymytz.lymytzsell.persistence.entity.YvsBaseFamilleArticle;
import com.lymytz.lymytzsell.persistence.entity.YvsComContenuDocVente;
import com.lymytz.lymytzsell.persistence.entity.YvsComDocVentes;
import com.lymytz.lymytzsell.persistence.entity.YvsComEnteteDocVente;
import com.lymytz.lymytzsell.persistence.entity.YvsComTaxeContenuVente;
import com.lymytz.lymytzsell.persistence.dao.LocalQueryFactories;
import com.lymytz.lymytzsell.persistence.dao.RQueryFactories;
import com.lymytz.lymytzsell.service.ClientMessage;
import com.lymytz.lymytzsell.service.ServeurMessage;
import com.lymytz.lymytzsell.service.application.FactureController;
import com.lymytz.lymytzsell.service.application.ManagedApplication;
import com.lymytz.lymytzsell.service.application.MyComptesController;
import com.lymytz.lymytzsell.service.application.bean.ContentPanier;
import com.lymytz.lymytzsell.view.component.ClaviersController;
import com.lymytz.lymytzsell.view.component.Onglets;
import com.lymytz.lymytzsell.service.application.config.PropertiesManager;
import com.lymytz.lymytzsell.service.application.loader.LoaderArticleTask;
import com.lymytz.lymytzsell.service.application.loader.LoaderFamilleArticleTask;
import com.lymytz.lymytzsell.service.application.loader.LoaderInitData;
import com.lymytz.lymytzsell.service.application.loader.LoaderStock;
import com.lymytz.lymytzsell.service.application.listener.ListenServersLocal;
import com.lymytz.lymytzsell.service.application.listener.ListenServersRemote;
import com.lymytz.lymytzsell.service.application.service.ServiceCreateFacture;
import com.lymytz.lymytzsell.service.application.service.ServiceLivraison;
import com.lymytz.lymytzsell.service.application.service.reglement.FormVirementController;
import com.lymytz.lymytzsell.service.application.service.reglement.ServiceReglement;
import com.lymytz.lymytzsell.service.application.service.report.ListingController;
import com.lymytz.lymytzsell.service.application.service.report.ListingCumuleController;
import com.lymytz.lymytzsell.service.application.synchro.ControlServiceController;
import com.lymytz.lymytzsell.service.application.synchro.ListenTableController;
import com.lymytz.lymytzsell.service.application.synchro.SynchronizeDataIn;
import com.lymytz.lymytzsell.service.application.synchro.SynchronizeDataOut;
import com.lymytz.lymytzsell.service.application.synchro.impor.ImportDataController;
import com.lymytz.lymytzsell.service.application.synchro.impor.ListenRemoteTableController;
import com.lymytz.lymytzsell.service.start.StartController;
import com.lymytz.lymytzsell.service.utils.Clock;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.CustomWindow;
import com.lymytz.lymytzsell.service.utils.LymytzService;
import com.lymytz.lymytzsell.service.application.service.report.PrintTiket;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import com.lymytz.lymytzsell.synchro.ws.WsSynchro;
import com.lymytz.lymytzsell.view.LocalLoader;
import com.lymytz.lymytzsell.view.component.CustomComponents;
import com.lymytz.lymytzsell.view.component.ToastService;
import com.lymytz.lymytzsell.view.main.report.PrintFacture;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static com.lymytz.lymytzsell.service.utils.Constantes.ETAT_CLOTURE;
import static com.lymytz.lymytzsell.service.utils.Constantes.ETAT_LIVRE;
import static com.lymytz.lymytzsell.service.utils.Constantes.ETAT_REGLE;
import static com.lymytz.lymytzsell.service.utils.Constantes.ETAT_VALIDE;
import static com.lymytz.lymytzsell.service.utils.Constantes.TYPE_FV;
import static com.lymytz.lymytzsell.service.utils.MessagesConstants.ERREUR;
import static com.lymytz.lymytzsell.service.utils.MessagesConstants.GENERATION_DE_LA_FACTURE_NON_REUSSI;
import static com.lymytz.lymytzsell.view.component.ToastService.ToastType.ERROR;
import static com.lymytz.lymytzsell.view.component.ToastService.ToastType.INFO;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class HomeCaisseController extends ManagedApplication implements Initializable {

    private final Logger LOGGER = LogManager.getLogger(HomeCaisseController.class);
    private final BooleanProperty connectRemoteServer = new SimpleBooleanProperty();
    private final LongProperty time = new SimpleLongProperty();
    public final AtomicInteger currentPage = new AtomicInteger(0);
    private static final int MAX_SIZE = 20;
    private final IntegerProperty totalPages = new SimpleIntegerProperty(0);
    private final IntegerProperty currentPageProperty = new SimpleIntegerProperty(0);
    @Setter
    @Getter
    private YvsBaseFamilleArticle selectedFamilleArticle;
    ClientMessage clientSocket;

    private SynchronizeDataOut myServiceOut;
    private SynchronizeDataIn myServiceIn;
    @Getter
    @Setter
    private Stage stageCreateFacture;

    @FXML
    private VBox MAIN_LEFT_PANE;
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
    public VBox MAIN_ARTICLE_CONTAINER;
    @FXML
    public VBox BOX_ARTICLES;
    //
    @FXML
    public TabPane TAB_FACTURES;
    DoubleProperty RigthBoxWidth;

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
    public Label LAB_DES;
    @FXML
    public Label QTE_FACTURE;
    @FXML
    private Label SESS_DUREE;
    //Footer
    @FXML
    private Label TEXT_SOCIETE;

    private final ProgressBar PROGRESS = new ProgressBar(0.0);
    private final Label PROGRESS_LABEL = new Label();
    private final HBox CATALOGUE_NAVIGATION = new HBox();

    public HomeCaisseController() {
        //utile pour l'api javafx
    }


    public Long getTime() {
        return time.get();
    }

    public void setTime(Long time) {
        this.time.set(time);
    }

    public Boolean getConnectRemoteServer() {
        return connectRemoteServer.get();
    }

    public void setConnect(Boolean connect) {
        this.connectRemoteServer.setValue(connect);
    }

    public void setCurrentPageProperty(int currentPage) {
        if (currentPage < 0 || currentPage > totalPages.get()) {
            currentPageProperty.set(0);
            this.currentPage.set(0);
        } else currentPageProperty.set(currentPage);
    }

    public void setTotalPages(int total) {
        totalPages.set(total);
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LoaderInitData loaderInitData = new LoaderInitData(dao, this);
        loaderInitData.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
            this.displayPropertiesFiche(UtilsProject.headerDoc);
        });
        initComponent();
        setMainPage(this);
        TEXT_FIND.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                event.consume();
                handlePressTab(TEXT_FIND);
            }
        });
    }

    private void handlePressTab(TextField textField) {
        filterArticleFromSearchField(textField.getText());
    }

    private void filterArticleFromSearchField(String keyFilter) {
        if (Constantes.asString(keyFilter) && UtilsProject.headerDoc != null) {
            LoaderArticleTask tache1 = new LoaderArticleTask(this, UtilsProject.headerDoc, keyFilter);
            YvsBaseConditionnement art = tache1.findOneArticle();
            if (art != null) {
                addInCardIfOneArtIsFind(art);
            } else {
                // init pagination properties
                currentPageProperty.set(0);
                loadCatalogue(UtilsProject.headerDoc, keyFilter);
            }
        } else if (UtilsProject.headerDoc == null) {
            ToastService.show(this.getMainStage(), "L'initialisation de la fiche de vente est requise pour commencer la recherche", 5000, ERROR);
        }
    }

    private void addInCardIfOneArtIsFind(YvsBaseConditionnement art) {
        Onglets tab = (Onglets) TAB_FACTURES.getSelectionModel().getSelectedItem();
        if (tab == null) {
            this.initFactureVenteClientDivers();
            tab = (Onglets) TAB_FACTURES.getSelectionModel().getSelectedItem();
        }
        Optional.ofNullable(tab).ifPresent(ong -> {
            if (ong.addArticleOnFacture(art, 1, false, art.getPrix())) {
                giveFocusAtTxtFind();
            }
        });
    }

    public void initComponent() {
        TAB_FACTURES.setPrefHeight(StartController.SCREENHEIGHT - 345d);
        RigthBoxWidth = new SimpleDoubleProperty(RIGHT_BOX.getPrefWidth());
        PROGRESS.setPrefHeight(18.0);
        PROGRESS.setPrefWidth(StartController.SCREENHEIGHT);
        CustomComponents.custumMenuAndToolBar(this);
        CustomComponents.initEventComponents(this);
        if (UtilsProject.currentAgence != null && UtilsProject.currentSociete != null) {
            TEXT_SOCIETE.setText(UtilsProject.currentSociete.getName() + "[" + UtilsProject.currentAgence.getDesignation() + "]");
        }
        /*loadFamilleArticles();
        loadCatalogue(" ");*/
        //Lance l'horloge d'écoulement du temps
        time.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> Platform.runLater(() -> SESS_DUREE.setText(Constantes.HMS.format(new Date(getTime())))));
        connectRemoteServer.addListener((observable, oldValue, newValue) -> {
            //ecrit sur la socket: seul le serveur peut écrire 
            if (newValue != null && (PropertiesManager.getInstance().getVal(Constantes.KEY_MODE).equals(Constantes.APPS_MODE_BOTH))) {
                ServeurMessage.writeMessage(newValue);
                LOGGER.info("Changement état connexion serveur distant");

            }
        });
        String name = (UtilsProject.currentUser != null) ? UtilsProject.currentUser.getUsers().getNomUsers() : (UtilsProject.modeAdmin ? "ADMINISTRATEUR" : "---");
        LAB_VEND.setText(name);
        startHorloge();
        loadProperties();
        //Code temporaire...
        if (UtilsProject.currentUser != null) {
            if (UtilsProject.currentUser.getUsers().getCodeUsers().equals("ADMINGLP")) {
                ITEM_RELOAD_PPTE.setVisible(true);
                ITEM_PREF.setVisible(true);
            } else {
                ITEM_RELOAD_PPTE.setVisible(false);
                ITEM_PREF.setVisible(false);
            }
        }
        addButtonsNav();
    }

    private void addButtonsNav() {
        Label label = new Label(currentPageProperty.get() + "/" + totalPages.get());
        Button prevButton = new Button("← Prev");
        Button nextButton = new Button("Next →");
        prevButton.getStyleClass().add("navigation-button");
        nextButton.getStyleClass().add("navigation-button");
        CATALOGUE_NAVIGATION.getChildren().addAll(prevButton, label, nextButton);
        initEventsPagination(prevButton, nextButton, label);
    }

    private void toogleDisplayButtonNav(boolean display) {
        if (display) {
            CATALOGUE_NAVIGATION.setAlignment(Pos.CENTER_RIGHT);
            CATALOGUE_NAVIGATION.setSpacing(10d);
            if (!MAIN_ARTICLE_CONTAINER.getChildren().contains(CATALOGUE_NAVIGATION)) {
                MAIN_ARTICLE_CONTAINER.getChildren().add(CATALOGUE_NAVIGATION);
            }
        } else {
            MAIN_ARTICLE_CONTAINER.getChildren().remove(CATALOGUE_NAVIGATION);
        }
    }

    private void initEventsPagination(Button prevButton, Button nextButton, Label label) {
        nextButton.setOnAction(event -> {
            setCurrentPageProperty(currentPage.incrementAndGet());
            loadCatalogue(UtilsProject.headerDoc, TEXT_FIND.getText());
        });
        prevButton.setOnAction(event -> {
            setCurrentPageProperty(currentPage.decrementAndGet());
            loadCatalogue(UtilsProject.headerDoc, TEXT_FIND.getText());
        });
        currentPageProperty.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> label.setText(newValue + "/" + totalPages.get()));
        totalPages.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            label.setText(currentPage.get() + "/" + newValue);
            toogleDisplayButtonNav(newValue != null && newValue.intValue() > 1);
        });
    }

    private void startHorloge() {
        Thread ttemp = new Thread(new Clock(this::setTime));
        ttemp.setName("Horloge locale");
        ttemp.start();
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

    public void createAndAddProgressBar() {
        Platform.runLater(() -> {
            var hboxProgress = new HBox(PROGRESS_LABEL, PROGRESS);
            MAIN_ARTICLE_CONTAINER.getChildren().add(0, hboxProgress);
        });
    }

    public void loadCatalogue(YvsComEnteteDocVente header, String ref) {
        loadCatalogue(header, ref, this.getSelectedFamilleArticle());
    }

    public void loadCatalogue(YvsComEnteteDocVente header, String ref, YvsBaseFamilleArticle familleArticle) {
        var loaderArticleTask = new LoaderArticleTask(this, header, ref, familleArticle, currentPageProperty.get(), MAX_SIZE);
        try {
            if (header != null) {
                setTotalPages(calculPageTotal(ref, familleArticle));
                BOX_ARTICLES.getChildren().clear();
                PROGRESS.progressProperty().unbind();
                PROGRESS_LABEL.textProperty().unbind();
                PROGRESS.progressProperty().bind(loaderArticleTask.progressProperty());
                PROGRESS_LABEL.textProperty().bind(loaderArticleTask.messageProperty());
                loaderArticleTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
                    ObservableList<GridPane> value = loaderArticleTask.getValue();
                    if (value != null) {
                        BOX_ARTICLES.getChildren().addAll(value);
                        PROGRESS_LABEL.textProperty().unbind();
                    }
                    if (value != null && !value.isEmpty()) {
                        PROGRESS_LABEL.setText("terminé !");
                        MAIN_ARTICLE_CONTAINER.getChildren().remove(0);
                    } else {
                        PROGRESS_LABEL.setText("Aucun résultat trouvé !");
                    }
                });
                new Thread(loaderArticleTask).start();
            }
        } catch (Exception ex) {
            LOGGER.error("Une exception survenue au chargement du catalogue", ex);
        }
    }

    public void loadFamilleArticles(YvsComEnteteDocVente header) {
        var loaderFamilleTask = new LoaderFamilleArticleTask(this, header);
        try {
            if (header != null) {
                loaderFamilleTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
                    VBox value = loaderFamilleTask.getValue();
                    ScrollPane scrollPane = new ScrollPane();
                    scrollPane.setId("ZONE_FAMILLE");
                    scrollPane.setContent(value);
                    scrollPane.setFitToWidth(true);
                    scrollPane.setMaxHeight(350);
                    var node = MAIN_LEFT_PANE.getChildren().stream().filter(elt -> "ZONE_FAMILLE".equals(elt.getId())).findFirst();
                    node.ifPresent((e) -> MAIN_LEFT_PANE.getChildren().remove(e));
                    MAIN_LEFT_PANE.getChildren().add(0, scrollPane);
                });
                new Thread(loaderFamilleTask).start();
            }
        } catch (Exception ex) {
            LOGGER.error("Une exception survenue au chargement du catalogue", ex);
        }
    }

    private int calculPageTotal(String ref, YvsBaseFamilleArticle familleArticle) {
        Long nbArticleCatalogue = new LoaderArticleTask(this, UtilsProject.headerDoc, ref, familleArticle, 0, 0).countArticlesInDb();
        return Optional.of(nbArticleCatalogue).map(n -> Math.ceil((double) n / MAX_SIZE))
                .map(Double::intValue).orElse(0);
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
        }
    }

    public void resetAllView(YvsComEnteteDocVente head) {
        if (UtilsProject.headerDoc != null && (UtilsProject.headerDoc.equals(head))) {
            displayDetailFacture(null);

        }
    }

    public void displayDetailFacture(YvsComDocVentes facture) {
        if (facture != null) {
            if (Constantes.asLong(facture.getId())) {
                LAB_REF_FACTURE.setText(facture.getNumDoc());
            } else {
                LAB_REF_FACTURE.setText(facture.getNumDoc() + ":" + facture.getClient().getCodeClient() + facture.getId());
            }
            LAB_CLIENT.setText(facture.getClient().getCodeClient());
            CHK_LIVRE.setSelected(facture.getStatutLivre().equals(ETAT_LIVRE));
            CHK_REGLE.setSelected(facture.getStatutRegle().equals(ETAT_REGLE));
            LAB_ADRESSSE.setText(facture.getAdresse() != null ? facture.getAdresse().getLibele() : "");
            LAB_NAME_CLT.setText(facture.getNomClient());
            double avance = facture.getMontantAvance();

            Onglets onglets = (Onglets) TAB_FACTURES.getSelectionModel().getSelectedItem();
            if (onglets != null) {
                onglets.setNetAPayer(facture.getMontantResteApayer());
                LAB_T_AVANCE.setText(Constantes.nbf.format(avance));
                LAB_NET_A_PAYER.setText(Constantes.nbf.format(onglets.getNetAPayer()));
            }
            if (facture.getStatut().equals(ETAT_VALIDE) || facture.getStatut().equals(ETAT_CLOTURE)) {
                BTN_SAVE.setVisible(false);
                BTN_PRINT.setVisible(true);
            } else {
                BTN_SAVE.setVisible(true);
                BTN_PRINT.setVisible(false);
            }
            BTN_REGLER.setVisible(ETAT_VALIDE.equals(facture.getStatut()) && !facture.getStatutRegle().equals(ETAT_REGLE));
            BTN_LIVRER.setVisible(ETAT_VALIDE.equals(facture.getStatut()) && !facture.getStatutLivre().equals(ETAT_LIVRE));
        } else {
            BTN_LIVRER.setVisible(false);
            BTN_REGLER.setVisible(false);
            LAB_REF_FACTURE.setText(null);
            LAB_CLIENT.setText(null);
            CHK_LIVRE.setSelected(false);
            CHK_REGLE.setSelected(false);
            LAB_ADRESSSE.setText(null);
            LAB_NAME_CLT.setText(null);
            BTN_SAVE.setVisible(!TAB_FACTURES.getTabs().isEmpty());
            BTN_PRINT.setVisible(false);
            ECRAN.setText("0");
        }
    }

    public void displayPropertyArticle(YvsBaseConditionnement art, boolean displayAllProperties) {
        if (art != null) {
            LAB_DES.setText(art.getArticle().getDesignation());
            // ZONE_IMG.getChildren().clear();
            createImageProduit(art.getArticle());
            // ZONE_IMG.getChildren().add(pagination);
            List<YvsBaseDepots> depots;
            if (displayAllProperties) {
                depots = dao.loadByNamedQuery("YvsBaseArticleDepot.findDepotActifByArt", new String[]{"article"}, new Object[]{art.getArticle()});
                depots.remove(UtilsProject.depotLivraison);
                depots.add(0, UtilsProject.depotLivraison);
            } else {
                depots = new ArrayList<>();
                depots.add(UtilsProject.depotLivraison);
            }
            PAN_STOCK.getChildren().clear();
            if (UtilsProject.headerDoc != null && UtilsProject.headerDoc.getCreneau() != null) {
                getAndDisplayArticleProperties(art, depots);
            }
        }
    }

    public double displayStockArticle(YvsBaseConditionnement art) {
        if (art != null) {
            PAN_STOCK.getChildren().clear();
            if (UtilsProject.headerDoc != null && UtilsProject.headerDoc.getCreneau() != null) {
                Double qte = (Double) dao.findOneObjectByNQ("YvsComContenuDocVente.countQteVendu", new String[]{"conditionnement", "header"}, new Object[]{art, UtilsProject.headerDoc});
                if (qte != null && qte > 0) {
                    QTE_FACTURE.setText(Constantes.nbf.format(qte));
                } else {
                    QTE_FACTURE.setText(Constantes.nbf.format(0));
                }
                if (!UtilsProject.REPLICATION && UtilsProject.depotLivraison != null) {
                    //si on est pas en mode replication, calcul immédiatement le stock
                    double stock = UtilsProject.getStocks(art, UtilsProject.depotLivraison.getId());
                    art.setStock(stock);
                    var hBox = new HBox(new Label(UtilsProject.depotLivraison.getDesignation() + ": "), new Label(stock + " " + art.getUnite().getReference()));
                    hBox.setSpacing(10);
                    PAN_STOCK.getChildren().add(hBox);
                    return stock;
                }
            }
        }
        return 0;
    }

    private void getAndDisplayArticleProperties(YvsBaseConditionnement art, List<YvsBaseDepots> depots) {
        //compte la quantité de l'article facturé
        Double qte = (Double) dao.findOneObjectByNQ("YvsComContenuDocVente.countQteVendu", new String[]{"conditionnement", "header"}, new Object[]{art, UtilsProject.headerDoc});
        if (qte != null && qte > 0) {
            QTE_FACTURE.setText(Constantes.nbf.format(qte));
        } else {
            QTE_FACTURE.setText(Constantes.nbf.format(0));
        }
        if (Boolean.FALSE.equals(UtilsProject.REPLICATION) && UtilsProject.depotLivraison != null) {
            //si on est pas en mode replication, calcul immédiatement le stock
            double stock = UtilsProject.getStocks(art, UtilsProject.depotLivraison.getId());
            art.setStock(stock);
        }
        try {
            LoaderStock service = new LoaderStock(this, depots, art);
            service.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (WorkerStateEvent event) -> {
                VBox containerStock = service.getValue();
                ScrollPane scrollPane = new ScrollPane(containerStock);
                scrollPane.setFitToHeight(true);
                scrollPane.setPrefHeight(150);
                Platform.runLater(() -> {
                    PAN_STOCK.getChildren().clear();
                    PAN_STOCK.getChildren().add(scrollPane);
                });
            });
            new Thread(service).start();
        } catch (Exception ex) {
            LOGGER.error("Une exception survenue à l'affichage des propriétés de l'article", ex);
        }
    }

    public void initFactureVenteClientDivers() {
        try {
            //ResponseAction<YvsComDocVentes> response
            ManagedFactureVente managedFacture = new ManagedFactureVente(UtilsProject.headerDoc, UtilsProject.defaultAdresse, UtilsProject.clientDivers, "Client Divers", TYPE_FV, UtilsProject.headerDoc.getDateEntete(), null);
            var response = managedFacture.createNonPersistFacture(TYPE_FV);
            processResponseCreateFacture(response);
        } catch (Exception ex) {
            LOGGER.error("Une exception survenue à l'initialisation de la facture", ex);
        }
    }

    public void processResponseCreateFacture(ResponseAction<YvsComDocVentes> response) {
        switch (response.getStatutResponse()) {
            case TIERS_INNEXISTANT ->
                    Platform.runLater(() -> LymytzService.openAlertDialog("Le tiers rattaché à ce client n'existe pas !", "Action abandonné !", ERREUR, Alert.AlertType.ERROR));
            case CLIENT_INNEXISTANT ->
                    Platform.runLater(() -> LymytzService.openAlertDialog(GENERATION_DE_LA_FACTURE_NON_REUSSI, ERREUR, "Action abandonné !", Alert.AlertType.ERROR));
            case NUMERO_DOC_NON_GENERE ->
                    Platform.runLater(() -> LymytzService.openAlertDialog(GENERATION_DE_LA_FACTURE_NON_REUSSI, ERREUR, "Le numéro de référence n'a pas pu être généré !", Alert.AlertType.ERROR));
            case FICHE_DEJA_CLOTURE ->
                    Platform.runLater(() -> LymytzService.openAlertDialog(GENERATION_DE_LA_FACTURE_NON_REUSSI, ERREUR, "Votre fiche de vente est déjà clôturé !", Alert.AlertType.ERROR));
            case ENTETE_FACTURE_NON_TROUVE ->
                    Platform.runLater(() -> LymytzService.openAlertDialog(GENERATION_DE_LA_FACTURE_NON_REUSSI, ERREUR, "Aucune entête n'a été trouvé !", Alert.AlertType.ERROR));
            case DATE_FICHE_INCORRECT ->
                    Platform.runLater(() -> LymytzService.openAlertDialog(GENERATION_DE_LA_FACTURE_NON_REUSSI, ERREUR, "Vérifier la date de votre fiche !", Alert.AlertType.ERROR));
            default -> Platform.runLater(() -> {
                displayDetailFacture(response.getEntity());
                initTabPane(response.getEntity());
                TEXT_FIND.setText("");
                BTN_REGLER.setVisible(response.getEntity().getStatutRegle().equals(ETAT_REGLE));
                BTN_LIVRER.setVisible(response.getEntity().getStatutLivre().equals(ETAT_LIVRE));
                afterCreateFacture();
                TEXT_FIND.requestFocus();
                if (getStageCreateFacture() != null) {
                    getStageCreateFacture().close();
                }
                ToastService.show(getMainStage(), "Facture initiée avec succès", 2000, INFO);
            });
        }
    }

    public void initTabPane(YvsComDocVentes facture) {
        if (facture != null) {
            TAB_FACTURES.getTabs().add(0, new Onglets(facture, this));
            TAB_FACTURES.getSelectionModel().select(0);
        }
    }

    public void afterCreateFacture() {
        Long nb = (Long) dao.findOneObjectByNQ("YvsComDocVentes.countDocByHeaderAndType", new String[]{"type1", "type2", "header"}, new Object[]{TYPE_FV, Constantes.TYPE_BCV, UtilsProject.headerDoc});
        L_TOTAL.setText(nb != null ? nb.toString() : "0");
    }

    ButtonType re;

    public void giveFocusAtTxtFind() {
        Platform.runLater(() -> {
            TEXT_FIND.setText("");
            TEXT_FIND.requestFocus();
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
            bean.setStatut(ETAT_VALIDE);
            bean.setStatutLivree(Constantes.STATUT_DOC_ATTENTE);
            lc.add(bean);
        }
        return lc;
    }
    /* 1. Engitrer la facture
     *  2. Generer la livraison
     *  3. Générer le règlement
     *  4. Comptabiliser
     *  5.
     * */

    public void confirmValideFacture(YvsComDocVentes facture, final double montantPaye, final double montantRecu) {
        List<YvsComContenuDocVente> contenuDuPanier = new ArrayList<>(facture.getContenus());
        facture.getContenus().clear();
        YvsComDocVentes entityFacture = saveFactureAndContent(facture, contenuDuPanier, montantPaye);
        if (entityFacture != null) {
            new ServiceCreateFacture(this).saveCurrentCommercial(facture);
            livrerEtReglerFactureValide(facture, montantPaye, montantRecu);
        } else {
            ToastService.show(getMainStage(), "Votre facture n'a pas été enregistré veuillez regarder vos notifications", 3500, ERROR);
        }
    }

    private void livrerEtReglerFactureValide(YvsComDocVentes facture, double montantPaye, double montantRecu) {
        Thread tcompta = new Thread(() -> {
            saveLivraisonAndreglement(new YvsComDocVentes(facture), montantPaye, montantRecu);
            if (!UtilsProject.REPLICATION && facture.getTypeDoc().equals(TYPE_FV)) {
                comptabilise(facture.getId());
            }
        });
        tcompta.start();
    }

    public void closeOngletFacture(Onglets currentOnglet) {
        Platform.runLater(() -> {
            TAB_FACTURES.getTabs().remove(currentOnglet);
            if (!TAB_FACTURES.getTabs().isEmpty()) {
                TAB_FACTURES.getSelectionModel().select(0);
            } else {
                ECRAN.setText("0");
            }
        });
    }

    private YvsComDocVentes saveFactureAndContent(YvsComDocVentes facture, List<YvsComContenuDocVente> contenuDocVentes, double montantPaye) {
        try {
            if (facture.getId() <= 0) {
                facture.setId(null);
                facture.setNumPiece(facture.getNumDoc());
                facture.setNumeroExterne(facture.getNumDoc());
                facture.setAuthor(UtilsProject.currentUser);
                facture.setStatut(ETAT_VALIDE);
                facture.setEtapeValide(1);
                facture.setStatutLivre(Constantes.ETAT_ATTENTE);
                facture.setStatutRegle(Constantes.ETAT_ATTENTE);
                facture.setMontantAvance(montantPaye);
                if (facture.getTypeDoc().equals(Constantes.TYPE_BCV)) {
                    facture.setLivraisonAuto(Boolean.FALSE);
                }
                facture = dao.save1(facture);
                Optional.ofNullable(facture).ifPresent(fac -> saveContentFacture(contenuDocVentes, fac));
            } else {
                return dao.update(facture);
            }
        } catch (Exception ex) {
            //Enregistrer la facture sous forme de json si elle n'a pas pu être enregistré
            var fatureLog = new YvsComDocVentes(facture);
            fatureLog.setContenus(contenuDocVentes);
            dao.saveLogsFacture(fatureLog);
        }
        return facture;
    }


    private boolean saveContentFacture(List<YvsComContenuDocVente> contents, YvsComDocVentes doc) {
        return contents.stream().noneMatch(c -> (!saveContentFacture(c, doc)));
    }

    private boolean saveContentFacture(YvsComContenuDocVente c, YvsComDocVentes doc) {
        c.setDocVente(doc);
        c.setParent(null);
        if (c.getId() <= 0) {
            c.setId(null);
            c = dao.save1(c);
            c.setId(c.getId());
            //Save les taxes
            saveAllTaxe(c);
        } else {
            dao.update(c);
        }
        return true;
    }

    public void saveAllTaxe(YvsComContenuDocVente y) {
        if (y != null && (y.getId() != null && y.getId() > 0)) {
            double prix = y.getPrix() - y.getRabais();
            double qte = y.getQuantite();
            double remise = y.getRemise();
            double taxe = 0;
            double valeur = 0;

            long categorie = y.getDocVente().getCategorieComptable().getId();

            String nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticle";
            YvsBaseArticleCategorieComptable articleCategorieComptable = dao.findOneByNQ(nameQueri, new String[]{"categorie", "article"}, new Object[]{new YvsBaseCategorieComptable(categorie), y.getArticle()});
            if (articleCategorieComptable != null && (articleCategorieComptable.getId() != null && articleCategorieComptable.getId() > 0)) {
                if (Boolean.TRUE.equals(y.getArticle().getPuvTtc())) {
                    for (YvsBaseArticleCategorieComptableTaxe t : articleCategorieComptable.getTaxes()) {
                        taxe += t.getTaxe().getTaux();
                    }
                    prix = prix / (1 + (taxe / 100));
                }
                valeur = qte * prix;
                for (YvsBaseArticleCategorieComptableTaxe t : articleCategorieComptable.getTaxes()) {
                    if (Boolean.TRUE.equals(t.getAppRemise())) {
                        taxe = (((valeur - remise) * t.getTaxe().getTaux()) / 100);
                    } else {
                        taxe = ((valeur * t.getTaxe().getTaux()) / 100);
                    }
                    taxe = dao.arrondi(UtilsProject.currentSociete.getId(), taxe);

                    YvsComTaxeContenuVente ct = dao.findOneByNQ("YvsComTaxeContenuVente.findOne", new String[]{"contenu", "taxe"}, new Object[]{y, t.getTaxe()});
                    if (ct != null && (ct.getId() != null && ct.getId() > 0)) {
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
        service.saveReglementFacture(facture, montantPaye, montantRecu);
        //3. Enregistrer le document de livraison. 
        //on enregistre directement que si on n'est pas en mode replication (car si on est en mode replication, le BL sera géré par le serveur d'application dès la validation de la facture)
        if (!UtilsProject.REPLICATION && (!facture.getTypeDoc().equals(Constantes.TYPE_BCV))) {
            ServiceLivraison serviceL = new ServiceLivraison(this);
            if (facture.getTrancheLivrer() == null) {
                facture.setTrancheLivrer(UtilsProject.headerDoc.getCreneau().getCreneauDepot().getTranche());
                dao.update(facture);
            }
            serviceL.saveLivraison(facture, false);

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
        CustomWindow<ImportDataController> windowModal = LymytzService.openWindowNew("/pages/main/import_data.fxml", "Lymytz /Importation", null, 1000.0, 500.0, true);
        assert windowModal != null;
        ImportDataController controler = windowModal.getController();
        this.stageCreateFacture = windowModal.getStage();
        if (controler != null) {
            controler.initComponents(this);
        }
    }

    @FXML
    public void openViewExport(ActionEvent ev) {
        //Ouvre la fenêtre de gestion des imports
        BorderPane root = null;
        LymytzService.openWindow("/pages/main/export_data.fxml", "Lymytz /Exportation", root, 1000.0, 600.0);
    }

    @FXML
    public void openViewControlService(ActionEvent ev) {
        //Ouvre la fenêtre de gestion des imports
        VBox root = null;
        ControlServiceController controler = LymytzService.openWindow("/pages/main/form_control_service.fxml", "Etat Service", root, 460d, 300d);
        controler.initPage(this, this.myServiceOut, this.myServiceIn);
    }

    @FXML
    public void openViewLog(ActionEvent ev) {
        //Ouvre la fenêtre de gestion des imports
        VBox root = null;
        LymytzService.openWindow("/pages/data/read_log.fxml", "Log_", root, 630d, 500d);
    }

    @FXML
    public void openViewPreference(ActionEvent ev) {
        //Ouvre la fenêtre de gestion des imports
        LymytzService.openWindow("/pages/main/preference.fxml", "Lymytz /Préférence", null, 550.0, 600.0);
    }

    @FXML
    public void openViewCatalogue(ActionEvent ev) {
        //Ouvre la fenêtre de gestion des imports
        LymytzService.openWindow("/pages/data/form_catalogue.fxml", "Lymytz /Catalogue", null, 1000.0, 550.0);
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
        if (!LocalQueryFactories.pingServer()) {
            LymytzService.openAlertDialog("Impossible d'ouvrir une connexion à la base de données locale, veuillez consulter le fichier de log pour en connaître la cause", "Echec de connexion", "Echec de connexion à la source de données locale", Alert.AlertType.ERROR);
        } else {
            LymytzService.success();
        }
    }

    @FXML
    private void openViewAbout(ActionEvent event) {
        VBox root = null;
        LymytzService.openWindow("/pages/component/form_about.fxml", "Lymytz /A propos", root, 400.0, 305.0, false);
    }

    @FXML
    private void openViewRaccorcis(ActionEvent event) {
        VBox root = null;
        LymytzService.openWindow("/pages/component/racourcis_clavier.fxml", "Lymytz /A propos", root, 400.0, 450.0, false);
    }

    @FXML
    private void openViewComptes(ActionEvent event) {
        openAndLoadFormCompte();
    }

    public void openAndLoadFormCompte() {
        MyComptesController controler = LymytzService.openWindow("/pages/main/form_comptes.fxml", "Lymytz /Mon compte", null, 1000.0, 605.0, true);
        controler.setMainController(this);
    }

    @FXML
    public void openTocreateNewFacture(ActionEvent ev) {
        try {
            VBox root = null;
            FactureController controler = LymytzService.openWindow("/pages/main/form_create_facture.fxml", "Lymytz /Nouvelle Facture", root, 600.0, 305.0, false, this);
            if (controler != null) {
                controler.initDataForm(this);
            }
        } catch (Exception ex) {
            LOGGER.error("Erreur à l'ouverture de la page form_create_facture.fxml", ex);
            LymytzService.openAlertDialog("Impossible d'ouvrir la page de création de la facture. Consultez votre fichier de log pour en savoir plus sur la cause", "Ouverture Impossible", "Ouverture de la page", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void openViewListingVente(ActionEvent ev) {
        VBox root = null;
        ListingController controler = LymytzService.openWindow("/pages/data/form_listing.fxml", "Lymytz /Listing des ventes", root, 950.0, 605.0, true, this);
        if (controler != null) {
            controler.initPage(this);
        }
    }

    @FXML
    public void openViewListingVenteCumule(ActionEvent ev) {
        VBox root = null;
        ListingCumuleController controler = LymytzService.openWindow("/pages/data/form_listing_1.fxml", "Lymytz /Listing des ventes", root, 950.0, 605.0, true, this);
        if (controler != null) {
            controler.initPage(this);
        }
    }

    @FXML
    public void openViewListFacture(ActionEvent ev) {
        VBox root = null;
        ListFacturesController controler = LymytzService.openWindow("/pages/data/form_facture.fxml", "Lymytz /Liste de Factures", root, 950.0, 605.0, true, this);
        if (controler != null) {
            controler.initPage(this, TYPE_FV);
        }
    }

    @FXML
    public void openViewListCommandes(ActionEvent ev) {
        VBox root = null;
        ListFacturesController controler = LymytzService.openWindow("/pages/data/form_facture.fxml", "Lymytz /Liste de Factures", root, 900.0, 605.0, true, this);
        if (controler != null) {
            controler.initPage(this, Constantes.TYPE_BCV);
        }
    }

    @FXML
    public void openDlgStatusSync(ActionEvent ev) {
        VBox root = null;
        ListenTableController controler = LymytzService.openWindow("/pages/main/synchro/listen_table.fxml", "Lymytz /Etat Synchronisation", root, 900.0, 505.0, true, this);
        if (controler != null) {
            controler.initPage(this);
        }
    }

    @FXML
    public void openDlgStatusSyncImp(ActionEvent ev) {
        VBox root = null;
        ListenRemoteTableController controler = LymytzService.openWindow("/pages/main/listen_table_remote.fxml", "Lymytz /Etat Synchronisation", root, 900.0, 505.0, true, this);
        if (controler != null) {
            controler.initPage(this);
        }
    }

    @FXML
    private void printFacture(ActionEvent event) {
        if (!TAB_FACTURES.getTabs().isEmpty()) {
            Onglets ong = (Onglets) TAB_FACTURES.getSelectionModel().getSelectedItem();
            Properties param = new Properties();
            Properties.readFile(LymytzService.getFileInputStream());
            if (ong != null) {
                ong.setNetAPayer(ong.getFacture().getMontantTTC());
                if (param.getTypeRapport().equals(UtilsProject.TYPE_RAPPORT_TICKET)) {
                    PrintTiket pt = new PrintTiket(0, "");
                    pt.setFacture(ong.getFacture());
                    pt.setMontantAvance(ong.getFacture().getMontantAvance());
                    pt.setMontantRecu(ong.getMontantRecu());
                    pt.setMontantTotal(ong.getFacture().getMontantTotal());
                    pt.setNetAPayer(ong.getNetAPayer());
                    new Thread(pt).start();
                } else {
                    PrintFacture preview = new PrintFacture();
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
    private void showDisplayCatalogueOptions(ActionEvent event) {
        //1. Controle la caisse et le mode de paiement
        Onglets tab = (Onglets) TAB_FACTURES.getSelectionModel().getSelectedItem();
        if (tab != null) {
            ServiceCreateFacture service = new ServiceCreateFacture(this);
            service.saveOrGeneratedPaiement_(tab);
        }
    }

    public void openDlgCalculatrice(Onglets onglet, String source, KeyBoardAction action, ContentPanier content) {
        try {
            if (onglet == null || onglet.getContentFacture().isEmpty()) {
                LymytzService.openAlertDialog("Votre panier est vide", "erreur contenu", "Erreur !", Alert.AlertType.ERROR);
                return;

            }
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource("/pages/component/claviers.fxml"));
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
            ClaviersController controler = load.getController();
            controler.initController(this, onglet, stage, source, action, content);
            scene.setOnKeyReleased(event -> {
                if (event.getCode().equals(KeyCode.ESCAPE)) {
                    stage.close();
                }
            });
        } catch (IOException ex) {
            LOGGER.error("Une exception survenue à l'ouverture du clavier", ex);
        }
    }

    public void openDlgCalculatrice(Onglets onglet, String source, KeyBoardAction action) {
        openDlgCalculatrice(onglet, source, action, null);
    }

    public void opnClotureFiche(Long idHeader) {
        try {
            FXMLLoader load = new FXMLLoader(LocalLoader.class
                    .getResource("/pages/main/form_virement_recette.fxml"));
            VBox root = load.load();
            Scene scene = new Scene(root, 600, 250);
            Stage stage = new Stage();

            stage.setScene(scene);

            stage.setTitle("Lymytz:extended caisse apps");
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(UtilsProject.primaryStage);
            stage.show();
            FormVirementController controller = load.getController();
            controller.initFormVirement(idHeader, UtilsProject.caisse, this, stage);
            scene.setOnKeyReleased(
                    (KeyEvent event) -> {
                        if (event.getCode().equals(KeyCode.ESCAPE)) {
                            stage.close();
                        }
                    });
        } catch (IOException ex) {
            LOGGER.error("Une exception survenue à l'ouverture du formulaire e virement", ex);
        }

    }

    @FXML
    public void goingOut(ActionEvent ev) {
        goingOutApplication();
    }

    public void goingOutApplication() {
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setTitle("Fermeture de session!");
        al.setContentText("Souhaitez vous terminer cette session ?");
        Optional<ButtonType> result = al.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            UtilsProject.primaryStage.close();
            System.exit(0);
            LymytzService.openApps(UtilsProject.primaryStage);
        }
    }

    public boolean closeApplication() {
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setTitle("Fermeture de session!");
        al.setContentText("Souhaitez vous terminer cette session et Arrêter l'application?");
        Optional<ButtonType> result = al.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            Platform.exit();
            System.exit(0);
            return true;
        } else {
            return false;
        }
    }

    @FXML
    public void reloadPropertieFile(ActionEvent ev) {
        UtilsProject.reloadFilePropertie();
        if (UtilsProject.properties != null) {
            UtilsProject.loadInitData();
            loadProperties();
            ToastService.show(getMainStage(), "Les propriétés de l'application ont été rechargés avec succès ", 2500, INFO);
        }
    }

    @FXML
    public void livrerFacture(ActionEvent ev) {
        Onglets fac = (Onglets) TAB_FACTURES.getSelectionModel().getSelectedItem();
        saveOrGenerateBl(fac);
    }

    public void saveOrGenerateBl(Onglets currentOnglet) {
        if (currentOnglet != null) {
            ServiceLivraison service = new ServiceLivraison(this);
            if (!currentOnglet.getFacture().isCommande()) {
                service.saveLivraison(currentOnglet.getFacture(), true);
            } else {
                Alert dlg = new Alert(Alert.AlertType.CONFIRMATION, "Confirmez vous la livraison de ce bon de commande ?", new ButtonType("Oui"), new ButtonType("Non"));
                Optional<ButtonType> resp = dlg.showAndWait();
                if (resp.isPresent() && resp.get().getText().equals("Oui") &&
                        (service.transmisOrder(currentOnglet.getFacture()))) {
                    BTN_LIVRER.setVisible(false);

                }
            }
        }
    }


    public void loadProperties() {
        if (UtilsProject.isProductionEnv()) {
            //Lance des ping sur le serveur distant pour savoir s'il est toujours connecté
            //cette methode est lancé seulement lorsque le serveur est en mode BOTH
            if (PropertiesManager.getInstance().getVal(Constantes.KEY_MODE).equals(Constantes.APPS_MODE_BOTH)) {
                new ListenServersRemote(10, this).start();
            } else {
                //lance la socket d'écoute client... (si on est en mode replication)
                if (Boolean.TRUE.equals(UtilsProject.REPLICATION)) {
                    Thread t = new Thread(() -> {
                        clientSocket = new ClientMessage("", this);
                        clientSocket.initClient();
                    });
                    t.start();
                    verifySocketIsConnected();
                }
            }
            //Lance des ping sur le serveur local pour savoir s'il est toujours connecté
            new ListenServersLocal(10, this).start();

        }
        ICO_ALERT_EX.setVisible(false);
        ICO_ALERT_IM.setVisible(false);
        if (UtilsProject.isReplicationMode()) {
            //Notifie sur la vue utilisateur si des opérations de synchronisation s'exécutent ou pas
            notifyIfSynchroIsRunning();
            startSynchronisation();
        } else {
            hideAllAppMonitoringIcone();
        }
    }

    private void startSynchronisation() {
        startSynchronisationDataOut();
        startSynchronisationDataIn();
    }

    private void startSynchronisationDataIn() {
        if (myServiceIn == null || !myServiceIn.isRunning()) {
            myServiceIn = new SynchronizeDataIn(25, this);
            myServiceIn.start();
        }
    }

    private void startSynchronisationDataOut() {
        if (myServiceOut == null || !myServiceOut.isRunning()) {
            myServiceOut = new SynchronizeDataOut(20, this);
            myServiceOut.start();
        }
    }

    private void hideAllAppMonitoringIcone() {
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

    public void notifyIfSynchroIsRunning() {
        LAB_TITLE_SYNC_T.setVisible(true);
        Thread t = new Thread(() -> {
            while (Boolean.FALSE.equals(UtilsProject.STOP_LISTEN)) {
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
                if (WsSynchro.runningIn.get() && SynchronizeDataIn.running) {
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
                Helpers.sleepFor(2000);
            }
        });
        t.start();
    }

    //méthode qui vérifie que la socket client est toujours connecté au serveur
    public void verifySocketIsConnected() {
        if (Constantes.APPS_MODE_SINGLE.equals(PropertiesManager.getInstance().getVal(Constantes.KEY_MODE))) {
            new Thread(this::monitorSocketConnection).start();
        }
    }

    private void monitorSocketConnection() {
        while (true) {
            checkAndMarkConnect();
            Helpers.sleepFor(5000);
        }
    }

    private void checkAndMarkConnect() {
        if (isSocketClose()) {
            new Thread(() -> clientSocket.initClient()).start();
        } else {
            this.setConnect(true);
        }
    }

    private boolean isSocketClose() {
        return clientSocket != null && clientSocket.getSocketClient() != null && clientSocket.getSocketClient().isClosed();
    }

    public Stage getMainStage() {
        return (Stage) TOOLBAR.getScene().getWindow();
    }
}
