package org.example.juego.controlador;

import javafx.fxml.Initializable;
import org.example.juego.modelo.Jugador;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TableroController {
    private List<Jugador> setJugadoresOrdenados;

    public void setJugadoresOrdenados(List<Jugador> setJugadoresOrdenados) {
        this.setJugadoresOrdenados = setJugadoresOrdenados;
    }
}
