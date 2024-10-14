package com.lymytz.lymytzsell.service.application.composant.date;

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
import javafx.scene.control.PopupControl;

import java.time.LocalDateTime;


public class DateTimePickerBehavior extends ComboBoxBaseBehavior<LocalDateTime> {
    public DateTimePickerBehavior(DateTimePicker var1) {
        super(var1);
    }

    public void onAutoHide(PopupControl var1) {
        if (!var1.isShowing() && this.getNode().isShowing()) {
            this.getNode().hide();
        }

        if (!this.getNode().isShowing()) {
            super.onAutoHide(var1);
        }

    }

}
