/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.utils;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author LYMYTZ
 */
@Setter
@Getter
public class CustomWindow<T> {

    private T controller;
    Stage stage;

    public CustomWindow() {
        //do nothing
    }

}
