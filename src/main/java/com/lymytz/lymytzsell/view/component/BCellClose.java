/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.view.component;

import javafx.event.ActionEvent;
import com.lymytz.lymytzsell.service.application.bean.HeaderDoc;
import com.lymytz.lymytzsell.view.controller.HomeCaisseController;
import lombok.Setter;

/**
 *
 * @author LENOVO
 */
@Setter
public class BCellClose extends AbstractButtonCell<HeaderDoc> {

    HomeCaisseController mainControler;

    public BCellClose(String text) {
        super(text);
    }


    @Override
    public void listenEvent(ActionEvent ev, HeaderDoc object) {
        mainControler.opnClotureFiche(object.getId());
    }

}
