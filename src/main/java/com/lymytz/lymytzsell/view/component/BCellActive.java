/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.view.component;

import javafx.event.ActionEvent;
import com.lymytz.lymytzsell.view.controller.MyComptesController;
import com.lymytz.lymytzsell.service.application.bean.HeaderDoc;
import com.lymytz.lymytzsell.view.controller.HomeCaisseController;
import lombok.Setter;

/**
 *
 * @author LENOVO
 */
@Setter
public class BCellActive extends AbstractButtonCell<HeaderDoc> {

    MyComptesController compteController;
    HomeCaisseController mainControler;

    public BCellActive(String text) {
        super(text);
    }

    @Override
    public void listenEvent(ActionEvent ev, HeaderDoc object) {
        compteController.changeHeader(object.getId(), compteController);
    }

    public void setMainControler(HomeCaisseController mainControler) {
        this.mainControler = mainControler;
    }

    public void setCompteController(MyComptesController compteController) {
        this.compteController = compteController;
    }

}
