package org.example.juego.modelo;

import java.util.LinkedList;

/**
 * Clase que representa una lista de jugadores.
 * Proporciona métodos para gestionar y buscar jugadores dentro de la lista.
 */
public class ListaJugador {
    /**
     * Lista enlazada que almacena los objetos Jugador.
     */
    private LinkedList<Jugador> jugador;

    /**
     * Crea una nueva lista de jugadores a partir de una lista existente.
     *
     * @param jugador Lista enlazada de jugadores.
     */
    public ListaJugador(LinkedList<Jugador> jugador) {
        this.jugador = jugador;
    }

    /**
     * Crea una nueva lista de jugadores vacía.
     */
    public ListaJugador() { this.jugador = new LinkedList<>(); }

    /**
     * Obtiene la lista de jugadores.
     *
     * @return Lista enlazada de jugadores.
     */
    public LinkedList<Jugador> getUsuarios() {
        return jugador;
    }

    /**
     * Establece la lista de jugadores.
     *
     * @param jugador Nueva lista enlazada de jugadores.
     */
    public void setUsuarios(LinkedList<Jugador> jugador) {
        this.jugador = jugador;
    }

    /**
     * Busca un jugador por su correo electrónico.
     *
     * @param correo Correo electrónico a buscar.
     * @return El jugador encontrado o null si no existe.
     */
    public Jugador buscarUsuarioCorreo(String correo){
        for(Jugador jugador : this.jugador){
            if(jugador.getCorreo().equals(correo)){
                return jugador;
            }
        }
        return null;
    }

    /**
     * Inserta un nuevo jugador en la lista.
     *
     * @param jugador Jugador a insertar.
     */
    public void insertarUsuario(Jugador jugador){
        this.jugador.add(jugador);
    }

    /**
     * Elimina un jugador de la lista.
     *
     * @param jugador Jugador a eliminar.
     */
    public void eliminarUsuario(Jugador jugador){
        this.jugador.remove(jugador);
    }
}
