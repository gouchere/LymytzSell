/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.synchro.export;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import lymytz.dao.Options;
import lymytz.dao.entity.YvsComContenuDocVente;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.dao.entity.YvsComEnteteDocVente;
import lymytz.dao.entity.YvsComptaAcompteClient;
import lymytz.dao.entity.YvsComptaCaissePieceVente;
import lymytz.dao.entity.YvsComptaNotifReglementVente;
import lymytz.dao.entity.service.EntityColumn;
import lymytz.dao.entity.service.LymytzData;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.application.synchro.BuilderEntitySynchro;
import lymytz.service.application.synchro.UtilEntityBase;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;
import lymytz.synchro.ws.ResultatAction;
import lymytz.synchro.ws.WsSynchro;
import org.json.JSONObject;

/**
 *
 * @author Admin
 * @param <T>
 */
public class ExportService<T extends Serializable> extends Task<Boolean> {

    LQueryFactories Ldao = new LQueryFactories();
    private List<LymytzData> listData;
    ExportDataController page;
    private String source = "T";  // T= (thread: lancé par le schéduler) IHM= lancé lar l'IHM
    String table, action;
    Long idListen, idLocal;

    public ExportService() {
    }

    public ExportService(String source, List<LymytzData> data, ExportDataController page) {
        this.listData = data;
        this.page = page;
        this.source = source;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getIdListen() {
        return idListen;
    }

    public void setIdListen(Long idListen) {
        this.idListen = idListen;
    }

    public Long getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Long idLocal) {
        this.idLocal = idLocal;
    }

    public List<LymytzData> getListData() {
        return listData;
    }

    public void setListData(List<LymytzData> listData) {
        this.listData = listData;
    }

    private boolean synchroniseData(Long id, String table) {
        return false;
    }

    @Override
    protected Boolean call() throws Exception {
        if (this.source.equals("IHM")) {
            List<LymytzData> temps = listData.subList(0, 1);
            runningSynchroData(temps);
        } else {
            WsSynchro.runningOut = true;
            if (Constantes.asString(table) && Constantes.asLong(idLocal) && Constantes.asLong(idListen)) {
                synchroniseData(table, idListen, idLocal, action, true);
            }
            WsSynchro.runningOut = false; //fin de l'exécution
        }
        return true;
    }

