package org.example.juego.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.juego.JuegoApplication;
import org.example.juego.helpers.SaveGameUtil;
import org.example.juego.modelo.ListaJugador;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.Objects;


public class IndexController {

    @FXML
    private Button btnSalir;


    /**
     * Abre la ventana modal para la selección de jugadores.
     * Carga la vista correspondiente, inicializa el controlador y configura la ventana modal.
     * @param event Evento de acción recibido desde la interfaz.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @FXML
    public void seleccionJugadores(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JuegoApplication.class.getResource("SeleccionJugadorView.fxml"));
        Parent root = fxmlLoader.load();
        Stage ventanaModal = new Stage();

        ventanaModal.setTitle("Jugadores");
        ventanaModal.setScene(new Scene(root));

        root.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        SeleccionJugadorController seleccionJugadorController = fxmlLoader.getController();
        seleccionJugadorController.ingresarJugadoresDisponibles();
        seleccionJugadorController.getBtnJugar().setDisable(true);

        ventanaModal.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/icono2.png"))));
        ventanaModal.setResizable(false);
        ventanaModal.initModality(Modality.APPLICATION_MODAL);
        ventanaModal.initOwner(((Node) event.getSource()).getScene().getWindow());
        ventanaModal.showAndWait();
    }

    /**
     * Cierra la ventana principal de la aplicación.
     */
    @FXML
    public void cerrarVentana() {
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.close();
    }

    /**
     * Carga una partida guardada y abre la ventana del tablero para continuar el juego.
     * Muestra un mensaje al usuario si no hay partida guardada o si ocurre un error al cargarla.
     * @param event Evento de acción recibido desde la interfaz.
     */
    @FXML
    public void cargarPartida(ActionEvent event) {
        try {
            if (!SaveGameUtil.existePartidaGuardada()) {
                mostrarMensaje("No hay partida guardada", "No se encontró ninguna partida guardada para cargar.");
                return;
            }
            SaveGameUtil.SaveData data = SaveGameUtil.cargarPartida();
            FXMLLoader fxmlLoader = new FXMLLoader(JuegoApplication.class.getResource("TableroView.fxml"));
            Parent root = fxmlLoader.load();
            Stage tableroStage = new Stage();
            tableroStage.setTitle("Tablero Trivia UCAB");
            tableroStage.setScene(new Scene(root));
            root.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            TableroController tableroController = fxmlLoader.getController();
            tableroController.setJugadores(data.jugadores);
            tableroController.setTurno(data.turno);
            tableroController.setStage(tableroStage);
            tableroStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/icono2.png"))));
            tableroStage.setResizable(false);
            tableroStage.initModality(Modality.APPLICATION_MODAL);
            tableroStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            tableroStage.showAndWait();
        } catch (Exception e) {
            mostrarMensaje("Error", "No se pudo cargar la partida: " + e.getMessage());
        }
    }

    /**
     * Abre la ventana de estadísticas de jugadores.
     */
    @FXML
    public void mostrarEstadisticas(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JuegoApplication.class.getResource("EstadisticasView.fxml"));
        Parent root = fxmlLoader.load();
        Stage ventanaEstadisticas = new Stage();
        ventanaEstadisticas.setTitle("Estadísticas de Jugadores");
        ventanaEstadisticas.setScene(new Scene(root));
        ventanaEstadisticas.initModality(Modality.APPLICATION_MODAL);
        root.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        ventanaEstadisticas.showAndWait();
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(titulo);
        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setStyle("-fx-padding: 35; -fx-background-color: #f8f9fa; -fx-background-radius: 18; -fx-border-radius: 18; -fx-border-width: 3; -fx-border-color: #1a5276; -fx-alignment: center;");
        Label label = new Label(mensaje);
        label.setStyle("-fx-font-size: 17px; -fx-text-fill: #1a5276; -fx-font-family: 'Consolas Bold'; -fx-font-weight: bold; -fx-padding: 10 0 10 0;");
        Button btnContinuar = new Button("Continuar");
        btnContinuar.setStyle("-fx-background-color: #1a5276; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 30 8 30; -fx-cursor: hand;");
        btnContinuar.setOnAction(e -> dialog.close());
        vbox.getChildren().addAll(label, btnContinuar);
        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(org.kordamp.bootstrapfx.BootstrapFX.bootstrapFXStylesheet());
        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.showAndWait();
    }
}