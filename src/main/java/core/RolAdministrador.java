/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juan Pablo
 */
public class RolAdministrador extends Rol{
    
    List<String> listaVacia = new ArrayList<>();

    public RolAdministrador() {
        super("administrador");
        
        listaVacia.add("publico");
        listaVacia.add("interno");
        listaVacia.add("reservado");
    }

    @Override
    public boolean tieneAccesoVer(Documento documento, String municipio) {
        return mismaDependencia(documento, municipio);
    }

    @Override
    public boolean tieneAccesoEditar() {
        return true;
    }

    @Override
    public boolean tieneAccesoEliminar() {
        return true;
    }

    @Override
    public boolean tieneAccesoSubir() {
        return true;
    }

}
