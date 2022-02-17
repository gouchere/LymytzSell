/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.composant.date;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleAttribute;
import static javafx.scene.AccessibleAttribute.TEXT;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Cell;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Control;
import javafx.scene.control.DateCell;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author LENOVO
 */
public class DateTimePicker extends ComboBoxBase<LocalDateTime> {

   private LocalDateTime lastValidDate = null;
    private Chronology lastValidChronology = IsoChronology.INSTANCE;

    /**
     * Creates a default DatePicker instance with a <code>null</code> date value set.
     */
    public DateTimePicker() {
        this(null);

        valueProperty().addListener(observable -> {
            LocalDateTime date = getValue();
            Chronology chrono = getChronology();

            if (validateDate(chrono, date)) {
                lastValidDate = date;
            } else {
                System.err.println("Restoring value to " +
                            ((lastValidDate == null) ? "null" : getConverter().toString(lastValidDate)));
                setValue(lastValidDate);
            }
        });

        chronologyProperty().addListener(observable -> {
            LocalDateTime date = getValue();
            Chronology chrono = getChronology();

            if (validateDate(chrono, date)) {
                lastValidChronology = chrono;
//                defaultConverter = new LocalDateStringConverter(FormatStyle.SHORT, null, chrono);
                defaultConverter = new CustomConvertDP();
            } else {
                System.err.println("Restoring value to " + lastValidChronology);
                setChronology(lastValidChronology);
            }
        });
    }

    private boolean validateDate(Chronology chrono, LocalDateTime date) {
        try {
            if (date != null) {
                chrono.date(date);
            }
            return true;
        } catch (DateTimeException ex) {
            System.err.println(ex);
            return false;
        }
    }

