package org.example.juego.modelos.usuario;

import java.util.LinkedList;

public class ListaUsuario {
    private final String archivo = "usuarios.json";
    private LinkedList<Usuario> usuarios;

    public ListaUsuario(LinkedList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public ListaUsuario() { this.usuarios = new LinkedList<>(); }

    public LinkedList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(LinkedList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario buscarUsuarioCorreo(String correo){
        for(Usuario usuario : usuarios){
            if(usuario.getCorreo().equals(correo)){
                return usuario;
            }
        }
        return null;
    }

    public void insertarUsuario(Usuario usuario){
        this.usuarios.add(usuario);
    }
}
