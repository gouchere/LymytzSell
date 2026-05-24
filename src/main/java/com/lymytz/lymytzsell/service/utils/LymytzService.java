/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.utils;

import com.lymytz.lymytzsell.business.helpers.Helpers;
import com.lymytz.lymytzsell.service.application.config.PropertiesManager;
import com.lymytz.lymytzsell.service.start.StartController;
import com.lymytz.lymytzsell.view.LocalLoader;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import java.nio.file.Files;
import java.util.Date;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;

import static com.lymytz.lymytzsell.service.utils.Constantes.PROPERTIE_FILE_NAME;
import static com.lymytz.lymytzsell.service.utils.MessagesConstants.FICHIER_PROPERTIE_MAL_CONFIGURE;
import static com.lymytz.lymytzsell.service.utils.MessagesConstants.IMPOSSIBE_DE_DEMARRER_L_APPLICATION;
import static com.lymytz.lymytzsell.service.utils.MessagesConstants.VOUS_DEVEZ_INITIALISER_LA_PROPRIETE;

/**
 * @author LENOVO
 */
public class LymytzService {

    public static boolean stopThread = false;
    private static final Logger LOGGER = LogManager.getLogger(LymytzService.class);

    private LymytzService() {
    }

    public static void alertConnexion() {
        Alert dlg = new Alert(Alert.AlertType.ERROR);
        dlg.setTitle("Erreur !!!!! ");
        dlg.setHeaderText("Erreur base de données non accessible !");
        dlg.setContentText("Base de données innaccessible!");
        Stage stage = (Stage) dlg.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(LymytzService.class.getResourceAsStream("/icones/memo_apps.png"))));
        dlg.showAndWait();
    }

    public static void openAlertDialog(String message, String title, String headersg, Alert.AlertType type) {
        Alert dlg = new Alert(type);
        dlg.setTitle(title);
        dlg.setHeaderText(headersg);
        dlg.setContentText(message);
        Stage stage = (Stage) dlg.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(LymytzService.class.getResourceAsStream("/icones/memo_apps.png"))));
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
        return re.orElse(null);
    }

    public static void success() {
        Alert dlg = new Alert(Alert.AlertType.INFORMATION);
        dlg.setTitle("Success !");
        dlg.setHeaderText("Success !");
        dlg.setContentText("");
        dlg.getDialogPane().getScene().getWindow();
        dlg.showAndWait();
    }

    public static void openExceptionDialog(String title, Alert.AlertType type, Exception ex) {
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
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException |
                         InvocationTargetException ex) {
                    LOGGER.error(ex);
                }
                stage.close();
            });
            if (home != null) {
                home.setStageCreateFacture(stage);
            }
            return load.getController();

        } catch (IOException ex) {
            LOGGER.error(ex);
        }
        return null;
    }

    public static <T> CustomWindow<T> openWindowNew(String page, String titlePage, Parent layout, Double width, Double height, boolean resize) {
        try {
            CustomWindow<T> customWindow = new CustomWindow<>();
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource(page));
            layout = load.load();
            Scene scene = new Scene(layout, width, height);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(titlePage);
            stage.centerOnScreen();
            stage.setIconified(false);
            stage.initOwner(UtilsProject.primaryStage);
            stage.setResizable(resize);
            stage.show();
            customWindow.setController(load.getController());
            customWindow.setStage(stage);
            scene.setOnKeyReleased(event -> {
                if (event.getCode().equals(KeyCode.ESCAPE)) {
                    stage.close();
                }
            });
            stage.setOnCloseRequest((WindowEvent event) -> {
                try {
                    Method m = customWindow.getController().getClass().getMethod("freeMemoryController");
                    m.invoke(customWindow.getController());
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException |
                         InvocationTargetException ex) {
                    LOGGER.error(ex);
                }
                stage.close();
            });
            return customWindow;

        } catch (IOException ex) {
            LOGGER.error(ex);
        }
        return null;
    }

    public static <T> T openWindow(String page, String titlePage, Parent layout, Double width, Double height) {
        return openWindow(page, titlePage, layout, width, height, true);
    }

    public static <T> T openWindow(String page, String titlePage, Parent layout, Double width, Double height, boolean resize) {
        return openWindow(page, titlePage, layout, width, height, resize, null);
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
                LOGGER.error(ex);
                LymytzService.openExceptionDialog("Erreur fichier ", Alert.AlertType.ERROR, ex);
            }
        }
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            LOGGER.error(ex);
            LymytzService.openExceptionDialog("Erreur file 2", Alert.AlertType.ERROR, ex);
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
                LOGGER.error(ex);
            }
        }
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            LOGGER.error(ex);
        }
        return null;
    }

    public static String getMacAdress() {
        try {
            NetworkInterface network = null;
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    network = networkInterface;
                    break;
                }
            }
            if (network != null) {
                byte[] mac = network.getHardwareAddress();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                return sb.toString();
            }
        } catch (SocketException e) {
            LOGGER.error(e);
        }
        return null;
    }

    public static String getHostName() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            return ip.getHostName();
        } catch (UnknownHostException e) {
            LOGGER.error(e);
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
            val = PropertiesManager.getInstance().getVal(Constantes.KEY_APPS_PORT);
            if (!Constantes.asString(val)) {
                openAlertDialog(IMPOSSIBE_DE_DEMARRER_L_APPLICATION, FICHIER_PROPERTIE_MAL_CONFIGURE, VOUS_DEVEZ_INITIALISER_LA_PROPRIETE + Constantes.KEY_APPS_PORT, Alert.AlertType.ERROR);
                return false;
            }
            val = PropertiesManager.getInstance().getVal(Constantes.KEY_ENVIRONNEMENT);
            if (!Constantes.asString(val)) {
                openAlertDialog(IMPOSSIBE_DE_DEMARRER_L_APPLICATION, FICHIER_PROPERTIE_MAL_CONFIGURE, VOUS_DEVEZ_INITIALISER_LA_PROPRIETE + Constantes.KEY_ENVIRONNEMENT, Alert.AlertType.ERROR);
                return false;
            }
            val = PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_AGENCE);
            if (!Constantes.asString(val)) {
                openAlertDialog(IMPOSSIBE_DE_DEMARRER_L_APPLICATION, FICHIER_PROPERTIE_MAL_CONFIGURE,
                        VOUS_DEVEZ_INITIALISER_LA_PROPRIETE + Constantes.KEY_LOCAL_AGENCE, Alert.AlertType.ERROR);
                return false;
            }
            val = PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_SOCIETE);
            if (!Constantes.asString(val)) {
                openAlertDialog(IMPOSSIBE_DE_DEMARRER_L_APPLICATION, FICHIER_PROPERTIE_MAL_CONFIGURE,
                        VOUS_DEVEZ_INITIALISER_LA_PROPRIETE + Constantes.KEY_LOCAL_SOCIETE, Alert.AlertType.ERROR);
                return false;
            }
        } else {
            openAlertDialog(IMPOSSIBE_DE_DEMARRER_L_APPLICATION, "Le fichier application.properties n'a pas été trouvé", "Vous devez initialiser le fichier de propriété", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    public static void openApps(Stage primary) {
        UtilsProject.primaryStage = primary;
        //Test l'existance des info du fichier de paramétrage
        try {
            FXMLLoader load = new FXMLLoader(LymytzService.class.getResource("/pages/start/form_start.fxml"));
            Pane root = load.load();
            Screen.getPrimary();
            Scene scene = new Scene(root, 500, 280);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initOwner(primary);
            stage.centerOnScreen();
            stage.initStyle(StageStyle.UNDECORATED);
            StartController controller = load.getController();
            stage.show();
            UtilsProject.stageConnect = stage;
            if (!UtilsProject.ENVIRONNEMENT.equals("PRODUCTION")) {
                stage.close();
                controller.openAppsForDev("ADMIN_CAISSE", "ADMIN");
            }

        } catch (IOException ex) {
            LOGGER.error(ex);
        }
    }
}
