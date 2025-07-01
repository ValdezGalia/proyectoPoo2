package org.example.juego.db;

import org.example.juego.modelo.ListaJugador;
import org.example.juego.modelo.Jugador;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Clase que implementa la interfaz DataBase para manipular usuarios almacenados en un archivo JSON.
 * Permite extraer los datos de los usuarios y convertirlos en una lista de Jugador.
 */
public class ManipuladorUsuario implements DataBase{
    private final String archivo = "usuarios.json";

    /**
     * Extrae los datos de los usuarios desde el archivo JSON y los convierte en una ListaJugador.
     * Si ocurre un error al leer o procesar el archivo, retorna una lista vacía.
     *
     * @return ListaJugador con los usuarios extraídos del archivo JSON.
     */
    @Override
    public ListaJugador extraerDatoUsuario() {
        ListaJugador listaJugador = new ListaJugador();
        try {
            // Obtengo el contenido del archivo en formato cadena
            String contenido = new String(Files.readAllBytes(Paths.get(archivo)));

            // Convierto el contenido de la cadena a un arrayList
            JSONArray arregloJSON = new JSONArray(contenido);
            for (int i = 0; i < arregloJSON.length(); i++) {
                // Trabajo esos datos de forma independiente como objetos json y me genera un MAP<key, value>
                JSONObject objetoJSONUsuario = arregloJSON.getJSONObject(i);

                // Extraigo los datos del usuario de forma independiente
                String correo = objetoJSONUsuario.getString("correo");
                String alias = objetoJSONUsuario.getString("alias");

                // Lo inserto a la lista vaciá
                listaJugador.insertarUsuario(new Jugador(correo, alias));
            }
        } catch (Exception e) {
            return new ListaJugador();
        }

        return listaJugador;
    }
}
