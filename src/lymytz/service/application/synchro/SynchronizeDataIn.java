/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.synchro;

import lymytz.service.application.synchro.impor.ImportService;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.util.Duration;
import lymytz.dao.Options;
import lymytz.dao.entity.service.EntityColumn;
import lymytz.dao.entity.service.LymytzData;
import lymytz.dao.entity.service.LymytzEntityClass;
import lymytz.dao.entity.service.LymytzLoaderEntity;
import lymytz.dao.query.LQueryFactories;
import lymytz.dao.query.RQueryFactories;
import lymytz.service.application.service.ListenServersRemote;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LQuery;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;
import lymytz.synchro.ws.WsSynchro;
import lymytz.view.main.HomeCaisseController;

/**
 *
 * @author LENOVO
 */
public class SynchronizeDataIn extends ScheduledService<Boolean> {

    HomeCaisseController page;
    RQueryFactories Rdao;
    LQueryFactories Ldao;
    public static boolean running;

    public SynchronizeDataIn(int tempsDiffer, HomeCaisseController page) {
        Rdao = new RQueryFactories<>();
        running = true;
        this.page = page;
        //temps  d'attente avant le demarrage du service...
        this.setDelay(Duration.seconds(tempsDiffer));
        this.setRestartOnFailure(true);
        this.setMaximumFailureCount(30);
        this.setPeriod(Duration.seconds(30));
        this.stateProperty().addListener((ObservableValue<? extends State> observable, State oldValue, State newValue) -> {
            running = newValue.equals(State.READY) || newValue.equals(State.RUNNING) || newValue.equals(State.SUCCEEDED) || newValue.equals(State.SCHEDULED);
        });
        // Ajoute un temps d'attente supplémentaire en cas d'echec du service
        this.setBackoffStrategy((ScheduledService<?> param) -> new Duration(SynchronizeDataIn.this.getCurrentFailureCount() * 5));
        this.setOnSucceeded((WorkerStateEvent event) -> {
        });
        this.setOnScheduled((WorkerStateEvent event) -> {
        });
        this.setOnCancelled((WorkerStateEvent event) -> {
            System.err.println(" Canceled");
        });
        new LymytzLoaderEntity<>(true);
    }

