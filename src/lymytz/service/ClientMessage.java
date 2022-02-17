/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import static lymytz.service.application.service.ListenServersRemote.remoteConnect;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzException;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import lymytz.view.LocalLoader;
import lymytz.view.main.HomeCaisseController;

/**
 *
 * @author LYMYTZ
 */
public class ClientMessage {

    String name;
    public Socket socketClient;
    public BufferedReader read;
    private String message;
    HomeCaisseController page;

    public ClientMessage(String name, HomeCaisseController page) {
        this.name = name;
        this.page = page;
    }

    public void initClient() {
        try {
            page.LAB_SYNC_MSG.setVisible(false);
            page.LAB_SYNC_MSG.setText("");
            String ip = UtilsProject.properties.getProperty(Constantes.KEY_LOCAL_HOST);
            String port = UtilsProject.properties.getProperty(Constantes.KEY_APPS_PORT);
            socketClient = new Socket(ip, Integer.valueOf(port));
            readMessage(socketClient);
        } catch (IOException ex) {
            Logger.getLogger(ClientMessage.class.getName()).log(Level.SEVERE, null, ex);
            // la socket serveur n'a pas été trouvé. Afficher un message 
            Platform.runLater(() -> {
                try {
                    page.LAB_SYNC_MSG.setVisible(true);
                    page.LAB_SYNC_MSG.setText("L'application serveur n'est pas demarrée; certaines fonctionnalités ne seront par conséquent pas disponibles");
                    if (socketClient != null) {
                        socketClient.close();
                    }
                } catch (IOException ex1) {
                    LymytzService.openAlertDialog("L'application serveur est arrêté", "Veillez demarrer l'application serveur", "", Alert.AlertType.ERROR);
                    Logger.getLogger(ClientMessage.class.getName()).log(Level.SEVERE, null, ex1);
                }
            });
        }
    }

    public void readMessage(Socket socketClient) {
        try {
            while (true) {
                read = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                message = read.readLine();
                if (message != null) {
                    Boolean rep = Boolean.valueOf(message);
                    changeStatut(rep);
                }
            }
        } catch (IOException ex) {
            try {
                System.err.println(" Lecture du message impossible .... ");
                page.setConnect(null);
                changeStatut(false);
                socketClient.close();
                Logger.getLogger(ClientMessage.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex1) {
                Logger.getLogger(ClientMessage.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void changeStatut(Boolean connect) {
        Platform.runLater(() -> {
            if (connect != null ? connect : false) {
                page.IMG_ETAT_SERVEUR_R.setImage(new Image(LocalLoader.class.getResource("icones/i_juste.png").toExternalForm()));
                Tooltip.install(page.IMG_ETAT_SERVEUR_R, new Tooltip("Serveur de base de données distant en ligne"));
            } else {
                page.IMG_ETAT_SERVEUR_R.setImage(new Image(LocalLoader.class.getResource("icones/x.png").toExternalForm()));
                Tooltip.install(page.IMG_ETAT_SERVEUR_R, new Tooltip("Serveur de base de données distant en ligne"));
            }
        });
    }

    private void setStyleButon(Button buton) {
//        buton.setAlignment(Pos.CENTER);
        buton.setAlignment(Pos.BOTTOM_CENTER);
        buton.setStyle("-fx-background-color: #FFF;-fx-border-color: #CCC; -fx-spacing:3; -fx-border-width:1px 0px 5px 0px");
    }
}
