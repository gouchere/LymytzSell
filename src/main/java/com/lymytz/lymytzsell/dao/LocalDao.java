/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.dao;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.lymytz.lymytzsell.service.application.config.PropertiesManager;
import com.lymytz.lymytzsell.service.application.exception.FonctionalException;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.EncryptMessage;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import lombok.Getter;

/**
 * @author LENOVO
 */
public class LocalDao {

    private static volatile LocalDao instance = null;
    @Getter
    private static EntityManagerFactory emf;
    private static final Object lock = new Object();

    private LocalDao() throws FonctionalException {
        //récupère les paramètres de connexion
        UtilsProject.loadFilePropertie();
        if (UtilsProject.properties != null) {
            String host = PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_HOST);
            String port = PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_PORT);
            String dbName = PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_DB_NAME);
            String user = EncryptMessage.decrypt(PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_USERS), Constantes.KEY_ENCRYPT);
            String password = EncryptMessage.decrypt(PropertiesManager.getInstance().getVal(Constantes.KEY_LOCAL_PASSWORD), Constantes.KEY_ENCRYPT);
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
            Map<String, String> properties = new HashMap<>();
            properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
            properties.put("javax.persistence.jdbc.url", url);
            properties.put("javax.persistence.jdbc.user", user);
            properties.put("javax.persistence.jdbc.password", password);
            LocalDao.emf = Persistence.createEntityManagerFactory("LYMYTZ_PU", properties);
        } else {
            throw new FonctionalException("Paramètres de connexion non trouvé !");
        }
    }

    public static void setInstance(LocalDao instance) {
        LocalDao.instance = instance;
    }

    public static LocalDao getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    try {
                        instance = new LocalDao();
                    } catch (Exception ex) {
                        Logger.getLogger(LocalDao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return instance;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

}
