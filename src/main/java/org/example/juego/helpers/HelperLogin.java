package org.example.juego.helpers;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

public class HelperLogin {
    public static void mostrarStado(Label lblStatus, String msg, boolean visible, boolean managed, String tipoAlerta){
        // Clases alert posibles
        List<String> alertClasses = Arrays.asList("alert-danger", "alert-success", "alert-warning", "alert-info");

        // Eliminar todas las clases alert actuales
        lblStatus.getStyleClass().removeAll(alertClasses);

        // Agregar clase base alert si no tiene
        if (!lblStatus.getStyleClass().contains("alert")) {
            lblStatus.getStyleClass().add("alert");
        }

        // Agregar clase tipoAlerta si es válida, si no se ignora
        if (alertClasses.contains(tipoAlerta)) {
            lblStatus.getStyleClass().add(tipoAlerta);
        }

        lblStatus.setText(msg);
        lblStatus.setManaged(managed);
        lblStatus.setVisible(visible);

        // Animacion del fade in
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), lblStatus);
        fadeOut.setFromValue(0.0);
        fadeOut.setToValue(1.0);
        fadeOut.play();

    }
}
