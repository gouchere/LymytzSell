/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.synchro.ws;

import com.lymytz.lymytzsell.dao.entity.YvsUsersAgence;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.LymytzService;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import com.lymytz.lymytzsell.service.utils.log.LogFiles;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONException;
import org.json.JSONObject;

import javax.print.attribute.standard.Severity;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.URI;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author LYMYTZ
 */
public class WsSynchro {

    private static final Logger LOGGER= LogManager.getLogger(WsSynchro.class);

    public static boolean runningOut = false; // est à true lorsque la synchronisation est en cours
    public static final AtomicBoolean runningIn = new AtomicBoolean(false); //pour controler la synchronisation entrante
    public static boolean dialogOpen = false;
    public static Long countI = -1L;
    public static Long countU = -1L;
    public static Long countD = -1L;
    public static Long countOutI = 0L;
    public static final HashSet<Long> currentListen = new HashSet<>();

    public WsSynchro() {
        //  nécessaire pour le demarrage du composant
    }

    public static URI getUriAdresse(String service) {
        UtilsProject.loadFilePropertie();
        if (UtilsProject.properties != null) {
            String adresse = UtilsProject.properties.getProperty(Constantes.KEY_WEB_HOST);
            String port = UtilsProject.properties.getProperty(Constantes.KEY_WEB_PORT);
            if ((adresse != null && !adresse.trim().isEmpty()) && (port != null && !port.trim().isEmpty())) {
                return UriBuilder.fromUri("http://" + adresse + ":" + port + "/Lymytz_Web/ws/services/" + service).build();
            }
        }
        return null;
    }

    public static boolean serverOnline() {
        try {
            Client client = ClientBuilder.newClient(new ClientConfig());
            WebTarget target = client.target(getUriAdresse("serverOnline"));
            Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
            Response rep = invocation.get();
            dialogOpen = false;
            return (rep.getStatus() == 200);
        } catch (Exception ex) {
            if (!dialogOpen && (ex.getCause() != null && (ex.getCause().getClass().equals(ConnectException.class)))) {
                        Platform.runLater(() -> {
                            LymytzService.openAlertDialog("Impossible de trouver les services distants! Verifiez votre connexion au serveur de "
                                    + "replication; si votre connexion est correcte, contactez votre Administrateur", "Connexion non trouvé !", "Connexion aux service distants impossible", Alert.AlertType.ERROR);
                            dialogOpen = true;
                        });


            }
            LOGGER.error(ex);
            return false;
        }
    }

    public static Long synchronizeAuthor(YvsUsersAgence entity, String uri) {
        try {
            Client client = ClientBuilder.newClient(new ClientConfig());
            WebTarget target = client.target(getUriAdresse("donnees_base/v1/" + uri));
            Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
            invocation.header("agence", entity.getAgence().getId());
            invocation.header("user", entity.getUsers().getId());
            Response rep = invocation.get();
            Long r = rep.readEntity(Long.class);
            if (r != null && r > 0) {
                UtilsProject.remoteAuthor = r;
            }
            return r;
        } catch (JSONException ex) {
            LOGGER.error(ex);
        }
        return null;
    }

