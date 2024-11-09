package com.lymytz.lymytzsell.view.component;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ToastService {

    public static void show(Stage stage, String message, int durationMillis) {
        if (stage == null) throw new AssertionError();
        double x = stage.getX() + stage.getWidth()/2-50;
        double y = stage.getY() + stage.getHeight() - 35;
        Popup popup = new Popup();
        popup.setAutoHide(true);

        Label label = new Label(message);
        label.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 10px;");

        StackPane pane = new StackPane(label);
        pane.setStyle("-fx-background-radius: 5px;");
        pane.setOpacity(0);
        pane.setAlignment(Pos.BOTTOM_RIGHT);

        popup.getContent().add(pane);
        popup.show(stage, x, y);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), pane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), pane);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.millis(durationMillis));
        fadeOut.setOnFinished(e -> popup.hide());

        fadeIn.play();
        fadeIn.setOnFinished(e -> fadeOut.play());
    }
}
