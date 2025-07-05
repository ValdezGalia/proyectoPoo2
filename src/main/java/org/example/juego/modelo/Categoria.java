package org.example.juego.modelo;

import javafx.scene.paint.Color;

public enum Categoria {
    GEOGRAFIA,
    HISTORIA,
    CIENCIASYNATURZALEZA,
    ARTEYLITERATURA,
    DEPORTESYPASATIEMPOS,
    ENTRETENIMIENTO;

    public Color getColor() {
        switch (this) {
            case GEOGRAFIA:
                return Color.BLUE;
            case HISTORIA:
                return Color.YELLOW;
            case CIENCIASYNATURZALEZA:
                return Color.GREEN;
            case ARTEYLITERATURA:
                return Color.PURPLE;
            case DEPORTESYPASATIEMPOS:
                return Color.ORANGE;
            case ENTRETENIMIENTO:
                return Color.PINK;
            default:
                return Color.BLACK; // Default color if none matches
        }
    }
}
