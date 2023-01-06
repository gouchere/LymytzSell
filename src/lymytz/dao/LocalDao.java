/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.EncryptMessage;
import lymytz.service.utils.UtilsProject;

/**
 *
 * @author LENOVO
 */
public class LocalDao {

    private static LocalDao instance = null;
    private static EntityManagerFactory emf;
    public static boolean connect = false;

    private LocalDao() throws Exception {
        //récupère les paramètres de connexion
        UtilsProject.loadFilePropertie();
        if (UtilsProject.properties != null) {
            String host = UtilsProject.properties.getProperty(Constantes.KEY_LOCAL_HOST);
            String port = UtilsProject.properties.getProperty(Constantes.KEY_LOCAL_PORT);
            String dbName = UtilsProject.properties.getProperty(Constantes.KEY_LOCAL_DB_NAME);
            String user = EncryptMessage.decrypt(UtilsProject.properties.getProperty(Constantes.KEY_LOCAL_USERS), Constantes.KEY_ENCRYPT);
            String password = EncryptMessage.decrypt(UtilsProject.properties.getProperty(Constantes.KEY_LOCAL_PASSWORD), Constantes.KEY_ENCRYPT);
            String URL = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
            Map<String, String> properties = new HashMap<>();
            properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
            properties.put("javax.persistence.jdbc.url", URL);
            properties.put("javax.persistence.jdbc.user", user);
            properties.put("javax.persistence.jdbc.password", password);
            this.emf = Persistence.createEntityManagerFactory("LYMYTZ_PU", properties);
        } else {
            throw new Exception("Paramètres de connexion non trouvé !");
        }
    }

    public static LocalDao getInstance() {
        return getInstance(true);
    }

    public static void setInstance(LocalDao instance) {
        LocalDao.instance = instance;
    }

    public static LocalDao getInstance(boolean recreate) {
//        if (recreate) {
//            instance = null;
//        }
        if (instance == null) {
            synchronized (LocalDao.class) {
                if (instance == null) {
                    try {
                        instance = new LocalDao();
                        connect = true;
                    } catch (Exception ex) {
                        Logger.getLogger(LocalDao.class.getName()).log(Level.SEVERE, null, ex);
                        connect = false;
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
