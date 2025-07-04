package org.example.juego.controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.juego.modelo.Jugador;
import org.example.juego.modelo.ListaJugador;

import java.util.LinkedList;
import java.util.List;


public class TableroController {

    @FXML
    public Pane tableroArea;

    public GridPane casillaprueba;
    @FXML
    private VBox vboxJugadores;

    @FXML
    private AnchorPane Dado;

    @FXML
    private DadoController dadoController;

    @FXML
    private ImageView imgDado;

    private ListaJugador jugadoresDisponibles;

    private int turno = 0;

    private Jugador jugadorTurno;

    private LinkedList<Jugador> jugadoresPendientes = null;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Tablero Trivia UCAB");
    }


    public void siguienteTurno() {
        turno++;
        if (turno > jugadoresDisponibles.getUsuarios().size() - 1) {
            turno = 0;
        }
        jugadorTurno = jugadoresDisponibles.getUsuarios().get(turno);
    }


    public void setJugadores(ListaJugador jugadoresDisponibles) {
        this.jugadoresDisponibles = jugadoresDisponibles;
        List<Jugador> jugadores = jugadoresDisponibles.getUsuarios();
        if (!jugadores.isEmpty()) {
            jugadorTurno = jugadores.get(turno);
        }
        // Limpiar el VBox antes de añadir jugadores
        vboxJugadores.getChildren().clear();

        // Crear un Accordion para los jugadores
        Accordion accordion = new Accordion();
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            String texto = (i + 1) + ". " + jugador.getAlias() + " 🎲 " + jugador.getResultadoDado();
            String infoTexto = "Información del jugador: Categoría respondida";
            Label infoLabel = new Label(infoTexto);
            VBox infoBox = new VBox(infoLabel);
            infoBox.setStyle("-fx-background-color: #f1f1f1; -fx-padding: 8px 16px; -fx-background-radius: 0 0 10px 10px;");
            TitledPane pane = new TitledPane(texto, infoBox);
            // Si es el turno actual, poner en verde el TitledPane completo
            if (jugador == jugadorTurno) {
                pane.setStyle("-fx-background-color: #28a745; -fx-font-weight: bold; -fx-background-radius: 10px; -fx-text-fill: #145a23;");
                // El gráfico será el nombre en verde oscuro y la flecha
                Label turnoLabel = new Label(jugador.getAlias() + " ⬅ TURNO");
                turnoLabel.setStyle("-fx-text-fill: #145a23; -fx-font-weight: bold; -fx-font-size: 16px;");
                pane.setText((i + 1) + ". 🎲 " + jugador.getResultadoDado());
                pane.setGraphic(turnoLabel);
                pane.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
            } else {
                pane.setStyle("");
                pane.setText(texto);
                pane.setGraphic(null);
            }
            accordion.getPanes().add(pane);
        }
        vboxJugadores.getChildren().add(accordion);
    }


    private String getColorPorPosicion(int posicion) {
        return switch (posicion) {
            case 0 -> "#28a745";
            case 1 -> "#007bff";
            case 2 -> "#ffc107";
            default -> "#6c757d";
        };

    }


    @FXML
    private void onSiguienteTurno() {
        siguienteTurno();
        setJugadores(jugadoresDisponibles);
    }



    @FXML
    public void initialize() {
        if (stage != null) {
            stage.setTitle("Tablero Trivia UCAB");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/juego/DadoView.fxml"));
            Parent dadoRoot = loader.load();
            DadoController dadoController = loader.getController();
            // Elimina cualquier dado anterior
            Dado.getChildren().clear();
            Dado.getChildren().add(dadoRoot);
            dadoController.start(1);
            // Asigna el evento de click al dado para animar y avanzar turno
            ImageView imgDado = (ImageView) dadoRoot.lookup("#imgDado");
            if (imgDado != null) {
                imgDado.setOnMouseClicked(e -> {
                    imgDado.setDisable(true); // Evita doble click
                    dadoController.clickDado(valor -> {
                        // Aquí puedes guardar el valor si lo necesitas
                        onSiguienteTurno();
                        imgDado.setDisable(false); // Permite lanzar de nuevo en el siguiente turno
                    });
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jugadoresPendientes != null) {
            setJugadoresOrdenados(jugadoresPendientes);
            jugadoresPendientes = null;
        }
        // Inyectar una ficha en cada celda del GridPane casilla
        inyectarFichasEnCasillaPrueba();
    }

    /**
     * Inyecta una ficha en cada celda del GridPane casilla de prueba.
     * Si el GridPane existe, recorre todas las filas y columnas, cargando la vista de ficha en cada celda.
     */
    private void inyectarFichasEnCasillaPrueba() {
        if (casillaprueba != null) {
            casillaprueba.setStyle("-fx-border-color: red; -fx-border-width: 3;");
            int filas = casillaprueba.getRowCount();
            int columnas = casillaprueba.getColumnCount();
            for (int row = 0; row < filas; row++) { // Se puede reemplazar con un for a través de los jugadores para llenar tantas fichas como jugadores haya.
                for (int col = 0; col < columnas; col++) {
                    try {
                        FXMLLoader fichaLoader = new FXMLLoader(getClass().getResource("/org/example/juego/FichaJugadorView.fxml"));
                        Parent ficha = fichaLoader.load();
                        casillaprueba.add(ficha, col, row);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Establece la lista de jugadores ordenados y la asigna al tablero si corresponde.
     * @param setJugadoresOrdenados Lista enlazada de jugadores ordenados.
     */
    public void setJugadoresOrdenados(LinkedList<Jugador> setJugadoresOrdenados) {
        if (vboxJugadores == null) {
            System.out.println("setJugadoresOrdenados: " + setJugadoresOrdenados);
            jugadoresDisponibles = new ListaJugador(setJugadoresOrdenados);
            this.setJugadores(jugadoresDisponibles);

        }
    }
}
