package org.example.juego.modelo;

public class Pregunta {
    private final String pregunta;
    private final String respuesta;
    private final String categoria;

    public Pregunta(String pregunta, String respuesta, String categoria) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.categoria = categoria;
    }

    public String getPregunta() {
        return pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public String getCategoria() {
        return categoria;
    }
}
