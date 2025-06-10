package org.example.juego.db;

import org.example.juego.modelos.usuario.ListaUsuario;
import org.example.juego.modelos.usuario.Usuario;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ManipuladorUsuario implements DataBase{
    private final String archivo = "usuarios.json";

    @Override
    public ListaUsuario extraerDatoUsuario() {
        ListaUsuario listaUsuario = new ListaUsuario();;
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
                String clave = objetoJSONUsuario.getString("clave");

                // Lo inserto a la lista vaciá
                listaUsuario.insertarUsuario(new Usuario(correo, clave));
            }
        } catch (Exception e) {
            return new ListaUsuario();
        }

        return listaUsuario;
    }
}