    /**
     * Creates a DatePicker instance and sets the
     * {@link #valueProperty() value} to the given date.
     *
     * @param localDate to be set as the currently selected date in the DatePicker. Can be null.
     */
    public DateTimePicker(LocalDateTime localDate) {
        setValue(localDate);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.DATE_PICKER);
        setEditable(true);
    }


    /***************************************************************************
     *                                                                         *
     * Properties                                                                 *
     *                                                                         *
     **************************************************************************/


    /**
     * A custom cell factory can be provided to customize individual
     * day cells in the DatePicker popup. Refer to {@link DateCell}
     * and {@link Cell} for more information on cell factories.
     * Example:
     *
     * <pre><code>
     * final Callback&lt;DatePicker, DateCell&gt; dayCellFactory = new Callback&lt;DatePicker, DateCell&gt;() {
     *     public DateCell call(final DatePicker datePicker) {
     *         return new DateCell() {
     *             &#064;Override public void updateItem(LocalDateTime item, boolean empty) {
     *                 super.updateItem(item, empty);
     *
     *                 if (MonthDay.from(item).equals(MonthDay.of(9, 25))) {
     *                     setTooltip(new Tooltip("Happy Birthday!"));
     *                     setStyle("-fx-background-color: #ff4444;");
     *                 }
     *                 if (item.equals(LocalDateTime.now().plusDays(1))) {
     *                     // Tomorrow is too soon.
     *                     setDisable(true);
     *                 }
     *             }
     *         };
     *     }
     * };
     * datePicker.setDayCellFactory(dayCellFactory);
     * </code></pre>
     *
     * @defaultValue null
     */
    private ObjectProperty<Callback<DateTimePicker, DateTimeCell>> dayCellFactory;
    public final void setDayCellFactory(Callback<DateTimePicker, DateTimeCell> value) {
        dayCellFactoryProperty().set(value);
    }
    public final Callback<DateTimePicker, DateTimeCell> getDayCellFactory() {
        return (dayCellFactory != null) ? dayCellFactory.get() : null;
    }
    public final ObjectProperty<Callback<DateTimePicker, DateTimeCell>> dayCellFactoryProperty() {
        if (dayCellFactory == null) {
            dayCellFactory = new SimpleObjectProperty<Callback<DateTimePicker, DateTimeCell>>(this, "dayCellFactory");
        }
        return dayCellFactory;
    }



    /**
     * The calendar system used for parsing, displaying, and choosing
     * dates in the DatePicker control.
     *
     * <p>The default value is returned from a call to
     * {@code Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT))}.
     * The default is usually {@link java.time.chrono.IsoChronology} unless
     * provided explicitly in the {@link java.util.Locale} by use of a
     * Locale calendar extension.
     *
     * Setting the value to <code>null</code> will restore the default
     * chronology.
     */
    public final ObjectProperty<Chronology> chronologyProperty() {
        return chronology;
    }
    private ObjectProperty<Chronology> chronology =
        new SimpleObjectProperty<Chronology>(this, "chronology", null);
    public final Chronology getChronology() {
        Chronology chrono = chronology.get();
        if (chrono == null) {
            try {
                chrono = Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT));
            } catch (Exception ex) {
                System.err.println(ex);
            }
            if (chrono == null) {
                chrono = IsoChronology.INSTANCE;
            }
            //System.err.println(chrono);
        }
        return chrono;
    }
    public final void setChronology(Chronology value) {
        chronology.setValue(value);
    }


    /**
     * Whether the DatePicker popup should display a column showing
     * week numbers.
     *
     * <p>The default value is specified in a resource bundle, and
     * depends on the country of the current locale.
     */
    public final BooleanProperty showWeekNumbersProperty() {
        if (showWeekNumbers == null) {
            String country = Locale.getDefault(Locale.Category.FORMAT).getCountry();
            boolean localizedDefault =
                (!country.isEmpty() &&
                 ControlResources.getNonTranslatableString("DatePicker.showWeekNumbers").contains(country));
            showWeekNumbers = new StyleableBooleanProperty(localizedDefault) {
                @Override public CssMetaData<DateTimePicker,Boolean> getCssMetaData() {
                    return StyleableProperties.SHOW_WEEK_NUMBERS;
                }

                @Override public Object getBean() {
                    return DateTimePicker.this;
                }

                @Override public String getName() {
                    return "showWeekNumbers";
                }
            };
        }
        return showWeekNumbers;
    }
    private BooleanProperty showWeekNumbers;
    public final void setShowWeekNumbers(boolean value) {
        showWeekNumbersProperty().setValue(value);
    }
    public final boolean isShowWeekNumbers() {
        return showWeekNumbersProperty().getValue();
    }


    // --- string converter
    /**
     * Converts the input text to an object of type LocalDateTime and vice
     * versa.
     *
     * <p>If not set by the application, the DatePicker skin class will
     * set a converter based on a {@link java.time.format.DateTimeFormatter}
     * for the current {@link java.util.Locale} and
     * {@link #chronologyProperty() chronology}. This formatter is
     * then used to parse and display the current date value.
     *
     * Setting the value to <code>null</code> will restore the default
     * converter.
     *
     * <p>Example using an explicit formatter:
     * <pre><code>
     * datePicker.setConverter(new StringConverter&lt;LocalDateTime&gt;() {
     *     String pattern = "yyyy-MM-dd";
     *     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
     *
     *     {
     *         datePicker.setPromptText(pattern.toLowerCase());
     *     }
     *
     *     &#064;Override public String toString(LocalDateTime date) {
     *         if (date != null) {
     *             return dateFormatter.format(date);
     *         } else {
     *             return "";
     *         }
     *     }
     *
     *     &#064;Override public LocalDateTime fromString(String string) {
     *         if (string != null && !string.isEmpty()) {
     *             return LocalDateTime.parse(string, dateFormatter);
     *         } else {
     *             return null;
     *         }
     *     }
     * });
     * </code></pre>
     * <p>Example that wraps the default formatter and catches parse exceptions:
     * <pre><code>
     *   final StringConverter&lt;LocalDateTime&gt; defaultConverter = datePicker.getConverter();
     *   datePicker.setConverter(new StringConverter&lt;LocalDateTime&gt;() {
     *       &#064;Override public String toString(LocalDateTime value) {
     *           return defaultConverter.toString(value);
     *       }
     *
     *       &#064;Override public LocalDateTime fromString(String text) {
     *           try {
     *               return defaultConverter.fromString(text);
     *           } catch (DateTimeParseException ex) {
     *               System.err.println("HelloDatePicker: "+ex.getMessage());
     *               throw ex;
     *           }
     *       }
     *   });
     * </code></pre>
     *
     * <p>The default base year for parsing input containing only two digits for
     * the year is 2000 (see {@link java.time.format.DateTimeFormatter}).  This
     * default is not useful for allowing a person's date of birth to be typed.
     * The following example modifies the converter's fromString() method to
     * allow a two digit year for birth dates up to 99 years in the past.
     * <pre><code>
     *   &#064;Override public LocalDateTime fromString(String text) {
     *       if (text != null && !text.isEmpty()) {
     *           Locale locale = Locale.getDefault(Locale.Category.FORMAT);
     *           Chronology chrono = datePicker.getChronology();
     *           String pattern =
     *               DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.SHORT,
     *                                                                    null, chrono, locale);
     *           String prePattern = pattern.substring(0, pattern.indexOf("y"));
     *           String postPattern = pattern.substring(pattern.lastIndexOf("y")+1);
     *           int baseYear = LocalDateTime.now().getYear() - 99;
     *           DateTimeFormatter df = new DateTimeFormatterBuilder()
     *                       .parseLenient()
     *                       .appendPattern(prePattern)
     *                       .appendValueReduced(ChronoField.YEAR, 2, 2, baseYear)
     *                       .appendPattern(postPattern)
     *                       .toFormatter();
     *           return LocalDateTime.from(chrono.date(df.parse(text)));
     *       } else {
     *           return null;
     *       }
     *   }
     * </code></pre>
     *
     * @see javafx.scene.control.ComboBox#converterProperty
     */
    public final ObjectProperty<StringConverter<LocalDateTime>> converterProperty() { return converter; }
    private ObjectProperty<StringConverter<LocalDateTime>> converter =
            new SimpleObjectProperty<StringConverter<LocalDateTime>>(this, "converter", null);
    public final void setConverter(StringConverter<LocalDateTime> value) { converterProperty().set(value); }
    public final StringConverter<LocalDateTime> getConverter() {
        StringConverter<LocalDateTime> converter = converterProperty().get();
        if (converter != null) {
            return converter;
        } else {
            return defaultConverter;
        }
    }

    // Create a symmetric (format/parse) converter with the default locale.
    private StringConverter<LocalDateTime> defaultConverter =new CustomConvertDP();
