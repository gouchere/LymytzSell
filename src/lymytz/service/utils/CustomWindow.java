/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.utils;

import javafx.stage.Stage;

/**
 *
 * @author LYMYTZ
 */
public class CustomWindow<T> {

    private T controller;
    Stage stage;

    public CustomWindow() {
    }

    public T getController() {
        return controller;
    }

    public void setController(T controller) {
        this.controller = controller;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