    public <T extends Serializable> ResultatAction<T> synchronizeDataCom(JSONObject entity, String uri) {
        try {
            Client client = ClientBuilder.newClient(new ClientConfig());
            WebTarget target = client.target(getUriAdresse("commercial/v1/" + uri));
            Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
            ResultatAction<T> resultatAction;
            try (Response rep = invocation.post(Entity.json(entity.toString()))) {
                resultatAction = rep.readEntity(ResultatAction.class);
            }
            return resultatAction;
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
        return null;
    }

    public <T extends Serializable> ResultatAction<T> synchronizeDataCompta(JSONObject entity, String uri) {
        try {
            Client client = ClientBuilder.newClient(new ClientConfig());
            WebTarget target = client.target(getUriAdresse("compta/v1/" + uri));
            Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
            Response rep = invocation.post(Entity.json(entity.toString()));
            return rep.readEntity(ResultatAction.class);
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
        return null;
    }

    public Boolean pingElementOnserver(Long idDistant, String table) {
        try {
            Client client = ClientBuilder.newClient(new ClientConfig());
            WebTarget target = client.target(getUriAdresse("entityExist"));
            Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
            invocation.header("id", idDistant);
            invocation.header("table", table);
            Response rep = invocation.get();
            return rep.readEntity(Boolean.class);
        } catch (Exception ex) {
            LOGGER.error(ex);
            return false;
        }
    }

    /**
     * Service cmptabilité
     *
     *
     * @param idDocVente
     * @param auteur
     * @return
     */
    public ResultatAction comptabiliseVente(long idDocVente, long auteur) {
        try {
            Client client = ClientBuilder.newClient(new ClientConfig());
            WebTarget target = client.target(WsSynchro.getUriAdresse("compta/comptabiliseVente"));
            Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
            invocation.header("doc", idDocVente);
            invocation.header("idUser", auteur);
            Response rep = invocation.post(Entity.text("{doc:"+idDocVente+", idUser:"+auteur+"}"));
            return rep.readEntity(ResultatAction.class);
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
        return null;
    }

    public ResultatAction comptabiliseVente(long idDocVente, long auteur, String numero) {
        if (serverOnline()) {
            return comptabiliseVente(idDocVente, auteur);
        } else {
            LogFiles.addLogInFile(numero + ", non comptabilisé: Le service de comptabilisation est introuvable", Severity.ERROR);
        }
        return null;
    }

    /**
     * Service stocks
     *
     *
     * @param article
     * @param cond
     * @param depot
     * @param date
     * @return
     */
    
    public static Double getStock(long article, long cond, long depot, String date) {
        try {
            Client client = ClientBuilder.newClient(new ClientConfig());
            WebTarget target = client.target(WsSynchro.getUriAdresse("commercial/get_stock"));
            Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
            invocation.header("societe", UtilsProject.RcurrentSociete.getId());
            invocation.header("agence", UtilsProject.RcurrentAgence.getId());
            invocation.header("depot", depot);
            invocation.header("tranche", 0L);
            invocation.header("article", article);
            invocation.header("unite", cond);
            invocation.header("lot", 0);
            invocation.header("date", date);
            Response rep = invocation.get();
            return rep.readEntity(Double.class);
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
        return null;
    }
    
    public static Double getPr(long article, long cond, long depot, String date) {
        try {
            Client client = ClientBuilder.newClient(new ClientConfig());
            WebTarget target = client.target(WsSynchro.getUriAdresse("commercial/getPr"));
            Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
            invocation.header("depot_", depot);
            invocation.header("article_", article);
            invocation.header("unite_", cond);
            Response rep = invocation.get();
            return rep.readEntity(Double.class);
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
        return null;
    }

    public <T extends Serializable>ResultatAction<T> livraisonDocVente(JSONObject entity, String uri) {
        try {
            Client client = ClientBuilder.newClient(new ClientConfig());
            WebTarget target = client.target(getUriAdresse("commercial/v1/" + uri));
            Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
            try (Response rep = invocation.post(Entity.json(entity.toString()))) {
                return rep.readEntity(ResultatAction.class);
            }
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
        return null;
    }

    public <T extends Serializable> ResultatAction<T> saveVirement(JSONObject entity) {
        try {
            if (serverOnline()) {
                Client clt = ClientBuilder.newClient(new ClientConfig());
                WebTarget target = clt.target(getUriAdresse("compta/v1/save_virement_caisse"));
                Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON);
                ResultatAction<T> resultatAction;
                try (Response response = builder.post(Entity.json(entity.toString()))) {
                    resultatAction = response.readEntity(ResultatAction.class);
                }
                return resultatAction;
            }
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
        return null;
    }
}
