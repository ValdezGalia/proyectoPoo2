package org.example.juego.modelo;

public class Jugador {
    private final String correo;
    private final String alias;
    private int resultadoDado;
    private int filaActual;
    private int columnaActual;

    public Jugador(String correo, String alias) {
        this.correo = correo;
        this.alias = alias;
        this.resultadoDado = 0; // Inicializamos en 0
        this.filaActual = 10;
        this.columnaActual = 10;
    }

    public String getCorreo() {
        return correo;
    }

    public String getAlias() {
        return alias;
    }


    public int getResultadoDado() {
        return resultadoDado;
    }


    public void setResultadoDado(int resultadoDado) {
        this.resultadoDado = resultadoDado;
    }

    public int getFilaActual() {
        return filaActual;
    }

    public void setFilaActual(int filaActual) {
        this.filaActual = filaActual;
    }

    public int getColumnaActual() {
        return columnaActual;
    }

    public void setColumnaActual(int columnaActual) {
        this.columnaActual = columnaActual;
    }
}
