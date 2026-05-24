/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.view.data;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import com.lymytz.lymytzsell.service.application.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author LYMYTZ
 */
public class ReadLogController implements Initializable, Controller {

    @FXML
    TextArea TEXT_LOG;
    @FXML
    Label PATH;
    @FXML
    ProgressIndicator PROGRESS;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PROGRESS.setVisible(true);
        ReadTextLog task = new ReadTextLog();
        task.setOnSucceeded((WorkerStateEvent event) -> {
            String text = task.getValue();
            Platform.runLater(() -> {
                TEXT_LOG.setText(text);
                PROGRESS.setVisible(false);
            });
        });
        new Thread(task).start();
    }

    @Override
    public void freeMemoryController() {
    }

    public class ReadTextLog extends Task<String> {

        public ReadTextLog() {
            super();
        }

        @Override
        protected String call() throws Exception {
            BufferedReader br = null;
            try {
                String line = "";
                String texte = "";
                File f = new File(System.getProperty("user.home") + "/lymytz-sell/logs/exception.log");
                br = new BufferedReader(new FileReader(f));
                int progress = 0;
                while ((line = br.readLine()) != null) {
                    texte += line + "\n";
                    progress++;
                    this.updateProgress(progress, 0);
                    this.updateMessage("Lecture du fichier en cour..." + progress);
                }
                PATH.setText(f.getAbsolutePath());
                return texte;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ReadLogController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ReadLogController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(ReadLogController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return "";
        }

    }

}
