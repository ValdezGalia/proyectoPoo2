package org.example.juego.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.juego.modelo.ListaJugador;
import java.io.File;
import java.io.IOException;

public class SaveGameUtil {
    private static final String SAVE_FILE = "partida_guardada.json";

    public static void guardarPartida(ListaJugador jugadores, int turno) throws IOException {
        if (jugadores == null || jugadores.getUsuarios() == null || jugadores.getUsuarios().isEmpty()) {
            // No guardar ni eliminar el archivo si no hay jugadores
            System.out.println("[SaveGameUtil] No se guardó la partida: lista de jugadores vacía o nula.");
            return;
        }
        // Guardar siempre que haya al menos 1 jugador (incluyendo rendidos)
        ObjectMapper mapper = new ObjectMapper();
        SaveData data = new SaveData(jugadores, turno);
        mapper.writeValue(new File(SAVE_FILE), data);
        System.out.println("[SaveGameUtil] Partida guardada correctamente con " + jugadores.getUsuarios().size() + " jugadores (incluyendo rendidos).");
    }

    public static SaveData cargarPartida() throws IOException {
        File file = new File(SAVE_FILE);
        if (!file.exists() || file.length() == 0) {
            throw new IOException("No hay datos válidos para cargar la partida.");
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, SaveData.class);
    }

    public static boolean existePartidaGuardada() {
        return new File(SAVE_FILE).exists();
    }

    public static class SaveData {
        public ListaJugador jugadores;
        public int turno;
        public SaveData() {}
        public SaveData(ListaJugador jugadores, int turno) {
            this.jugadores = jugadores;
            this.turno = turno;
        }
    }
}
