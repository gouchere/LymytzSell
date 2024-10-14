package com.lymytz.lymytzsell.view.component;

import com.lymytz.lymytzsell.service.utils.LymytzService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DateTimePickerController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(DateTimePickerController.class);
    @FXML
    private DatePicker FX_DATE;
    @FXML
    private TextField FX_TIME;
    @FXML
    private HBox rootPane;

    private final StringProperty timeProperty = new SimpleStringProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.debug("open datetime picker component...");
        rootPane.getProperties().put("controller", this);
        timeProperty.bindBidirectional(FX_TIME.textProperty());
        setDateTime(LocalDateTime.now());
    }

    public LocalDateTime getDateTime() {
        var datetime = FX_DATE.getValue();
        var time = parseTime(FX_TIME.getText());
        return LocalDateTime.of(datetime, time);
    }

    public void setDateTime(LocalDateTime dateTime) {
        Platform.runLater(() -> {
            FX_DATE.setValue(dateTime.toLocalDate());
            //FX_TIME.setText(formatTime(dateTime.toLocalTime()));
            timeProperty.set(formatTime(dateTime.toLocalTime()));
        });
    }

    private LocalTime parseTime(String time) {
        try {
            return LocalTime.parse(time);
        } catch (Exception e) {
            LymytzService.openAlertDialog("Entrez une heure au format HH:mm", "Veuillez entrer une heure au format HH:mm", "Format de l'heure erroné", Alert.AlertType.ERROR);
            throw new DateTimeException("Format de date erroné", e);
        }
    }

    private String formatTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
