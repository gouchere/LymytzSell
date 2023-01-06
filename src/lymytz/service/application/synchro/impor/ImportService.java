/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.synchro.impor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javax.print.attribute.standard.Severity;
import lymytz.dao.Options;
import lymytz.dao.entity.service.EntityColumn;
import lymytz.dao.entity.service.LymytzData;
import lymytz.dao.entity.service.LymytzEntityClass;
import lymytz.dao.entity.service.LymytzLoaderEntity;
import lymytz.dao.query.LQueryFactories;
import lymytz.dao.query.RQueryFactories;
import lymytz.service.application.synchro.ParamQuery;
import lymytz.service.application.synchro.UtilEntityBase;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;
import lymytz.synchro.ws.WsSynchro;

/**
 *
 * @author Admin
 */
public class ImportService extends Task<Boolean> {

    LQueryFactories Ldao = new LQueryFactories();
    RQueryFactories Rdao = new RQueryFactories();

    Logger log;
    private Options[] parametres;
    private List<ParamQuery> params;
    private List<LymytzData> listData;
    private List<LymytzData> listDataResult;
    private String table;
    ImportDataController page;
    private int compteur;
    private boolean fromService;

    private Long remoteIdListen, remoteIdLocal;

    public ImportService() {
    }

    public ImportService(List<LymytzData> data, String table, ImportDataController page, boolean service) {
        this.fromService = service;
        this.listData = data;
        listDataResult = new ArrayList<>();
        this.table = table;
        this.page = page;
    }

    public Long getRemoteIdListen() {
        return remoteIdListen;
    }

    public void setRemoteIdListen(Long remoteIdListen) {
        this.remoteIdListen = remoteIdListen;
    }

    public Long getRemoteIdLocal() {
        return remoteIdLocal;
    }

    public void setRemoteIdLocal(Long remoteIdLocal) {
        this.remoteIdLocal = remoteIdLocal;
    }

    public List<LymytzData> getListData() {
        return listData;
    }

    public void setListData(List<LymytzData> listData) {
        this.listData = listData;
    }

    public List<ParamQuery> getParams() {
        return params;
    }

    public void setParams(List<ParamQuery> params) {
        this.params = params;
    }

    @Override
    protected Boolean call() throws Exception {
        compteur = 1;
        if (listData != null ? !listData.isEmpty() : false) {
            List<LymytzData> result = mapForeignKeyToLocalKey(listData);
            if (page != null) {
                page.actualiseMessageImport("");
            }
            compteur = 1;
            insertDataIntoLocalDB(table, result);
        } else {
            if (table != null) {
                insertDataIntoLocalDB(table, null, true);
            }
        }
        WsSynchro.runningIn = false;  // fin d'exécution d'une ligne
        return true;
    }

    public List<LymytzData> mapForeignKeyToLocalKey(List<LymytzData> listData) {
        LymytzData row;
        Long key = null;
        Long localKey = null;
        List<LymytzData> re = new ArrayList<>();
        try {
            for (LymytzData lineR : listData) { //parcours des lignes            
                row = new LymytzData(new EntityColumn[lineR.getValue().length]);
                row.setIdListen(lineR.getIdListen());
                row.setIdDistant(lineR.getIdDistant());
                int i = 0;
                for (EntityColumn col : lineR.getValue()) {
                    //parcours des colonnes
                    //Information sur la colone
                    row.getValue()[i] = col;
                    if (col.isHasJoinColum()) {
                        //trouve l'élément de liaison
                        key = findForeignKey(col, col.getJoinTable());
//                        log.log(Level.INFO, "{0}   Id Locale {1}", new Object[]{col.getJoinTable(), key});
//                        System.err.println(col.getJoinTable()+" Id locale "+key);
                        if (key == null) {
                            // si la clé n'a pas encore été synchronisé sur le serveur local
                            key = findForeignTableFromRemoteDB(col, col.getTableName());
                        }
                        row.getValue()[i].setColumnValue(key);
                        if ((key != null) ? key <= 0 : false) {
                            //si la clé n'est pas trouvé, on peut interrompre le traitement en indiquant la valeur non trouvé
                            //(Créer un fichier de log des importations)
                            LogFiles.addLogInFile("Erreur d'importation de la l'entité " + table + " pour la clé " + row.getpKey() + ". La référence à la table " + col.getJoinTable() + " Id=" + col.getColumnValue(), Severity.WARNING);
                            return null;
                        }
                    }
                    if (col.getColumnName().equals("id")) {
                        //Vérifie si l'information n'existe pas déjà sur le serveur local
                        row.setIdDistant((Long) col.getColumnValue());
                        localKey = findForeignKey(col, col.getTableName());
                        if (localKey == null) {
                            row.setAction(Constantes.INSERT_ACTION);
                            row.setpKey(null);
                        } else {
                            row.setAction(Constantes.UPDATE_ACTION);
                            row.setpKey(localKey);
                        }
                    }
                    i++;
                }
//                Incrémente l'indicateur graphique de progression
                if (row.getValue()[0].getTableName().equals(table)) {
                    compteur++;
                    this.updateProgress(compteur, this.listData.size());
                    this.updateMessage("Récupération: " + compteur + " lignes sur " + this.listData.size());
                }
                re.add(row);
            }
        } catch (Exception ex) {
            WsSynchro.runningIn = false;
        }
        return re;
    }

