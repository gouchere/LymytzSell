/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.composant;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import lymytz.service.application.ManagedApplication;
import lymytz.service.application.bean.Planning;

/**
 *
 * @author LENOVO
 */
public class ButtonCell extends TableCell<Planning, Long> {

    final Button cellButon = new Button();
    ManagedApplication mainContoler;

    public ButtonCell(ManagedApplication appc, String text) {
        this.cellButon.setText(text);
        this.mainContoler = appc;
        cellButon.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                final Planning item = (Planning) getTableRow().getItem();
                final Long id = item.getId();
//                mainContoler.createNewFicheFromCreneaux(id,null);
            }
        });
    }

    @Override
    protected void updateItem(Long item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            setGraphic(cellButon);
        }
    }
}
