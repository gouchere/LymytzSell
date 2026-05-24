/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.utils.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.print.attribute.standard.Severity;
import java.io.File;

/**
 * Facade de logging conservée pour compatibilité ascendante.
 * Délègue désormais à Log4j2 : tous les messages vont dans app.log.
 *
 * @deprecated Utiliser directement {@code LogManager.getLogger(MyClass.class)} dans chaque classe.
 */
@Deprecated
public class LogFiles {

    private static final Logger LOGGER = LogManager.getLogger(LogFiles.class);

    private LogFiles() {
    }

    /**
     * Crée le dossier conf et le fichier servConfig.ltz si nécessaire.
     * Les fichiers de log sont gérés automatiquement par Log4j2.
     */
    public static boolean createLogfile() {
        File confDir = new File("conf");
        if (!confDir.exists()) {
            confDir.mkdirs();
        }
        File servConfig = new File("conf/servConfig.ltz");
        if (!servConfig.exists()) {
            try {
                servConfig.createNewFile();
            } catch (java.io.IOException ex) {
                LOGGER.error("Impossible de créer le fichier servConfig.ltz", ex);
            }
        }
        return servConfig.exists();
    }

    public static boolean addLogInFile(String message, Exception ex) {
        LOGGER.error(message != null ? message : "", ex);
        return true;
    }

    public static boolean addLogInFile(String message, Severity severity) {
        return addLogInFile(message, severity, null, null);
    }

    public synchronized static boolean addLogInFile(String message, Severity severity, String sourceFile, Exception ex) {
        String msg = (message != null) ? message : "";
        if (severity == Severity.ERROR) {
            LOGGER.error(msg, ex);
        } else if (severity == Severity.WARNING) {
            LOGGER.warn(msg, ex);
        } else {
            LOGGER.info(msg, ex);
        }
        return true;
    }
}
