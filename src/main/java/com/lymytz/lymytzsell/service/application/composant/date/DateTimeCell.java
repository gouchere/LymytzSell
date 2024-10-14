/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
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

package com.lymytz.lymytzsell.service.application.composant.date;

import javafx.scene.control.Cell;
import javafx.scene.control.Skin;

import java.time.LocalDateTime;

public class DateTimeCell extends Cell<LocalDateTime> {
    public DateTimeCell() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    /** {@inheritDoc} */
    @Override public void updateItem(LocalDateTime item, boolean empty) {
        super.updateItem(item, empty);
    }

    /** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new DateTimeCellSkin(this);
    }

    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "date-cell";
}
