package com.lymytz.lymytzsell.service.application.config;

import com.lymytz.lymytzsell.business.helpers.Helpers;
import com.lymytz.lymytzsell.service.application.PreferenceController;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.EncryptMessage;
import com.lymytz.lymytzsell.service.utils.LymytzService;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import javafx.scene.control.Alert;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Singleton responsable du chargement du fichier application.properties
 * et de la construction du {@link Properties} associé.
 * <p>
 * Utilise le pattern "initialization-on-demand holder" : lazy, thread-safe, sans synchronized.
 */
public class PropertiesManager {

    private final Logger LOGGER = LogManager.getLogger(PropertiesManager.class);

    private java.util.Properties propertiesConfig;
    @Getter
    private Properties properties;

    // Constructeur privé — chargement au moment de la première instanciation (lazy via Holder)
    private PropertiesManager() {
        doLoad();
    }

    // Holder : instancié au premier appel de getInstance(), thread-safe par la JVM
    private static final class Holder {
        private static final PropertiesManager INSTANCE = new PropertiesManager();
    }

    public static PropertiesManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Force le rechargement depuis le disque.
     * À appeler uniquement lors d'un rechargement explicite par l'utilisateur.
     */
    public void reload() {
        doLoad();
    }

    private void doLoad() {
        try {
            if (propertiesConfig == null) {
                LOGGER.info("Initialisation des propriétés de l'application");
                propertiesConfig = new java.util.Properties();
            }
            try (FileInputStream fis = propertiesAsStream()) {
                propertiesConfig.load(fis);
                LOGGER.info("Propriétés chargées avec succès");
                buildParamConnection();
            }
        } catch (IOException ex) {
            LOGGER.error("Fichier d'environnement non trouvé !", ex);
        }
    }

    private void buildParamConnection() {
        if (properties == null) {
            properties = new Properties();
        }
        properties.setCheminPhotos(getVal(Constantes.KEY_PATH));

        String s = getVal(Constantes.KEY_CLIENT_DIVERS);
        properties.setClientDivers(Constantes.asString(s) ? Long.parseLong(s) : 0L);
        properties.setCodeAgence(Long.parseLong(getVal(Constantes.KEY_LOCAL_AGENCE)));
        properties.setCodeSociete(Long.parseLong(getVal(Constantes.KEY_LOCAL_SOCIETE)));
        properties.setDataBase(getVal(Constantes.KEY_LOCAL_DB_NAME));
        properties.setDataBaseRemote(getVal(Constantes.KEY_REMOTE_DB_NAME));
        properties.setHostWeb(getVal(Constantes.KEY_WEB_HOST));
        properties.setIdRemoteScte(Long.parseLong(getVal(Constantes.KEY_REMOTE_SOCIETE)));
        properties.setModeReg(Long.parseLong(getVal(Constantes.KEY_MODE_REGLEMENT)));
        properties.setModelReg(Long.parseLong(getVal(Constantes.KEY_MODEL_REGLEMENT)));
        properties.setP_default(!Constantes.asString(getVal(Constantes.KEY_USE_PRINTER)) || Boolean.parseBoolean(getVal(Constantes.KEY_USE_PRINTER)));
        properties.setP_height(parseDouble(getVal(Constantes.KEY_PAPER_HEIGHT)));
        properties.setP_width(parseDouble(getVal(Constantes.KEY_PAPER_WIDTH)));
        properties.setP_mb(parseDouble(getVal(Constantes.KEY_PAPER_M_BOTOM)));
        properties.setP_ml(parseDouble(getVal(Constantes.KEY_PAPER_M_LEFT)));
        properties.setP_mr(parseDouble(getVal(Constantes.KEY_PAPER_M_RIGHT)));
        properties.setP_mt(parseDouble(getVal(Constantes.KEY_PAPER_M_TOP)));
        properties.setPassword(getVal(Constantes.KEY_LOCAL_PASSWORD));
        properties.setPasswordRemote(getVal(Constantes.KEY_REMOTE_PASSWORD));
        properties.setPort(getVal(Constantes.KEY_LOCAL_PORT));
        properties.setPortRemote(getVal(Constantes.KEY_REMOTE_PORT));
        properties.setPortWeb(getVal(Constantes.KEY_WEB_PORT));
        properties.setSever(getVal(Constantes.KEY_LOCAL_HOST));
        properties.setSeverRemote(getVal(Constantes.KEY_REMOTE_HOST));
        properties.setTypeRapport(getVal(Constantes.KEY_TYPE_PRINT));
        properties.setUseCodeBarre(!Constantes.asString(getVal(Constantes.KEY_USE_CODE_BARRE)) || Boolean.parseBoolean(getVal(Constantes.KEY_USE_CODE_BARRE)));
        properties.setUsePrinter(!Constantes.asString(getVal(Constantes.KEY_USE_PRINTER)) || Boolean.parseBoolean(getVal(Constantes.KEY_USE_PRINTER)));
        properties.setUsers(getVal(Constantes.KEY_LOCAL_USERS));
        properties.setUsersRemote(getVal(Constantes.KEY_REMOTE_USERS));
        String loadCatalogue = getVal(Constantes.KEY_LOAD_CATALOGUE);
        properties.setLoadCatalogue(Constantes.asString(loadCatalogue) && Boolean.parseBoolean(loadCatalogue));
    }

    // --- Chargement du fichier de propriétés ---

