package org.example.juego.modelo;

public class Jugador {
    private final String correo;
    private final String alias;

    public Jugador(String correo, String alias) {
        this.correo = correo;
        this.alias = alias;
    }

    public String getCorreo() {
        return correo;
    }

    public String getAlias() {
        return alias;
    }
}
