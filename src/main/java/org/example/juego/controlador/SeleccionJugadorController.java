package org.example.juego.controlador;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.juego.JuegoApplication;
import org.example.juego.db.ManipuladorUsuario;
import static org.example.juego.helpers.HelperLogin.mostrarStado;
import org.example.juego.modelo.ListaJugador;
import org.example.juego.modelo.Jugador;
import org.kordamp.bootstrapfx.BootstrapFX;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controlador para la selección de jugadores antes de iniciar el juego.
 * Permite agregar y quitar jugadores al tablero, iniciar el juego y gestionar la interfaz de selección.
 */
public class SeleccionJugadorController {
    /**
     * Lista de jugadores disponibles obtenidos desde la base de datos (JSON).
     */
    private final ListaJugador jugadoresDisponiblesJson = new ManipuladorUsuario().extraerDatoUsuario();

    /** Botón para agregar un jugador al tablero. */
    @FXML
    private Button btnAgregar;
    /** Botón para iniciar el juego. */
    @FXML
    private Button btnJugar;
    /** Botón para quitar un jugador del tablero. */
    @FXML
    private Button btnQuitar;
    /** Botón para volver a la pantalla anterior. */
    @FXML
    private Button btnVolver;
    /** Etiqueta para mostrar mensajes de estado. */
    @FXML
    private Label lblStatus;
    /** Lista visual de jugadores disponibles. */
    @FXML
    private ListView<String> listaJugadoresDisponibles;
    /** Lista visual de jugadores seleccionados para el tablero. */
    @FXML
    private ListView<String> listaJugadoresTablero;
    /** Etiqueta para mostrar datos del jugador. */
    @FXML
    private Label lbldatosdeljugador;
    /** Etiqueta para mostrar el alias del jugador. */
    @FXML
    private Label lblaliasjugador;
    /** Botón para lanzar el dado (si aplica). */
    @FXML
    private Button btndado;

    /** Lista de jugadores disponibles en memoria. */
    private ListaJugador jugadoresDisponibles;

    /**
     * Carga los jugadores disponibles en la lista visual desde la base de datos.
     */
    @FXML
    public void ingresarJugadoresDisponibles(){
        for(Jugador jugadorDisponible : jugadoresDisponiblesJson.getUsuarios()){
            listaJugadoresDisponibles.getItems().add(jugadorDisponible.getAlias());
        }
    }

    /**
     * Agrega el jugador seleccionado a la lista del tablero y actualiza la interfaz.
     * Muestra mensajes de estado y habilita/deshabilita botones según corresponda.
     */
    @FXML
    public void agregarJugadorTablero() {
        String jugador = listaJugadoresDisponibles.getSelectionModel().getSelectedItem();
        if(jugador == null){
            mostrarStado(lblStatus, "Seleccione un jugador para agragar!", true, true, "alert-warning");
            return;
        }

        listaJugadoresTablero.getItems().add(jugador);
        if (listaJugadoresTablero.getItems().size() > 1) {
            btnJugar.setDisable(false);
            btnQuitar.setDisable(false);
        }

        listaJugadoresDisponibles.getItems().remove(jugador);
        if (listaJugadoresTablero.getItems().size() == 6) {
            mostrarStado(lblStatus, "Jugadores completos para jugar!", true, true, "alert-success");
            btnAgregar.setDisable(true);
            return;
        }

        mostrarStado(lblStatus, jugador + ", agregado al tablero!", true, true, "alert-success");

        if (listaJugadoresDisponibles.getItems().isEmpty()) {
            btnAgregar.setDisable(true);
        }
    }

    /**
     * Quita el jugador seleccionado del tablero y lo devuelve a la lista de disponibles.
     * Actualiza la interfaz y muestra mensajes de estado.
     */
    @FXML
    public void quitarJugadorTablero() {
        String jugador = listaJugadoresTablero.getSelectionModel().getSelectedItem();
        if(jugador == null){
            mostrarStado(lblStatus, "Seleccione un jugador para quitar del tablero!", true, true, "alert-warning");
            return;
        }

        btnAgregar.setDisable(false);
        listaJugadoresDisponibles.getItems().add(jugador);
        mostrarStado(lblStatus, jugador + ", eliminado del tablero!", true, true, "alert-success");
        listaJugadoresTablero.getItems().remove(jugador);

        if (listaJugadoresTablero.getItems().size() < 2) {
            btnJugar.setDisable(true);
        }

        if (listaJugadoresTablero.getItems().isEmpty()) {
            btnJugar.setDisable(true);
            btnQuitar.setDisable(true);
            btnAgregar.setDisable(false);
            mostrarStado(lblStatus, "Agrega jugadores al tablero!", true, true, "alert-warning");
        }
    }

    /**
     * Inicia el juego abriendo la ventana de turnos y pasando los jugadores seleccionados.
     *
     * @param event Evento de acción que dispara el inicio del juego.
     * @throws IOException Si ocurre un error al cargar la vista FXML.
     */
    @FXML
    public void comenzarJuego(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JuegoApplication.class.getResource("TurnoView.fxml"));
        Parent root = fxmlLoader.load();

        // Aplicar estilo redondeado y sombra
        root.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0, 0, 5);" +
                        "-fx-padding: 10;"
        );

        // Cargar estilos adicionales
        root.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/estilos.css")).toExternalForm());

        // Usar escena y stage transparentes
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT); // ¡IMPORTANTE!

        Stage turnoAsignado = new Stage();
        turnoAsignado.initStyle(StageStyle.TRANSPARENT); // ¡CLAVE para transparencia real!
        turnoAsignado.initModality(Modality.APPLICATION_MODAL);
        turnoAsignado.setScene(scene);

        // Configurar controlador
        TurnoController turnoController = fxmlLoader.getController();
        List<Jugador> listaJugadorTablero = new ArrayList<>();
        for (Jugador jugador : jugadoresDisponiblesJson.getUsuarios()) {
            if (listaJugadoresTablero.getItems().contains(jugador.getAlias())) {
                listaJugadorTablero.add(jugador);
            }
        }

        turnoController.setJugadoresTablero(listaJugadorTablero);
        turnoController.iniciarRonda(listaJugadorTablero);

        turnoAsignado.initOwner(((Node) event.getSource()).getScene().getWindow());
        turnoAsignado.showAndWait();
    }

    /**
     * Cierra la ventana actual y vuelve a la pantalla anterior.
     */
    @FXML
    public void volverPaginaAnterior() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }

    /**
     * Sale completamente del juego cerrando la aplicación.
     */
    @FXML
    public void salirJuego() {
        volverPaginaAnterior();
        Platform.runLater(Platform::exit);
    }

    /**
     * Devuelve el botón para iniciar el juego (útil para pruebas o acceso externo).
     * @return Botón btnJugar
     */
    public Button getBtnJugar() {
        return btnJugar;
    }

}
