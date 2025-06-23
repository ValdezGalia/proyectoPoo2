package org.example.juego.controlador;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.example.juego.modelo.Jugador;
import org.example.juego.modelo.ListaJugador;

import java.util.List;

public class TableroController {

    @FXML
    private VBox vboxJugadores;

    @FXML
    private AnchorPane Dado;

    @FXML
    private DadoController dadoController;

    @FXML
    private javafx.scene.control.Button btnSiguienteTurno;

    @FXML
    private ImageView imgDado;

    ListaJugador jugadoresDisponibles;

    private int turno = 0;

    private Jugador jugadorTurno;

    public void siguienteTurno() {
        turno++;
        if (turno > jugadoresDisponibles.getUsuarios().size() - 1) {
            turno = 0;
        }
        jugadorTurno = jugadoresDisponibles.getUsuarios().get(turno);
        // Actualizar la interfaz de usuario o realizar otras acciones necesarias

    }

    public void setJugadores(ListaJugador jugadoresDisponibles) {
        this.jugadoresDisponibles = jugadoresDisponibles;
        List<Jugador> jugadores = jugadoresDisponibles.getUsuarios();
        if (!jugadores.isEmpty()) {
            jugadorTurno = jugadores.get(turno);
        }

        // Limpiar el VBox antes de añadir jugadores
        vboxJugadores.getChildren().clear();

        // Mostrar cada jugador con estilo
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            String texto = (i + 1) + ". " + jugador.getAlias() + " 🎲 " + jugador.getUltimoResultadoDado();

            Label label = new Label(texto);
            // Si es el turno actual, poner en verde
            if (jugador == jugadorTurno) {
                label.setStyle(
                        "-fx-font-size: 16px;" +
                                "-fx-padding: 8px;" +
                                "-fx-background-color: #28a745;" + // Verde para el turno actual
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 10px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.4), 3, 0.5, 1, 1);"
                );
            } else {
                label.setStyle(
                        "-fx-font-size: 16px;" +
                                "-fx-padding: 8px;" +
                                "-fx-background-color: #6c757d;" + // Gris para los demás
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 10px;" +
                                "-fx-font-weight: bold;"
                );
            }
            vboxJugadores.getChildren().add(label);
        }

        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/org/example/juego/DadoView.fxml")
            );
            Parent root = loader.load();
            dadoController = loader.getController();

            // Listener para guardar el resultado del dado en el modelo Jugador
            dadoController.setGetValorListener(valor -> {
                if (jugadorTurno != null) {
                    jugadorTurno.setUltimoResultadoDado(valor);
                }
                // Deshabilitar la imagen del dado después de tirar
                imgDado.setDisable(true);
                // Habilitar el botón de siguiente turno
                btnSiguienteTurno.setDisable(false);
            }); 

            // Limpiar el AnchorPane y agregar el nuevo contenido
            Dado.getChildren().clear();
            Dado.getChildren().add(root);
            dadoController.start(1);
            // Habilitar la imagen del dado al iniciar el turno
            imgDado = (javafx.scene.image.ImageView) root.lookup("#imgDado");
            if (imgDado != null) {
                imgDado.setDisable(false);
            }
            // Deshabilitar el botón de siguiente turno hasta que se lance el dado
            btnSiguienteTurno.setDisable(true);
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

    @FXML
    private void onSiguienteTurno() {
        siguienteTurno();
        setJugadores(jugadoresDisponibles);
        // Habilitar la imagen del dado al pasar de turno
        if (imgDado != null) {
            imgDado.setDisable(false);
        }
        // Deshabilitar el botón de siguiente turno hasta que se lance el dado
        btnSiguienteTurno.setDisable(true);
    }
}
