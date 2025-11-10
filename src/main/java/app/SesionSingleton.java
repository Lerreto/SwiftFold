/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import core.UsuarioPrueba;

/**
 *
 * @author Juan Pablo
 */
public class SesionSingleton {

    private static SesionSingleton instance;
    private UsuarioPrueba usuarioLogueado;

    private SesionSingleton() {}

    public static SesionSingleton getInstance() {
        if (instance == null) {
            instance = new SesionSingleton();
        }
        return instance;
    }

    // Método para establecer el usuario logueado
    public void setUsuarioLogueado(UsuarioPrueba usuario) {
        this.usuarioLogueado = usuario;
    }

    // Método para obtener el usuario logueado
    public UsuarioPrueba getUsuarioLogueado() {
        return usuarioLogueado;
    }
}
