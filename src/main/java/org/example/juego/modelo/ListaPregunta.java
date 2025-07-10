package org.example.juego.modelo;

import java.util.ArrayList;
import java.util.List;

public class ListaPregunta {
    private final List<Pregunta> preguntas;

    public ListaPregunta() {
        this.preguntas = new ArrayList<>();
    }

    public void agregarPregunta(Pregunta pregunta) {
        this.preguntas.add(pregunta);
    }

    public void agregarPregunta(String pregunta, String respuesta, String categoria) {
        preguntas.add(new Pregunta(pregunta, respuesta, categoria));
    }

    public Pregunta getPregunta(int index) {
        return preguntas.get(index);
    }

    public int getCantidadPreguntas() {
        return preguntas.size();
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public List<Pregunta> buscarPorCategoria(String categoria) {
        List<Pregunta> resultado = new ArrayList<>();
        for (Pregunta p : preguntas) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}
