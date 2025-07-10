package org.example.juego.controlador;

import javafx.fxml.FXML;
import javafx.scene.shape.Arc;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import java.util.Random;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Group;


public class FichaJugadorController {
    @FXML
    private Arc arc1;
    @FXML
    private Arc arc2;
    @FXML
    private Arc arc3;
    @FXML
    private Arc arc4;
    @FXML
    private Arc arc5;
    @FXML
    private Arc arc6;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Group group;

    private final Random random = new Random();
    private Arc[] arcs;
    private final Color[] mutedColors = {
            Color.web("#ffffff"),
            Color.web("#ffffff"),
            Color.web("#ffffff"),
            Color.web("#ffffff"),
            Color.web("#ffffff"),
            Color.web("#ffffff")
    };
    private final Color[] highlightColors = {
            Color.RED, Color.ORANGE, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PURPLE
    };


    public void initialize() {
        arcs = new Arc[]{arc1, arc2, arc3, arc4, arc5, arc6};
        // Ajustar el radio de los arcos al 40% del lado menor del AnchorPane
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> ajustarRadioYCentrar());
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> ajustarRadioYCentrar());
        ajustarRadioYCentrar();
    }

    /**
     * Ajusta el radio de los arcos y centra el grupo dentro del panel raíz.
     * Calcula el radio en función del tamaño del panel y centra los elementos gráficos.
     */
    private void ajustarRadioYCentrar() {
        double w = rootPane.getWidth();
        double h = rootPane.getHeight();
        if (w <= 0 || h <= 0) return;
        double radio = 0.4 * Math.min(w, h);
        double center = radio;
        for (Arc arc : arcs) {
            arc.setRadiusX(radio);
            arc.setRadiusY(radio);
            arc.setCenterX(center);
            arc.setCenterY(center);
        }
        // Centrar el grupo
        if (group != null) {
            double groupWidth = radio * 2;
            double groupHeight = radio * 2;
            group.setLayoutX((w - groupWidth) / 2);
            group.setLayoutY((h - groupHeight) / 2);
        }
    }

    /**
     * Resalta el sector especificado cambiando su color.
     * Restaura los colores atenuados y resalta el sector indicado si está en el rango 1-6.
     * @param numero Número del sector a resaltar (1-6)
     */
    public void resaltarSector(int numero) {
        for (int i = 0; i < arcs.length; i++) {
            arcs[i].setFill(mutedColors[i]);
        }
        if (numero >= 1 && numero <= 6) {
            arcs[numero - 1].setFill(highlightColors[numero - 1]);
        }
    }

    /**
     * Evento de interfaz para resaltar un sector aleatorio.
     * Selecciona un sector al azar y lo resalta.
     * @param event Evento de acción recibido desde la interfaz.
     */
    @FXML
    private void onResaltarAleatorio(ActionEvent event) {
        int sector = 1 + random.nextInt(6);
        resaltarSector(sector);
    }

    public void rellenarQuesito(String categoria) {
        // Mapeo de categoría a índice de arco
        int index = switch (categoria.toLowerCase()) {
            case "historia" -> 4; // amarillo
            case "geografía", "geografia" -> 3; // verde
            case "arte y literatura" -> 0; // rojo
            case "entretenimiento" -> 5; // morado
            case "deportes" -> 1; // naranja
            case "ciencia" -> 2; // azul
            default -> -1;
        };
        if (index >= 0 && arcs != null && index < arcs.length) {
            arcs[index].setFill(highlightColors[index]);
        }
    }
}
