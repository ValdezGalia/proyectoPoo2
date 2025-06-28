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

public class SeleccionJugadorController {
    private final ListaJugador jugadoresDisponiblesJson = new ManipuladorUsuario().extraerDatoUsuario();

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
    public void ingresarJugadoresDisponibles(){
        for(Jugador jugadorDisponible : jugadoresDisponiblesJson.getUsuarios()){
            listaJugadoresDisponibles.getItems().add(jugadorDisponible.getAlias());
        }
    }

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
//    public void comenzarJuego(ActionEvent event) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(JuegoApplication.class.getResource("TurnoView.fxml"));
//        Parent root = fxmlLoader.load();
//
//        root.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
//        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/estilos.css")).toExternalForm());
//        TurnoController turnoController = fxmlLoader.getController();
//
//        List<Jugador> listaJugadorTablero = new ArrayList<>();
//
//        for(Jugador jugador : jugadoresDisponiblesJson.getUsuarios()){
//            if(listaJugadoresTablero.getItems().contains(jugador.getAlias())){
//                listaJugadorTablero.add(jugador);
//            }
//        }
//        Stage turnoAsignado = new Stage();
//        turnoAsignado.setScene(new Scene(root));
//
//        root.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-border-radius: 20;");
//        turnoAsignado.initModality(Modality.APPLICATION_MODAL);
//        turnoAsignado.initStyle(StageStyle.UNDECORATED);
//
//        turnoController.setJugadoresTablero(listaJugadorTablero);
//        turnoController.iniciarRonda(listaJugadorTablero);
//
//        turnoAsignado.initOwner(((Node) event.getSource()).getScene().getWindow());
//        turnoAsignado.showAndWait();
//    }

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

    /* int turnoActual = 0;

    @FXML
    public void mostrarDialogoSecuencailes() {
        ArrayList<String> jugadores = new ArrayList<>(listaJugadoresTablero.getItems());
        if (jugadores.isEmpty())
            return;

        java.util.Map<String, Integer> resultados = new java.util.HashMap<>();
        ArrayList<String> jugadoresPendientes = new ArrayList<>(jugadores);
        boolean hayRepetidos;
        do {
            for (String jugador : jugadoresPendientes) {
                ButtonType lanzarButtonType = new ButtonType("Lanzar dado 🎲", ButtonBar.ButtonData.OK_DONE);
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle(""); // Sin título
                dialog.setHeaderText("Es el turno de: " + jugador);
                // Quitar decoración de la ventana y la X de cerrar
                Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
                dialogStage.initStyle(javafx.stage.StageStyle.UNDECORATED);

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
                        HelperLogin.mostrarStado(lblStatus, jugador + " sacó " + resultado[0] + " en el dado", true,
                                true, "alert-info");
                        System.out.println("el jugador " + jugador+ " sacó " + resultado[0]);

                        // Guarda el resultado en el objeto Jugador
                        for (Jugador j : jugadoresDisponibles.getUsuarios()) {
                            if (j.getAlias().equals(jugador)) {
                                j.setResultadoDado(resultado[0]);
                                break;
                            }
                        }

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
                HelperLogin.mostrarStado(lblStatus,
                        "¡Hay repeticiones! Solo los jugadores con el mismo valor volverán a tirar.", true, true,
                        "alert-warning");
            }
        } while (hayRepetidos);
        HelperLogin.mostrarStado(lblStatus, "Todos los jugadores tienen valores únicos!", true, true, "alert-success");
        turnoActual = 0;
        // Ordenar la lista de jugadores por el número que tiraron (de mayor a menor)
        LinkedList<Jugador> jugadoresLista = jugadoresDisponibles.getUsuarios();
        jugadoresLista.sort((j1, j2) -> Integer.compare(j2.getResultadoDado(), j1.getResultadoDado()));
    }

    @FXML
    public void empezarJuego(ActionEvent event) {
        ArrayList<String> jugadores = new ArrayList<>(listaJugadoresTablero.getItems());

        jugadoresDisponibles.getUsuarios().removeIf(jugador -> !jugadores.contains(jugador.getAlias()));
        mostrarDialogoSecuencailes();

        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/org/example/juego/TableroView.fxml"));
            Parent root = loader.load();
            Stage tableroStage = new Stage();

            tableroStage.setTitle("Tablero Trivia");
            tableroStage.setScene(new Scene(root));

            TableroController tableroController = loader.getController();
            tableroController.setJugadores(jugadoresDisponibles);
            
            tableroStage.initModality(Modality.APPLICATION_MODAL);
            tableroStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            tableroStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Añadir clases BootstrapFX a los controles principales
        // if (btnAgregar != null) btnAgregar.getStyleClass().addAll("btn",
        // "btn-primary");
        if (btnJugar != null)
            btnJugar.getStyleClass().addAll("btn", "btn-success");
        // if (btnQuitar != null) btnQuitar.getStyleClass().addAll("btn", "btn-danger");
        if (btnVolver != null)
            btnVolver.getStyleClass().addAll("btn", "btn-secondary");
        if (btndado != null)
            btndado.getStyleClass().addAll("btn", "btn-warning");
        if (lblStatus != null)
            lblStatus.getStyleClass().addAll("alert");
        if (lbldatosdeljugador != null)
            lbldatosdeljugador.getStyleClass().addAll("lead");
        if (lblaliasjugador != null)
            lblaliasjugador.getStyleClass().addAll("lead");
        if (listaJugadoresDisponibles != null)
            listaJugadoresDisponibles.getStyleClass().addAll("list-group");
        if (listaJugadoresTablero != null)
            listaJugadoresTablero.getStyleClass().addAll("list-group");
    } */
}
