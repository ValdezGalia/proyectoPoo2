package org.example.juego;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.example.juego.controlador.TableroController;
import org.example.juego.helpers.SaveGameUtil;

import java.io.IOException;
import java.util.Objects;

/**
 * Clase principal de la aplicación del juego.
 * Inicia la interfaz gráfica utilizando JavaFX wy carga la vista principal (IndexView.fxml).
 */
public class JuegoApplication extends Application {

    /**
     * Método de inicio de la aplicación JavaFX.
     * Configura la ventana principal, carga la vista y aplica estilos.
     *
     * @param stage Ventana principal de la aplicación.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JuegoApplication.class.getResource("IndexView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 360, 375);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/icono2.png"))));
        stage.setResizable(false);
        stage.setScene(scene);
        // Hook global para guardar partida al cerrar la app
        stage.setOnCloseRequest(event -> {
            TableroController ctrl = TableroController.getInstanciaActiva();
            if (ctrl != null && ctrl.getJugadoresDisponibles() != null) {
                long activos = ctrl.getJugadoresDisponibles().getUsuarios().stream().filter(j -> !j.isRendido()).count();
                try {
                    if (activos > 1) {
                        SaveGameUtil.guardarPartida(ctrl.getJugadoresDisponibles(), ctrl.getTurno());
                    } else {
                        SaveGameUtil.guardarPartida(null, 0);
                    }
                } catch (Exception e) {
                    System.err.println("Error guardando partida al cerrar la app: " + e.getMessage());
                }
            }
        });
        stage.show();
    }

    /**
     * Método principal. Lanza la aplicación JavaFX.
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        launch();
    }
}