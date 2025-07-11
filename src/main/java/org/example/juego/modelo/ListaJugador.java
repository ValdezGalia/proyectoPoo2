package org.example.juego.modelo;

import java.util.LinkedList;

public class ListaJugador {

    private LinkedList<Jugador> jugador;
    private static ListaJugador instancia;

    public ListaJugador(LinkedList<Jugador> jugador) {
        this.jugador = jugador;
    }


    public ListaJugador() { this.jugador = new LinkedList<>(); }

    public LinkedList<Jugador> getUsuarios() {
        return jugador;
    }

    public void setUsuarios(LinkedList<Jugador> jugador) {
        this.jugador = jugador;
    }

    public Jugador buscarUsuarioCorreo(String correo){
        for(Jugador jugador : this.jugador){
            if(jugador.getCorreo().equals(correo)){
                return jugador;
            }
        }
        return null;
    }

    public void insertarUsuario(Jugador jugador){
        this.jugador.add(jugador);
    }

    public void eliminarUsuario(Jugador jugador){
        this.jugador.remove(jugador);
    }

    public static ListaJugador getInstancia() {
        if (instancia == null) {
            instancia = new ListaJugador();
        }
        return instancia;
    }
}
