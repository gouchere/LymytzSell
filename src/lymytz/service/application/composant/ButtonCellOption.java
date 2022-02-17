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
import javafx.scene.layout.HBox;
import lymytz.service.application.bean.ContentPanier;
import lymytz.view.LocalLoader;
import lymytz.view.main.HomeCaisseController;

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
        ImageView im = new ImageView(new Image(LocalLoader.class.getResource("icones/plus.png").toExternalForm()));
        ImageView im1 = new ImageView(new Image(LocalLoader.class.getResource("icones/moins.png").toExternalForm()));
        im.setFitHeight(15);
        im.setFitWidth(15);
        im1.setFitHeight(15);
        im1.setFitWidth(15);
        plus.setGraphic(im);
        moins.setGraphic(im1);
        plus.getStyleClass().add("noStyle");
        moins.getStyleClass().add("noStyle");
        plus.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
//                final ContentPanier item = (ContentPanier) getTableRow().getItem();
                final int numRow = getTableRow().getIndex();
                mainContoler.incrementLinePanier(numRow, 1);
            }
        });
        moins.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
//                final ContentPanier item = (ContentPanier) getTableRow().getItem();
                final int numRow = getTableRow().getIndex();
                mainContoler.incrementLinePanier(numRow, -1);
            }
        });
        box = new HBox(plus, moins);
    }

    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        if (getTableRow() != null) {
            final int numRow = getTableRow().getIndex();
            Onglets tab = (Onglets) mainContoler.TAB_FACTURES.getSelectionModel().getSelectedItem(); 
//            if (!empty && numRow < tab.getContentsFacture().size()) {
//                setGraphic(box);
//                box.setVisible(true);
//            } else {
//                box.setVisible(false);
//            }
        }
    }

    private boolean indexExist(Onglets tab, int idx) {
        try {
//            tab.getContentsFacture().get(idx);
            return true;
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }
}
