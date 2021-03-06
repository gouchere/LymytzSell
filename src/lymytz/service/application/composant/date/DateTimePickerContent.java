/*
 * Copyright (c) 2013, 2015, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package lymytz.service.application.composant.date;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.chrono.Chronology;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WeakChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;

import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.traversal.Direction;

import static com.sun.javafx.PlatformUtil.*;
import com.sun.javafx.scene.control.skin.Utils;
import java.time.LocalDateTime;

/**
 * The full content for the DatePicker popup. This class could
 * probably be used more or less as-is with an embeddable type of date
 * picker that doesn't use a popup.
 */
public class DateTimePickerContent extends VBox {
    protected DateTimePicker datePicker;
    private Button backMonthButton;
    private Button forwardMonthButton;
    private Button backYearButton;
    private Button forwardYearButton;
    private Label monthLabel;
    private Label yearLabel;
    protected GridPane gridPane;

    private int daysPerWeek;
    private List<DateTimeCell> dayNameCells = new ArrayList<DateTimeCell>();
    private List<DateTimeCell> weekNumberCells = new ArrayList<DateTimeCell>();
    protected List<DateTimeCell> dayCells = new ArrayList<DateTimeCell>();
    private LocalDateTime[] dayCellDates;
    private DateTimeCell lastFocusedDayCell = null;

    final DateTimeFormatter monthFormatter =
        DateTimeFormatter.ofPattern("MMMM");

    final DateTimeFormatter monthFormatterSO =
            DateTimeFormatter.ofPattern("LLLL"); // Standalone month name

    final DateTimeFormatter yearFormatter =
        DateTimeFormatter.ofPattern("y");

    final DateTimeFormatter yearWithEraFormatter =
        DateTimeFormatter.ofPattern("GGGGy"); // For Japanese. What to use for others??

    final DateTimeFormatter weekNumberFormatter =
        DateTimeFormatter.ofPattern("w");

    final DateTimeFormatter weekDayNameFormatter =
            DateTimeFormatter.ofPattern("ccc"); // Standalone day name

    final DateTimeFormatter dayCellFormatter =
        DateTimeFormatter.ofPattern("d");

    static String getString(String key) {
        return ControlResources.getString("DatePicker."+key);
    }

