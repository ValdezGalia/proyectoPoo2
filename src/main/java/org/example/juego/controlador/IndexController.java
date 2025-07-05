package org.example.juego.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.juego.JuegoApplication;
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
     * Abre la ventana modal para mostrar las estadísticas del juego.
     * Carga la vista correspondiente y configura la ventana modal.
     * @param event Evento de acción recibido desde la interfaz.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @FXML
    public void abrirEstadistica(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JuegoApplication.class.getResource("EstadisticaView.fxml"));
        Parent root = fxmlLoader.load();
        Stage ventanaModal = new Stage();
        ventanaModal.setTitle("Estadísticas");
        Scene scene = new Scene(root);
        ventanaModal.setScene(scene);
        ventanaModal.setMaximized(true); // Abrir en pantalla grande
        ventanaModal.initModality(Modality.APPLICATION_MODAL);
        ventanaModal.showAndWait();
    }
}