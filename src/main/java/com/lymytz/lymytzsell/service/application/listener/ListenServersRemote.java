/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application.listener;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.util.Duration;
import com.lymytz.lymytzsell.persistence.dao.RQueryFactories;
import com.lymytz.lymytzsell.view.LocalLoader;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;

/**
 *
 * @author LENOVO
 */
public class ListenServersRemote extends ScheduledService<Long> {

    ListenServersRemote current;
    public static  boolean remoteConnect;
    HomeCaisseController page;

        public ListenServersRemote(int tempsDiffer, HomeCaisseController page) {
        current = this;
        this.page = page;
        this.setDelay(Duration.seconds(tempsDiffer));
        this.setMaximumFailureCount(15);
        this.setPeriod(Duration.seconds(30));
        // Ajoute un temps d'attente supplémentaire en cas d'echec du service
        this.setBackoffStrategy((ScheduledService<?> param) -> new Duration(current.getCurrentFailureCount() * 5));
        this.setOnSucceeded((WorkerStateEvent event) -> Platform.runLater(() -> {
            if (remoteConnect) {
                page.IMG_ETAT_SERVEUR_R.setImage(new Image(LocalLoader.class.getResourceAsStream("/icones/i_juste.png")));
                Tooltip.install(page.IMG_ETAT_SERVEUR_R, new Tooltip("Serveur de base de données distant en ligne"));
            } else {
                page.IMG_ETAT_SERVEUR_R.setImage(new Image(LocalLoader.class.getResourceAsStream("/icones/x.png")));
                Tooltip.install(page.IMG_ETAT_SERVEUR_R, new Tooltip("Serveur de base de données distant en ligne"));
            }
        }));
    }

    @Override
    protected Task<Long> createTask() {
        return new Task<Long>() {

            @Override
            protected Long call() throws Exception {
                new Thread(() -> {
                    remoteConnect = RQueryFactories.pingServer();
                    page.setConnect(remoteConnect);
                }).start();
                return 0L;
            }
        };
    }

}
