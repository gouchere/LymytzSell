package lymytz.service.application.composant.date;


import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.HijrahChronology;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.control.skin.DatePickerContent;
import java.time.LocalDateTime;

public class DateTimePickerSkin extends ComboBoxPopupControl<LocalDateTime> {

    private DateTimePicker datePicker;
    private TextField displayNode;
    private DateTimePickerContent datePickerContent;

    public DateTimePickerSkin(final DateTimePicker datePicker) {
        super(datePicker, new DateTimePickerBehavior(datePicker));

        this.datePicker = datePicker;

        // The "arrow" is actually a rectangular svg icon resembling a calendar.
        // Round the size of the icon to whole integers to get sharp edges.
        arrow.paddingProperty().addListener(new InvalidationListener() {
            // This boolean protects against unwanted recursion.
            private boolean rounding = false;
            @Override public void invalidated(Observable observable) {
                if (!rounding) {
                    Insets padding = arrow.getPadding();
                    Insets rounded = new Insets(Math.round(padding.getTop()), Math.round(padding.getRight()),
                                                Math.round(padding.getBottom()), Math.round(padding.getLeft()));
                    if (!rounded.equals(padding)) {
                        rounding = true;
                        arrow.setPadding(rounded);
                        rounding = false;
                    }
                }
            }
        });

        registerChangeListener(datePicker.chronologyProperty(), "CHRONOLOGY");
        registerChangeListener(datePicker.converterProperty(), "CONVERTER");
        registerChangeListener(datePicker.dayCellFactoryProperty(), "DAY_CELL_FACTORY");
        registerChangeListener(datePicker.showWeekNumbersProperty(), "SHOW_WEEK_NUMBERS");
        registerChangeListener(datePicker.valueProperty(), "VALUE");
    }

    @Override public Node getPopupContent() {
        if (datePickerContent == null) {
            if (datePicker.getChronology() instanceof HijrahChronology) {
                datePickerContent = new DateTimePickerContent(datePicker);
            } else {
                datePickerContent = new DateTimePickerContent(datePicker);
            }
        }

        return datePickerContent;
    }

    @Override protected double computeMinWidth(double height,
                                               double topInset, double rightInset,
                                               double bottomInset, double leftInset) {
        return 50;
    }

    @Override protected void focusLost() {
        // do nothing
    }


    @Override public void show() {
        super.show();
        datePickerContent.clearFocus();
    }

    @Override protected void handleControlPropertyChanged(String p) {

        if ("CHRONOLOGY".equals(p) ||
            "DAY_CELL_FACTORY".equals(p)) {

            updateDisplayNode();
//             if (datePickerContent != null) {
//                 datePickerContent.refresh();
//             }
            datePickerContent = null;
            popup = null;
        } else if ("CONVERTER".equals(p)) {
            updateDisplayNode();
        } else if ("EDITOR".equals(p)) {
            getEditableInputNode();
        } else if ("SHOWING".equals(p)) {
            if (datePicker.isShowing()) {
                if (datePickerContent != null) {
                    LocalDateTime date = datePicker.getValue();
                    datePickerContent.displayedYearMonthProperty().set((date != null) ? TYearMonth.from(date) : TYearMonth.now());
                    datePickerContent.updateValues();
                }
                show();
            } else {
                hide();
            }
        } else if ("SHOW_WEEK_NUMBERS".equals(p)) {
            if (datePickerContent != null) {
                datePickerContent.updateGrid();
                datePickerContent.updateWeeknumberDateCells();
            }
        } else if ("VALUE".equals(p)) {
            updateDisplayNode();
            if (datePickerContent != null) {
                LocalDateTime date = datePicker.getValue();
                datePickerContent.displayedYearMonthProperty().set((date != null) ? TYearMonth.from(date) : TYearMonth.now());
                datePickerContent.updateValues();
            }
            datePicker.fireEvent(new ActionEvent());
        } else {
            super.handleControlPropertyChanged(p);
        }
    }

    @Override protected TextField getEditor() {
        // Use getSkinnable() here because this method is called from
        // the super constructor before datePicker is initialized.
        return ((DateTimePicker)getSkinnable()).getEditor();
    }

    @Override protected StringConverter<LocalDateTime> getConverter() {
        return ((DateTimePicker)getSkinnable()).getConverter();
    }

    @Override public Node getDisplayNode() {
        if (displayNode == null) {
            displayNode = getEditableInputNode();
            displayNode.getStyleClass().add("date-picker-display-node");
            updateDisplayNode();
        }
        displayNode.setEditable(datePicker.isEditable());

        return displayNode;
    }

    public void syncWithAutoUpdate() {
        if (!getPopup().isShowing() && datePicker.isShowing()) {
            // Popup was dismissed. Maybe user clicked outside or typed ESCAPE.
            // Make sure DatePicker button is in sync.
            datePicker.hide();
        }
    }
}
