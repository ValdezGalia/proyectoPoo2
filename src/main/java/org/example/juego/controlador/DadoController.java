package org.example.juego.controlador;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Random;

public class DadoController {
    private int valor;
    @FXML
    private ImageView imagenDado;

    @FXML
    private void animarLanzamiento(ImageView imagenDado) {
        Timeline timeline = new Timeline();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int retraso = i * 100;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(retraso), e -> {
                int cara = random.nextInt(6) + 1;
                imagenDado.setImage(new Image("/img/dado" + cara + ".png"));
            }));
        }

        timeline.setOnFinished(e -> {
            int resultadoFinal = random.nextInt(6) + 1;
            imagenDado.setImage(new Image("/img/dado" + resultadoFinal + ".png"));
            System.out.println("Resultado final: " + resultadoFinal);
        });

        timeline.play();
    }    public void lanzar() {
        valor = (int) (Math.random() * 6) + 1;
    }

    public int getValor() {
        return valor;
    }
    @FXML
    public void clickDado(ActionEvent actionEvent) {
        animarLanzamiento(imagenDado);
    }
}
