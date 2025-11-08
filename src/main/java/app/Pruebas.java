/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import data.Db;

/**
 *
 * @author Juan Pablo
 */
public class Pruebas {
    
    
    public static void main(String[] args) {
        Db base = new Db();
        base.establecerConexion();
        base.cerrarConexion();
    }
    
}
