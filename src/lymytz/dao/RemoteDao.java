/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.standard.Severity;
import lymytz.service.application.exception.CloseConException;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.EncryptMessage;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;

/**
 *
 * @author LENOVO
 */
public class RemoteDao {

    private static RemoteDao instance = null;
    private static Connection con;

    private RemoteDao() throws Exception {
        //récupère les paramètres de connexion        
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            LogFiles.addLogInFile("Aucun pilote de connexion à votre BD n'a été trouvé", ex);
            throw new Exception("Aucun pilote de connexion à votre BD n'a été trouvé", ex.getCause());
        }
        UtilsProject.loadFilePropertie();
        if (UtilsProject.properties != null) {
            String host = UtilsProject.properties.getProperty(Constantes.KEY_REMOTE_HOST);
            String port = UtilsProject.properties.getProperty(Constantes.KEY_REMOTE_PORT);
            String dbName = UtilsProject.properties.getProperty(Constantes.KEY_REMOTE_DB_NAME);
            String user = EncryptMessage.decrypt(UtilsProject.properties.getProperty(Constantes.KEY_REMOTE_USERS), Constantes.KEY_ENCRYPT);
            String password = EncryptMessage.decrypt(UtilsProject.properties.getProperty(Constantes.KEY_REMOTE_PASSWORD), Constantes.KEY_ENCRYPT);
            String URL = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
            try {
                Properties p = new Properties();
                RemoteDao.con = DriverManager.getConnection(URL, user, password);
            } catch (SQLException ex) {
                LogFiles.addLogInFile("Connexion au serveur de données echoué", ex);
                throw new Exception("Connexion au serveur de données echoué");
            }
        } else {
            LogFiles.addLogInFile("Aucun paramètres de connexion à la BD trouvé !", Severity.REPORT);
        }
    }

    public static RemoteDao getInstance() {
        return getInstance(false);
    }

    public static void setInstance(RemoteDao instance) {
        if (instance == null) {
            try {
                //détruit la connexion
                RemoteDao.instance.getConnection().close();
            } catch (CloseConException | SQLException ex) {
                Logger.getLogger(RemoteDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        RemoteDao.instance = instance;
    }

    public static RemoteDao getInstance(boolean recreate) {
        if (recreate) {
            instance = null;
        }
        if (instance == null) {
            synchronized (RemoteDao.class) {
                if (instance == null) {
                    try {
                        instance = new RemoteDao();
                    } catch (Exception ex) {
                        instance = null;
                        Logger.getLogger(RemoteDao.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws CloseConException {
        try {
            if (!con.isClosed()) {
                return con;
            } else {
                con = null;
                throw new CloseConException();
            }
        } catch (SQLException ex) {
            Logger.getLogger(RemoteDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
