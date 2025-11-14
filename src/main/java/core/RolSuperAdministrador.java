package core;

import java.util.ArrayList;
import java.util.List;


public class RolSuperAdministrador extends Rol{
    
    List<String> listaVacia = new ArrayList<>();

    public RolSuperAdministrador() {
        listaVacia.add("publico");
        listaVacia.add("interno");
        listaVacia.add("reservado");
    }

    @Override
    public boolean tieneAccesoVer(Documento documento, String municipio) {
        return true;
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
