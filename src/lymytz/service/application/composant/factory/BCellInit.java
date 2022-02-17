/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.composant.factory;

import java.util.Date;
import javafx.event.ActionEvent;
import lymytz.service.application.MyComptesController;
import lymytz.service.application.bean.Planning;

/**
 *
 * @author LENOVO
 */
public class BCellInit extends AbstractButtonCell<Planning> {

    MyComptesController mainContoler;
    private Date date;

    public BCellInit(String text) {
        super(text);
    }

    public BCellInit(String text, MyComptesController appsc) {
        this(text);
        mainContoler = appsc;
    }

    public void setMainContoler(MyComptesController mainContoler) {
        this.mainContoler = mainContoler;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public void listenEvent(ActionEvent ev, Planning bean) {
        mainContoler.mainControler.displayPropertiesFiche(mainContoler.createNewFicheFromCreneaux(bean.getId(), date));
    }

}