//                new LocalDateTimeStringConverter(FormatStyle.SHORT, null, getChronology());


    // --- Editor
    /**
     * The editor for the DatePicker.
     *
     * @see javafx.scene.control.ComboBox#editorProperty
     */
    private ReadOnlyObjectWrapper<TextField> editor;
    public final TextField getEditor() {
        return editorProperty().get();
    }
    public final ReadOnlyObjectProperty<TextField> editorProperty() {
        if (editor == null) {
            editor = new ReadOnlyObjectWrapper<TextField>(this, "editor");
            editor.set(new ComboBoxListViewSkin.FakeFocusTextField());
        }
        return editor.getReadOnlyProperty();
    }

    /**
     * @return  {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new DateTimePickerSkin(this);
    }


    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "date-picker";

     /**
      * @treatAsPrivate implementation detail
      */
    private static class StyleableProperties {
        private static final String country =
            Locale.getDefault(Locale.Category.FORMAT).getCountry();
        private static final CssMetaData<DateTimePicker, Boolean> SHOW_WEEK_NUMBERS =
              new CssMetaData<DateTimePicker, Boolean>("-fx-show-week-numbers",
                   BooleanConverter.getInstance(),
                   (!country.isEmpty() &&
                    ControlResources.getNonTranslatableString("DatePicker.showWeekNumbers").contains(country))) {
            @Override public boolean isSettable(DateTimePicker n) {
                return n.showWeekNumbers == null || !n.showWeekNumbers.isBound();
            }

            @Override public StyleableProperty<Boolean> getStyleableProperty(DateTimePicker n) {
                return (StyleableProperty<Boolean>)(WritableValue<Boolean>)n.showWeekNumbersProperty();
            }
        };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                new ArrayList<CssMetaData<? extends Styleable, ?>>(Control.getClassCssMetaData());
            Collections.addAll(styleables,
                SHOW_WEEK_NUMBERS
            );
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    /**
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    /**
     * {@inheritDoc}
     * @return 
     */
   @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    /***************************************************************************
     *                                                                         *
     * Accessibility handling                                                  *
     *                                                                         *
     **************************************************************************/

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case DATE: return getValue();
            case TEXT: {
                String accText = getAccessibleText();
                if (accText != null && !accText.isEmpty()) return accText;

                LocalDateTime date = getValue();
                StringConverter<LocalDateTime> c = getConverter();
                if (date != null && c != null) {
                    return c.toString(date);
                }
                return "";
            }
            default: return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
