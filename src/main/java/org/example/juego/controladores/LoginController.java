package org.example.juego.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoginController {
    @FXML
    private Label lblStatus;

    @FXML
    protected void mostrarEstado(){
        lblStatus.setText("Error al mostrar estado");
        lblStatus.setVisible(true);
        lblStatus.setManaged(true);
    }
}