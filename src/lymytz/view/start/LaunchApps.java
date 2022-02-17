/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.view.start;

import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author LENOVO
 */
public class LaunchApps extends Preloader {
    

    private Stage preloaderStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.preloaderStage = primaryStage;

        VBox loading = new VBox(20);
//        loading.setMaxWidth(Region.USE_PREF_SIZE);
//        loading.setMaxHeight(Region.USE_PREF_SIZE);
        loading.getChildren().add(new Label("WELCOME TO LYMYTZ CAISSE-SOLUTION"));
        loading.getChildren().add(new ProgressBar());
        loading.getChildren().add(new Label("Please wait..."));
        loading.setStyle("-fx-background-color: cornflowerblue");
        loading.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane();
        HBox head=new HBox();
        head.setPrefWidth(300);
        head.setPrefHeight(30);
        head.setStyle("-fx-background-color:#000000");
        HBox foot=new HBox();
        foot.setStyle("-fx-background-color:#000000");
        foot.setPrefWidth(300);
        foot.setPrefHeight(30);
        root.setBottom(foot);
        root.setTop(head);
        root.setCenter(loading);
        Scene scene = new Scene(root);

        primaryStage.setWidth(300);
        primaryStage.setHeight(300);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    @Override
    public void handleStateChangeNotification(Preloader.StateChangeNotification stateChangeNotification) {
        if (stateChangeNotification.getType() == Type.BEFORE_START) {
            preloaderStage.hide();
        }
    }

}
