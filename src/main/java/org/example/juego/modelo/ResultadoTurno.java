package org.example.juego.modelo;

import org.example.juego.modelo.Jugador;

/**
 * Clase que representa el resultado de un turno para un jugador.
 * Almacena el jugador y el valor obtenido en el dado durante su turno.
 */
public class ResultadoTurno {
    /**
     * Jugador al que corresponde esta tirada.
     */
    private final Jugador jugador;
    /**
     * Valor obtenido en el dado durante el turno.
     */
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

    /**
     * Obtiene el jugador asociado a este resultado de turno.
     * @return Jugador de la tirada.
     */
    public Jugador getJugador() {
        return jugador;
    }

    /**
     * Obtiene el valor del dado obtenido en el turno.
     * @return Valor del dado.
     */
    public int getValorDado() {
        return valorDado;
    }

    /**
     * Establece el valor del dado para este turno.
     * @param valorDado Nuevo valor del dado.
     */
    public void setValorDado(int valorDado) {
        this.valorDado = valorDado;
    }

    /**
     * Devuelve una representación en texto del resultado del turno.
     * @return Cadena con el alias del jugador y el valor del dado.
     */
    @Override
    public String toString() {
        return jugador.getAlias() + " → " + valorDado;
    }
}