    private Long findForeignKey(EntityColumn col, String table) {
        //Vérifie d'abord dans une liste tempon        
        String query = "SELECT l.id_source FROM yvs_synchro_data_synchro s INNER JOIN yvs_synchro_listen_table l ON l.id=s.id_listen "
                + "INNER JOIN " + table + " y ON (y.id=l.id_source AND l.name_table=?) WHERE s.id_distant=?::bigint ";
        Long remoteKey = (Long) Ldao.findOneObjectBySQLQ(query, new Options[]{new Options(table, 1), new Options(col.getColumnValue(), 2)});
        return remoteKey;
    }

    private Long findForeignTableFromRemoteDB(EntityColumn col, String table) {
        if (!table.equals(col.getJoinTable())) {
// Si la table local est vide, on cherche à la récupérer dans son intégralité
            Long count = (Long) Ldao.findOneObjectBySQLQ("SELECT COUNT(*) FROM " + col.getJoinTable(), new Options[]{});
            if (count != null ? count <= 0 : true) {
                //Sinon, on ne récupère que la ligne correspondant à la clé étrangère
                if (page != null) {
                    page.actualiseMessageImport("Récupération de la table " + col.getJoinTable());
                }
                importAllDataFromRemoteTable(col.getJoinTable());
                if (page != null) {
                    page.actualiseMessageImport("");
                }

            } else {
                //Sinon, on ne récupère que la ligne correspondant à la clé étrangère
                if (page != null) {
                    page.actualiseMessageImport("Récupération de la ligne " + col.getColumnValue().toString() + " sur la table " + col.getJoinTable());
                }
                importDataFromRemoteTableByKey(col.getJoinTable(), col);
                if (page != null) {
                    page.actualiseMessageImport("");
                }

            }
            return findForeignKey(col, col.getJoinTable());
        } else {
            //gérer les relations reflexives
            if (col.getColumnValue() != null) {
                // Trouve la clé à partir d'une autre méthode
                return findForeignKey(col, table);

            } else {
                return null;
            }
        }
    }

    private void importAllDataFromRemoteTable(String table) {
        LymytzEntityClass entity = LymytzLoaderEntity.ALLENTITY.get(LymytzLoaderEntity.ALLENTITY.indexOf(new LymytzEntityClass(null, null, table)));
        //récupère les colonnes
        List<EntityColumn> colonnes = LymytzLoaderEntity.loadEntityColumn(entity.getEntity());
        LoaderRemotelImportData task = new LoaderRemotelImportData();
        task.setTable(table);
        task.setColonnes(colonnes);
        task.requeteLibre(task.dao.buildeGenericRemoteQuery(table, colonnes, null, null));
        insertDataIntoLocalDB(table, mapForeignKeyToLocalKey(task.getListData()));
    }

