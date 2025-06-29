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
     * Resalta el sector indicado (1 a 6) con un color vivo y apaga los demÃ¡s.
     * @param numero Sector a resaltar (1 a 6)
     */
    public void resaltarSector(int numero) {
        // for (int i = 0; i < arcs.length; i++) {
        //     arcs[i].setFill(mutedColors[i]);
        // }
        if (numero >= 1 && numero <= 6) {
            arcs[numero - 1].setFill(highlightColors[numero - 1]);
        }
    }

    @FXML
    private void onResaltarAleatorio(ActionEvent event) {
        int sector = 1 + random.nextInt(6);
        resaltarSector(sector);
    }
}
