/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.lymytz.lymytzsell.service.application.config.PropertiesManager;
import com.lymytz.lymytzsell.service.application.exception.CloseConException;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.EncryptMessage;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author LENOVO
 */
public class RemoteDao {

    private static final Logger LOGGER = LogManager.getLogger(RemoteDao.class);
    private static RemoteDao instance = null;
    private static Connection con;

    private RemoteDao() throws Exception {
        //récupère les paramètres de connexion        
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            LOGGER.error("Aucun pilote de connexion à votre BD n'a été trouvé", ex);
            throw new Exception("Aucun pilote de connexion à votre BD n'a été trouvé", ex.getCause());
        }
        UtilsProject.loadFilePropertie();
        if (UtilsProject.properties != null) {
            String host = PropertiesManager.getInstance().getVal(Constantes.KEY_REMOTE_HOST);
            String port = PropertiesManager.getInstance().getVal(Constantes.KEY_REMOTE_PORT);
            String dbName = PropertiesManager.getInstance().getVal(Constantes.KEY_REMOTE_DB_NAME);
            String user = EncryptMessage.decrypt(PropertiesManager.getInstance().getVal(Constantes.KEY_REMOTE_USERS), Constantes.KEY_ENCRYPT);
            String password = EncryptMessage.decrypt(PropertiesManager.getInstance().getVal(Constantes.KEY_REMOTE_PASSWORD), Constantes.KEY_ENCRYPT);
            String URL = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
            try {
                Properties p = new Properties();
                RemoteDao.con = DriverManager.getConnection(URL, user, password);
            } catch (SQLException ex) {
                LOGGER.error("Connexion au serveur de données echoué", ex);
                throw new Exception("Connexion au serveur de données echoué");
            }
        } else {
            LOGGER.info("Aucun paramètres de connexion à la BD trouvé !");
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
                LOGGER.error("Erreur lors de la fermeture de la connexion", ex);
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
                        LOGGER.error(ex.getMessage(), ex);
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
            LOGGER.error("Erreur lors de l'obtention de la connexion", ex);
        }
        return null;
    }

}
