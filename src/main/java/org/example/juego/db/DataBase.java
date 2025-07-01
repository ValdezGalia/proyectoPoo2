package org.example.juego.db;

import org.example.juego.modelo.ListaJugador;

/**
 * Interfaz que define las operaciones básicas para la extracción de datos de usuarios desde una base de datos.
 */
public interface DataBase {
    /**
     * Extrae los datos de los usuarios almacenados en la base de datos.
     *
     * @return ListaJugador que contiene todos los usuarios extraídos.
     */
    public ListaJugador extraerDatoUsuario();
}
