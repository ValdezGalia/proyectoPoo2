package org.example.juego.modelo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Jugador {
    private final String correo;
    private final String alias;
    private int resultadoDado;
    private int filaActual;
    private int columnaActual;
    private boolean rendido = false; // Agregado para soportar la lógica de rendición

    // Mapa para contar categorías respondidas correctamente
    private final Map<String, Integer> categoriasRespondidas = new HashMap<>();

    // Estado de quesitos rellenados por categoría
    private final Set<String> quesitosRellenos = new HashSet<>();

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

    public void asignarCategoria(String categoria) {
        categoriasRespondidas.put(categoria, categoriasRespondidas.getOrDefault(categoria, 0) + 1);
    }

    public int getCantidadCategoria(String categoria) {
        return categoriasRespondidas.getOrDefault(categoria, 0);
    }

    public Map<String, Integer> getCategoriasRespondidas() {
        return categoriasRespondidas;
    }

    public void rellenarQuesitoCategoria(String categoria) {
        quesitosRellenos.add(categoria.toLowerCase());
    }

    public boolean tieneQuesitoCategoria(String categoria) {
        return quesitosRellenos.contains(categoria.toLowerCase());
    }

    public Set<String> getQuesitosRellenos() {
        return quesitosRellenos;
    }

    public boolean isRendido() {
        return rendido;
    }

    public void setRendido(boolean rendido) {
        this.rendido = rendido;
    }

    // Devuelve la cantidad de categorías acertadas (quesitos rellenados)
    public int getCategoriasAcertadas() {
        return quesitosRellenos.size();
    }

    // Simulación de tiempo total de juego (puedes ajustar la lógica real)
    private long tiempoTotal = 0L;
    public long getTiempoTotal() {
        return tiempoTotal;
    }
    public void setTiempoTotal(long tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }
}
