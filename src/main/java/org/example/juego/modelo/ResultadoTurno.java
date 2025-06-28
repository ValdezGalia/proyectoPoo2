package org.example.juego.modelo;

import org.example.juego.modelo.Jugador;

public class ResultadoTurno {
    private final Jugador jugador;
    private int valorDado;

    /**
     * Constructor principal.
     * @param jugador   el jugador al que corresponde esta tirada
     * @param valorDado el valor obtenido en el dado
     */
    public ResultadoTurno(Jugador jugador, int valorDado) {
        this.jugador = jugador;
        this.valorDado = valorDado;
    }

    /**
     * Constructor de conveniencia si primero quieres crear el objeto
     * y asignar el valor más tarde.
     * @param jugador el jugador al que corresponde esta tirada
     */
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