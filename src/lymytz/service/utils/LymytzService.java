/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javax.print.attribute.standard.Severity;
import lymytz.service.application.ManagedApplication;
import lymytz.service.start.StartController;
import lymytz.service.utils.log.LogFiles;
import lymytz.view.LocalLoader;
import lymytz.view.main.HomeCaisseController;
import lymytzsell.LymytzSell;

/**
 *
 * @author LENOVO
 */
public class LymytzService {

    public static boolean stopThread = false;

    public LymytzService() {
    }

    public static void alertConnexion() {
        Alert dlg = new Alert(Alert.AlertType.ERROR);
        dlg.setTitle("Erreur !!!!! ");
        dlg.setHeaderText("Erreur base de données non accessible !");
        dlg.setContentText("Base de données innaccessible!");
        Stage stage = (Stage) dlg.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(LocalLoader.class.getResource("icones/memo_apps.png").toExternalForm()));
        dlg.showAndWait();
    }

    public static void openAlertDialog(String message, String title, String headersg, Alert.AlertType type) {
        Alert dlg = new Alert(type);
        dlg.setTitle(title);
        dlg.setHeaderText(headersg);
        dlg.setContentText(message);
        Stage stage = (Stage) dlg.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(LocalLoader.class.getResource("icones/memo_apps.png").toExternalForm()));
        dlg.showAndWait();
    }

    public static ButtonType openAlertDialogChoice(String message, String title, String headersg, Alert.AlertType type) {
        Alert dlg = new Alert(type);
        dlg.setTitle(title);
        dlg.setHeaderText(headersg);
        dlg.setContentText(message);
        Optional<ButtonType> re = dlg.showAndWait();
        return re.get();
    }

    public static ButtonType openCustumAlertDialogChoice(String message, String title, String headersg, Alert.AlertType type, CheckBox control) {
        Alert dlg = new Alert(type);
        dlg.setTitle(title);
        dlg.setHeaderText(headersg);
        VBox box = new VBox(new Label(message));
        box.getChildren().add(control);
        dlg.getDialogPane().setContent(box);
        Optional<ButtonType> re = dlg.showAndWait();
        return re.get();
    }

    public static void success() {
        Alert dlg = new Alert(Alert.AlertType.INFORMATION);
        dlg.setTitle("Success !");
        dlg.setHeaderText("Success !");
        dlg.setContentText("");
        Stage stage = (Stage) dlg.getDialogPane().getScene().getWindow();
        dlg.showAndWait();
    }

