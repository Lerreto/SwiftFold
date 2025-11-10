/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import core.Usuario;

/**
 *
 * @author Juan Pablo
 */
public class SesionSingleton {

    private static SesionSingleton instance;
    private Usuario usuarioLogueado;

    private SesionSingleton() {}

    public static SesionSingleton getInstance() {
        if (instance == null) {
            instance = new SesionSingleton();
        }
        return instance;
    }

    // Método para establecer el usuario logueado
    public void setUsuarioLogueado(Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    // Método para obtener el usuario logueado
    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
}
