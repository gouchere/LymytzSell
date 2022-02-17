/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.composant.factory;

/**
 *
 * @author LENOVO
 * @param <T>
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
    public BCellClose getBCellDelete(String text) {
        return new BCellClose(text);
    }

    @Override
    public BCellInit getBCellInit(String text) {
        return new BCellInit(text);
    }

    @Override
    public BCellValidStock getBCellValidStock(String text) {
        return new BCellValidStock(text);
    }

}
