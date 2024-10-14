/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.view.component;

import com.lymytz.lymytzsell.view.LocalLoader;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.UtilsProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 *
 * @author LYMYTZ
 */
public class LoaderImage extends Task<ImageView> {

    private String photo;

    public LoaderImage(String photo) {
        this.photo = photo;
    }

    @Override
    protected ImageView call() throws Exception {
        ImageView imgV = new ImageView();
        Image img = null;
        try {
            if (Constantes.asString(this.photo)) {
                String path = UtilsProject.properties.getProperty(Constantes.KEY_PATH);
                if (Constantes.asString(path)) {
                    File f = new File(path + "\\" + photo);
                    if (f.exists()) {
                        img = new Image(new FileInputStream(f));
                    } else {
                        img = new Image(LocalLoader.class.getResourceAsStream("/icones/coffee.png"));
                    }
                } else {
                    img = new Image(LocalLoader.class.getResourceAsStream("/icones/coffee.png"));
                }
            } else {
                img = new Image(LocalLoader.class.getResourceAsStream("/icones/coffee.png"));
            }
        } catch (FileNotFoundException ex) {
//            File f = new File(LocalLoader.class.getResourceAsStream("/icones/coffee.png"));  
            img = new Image(LocalLoader.class.getResourceAsStream("/icones/coffee.png"));
        }
        imgV.setImage(img);
        imgV.setFitHeight(110);
        imgV.setFitWidth(110);
        imgV.setPreserveRatio(true);
        imgV.setStyle("-fx-background-color: #FFF");
        return imgV;
    }

}
