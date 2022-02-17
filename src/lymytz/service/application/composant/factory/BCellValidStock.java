/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.composant.factory;

import javafx.event.ActionEvent;
import lymytz.service.application.ManagedApplication;
import lymytz.service.application.MyComptesController;
import lymytz.service.application.bean.ContenuDocStock;

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
