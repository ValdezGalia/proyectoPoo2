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

/**
 * Controlador para la lógica del dado en la interfaz gráfica.
 * Permite animar el lanzamiento del dado y obtener su valor.
 */
public class DadoController {
    /**
     * Valor actual del dado tras el lanzamiento.
     */
    private int valor;

    /**
     * Imagen del dado en la interfaz.
     */
    @FXML
    private ImageView imgDado;

    private final Random random = new Random();

    /**
     * Anima el dado y, cuando termine, invoca el callback con el valor final (1–6).
     * @param onResult función que recibe el valor final del dado tras la animación
     */
    public void clickDado(Consumer<Integer> onResult) {
        // Pinta la cara inicial
        imgDado.setImage(new Image(Objects.requireNonNull(
                getClass().getResource("/img/cara1.png")
        ).toExternalForm()));

        // Crea animación
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

        // Al finalizar, calcula el resultado y lo pasa al callback
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

    /**
     * Devuelve el valor actual del dado.
     * @return valor del dado (1–6)
     */
    public int getValor() {
        return valor;
    }

    /**
     * Muestra la imagen correspondiente al valor del dado recibido.
     * @param valor valor del dado a mostrar (1–6)
     */
    @FXML
    public void start(int valor){
        imgDado.setImage(new Image(Objects.requireNonNull(getClass().getResource("/img/cara" + valor + ".png")).toExternalForm()));
    }
}