    DateTimePickerContent(final DateTimePicker datePicker) {
        this.datePicker = datePicker;

        getStyleClass().add("date-picker-popup");

        daysPerWeek = getDaysPerWeek();

        {
            LocalDateTime date = datePicker.getValue();
            displayedYearMonth.set((date != null) ? TYearMonth.from(date) : TYearMonth.now());
        }

        displayedYearMonth.addListener((observable, oldValue, newValue) -> {
            updateValues();
        });


        getChildren().add(createMonthYearPane());

        gridPane = new GridPane() {
            @Override protected double computePrefWidth(double height) {
                final double width = super.computePrefWidth(height);

                // RT-30903: Make sure width snaps to pixel when divided by
                // number of columns. GridPane doesn't do this with percentage
                // width constraints. See GridPane.adjustColumnWidths().
                final int nCols = daysPerWeek + (datePicker.isShowWeekNumbers() ? 1 : 0);
                final double snaphgap = snapSpace(getHgap());
                final double left = snapSpace(getInsets().getLeft());
                final double right = snapSpace(getInsets().getRight());
                final double hgaps = snaphgap * (nCols - 1);
                final double contentWidth = width - left - right - hgaps;
                return ((snapSize(contentWidth / nCols)) * nCols) + left + right + hgaps;
            }

            @Override protected void layoutChildren() {
                // Prevent AssertionError in GridPane
                if (getWidth() > 0 && getHeight() > 0) {
                    super.layoutChildren();
                }
            }
        };
        gridPane.setFocusTraversable(true);
        gridPane.getStyleClass().add("calendar-grid");
        gridPane.setVgap(-1);
        gridPane.setHgap(-1);

        // Add a focus owner listener to Scene when it becomes available.
        final WeakChangeListener<Node> weakFocusOwnerListener =
            new WeakChangeListener<Node>((ov2, oldFocusOwner, newFocusOwner) -> {
                if (newFocusOwner == gridPane) {
                    if (oldFocusOwner instanceof DateCell) {
                        // Backwards traversal, skip gridPane.
                        gridPane.impl_traverse(Direction.PREVIOUS);
                    } else {
                        // Forwards traversal, pass focus to day cell.
                        if (lastFocusedDayCell != null) {
                            Platform.runLater(() -> {
                                lastFocusedDayCell.requestFocus();
                            });
                        } else {
                            clearFocus();
                        }
                    }
                }
            });
        gridPane.sceneProperty().addListener(new WeakChangeListener<Scene>((ov, oldScene, newScene) -> {
            if (oldScene != null) {
                oldScene.focusOwnerProperty().removeListener(weakFocusOwnerListener);
            }
            if (newScene != null) {
                newScene.focusOwnerProperty().addListener(weakFocusOwnerListener);
            }
        }));
        if (gridPane.getScene() != null) {
            gridPane.getScene().focusOwnerProperty().addListener(weakFocusOwnerListener);
        }

        // get the weekday labels starting with the weekday that is the
        // first-day-of-the-week according to the locale in the
        // displayed LocalDate
        for (int i = 0; i < daysPerWeek; i++) {
            DateTimeCell cell = new DateTimeCell();
            cell.getStyleClass().add("day-name-cell");
            dayNameCells.add(cell);
        }

        // Week number column
        for (int i = 0; i < 6; i++) {
            DateTimeCell cell = new DateTimeCell();
            cell.getStyleClass().add("week-number-cell");
            weekNumberCells.add(cell);
        }

        createDayCells();
        updateGrid();
        getChildren().add(gridPane);

        refresh();

        // RT-30511: This prevents key events from reaching the popup's owner.
        addEventHandler(KeyEvent.ANY, e -> {
            Node node = getScene().getFocusOwner();
            if (node instanceof DateTimeCell) {
                lastFocusedDayCell = (DateTimeCell)node;
            }

            if (e.getEventType() == KeyEvent.KEY_PRESSED) {
                switch (e.getCode()) {
                  case HOME:
                      goToDate(LocalDateTime.now(), true);
                      e.consume();
                      break;


                  case PAGE_UP:
                      if ((isMac() && e.isMetaDown()) || (!isMac() && e.isControlDown())) {
                          if (!backYearButton.isDisabled()) {
                              forward(-1, YEARS, true);
                          }
                      } else {
                          if (!backMonthButton.isDisabled()) {
                              forward(-1, MONTHS, true);
                          }
                      }
                      e.consume();
                      break;

                  case PAGE_DOWN:
                      if ((isMac() && e.isMetaDown()) || (!isMac() && e.isControlDown())) {
                          if (!forwardYearButton.isDisabled()) {
                              forward(1, YEARS, true);
                          }
                      } else {
                          if (!forwardMonthButton.isDisabled()) {
                              forward(1, MONTHS, true);
                          }
                      }
                      e.consume();
                      break;
                }

                node = getScene().getFocusOwner();
                if (node instanceof DateTimeCell) {
                    lastFocusedDayCell = (DateTimeCell)node;
                }
            }

            // Consume all key events except those that control
            // showing the popup and traversal.
            switch (e.getCode()) {
              case F4:
              case F10:
              case UP:
              case DOWN:
              case LEFT:
              case RIGHT:
              case TAB:
                    break;

              case ESCAPE:
                datePicker.hide();
                e.consume();
                break;

              default:
                e.consume();
            }
        });
    }

    private ObjectProperty<TYearMonth> displayedYearMonth =
        new SimpleObjectProperty<TYearMonth>(this, "displayedYearMonth");

    ObjectProperty<TYearMonth> displayedYearMonthProperty() {
        return displayedYearMonth;
    }


