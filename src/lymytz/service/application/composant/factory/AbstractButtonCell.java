/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.composant.factory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

/**
 *
 * @author LENOVO
 */
public abstract class AbstractButtonCell<T> extends TableCell<T, Long> {

    final Button cellButon = new Button();
    T item;

    public AbstractButtonCell(String text) {
        this.cellButon.setText(text);
        cellButon.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                item =(T) getTableRow().getItem();
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
