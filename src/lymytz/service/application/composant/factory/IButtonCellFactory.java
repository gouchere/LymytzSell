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
public interface IButtonCellFactory {

    public BCellActive getBCellActive(String text);

    public BCellClose getBCellClose(String text);

    public BCellClose getBCellDelete(String text);

    public BCellInit getBCellInit(String text);

    public BCellValidStock getBCellValidStock(String text);
}
