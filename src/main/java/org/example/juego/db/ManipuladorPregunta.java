package org.example.juego.db;

import org.example.juego.modelo.ListaPreguntas;
import org.example.juego.modelo.Pregunta;
import org.json.JSONObject;
import org.json.JSONArray;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ManipuladorPregunta implements IManipuladorPreguntas {
    private final String ruta = "preguntasJuegoTrivia_final.json";

    @Override
    public ListaPreguntas getListaPreguntas() {
        ListaPreguntas listaPregunta = new ListaPreguntas();
        try {
            // Lee el contenido del archivo JSON como una cadena
            String contenido = new String(Files.readAllBytes(Paths.get(ruta)));
            // Convierte la cadena leída en un objeto JSON principal
            JSONObject jsonCompleto = new JSONObject(contenido);
            // Recorre cada categoría en el archivo JSON
            for (String categoria : jsonCompleto.keySet()) {
                // Obtiene el arreglo de preguntas de la categoría actual
                JSONArray preguntas = jsonCompleto.getJSONArray(categoria);
                // Recorre cada pregunta de la categoría
                for (int i = 0; i < preguntas.length(); i++) {
                    JSONObject objPregunta = preguntas.getJSONObject(i);
                    String pregunta = objPregunta.getString("pregunta");
                    String respuesta = objPregunta.getString("respuesta");
                    // Agrega la pregunta a la lista, junto con su categoría
                    Pregunta preguntaJson = new Pregunta(pregunta, respuesta, categoria);
                    listaPregunta.agregarPregunta(preguntaJson);
                }
            }
            return listaPregunta;
        } catch (Exception e) {
            // Si ocurre cualquier error, retorna una lista vacía
            return new ListaPreguntas();
        }
    }
}