    private Date getDate(String format) {
        try {
            if (Constantes.asString(format)) {
                String[] date_ = format.split("-");
                int d = Integer.valueOf(date_[0]);
                int m = Integer.valueOf(date_[1]);
                int y = Integer.valueOf(date_[2]);
                Calendar cal = Calendar.getInstance();
                cal.set(y, (m - 1), d);
                return cal.getTime();
            }
        } catch (NumberFormatException ex) {
            LogFiles.addLogInFile("Récupération de la date erronée !", ex);
        }
        return new Date();
    }
    Long NInsert = 0l, NUpdate = 0l, NDelete = 0L;

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {

            @Override
            protected Boolean call() throws Exception {
                try {
                    if (ListenServersRemote.remoteConnect) {
                        if (!WsSynchro.runningIn && UtilsProject.RcurrentSociete != null && Constantes.asLong(UtilsProject.ID_SERVEUR)) {
                            String query = Constantes.getQueryListenData();
                            String dure_init = UtilsProject.properties.get("DATE_INIT").toString();
                            Date date = getDate(dure_init);
                            List<Object[]> l = Rdao.loadBySQLQuery(query, new Options[]{new Options(UtilsProject.ID_SERVEUR, 1), new Options(UtilsProject.RcurrentSociete.getId(), 2), new Options(date, 3)});
                            if (l != null ? !l.isEmpty() : false) {
                                WsSynchro.runningIn = true;
                                Long idListenOnRemote = Long.valueOf((String) l.get(0)[0]);
                                String table = (String) l.get(0)[1];
                                Long idLocalOnRemote = Long.valueOf((String) l.get(0)[2]);
                                String action = (String) l.get(0)[3];
                                int idx = LymytzLoaderEntity.ALLENTITY.indexOf(new LymytzEntityClass("", "", table));
                                if (idx >= 0) {
                                    try {
                                        ImportService service;
                                        LymytzEntityClass classe = LymytzLoaderEntity.ALLENTITY.get(idx);
                                        List<EntityColumn> colonnes = LymytzLoaderEntity.loadEntityColumn(classe.getEntity());
                                        //exécute la requête de récupération
                                        LQuery queryData = UtilsProject.buildQueryRemote(table, colonnes, new String[]{"y.id"}, false, idListenOnRemote);
                                        List<LymytzData> datas = Rdao.loadDataBySQLQuery(queryData.getQuery(), new Options[]{new Options(idLocalOnRemote, 1)}, colonnes);
                                        //analyse et insertion du résultat
                                        service = new ImportService(datas, table, null, true);
                                        service.setRemoteIdListen(idListenOnRemote);
                                        service.setRemoteIdLocal(idLocalOnRemote);
                                        new Thread(service).start();
                                    } catch (Exception ex) {
                                        LogFiles.addLogInFile("", ex);
                                        WsSynchro.runningIn = false;
                                        Logger.getLogger(SynchronizeDataIn.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    WsSynchro.runningIn = false;
                                }
                            }
                            //compte le nombre d'enregistrement qu'il reste à synchroniser depuis le serveur distant
                            String query_ = Constantes.getQueryListenDataCount();
                            List<Object[]> re = Rdao.loadBySQLQuery(query_, new Options[]{new Options(UtilsProject.ID_SERVEUR, 1), new Options(UtilsProject.RcurrentSociete.getId(), 2), new Options(date, 3)});
                            String action;                            
                            for (Object[] current_ : re) {
                                action = (String) current_[1];
                                System.err.println(" After Import "+action+" ---- "+current_[0]);
                                switch (action) {
                                    case Constantes.INSERT_ACTION:
                                        if (Constantes.asString((String) current_[0])) {
                                            NInsert = Long.valueOf((String) current_[0]);
                                        }
                                        break;
                                    case Constantes.UPDATE_ACTION:
                                        if (Constantes.asString((String) current_[0])) {
                                            NUpdate = Long.valueOf((String) current_[0]);
                                        }
                                        break;
                                    case Constantes.DELETE_ACTION:
                                        if (Constantes.asString((String) current_[0])) {
                                            NDelete = Long.valueOf((String) current_[0]);
                                        }
                                        break;
                                }
                            }
                            Platform.runLater(() -> page.LAB_NB_DATA_IN.setText("\t Importation -\t" + "Création : " + (WsSynchro.countI) + "/ " + (NInsert + WsSynchro.countI) + " \t"
                                    + "Modification : " + (WsSynchro.countU) + "/ " + (NUpdate + WsSynchro.countU) + " \t Suppression: " + (WsSynchro.countD) + "/ " + (NDelete + WsSynchro.countD)));
                        }
                    }
                } catch (Exception ex) {
                    LogFiles.addLogInFile("Synchronisation des données de ventes non réussi ", ex);
                    Logger.getLogger(SynchronizeDataIn.class.getName()).log(Level.SEVERE, null, ex);
                    this.failed();
                }
                return true;           
            }
      };
//        ImportService service = new ImportService(null, null, null, true);
//        
//        return service;

    }

//    int i = 0;
//
//    public boolean insertDataIntoLocalDB(String table, List<LymytzData> data) {
//        if (data != null) {
//            i = 0;
//            data.stream().forEach((row) -> {
//                //vérifie si cette donnée existe déjà sur le serveur locale   
//                String query;
//                Long localId;
//                if (row.getAction().equals(Constantes.INSERT_ACTION)) {
//                    query = UtilsProject.buildInsertQuery(table, row.getValue());
//                    localId = Ldao.insertFromSqlQuery(table, query, UtilsProject.buildValueParam(row.getValue()));
//
//                } else {
//                    query = UtilsProject.buildUpdateQuery(table, row.getValue(), row.getpKey());
//                    localId = row.getpKey();
//                    Ldao.updateFromSqlQuery(query, UtilsProject.buildValueParam(row.getValue()));
//                }
//                //insert la ligne dans la table yvs_synchro_data_synchro et yvs_synchro_listen_table
//                Ldao.insertListenData(localId, table, null, UtilsProject.getValKey(row.getValue()));
//            });
//        }
//        return true;
//    }
}