    private FileInputStream propertiesAsStream() throws IOException {
        File file = Helpers.getPropertiesFile(Constantes.PROPERTIE_FILE_NAME);
        assert file != null;
        var lines = Files.readAllLines(file.toPath());
        if (!file.exists() || lines.isEmpty()) {
            propertiesConfig.setProperty(Constantes.KEY_APPS_PORT, "1025");
            propertiesConfig.setProperty(Constantes.KEY_CLIENT_DIVERS, "");
            propertiesConfig.setProperty(Constantes.KEY_ENVIRONNEMENT, "PRODUCTION");
            propertiesConfig.setProperty(Constantes.KEY_LOCAL_AGENCE, "");
            propertiesConfig.setProperty(Constantes.KEY_LOCAL_DB_NAME, "lymytz_sell_extension");
            propertiesConfig.setProperty(Constantes.KEY_LOCAL_HOST, "localhost");
            propertiesConfig.setProperty(Constantes.KEY_LOCAL_PASSWORD, EncryptMessage.encrypt("yves1910/", Constantes.KEY_ENCRYPT));
            propertiesConfig.setProperty(Constantes.KEY_LOCAL_PORT, "5432");
            propertiesConfig.setProperty(Constantes.KEY_LOCAL_SOCIETE, "");
            propertiesConfig.setProperty(Constantes.KEY_LOCAL_USERS, EncryptMessage.encrypt("postgres", Constantes.KEY_ENCRYPT));
            propertiesConfig.setProperty(Constantes.KEY_MODE, "BOTH");
            propertiesConfig.setProperty(Constantes.KEY_MODEL_REGLEMENT, "");
            propertiesConfig.setProperty(Constantes.KEY_MODE_REGLEMENT, "");
            propertiesConfig.setProperty(Constantes.KEY_ORIENTATION_PRINT, "");
            propertiesConfig.setProperty(Constantes.KEY_PAPER_HEIGHT, "0");
            propertiesConfig.setProperty(Constantes.KEY_PAPER_WIDTH, "0");
            propertiesConfig.setProperty(Constantes.KEY_PAPER_M_BOTOM, "0");
            propertiesConfig.setProperty(Constantes.KEY_PAPER_M_LEFT, "0");
            propertiesConfig.setProperty(Constantes.KEY_PAPER_M_RIGHT, "0");
            propertiesConfig.setProperty(Constantes.KEY_PAPER_M_TOP, "0");
            propertiesConfig.setProperty(Constantes.KEY_PATH, "");
            propertiesConfig.setProperty(Constantes.KEY_REMOTE_DB_NAME, "lymytz_demo_0");
            propertiesConfig.setProperty(Constantes.KEY_REMOTE_HOST, "");
            propertiesConfig.setProperty(Constantes.KEY_REMOTE_PASSWORD, EncryptMessage.encrypt("yves1910/", Constantes.KEY_ENCRYPT));
            propertiesConfig.setProperty(Constantes.KEY_REMOTE_PORT, "5432");
            propertiesConfig.setProperty(Constantes.KEY_REMOTE_SOCIETE, "");
            propertiesConfig.setProperty(Constantes.KEY_REMOTE_USERS, EncryptMessage.encrypt("postgres", Constantes.KEY_ENCRYPT));
            propertiesConfig.setProperty(Constantes.KEY_SECTEUR, "");
            propertiesConfig.setProperty(Constantes.KEY_TYPE_PRINT, "TICKET");
            propertiesConfig.setProperty(Constantes.KEY_USE_CODE_BARRE, "TRUE");
            propertiesConfig.setProperty(Constantes.KEY_USE_PRINTER, "TRUE");
            propertiesConfig.setProperty(Constantes.KEY_VILLE, "");
            propertiesConfig.setProperty(Constantes.KEY_WEB_HOST, "");
            propertiesConfig.setProperty(Constantes.KEY_WEB_PORT, "8080");
            propertiesConfig.setProperty(Constantes.KEY_DATE_INIT, Constantes.dfD.format(new Date()));
            try (FileOutputStream oStream = new FileOutputStream(file)) {
                propertiesConfig.store(oStream, "test");
                return new FileInputStream(file);
            } catch (IOException ex) {
                LOGGER.error("Le fichier de configuration n'a pas pu être initialisé", ex);
            }
        } else {
            try {
                LOGGER.info("Chargement des propriétés de l'application");
                return new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                LOGGER.error("Le fichier de configuration n'a pas pu être récupéré", ex);
            }
        }
        return null;
    }


    private double parseDouble(String val) {
        return Constantes.asString(val) ? Double.parseDouble(Objects.requireNonNull(val)) : 0d;
    }

    /**
     * Retourne la valeur d'une clé, ou {@code null} si absente ou vide.
     */
    public String getVal(String key) {
        if (propertiesConfig == null) {
            return null;
        }
        try {
            String value = propertiesConfig.getProperty(key);
            return Constantes.asString(value) ? value : null;
        } catch (Exception ex) {
            LOGGER.error("Récupération de la clé erronée ! {}", key, ex);
        }
        return null;
    }

    public void setProperty(String key, String value) {
        if (propertiesConfig != null) {
            propertiesConfig.setProperty(key, value);
        }
    }

    public void storeValues(File file) {
        try (FileOutputStream oStream = new FileOutputStream(file)) {
            propertiesConfig.store(oStream, "");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(PreferenceController.class.getName()).log(Level.SEVERE, null, ex);
            LymytzService.openExceptionDialog("", Alert.AlertType.ERROR, ex);
        }
    }
}

