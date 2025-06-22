package org.example.juego.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.juego.modelo.Jugador;
import org.example.juego.modelo.ListaJugador;

import java.util.Comparator;
import java.util.List;

public class TableroController {

    @FXML
    private VBox vboxJugadores;

    public void setJugadores(ListaJugador jugadoresDisponibles) {
        List<Jugador> jugadores = jugadoresDisponibles.getUsuarios();

        // Ordenar de mayor a menor según el valor del dado
        jugadores.sort(Comparator.comparingInt(Jugador::getUltimoResultadoDado).reversed());

        // Limpiar el VBox antes de añadir jugadores
        vboxJugadores.getChildren().clear();

        // Mostrar cada jugador con estilo
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            String texto = (i + 1) + ". " + jugador.getAlias() + " 🎲 " + jugador.getUltimoResultadoDado();

            Label label = new Label(texto);
            label.setStyle(
                    "-fx-font-size: 16px;" +
                            "-fx-padding: 8px;" +
                            "-fx-background-color: " + getColorPorPosicion(i) + ";" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 10px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.4), 3, 0.5, 1, 1);"
            );

            vboxJugadores.getChildren().add(label);
        }
    }

    // Estilo distinto para los primeros 3 puestos
    private String getColorPorPosicion(int posicion) {
        return switch (posicion) {
            case 0 -> "#28a745"; // Verde - 1er lugar
            case 1 -> "#007bff"; // Azul - 2do lugar
            case 2 -> "#ffc107"; // Amarillo - 3er lugar
            default -> "#6c757d"; // Gris para ;;;los demás
        };
    }
}
