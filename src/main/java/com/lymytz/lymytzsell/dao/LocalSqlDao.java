/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.lymytz.lymytzsell.service.application.config.PropertiesManager;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.EncryptMessage;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author LENOVO
 */
public class LocalSqlDao {

    private static final Logger LOGGER = LogManager.getLogger(LocalSqlDao.class);
    private static LocalSqlDao instance = null;
    private static Connection con;

    private LocalSqlDao() throws Exception {
        //récupère les paramètres de connexion        
        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                LOGGER.error("Aucun pilote de connexion à votre BD n'a été trouvé", ex);
                throw new Exception("Aucun pilote de connexion à votre BD n'a été trouvé", ex.getCause());
            }
            UtilsProject.loadFilePropertie();
            if (UtilsProject.properties != null) {

                String host = PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_HOST);
                String port = PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_PORT);
                String dbName = PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_DB_NAME);
                String user = EncryptMessage.decrypt(PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_USERS), Constantes.KEY_ENCRYPT);
                String password = EncryptMessage.decrypt(PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_PASSWORD), Constantes.KEY_ENCRYPT);
                String URL = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
                try {
                    LocalSqlDao.con = DriverManager.getConnection(URL, user, password);
                } catch (SQLException ex) {
                    LOGGER.error("Connexion au serveur de données echoué", ex);
                    throw new Exception("Connexion au serveur de données echoué", ex);
                }
            } else {
                LOGGER.info("Aucun paramètres de connexion à la BD trouvé !");
            }
        } catch (Exception ex) {
            LOGGER.error("Erreur lors de la lecture des paramètres de connexion !", ex);
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
                        LOGGER.error(ex.getMessage(), ex);
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
