/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.composant.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.util.StringConverter;

/**
 *
 * @author LENOVO
 */
public class CustomConvertDP extends StringConverter<LocalDateTime> {

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public String toString(LocalDateTime date) {
        if (date != null) {
            return dateFormatter.format(date);
        } else {
            return dateFormatter.format(LocalDate.now());
        }
    }

    @Override
    public LocalDateTime fromString(String string) {
        if (string != null ? !string.isEmpty() : false) {
            return LocalDateTime.parse(string, dateFormatter);
        } else {
            return null;
        }
    }

}
