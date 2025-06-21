package org.example.juego.controlador;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.juego.db.ManipuladorUsuario;
import org.example.juego.helpers.HelperLogin;
import org.example.juego.modelo.ListaJugador;
import org.example.juego.modelo.Jugador;

import java.util.ArrayList;
import java.util.LinkedList;

public class SeleccionJugadorController {
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnJugar;
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
    private Label lbldatosdeljugador;
    @FXML
    private Label lblaliasjugador;
    @FXML
    private Button btndado;

    private ListaJugador jugadoresDisponibles;

    @FXML
    public void ingresarJugadoresDisponibles() {
        ManipuladorUsuario manipulador = new ManipuladorUsuario();
        jugadoresDisponibles = manipulador.extraerDatoUsuario();
        for (Jugador jugadorDisponible : jugadoresDisponibles.getUsuarios()) {
            listaJugadoresDisponibles.getItems().add(jugadorDisponible.getCorreo());
        }
    }

    @FXML
    public void agregarJugadorTablero() {
        String jugador = listaJugadoresDisponibles.getSelectionModel().getSelectedItem();
        if (jugador == null) {
            HelperLogin.mostrarStado(lblStatus, "Seleccione un jugador para agragar!", true, true, "alert-warning");
            return;
        }

        listaJugadoresTablero.getItems().add(jugador);
        if (listaJugadoresTablero.getItems().size() > 1) {
            btnJugar.setDisable(false);
            btnQuitar.setDisable(false);
        }

        listaJugadoresDisponibles.getItems().remove(jugador);
        if (listaJugadoresTablero.getItems().size() == 6) {
            HelperLogin.mostrarStado(lblStatus, "Jugadores completos para jugar!", true, true, "alert-success");
            btnAgregar.setDisable(true);
            return;
        }

        HelperLogin.mostrarStado(lblStatus, jugador + ", agregado al tablero!", true, true, "alert-success");

        if (listaJugadoresDisponibles.getItems().isEmpty()) {
            btnAgregar.setDisable(true);
        }
    }

    @FXML
    public void quitarJugadorTablero() {
        String jugador = listaJugadoresTablero.getSelectionModel().getSelectedItem();
        if (jugador == null) {
            HelperLogin.mostrarStado(lblStatus, "Seleccione un jugador para quitar del tablero!", true, true, "alert-warning");
            return;
        }

        btnAgregar.setDisable(false);
        listaJugadoresDisponibles.getItems().add(jugador);
        HelperLogin.mostrarStado(lblStatus, jugador + ", eliminado del tablero!", true, true, "alert-success");
        listaJugadoresTablero.getItems().remove(jugador);

        if (listaJugadoresTablero.getItems().size() < 2) {
            btnJugar.setDisable(true);
        }

        if (listaJugadoresTablero.getItems().isEmpty()) {
            btnJugar.setDisable(true);
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
    public void salirJuego() {
        volverPaginaAnterior();
        Platform.runLater(Platform::exit);
    }

    public Button getBtnJugar() {
        return btnJugar;
    }

    int turnoActual = 0;

    @FXML
    public void mostrarDialogoSecuencailes() {
        ArrayList<String> jugadores = new ArrayList<>(listaJugadoresTablero.getItems());
        if (jugadores.isEmpty()) return;

        java.util.Map<String, Integer> resultados = new java.util.HashMap<>();
        ArrayList<String> jugadoresPendientes = new ArrayList<>(jugadores);
        boolean hayRepetidos;
        do {
            for (String jugador : jugadoresPendientes) {
                ButtonType lanzarButtonType = new ButtonType("Lanzar dado 🎲", ButtonBar.ButtonData.OK_DONE);
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Turno de " + jugador);
                dialog.setHeaderText("Es el turno de: " + jugador);

                // Label para mostrar el resultado
                Label lblResultado = new Label("Resultado: -");
                lblResultado.setMinWidth(100);

                // Contenido horizontal: resultado a la izquierda, texto al centro
                VBox vbox = new VBox();
                vbox.setSpacing(10);
                HBox hbox = new HBox();
                hbox.setSpacing(10);
                hbox.getChildren().addAll(lblResultado, new Label("Presiona el botón para lanzar el dado."));
                vbox.getChildren().add(hbox);

                dialog.getDialogPane().setContent(vbox);
                dialog.getDialogPane().getButtonTypes().add(lanzarButtonType);

                final int[] resultado = new int[1];
                dialog.setResultConverter(buttonType -> {
                    if (buttonType == lanzarButtonType) {
                        resultado[0] = (int) (Math.random() * 6) + 1;
                        lblResultado.setText("Resultado: " + resultado[0]);
                        HelperLogin.mostrarStado(lblStatus, jugador + " sacó " + resultado[0] + " en el dado", true, true, "alert-info");
                        System.out.println("sacó " + resultado[0]);
                        return buttonType;
                    }
                    return null;
                });

                dialog.showAndWait();
                resultados.put(jugador, resultado[0]);
            }
            // Verificar repeticiones
            java.util.Map<Integer, java.util.List<String>> valores = new java.util.HashMap<>();
            for (var entry : resultados.entrySet()) {
                valores.computeIfAbsent(entry.getValue(), k -> new java.util.ArrayList<>()).add(entry.getKey());
            }
            jugadoresPendientes.clear();
            hayRepetidos = false;
            for (var entry : valores.entrySet()) {
                if (entry.getValue().size() > 1) {
                    hayRepetidos = true;
                    jugadoresPendientes.addAll(entry.getValue());
                }
            }
            if (hayRepetidos) {
                HelperLogin.mostrarStado(lblStatus, "¡Hay repeticiones! Solo los jugadores con el mismo valor volverán a tirar.", true, true, "alert-warning");
            }
        } while (hayRepetidos);
        HelperLogin.mostrarStado(lblStatus, "Todos los jugadores tienen valores únicos!", true, true, "alert-success");
        turnoActual = 0;
    }

    @FXML
    public void empezarJuego() {
        ArrayList<String> jugadores = new ArrayList<>(listaJugadoresTablero.getItems());
        LinkedList<Jugador> JugadoresTablero = jugadoresDisponibles.getUsuarios();
        ArrayList<Jugador> aEliminar = new ArrayList<>();
        for (Jugador jugadorActual : jugadoresDisponibles.getUsuarios()) {
            if (!jugadores.contains(jugadorActual.getCorreo())) {
                aEliminar.add(jugadorActual);
            }
        }

        for (Jugador jugador : aEliminar) {
            jugadoresDisponibles.eliminarUsuario(jugador);
        }
        mostrarDialogoSecuencailes();
    }

}
