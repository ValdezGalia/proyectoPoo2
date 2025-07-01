package org.example.juego.modelo;

public class Jugador {
    private final String correo;
    private final String alias;
    private int resultadoDado;


    public Jugador(String correo, String alias) {
        this.correo = correo;
        this.alias = alias;
        this.resultadoDado = 0; // Inicializamos en 0
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
}