    protected BorderPane createMonthYearPane() {
        BorderPane monthYearPane = new BorderPane();
        monthYearPane.getStyleClass().add("month-year-pane");

        // Month spinner

        HBox monthSpinner = new HBox();
        monthSpinner.getStyleClass().add("spinner");

        backMonthButton = new Button();
        backMonthButton.getStyleClass().add("left-button");

        forwardMonthButton = new Button();
        forwardMonthButton.getStyleClass().add("right-button");

        StackPane leftMonthArrow = new StackPane();
        leftMonthArrow.getStyleClass().add("left-arrow");
        leftMonthArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        backMonthButton.setGraphic(leftMonthArrow);

        StackPane rightMonthArrow = new StackPane();
        rightMonthArrow.getStyleClass().add("right-arrow");
        rightMonthArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        forwardMonthButton.setGraphic(rightMonthArrow);


        backMonthButton.setOnAction(t -> {
            forward(-1, MONTHS, false);
        });

        monthLabel = new Label();
        monthLabel.getStyleClass().add("spinner-label");

        forwardMonthButton.setOnAction(t -> {
            forward(1, MONTHS, false);
        });

        monthSpinner.getChildren().addAll(backMonthButton, monthLabel, forwardMonthButton);
        monthYearPane.setLeft(monthSpinner);

        // Year spinner

        HBox yearSpinner = new HBox();
        yearSpinner.getStyleClass().add("spinner");

        backYearButton = new Button();
        backYearButton.getStyleClass().add("left-button");

        forwardYearButton = new Button();
        forwardYearButton.getStyleClass().add("right-button");

        StackPane leftYearArrow = new StackPane();
        leftYearArrow.getStyleClass().add("left-arrow");
        leftYearArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        backYearButton.setGraphic(leftYearArrow);

        StackPane rightYearArrow = new StackPane();
        rightYearArrow.getStyleClass().add("right-arrow");
        rightYearArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        forwardYearButton.setGraphic(rightYearArrow);


        backYearButton.setOnAction(t -> {
            forward(-1, YEARS, false);
        });

        yearLabel = new Label();
        yearLabel.getStyleClass().add("spinner-label");

        forwardYearButton.setOnAction(t -> {
            forward(1, YEARS, false);
        });

        yearSpinner.getChildren().addAll(backYearButton, yearLabel, forwardYearButton);
yearSpinner.setFillHeight(false);
        monthYearPane.setRight(yearSpinner);

        return monthYearPane;
    }

    private void refresh() {
        updateMonthLabelWidth();
        updateDayNameCells();
        updateValues();
    }

    void updateValues() {
        // Note: Preserve this order, as DatePickerHijrahContent needs
        // updateDayCells before updateMonthYearPane().
        updateWeeknumberDateCells();
        updateDayCells();
        updateMonthYearPane();
    }

    void updateGrid() {
        gridPane.getColumnConstraints().clear();
        gridPane.getChildren().clear();

        int nCols = daysPerWeek + (datePicker.isShowWeekNumbers() ? 1 : 0);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100); // Treated as weight
        for (int i = 0; i < nCols; i++) {
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < daysPerWeek; i++) {
            gridPane.add(dayNameCells.get(i), i + nCols - daysPerWeek, 1);  // col, row
        }

        // Week number column
        if (datePicker.isShowWeekNumbers()) {
            for (int i = 0; i < 6; i++) {
                gridPane.add(weekNumberCells.get(i), 0, i + 2);  // col, row
            }
        }

