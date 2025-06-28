
package org.example.juego.controlador;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;

public class DadoController {
    private int valor;

    @FXML
    private ImageView imgDado;
    Random random = new Random();

    /**
     * Anima el dado y, cuando termine, invoca onResult con el valor final (1–6).
     */
    public void clickDado(Consumer<Integer> onResult) {
        // 1. Pinta la cara inicial
        imgDado.setImage(new Image(Objects.requireNonNull(
                getClass().getResource("/img/cara1.png")
        ).toExternalForm()));

        // 2. Crea animación
        Timeline timeline = new Timeline();
        for (int i = 0; i < 10; i++) {
            int delay = i * 100;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(delay), e -> {
                int cara = random.nextInt(6) + 1;
                imgDado.setImage(new Image(Objects.requireNonNull(
                        getClass().getResource("/img/cara" + cara + ".png")
                ).toExternalForm()));
            }));
        }

        // 3. Al finalizar, calcula el resultado y lo pasa al callback
        timeline.setOnFinished(e -> {
            int resultadoFinal = random.nextInt(6) + 1;
            imgDado.setImage(new Image(Objects.requireNonNull(
                    getClass().getResource("/img/cara" + resultadoFinal + ".png")
            ).toExternalForm()));
            this.valor = resultadoFinal;
            onResult.accept(resultadoFinal);
        });

        timeline.play();
    }

    @FXML
    public void start(int valor){
        imgDado.setImage(new Image(Objects.requireNonNull(getClass().getResource("/img/cara" + valor + ".png")).toExternalForm()));
    }
}
