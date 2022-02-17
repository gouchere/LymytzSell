package lymytz.service.application.composant.date;

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


import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import java.util.ArrayList;
import java.util.List;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import java.time.LocalDateTime;


public class DateTimePickerBehavior extends ComboBoxBaseBehavior<LocalDateTime> {

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     *
     */
    public DateTimePickerBehavior(final DateTimePicker datePicker) {
        super(datePicker, DATE_PICKER_BINDINGS);
    }

    /***************************************************************************
     *                                                                         *
     * Key event handling                                                      *
     *                                                                         *
     **************************************************************************/

    protected static final List<KeyBinding> DATE_PICKER_BINDINGS = new ArrayList<KeyBinding>();
    static {
        DATE_PICKER_BINDINGS.addAll(COMBO_BOX_BASE_BINDINGS);
    }

    @Override public void onAutoHide() {
        // when we click on some non-interactive part of the
        // calendar - we do not want to hide.
        DateTimePicker datePicker = (DateTimePicker)getControl();
        DateTimePickerSkin cpSkin = (DateTimePickerSkin)datePicker.getSkin();
        cpSkin.syncWithAutoUpdate();
        // if the DatePicker is no longer showing, then invoke the super method
        // to keep its show/hide state in sync.
        if (!datePicker.isShowing()) super.onAutoHide();
    }

}
