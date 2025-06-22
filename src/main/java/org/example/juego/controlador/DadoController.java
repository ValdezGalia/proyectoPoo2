package org.example.juego.controlador;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;

public class DadoController {
    private int valor;

    @FXML
    private ImageView imgDado;

    Random random = new Random();


    @FXML
    private void animarLanzamiento(ImageView imgDado) {
        Timeline timeline = new Timeline();
        for (int i = 0; i < 10; i++) {
            int retraso = i * 100;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(retraso), e -> {
                int cara = random.nextInt(6) + 1;
                imgDado.setImage(new Image(Objects.requireNonNull(getClass().getResource("/img/cara" + cara + ".png")).toExternalForm()));
            }));
        }
        timeline.setOnFinished(e -> {
            int resultadoFinal = random.nextInt(6) + 1;
            String path = "/img/cara" + resultadoFinal + ".png";
            imgDado.setImage(new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm()));
            System.out.println("Resultado final: " + resultadoFinal);
            valor = resultadoFinal;
            // Notificar al listener aquí, después de asignar el valor final
            if (getValorListener != null) {
                getValorListener.onDadoLanzado(valor);
            }
        });
        timeline.play();
    }

    public void lanzar() {
        valor = (int) (Math.random() * 6) + 1;
    }

    public int getValor() {
        return valor;
    }

    @FXML
    public void clickDado(javafx.scene.input.MouseEvent event) {
        imgDado.setImage(new Image(Objects.requireNonNull(getClass().getResource("/img/cara1.png")).toExternalForm()));
        animarLanzamiento(this.imgDado);
        // El listener se notificará al finalizar la animación
    }

    // Listener para notificar el valor del dado
    public interface GetValorListener {
        void onDadoLanzado(int valor);
    }
    private GetValorListener getValorListener;
    public void setGetValorListener(GetValorListener listener) {
        this.getValorListener = listener;
    }
    @FXML
    public void start(int valor){
        imgDado.setImage(new Image(Objects.requireNonNull(getClass().getResource("/img/cara"+valor+".png")).toExternalForm()));
    }
}
