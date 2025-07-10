package org.example.juego.modelo;

import org.example.juego.modelo.Jugador;

public class ResultadoTurno {

    private final Jugador jugador;

    private int valorDado;

    public ResultadoTurno(Jugador jugador, int valorDado) {
        this.jugador = jugador;
        this.valorDado = valorDado;
    }

    public ResultadoTurno(Jugador jugador) {
        this(jugador, 0);
    }

    public Jugador getJugador() {
        return jugador;
    }

    public int getValorDado() {
        return valorDado;
    }

    public void setValorDado(int valorDado) {
        this.valorDado = valorDado;
    }

    @Override
    public String toString() {
        return jugador.getAlias() + " → " + valorDado;
    }
}