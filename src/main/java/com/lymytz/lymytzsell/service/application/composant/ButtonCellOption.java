    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application.composant;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import com.lymytz.lymytzsell.service.application.bean.ContentPanier;
import com.lymytz.lymytzsell.view.LocalLoader;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;

import java.util.Objects;

    /**
 *
 * @author LENOVO
 */
public class ButtonCellOption extends TableCell<ContentPanier, Integer> {

    final Button plus = new Button();
    final Button moins = new Button();
    HBox box;
    HomeCaisseController mainContoler;

    public ButtonCellOption(HomeCaisseController appc) {
        this.mainContoler = appc;
        ImageView im = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/plus.png"))));
        ImageView im1 = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/moins.png"))));
        im.setFitHeight(15);
        im.setFitWidth(15);
        im1.setFitHeight(15);
        im1.setFitWidth(15);
        plus.setGraphic(im);
        moins.setGraphic(im1);
        plus.getStyleClass().add("noStyle");
        moins.getStyleClass().add("noStyle");
        plus.setOnAction(event -> {
            getTableRow().getIndex();
        });
        box = new HBox(plus, moins);
    }

    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        if (getTableRow() != null) {
            getTableRow().getIndex();
            mainContoler.TAB_FACTURES.getSelectionModel().getSelectedItem();
        }
    }
}
