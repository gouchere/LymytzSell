/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.composant.factory;

import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lymytz.service.application.bean.ContentPanier;
import lymytz.view.LocalLoader;
import lymytz.view.main.HomeCaisseController;

/**
 *
 * @author LENOVO
 */
public class BCellDelete extends AbstractButtonCell<ContentPanier> {

    HomeCaisseController mainControler;

    public BCellDelete(String text) {
        super(text);
    }

    public BCellDelete(String text, HomeCaisseController main) {
        this(text);
        ImageView im = new ImageView(new Image(LocalLoader.class.getResource("icones/x.png").toExternalForm()));
        im.setFitHeight(15);
        im.setFitWidth(15);
        cellButon.setGraphic(im);
        cellButon.getStyleClass().add("noStyle");
        mainControler = main;
    }

    @Override
    public void listenEvent(ActionEvent ev, ContentPanier object) {
        mainControler.opnClotureFiche(object.getIdContent());
    }

}
