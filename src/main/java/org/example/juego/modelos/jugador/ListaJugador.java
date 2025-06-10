package org.example.juego.modelos.jugador;

import java.util.LinkedList;

public class ListaJugador {
    private LinkedList<Jugador> jugadores;

    public ListaJugador() {
        this.jugadores = new LinkedList<>();
    }

    public ListaJugador(LinkedList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public LinkedList<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(LinkedList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public void insertarJugador(Jugador jugador) {
        this.jugadores.add(jugador);
    }
}
