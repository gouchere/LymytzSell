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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lymytz.service.application.ManagedApplication;
import lymytz.service.application.bean.ContentPanier;
import lymytz.view.LocalLoader;

/**
 *
 * @author LENOVO
 */
public class ButtonCellDelete extends TableCell<ContentPanier, Integer> {

    final Button cellButon = new Button();
    ManagedApplication mainContoler;

    public ButtonCellDelete(ManagedApplication appc) {
        this.mainContoler = appc;
        ImageView im = new ImageView(new Image(LocalLoader.class.getResource("icones/x.png").toExternalForm()));
        im.setFitHeight(15);
        im.setFitWidth(15);
        cellButon.setGraphic(im);
        cellButon.getStyleClass().add("noStyle");
        cellButon.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
//                final ContentPanier item = (ContentPanier) getTableRow().getItem();
                final int numRow = getTableRow().getIndex();
//                mainContoler.deleteLineContenu(numRow);
            }
        });
    }

    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        if (getTableRow() != null) {
            final int numRow = getTableRow().getIndex();
//            Onglets tab = (Onglets) mainContoler.tabView.getSelectionModel().getSelectedItem();
//            if (!empty && numRow < tab.getContentsFacture().size()) {
//                setGraphic(cellButon);
//                cellButon.setVisible(true);
//            } else {
//                cellButon.setVisible(false);
//            }
        }
    }
}
