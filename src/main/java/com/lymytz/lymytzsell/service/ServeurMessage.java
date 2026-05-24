/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lymytz.lymytzsell.dao.query.RQueryFactories;

/**
 *
 * @author LYMYTZ
 */
public class ServeurMessage {

    private static final Logger LOGGER = LogManager.getLogger(ServeurMessage.class);

    public static ServerSocket server;
    public static Socket socketServeur;
    public static PrintWriter writter;
    public static BufferedReader in;

    private static List<Socket> mesSockets;

    public ServeurMessage() {
        mesSockets = new ArrayList<>();
    }

    public static void initSocket() {
        mesSockets = new ArrayList<>();
        try {
            ServeurMessage.server = new ServerSocket(1124);
            // une boucle infini ici permet d'attendre la connexion de nouveaux client...
            while (true) {
                socketServeur = server.accept();
                mesSockets.add(socketServeur);
                new ServerThread(socketServeur, socketServeur.getLocalAddress().getHostName()).start();
                //demande l'état actuel du serveur
                Boolean b = RQueryFactories.pingServer();
                writeMessage(b);
            }
        } catch (IOException ex) {
            LOGGER.error("Erreur socket", ex);
            LOGGER.error("Erreur socket", ex);
        }
    }

    public static void writeMessage(Boolean etat) {
        mesSockets.forEach(s-> {
            if (s != null && s.isClosed()) {
                try {
                    writter = new PrintWriter(s.getOutputStream());
                    writter.println(etat);
                    writter.flush();
                } catch (IOException ex) {
                    LOGGER.error("Erreur socket", ex);
                }
            } else {
                mesSockets.remove(s);
            }
        });
    }
}
