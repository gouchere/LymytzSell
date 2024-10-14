/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application.service;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.util.Duration;
import com.lymytz.lymytzsell.dao.query.LQueryFactories;
import com.lymytz.lymytzsell.view.LocalLoader;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;

/**
 *
 * @author LENOVO
 */
public class ListenServersLocal extends ScheduledService<Long> {

    ListenServersLocal current;
    private boolean localConnect;
    HomeCaisseController page;

    public ListenServersLocal(int tempsDiffer, HomeCaisseController page) {
        current = this;
        this.page = page;
        this.setDelay(Duration.seconds(tempsDiffer));
        this.setMaximumFailureCount(15);
        this.setPeriod(Duration.seconds(25));
        // Ajoute un temps d'attente supplémentaire en cas d'echec du service
        this.setBackoffStrategy((ScheduledService<?> param) -> new Duration(current.getCurrentFailureCount() * 5));
        this.setOnSucceeded((WorkerStateEvent event) -> Platform.runLater(() -> {
            if (localConnect) {
                page.IMG_ETAT_SERVEUR_L.setImage(new Image(LocalLoader.class.getResourceAsStream("/icones/i_juste.png")));
                Tooltip.install(page.IMG_ETAT_SERVEUR_L, new Tooltip("Serveur de base de données local en ligne"));
            } else {
                page.IMG_ETAT_SERVEUR_L.setImage(new Image(LocalLoader.class.getResourceAsStream("/icones/x.png")));
                Tooltip.install(page.IMG_ETAT_SERVEUR_L, new Tooltip("Serveur de base de données local hors ligne"));
            }
        }));
//        this.setOnFailed(new EventHandler<WorkerStateEvent>() {
//
//            @Override
//            public void handle(WorkerStateEvent event) {
//                if (localConnect) {
////                    page.LAB_ETAT_SERVEUR_L.setText("Actif");
////                    page.LAB_ETAT_SERVEUR_L.getStyleClass().add("color_blue");
//                    page.IMG_ETAT_SERVEUR_L.setImage(new Image(LocalLoader.class.getResourceAsStream("/icones/i_juste.png")));
//                } else {
////                    page.LAB_ETAT_SERVEUR_L.setText("Innactif");
////                    page.LAB_ETAT_SERVEUR_L.getStyleClass().add("color_red");
//                    page.IMG_ETAT_SERVEUR_L.setImage(new Image(LocalLoader.class.getResourceAsStream("/icones/x.png")));
//                }
//            }
//        });
    }

    @Override
    protected Task<Long> createTask() {
        return new Task<Long>() {

            @Override
            protected Long call() throws Exception {
                new Thread(() -> {
                    localConnect = LQueryFactories.pingServer();
                }).start();
                return 0L;
            }
        };
    }

}
