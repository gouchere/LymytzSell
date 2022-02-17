/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.synchro;

import lymytz.service.application.synchro.export.ExportService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import javax.print.attribute.standard.Severity;
import lymytz.dao.Options;
import lymytz.dao.entity.YvsUsersAgence;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;
import lymytz.synchro.ws.WsSynchro;
import lymytz.view.main.HomeCaisseController;

/**
 *
 * @author LENOVO
 */
public class SynchronizeDataOut extends ScheduledService<Long> {

    HomeCaisseController page;
    LQueryFactories Ldao;
    public static boolean running;

    public SynchronizeDataOut(int tempsDiffer, HomeCaisseController page) {
        running=true;
        Ldao = new LQueryFactories<>();
        this.page = page;
        //temps  d'attente avant le demarrage du service...
        this.setDelay(Duration.seconds(tempsDiffer));
        this.setRestartOnFailure(true);
        this.setMaximumFailureCount(30);
        this.setPeriod(Duration.seconds(30));
        this.stateProperty().addListener((ObservableValue<? extends State> observable, State oldValue, State newValue) -> {
            running=newValue.equals(State.READY) || newValue.equals(State.RUNNING) || newValue.equals(State.SUCCEEDED) || newValue.equals(State.SCHEDULED);            
        });
        // Ajoute un temps d'attente supplémentaire en cas d'echec du service
        this.setBackoffStrategy((ScheduledService<?> param) -> new Duration(SynchronizeDataOut.this.getCurrentFailureCount() * 5));
//        this.setOnReady((WorkerStateEvent event) -> {
//            running=true;
//        });
//        this.setOnSucceeded((WorkerStateEvent event) -> {
//            running=true;
//        });
//        this.setOnFailed((WorkerStateEvent event) -> {
//            running=false;
//        });
//        this.setOnCancelled((WorkerStateEvent event) -> {
//            running=false;
//        });
    }

    @Override
    protected Task<Long> createTask() {
        return new Task<Long>() {

            @Override
            protected Long call() throws Exception {
                try {
                    if (Constantes.asLong(UtilsProject.remoteAuthor)) {
                        if (WsSynchro.serverOnline()) {
                            if (UtilsProject.APPLICATION_IHM) {
                                Platform.runLater(() -> {
                                    page.LAB_SYNC_MSG.setText("");
                                });
                            }
                            if (!WsSynchro.runningOut) {
                                String query = "SELECT y.id, y.name_table, y.id_source, y.action_name FROM yvs_synchro_listen_table y "
                                        + "WHERE y.to_listen IS TRUE AND y.message IS NULL AND name_table IN ( "
                                        + "'yvs_com_entete_doc_vente','yvs_com_doc_ventes','yvs_com_contenu_doc_vente',"
                                        + "'yvs_compta_acompte_client','yvs_compta_notif_reglement_vente', "
                                        + "'yvs_compta_caisse_piece_vente' "
                                        + ") ORDER BY y.id LIMIT 20";
                                List<Object[]> l = Ldao.loadBySQLQuery(query, new Options[]{});
                                ExportService service;
                                if (!l.isEmpty()) {
                                    for (Object[] line : l) {
                                        WsSynchro.runningOut = true;
                                        String table = (String) line[1];
                                        Long idListen = (Long) line[0];
                                        Long idLocal = (Long) line[2];
                                        String action = (String) line[3];
                                        if (Constantes.asLong(idLocal)) {
                                            if (!WsSynchro.currentListen.contains(idListen)) {
                                                //Ajoute IdListen et les ids de ses dépendances à la liste des élément encours de synchronisation
                                                ExportService.addRemoveIdListenWithDependence(idLocal, idListen, table, true);
                                                service = new ExportService("T", null, null);
                                                service.setTable(table);
                                                service.setIdListen(idListen);
                                                service.setIdLocal(idLocal);
                                                service.setAction(action);
                                                new Thread(service).start();
//                                            service.synchroniseData(table, idListen, idLocal, action, true);
                                            } else {
                                                //passe en silence
                                            }
                                        } else {

                                        }
                                    }
                                    WsSynchro.runningOut = false;
                                }
                            }
                        } else {
                            LogFiles.addLogInFile("Les services distants sont indisponible", Severity.ERROR);
                            if (UtilsProject.APPLICATION_IHM) {
                                Platform.runLater(() -> {
                                    UtilsProject.currentPage.LAB_SYNC_MSG.setText("Le service n'est pas en ligne !");
                                });
                            }
                        }
                    } else {
                        if (UtilsProject.APPLICATION_IHM) {
                            Platform.runLater(() -> {
                                page.LAB_SYNC_MSG.setText("la synchro a été interompu car l'auteur distant n'a pas été trouvé.");
                            });
                        }
                        if (UtilsProject.currentUser != null ? Constantes.asLong(UtilsProject.currentUser.getId()) : false) {
                            UtilsProject.remoteAuthor = UtilEntityBase.findIdRemoteData(Constantes.TABLE_USER_AGENCE_CODE, UtilsProject.currentUser.getId());
                            if (UtilsProject.remoteAuthor != null ? UtilsProject.remoteAuthor <= 0 : true) {
                                //Synchronise remote author
                                Long r = UtilEntityBase.synchronizeAuthor(new YvsUsersAgence(UtilsProject.currentUser.getId(), UtilsProject.currentUser.getUsers(), UtilsProject.currentUser.getAgence()));
                                if (r != null ? r > 0 : false) {
                                    UtilsProject.remoteAuthor = r;
                                }
                            }
                        }
                    }
                    if (UtilsProject.APPLICATION_IHM) {
                        //compte le nombre d'enregistrement qu'il reste à synchroniser depuis le serveur distant
                        String query_ = "SELECT COUNT(y.id) FROM yvs_synchro_listen_table y "
                                + "WHERE y.to_listen IS TRUE AND name_table IN ( "
                                + "'yvs_com_entete_doc_vente','yvs_com_doc_ventes','yvs_com_contenu_doc_vente',"
                                + "'yvs_compta_acompte_client','yvs_compta_notif_reglement_vente', "
                                + "'yvs_compta_caisse_piece_vente' "
                                + ")";
                        final Long re = (Long) Ldao.findOneObjectBySQLQ(query_, new Options[]{});
                        Platform.runLater(() -> page.LAB_NB_DATA_OUT.setText("\t " + (WsSynchro.countOutI) + "/ " + (re + WsSynchro.countOutI) + " Enregistrement(s) à envoyer au serveur distant"));
                        query_ = "SELECT COUNT(id) FROM yvs_synchro_listen_table WHERE to_listen=true AND message IS NOT NULL ";
                        final Long re1 = (Long) Ldao.findOneObjectBySQLQ(query_, new Options[]{});
                        if (Constantes.asLong(re1)) {
                            Platform.runLater(() -> {
                                page.ICO_ALERT_EX.setVisible(true);
                                Tooltip.install(page.ICO_ALERT_EX, new Tooltip("Des lignes non synchronisées sont en attente d'une action de votre part !"));
                            });
                        } else {
                            Platform.runLater(() -> {
                                page.ICO_ALERT_EX.setVisible(false);
                            });
                        }
                        updateProgress(0, 1);
                    }
                } catch (Exception ex) {
                    LogFiles.addLogInFile("Synchronisation des données de ventes non réussi ", ex);
                    Logger.getLogger(SynchronizeDataOut.class.getName()).log(Level.SEVERE, null, ex);
                    this.failed();
                }
                return 0L;
            }
        };
    }

}
