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

package lymytz.service.application.composant.date;

import java.time.LocalDate;

import com.sun.javafx.scene.control.skin.DateCellSkin;
import java.time.LocalDateTime;
import javafx.scene.control.Cell;
import javafx.scene.control.Skin;

/**
 * DateTimeCell is used by {@link DatePicker} to render the individual
 * grid cells in the calendar month. By providing a
 * {@link DatePicker#dayCellFactoryProperty() dayCellFactory}, an
 * application can provide an update method to change each cell's
 * properties such as text, background color, etc.
 *
 * @since JavaFX 8.0
 */
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
