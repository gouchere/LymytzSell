package com.lymytz.lymytzsell.business.helpers;

import javafx.scene.control.Alert;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.lymytz.lymytzsell.service.utils.MessagesConstants.FICHIER_PROPERTIE_MAL_CONFIGURE;
import static com.lymytz.lymytzsell.service.utils.MessagesConstants.IMPOSSIBE_DE_DEMARRER_L_APPLICATION;
import static com.lymytz.lymytzsell.service.utils.LymytzService.openAlertDialog;
import static java.lang.Thread.sleep;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Helpers {
    private static final Logger LOGGER = Logger.getLogger(Helpers.class.getName());

    public static void sleepFor(int timeMillis) {
        try {
            sleep(timeMillis);
        } catch (InterruptedException ex) {
            Logger.getLogger(Helpers.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
    }

    public static File getPropertiesFile(String fileName) {
        var path = Paths.get(System.getProperty("user.home"), "lymytz-sell", "conf");
        if (Files.isWritable(Paths.get(System.getProperty("user.home")))) {
            try {
                path = Files.createDirectories(path);
                var configFile = path.resolve(fileName);
                if (Files.notExists(configFile)) {
                    Files.createFile(configFile);
                }
                return configFile.toFile();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Création de la ressource impossible ", e);
            }
        } else {
            LOGGER.log(Level.SEVERE, String.format("L'application ne dispose pas de privillèges pours écrire dans le dossier %s", System.getProperty("user.home")));
            openAlertDialog(IMPOSSIBE_DE_DEMARRER_L_APPLICATION, FICHIER_PROPERTIE_MAL_CONFIGURE + "\n\nAstuce: Lancer l'application avec les privillèges plus élevés, ou assurez vous de donner les droits en écriture au repertoire " + System.getProperty("user.home")
                    , "Impossible d'écrire dans le dossier " + System.getProperty("user.home"), Alert.AlertType.ERROR);
            return null;
        }
        return null;
    }
}
