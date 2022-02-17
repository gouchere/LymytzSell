/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.synchro;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import lymytz.dao.query.RQueryFactories;
import lymytz.service.ClientMessage;
import lymytz.service.ServeurMessage;
import lymytz.service.application.Controller;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.UtilsProject;
import lymytz.synchro.ws.WsSynchro;
import lymytz.view.main.HomeCaisseController;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class ControlServiceController implements Initializable, Controller {

    RQueryFactories dao = new RQueryFactories();
    HomeCaisseController page;
    SynchronizeDataOut serviceOut;
    SynchronizeDataIn serviceIn;
    @FXML
    public Button STOP_OUT;
    @FXML
    public Button LAUNCH_OUT;
    @FXML
    public Button PAUSE_OUT;
    @FXML
    public Button STOP_IN;
    @FXML
    public Button LAUNCH_IN;
    @FXML
    public Button PAUSE_IN;

    public ControlServiceController() {
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void initPage(HomeCaisseController page, SynchronizeDataOut service, SynchronizeDataIn serviceIn) {
        this.page = page;
        this.serviceOut = service;
        this.serviceIn = serviceIn;
        if (service != null) {
            if (service.isRunning()) {
                LAUNCH_OUT.setDisable(true);
                STOP_OUT.setDisable(false);
                PAUSE_OUT.setDisable(false);
            } else {
                LAUNCH_OUT.setDisable(false);
                STOP_OUT.setDisable(true);
                PAUSE_OUT.setDisable(true);
            }
        }
        if (serviceIn != null) {
            if (serviceIn.isRunning()) {
                LAUNCH_IN.setDisable(true);
                STOP_IN.setDisable(false);
                PAUSE_IN.setDisable(false);
            } else {
                LAUNCH_IN.setDisable(false);
                STOP_IN.setDisable(true);
                PAUSE_IN.setDisable(true);
            }
        }
    }

    @FXML
    private void launchServiceOut(ActionEvent event) {
        if (serviceOut != null && UtilsProject.REPLICATION && Constantes.APPS_MODE_BOTH.equals(UtilsProject.properties.getProperty(Constantes.KEY_MODE))) {
            this.serviceOut.restart();
            this.serviceOut.start();
            LAUNCH_OUT.setDisable(true);
            STOP_OUT.setDisable(false);
            PAUSE_OUT.setDisable(false);
            WsSynchro.runningOut = false;
        }
    }

    @FXML
    private void pauseServiceOut(ActionEvent event) {
        if (serviceOut != null && UtilsProject.REPLICATION) {
            this.serviceOut.cancel();
            LAUNCH_OUT.setDisable(false);
            STOP_OUT.setDisable(true);
            PAUSE_OUT.setDisable(true);
            WsSynchro.runningOut = true;
        }
    }

    @FXML
    private void stopServiceOut(ActionEvent event) {
        if (serviceOut != null && UtilsProject.REPLICATION) {
            this.serviceOut.cancel();
            LAUNCH_OUT.setDisable(false);
            STOP_OUT.setDisable(true);
            PAUSE_OUT.setDisable(true);
            WsSynchro.runningOut = true;
        }
    }

    @FXML
    private void launchServiceIn(ActionEvent event) {
        if (serviceIn != null && UtilsProject.REPLICATION) {
            this.serviceIn.restart();
            LAUNCH_IN.setDisable(true);
            STOP_IN.setDisable(false);
            PAUSE_IN.setDisable(false);
            WsSynchro.runningIn = false;
        }
    }

    @FXML
    private void pauseServiceIn(ActionEvent event) {
        if (serviceIn != null && UtilsProject.REPLICATION) {
            this.serviceIn.cancel();
            LAUNCH_IN.setDisable(false);
            STOP_IN.setDisable(true);
            PAUSE_IN.setDisable(true);
            WsSynchro.runningIn = true;
        }
    }

    @FXML
    private void stopServiceIn(ActionEvent event) {
        if (serviceIn != null && UtilsProject.REPLICATION) {
            this.serviceIn.cancel();
            LAUNCH_IN.setDisable(false);
            STOP_IN.setDisable(true);
            PAUSE_IN.setDisable(true);
            WsSynchro.runningIn = true;
        }
    }

    @Override
    public void freeMemoryController() {

    }

    @FXML
    private void invokeClient1(ActionEvent event) {
//        Thread t = new Thread(() -> {
//            ClientMessage msg = new ClientMessage("Client 1");
//            msg.initClient();
//        });
//        t.start();
    }

    @FXML
    private void invokeClient2(ActionEvent event) {
//        Thread t = new Thread(() -> {
//            ClientMessage msg = new ClientMessage("Client 2");
//            msg.initClient();
//        });
//        t.start();
    }

    @FXML
    private void invokeClient3(ActionEvent event) {
//        Thread t = new Thread(() -> {
//            ClientMessage msg = new ClientMessage("Client 3");
//            msg.initClient();
//        });
//        t.start();
    }

    @FXML
    private void invokeClient4(ActionEvent event) {
        Thread t = new Thread(() -> {
            ServeurMessage.writeMessage(Boolean.TRUE);
        });
        t.start();
    }

}