        // setup: 6 rows of daysPerWeek (which is the maximum number of cells required in the worst case layout)
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < daysPerWeek; col++) {
                gridPane.add(dayCells.get(row*daysPerWeek+col), col + nCols - daysPerWeek, row + 2);
            }
        }
    }

    void updateDayNameCells() {
        // first day of week, 1 = monday, 7 = sunday
        int firstDayOfWeek = WeekFields.of(getLocale()).getFirstDayOfWeek().getValue();

        // july 13th 2009 is a Monday, so a firstDayOfWeek=1 must come out of the 13th
        LocalDate date = LocalDate.of(2009, 7, 12 + firstDayOfWeek);
        for (int i = 0; i < daysPerWeek; i++) {
            String name = weekDayNameFormatter.withLocale(getLocale()).format(date.plus(i, DAYS));
            dayNameCells.get(i).setText(titleCaseWord(name));
        }
    }

    void updateWeeknumberDateCells() {
        if (datePicker.isShowWeekNumbers()) {
            final Locale locale = getLocale();
            final int maxWeeksPerMonth = 6; // TODO: Get this from chronology?

            LocalDateTime firstOfMonth = displayedYearMonth.get().atDay(1);
            for (int i = 0; i < maxWeeksPerMonth; i++) {
                LocalDateTime date = firstOfMonth.plus(i, WEEKS);
                // Use a formatter to ensure correct localization,
                // such as when Thai numerals are required.
                String cellText =
                    weekNumberFormatter.withLocale(locale)
                                       .withDecimalStyle(DecimalStyle.of(locale))
                                       .format(date);
                weekNumberCells.get(i).setText(cellText);
            }
        }
    }

    void updateDayCells() {
        Locale locale = getLocale();
        Chronology chrono = getPrimaryChronology();
        int firstOfMonthIdx = determineFirstOfMonthDayOfWeek();
        TYearMonth curMonth = displayedYearMonth.get();

        // RT-31075: The following are now set in the try-catch block.
        TYearMonth prevMonth = null;
        TYearMonth nextMonth = null;
        int daysInCurMonth = -1;
        int daysInPrevMonth = -1;
        int daysInNextMonth = -1;

        for (int i = 0; i < 6 * daysPerWeek; i++) {
            DateTimeCell dayCell = dayCells.get(i);
            dayCell.getStyleClass().setAll("cell", "date-cell", "day-cell");
            dayCell.setDisable(false);
            dayCell.setStyle(null);
            dayCell.setGraphic(null);
            dayCell.setTooltip(null);

            try {
                if (daysInCurMonth == -1) {
                    daysInCurMonth = curMonth.lengthOfMonth();
                }
                TYearMonth month = curMonth;
                int day = i - firstOfMonthIdx + 1;
                //int index = firstOfMonthIdx + i - 1;
                if (i < firstOfMonthIdx) {
                    if (prevMonth == null) {
                        prevMonth = curMonth.minusMonths(1);
                        daysInPrevMonth = prevMonth.lengthOfMonth();
                    }
                    month = prevMonth;
                    day = i + daysInPrevMonth - firstOfMonthIdx + 1;
                    dayCell.getStyleClass().add("previous-month");
                } else if (i >= firstOfMonthIdx + daysInCurMonth) {
                    if (nextMonth == null) {
                        nextMonth = curMonth.plusMonths(1);
                        daysInNextMonth = nextMonth.lengthOfMonth();
                    }
                    month = nextMonth;
                    day = i - daysInCurMonth - firstOfMonthIdx + 1;
                    dayCell.getStyleClass().add("next-month");
                }
                LocalDateTime date = month.atDay(day);
                dayCellDates[i] = date;
                ChronoLocalDate cDate = chrono.date(date);

                dayCell.setDisable(false);

                if (isToday(date)) {
                    dayCell.getStyleClass().add("today");
                }

                if (date.equals(datePicker.getValue())) {
                    dayCell.getStyleClass().add("selected");
                }

                String cellText =
                    dayCellFormatter.withLocale(locale)
                                    .withChronology(chrono)
                                    .withDecimalStyle(DecimalStyle.of(locale))
                                    .format(cDate);
                dayCell.setText(cellText);

                dayCell.updateItem(date, false);
            } catch (DateTimeException ex) {
                // Date is out of range.
                // System.err.println(dayCellDate(dayCell) + " " + ex);
                dayCell.setText(" ");
                dayCell.setDisable(true);
            }
        }
    }

    private int getDaysPerWeek() {
        ValueRange range = getPrimaryChronology().range(DAY_OF_WEEK);
        return (int)(range.getMaximum() - range.getMinimum() + 1);
    }

    private int getMonthsPerYear() {
        ValueRange range = getPrimaryChronology().range(MONTH_OF_YEAR);
        return (int)(range.getMaximum() - range.getMinimum() + 1);
    }

    private void updateMonthLabelWidth() {
        if (monthLabel != null) {
            int monthsPerYear = getMonthsPerYear();
            double width = 0;
            for (int i = 0; i < monthsPerYear; i++) {
                TYearMonth yearMonth = displayedYearMonth.get().withMonth(i + 1);
                String name = monthFormatterSO.withLocale(getLocale()).format(yearMonth);
                if (Character.isDigit(name.charAt(0))) {
                    // Fallback. The standalone format returned a number, so use standard format instead.
                    name = monthFormatter.withLocale(getLocale()).format(yearMonth);
                }
//                width = Math.max(width, Utils.computeTextWidth(monthLabel.getFont(), name, 0));
            }
            monthLabel.setMinWidth(width);
        }
    }

    protected void updateMonthYearPane() {
        TYearMonth yearMonth = displayedYearMonth.get();
        String str = formatMonth(yearMonth);
        monthLabel.setText(str);

        str = formatYear(yearMonth);
        yearLabel.setText(str);
//        double width = Utils.computeTextWidth(yearLabel.getFont(), str, 0);
//        if (width > yearLabel.getMinWidth()) {
//            yearLabel.setMinWidth(width);
//        }

        Chronology chrono = datePicker.getChronology();
        LocalDateTime firstDayOfMonth = yearMonth.atDay(1);
        backMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, -1, DAYS));
        forwardMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, +1, MONTHS));
        backYearButton.setDisable(!isValidDate(chrono, firstDayOfMonth, -1, YEARS));
        forwardYearButton.setDisable(!isValidDate(chrono, firstDayOfMonth, +1, YEARS));
    }

    private String formatMonth(TYearMonth yearMonth) {
        Locale locale = getLocale();
        Chronology chrono = getPrimaryChronology();
        try {
            ChronoLocalDate cDate = chrono.date(yearMonth.atDay(1));

            String str = monthFormatterSO.withLocale(getLocale())
                                         .withChronology(chrono)
                                         .format(cDate);
            if (Character.isDigit(str.charAt(0))) {
                // Fallback. The standalone format returned a number, so use standard format instead.
                str = monthFormatter.withLocale(getLocale())
                                    .withChronology(chrono)
                                    .format(cDate);
            }
            return titleCaseWord(str);
        } catch (DateTimeException ex) {
            // Date is out of range.
            return "";
        }
    }

    private String formatYear(TYearMonth yearMonth) {
        Locale locale = getLocale();
        Chronology chrono = getPrimaryChronology();
        try {
            DateTimeFormatter formatter = yearFormatter;
            ChronoLocalDate cDate = chrono.date(yearMonth.atDay(1));
            int era = cDate.getEra().getValue();
            int nEras = chrono.eras().size();

            /*if (cDate.get(YEAR) < 0) {
                formatter = yearForNegYearFormatter;
            } else */
            if ((nEras == 2 && era == 0) || nEras > 2) {
                formatter = yearWithEraFormatter;
            }

            // Fixme: Format Japanese era names with Japanese text.
            String str = formatter.withLocale(getLocale())
                                  .withChronology(chrono)
                                  .withDecimalStyle(DecimalStyle.of(getLocale()))
                                  .format(cDate);

            return str;
        } catch (DateTimeException ex) {
            // Date is out of range.
            return "";
        }
    }

    // Ensures that month and day names are titlecased (capitalized).
    private String titleCaseWord(String str) {
        if (str.length() > 0) {
            int firstChar = str.codePointAt(0);
            if (!Character.isTitleCase(firstChar)) {
                str = new String(new int[] { Character.toTitleCase(firstChar) }, 0, 1) +
                      str.substring(Character.offsetByCodePoints(str, 0, 1));
            }
        }
        return str;
    }



    /**
     * determine on which day of week idx the first of the months is
     */
    private int determineFirstOfMonthDayOfWeek() {
        // determine with which cell to start
        int firstDayOfWeek = WeekFields.of(getLocale()).getFirstDayOfWeek().getValue();
        int firstOfMonthIdx = displayedYearMonth.get().atDay(1).getDayOfWeek().getValue() - firstDayOfWeek;
        if (firstOfMonthIdx < 0) {
            firstOfMonthIdx += daysPerWeek;
        }
        return firstOfMonthIdx;
    }

    private boolean isToday(LocalDateTime localDate) {
        return (localDate.equals(LocalDateTime.now()));
    }

    protected LocalDateTime dayCellDate(DateTimeCell dateCell) {
        assert (dayCellDates != null);
        return dayCellDates[dayCells.indexOf(dateCell)];
    }

    // public for behavior class
    public void goToDayCell(DateTimeCell dateCell, int offset, ChronoUnit unit, boolean focusDayCell) {
        goToDate(dayCellDate(dateCell).plus(offset, unit), focusDayCell);
    }

    protected void forward(int offset, ChronoUnit unit, boolean focusDayCell) {
        TYearMonth yearMonth = displayedYearMonth.get();
        DateTimeCell dateCell = lastFocusedDayCell;
        if (dateCell == null || !dayCellDate(dateCell).getMonth().equals(yearMonth.getMonth())) {
            dateCell = findDayCellForDate(yearMonth.atDay(1));
        }
        goToDayCell(dateCell, offset, unit, focusDayCell);
    }

    // public for behavior class
    public void goToDate(LocalDateTime date, boolean focusDayCell) {
        if (isValidDate(datePicker.getChronology(), date)) {
            displayedYearMonth.set(TYearMonth.from(date));
            if (focusDayCell) {
                findDayCellForDate(date).requestFocus();
            }
        }
    }

    // public for behavior class
    public void selectDayCell(DateTimeCell dateCell) {
        datePicker.setValue(dayCellDate(dateCell));
        datePicker.hide();
    }

    private DateTimeCell findDayCellForDate(LocalDateTime date) {
        for (int i = 0; i < dayCellDates.length; i++) {
            if (date.equals(dayCellDates[i])) {
                return dayCells.get(i);
            }
        }
        return dayCells.get(dayCells.size()/2+1);
    }

    void clearFocus() {
        LocalDateTime focusDate = datePicker.getValue();
        if (focusDate == null) {
            focusDate = LocalDateTime.now();
        }
        if (YearMonth.from(focusDate).equals(displayedYearMonth.get())) {
            // focus date
            goToDate(focusDate, true);
        } else {
            // focus month spinner (should not happen)
            backMonthButton.requestFocus();
        }

        // RT-31857
        if (backMonthButton.getWidth() == 0) {
            backMonthButton.requestLayout();
            forwardMonthButton.requestLayout();
            backYearButton.requestLayout();
            forwardYearButton.requestLayout();
        }
    }

    protected void createDayCells() {
        final EventHandler<MouseEvent> dayCellActionHandler = ev -> {
            if (ev.getButton() != MouseButton.PRIMARY) {
                return;
            }

            DateTimeCell dayCell = (DateTimeCell)ev.getSource();
            selectDayCell(dayCell);
            lastFocusedDayCell = dayCell;
        };

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < daysPerWeek; col++) {
                DateTimeCell dayCell = createDayCell();
                dayCell.addEventHandler(MouseEvent.MOUSE_CLICKED, dayCellActionHandler);
                dayCells.add(dayCell);
            }
        }

        dayCellDates = new LocalDateTime[6 * daysPerWeek];
    }

    private DateTimeCell createDayCell() {
        DateTimeCell cell = null;
        if (datePicker.getDayCellFactory() != null) {
            cell = datePicker.getDayCellFactory().call(datePicker);
        }
        if (cell == null) {
            cell = new DateTimeCell();
        }

        return cell;
    }

    protected Locale getLocale() {
        return Locale.getDefault(Locale.Category.FORMAT);
    }

    /**
     * The primary chronology for display. This may be overridden to
     * be different than the DatePicker chronology. For example
     * DatePickerHijrahContent uses ISO as primary and Hijrah as a
     * secondary chronology.
     */
    protected Chronology getPrimaryChronology() {
        return datePicker.getChronology();
    }

    protected boolean isValidDate(Chronology chrono, LocalDateTime date, int offset, ChronoUnit unit) {
        if (date != null) {
            try {
                return isValidDate(chrono, date.plus(offset, unit));
            } catch (DateTimeException ex) {
            }
        }
        return false;
    }

    protected boolean isValidDate(Chronology chrono, LocalDateTime date) {
        try {
            if (date != null) {
                chrono.date(date);
            }
            return true;
        } catch (DateTimeException ex) {
            return false;
        }
    }
}
