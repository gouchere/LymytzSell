/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.view.component;

import java.util.Date;
import javafx.event.ActionEvent;
import com.lymytz.lymytzsell.view.controller.MyComptesController;
import com.lymytz.lymytzsell.service.application.bean.Planning;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author LENOVO
 */
@Setter
public class BCellInit extends AbstractButtonCell<Planning> {

    MyComptesController mainContoler;
    @Getter
    private Date date;

    public BCellInit(String text) {
        super(text);
    }

    @Override
    public void listenEvent(ActionEvent ev, Planning bean) {
        mainContoler.mainControler.displayPropertiesFiche(mainContoler.createNewFicheFromCreneaux(bean.getId(), null));
    }

}
