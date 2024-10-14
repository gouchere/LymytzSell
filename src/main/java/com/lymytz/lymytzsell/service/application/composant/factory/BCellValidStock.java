/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application.composant.factory;

import javafx.event.ActionEvent;
import com.lymytz.lymytzsell.service.application.ManagedApplication;
import com.lymytz.lymytzsell.service.application.MyComptesController;
import com.lymytz.lymytzsell.service.application.bean.ContenuDocStock;

/**
 *
 * @author LENOVO
 */
public class BCellValidStock extends AbstractButtonCellT<ContenuDocStock> {

    ManagedApplication mainControler;
    MyComptesController compteController;

    public BCellValidStock(String text) {
        super(text);
    }

    public BCellValidStock(String text, ManagedApplication appsc) {
        this(text);
        mainControler = appsc;
    }

    public void setMainContoler(ManagedApplication mainContoler) {
        this.mainControler = mainContoler;
    }

    public MyComptesController getCompteController() {
        return compteController;
    }

    public void setCompteController(MyComptesController compteController) {
        this.compteController = compteController;
    }

    @Override
    public void listenEvent(ActionEvent ev, ContenuDocStock bean) {
//        compteController.valideReceptionTransfert(bean.getId(), bean.isDisplayButtonOption());
    }

}
