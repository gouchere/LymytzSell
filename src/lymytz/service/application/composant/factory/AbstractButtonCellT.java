/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.composant.factory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TreeTableCell;

/**
 *
 * @author LENOVO
 * @param <T>
 */
public abstract class AbstractButtonCellT<T> extends TreeTableCell<T, Long> {
     final Button cellButon = new Button();
    T item;

    public AbstractButtonCellT(String text) {   
        this.cellButon.setText(text);
        cellButon.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                item =(T) getTreeTableRow().getItem();
                listenEvent(event, item);
            }
        });
    }

    public abstract void listenEvent(ActionEvent ev, T object);

    @Override
    protected void updateItem(Long item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            setGraphic(cellButon);
        }
    }
}
