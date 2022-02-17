/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.standard.Severity;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.EncryptMessage;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;

/**
 *
 * @author LENOVO
 */
public class LocalSqlDao {

    private static LocalSqlDao instance = null;
    private static Connection con;

    private LocalSqlDao() throws Exception {
        //récupère les paramètres de connexion        
        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                LogFiles.addLogInFile("Aucun pilote de connexion à votre BD n'a été trouvé", ex);
                throw new Exception("Aucun pilote de connexion à votre BD n'a été trouvé", ex.getCause());
            }
            UtilsProject.loadFilePropertie();
            if (UtilsProject.properties != null) {

                String host = UtilsProject.properties.getProperty(Constantes.KEY_LOCAL_HOST);
                String port = UtilsProject.properties.getProperty(Constantes.KEY_LOCAL_PORT);
                String dbName = UtilsProject.properties.getProperty(Constantes.KEY_LOCAL_DB_NAME);
                String user = EncryptMessage.decrypt(UtilsProject.properties.getProperty(Constantes.KEY_LOCAL_USERS), Constantes.KEY_ENCRYPT);
                String password = EncryptMessage.decrypt(UtilsProject.properties.getProperty(Constantes.KEY_LOCAL_PASSWORD), Constantes.KEY_ENCRYPT);
                String URL = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
                try {
                    LocalSqlDao.con = DriverManager.getConnection(URL, user, password);
                } catch (SQLException ex) {
                    LogFiles.addLogInFile("Connexion au serveur de données echoué", ex);
                    throw new Exception("Connexion au serveur de données echoué", ex);
                }
            } else {
                LogFiles.addLogInFile("Aucun paramètres de connexion à la BD trouvé !", Severity.REPORT);
            }
        } catch (Exception ex) {
            LogFiles.addLogInFile("Erreur lors de la lecture des paramètres de connexion !", ex);
            throw new Exception("Erreur lors de la lecture des paramètres de connexion !", ex);
        }
    }

    public static LocalSqlDao getInstance() {
        return getInstance(false);
    }

    public static void setInstance(LocalSqlDao instance) {
        LocalSqlDao.instance = instance;
    }

    public static LocalSqlDao getInstance(boolean recreate) {
        if (recreate) {
            instance = null;
        }
        if (instance == null) {
            synchronized (LocalSqlDao.class) {
                if (instance == null) {
                    try {
                        instance = new LocalSqlDao();
                    } catch (Exception ex) {
                        Logger.getLogger(LocalSqlDao.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return con;
    }

}
