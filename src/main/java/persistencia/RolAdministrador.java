package persistencia;

import java.util.ArrayList;
import java.util.List;


public class RolAdministrador extends Rol{
    
    List<String> listaVacia = new ArrayList<>();

    public RolAdministrador() {  
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
    
    @Override
    public boolean tieneAccesoCategoria() {
        return true;
    }
    
    @Override
    public boolean tieneAccesoUsuario() {
        return true;
    }

}
