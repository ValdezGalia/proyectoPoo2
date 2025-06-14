package org.example.juego.controlador;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.juego.db.ManipuladorUsuario;
import org.example.juego.helpers.HelperLogin;
import org.example.juego.modelo.ListaJugador;
import org.example.juego.modelo.Jugador;

public class SeleccionJugadorController {
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnQuitar;
    @FXML
    private Button btnVolver;
    @FXML
    private Label lblStatus;
    @FXML
    private ListView<String> listaJugadoresDisponibles;
    @FXML
    private ListView<String> listaJugadoresTablero;

    @FXML
    public void ingresarJugadoresDisponibles(){
        ManipuladorUsuario manipulador = new ManipuladorUsuario();
        ListaJugador jugadoresDisponibles = manipulador.extraerDatoUsuario();
        for(Jugador jugadorDisponible : jugadoresDisponibles.getUsuarios()){
            listaJugadoresDisponibles.getItems().add(jugadorDisponible.getCorreo());
        }
    }

    @FXML
    public void agregarJugadorTablero(){
        String jugador = listaJugadoresDisponibles.getSelectionModel().getSelectedItem();
        if(jugador == null){
            HelperLogin.mostrarStado(lblStatus, "Seleccione un jugador para agragar!", true, true, "alert-warning");
            return;
        }

        listaJugadoresTablero.getItems().add(jugador);
        if(listaJugadoresTablero.getItems().size() > 1){
            btnQuitar.setDisable(false);
        }

        listaJugadoresDisponibles.getItems().remove(jugador);
        if (listaJugadoresTablero.getItems().size() == 6) {
            HelperLogin.mostrarStado(lblStatus, "Jugadores completos para jugar!", true, true, "alert-success");
            btnAgregar.setDisable(true);
            return;
        }

        HelperLogin.mostrarStado(lblStatus, jugador + ", agregado al tablero!", true, true, "alert-success");

        if(listaJugadoresDisponibles.getItems().isEmpty()){
            btnAgregar.setDisable(true);
        }
    }

    @FXML
    public void quitarJugadorTablero(){
        String jugador = listaJugadoresTablero.getSelectionModel().getSelectedItem();
        if(jugador == null){
            HelperLogin.mostrarStado(lblStatus, "Seleccione un jugador para quitar del tablero!", true, true, "alert-warning");
            return;
        }

        btnAgregar.setDisable(false);
        listaJugadoresDisponibles.getItems().add(jugador);
        HelperLogin.mostrarStado(lblStatus, jugador + ", eliminado del tablero!", true, true, "alert-success");
        listaJugadoresTablero.getItems().remove(jugador);

        if(listaJugadoresTablero.getItems().isEmpty()){
            btnQuitar.setDisable(true);
            btnAgregar.setDisable(false);
            HelperLogin.mostrarStado(lblStatus, "Agrega jugadores al tablero!", true, true, "alert-warning");
        }
    }

    @FXML
    public void volverPaginaAnterior() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void salirJuego(){
        volverPaginaAnterior();
        Platform.runLater(Platform::exit);
    }
}
