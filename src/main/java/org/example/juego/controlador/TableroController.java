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

/**
 * Controlador del tablero principal del juego.
 * Gestiona la visualización de los jugadores, turnos y fichas en el tablero.
 */
public class TableroController {
    /**
     * Área principal del tablero donde se muestran las fichas y el estado del juego.
     */
    @FXML
    public Pane tableroArea;

    /**
     * GridPane de prueba para colocar fichas de jugadores.
     */
    public GridPane casillaprueba;

    /**
     * Contenedor vertical para mostrar la lista de jugadores y sus turnos.
     */
    @FXML
    private VBox vboxJugadores;

    /**
     * Panel anclado para mostrar el dado.
     */
    @FXML
    private AnchorPane Dado;

    /**
     * Controlador del dado asociado a la vista del dado.
     */
    @FXML
    private DadoController dadoController;

    /**
     * Imagen del dado en la interfaz.
     */
    @FXML
    private ImageView imgDado;

    /**
     * Lista de jugadores disponibles en el tablero.
     */
    private ListaJugador jugadoresDisponibles;

    /**
     * Índice del turno actual.
     */
    private int turno = 0;

    /**
     * Jugador que tiene el turno actual.
     */
    private Jugador jugadorTurno;

    /**
     * Lista de jugadores pendientes de acción (si aplica).
     */
    private LinkedList<Jugador> jugadoresPendientes = null;

    /**
     * Stage principal donde se muestra el tablero.
     */
    private Stage stage;

    /**
     * Permite inyectar el Stage principal para poder modificar el título de la ventana.
     *
     * @param stage Stage principal donde se muestra el tablero.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Tablero Trivia UCAB");
    }

    /**
     * Avanza al siguiente turno y actualiza el jugador en turno.
     */
    public void siguienteTurno() {
        turno++;
        if (turno > jugadoresDisponibles.getUsuarios().size() - 1) {
            turno = 0;
        }
        jugadorTurno = jugadoresDisponibles.getUsuarios().get(turno);
    }

    /**
     * Establece la lista de jugadores y actualiza la visualización en el tablero.
     *
     * @param jugadoresDisponibles Lista de jugadores a mostrar.
     */
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

    /**
     * Devuelve el color correspondiente según la posición del jugador.
     *
     * @param posicion Posición en el ranking.
     * @return Color en formato hexadecimal.
     */
    private String getColorPorPosicion(int posicion) {
        return switch (posicion) {
            case 0 -> "#28a745";
            case 1 -> "#007bff";
            case 2 -> "#ffc107";
            default -> "#6c757d";
        };

    }

    /**
     * Avanza al siguiente turno y actualiza la visualización de jugadores.
     */
    @FXML
    private void onSiguienteTurno() {
        siguienteTurno();
        setJugadores(jugadoresDisponibles);
    }

    /**
     * Inicializa el tablero de juego, cargando la vista del dado, configurando eventos y generando las fichas de los jugadores.
     * <p>
     * - Carga la vista del dado y la inserta en el panel correspondiente.
     * - Asigna el evento de click al dado para animar y avanzar el turno.
     * - Si hay jugadores pendientes, los ordena y actualiza la visualización.
     * - Genera fichas en el GridPane de prueba para cada celda.
     */
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
     * Establece la lista de jugadores ordenados y actualiza la visualización si corresponde.
     *
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
