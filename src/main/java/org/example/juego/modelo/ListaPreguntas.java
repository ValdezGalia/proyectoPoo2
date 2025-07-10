package org.example.juego.modelo;

import java.util.LinkedList;

public class ListaPreguntas {
    private LinkedList<Pregunta> preguntas;

    public ListaPreguntas() {
        preguntas = new LinkedList<>();
    }

    public ListaPreguntas(LinkedList<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    public LinkedList<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(LinkedList<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    public void agregarPregunta(Pregunta pregunta){
        this.preguntas.add(pregunta);
    }

    public int sizeListaPreguntas(){
        return preguntas.size();
    }

    /**
     * Obtiene las preguntas de una categoria en concreto y retorna dicha lista con su categoria respondiente.
     * @param categoria cadena de texto de la categoria que queremos.
     * @return ListaPreguntas
     * */

    public ListaPreguntas buscarPreguntaCategoria(String categoria){
        ListaPreguntas preguntasFiltradas = new ListaPreguntas();
        for(Pregunta pregunta : preguntas){
            if(pregunta.getCategoria().equalsIgnoreCase(categoria)){
                preguntasFiltradas.agregarPregunta(pregunta);
            }
        }
        return preguntasFiltradas;
    }
}
