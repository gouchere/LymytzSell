package com.lymytz.lymytzsell;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sun.javafx.application.LauncherImpl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Objects;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.print.attribute.standard.Severity;
import com.lymytz.lymytzsell.service.ServeurMessage;
import com.lymytz.lymytzsell.service.application.ManagedApplication;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.LymytzService;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import com.lymytz.lymytzsell.service.utils.log.ListenFolder;
import com.lymytz.lymytzsell.service.utils.log.LogFiles;
import com.lymytz.lymytzsell.view.LocalLoader;
import com.lymytz.lymytzsell.view.start.LaunchApps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author LENOVO
 */
public class LymytzSell extends Application {

    private Stage primaryStage;
    private boolean openDlgConnect = false;
    private boolean connecte = false;
    private boolean openDlgParam = false;
    Exception exception;
    private static final Logger logger= LoggerFactory.getLogger(LymytzSell.class);


    public LymytzSell() {
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public boolean isConnecte() {
        return connecte;
    }

    public void setConnecte(boolean connecte) {
        this.connecte = connecte;
    }

    @Override
    public void init() {
        initApps(true);
        //créer un fichier de log
        LogFiles.createLogfile();
        LogFiles.addLogInFile("Ouverture de l'application...", Severity.REPORT);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Thread t = new Thread(new ListenFolder());
        t.setName("Listen folder");
        t.start();
        Platform.setImplicitExit(true);
        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);
        initiatePort();
        startApps();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LauncherImpl.launchApplication(LymytzSell.class, LaunchApps.class, args);
        logger.debug("Démarrage de l'application de caisse...");
        //Lance ensuite la méthode init() et ensuite la méthode start
        //Créer et lancer le thred d'écoute du fichier de log       

    }

    public void initApps(boolean first) {
        try {
            setConnecte(false);
            openDlgConnect = true;
            openDlgParam = false;
        } catch (Exception ex) {
            openDlgConnect = false;
            openDlgParam = true;
            this.exception = ex;
        } finally {
            if (!first) {
                startApps();
            }
        }
    }

    private void startApps() {
        try {
            final Image imgIcon1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icones/memo_apps.png")));
            primaryStage.getIcons().add(imgIcon1);
            setUserAgentStylesheet(STYLESHEET_CASPIAN);
            LymytzService.openApps(primaryStage);
        } catch (Exception ex) {
            ex.printStackTrace();
            LogFiles.createLogfile();
        }
    }

    public void openViewParam(boolean establish) {
        new ManagedApplication().openViewParam(establish);
    }

    /**
     * Sets up a system tray icon for the application.
     */
    private void addAppToTray() {
        try {
            java.awt.Toolkit.getDefaultToolkit();
            if (!SystemTray.isSupported()) {
                LogFiles.addLogInFile("Votre version de java ne supporte pas le systèm Tray...", Severity.WARNING);
                return;
            }
            // set up a system tray icon.
            SystemTray tray = SystemTray.getSystemTray();
            URL imageLoc = new URL(LocalLoader.class.getResource("/icones/memo_apps.png").toExternalForm());
            java.awt.Image image = ImageIO.read(imageLoc);
            TrayIcon trayIcon = new TrayIcon(image);

            // if the user double-clicks on the tray icon, show the main app stage.
            trayIcon.addActionListener((ActionEvent e) -> {
            });
            MenuItem readLogItem = new MenuItem("Lire les logs");
            readLogItem.addActionListener((ActionEvent e) -> Platform.runLater(() -> {
            }));
            MenuItem exitItem = new MenuItem("Quitter l'application");

            exitItem.addActionListener((ActionEvent e) -> System.exit(0));
            // setup the popup menu for the application.
            final PopupMenu popup = new java.awt.PopupMenu();
            popup.addSeparator();
            popup.add(exitItem);
            popup.addSeparator();
            popup.add(readLogItem);
            trayIcon.setPopupMenu(popup);
            tray.add(trayIcon);
        } catch (java.awt.AWTException | IOException e) {
            logger.error("Unable to init system tray", e);
        }
    }

    public void initiatePort() {
        UtilsProject.loadFilePropertie();
        Integer port = Integer.valueOf(UtilsProject.properties.getProperty(Constantes.KEY_APPS_PORT));
        UtilsProject.ENVIRONNEMENT = UtilsProject.properties.getProperty(Constantes.KEY_ENVIRONNEMENT);
        try {
            UtilsProject.server = new ServerSocket(port);
        } catch (IOException e) {
            LymytzService.openAlertDialog("Arrêt du demarrage", "Erreur !", "Une instance de l'application est déjà en cours: Vérifié dans votre zone de notification", Alert.AlertType.ERROR);
            logger.error("Unable to init system tray", e);
            System.exit(0);
        }
        Thread t=new Thread(LymytzSell.this::initialiserLaSynchronisation);
        t.start();
       
    }

    private void initialiserLaSynchronisation() {
        UtilsProject.loadFilePropertie();
        String mode = UtilsProject.properties.getProperty(Constantes.KEY_MODE);
        if (Constantes.APPS_MODE_BOTH.equals(mode)) {
            ServeurMessage.initSocket();
        }
    }
}
