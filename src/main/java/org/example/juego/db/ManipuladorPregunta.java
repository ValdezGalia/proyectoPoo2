package org.example.juego.db;

import org.example.juego.modelo.ListaPregunta;
import org.example.juego.modelo.Pregunta;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ManipuladorPregunta implements IManipuladorPregunta {
    // Nombre del archivo JSON que contiene las preguntas
    private final String archivo = "preguntasJuegoTrivia_final.json";

    /**
     * Extrae todas las preguntas del archivo JSON y las convierte en una ListaPregunta.
     * Si ocurre un error, retorna una lista vacía.
     * @return ListaPregunta con todas las preguntas extraídas del archivo.
     */
    @Override
    public ListaPregunta extraerDatoPregunta() {
        ListaPregunta listaPregunta = new ListaPregunta();
        try {
            // Lee el contenido del archivo JSON como una cadena
            String contenido = new String(Files.readAllBytes(Paths.get(archivo)));
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
                    listaPregunta.agregarPregunta(pregunta, respuesta, categoria);
                }
            }
        } catch (Exception e) {
            // Si ocurre cualquier error, retorna una lista vacía
            return new ListaPregunta();
        }
        return listaPregunta;
    }
}
