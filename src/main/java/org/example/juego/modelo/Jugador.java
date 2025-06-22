package org.example.juego.modelo;

public class Jugador {
    private final String correo;
    private final String alias;
    private int ultimoResultadoDado;  // Aquí agregamos la propiedad

    public Jugador(String correo, String alias) {
        this.correo = correo;
        this.alias = alias;
        this.ultimoResultadoDado = 0; // Inicializamos en 0
    }

    public String getCorreo() {
        return correo;
    }

    public String getAlias() {
        return alias;
    }

    // Getter para obtener el último resultado del dado
    public int getUltimoResultadoDado() {
        return ultimoResultadoDado;
    }

    // Setter para cambiar el último resultado del dado
    public void setUltimoResultadoDado(int ultimoResultadoDado) {
        this.ultimoResultadoDado = ultimoResultadoDado;
    }
}
