package com.lymytz.lymytzsell.view.component.date;

import com.sun.javafx.scene.control.FakeFocusTextField;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.BooleanConverter;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DateTimePicker extends ComboBoxBase<LocalDateTime> {
    private LocalDateTime lastValidDate;
    private Chronology lastValidChronology;
    private ObjectProperty<Callback<DateTimePicker, DateTimeCell>> dayCellFactory;
    private ObjectProperty<Chronology> chronology;
    private BooleanProperty showWeekNumbers;
    private ObjectProperty<StringConverter<LocalDateTime>> converter;
    private StringConverter<LocalDateTime> defaultConverter;
    private ReadOnlyObjectWrapper<TextField> editor;
    private static final String DEFAULT_STYLE_CLASS = "date-picker";

    public DateTimePicker() {
        this(null);
        this.valueProperty().addListener((var1) -> {
            LocalDateTime var2 = this.getValue();
            Chronology var3 = this.getChronology();
            if (this.validateDate(var3, var2)) {
                this.lastValidDate = var2;
            } else {
                String var10001 = this.lastValidDate == null ? "null" : this.getConverter().toString(this.lastValidDate);
                System.err.println("Restoring value to " + var10001);
                this.setValue(this.lastValidDate);
            }

        });
        this.chronologyProperty().addListener((var1) -> {
            LocalDateTime var2 = this.getValue();
            Chronology var3 = this.getChronology();
            if (this.validateDate(var3, var2)) {
                this.lastValidChronology = var3;
                this.defaultConverter = new CustomConvertDP();
            } else {
                System.err.println("Restoring value to " + this.lastValidChronology);
                this.setChronology(this.lastValidChronology);
            }

        });
    }

    public DateTimePicker(LocalDateTime var1) {
        this.lastValidDate = null;
        this.lastValidChronology = IsoChronology.INSTANCE;
        this.chronology = new SimpleObjectProperty(this, "chronology", (Object) null);
        this.converter = new SimpleObjectProperty(this, "converter", (Object) null);
        this.defaultConverter = new CustomConvertDP();
        this.setValue(var1);
        this.getStyleClass().add("date-picker");
        this.setAccessibleRole(AccessibleRole.DATE_PICKER);
        this.setEditable(true);
    }

    private boolean validateDate(Chronology var1, LocalDateTime var2) {
        try {
            if (var2 != null) {
                var1.date(var2);
            }

            return true;
        } catch (DateTimeException var4) {
            System.err.println(var4);
            return false;
        }
    }

    public final void setDayCellFactory(Callback<DateTimePicker, DateTimeCell> var1) {
        this.dayCellFactoryProperty().set(var1);
    }

    public final Callback<DateTimePicker, DateTimeCell> getDayCellFactory() {
        return this.dayCellFactory != null ? (Callback) this.dayCellFactory.get() : null;
    }

    public final ObjectProperty<Callback<DateTimePicker, DateTimeCell>> dayCellFactoryProperty() {
        if (this.dayCellFactory == null) {
            this.dayCellFactory = new SimpleObjectProperty(this, "dayCellFactory");
        }

        return this.dayCellFactory;
    }

    public final ObjectProperty<Chronology> chronologyProperty() {
        return this.chronology;
    }

    public final Chronology getChronology() {
        Object var1 = (Chronology) this.chronology.get();
        if (var1 == null) {
            try {
                var1 = Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT));
            } catch (Exception var3) {
                System.err.println(var3);
            }

            if (var1 == null) {
                var1 = IsoChronology.INSTANCE;
            }
        }

        return (Chronology) var1;
    }

    public final void setChronology(Chronology var1) {
        this.chronology.setValue(var1);
    }

    public final BooleanProperty showWeekNumbersProperty() {
        if (this.showWeekNumbers == null) {
            String var1 = Locale.getDefault(Locale.Category.FORMAT).getCountry();
            boolean var2 = !var1.isEmpty() && ControlResources.getNonTranslatableString("DatePicker.showWeekNumbers").contains(var1);
            this.showWeekNumbers = new StyleableBooleanProperty(var2) {
                public CssMetaData<DateTimePicker, Boolean> getCssMetaData() {
                    return DateTimePicker.StyleableProperties.SHOW_WEEK_NUMBERS;
                }

                public Object getBean() {
                    return DateTimePicker.this;
                }

                public String getName() {
                    return "showWeekNumbers";
                }
            };
        }

        return this.showWeekNumbers;
    }

    public final void setShowWeekNumbers(boolean var1) {
        this.showWeekNumbersProperty().setValue(var1);
    }

    public final boolean isShowWeekNumbers() {
        return this.showWeekNumbersProperty().getValue();
    }

    public final ObjectProperty<StringConverter<LocalDateTime>> converterProperty() {
        return this.converter;
    }

    public final void setConverter(StringConverter<LocalDateTime> var1) {
        this.converterProperty().set(var1);
    }

    public final StringConverter<LocalDateTime> getConverter() {
        StringConverter var1 = this.converterProperty().get();
        return var1 != null ? var1 : this.defaultConverter;
    }

    public final TextField getEditor() {
        return (TextField) this.editorProperty().get();
    }

    public final ReadOnlyObjectProperty<TextField> editorProperty() {
        if (this.editor == null) {
            this.editor = new ReadOnlyObjectWrapper(this, "editor");
            this.editor.set(new FakeFocusTextField());
        }

        return this.editor.getReadOnlyProperty();
    }

    protected Skin<?> createDefaultSkin() {
        return new DateTimePickerSkin(this);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return DateTimePicker.StyleableProperties.STYLEABLES;
    }

    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
        switch (var1) {
            case DATE:
                return this.getValue();
            case TEXT:
                String var3 = this.getAccessibleText();
                if (var3 != null && !var3.isEmpty()) {
                    return var3;
                } else {
                    LocalDateTime var4 = this.getValue();
                    StringConverter var5 = this.getConverter();
                    if (var4 != null && var5 != null) {
                        return var5.toString(var4);
                    }

                    return "";
                }
            default:
                return super.queryAccessibleAttribute(var1, var2);
        }
    }

    private static class StyleableProperties {
        private static final String country;
        private static final CssMetaData<DateTimePicker, Boolean> SHOW_WEEK_NUMBERS;
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            country = Locale.getDefault(Locale.Category.FORMAT).getCountry();
            SHOW_WEEK_NUMBERS = new CssMetaData<DateTimePicker, Boolean>("-fx-show-week-numbers", BooleanConverter.getInstance(), !country.isEmpty() && ControlResources.getNonTranslatableString("DatePicker.showWeekNumbers").contains(country)) {
                public boolean isSettable(DateTimePicker var1) {
                    return var1.showWeekNumbers == null || !var1.showWeekNumbers.isBound();
                }

                public StyleableProperty<Boolean> getStyleableProperty(DateTimePicker var1) {
                    return (StyleableProperty) var1.showWeekNumbersProperty();
                }
            };
            ArrayList var0 = new ArrayList(Control.getClassCssMetaData());
            Collections.addAll(var0, new CssMetaData[]{SHOW_WEEK_NUMBERS});
            STYLEABLES = Collections.unmodifiableList(var0);
        }
    }
}
