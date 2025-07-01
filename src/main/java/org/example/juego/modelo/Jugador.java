package org.example.juego.modelo;

/**
 * Clase que representa a un jugador del juego.
 * Contiene información básica como correo, alias y el resultado del dado.
 */
public class Jugador {
    private final String correo;
    private final String alias;
    /**
     * Resultado obtenido por el jugador al lanzar el dado.
     */
    private int resultadoDado;

    /**
     * Crea un nuevo jugador con correo y alias especificados.
     * El resultado del dado se inicializa en 0.
     *
     * @param correo Correo electrónico del jugador.
     * @param alias  Alias o nombre de usuario del jugador.
     */
    public Jugador(String correo, String alias) {
        this.correo = correo;
        this.alias = alias;
        this.resultadoDado = 0; // Inicializamos en 0
    }

    /**
     * Obtiene el correo electrónico del jugador.
     *
     * @return Correo electrónico del jugador.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Obtiene el alias del jugador.
     *
     * @return Alias del jugador.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Obtiene el resultado actual del dado para el jugador.
     *
     * @return Resultado del dado.
     */
    public int getResultadoDado() {
        return resultadoDado;
    }

    /**
     * Establece el resultado del dado para el jugador.
     *
     * @param resultadoDado Nuevo resultado del dado.
     */
    public void setResultadoDado(int resultadoDado) {
        this.resultadoDado = resultadoDado;
    }
}
