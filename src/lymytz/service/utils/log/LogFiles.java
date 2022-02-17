/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.utils.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.standard.Severity;
import lymytz.service.utils.ConsUtil;
import lymytz.service.utils.Constantes;

/**
 *
 * @author LENOVO
 */
public class LogFiles {

    private static final long LIMIT = 1024;
    private static final long SIZE = 500;

    public LogFiles() {
    }

    public static boolean createLogfile() {
        File logFile = new File("log");
        File logFile_ = new File("conf");
        if (!logFile.exists()) {
            logFile.mkdirs();
        }
        if (!logFile_.exists()) {
            logFile_.mkdirs();
        }
        logFile = new File("log/" + ConsUtil.SOURCE_LOG_FILE_SYNC);
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            logFile = new File("log/" + ConsUtil.SOURCE_LOG_FILE_EXCEPTION);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            logFile = new File("log/" + ConsUtil.SOURCE_LOG_FILE_USER);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException ex) {
            Logger.getLogger(LogFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        logFile_ = new File("conf/servConfig.ltz");
        if (!logFile_.exists()) {
            try {
                logFile_.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(LogFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return logFile.exists() && logFile_.exists();
    }

    public static boolean addLogInFile(String message, Exception ex) {
        return addLogInFile(message, Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
    }

    public static boolean addLogInFile(String message, Severity severity) {
        return addLogInFile(message, severity, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, null);
    }

    public synchronized static boolean addLogInFile(String message, Severity severity, String sourceFile, Exception ex) {
        try {
            File f = new File("log/" + sourceFile);
            if ((f.length() / LIMIT) > SIZE) {
                f.renameTo(new File("log/" + sourceFile.replace(".conf", "").concat("_log_").concat(Constantes.dfh.format(new Date())).concat(".conf")));
                createLogfile();
            }
            final FileWriter fw = new FileWriter(f, true);
            try (PrintWriter pw = new PrintWriter(fw, true)) {
                pw.write(Constantes.dfh.format(new Date()) + " | " + getSeverity(severity));
                if (severity == Severity.ERROR) {
                    pw.println();
                }
                if (message != null) {
                    pw.write(message);
                pw.println();
                }
                if (ex != null) {
                    ex.printStackTrace(pw);
                }
            }
        } catch (IOException ex1) {
            Logger.getLogger(LogFiles.class.getName()).log(Level.SEVERE, null, ex1);
        } finally {

        }
        return true;
    }

    private static String getSeverity(Severity s) {
        if (s == Severity.REPORT) {
            return "[INFO]";
        } else if (s == Severity.ERROR) {
            return "[ERREUR]";
        } else {
            return "[AVERTISSEMENT]";
        }
    }
}
