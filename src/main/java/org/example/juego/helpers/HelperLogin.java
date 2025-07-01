package org.example.juego.helpers;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

/**
 * Clase de utilidad para mostrar mensajes de estado en la interfaz de login.
 * Permite mostrar mensajes con diferentes estilos de alerta y animación fade in.
 */
public class HelperLogin {
    /**
     * Muestra un mensaje de estado en un Label con estilos de alerta y animación.
     *
     * @param lblStatus   Label donde se mostrará el mensaje.
     * @param msg         Mensaje a mostrar.
     * @param visible     Indica si el Label debe ser visible.
     * @param managed     Indica si el Label debe ser gestionado por el layout.
     * @param tipoAlerta  Tipo de alerta ("alert-danger", "alert-success", "alert-warning", "alert-info").
     */
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
