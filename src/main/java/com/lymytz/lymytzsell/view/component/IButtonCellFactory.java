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
public interface IButtonCellFactory {

    BCellActive getBCellActive(String text);

    BCellClose getBCellClose(String text);

    BCellInit getBCellInit(String text);

}
