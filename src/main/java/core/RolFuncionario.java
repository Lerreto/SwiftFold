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
public class RolFuncionario extends Rol{
    
        List<String> listaVacia = new ArrayList<>();

    public RolFuncionario() {
        super("administrador");
        
        listaVacia.add("publico");
        listaVacia.add("interno");
    }

    @Override
    public boolean tieneAccesoVer(Documento documento, String municipio) {
        String accesoLimpio = limpiarTexto(documento.getTipoAcceso());
        return (mismaDependencia(documento, municipio) || listaVacia.contains(accesoLimpio));
    }

    @Override
    public boolean tieneAccesoEditar() {
        return false;
    }

    @Override
    public boolean tieneAccesoEliminar() {
        return false;
    }

    @Override
    public boolean tieneAccesoSubir() {
        return false;
    }
    
    
}