    private boolean beforeRunningSynchro() {
        // vérifie que la valeur distante de auteur existe bien
        if (UtilsProject.remoteAuthor != null ? UtilsProject.remoteAuthor <= 0 : true) {
            Long r = UtilEntityBase.synchronizeAuthor(UtilsProject.currentUser);
            if (r != null ? r > 0 : false) {
                UtilsProject.remoteAuthor = r;
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    public List<LymytzData> runningSynchroData(List<LymytzData> listData) {
        LymytzData row;
        Long idListen = null;
        Long localKey = null;
        String action = null;
        List<LymytzData> re = new ArrayList<>();
        if (beforeRunningSynchro()) {
            for (LymytzData lineR : listData) { //parcours des lignes            
                row = new LymytzData(new EntityColumn[lineR.getValue().length]);
                int i = 0;
                EntityColumn selectCol = null;
                for (EntityColumn col : lineR.getValue()) {
                    if ("id".equals(col.getName())) {
                        localKey = (Long) col.getColumnValue();
                        selectCol = col;
                        i = i + 1;
                    }
                    if ("id_listen".equals(col.getName())) {
                        idListen = (Long) col.getColumnValue();
                        i = i + 1;
                    }
                    if ("action_name".equals(col.getName())) {
                        action = (String) col.getColumnValue();
                        i = i + 1;
                    }
                    if (i == 2) {
                        break;
                    }
                }
                if (localKey != null ? localKey > 0 : false) {
                    synchroniseData(selectCol.getTableName(), idListen, localKey, action, true);
                }
                re.add(row);
            }
        }
        System.err.println("End synchro...");
        return re;
    }

    public T synchroniseData(String table, Long idListen, Long localKey, String action, boolean withChild) {
        switch (table) {
            case Constantes.TABLE_HEADER_DOC_CODE: {
                //Construction de l'objet à partir de la base de données locale
                YvsComEnteteDocVente en = BuilderEntitySynchro.builderHeader(localKey, idListen);
                WsSynchro ws = new WsSynchro();
                JSONObject entityJson = UtilExport.exportEnteteDoc(en,idListen);
                ResultatAction<YvsComEnteteDocVente> result = null;
                if (entityJson != null) {
                    switch (action) {
                        case Constantes.INSERT_ACTION:
                            result = ws.synchronizeDataCom(entityJson, "save_entete_vente");
                            break;
                        case Constantes.UPDATE_ACTION:
                            result = ws.synchronizeDataCom(entityJson, "update_entete_vente");
                            break;
                        case Constantes.DELETE_ACTION:
                            result = ws.synchronizeDataCom(entityJson, "delete_entete_vente");
                            break;
                    }
                }
                afterSynchroniseHeader(result, idListen);
            }
            break;
            case Constantes.TABLE_DOC_VENTE_CODE: {
                //Construction de l'objet à partir de la base de données locale
                YvsComDocVentes en = BuilderEntitySynchro.builderDocVente(localKey, idListen, true);
                WsSynchro ws = new WsSynchro();
                //Construction de l'objet avec ses liaisons sur le serveur distant
                if (action.equals(Constantes.INSERT_ACTION) && en != null) {
                    en.setStatut(Constantes.ETAT_EDITABLE);
                    en.setStatutLivre(Constantes.ETAT_ATTENTE);
                    en.setStatutRegle(Constantes.ETAT_ATTENTE);
                }
                JSONObject entityJson = UtilExport.exportDocVente(en, withChild, idListen);
                ResultatAction<YvsComDocVentes> result = null;
                if (entityJson != null) {
                    switch (action) {
                        case Constantes.INSERT_ACTION:
                            result = ws.synchronizeDataCom(entityJson, "save_doc_vente");
                            break;
                        case Constantes.UPDATE_ACTION:
                            result = ws.synchronizeDataCom(entityJson, "update_doc_vente");
                            break;
                        case Constantes.DELETE_ACTION:
                            result = ws.synchronizeDataCom(entityJson, "delete_doc_vente");
                            break;
                    }
                    if (!Constantes.asLong(idListen) && action.equals(Constantes.INSERT_ACTION)) {
                        // insère une ligne de listen
                        idListen = Ldao.insertNewListenData(localKey, table, UtilsProject.currentUser.getId(), null);
                    }
                }
                afterSynchroniseDocVente(result, idListen, localKey);
            }
            break;
            case Constantes.TABLE_ACOMPTE_CLIENT_CODE: {
                //Construction de l'objet à partir de la base de données locale
                YvsComptaAcompteClient en = BuilderEntitySynchro.builderAcompteClient(localKey, idListen);
                WsSynchro ws = new WsSynchro();
                //Construction de l'objet avec ses liaisons sur le serveur distant
                JSONObject entityJson = UtilExport.exportAcompteClient(en,idListen);
                ResultatAction<YvsComptaAcompteClient> result = null;
                if (entityJson != null) {
                    switch (action) {
                        case Constantes.INSERT_ACTION:
                            result = ws.synchronizeDataCompta(entityJson, "save_acompte_client");
                            break;
                        case Constantes.UPDATE_ACTION:
                            result = ws.synchronizeDataCompta(entityJson, "update_acompte_client");
                            break;
                        case Constantes.DELETE_ACTION:
                            result = ws.synchronizeDataCompta(entityJson, "delete_acompte_client");
                            break;
                    }
                }
                afterSynchroniseAcompteClient(result, idListen);
            }
            break;
            case Constantes.TABLE_NOTIF_ACOMPTE_CLIENT_CODE: {
                //Construction de l'objet à partir de la base de données locale
                YvsComptaNotifReglementVente en = BuilderEntitySynchro.builderNotifReglement(localKey, idListen);
                WsSynchro ws = new WsSynchro();
                //Construction de l'objet avec ses liaisons sur le serveur distant
                JSONObject entityJson = UtilExport.exportNotifReglement(en,idListen);
                ResultatAction<YvsComptaAcompteClient> result = null;
                if (entityJson != null) {
                    switch (action) {
                        case Constantes.INSERT_ACTION:
                            result = ws.synchronizeDataCompta(entityJson, "save_notif_reg_vente");
                            break;
                        case Constantes.UPDATE_ACTION:
                            result = ws.synchronizeDataCompta(entityJson, "update_acompte_client");
                            break;
                        case Constantes.DELETE_ACTION:
                            result = ws.synchronizeDataCompta(entityJson, "delete_acompte_client");
                            break;
                    }
                }
                afterSynchroniseNotifReg(result, idListen);
            }
            break;
            case Constantes.TABLE_CONTENT_DOC_VENTE_CODE: {
                //Construction de l'objet à partir de la base de données locale
                YvsComContenuDocVente en = BuilderEntitySynchro.builderContentDocVente(localKey, idListen);
                WsSynchro ws = new WsSynchro();
                //Construction de l'objet avec ses liaisons sur le serveur distant
                JSONObject entityJson = UtilExport.exportContentVente(en, idListen);
                ResultatAction<YvsComContenuDocVente> result = null;
                if (entityJson != null) {
                    switch (action) {
                        case Constantes.INSERT_ACTION:
                            result = ws.synchronizeDataCom(entityJson, "save_contenu_vente");
                            break;
                        case Constantes.UPDATE_ACTION:
                            result = ws.synchronizeDataCom(entityJson, "update_contenu_vente");
                            break;
                        case Constantes.DELETE_ACTION:
                            result = ws.synchronizeDataCom(entityJson, "delete_contenu_vente");
                            break;
                    }
                }
                afterSynchroniseContentDocVente(result, idListen);
            }
            break;
            case Constantes.TABLE_PIECE_CAISSE_VENTE_CODE: {
                //Construction de l'objet à partir de la base de données locale
                YvsComptaCaissePieceVente en = BuilderEntitySynchro.builderPieceReglement(localKey, idListen);
                WsSynchro ws = new WsSynchro();
                //Construction de l'objet avec ses liaisons sur le serveur distant
                JSONObject entityJson = UtilExport.exportPieceCaisse(en,idListen);
                ResultatAction<YvsComptaCaissePieceVente> result = null;
                if (entityJson != null) {
                    switch (action) {
                        case Constantes.INSERT_ACTION:
                            result = ws.synchronizeDataCom(entityJson, "save_caisse_piece_vente");
                            break;
                        case Constantes.UPDATE_ACTION:
                            result = ws.synchronizeDataCom(entityJson, "update_caisse_piece_vente");
                            break;
                        case Constantes.DELETE_ACTION:
                            result = ws.synchronizeDataCom(entityJson, "delete_caisse_piece_vente");
                            break;
                    }
                }
                afterSynchronisePieceCaisse(result, idListen);
            }
            break;
            default:
                //mettre le listen à false
                String st = "UPDATE yvs_synchro_listen_table SET message='Table non géré' WHERE id=?";
                Ldao.executeSqlQuery(st, new Options[]{new Options(idListen, 1)});
                WsSynchro.currentListen.remove(idListen);
                break;
        }
        return null;
    }

    private YvsComEnteteDocVente afterSynchroniseHeader(ResultatAction result, Long idListen) {
        YvsComEnteteDocVente entity = null;
        try {
            if (result != null ? result.getData() != null : false) {
                Gson gson = UtilEntityBase.createGson();
                if (gson != null) {
                    JsonObject jo = gson.toJsonTree(result.getData()).getAsJsonObject();
                    entity = gson.fromJson(jo.toString(), YvsComEnteteDocVente.class);
                    if (result.isResult() || result.isContinu()==false) {
                        //insert dans la table data_syncrho                            
                        Ldao.insertDataSynchro(idListen, entity.getId(), "OK", true);
                        WsSynchro.countOutI++;
                    } else {
                        //met à jour la table listen_data pour traiter le message
                        Ldao.insertDataSynchro(idListen, entity.getId(), result.getMessage(), false);
                    }
                }
            } else {
                Integer nbFailed = (Integer) Ldao.findOneObjectByNQ("YvsSynchroListenTable.findNbFailedById", new String[]{"id"}, new Object[]{idListen});
                nbFailed = (nbFailed != null) ? nbFailed : 0;
                if (nbFailed > Constantes.NB_FAILED) {
                    Ldao.insertDataSynchro(idListen, -1L, (result != null ? result.getMessage() : "Synchronisation non réussi"), false);
                } else {
                    nbFailed++;
                    Ldao.incrementNbFailed(idListen, nbFailed);
                }
            }
        } catch (JsonSyntaxException ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(ExportService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //enlève de la file des synchro en cours
            WsSynchro.currentListen.remove(idListen);
        }
        return entity;

    }

    private YvsComDocVentes afterSynchroniseDocVente(ResultatAction result, Long idListen, Long idSource) {
        YvsComDocVentes entity = null;
        try {
            if (result != null ? result.getData() != null : false) {
                Gson gson = UtilEntityBase.createGson();
                if (gson != null) {
                    JsonObject jo = gson.toJsonTree(result.getData()).getAsJsonObject();
                    entity = gson.fromJson(jo.toString(), YvsComDocVentes.class);
                    if (result.isResult()) {
                        //insert dans la table data_syncrho                            
                        Ldao.insertDataSynchro(idListen, entity.getId(), "OK", true);
                        //met à jour le numéro de la facture dans la bd locale
                        String query = "UPDATE yvs_com_doc_ventes SET num_doc=?, statut_livre=?, statut_regle=? WHERE id=? ";
                        Ldao.executeSqlQuery(query, new Options[]{new Options(entity.getNumDoc(), 1), new Options(entity.getStatutLivre(), 2), new Options(entity.getStatutRegle(), 3), new Options(entity.getIdDistant(), 4)});
                        WsSynchro.countOutI++;
                        //enlève de la file des synchro en cours les ids Listen concernant le doc de vente
                        WsSynchro.currentListen.remove(idListen);
                        //Traitement des contenu
                        if (entity.getContenus() != null ? !entity.getContenus().isEmpty() : false) {
                            Long idListen_;
                            for (YvsComContenuDocVente c : entity.getContenus()) {
                                //trouve l'id du listen
                                if (Constantes.asLong(c.getIdDistant()) && Constantes.asLong(c.getId())) {
                                    idListen_ = UtilEntityBase.findIdListen(Constantes.TABLE_CONTENT_DOC_VENTE_CODE, c.getIdDistant());
                                    if (Constantes.asLong(idListen_)) {
                                        Ldao.insertDataSynchro(idListen_, c.getIdDistant(), "OK", true);
                                        WsSynchro.countOutI++;
                                        //enlève de la file des synchro en cours
                                        WsSynchro.currentListen.remove(idListen_);
                                    }
                                }
                            }
                        }
                        //Traitement des Règlement
                        if (entity.getReglements() != null ? !entity.getReglements().isEmpty() : false) {
                            Long idListen_;
                            for (YvsComptaCaissePieceVente c : entity.getReglements()) {
                                //trouve l'id du listen
                                if (Constantes.asLong(c.getIdDistant()) && Constantes.asLong(c.getId())) {
                                    idListen_ = UtilEntityBase.findIdListen(Constantes.TABLE_PIECE_CAISSE_VENTE_CODE, c.getIdDistant());
                                    if (Constantes.asLong(idListen_)) {
                                        Ldao.insertDataSynchro(idListen_, c.getIdDistant(), "OK", true);
                                        WsSynchro.countOutI++;
                                        //enlève de la file des synchro en cours
                                        WsSynchro.currentListen.remove(idListen_);
                                    }
                                }
                            }
                        }
                    } else {
                        //met à jour la table listen_data pour traiter le message
                        Ldao.insertDataSynchro(idListen, entity.getId(), result.getMessage(), false);
                    }
                }

            } else {
                //
                Integer nbFailed = (Integer) Ldao.findOneObjectByNQ("YvsSynchroListenTable.findNbFailedById", new String[]{"id"}, new Object[]{idListen});
                nbFailed = (nbFailed != null) ? nbFailed : 0;
                if (nbFailed > Constantes.NB_FAILED) {
                    Ldao.insertDataSynchro(idListen, -1L, (result != null ? result.getMessage() : "Synchronisation non réussi"), false);
                } else {
                    nbFailed++;
                    Ldao.incrementNbFailed(idListen, nbFailed);
                }
            }
        } catch (JsonSyntaxException ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(ExportService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //enlève de la file des synchro en cours
            addRemoveIdListenWithDependence(idSource, idListen, Constantes.TABLE_DOC_VENTE_CODE, false);
        }
        return entity;
    }

    private YvsComptaAcompteClient afterSynchroniseAcompteClient(ResultatAction result, Long idListen) {
        YvsComptaAcompteClient entity = null;
        try {
            if (result != null ? result.getData() != null : false) {
                Gson gson = UtilEntityBase.createGson();
                if (gson != null) {
                    JsonObject jo = gson.toJsonTree(result.getData()).getAsJsonObject();
                    entity = gson.fromJson(jo.toString(), YvsComptaAcompteClient.class);
                    if (result.isResult()) {
                        //insert dans la table data_syncrho                            
                        Ldao.insertDataSynchro(idListen, entity.getId(), "OK", true);
                        //met à jour le numéro de la facture dans la bd locale
                        String query = "UPDATE yvs_compta_acompte_client SET num_refrence=? WHERE id=? ";
                        Ldao.executeSqlQuery(query, new Options[]{new Options(entity.getNumRefrence(), 1), new Options(entity.getIdDistant(), 2)});
                        WsSynchro.countOutI++;
                    } else {
                        //met à jour la table listen_data pour traiter le message
                        Ldao.insertDataSynchro(idListen, entity.getId(), result.getMessage(), false);
                    }
                } else {
                    //
                    Ldao.insertDataSynchro(idListen, -1L, (result != null ? result.getMessage() : "Syncronisation non réussi"), false);
                }
            } else {
                Integer nbFailed = (Integer) Ldao.findOneObjectByNQ("YvsSynchroListenTable.findNbFailedById", new String[]{"id"}, new Object[]{idListen});
                nbFailed = (nbFailed != null) ? nbFailed : 0;
                if (nbFailed > Constantes.NB_FAILED) {
                    Ldao.insertDataSynchro(idListen, -1L, (result != null ? result.getMessage() : "Synchronisation non réussi"), false);
                } else {
                    nbFailed++;
                    Ldao.incrementNbFailed(idListen, nbFailed);
                }
            }
        } catch (JsonSyntaxException ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(ExportService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //enlève de la file des synchro en cours
            WsSynchro.currentListen.remove(idListen);
        }
        //fin de l'exécution d'une tâche
        return entity;
    }

    private YvsComContenuDocVente afterSynchroniseContentDocVente(ResultatAction result, Long idListen) {
        YvsComContenuDocVente entity = null;
        try {
            if (result != null ? result.getData() != null : false) {
                Gson gson = UtilEntityBase.createGson();
                if (gson != null) {
                    JsonObject jo = gson.toJsonTree(result.getData()).getAsJsonObject();
                    entity = gson.fromJson(jo.toString(), YvsComContenuDocVente.class);
                    if (result.isResult()) {
                        //insert dans la table data_syncrho                            
                        Ldao.insertDataSynchro(idListen, entity.getId(), "OK", true);
                        WsSynchro.countOutI++;
                    } else {
                        //met à jour la table listen_data pour traiter le message
                        Ldao.insertDataSynchro(idListen, entity.getId(), result.getMessage(), false);
                    }
                } else {
                    Ldao.insertDataSynchro(idListen, -1L, result.getMessage(), false);
                }
            } else {
                Integer nbFailed = (Integer) Ldao.findOneObjectByNQ("YvsSynchroListenTable.findNbFailedById", new String[]{"id"}, new Object[]{idListen});
                nbFailed = (nbFailed != null) ? nbFailed : 0;
                if (nbFailed > Constantes.NB_FAILED) {
                    Ldao.insertDataSynchro(idListen, -1L, (result != null ? result.getMessage() : "Synchronisation non réussi"), false);
                } else {
                    nbFailed++;
                    Ldao.incrementNbFailed(idListen, nbFailed);
                }
            }
        } catch (JsonSyntaxException ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(ExportService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //enlève de la file des synchro en cours
            WsSynchro.currentListen.remove(idListen);
        }
        //fin de l'exécution d'une tâche
        return entity;
    }

    private YvsComptaCaissePieceVente afterSynchronisePieceCaisse(ResultatAction result, Long idListen) {
        YvsComptaCaissePieceVente entity = null;
        try {
            if (result != null ? result.getData() != null : false) {
                Gson gson = UtilEntityBase.createGson();
                if (gson != null) {
                    JsonObject jo = gson.toJsonTree(result.getData()).getAsJsonObject();
                    entity = gson.fromJson(jo.toString(), YvsComptaCaissePieceVente.class);
                    if (result.isResult()) {
                        //insert dans la table data_syncrho                            
                        Ldao.insertDataSynchro(idListen, entity.getId(), "OK", true);
                        WsSynchro.countOutI++;
                    } else {
                        //met à jour la table listen_data pour traiter le message
                        Ldao.insertDataSynchro(idListen, entity.getId(), result.getMessage(), false);
                    }
                } else {
                    Ldao.insertDataSynchro(idListen, -1L, result.getMessage(), false);
                }
            } else {
                Integer nbFailed = (Integer) Ldao.findOneObjectByNQ("YvsSynchroListenTable.findNbFailedById", new String[]{"id"}, new Object[]{idListen});
                nbFailed = (nbFailed != null) ? nbFailed : 0;
                if (nbFailed > Constantes.NB_FAILED) {
                    Ldao.insertDataSynchro(idListen, -1L, (result != null ? result.getMessage() : "Synchronisation non réussi"), false);
                } else {
                    nbFailed++;
                    Ldao.incrementNbFailed(idListen, nbFailed);
                }
            }
        } catch (Exception ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(ExportService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //enlève de la file des synchro en cours
            WsSynchro.currentListen.remove(idListen);
        }
        //fin de l'exécution d'une tâche
        return entity;
    }

    private YvsComptaNotifReglementVente afterSynchroniseNotifReg(ResultatAction result, Long idListen) {
        YvsComptaNotifReglementVente entity = null;
        try {
            if (result != null ? result.getData() != null : false) {
                Gson gson = UtilEntityBase.createGson();
                if (gson != null) {
                    JsonObject jo = gson.toJsonTree(result.getData()).getAsJsonObject();
                    entity = gson.fromJson(jo.toString(), YvsComptaNotifReglementVente.class);
                    if (result.isResult()) {
                        //insert dans la table data_syncrho                            
                        Ldao.insertDataSynchro(idListen, entity.getId(), "OK", true);
                        WsSynchro.countOutI++;
                    } else {
                        //met à jour la table listen_data pour traiter le message
                        Ldao.insertDataSynchro(idListen, entity.getId(), result.getMessage(), false);
                    }
                }
            } else {
                Integer nbFailed = (Integer) Ldao.findOneObjectByNQ("YvsSynchroListenTable.findNbFailedById", new String[]{"id"}, new Object[]{idListen});
                nbFailed = (nbFailed != null) ? nbFailed : 0;
                if (nbFailed > Constantes.NB_FAILED) {
                    Ldao.insertDataSynchro(idListen, -1L, (result != null ? result.getMessage() : "Synchronisation non réussi"), false);
                } else {
                    nbFailed++;
                    Ldao.incrementNbFailed(idListen, nbFailed);
                }
            }
        } catch (JsonSyntaxException ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(ExportService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(ExportService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //enlève de la file des synchro en cours
            WsSynchro.currentListen.remove(idListen);
        }
        //fin de l'exécution d'une tâche
        return entity;
    }

    public void afterSyncroAll(ResultatAction result, Long idListen, Long remoteId) {
//        try {
//            int idx = waitingFile.indexOf(new FileDeSynchro(idListen));
//            if (idx >= 0) {
//                waitingFile.get(idx).setStatut(Constantes.ETAT_TERMINE);
//            }
//            for (Long id_ : WsSynchro.currentListen) {
//                if (result.isResult()) {
//                    //insert dans la table data_syncrho                            
//                    Ldao.insertDataSynchro(id_, remoteId, "OK", true);
//                } else {
//                    //met à jour la table listen_data pour traiter le message (Si l'information n'est pas déjà synchronisé)
//                    Ldao.insertDataSynchro(id_, remoteId, result.getMessage(), false);
//                }
//                idx = waitingFile.indexOf(new FileDeSynchro(id_));
//                if (idx >= 0) {
//                    waitingFile.get(idx).setStatut(Constantes.ETAT_TERMINE);
//                }
//            }
//        } catch (Exception ex) {
//            LogFiles.addLogInFile("", ex);
//            Logger.getLogger(ExportService.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public static void addRemoveIdListenWithDependence(Long id_source, Long idListen, String table, boolean add) {
        LQueryFactories Ldao = new LQueryFactories();
        switch (table) {
            case Constantes.TABLE_DOC_VENTE_CODE:
                String query = "SELECT l.id FROM yvs_synchro_listen_table l "
                        + "INNER JOIN yvs_com_doc_ventes d ON (d.id=l.id_source AND l.name_table='yvs_com_doc_ventes') "
                        + "WHERE d.id=? AND l.to_listen IS TRUE";
                if (add) {
                    WsSynchro.currentListen.addAll(Ldao.loadBySQLQuery(query, new Options[]{new Options(id_source, 1)}));
                } else {
                    WsSynchro.currentListen.removeAll(Ldao.loadBySQLQuery(query, new Options[]{new Options(id_source, 1)}));
                }
                //content_vente
                query = "SELECT l.id from yvs_synchro_listen_table l "
                        + "INNER JOIN yvs_com_contenu_doc_vente c ON (c.id=l.id_source AND l.name_table='yvs_com_contenu_doc_vente') "
                        + "WHERE c.doc_vente=? AND l.to_listen IS TRUE";
                if (add) {
                    WsSynchro.currentListen.addAll(Ldao.loadBySQLQuery(query, new Options[]{new Options(id_source, 1)}));
                } else {
                    WsSynchro.currentListen.removeAll(Ldao.loadBySQLQuery(query, new Options[]{new Options(id_source, 1)}));
                }
                //règlement vente
                query = "SELECT l.id from yvs_synchro_listen_table l "
                        + "INNER JOIN yvs_compta_caisse_piece_vente pc ON (pc.id=l.id_source AND l.name_table='yvs_compta_caisse_piece_vente')"
                        + "WHERE pc.vente=? AND l.to_listen IS TRUE";
                if (add) {
                    WsSynchro.currentListen.addAll(Ldao.loadBySQLQuery(query, new Options[]{new Options(id_source, 1)}));
                } else {
                    WsSynchro.currentListen.removeAll(Ldao.loadBySQLQuery(query, new Options[]{new Options(id_source, 1)}));
                }
                //taxes
                break;
            default:
                if (add) {
                    WsSynchro.currentListen.add(idListen);
                } else {
                    WsSynchro.currentListen.remove(idListen);
                }
                break;

        }
    }
}
