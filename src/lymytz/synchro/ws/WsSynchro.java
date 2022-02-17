/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.synchro.ws;

import java.io.Serializable;
import java.net.ConnectException;
import java.net.URI;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javax.print.attribute.standard.Severity;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import lymytz.dao.entity.YvsUsersAgence;
import lymytz.service.utils.ConsUtil;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;
import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author LYMYTZ
 * @param <T> : Entity persistant
 */
public class WsSynchro<T extends Serializable> {

    public static boolean runningOut = false; // est à true lorsque la synchronisation est en cours
//    public static boolean running = false;
    public static boolean runningIn = false; //pour controler la synchronisation entrante
    public static boolean dialogOpen = false;
    public static Long countI = -1L, countU = -1L, countD = -1L;
    public static Long countOutI = 0L, countOutU = 0L, countOutD = 0L;
    public static HashSet<Long> currentListen = new HashSet<>();

    public WsSynchro() {
    }

    public static URI getUriAdresse(String service) {
        UtilsProject.loadFilePropertie();
        if (UtilsProject.properties != null) {
            String adresse = UtilsProject.properties.getProperty(Constantes.KEY_WEB_HOST);
            String port = UtilsProject.properties.getProperty(Constantes.KEY_WEB_PORT);
            if ((adresse != null ? !adresse.trim().isEmpty() : false) && (port != null ? !port.trim().isEmpty() : false)) {
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
            if (!dialogOpen) {
                if (ex.getCause() != null) {
                    if (ex.getCause().getClass().equals(ConnectException.class)) {
                        Platform.runLater(() -> {
                            LymytzService.openAlertDialog("Impossible de trouver les services distants! Verifiez votre connexion au serveur de "
                                    + "replication; si votre connexion est correcte, contactez votre Administrateur", "Connexion non trouvé !", "Connexion aux service distants impossible", Alert.AlertType.ERROR);
                            dialogOpen = true;
                        });
                    }
                }
            }
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
            if (r != null ? r > 0 : false) {
                UtilsProject.remoteAuthor = r;
            }
            return r;
        } catch (JSONException ex) {
            LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
            Logger.getLogger(WsSynchro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ResultatAction<T> synchronizeDataCom(JSONObject entity, String uri) {
        try {
            Client client = ClientBuilder.newClient(new ClientConfig());
            WebTarget target = client.target(getUriAdresse("commercial/v1/" + uri));
            Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
            Response rep = invocation.post(Entity.json(entity.toString()));
            ResultatAction<T> r = rep.readEntity(ResultatAction.class);
            return r;
        } catch (JSONException ex) {
            LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
            Logger.getLogger(WsSynchro.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
            Logger.getLogger(WsSynchro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ResultatAction<T> synchronizeDataCompta(JSONObject entity, String uri) {
        try {
            Client client = ClientBuilder.newClient(new ClientConfig());
            WebTarget target = client.target(getUriAdresse("compta/v1/" + uri));
            Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
            Response rep = invocation.post(Entity.json(entity.toString()));
            System.err.println("Réponse " + rep.getStatus());
            ResultatAction<T> r = rep.readEntity(ResultatAction.class);
            return r;
//            }
        } catch (Exception ex) {
            LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
            Logger.getLogger(WsSynchro.class.getName()).log(Level.SEVERE, null, ex);
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
            Boolean re = rep.readEntity(Boolean.class);
            return re;
        } catch (Exception ex) {
            LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
            Logger.getLogger(WsSynchro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
            ResultatAction r = rep.readEntity(ResultatAction.class);
            return r;
        } catch (Exception ex) {
            LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
            Logger.getLogger(WsSynchro.class.getName()).log(Level.SEVERE, null, ex);
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
            Double r = rep.readEntity(Double.class);
            return r;
        } catch (Exception ex) {
            LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
            Logger.getLogger(WsSynchro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ResultatAction livraisonDocVente(JSONObject entity, String uri) {
        try {
            Client client = ClientBuilder.newClient(new ClientConfig());
            WebTarget target = client.target(getUriAdresse("commercial/v1/" + uri));
            Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
            Response rep = invocation.post(Entity.json(entity.toString()));
            ResultatAction<T> r = rep.readEntity(ResultatAction.class);
            
            return r;
        } catch (Exception ex) {
            LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
            Logger.getLogger(WsSynchro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ResultatAction saveVirement(JSONObject entity) {
        try {
            if (serverOnline()) {
                Client clt = ClientBuilder.newClient(new ClientConfig());
                WebTarget target = clt.target(getUriAdresse("compta/v1/save_virement_caisse"));
                Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON);
                Response response = builder.post(Entity.json(entity.toString()));
                ResultatAction<T> r = response.readEntity(ResultatAction.class);
                return r;
            }
        } catch (JSONException ex) {
            LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
            Logger.getLogger(WsSynchro.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
            Logger.getLogger(WsSynchro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
