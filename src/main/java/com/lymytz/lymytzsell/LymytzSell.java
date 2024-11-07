package com.lymytz.lymytzsell;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.lymytz.lymytzsell.service.ServeurMessage;
import com.lymytz.lymytzsell.service.application.ManagedApplication;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.LymytzService;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import com.lymytz.lymytzsell.service.utils.log.ListenFolder;
import com.lymytz.lymytzsell.service.utils.log.LogFiles;
import com.lymytz.lymytzsell.view.LocalLoader;
import com.lymytz.lymytzsell.view.start.LaunchApps;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.Severity;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * @author LENOVO
 */
public class LymytzSell extends Application {

    @Setter
    @Getter
    private Stage primaryStage;
    @Setter
    @Getter
    private boolean connecte = false;
    Exception exception;
    private static final Logger LOGGER = getLogger(LymytzSell.class.getName());


    public LymytzSell() {
        // not necessary to implement
    }

    @Override
    public void init() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Ouverture de l'application à {}", ISO_DATE_TIME.format(LocalDateTime.now()));
        }
        initApps(true);
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> getLogger("UncaughtExceptionLogger").error("Uncaught exception in thread {}", thread.getName(), throwable));
        //créer un fichier de log
        LogFiles.createLogfile();

    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Thread t = new Thread(new ListenFolder());
        t.setName("Listen folder");
        t.start();
        Platform.setImplicitExit(true);
        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);
        initializePort();
        startApps();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(LymytzSell.class, "--preloader", LaunchApps.class.getName(), Arrays.toString(args));
        LOGGER.info("Démarrage de l'application de caisse...");
        //Lance ensuite la méthode init() et ensuite la méthode start
        //Créer et lancer le thred d'écoute du fichier de log       

    }

    public void initApps(boolean first) {
        try {
            setConnecte(false);
        } catch (Exception ex) {
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
            LOGGER.error(ex.getMessage());
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
            LOGGER.error("Unable to init system tray", e);
        }
    }

    public void initializePort() {
        UtilsProject.loadFilePropertie();
        int port = Integer.parseInt(UtilsProject.properties.getProperty(Constantes.KEY_APPS_PORT));
        UtilsProject.ENVIRONNEMENT = UtilsProject.properties.getProperty(Constantes.KEY_ENVIRONNEMENT);
        try {
            UtilsProject.server = new ServerSocket(port);
        } catch (IOException e) {
            LymytzService.openAlertDialog("Arrêt du demarrage", "Erreur !", "Une instance de l'application est déjà en cours: Vérifié dans votre zone de notification", Alert.AlertType.ERROR);
            LOGGER.error("Unable to init system tray", e);
            System.exit(0);
        }
        new Thread(LymytzSell.this::initialiserLaSynchronisation).start();
    }

    private void initialiserLaSynchronisation() {
        UtilsProject.loadFilePropertie();
        String mode = UtilsProject.properties.getProperty(Constantes.KEY_MODE);
        if (Constantes.APPS_MODE_BOTH.equals(mode)) {
            ServeurMessage.initSocket();
        }
    }
}
