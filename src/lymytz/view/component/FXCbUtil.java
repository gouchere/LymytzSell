/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.view.component;

/**
 *
 * @author LYMYTZ
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lymytz.dao.entity.YvsComClient;

public class FXCbUtil {

    public interface AutoCompleteComparator<T> {

        boolean matches(String typedText, T objectToCompare);
    }

    public static <T> void autoCompleteComboBoxPlus(ComboBox<T> comboBox, AutoCompleteComparator<T> comparatorMethod) {
        ObservableList<T> data = comboBox.getItems();

        comboBox.setEditable(true);
        comboBox.getEditor().focusedProperty().addListener(observable -> {
            if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
                System.err.println(" Lost focused With index... "+comboBox.getSelectionModel().getSelectedIndex());
            } else {
                comboBox.removeEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
                    //do nothing
                });
                System.err.println("......" + ((YvsComClient) getComboBoxValue(comboBox)).getNom_prenom());
                comboBox.getEditor().setText(((YvsComClient) getComboBoxValue(comboBox)).getCodeClient());
            }
        });
//        comboBox.addEventHandler(KeyEvent.KEY_PRESSED, t -> comboBox.hide());
        comboBox.getEditor().addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

            private boolean moveCaretToPos = false;
            private int caretPos;

            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case DOWN:
                        if (!comboBox.isShowing()) {
                            comboBox.show();
                        }
                    case UP:
                        caretPos = -1;
                        moveCaret(comboBox.getEditor().getText().length());
                        return;
                    case BACK_SPACE:
                    case DELETE:
                        moveCaretToPos = true;
                        caretPos = comboBox.getEditor().getCaretPosition();
                        break;
                    default:
                        System.err.println("Key Press... "+event.getCode());
                        break;
                }

//                if (KeyCode.RIGHT == event.getCode() || KeyCode.LEFT == event.getCode()
//                        || event.isControlDown() || KeyCode.HOME == event.getCode()
//                        || KeyCode.END == event.getCode() || KeyCode.TAB == event.getCode()) {
//                    return;
//                }

                final ObservableList<T> list = FXCollections.observableArrayList();
                for (T aData : data) {
                    if (shouldDataBeAddedToInput(aData)) {
                        list.add(aData);
                    }
                }
                final String text = comboBox.getEditor().getText();

                comboBox.setItems(list);
                comboBox.getEditor().setText(text);
                if (!moveCaretToPos) {
                    caretPos = -1;
                }
                moveCaret(text.length());
                if (!list.isEmpty()) {
                    comboBox.show();
                }
            }

            private boolean shouldDataBeAddedToInput(T aData) {
                return aData != null && comboBox.getEditor().getText() != null && comparatorMethod.matches(comboBox.getEditor().getText(), aData);
            }

//            private boolean inputStartsWith(T aData) {
//                final String dataValue = aData.toString().toLowerCase();
//                final String inputValue = comboBox.getEditor().getText().toLowerCase();
//                return dataValue.startsWith(inputValue);
//            }
//
//            private boolean inputContains(T aData) {
//                final String dataValue = aData.toString().toLowerCase();
//                final String inputValue = comboBox.getEditor().getText().toLowerCase();
//                return dataValue.contains(inputValue);
//            }

            private void moveCaret(int textLength) {
                if (-1 == caretPos) {
                    comboBox.getEditor().positionCaret(textLength);
                } else {
                    comboBox.getEditor().positionCaret(caretPos);
                }
                moveCaretToPos = false;
            }
        });
    }

    public static <T> T getComboBoxValue(ComboBox<T> comboBox) {
        if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
            return null;
        } else {
            return comboBox.getItems().get(comboBox.getSelectionModel().getSelectedIndex());
        }
    }

}
