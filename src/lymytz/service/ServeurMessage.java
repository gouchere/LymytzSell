/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.standard.Severity;
import lymytz.dao.query.RQueryFactories;
import lymytz.service.utils.ConsUtil;
import lymytz.service.utils.log.LogFiles;

/**
 *
 * @author LYMYTZ
 */
public class ServeurMessage {

    private static String name;
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
            Logger.getLogger(ServeurMessage.class.getName()).log(Level.SEVERE, null, ex);
            LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
        }
    }

    public static void writeMessage(Boolean etat) {
        mesSockets.stream().forEach((s) -> {
            if (s!=null?s.isClosed():false) {
                try {
                    writter = new PrintWriter(s.getOutputStream());
                    writter.println(etat);
                    writter.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ServeurMessage.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                mesSockets.remove(s);
            }
        });
    }
}
