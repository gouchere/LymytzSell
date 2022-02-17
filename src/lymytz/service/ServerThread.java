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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LYMYTZ
 */
public class ServerThread extends Thread {

    String message;
    String clientName;
    BufferedReader reader;
    PrintWriter writter;
    Socket socket;

    public ServerThread(Socket socket, String clientName) {
        this.socket = socket;
        this.clientName = clientName;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writter = new PrintWriter(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            message=reader.readLine();
            while (message != null ? message.equals("QUIT") : false) {
                writter.println(message);
                writter.flush();
                message = reader.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writter != null) {
                    writter.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     public void writeMessage(Boolean etat) {
        //transmet l'état actuel du serveur distant
        writter.println(etat.toString());
        writter.flush();
//            socketServeur.close();
//            server.close();
    }
}
