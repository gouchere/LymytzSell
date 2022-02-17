package lymytzsell;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sun.javafx.application.LauncherImpl;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.STYLESHEET_CASPIAN;
import static javafx.application.Application.setUserAgentStylesheet;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.print.attribute.standard.Severity;
import lymytz.service.ServeurMessage;
import lymytz.service.application.ManagedApplication;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.ListenFolder;
import lymytz.service.utils.log.LogFiles;
import lymytz.view.LocalLoader;
import lymytz.view.start.LaunchApps;

/**
 *
 * @author LENOVO
 */
public class LymytzSell extends Application {

    private Stage primaryStage;
    boolean quit = false;
    private boolean openDlgConnect = false;
    private boolean connecte = false;
    private boolean openDlgParam = false;
    boolean openWindowApps = false;
    Exception exception;

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
            final Image imgIcon1 = new Image(LocalLoader.class.getResource("icones/memo_apps.png").toExternalForm());
            primaryStage.getIcons().add(imgIcon1);
            setUserAgentStylesheet(STYLESHEET_CASPIAN);
            LymytzService.openApps(primaryStage);
        } catch (Exception ex) {
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
            // ensure awt toolkit is initialized.
            java.awt.Toolkit.getDefaultToolkit();

            // app requires system tray support, just exit if there is no support.
            if (!java.awt.SystemTray.isSupported()) {
                LogFiles.addLogInFile("Votre version de java ne supporte pas le système Tray...", Severity.WARNING);
            }

            // set up a system tray icon.
            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            URL imageLoc = new URL(LocalLoader.class.getResource("icones/memo_apps.png").toExternalForm());
            java.awt.Image image = ImageIO.read(imageLoc);
            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

            // if the user double-clicks on the tray icon, show the main app stage.
            trayIcon.addActionListener((ActionEvent e) -> {
                System.err.println(" double click sur tary icon");
            });
            java.awt.MenuItem readLogItem = new java.awt.MenuItem("Lire les logs");
            readLogItem.addActionListener((ActionEvent e) -> {
                Platform.runLater(() -> {
                });
            });
            java.awt.MenuItem exitItem = new java.awt.MenuItem("Quitter l'application");

            exitItem.addActionListener((ActionEvent e) -> {
                System.exit(0);
            });
            // setup the popup menu for the application.
            final PopupMenu popup = new java.awt.PopupMenu();
            popup.addSeparator();
            popup.add(exitItem);
            popup.addSeparator();
            popup.add(readLogItem);
            trayIcon.setPopupMenu(popup);
            tray.add(trayIcon);
        } catch (java.awt.AWTException | IOException e) {
            Logger.getLogger(LymytzSell.class.getName()).log(Level.SEVERE, "Unable to init system tray", e);
        }
    }

    public void initiatePort() {
        UtilsProject.loadFilePropertie();
        Integer PORT = new Integer(UtilsProject.properties.getProperty(Constantes.KEY_APPS_PORT));  
        UtilsProject.ENVIRONNEMENT = UtilsProject.properties.getProperty(Constantes.KEY_ENVIRONNEMENT);
        try {
            UtilsProject.server = new ServerSocket(PORT);
        } catch (IOException e) {
            LymytzService.openAlertDialog("Arrêt du demarrage", "Erreur !", "Une instance de l'application est déjà en cours: Vérifié dans votre zone de notification", Alert.AlertType.ERROR);
            Logger.getLogger(LymytzSell.class.getName()).log(Level.SEVERE, "Unable to init system tray", e);
            System.exit(0);
        }
        Thread t=new Thread(() -> {
            initEchange();
        });
        t.start();
       
    }

    public void initEchange() {
        UtilsProject.loadFilePropertie();
        String mode = UtilsProject.properties.getProperty(Constantes.KEY_MODE);
        if (mode.equals(Constantes.APPS_MODE_BOTH)) {
            ServeurMessage.initSocket();
        }
    }
}
