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

        ventanaModal.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/icono2.png"))));
        ventanaModal.setResizable(false);
        ventanaModal.initModality(Modality.APPLICATION_MODAL);
        ventanaModal.initOwner(((Node) event.getSource()).getScene().getWindow());
        ventanaModal.showAndWait();
    }

    @FXML
    public void cerrarVentana() {
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.close();
    }
}