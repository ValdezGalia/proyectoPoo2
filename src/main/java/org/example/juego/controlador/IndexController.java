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

/**
 * Controlador principal de la ventana de inicio del juego.
 * Permite abrir la ventana de selección de jugadores y cerrar la aplicación.
 */
public class IndexController {
    /**
     * Botón para salir de la aplicación.
     */
    @FXML
    private Button btnSalir;

    /**
     * Abre una ventana modal para seleccionar los jugadores.
     * Deshabilita el botón de jugar hasta que se ingresen los jugadores.
     *
     * @param event Evento de acción que dispara la apertura de la ventana.
     * @throws IOException Si ocurre un error al cargar la vista FXML.
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
}