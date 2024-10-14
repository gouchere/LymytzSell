/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application.composant.factory;

import javafx.event.ActionEvent;
import com.lymytz.lymytzsell.service.application.bean.HeaderDoc;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;

/**
 *
 * @author LENOVO
 */
public class BCellClose extends AbstractButtonCell<HeaderDoc> {

    HomeCaisseController mainControler;

    public BCellClose(String text) {
        super(text);
    }

    public BCellClose(String text, HomeCaisseController main) {
        this(text);
        mainControler = main;
    }

    public void setMainControler(HomeCaisseController mainControler) {
        this.mainControler = mainControler;
    }

    @Override
    public void listenEvent(ActionEvent ev, HeaderDoc object) {
        mainControler.opnClotureFiche(object.getId());
    }

}