//    public static boolean autoriserRessource(String ressource) {
//        Requete rq = new Requete();
//        YvsNiveauAcces niveau = null;
//        for (YvsNiveauUsers nu : UtilsProject.currentUser.getUsers().getNiveauxAcces()) {
//            if (nu.getIdNiveau().getSociete().equals(UtilsProject.currentSociete)) {
//                niveau = nu.getIdNiveau();
//                break;
//            }
//        }
//        Boolean re = (Boolean) rq.loadObjectByNameQueries("YvsAutorisationRessourcesPage.findAccesRessource", new String[]{"reference", "niveau"}, new Object[]{ressource, niveau});
//        return re != null ? re : false;
//    }
//    public static boolean autoriserPage(String ressource) {
//        Requete rq = new Requete();
//        YvsNiveauAcces niveau = null;
//        for (YvsNiveauUsers nu : UtilsProject.currentUser.getUsers().getNiveauxAcces()) {
//            if (nu.getIdNiveau().getSociete().equals(UtilsProject.currentSociete)) {
//                niveau = nu.getIdNiveau();
//                break;
//            }
//        }
////        Boolean re = (Boolean) rq.loadObjectByNameQueries("YvsAutorisationPageModule.findAccesRessource", new String[]{"reference", "niveau"}, new Object[]{ressource, niveau});
//        YvsAutorisationPageModule re = (YvsAutorisationPageModule) rq.loadObjectByNameQueries("YvsAutorisationPageModule.findAccesRessource", new String[]{"reference", "niveau"}, new Object[]{ressource, niveau});
//        return re != null ? re.getAcces() : false;
//    }
    public static void openExceptionDialog(String message, String title, String headersg, Alert.AlertType type, Exception ex) {
        Alert dlg = new Alert(type);
        dlg.setTitle(title);
        dlg.setHeaderText(null);
        dlg.setContentText(null);
        Label intro = new Label("L'erreur suivante c'est produite!");
        TextArea text = new TextArea("");
        if (ex != null) {
            text.setText(ex.getMessage().concat("\n"));
            for (StackTraceElement s : ex.getStackTrace()) {
                text.setText(text.getText().concat(s.toString()).concat("\n"));
            }
        }
        text.setEditable(false);
        text.setPrefWidth(500);
        GridPane.setVgrow(text, Priority.ALWAYS);
        GridPane.setHgrow(text, Priority.ALWAYS);
        GridPane grid = new GridPane();
        grid.setMaxWidth(Double.MAX_VALUE);
        grid.add(intro, 0, 0);
        grid.addColumn(0, text);
        dlg.setGraphic(grid);
        dlg.showAndWait();
    }

    public static <T> T openWindow(String page, String titlePage, Parent layout, Double width, Double height, boolean resize, HomeCaisseController home) {
        try {
            T controller;
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource(page));
            layout = load.load();
            Screen sc = Screen.getPrimary();
            Rectangle2D bounds = sc.getVisualBounds();
            Scene scene = new Scene(layout, width, height);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(titlePage);
            stage.centerOnScreen();
            stage.setIconified(false);
            stage.initOwner(UtilsProject.primaryStage);
            stage.setResizable(resize);
            stage.show();
            controller = load.getController();
            scene.setOnKeyReleased((KeyEvent event) -> {
                if (event.getCode().equals(KeyCode.ESCAPE)) {
                    stage.close();
                }
            });
            stage.setOnCloseRequest((WindowEvent event) -> {
                try {
                    Method m = controller.getClass().getMethod("freeMemoryController");
                    m.invoke(controller);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(LymytzService.class.getName()).log(Level.SEVERE, null, ex);
                }
                stage.close();
            });
            if (home != null) {
                home.stageCreateFacture = stage;
            }
            return load.getController();

        } catch (IOException ex) {
            Logger.getLogger(ManagedApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static CustomWindow openWindowNew(String page, String titlePage, Parent layout, Double width, Double height, boolean resize) {
        try {
            CustomWindow re = new CustomWindow();
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource(page));
            layout = load.load();
            Screen sc = Screen.getPrimary();
            Rectangle2D bounds = sc.getVisualBounds();
            Scene scene = new Scene(layout, width, height);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(titlePage);
            stage.centerOnScreen();
            stage.setIconified(false);
            stage.initOwner(UtilsProject.primaryStage);
            stage.setResizable(resize);
            stage.show();
            re.setController(load.getController());
            re.setStage(stage);
            scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode().equals(KeyCode.ESCAPE)) {
                        stage.close();
                    }
                }
            });
            stage.setOnCloseRequest((WindowEvent event) -> {
                try {
                    Method m = re.getController().getClass().getMethod("freeMemoryController");
                    m.invoke(re.getController());
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(LymytzService.class.getName()).log(Level.SEVERE, null, ex);
                }
                stage.close();
            });
            return re;

        } catch (IOException ex) {
            Logger.getLogger(ManagedApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static <T> T openWindow(String page, String titlePage, Parent layout, Double width, Double height) {
        return openWindow(page, titlePage, layout, width, height, true);
    }

    public static <T> T openWindow(String page, String titlePage, Parent layout, Double width, Double height, boolean resize) {
        return openWindow(page, titlePage, layout, width, height, resize, null);
    }

    public static Date onlyDate(Date d) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date giveDate(LocalDate d) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.DAY_OF_MONTH, d.getDayOfMonth());
        c.set(Calendar.MONTH, d.getMonthValue() - 1);
        c.set(Calendar.YEAR, d.getYear());
        return c.getTime();
    }

    public static FileInputStream getFileInputStream() {
        File file = new File("conf/servConfig.ltz");
        if (!file.exists()) {
            file = new File("conf");
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File("conf/servConfig.ltz");
            try {
                file.createNewFile();
            } catch (IOException ex) {
//                Logger.getLogger(LymytzSell.class.getName()).log(Level.SEVERE, null, ex);
                LymytzService.openExceptionDialog("Ereur 1", "Erreur fichier ", "Erreur Fatal A!", Alert.AlertType.ERROR, ex);
            }
        }
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException ex) {
//            Logger.getLogger(LymytzSell.class.getName()).log(Level.SEVERE, null, ex);
            LymytzService.openExceptionDialog("Ereur", "Erreur file 2", "Erreur Fatal B!", Alert.AlertType.ERROR, ex);
        }
        return null;
    }

    public static FileInputStream getPropertiesFileInputStream() {
        File file = new File("conf/application.properties");
        if (!file.exists()) {
            try {
                //create file
                file.createNewFile();
                //ajoute y des entrée
                UtilsProject.properties.setProperty(Constantes.KEY_APPS_PORT, "1025");
                UtilsProject.properties.setProperty(Constantes.KEY_CLIENT_DIVERS, "");
                UtilsProject.properties.setProperty(Constantes.KEY_ENVIRONNEMENT, "PRODUCTION");
                UtilsProject.properties.setProperty(Constantes.KEY_LOCAL_AGENCE, "");
                UtilsProject.properties.setProperty(Constantes.KEY_LOCAL_DB_NAME, "lymytz_sell_extension");
                UtilsProject.properties.setProperty(Constantes.KEY_LOCAL_HOST, "localhost");
                UtilsProject.properties.setProperty(Constantes.KEY_LOCAL_PASSWORD, "yves1910/");
                UtilsProject.properties.setProperty(Constantes.KEY_LOCAL_PORT, "5432");
                UtilsProject.properties.setProperty(Constantes.KEY_LOCAL_SOCIETE, "");
                UtilsProject.properties.setProperty(Constantes.KEY_LOCAL_USERS, "postgres");
                UtilsProject.properties.setProperty(Constantes.KEY_MODE, "BOTH");
                UtilsProject.properties.setProperty(Constantes.KEY_MODEL_REGLEMENT, "");
                UtilsProject.properties.setProperty(Constantes.KEY_MODE_REGLEMENT, "");
                UtilsProject.properties.setProperty(Constantes.KEY_ORIENTATION_PRINT, "");
                UtilsProject.properties.setProperty(Constantes.KEY_PAPER_HEIGHT, "0");
                UtilsProject.properties.setProperty(Constantes.KEY_PAPER_WIDTH, "0");
                UtilsProject.properties.setProperty(Constantes.KEY_PAPER_M_BOTOM, "0");
                UtilsProject.properties.setProperty(Constantes.KEY_PAPER_M_LEFT, "0");
                UtilsProject.properties.setProperty(Constantes.KEY_PAPER_M_RIGHT, "0");
                UtilsProject.properties.setProperty(Constantes.KEY_PAPER_M_TOP, "0");
                UtilsProject.properties.setProperty(Constantes.KEY_PATH, "");
                UtilsProject.properties.setProperty(Constantes.KEY_REMOTE_DB_NAME, "lymytz_demo_0");
                UtilsProject.properties.setProperty(Constantes.KEY_REMOTE_HOST, "");
                UtilsProject.properties.setProperty(Constantes.KEY_REMOTE_PASSWORD, "yves1910/");
                UtilsProject.properties.setProperty(Constantes.KEY_REMOTE_PORT, "5432");
                UtilsProject.properties.setProperty(Constantes.KEY_REMOTE_SOCIETE, "");
                UtilsProject.properties.setProperty(Constantes.KEY_REMOTE_USERS, "postgres");
                UtilsProject.properties.setProperty(Constantes.KEY_SECTEUR, "");
                UtilsProject.properties.setProperty(Constantes.KEY_TYPE_PRINT, "TICKET");
                UtilsProject.properties.setProperty(Constantes.KEY_USE_CODE_BARRE, "TRUE");
                UtilsProject.properties.setProperty(Constantes.KEY_USE_PRINTER, "TRUE");
                UtilsProject.properties.setProperty(Constantes.KEY_VILLE, "");
                UtilsProject.properties.setProperty(Constantes.KEY_WEB_HOST, "");
                UtilsProject.properties.setProperty(Constantes.KEY_WEB_PORT, "8080");
                UtilsProject.properties.setProperty(Constantes.KEY_DATE_INIT, Constantes.dfD.format(new Date()));
                FileOutputStream oStream = new FileOutputStream(file);
                UtilsProject.properties.store(oStream, "test");
                return new FileInputStream(file);
            } catch (IOException ex) {
                LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
                Logger.getLogger(LymytzService.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
                Logger.getLogger(LymytzSell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static FileOutputStream getFileoutputStream() {
        File file = new File("conf/servConfig.ltz");
        if (!file.exists()) {
            file = new File("conf");
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File("conf/servConfig.ltz");
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(LymytzSell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LymytzSell.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String getMacAdress() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            return sb.toString();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHostName() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            return ip.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Contrôle que tous les paramètres du fichier properties de l'application
     * existent et ont une valseur*
     */
    public static boolean controleFileProperties() {
        UtilsProject.loadFilePropertie();
        String val;
        if (UtilsProject.properties != null) {
            val = (String) UtilsProject.properties.get(Constantes.KEY_APPS_PORT);
            if (!Constantes.asString(val)) {
                openAlertDialog("Impossible de demarrer l'application", "Le fichier application.properties est mal configurer", "Vous devez initialiser la propriété " + Constantes.KEY_APPS_PORT, Alert.AlertType.ERROR);
                return false;
            }
            val = (String) UtilsProject.properties.get(Constantes.KEY_ENVIRONNEMENT);
            if (!Constantes.asString(val)) {
                openAlertDialog("Impossible de demarrer l'application", "Le fichier application.properties est mal configurer", "Vous devez initialiser la propriété " + Constantes.KEY_ENVIRONNEMENT, Alert.AlertType.ERROR);
                return false;
            }
            val = (String) UtilsProject.properties.get(Constantes.KEY_LOCAL_AGENCE);
            if (!Constantes.asString(val)) {
                openAlertDialog("Impossible de demarrer l'application", "Le fichier application.properties est mal configurer",
                        "Vous devez initialiser la propriété " + Constantes.KEY_LOCAL_AGENCE, Alert.AlertType.ERROR);
                return false;
            }
            val = (String) UtilsProject.properties.get(Constantes.KEY_LOCAL_SOCIETE);
            if (!Constantes.asString(val)) {
                openAlertDialog("Impossible de demarrer l'application", "Le fichier application.properties est mal configurer",
                        "Vous devez initialiser la propriété " + Constantes.KEY_LOCAL_SOCIETE, Alert.AlertType.ERROR);
                return false;
            }
        } else {
            openAlertDialog("Impossible de demarrer l'application", "Le fichier application.properties n'a pas été trouvé", "Vous devez initialiser le fichier de propriété", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    public static void openApps(Stage primary) {
        boolean openParam = false;
        UtilsProject.primaryStage = primary;
        //Test l'existance des info du fichier de paramétrage
        try {
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource("start/form_start.fxml"));
            Pane root = load.load();
            Screen sc = Screen.getPrimary();
            Scene scene = new Scene(root, 500, 280);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initOwner(primary);
            stage.centerOnScreen();
            stage.initStyle(StageStyle.UNDECORATED);
            StartController controller=(StartController)load.getController();
            System.err.println(" .... herer ...");
            stage.show();
            UtilsProject.stageConnect = stage;
            if (!UtilsProject.ENVIRONNEMENT.equals("PRODUCTION")) {
                stage.close();
                 controller.openAppsForDev("ADMIN_CAISSE", "ADMIN");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(LymytzSell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
