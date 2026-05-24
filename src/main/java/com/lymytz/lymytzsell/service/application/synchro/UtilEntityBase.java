/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application.synchro;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lymytz.lymytzsell.dao.query.LocalQueryFactories;
import javafx.application.Platform;
import com.lymytz.lymytzsell.dao.Options;
import com.lymytz.lymytzsell.dao.entity.YvsAgences;
import com.lymytz.lymytzsell.dao.entity.YvsUsers;
import com.lymytz.lymytzsell.dao.entity.YvsUsersAgence;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.DoubleAdapter;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import com.lymytz.lymytzsell.synchro.ws.WsSynchro;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.bind.annotation.XmlTransient;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.lymytz.lymytzsell.synchro.ws.WsSynchro.serverOnline;

/**
 *
 * @author LYMYTZ
 */
public class UtilEntityBase {

    private static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(UtilEntityBase.class);

    public static Long findIdRemoteData(String table, Long localId) {
        Long id = -1L;
        if (Constantes.asLong(localId)) {
            LocalQueryFactories dao = new LocalQueryFactories();
            String query = "SELECT id_distant FROM yvs_synchro_listen_table l "
                    + "INNER JOIN  yvs_synchro_data_synchro ds ON ds.id_listen=l.id "
                    + "WHERE l.name_table=? AND l.id_source=? ";
            id = (Long) dao.findOneObjectBySQLQ(query, new Options[]{new Options(table, 1), new Options(localId, 2)});
            if(!Constantes.asLong(id) && table.equals(Constantes.TABLE_USER_AGENCE_CODE)){
                id=UtilsProject.remoteAuthor;
            }
        }
        return id;
    }

    public static Long findIdLocalFromIdListen(String table, Long localId) {
        LocalQueryFactories dao = new LocalQueryFactories();
        String query = "SELECT id_source FROM yvs_synchro_listen_table l INNER JOIN yvs_synchro_data_synchro s ON s.id_listen=l.id "
                + "WHERE l.name_table=? AND s.id_distant=? ";
        return (Long) dao.findOneObjectBySQLQ(query, new Options[]{new Options(table, 1), new Options(localId, 2)});
    }

    public static Long findIdListen(String table, Long localId, String action) {
        LocalQueryFactories dao = new LocalQueryFactories();
        String query = "SELECT id FROM yvs_synchro_listen_table l "
                + "WHERE l.name_table=? AND l.id_source=? AND l.action_name=? ";
        return (Long) dao.findOneObjectBySQLQ(query, new Options[]{new Options(table, 1), new Options(localId, 2), new Options(action, 3)});
    }

    public static Long findIdListen(String table, Long localId) {
        return findIdListen(table, localId, Constantes.INSERT_ACTION);
    }

    public static Date parseDate(Date date) {
        String d = Constantes.dfD.format(date);
        try {
            return Constantes.dfD.parse(d);
        } catch (ParseException ex) {
            LOGGER.error(d, ex);
            
        }
        return null;
    }

    public static Gson createGson() {
        try {
            Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    if (f.getName().equals("serialVersionUID")) {
                        return true;
                    }
                    return f.getAnnotation(XmlTransient.class) != null;
                }

                @Override
                public boolean shouldSkipClass(Class<?> type) {
                    return false;
                }
            }).setDateFormat("dd-MM-yyyy HH:mm:ss")
              .registerTypeAdapter(Double.class, new DoubleAdapter()).create();
            return gson;
        } catch (Exception ex) {
            LOGGER.error("", ex);
            
        }
        return null;
    }

    public static JSONObject factoryJsonObject(Object entity, Type type) {
        try {
            Gson gson = createGson();
            String jsonStr = gson.toJson(entity, type);
            JSONObject json = new JSONObject(jsonStr);
            return json;
        } catch (JSONException ex) {
            LOGGER.error("", ex);
            
        }
        return null;
    }

    public static Long synchronizeAuthor(YvsUsersAgence ua) {
        WsSynchro ws = new WsSynchro();
        if (serverOnline()) {
            ua.setId(-1L);
            ua.setDateSave(new Date());
            ua.setDateUpdate(new Date());
            ua.setAgence(new YvsAgences(UtilEntityBase.findIdRemoteData(Constantes.TABLE_AGENCE_CODE, ua.getAgence().getId())));
            ua.setUsers(new YvsUsers(UtilEntityBase.findIdRemoteData(Constantes.TABLE_USERS_CODE, ua.getUsers().getId())));
            return WsSynchro.synchronizeAuthor(ua, "save_author");
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    UtilsProject.currentPage.LAB_SYNC_MSG.setText("Le service n'est pas en ligne !");

                }
            });
        }
        return null;
    }
}