    private void importDataFromRemoteTableByKey(String table, EntityColumn col) {
        int idx = LymytzLoaderEntity.ALLENTITY.indexOf(new LymytzEntityClass(null, null, table));
        LymytzEntityClass entity = LymytzLoaderEntity.ALLENTITY.get(idx);
        //récupère les colonnes
        List<EntityColumn> colonnes = LymytzLoaderEntity.loadEntityColumn(entity.getEntity());
        LoaderRemotelImportData task = new LoaderRemotelImportData();
        task.setTable(table);
        task.setColonnes(colonnes);
        String[] cols = new String[]{"y.id"};
        task.setColFilter(cols);
        task.setValueFilter(col.getColumnValue());
        task.requeteLibre(task.dao.buildeGenericRemoteQuery(table, colonnes, task.getColFilter(), col.getColumnValue()));
        insertDataIntoLocalDB(table, mapForeignKeyToLocalKey(task.getListData()), false);
    }
    int i = 0;

    public boolean insertDataIntoLocalDB(String table, List<LymytzData> data) {
        return insertDataIntoLocalDB(table, data, false);
    }

    public boolean insertDataIntoLocalDB(String table, List<LymytzData> data, boolean onlyDelete) {
        try {
            if (data != null) {
                i = 0;
                data.stream().forEach((row) -> {
                    String query;
                    Long localId;
                    String action = row.getAction();
                    Long idL = null;
                    if (!listData.isEmpty()) {
                        //Maj des données sur le serveur locale
                        switch (action) {
                            case Constantes.INSERT_ACTION:
                                query = UtilsProject.buildInsertQuery(table, row.getValue());
                                localId = Ldao.insertFromSqlQuery(table, query, UtilsProject.buildValueParam(row.getValue(), (row.getValue().length - 1)));
                                break;
                            case Constantes.UPDATE_ACTION:
                                query = UtilsProject.buildUpdateQuery(table, row.getValue(), row.getpKey());
                                localId = row.getpKey();
                                Options[] parametres_ = UtilsProject.buildValueParam(row.getValue(), row.getValue().length);
                                parametres_[row.getValue().length - 1] = new Options(localId, row.getValue().length);
                                Ldao.executeSqlQuery(query, parametres_);
                                break;
                            default:
                                localId = UtilEntityBase.findIdLocalFromIdListen(table, row.getIdDistant());
                                if (localId != null ? localId > 0 : false) {
                                    query = "DELETE FROM " + table + " WHERE id= ?";
                                    Ldao.executeSqlQuery(query, new Options[]{new Options(localId, 1)});
                                }
                                break;
                        }
                        //insert la ligne dans la table yvs_synchro_data_synchro et yvs_synchro_listen_table
                        Ldao.insertListenData(localId, table, null, row.getIdDistant(), false);
                        //insert à nouveau sur le serveur distant dans la table yvs_synchro_data_synchro
                        if (Constantes.asLong(row.getIdListen())) {
                            // trouve l'id de l'élément dans la table yvs_synchro_listen_table du serveur distant
                            if (fromService) {
                                Rdao.insertIntoDataSynchro(row.getIdListen(), localId, UtilsProject.ID_SERVEUR);
                            } else {
                                // met à jour les élément importés en fonction de l'id_source
                                Rdao.insertIntoDataSynchroByIdSource(row.getIdDistant(), localId, UtilsProject.ID_SERVEUR, table);
                            }
                        }
                    }
                    switch (action) {
                        case Constantes.INSERT_ACTION:
                            WsSynchro.countI++;
                            break;
                        case Constantes.UPDATE_ACTION:
                            WsSynchro.countU++;
                            break;
                        case Constantes.DELETE_ACTION:
                            WsSynchro.countD++;
                            break;
                    }
                    if (row.getValue()[0].getTableName().equals(this.table)) {
                        compteur++;
                        this.updateProgress(compteur, this.listData.size());
                        this.updateMessage("Insertion: " + compteur + " lignes sur " + this.listData.size());
                    }
                });
            } else if (onlyDelete) {
                Long localId = UtilEntityBase.findIdLocalFromIdListen(table, remoteIdLocal);
                if (localId != null ? localId > 0 : false) {
                    String query = "DELETE FROM " + table + " WHERE id= ?";
                    Ldao.executeSqlQuery(query, new Options[]{new Options(localId, 1)});
                }
                //insert à nouveau sur le serveur distant dans la table yvs_synchro_data_synchro
                if (remoteIdListen != null) {
                    Rdao.insertIntoDataSynchro(remoteIdListen, remoteIdLocal, UtilsProject.ID_SERVEUR);
                }
            }
        } catch (Exception ex) {
            WsSynchro.runningIn = false;
        }
        return true;
    }
}
