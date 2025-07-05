package org.example.juego.modelo;

import java.util.Map;
import java.util.EnumMap;

public class Jugador {
    private final String correo;
    private final String alias;
    private int resultadoDado;
    private Map<Categoria, Integer> categorias;

    public Jugador(String alias, String correo, int resultadoDado, Map<Categoria, Integer> categorias) { //generamos un constructor que nos permite manejar el treeMap
        this.alias = alias;
        this.correo = correo;
        this.resultadoDado = resultadoDado;
        this.categorias = categorias;
    }

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

    public Integer getAciertosEnCategorias(Categoria categoria) {
        return categorias.get(categoria);
    }

    public Integer getAciertosEnCategorias(String categoria) {
        return categorias.get(categoria);
    }

    public int sumarAciertosACategoria(Categoria categoria) {
        if (categorias.containsKey(categoria)) {
            int aciertosActuales = categorias.get(categoria);
            categorias.put(categoria, aciertosActuales + 1);
            return aciertosActuales + 1;
        } else {
            categorias.put(categoria, 1);
            return 1;
        }
    }


    //cada jugador tiene un mapa de categorías y sus respectivos aciertos
    public Map<Categoria, Integer> getCategorias() {
        return this.categorias;
    }

    public void ponerCategorias() {
        this.categorias= new EnumMap<>(Categoria.class);
        // Inicializar todas las categorías con valor 0
        for (Categoria cat : Categoria.values()) {
            this.categorias.put(cat, 0);
        }
        System.out.println(this.categorias);
    }
}
