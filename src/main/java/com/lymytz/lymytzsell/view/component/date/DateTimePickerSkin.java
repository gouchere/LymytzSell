package com.lymytz.lymytzsell.view.component.date;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxPopupControl;
import javafx.scene.layout.Region;
import javafx.util.StringConverter;

import java.time.LocalDateTime;

public class DateTimePickerSkin extends ComboBoxPopupControl<LocalDateTime> {
    private final DateTimePicker dateTimePicker;
    private TextField displayNode;
    private DateTimePickerContent datePickerContent;
    private final DateTimePickerBehavior behavior;
    private Region arrow;
    private TextField textField;
    private String initialTextFieldValue = null;
    private final ComboBoxBase<LocalDateTime> comboBoxBase;

    public DateTimePickerSkin(DateTimePicker var1) {
        super(var1);
        this.comboBoxBase=var1;
        this.dateTimePicker = var1;
        this.behavior = new DateTimePickerBehavior(var1);
        this.arrow = new Region();
        this.arrow.setFocusTraversable(false);
        this.arrow.getStyleClass().setAll(new String[]{"arrow"});
        this.arrow.setId("arrow");
        this.arrow.setMaxWidth(Double.NEGATIVE_INFINITY);
        this.arrow.setMaxHeight(Double.NEGATIVE_INFINITY);
        this.arrow.setMouseTransparent(true);
        this.arrow.paddingProperty().addListener(new InvalidationListener() {
            private boolean rounding = false;

            public void invalidated(Observable var1) {
                if (!this.rounding) {
                    Insets var2 = arrow.getPadding();
                    Insets var3 = new Insets((double)Math.round(var2.getTop()), (double)Math.round(var2.getRight()), (double)Math.round(var2.getBottom()), (double)Math.round(var2.getLeft()));
                    if (!var3.equals(var2)) {
                        this.rounding = true;
                        arrow.setPadding(var3);
                        this.rounding = false;
                    }
                }

            }
        });
        this.registerChangeListener(var1.chronologyProperty(), (var1x) -> {
            this.updateDisplayNode();
            this.datePickerContent = null;
            //this.popup = null;
        });
        this.registerChangeListener(var1.converterProperty(), (var1x) -> {
            this.updateDisplayNode();
        });
        this.registerChangeListener(var1.dayCellFactoryProperty(), (var1x) -> {
            this.updateDisplayNode();
            this.datePickerContent = null;
            //this.popup = null;
        });
        this.registerChangeListener(var1.showWeekNumbersProperty(), (var1x) -> {
            if (this.datePickerContent != null) {
                this.datePickerContent.updateGrid();
                this.datePickerContent.updateWeeknumberDateCells();
            }

        });
        this.registerChangeListener(var1.valueProperty(), (var2) -> {
            this.updateDisplayNode();
            if (this.datePickerContent != null) {
                LocalDateTime var3 = var1.getValue();
                this.datePickerContent.displayedYearMonthProperty().set(var3 != null ? TYearMonth.from(var3) : TYearMonth.now());
                this.datePickerContent.updateValues();
            }

            var1.fireEvent(new ActionEvent());
        });
        this.registerChangeListener(var1.showingProperty(), (var2) -> {
            if (var1.isShowing()) {
                if (this.datePickerContent != null) {
                    LocalDateTime var3 = var1.getValue();
                    this.datePickerContent.displayedYearMonthProperty().set(var3 != null ? TYearMonth.from(var3) : TYearMonth.now());
                    this.datePickerContent.updateValues();
                }

                this.show();
            } else {
                this.hide();
            }

        });
        if (var1.isShowing()) {
            this.show();
        }

    }

    public void dispose() {
        super.dispose();
        if (this.behavior != null) {
            this.behavior.dispose();
        }

    }

    public Node getPopupContent() {
        if (this.datePickerContent == null) {
            if (this.dateTimePicker.getChronology() instanceof DateTimePickerContent) {
                this.datePickerContent = new DateTimePickerContent(this.dateTimePicker);
            } else {
                this.datePickerContent = new DateTimePickerContent(this.dateTimePicker);
            }
        }

        return this.datePickerContent;
    }

    protected double computeMinWidth(double var1, double var3, double var5, double var7, double var9) {
        return 50.0;
    }

    public void show() {
        super.show();
        this.datePickerContent.clearFocus();
    }

    protected TextField getEditor() {
        return ((DateTimePicker)this.getSkinnable()).getEditor();
    }

    protected StringConverter<LocalDateTime> getConverter() {
        return ((DateTimePicker)this.getSkinnable()).getConverter();
    }

    public Node getDisplayNode() {
        if (this.displayNode == null) {
            this.displayNode = this.getEditableInputNode();
            this.displayNode.getStyleClass().add("date-picker-display-node");
            this.updateDisplayNode();
        }

        this.displayNode.setEditable(this.dateTimePicker.isEditable());
        return this.displayNode;
    }

    void focusLost() {
    }

    ComboBoxBaseBehavior getBehavior() {
        return this.behavior;
    }

    TextField getEditableInputNode() {
        if (this.textField == null && this.getEditor() != null) {
            this.textField = this.getEditor();
            this.textField.setFocusTraversable(false);
            this.textField.promptTextProperty().bind(this.comboBoxBase.promptTextProperty());
            this.textField.tooltipProperty().bind(this.comboBoxBase.tooltipProperty());
            this.textField.getProperties().put("TextInputControlBehavior.disableForwardToParent", true);
            this.initialTextFieldValue = this.textField.getText();
        }

        return this.textField;
    }
    void updateDisplayNode() {
        if (this.textField != null && this.getEditor() != null) {
            Object var1 = this.comboBoxBase.getValue();
            StringConverter var2 = this.getConverter();
            if (this.initialTextFieldValue != null && !this.initialTextFieldValue.isEmpty()) {
                this.textField.setText(this.initialTextFieldValue);
                this.initialTextFieldValue = null;
            } else {
                String var3 = var2.toString(var1);
                if (var1 != null && var3 != null) {
                    if (!var3.equals(this.textField.getText())) {
                        this.textField.setText(var3);
                    }
                } else {
                    this.textField.setText("");
                }
            }
        }

    }
}

