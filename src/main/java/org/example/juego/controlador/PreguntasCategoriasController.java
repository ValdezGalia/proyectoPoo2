package org.example.juego.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.juego.modelo.ListaPreguntas;

import java.util.function.Consumer;

public class PreguntasCategoriasController {
    // Componentes de la vista
    @FXML Pane paneColor;
    @FXML Label lblStatus;
    @FXML Label lblCategoria;
    @FXML Label lblPregunta;
    @FXML TextField txtFildRespuesta;
    @FXML Button btnEnviar;
    @FXML Button btnCancelar;

    // Atributos del controlador
    private Stage stage;
    private String respuesta;
    private boolean preguntaCancelada;
    private boolean preguntaEstaCorrecta;


    // ① callback que el TableroController va a inyectar
    private Consumer<Boolean> onAnswerComplete;

    /** ② método para inyectar la función de "siguiente turno" */
    public void setOnAnswerComplete(Consumer<Boolean> callback) {
        this.onAnswerComplete = callback;
    }

    @FXML
    private void initialize() {
        btnEnviar.setOnAction(evt -> {
            boolean correcto = txtFildRespuesta.getText().trim().equalsIgnoreCase(respuesta);
            // … aquí puedes mostrar feedback al usuario …
            // ③ notificar al TableroController
            if (onAnswerComplete != null) {
                onAnswerComplete.accept(correcto);
            }
            // cerrar modal
            stage.close();
        });

        btnCancelar.setOnAction(evt -> {
            if (onAnswerComplete != null) {
                onAnswerComplete.accept(false);
            }
            stage.close();
        });
    }

    @FXML public void onEnviarRespuesta(){
        String respuestaJugador = txtFildRespuesta.getText();

        if(respuestaJugador.isEmpty()){
            lblStatus.setText("Ingrese alguna respuesta o no la responda.");
            lblStatus.setVisible(true);
            return;
        }
        
        preguntaCancelada = false;
        if(respuestaJugador.trim().toUpperCase().equals(respuesta)){
            preguntaEstaCorrecta = true;
            stage.close();
        }
    }

    @FXML public void onCancelarPregunta(){
        preguntaCancelada = true;
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Pane getPaneColor() {
        return paneColor;
    }

    public Label getLblCategoria() {
        return lblCategoria;
    }

    public Label getLblPregunta() {
        return lblPregunta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public boolean isPreguntaCancelada() {
        return preguntaCancelada;
    }

    public boolean isPreguntaEstaCorrecta() {
        return preguntaEstaCorrecta;
    }
}
