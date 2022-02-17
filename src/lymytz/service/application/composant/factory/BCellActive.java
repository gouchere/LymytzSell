/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.composant.factory;

import javafx.event.ActionEvent;
import lymytz.service.application.MyComptesController;
import lymytz.service.application.bean.HeaderDoc;
import lymytz.view.main.HomeCaisseController;

/**
 *
 * @author LENOVO
 */
public class BCellActive extends AbstractButtonCell<HeaderDoc> {

    HomeCaisseController mainControler;
    MyComptesController compteController;

    public BCellActive(String text) {
        super(text);
    }

    public BCellActive(String text, HomeCaisseController main) {
        this(text);
        mainControler = main;
    }

    public void setMainControler(HomeCaisseController mainControler) {
        this.mainControler = mainControler;
    }

    public void setCompteController(MyComptesController compteController) {
        this.compteController = compteController;
    }

    @Override
    public void listenEvent(ActionEvent ev, HeaderDoc object) {
        compteController.changeHeader(object.getId(),compteController);
    }

}
