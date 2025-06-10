package org.example.juego.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.ValidationMessage;
import net.synedra.validatorfx.Validator;
import org.example.juego.db.ManipuladorUsuario;
import static org.example.juego.helpers.HelperLogin.mostrarStado;
import org.example.juego.modelos.usuario.ListaUsuario;
import org.example.juego.modelos.usuario.Usuario;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LoginController {
    @FXML
    private Label lblStatus;
    @FXML
    private TextField txtFieldUsuario;
    @FXML
    private PasswordField txtFieldClave;

    private static boolean correoValido(String correo) {
        if (correo == null || correo.isEmpty()) return false;
        String caracteres = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern cumpleCorreo = Pattern.compile(caracteres);
        Matcher comparador = cumpleCorreo.matcher(correo);
        return comparador.matches();
    }

    @FXML
    protected void btnLogin(){
        ManipuladorUsuario manipulador = new ManipuladorUsuario();
        ListaUsuario usuariosPermitidos = manipulador.extraerDatoUsuario();

        String correo = txtFieldUsuario.getText().toLowerCase();
        String clave = txtFieldClave.getText();

        if(correo.isEmpty() && clave.isEmpty()){
            mostrarStado(lblStatus, "Los campos no pueden estar vacios.", true, true, "alert-warning");
            return;
        }else if(!correoValido(correo)){
            mostrarStado(lblStatus, "Ingrese un correo valido!", true, true, "alert-danger");
            return;
        }

        Usuario usuarioEncontrado = usuariosPermitidos.buscarUsuarioCorreo(correo);

        if(usuarioEncontrado == null){
            mostrarStado(lblStatus, "Usuario no encontrado", true, true, "alert-warning");
            return;
        }

        if(!usuarioEncontrado.getClave().equals(clave)){
            mostrarStado(lblStatus, "Contraseña invalida.", true, true, "alert-danger");
            return;
        }

        mostrarStado(lblStatus, "", false, false, "");
        System.out.println("Usuario logeado");
        txtFieldUsuario.setText("");
        txtFieldClave.setText("");

    }
}