/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application.composant.date;

import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 *
 * @author LENOVO
 */
public class CustomConvertDP extends StringConverter<LocalDateTime> {

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public String toString(LocalDateTime date) {
        return dateFormatter.format(Objects.requireNonNullElseGet(date, LocalDate::now));
    }

    @Override
    public LocalDateTime fromString(String string) {
        if (string != null && !string.isEmpty()) {
            return LocalDateTime.parse(string, dateFormatter);
        } else {
            return null;
        }
    }

}
