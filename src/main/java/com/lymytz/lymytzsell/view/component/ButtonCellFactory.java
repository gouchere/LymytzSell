/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.view.component;

/**
 *
 * @author LENOVO
 */
public class ButtonCellFactory implements IButtonCellFactory {

    @Override
    public BCellActive getBCellActive(String text) {
        return new BCellActive(text);
    }

    @Override
    public BCellClose getBCellClose(String text) {
        return new BCellClose(text);
    }

    @Override
    public BCellInit getBCellInit(String text) {
        return new BCellInit(text);
    }


}
