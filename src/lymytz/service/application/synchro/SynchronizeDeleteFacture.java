/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.synchro;

import lymytz.service.application.synchro.impor.ImportServiceDelFacture;
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
public class SynchronizeDeleteFacture extends ScheduledService<Boolean> {

    HomeCaisseController page;
    RQueryFactories Rdao;
    LQueryFactories Ldao;

    public SynchronizeDeleteFacture(int tempsDiffer, HomeCaisseController page) {
        Rdao = new RQueryFactories<>();
        this.page = page;
        //temps  d'attente avant le demarrage du service...
        this.setDelay(Duration.seconds(tempsDiffer));
        this.setMaximumFailureCount(3);
        this.setPeriod(Duration.seconds(20));
        this.stateProperty().addListener((ObservableValue<? extends State> observable, State oldValue, State newValue) -> {
        });
        // Ajoute un temps d'attente supplémentaire en cas d'echec du service
        this.setBackoffStrategy((ScheduledService<?> param) -> new Duration(SynchronizeDeleteFacture.this.getCurrentFailureCount() * 5));
        this.setOnSucceeded((WorkerStateEvent event) -> {
        });
        this.setOnScheduled((WorkerStateEvent event) -> {
        });
        new LymytzLoaderEntity<>(false);
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
        ImportServiceDelFacture service = new ImportServiceDelFacture(null, null, null, true);
        try {
            if (ListenServersRemote.remoteConnect) {
                if (UtilsProject.RcurrentSociete != null && Constantes.asLong(UtilsProject.ID_SERVEUR)) {
                    String query = Constantes.getQueryListenDelFacture();
                    String dure_init = UtilsProject.properties.get("DATE_INIT").toString();
                    Date date = getDate(dure_init);
                    List<Object[]> l = Rdao.loadBySQLQuery(query, new Options[]{new Options(UtilsProject.ID_SERVEUR, 1), new Options(UtilsProject.RcurrentAgence.getId(), 2), new Options(date, 3)});
                    if (l != null ? !l.isEmpty() : false) {
                        Long idListenOnRemote = Long.valueOf((String) l.get(0)[0]);
                        String table = (String) l.get(0)[1];
                        Long idLocalOnRemote = Long.valueOf((String) l.get(0)[2]);
                        String action = (String) l.get(0)[3];
                        int idx = LymytzLoaderEntity.ALLENTITY.indexOf(new LymytzEntityClass("", "", table));
                        if (idx >= 0) {
                            try {
                                LymytzEntityClass classe = LymytzLoaderEntity.ALLENTITY.get(idx);
                                List<EntityColumn> colonnes = LymytzLoaderEntity.loadEntityColumn(classe.getEntity());
                                //exécute la requête de récupération
                                LQuery queryData = UtilsProject.buildQueryRemote(table, colonnes, new String[]{"y.id"}, false, idListenOnRemote);
                               
                                List<LymytzData> datas = Rdao.loadDataBySQLQuery(queryData.getQuery(), new Options[]{new Options(idLocalOnRemote, 1)}, colonnes);
                                //analyse et insertion du résultat
                                service = new ImportServiceDelFacture(datas, table, null, true);
                                service.setRemoteIdListen(idListenOnRemote);
                                service.setRemoteIdLocal(idLocalOnRemote);
                            } catch (Exception ex) {
                                LogFiles.addLogInFile("", ex);
                                Logger.getLogger(SynchronizeDeleteFacture.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    //compte le nombre d'enregistrement qu'il reste à synchroniser depuis le serveur distant
                    String query_ = Constantes.getQueryListenDataCount();
                    List<Object[]> re = Rdao.loadBySQLQuery(query_, new Options[]{new Options(UtilsProject.ID_SERVEUR, 1), new Options(UtilsProject.RcurrentSociete.getId(), 2), new Options(date, 3)});
                    String action;
                    for (Object[] current_ : re) {
                        action = (String) current_[1];
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
            Logger.getLogger(SynchronizeDeleteFacture.class.getName()).log(Level.SEVERE, null, ex);
            this.failed();
        }
        return service;

    }
}
