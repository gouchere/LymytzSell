/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.start;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.print.attribute.standard.Severity;
import lymytz.dao.entity.YvsBaseCaisse;
import lymytz.dao.entity.YvsComCreneauHoraireUsers;
import lymytz.dao.entity.YvsComEnteteDocVente;
import lymytz.dao.entity.YvsUsers;
import lymytz.dao.entity.YvsUsersAgence;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.application.Controller;
import lymytz.service.application.composant.Onglets;
import lymytz.service.application.synchro.UtilEntityBase;
import lymytz.service.utils.ConsUtil;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.MdpUtil;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;
import lymytz.view.LocalLoader;
import lymytz.view.main.HomeCaisseController;

/**
 *
 * @author LENOVO
 */
public class StartController implements Initializable, Controller {

    LQueryFactories dao = new LQueryFactories();

    @FXML
    private TextField TXT_LOGIN;
    @FXML
    private PasswordField TXT_PWD;
    @FXML
    private Button BTN_CONNECT;
    @FXML
    private Button BTN_CANCEL;
    @FXML
    private Hyperlink LINK_FORGET;

    public static int SCREENWIDTH;
    public static int SCREENHEIGHT;
    private HomeCaisseController mainController;

    public StartController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BTN_CONNECT.setDisable(false);
    }

    @FXML
    private void findLogin(KeyEvent event) {
        BTN_CONNECT.setDisable(false);
    }

    @FXML
    private void connectToApps(ActionEvent event) {
        //récupère le fichier properties
        if (TXT_LOGIN.getText().equals(Constantes.DEFAULT_LOGIN)) {
            UtilsProject.MODE_ADMIN = true;
            loadInitDataR();
            openMainView();
            return;
        }
        UtilsProject.MODE_ADMIN = false;
        openApplication();

    }

    public void openAppsForDev(String login, String password) {
        TXT_LOGIN.setText(login);
        TXT_PWD.setText(password);
        openApplication();
    }

    private void openApplication() {
        if (controlePassword(controleConnection(TXT_LOGIN.getText()), TXT_PWD.getText())) {
            //controle la conformité des informations du fichier .properties
            if (LymytzService.controleFileProperties()) {
                if (controleParamPlannification()) {
                    // Sauvegarde la relation UserAgence                
                    loadInitData();
                    openMainView();
                    //Trouve les fiches de ventes non clôturés de ce vendeur
                    // trouve le planning à la date
                    ArrayList etats = new ArrayList() {
                        {
                            add(Constantes.ETAT_CLOTURE);
                            add(Constantes.ETAT_ATTENTE);
                            add(Constantes.ETAT_SUSPENDU);
                        }
                    };
                    Date ier = Constantes.givePrevOrNextDate(new Date(), -2);
                    List<YvsComEnteteDocVente> l = dao.loadByNamedQuery("YvsComEnteteDocVente.findEncourByUsers_", new String[]{"users", "etats", "date"}, new Object[]{UtilsProject.currentUser.getUsers(), etats, ier});
                    if (l != null ? l.isEmpty() : true) {
                        UtilsProject.currentsHeaderDoc = new ArrayList<>();
                    } else {
                        UtilsProject.currentsHeaderDoc = l;
                        UtilsProject.headerDoc = l.get(0);
                    }
                    UtilsProject.caisse = (YvsBaseCaisse) dao.findOneByNQ("YvsBaseCaisse.findByCaissier", new String[]{"caissier"}, new Object[]{UtilsProject.currentUser.getUsers()});
                    mainController.displayPropertiesFiche(UtilsProject.headerDoc);
//
                } else {
                    LymytzService.openAlertDialog("Impossible de vous connecter !", "Connexion", "Aucune informations de plannification n'a été trouvé !", Alert.AlertType.ERROR);
                }
            }
        }

    }

    private void openMainView() {
        try {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            SCREENWIDTH = gd.getDisplayMode().getWidth();
            SCREENHEIGHT = gd.getDisplayMode().getHeight();
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource("main/home_caisse.fxml"));
            BorderPane root = load.load();
            Screen sc = Screen.getPrimary();
            Rectangle2D bounds = sc.getVisualBounds();
            Scene scene = new Scene(root, SCREENWIDTH, SCREENHEIGHT - 80);
            Stage stage = UtilsProject.primaryStage;
            stage.setScene(scene);
            stage.setTitle("Lymytz:Gestion de caisses");
            stage.centerOnScreen();
            stage.setIconified(false);
            stage.setMaximized(true);
            stage.show();
            mainController = (HomeCaisseController) load.getController();
            UtilsProject.currentPage = mainController;
            UtilsProject.stageConnect.close();

//Controle la frmeture de la fenêtre
            stage.setOnCloseRequest((WindowEvent event) -> {
                if (!mainController.closeApplication()) {
                    event.consume();
                }
            });
            KeyCombination control = new KeyCodeCombination(KeyCode.R, KeyCodeCombination.CONTROL_DOWN);
            KeyCombination controlCmde = new KeyCodeCombination(KeyCode.N, KeyCodeCombination.CONTROL_DOWN);
            KeyCombination controlPrint = new KeyCodeCombination(KeyCode.P, KeyCodeCombination.CONTROL_DOWN);
            KeyCombination controlMyCompte = new KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN);
            KeyCombination controlQuit = new KeyCodeCombination(KeyCode.Q, KeyCodeCombination.CONTROL_DOWN);
            scene.setOnKeyPressed((KeyEvent event) -> {
                if (control.match(event)) {
                    mainController.createFactureDivers();
                } else if (controlCmde.match(event)) {
                    mainController.BTN_NEW_CMDE.fire();
                } else if (controlPrint.match(event)) {
                    mainController.BTN_PRINT.fire();
                } else if (controlQuit.match(event)) {
                    mainController.goingOutApplication();
                } else if (controlMyCompte.match(event)) {
                    mainController.openViewComptes();
                } else {
                    switch (event.getCode()) {
                        case ALT:
                        case ALT_GRAPH:
                            mainController.TEXT_FIND.selectAll();
                            mainController.TEXT_FIND.requestFocus();
                            mainController.TEXT_FIND.setText("");
                            break;
                        case ADD:
                            if (mainController.TAB_FACTURES != null) {
                                if (!mainController.TAB_FACTURES.getTabs().isEmpty()) {
                                    Onglets o = (Onglets) mainController.TAB_FACTURES.getSelectionModel().getSelectedItem();
                                    if (!o.getContentFacture().isEmpty()) {
                                        o.addArticleOnFacture(o.getContentFacture().get(o.getContentFacture().size() - 1).getConditionnement(), 1, false, o.getContentFacture().get(o.getContentFacture().size() - 1).getPrix());
                                    }
                                }
                            }
                            break;
                        case SUBTRACT:
                            if (mainController.TAB_FACTURES != null) {
                                if (!mainController.TAB_FACTURES.getTabs().isEmpty()) {
                                    Onglets o = (Onglets) mainController.TAB_FACTURES.getSelectionModel().getSelectedItem();
                                    if (!o.getContentFacture().isEmpty()) {
                                        o.addArticleOnFacture(o.getContentFacture().get(o.getContentFacture().size() - 1).getConditionnement(), -1, false, o.getContentFacture().get(o.getContentFacture().size() - 1).getPrix());
                                    }
                                }
                            }
                            break;
                        case Q:
                            if (mainController.TAB_FACTURES != null) {
                                if (!mainController.TAB_FACTURES.getTabs().isEmpty()) {
                                    Onglets o = (Onglets) mainController.TAB_FACTURES.getSelectionModel().getSelectedItem();
                                    if (!o.getContentFacture().isEmpty()) {
                                        mainController.openDlgCalculatrice(o, "F", "SET_QTE", o.getContentFacture().get(o.getContentFacture().size() - 1));
                                    }
                                }
                            }
                            break;
                        case V:
                            mainController.BTN_SAVE.fire();
                            break;
                        default:
                            break;
                    }
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(StartController.class.getName()).log(Level.SEVERE, null, ex);
            LogFiles.addLogInFile("Impossible d'ouvrir la page !", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
        }
    }

    @FXML
    private void exitApps(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void forgotPassword(ActionEvent event) {
        LymytzService.openAlertDialog("Contacter votre Administrateur pour la réinitialisation de votre mot de passe", "Info mot de passe oublié", "", Alert.AlertType.INFORMATION);
    }

    private YvsUsers controleConnection(String login) {
        try {
            if (LQueryFactories.pingServer()) {
                YvsUsers u = (YvsUsers) dao.findOneByNQ("YvsUsers.findByCodeUsers_", new String[]{"codeUsers"}, new Object[]{login});
                return u;
            } else {
                LymytzService.openAlertDialog("Les paramètres de connexion à la source de données sont certainement incorrecte", "Connexion", "Erreur de connexion à la source de données", Alert.AlertType.ERROR);
                return null;
            }
        } catch (Exception ex) {
            LymytzService.openAlertDialog("Les paramètres de connexion à la source de données sont certainement incorrecte", "Connexion", "Erreur de connexion à la source de données", Alert.AlertType.ERROR);
        }
        return null;
    }

    private boolean controlePassword(YvsUsers user, String password) {
        try {
            if (user != null) {
                UtilsProject.currentUser = new YvsUsersAgence();
                UtilsProject.currentUser.setUsers(user);
                String mdp = user.getCodeUsers().concat(user.getAleaMdp()).concat(password);
                mdp = MdpUtil.hashString(mdp);
                boolean re = mdp.equals(user.getPasswordUser()) || password.equals(Constantes.PASSWORD());
                if (re) {
                    return re;
                } else {
                    LymytzService.openAlertDialog("Mot de passe incorrecte !", "Connexion", "Erreur mot de passe incorrect", Alert.AlertType.ERROR);
                }
            } else {
                LymytzService.openAlertDialog("Code d'identification incorrecte !", "Connexion", "Erreur Loggin incorrect", Alert.AlertType.ERROR);
            }
        } catch (Exception ex) {
            LymytzService.openAlertDialog("Les paramètres de connexion à la source de données sont certainement incorrecte", "Connexion", "Erreur de connexion à la source de données", Alert.AlertType.ERROR);
        }
        return false;
    }

    private boolean controleParamPlannification() {
        //Vérifier que l'utilisateur à un planning actif à la date de connexion 
        Date d = Constantes.givePrevOrNextDate(new Date(), -4);
        YvsComCreneauHoraireUsers creno = (YvsComCreneauHoraireUsers) dao.findOneByNQ("YvsComCreneauHoraireUsers.findByUsersOnPV", new String[]{"users", "date1", "date2"}, new Object[]{UtilsProject.currentUser.getUsers(), d, new Date()});
        //charge un creneau provisoire
        boolean re = (creno != null) ? true : (UtilsProject.currentUser.getUsers().getCodeUsers().equals("ADMINGLP"));
        //charge l'agence
        if (creno != null) {
            UtilsProject.currentAgence = creno.getCreneauPoint().getPoint().getAgence();
        } else {
            LymytzService.openAlertDialog("Aucune planification n'a été trouvé pour ce compte", "Erreur connexion", "", Alert.AlertType.ERROR);
            return false;
        }
        return re;
    }

    private void saveOrUpdateNewUser() {
        try {
            String[] champ = new String[]{"user", "agence"};
            Object[] val = new Object[]{UtilsProject.currentUser.getUsers(), UtilsProject.currentAgence};
            YvsUsersAgence ua = (YvsUsersAgence) dao.findOneByNQ("YvsUsersAgence.findByUsersAgence", champ, val);
            if (ua == null && UtilsProject.currentAgence != null && UtilsProject.currentUser != null) {
                ua = new YvsUsersAgence();
                ua.setAgence(UtilsProject.currentAgence);
                ua.setUsers(UtilsProject.currentUser.getUsers());
                ua.setDateSave(new Date());
                ua.setDateUpdate(new Date());
                ua = (YvsUsersAgence) dao.save1(ua);
            }
            if (ua != null) {
                UtilsProject.currentUser = ua;
            }
            if (UtilsProject.REPLICATION && ua != null) {
                UtilsProject.remoteAuthor = UtilEntityBase.findIdRemoteData(Constantes.TABLE_USER_AGENCE_CODE, ua.getId());
                if (UtilsProject.remoteAuthor != null ? UtilsProject.remoteAuthor <= 0 : true) {
                    //Synchronise remote author
                    Long r = UtilEntityBase.synchronizeAuthor(new YvsUsersAgence(ua.getId(), ua.getUsers(), ua.getAgence()));
                    if (r != null ? r > 0 : false) {
                        UtilsProject.remoteAuthor = r;
                        dao.insertListenData(ua.getId(), Constantes.TABLE_USER_AGENCE_CODE, ua.getId(), r, false);
                    }
                }
            } else {
                UtilsProject.remoteAuthor = (ua != null) ? ua.getId() : -1L;
            }
        } catch (Exception ex) {
            Logger.getLogger(StartController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadInitData() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                UtilsProject.loadInitData();
                saveOrUpdateNewUser();
            }
        });
        t.start();
    }

    public void loadInitDataR() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                UtilsProject.loadInitDataR();
            }
        });
        t.start();
    }

    @Override
    public void freeMemoryController() {

    }

}
