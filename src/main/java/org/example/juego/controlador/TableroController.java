package org.example.juego.controlador;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.juego.modelo.Jugador;
import org.example.juego.modelo.ListaJugador;

import java.util.Comparator;
import java.util.List;

public class TableroController {

    @FXML
    private VBox vboxJugadores;

    @FXML
    private AnchorPane Dado;

    @FXML
    private DadoController dadoController;


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

        try{
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/org/example/juego/DadoView.fxml")
            );
            Parent root = loader.load();
            dadoController = loader.getController();

            // Limpiar el AnchorPane y agregar el nuevo contenido
            Dado.getChildren().clear();
            Dado.getChildren().add(root);
            dadoController.start(1);
        } catch (Exception e) {
            e.printStackTrace();
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
