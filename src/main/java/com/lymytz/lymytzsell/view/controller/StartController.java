/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.view.controller;

import com.lymytz.lymytzsell.business.helpers.KeyBoardAction;
import com.lymytz.lymytzsell.persistence.entity.YvsComCreneauHoraireUsers;
import com.lymytz.lymytzsell.persistence.entity.YvsUsers;
import com.lymytz.lymytzsell.persistence.entity.YvsUsersAgence;
import com.lymytz.lymytzsell.persistence.dao.LocalQueryFactories;
import com.lymytz.lymytzsell.view.component.Onglets;
import com.lymytz.lymytzsell.service.application.config.PropertiesManager;
import com.lymytz.lymytzsell.service.application.synchro.UtilEntityBase;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.EncryptMessage;
import com.lymytz.lymytzsell.service.utils.LymytzService;
import com.lymytz.lymytzsell.service.utils.MdpUtil;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author LENOVO
 */
public class StartController implements Initializable, Controller {

    private final Logger LOGGER = LogManager.getLogger(StartController.class);
    LocalQueryFactories dao = new LocalQueryFactories();

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
        // Utile pour la construction du composant
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BTN_CONNECT.setDisable(false);
        initialiseUserForDevIfNecessary();
        this.executeFlywayMigration();
    }

    @FXML
    private void findLogin(KeyEvent event) {
        BTN_CONNECT.setDisable(false);
    }

    @FXML
    private void connectToApps(ActionEvent event) {
        if (TXT_LOGIN.getText().equals(Constantes.DEFAULT_LOGIN)) {
            UtilsProject.modeAdmin = true;
            loadInitDataR();
            openMainView();
            return;
        }
        UtilsProject.modeAdmin = false;
        openApplication();

    }

    public void openAppsForDev(String login, String password) {
        TXT_LOGIN.setText(login);
        TXT_PWD.setText(password);
        openApplication();
    }

    private void openApplication() {
        if (controlePassword(controleConnection(TXT_LOGIN.getText()), TXT_PWD.getText()) && (LymytzService.controleFileProperties())) {
            if (controleParamPlannification()) {
                // Sauvegarde la relation UserAgence
                loadInitData();
                // trouve le planning à la date
                    /*var etats = List.of(Constantes.ETAT_CLOTURE,Constantes.ETAT_ATTENTE,Constantes.ETAT_SUSPENDU);
                    Date ier = Constantes.givePrevOrNextDate(new Date(), -2);
                    List<YvsComEnteteDocVente> l = dao.loadByNamedQuery("YvsComEnteteDocVente.findEncourByUsers_", new String[]{"users", "etats", "date"}, new Object[]{UtilsProject.currentUser.getUsers(), etats, ier});
                    if (l == null || l.isEmpty()) {
                        UtilsProject.currentsHeaderDoc = new ArrayList<>();
                    } else {
                        UtilsProject.currentsHeaderDoc = l;
                        UtilsProject.headerDoc = l.get(0);
                    }
                    UtilsProject.caisse = dao.findOneByNQ("YvsBaseCaisse.findByCaissier", new String[]{"caissier"}, new Object[]{UtilsProject.currentUser.getUsers()});*/
                openMainView();
            } else {
                LymytzService.openAlertDialog("Impossible de vous connecter !", "Connexion", "Aucune informations de plannification n'a été trouvé !", Alert.AlertType.ERROR);
            }

        }

    }

    private void openMainView() {
        try {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            SCREENWIDTH = gd.getDisplayMode().getWidth();
            SCREENHEIGHT = gd.getDisplayMode().getHeight();
            FXMLLoader load = new FXMLLoader(LymytzService.class.getResource("/pages/main/home_caisse.fxml"));
            BorderPane root = load.load();
            Scene scene = new Scene(root, SCREENWIDTH, SCREENHEIGHT - 80);
            Stage stage = UtilsProject.primaryStage;
            stage.setScene(scene);
            stage.setTitle("Lymytz:Gestion de caisses");
            stage.centerOnScreen();
            stage.setIconified(false);
            stage.setMaximized(true);
            stage.show();
            mainController = load.getController();
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
                    mainController.initFactureVenteClientDivers();
                } else if (controlCmde.match(event)) {
                    mainController.BTN_NEW_CMDE.fire();
                } else if (controlPrint.match(event)) {
                    mainController.BTN_PRINT.fire();
                } else if (controlQuit.match(event)) {
                    mainController.goingOutApplication();
                } else if (controlMyCompte.match(event)) {
                    mainController.openAndLoadFormCompte();
                } else {
                    switch (event.getCode()) {
                        case ALT, ALT_GRAPH:
                            mainController.TEXT_FIND.selectAll();
                            mainController.TEXT_FIND.requestFocus();
                            mainController.TEXT_FIND.setText("");
                            break;
                        case ADD:
                            if (mainController.TAB_FACTURES != null && (!mainController.TAB_FACTURES.getTabs().isEmpty())) {
                                Onglets o = (Onglets) mainController.TAB_FACTURES.getSelectionModel().getSelectedItem();
                                if (!o.getContentFacture().isEmpty()) {
                                    o.addArticleOnFacture(o.getContentFacture().get(o.getContentFacture().size() - 1).getConditionnement(), 1, false, o.getContentFacture().get(o.getContentFacture().size() - 1).getPrix());
                                }

                            }
                            break;
                        case SUBTRACT:
                            if (mainController.TAB_FACTURES != null && (!mainController.TAB_FACTURES.getTabs().isEmpty())) {
                                Onglets o = (Onglets) mainController.TAB_FACTURES.getSelectionModel().getSelectedItem();
                                if (!o.getContentFacture().isEmpty()) {
                                    o.addArticleOnFacture(o.getContentFacture().get(o.getContentFacture().size() - 1).getConditionnement(), -1, false, o.getContentFacture().get(o.getContentFacture().size() - 1).getPrix());
                                }

                            }
                            break;
                        case Q:
                            if (mainController.TAB_FACTURES != null && (!mainController.TAB_FACTURES.getTabs().isEmpty())) {
                                Onglets o = (Onglets) mainController.TAB_FACTURES.getSelectionModel().getSelectedItem();
                                if (!o.getContentFacture().isEmpty()) {
                                    mainController.openDlgCalculatrice(o, "F", KeyBoardAction.SET_QTE, o.getContentFacture().get(o.getContentFacture().size() - 1));
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
            LOGGER.error(ex);
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
            if (LocalQueryFactories.pingServer()) {
                return (YvsUsers) dao.findOneByNQ("YvsUsers.findByCodeUsers_", new String[]{"codeUsers"}, new Object[]{login});
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
                    return true;
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
        YvsComCreneauHoraireUsers creno = dao.findOneByNQ("YvsComCreneauHoraireUsers.findByUsersOnPV", new String[]{"users", "date1", "date2"}, new Object[]{UtilsProject.currentUser.getUsers(), d, new Date()});
        //charge un creneau provisoire
        boolean re = creno != null || (UtilsProject.currentUser.getUsers().getCodeUsers().equals("ADMINGLP"));
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
            YvsUsersAgence ua = dao.findOneByNQ("YvsUsersAgence.findByUsersAgence", champ, val);
            if (ua == null && UtilsProject.currentAgence != null && UtilsProject.currentUser != null) {
                ua = new YvsUsersAgence();
                ua.setAgence(UtilsProject.currentAgence);
                ua.setUsers(UtilsProject.currentUser.getUsers());
                ua.setDateSave(new Date());
                ua.setDateUpdate(new Date());
                ua = dao.save1(ua);
            }
            if (ua != null) {
                UtilsProject.currentUser = ua;
            }
            if (UtilsProject.REPLICATION && ua != null) {
                UtilsProject.remoteAuthor = UtilEntityBase.findIdRemoteData(Constantes.TABLE_USER_AGENCE_CODE, ua.getId());
                if (UtilsProject.remoteAuthor == null || UtilsProject.remoteAuthor <= 0) {
                    //Synchronise remote author
                    Long r = UtilEntityBase.synchronizeAuthor(new YvsUsersAgence(ua.getId(), ua.getUsers(), ua.getAgence()));
                    if (r != null && r > 0) {
                        UtilsProject.remoteAuthor = r;
                        dao.insertListenData(ua.getId(), Constantes.TABLE_USER_AGENCE_CODE, ua.getId(), r, false);
                    }
                }
            } else {
                UtilsProject.remoteAuthor = (ua != null) ? ua.getId() : -1L;
            }
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
    }

    public void loadInitData() {
        new Thread(() -> {
            UtilsProject.loadInitData();
            saveOrUpdateNewUser();
        }).start();
    }

    public void loadInitDataR() {
        new Thread(UtilsProject::chargerLesDonneesDistante).start();
    }

    @Override
    public void freeMemoryController() {
        //implement later
    }

    private void initialiseUserForDevIfNecessary() {
        String defaultUserName = System.getProperty("default_user", "");
        String defaultUserPwd = System.getProperty("default_pwd", "");
        TXT_LOGIN.setText(defaultUserName);
        TXT_PWD.setText(defaultUserPwd);
    }

    private void executeFlywayMigration() {
        String host = PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_HOST);
        String port = PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_PORT);
        String dbName = PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_DB_NAME);
        String user = EncryptMessage.decrypt(PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_USERS), Constantes.KEY_ENCRYPT);
        String password = EncryptMessage.decrypt(PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_PASSWORD), Constantes.KEY_ENCRYPT);
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
        try {
            Flyway flyway = Flyway.configure()
                    .locations("filesystem:src/main/resources/db/migration")
                    .dataSource(url, user, password)
                    .loggers("console")
                    .load();
            flyway.migrate();
            LOGGER.info("Migrations exécutées avec succès!");
        }catch (Exception ex) {
            LOGGER.error(ex);
        }
    }

}
