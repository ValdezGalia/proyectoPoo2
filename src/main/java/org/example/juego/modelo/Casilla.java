package org.example.juego.modelo;

public class Casilla {
    private final int x, y;
    private String tipo;
    private String color;
    private boolean visible = true;

    public Casilla(int x, int y, String tipo) {
        this.x = x; this.y = y; this.tipo = tipo;
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public String getTipo() { return tipo; }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}