package persistencia;

import java.util.ArrayList;
import java.util.List;

public class RolFuncionario extends Rol{
    
    List<String> listaVacia = new ArrayList<>();

    public RolFuncionario() {
        listaVacia.add("publico");
        listaVacia.add("interno");
    }

    @Override
    public boolean tieneAccesoVer(Documento documento, String municipio) {
        String accesoLimpio = limpiarTexto(documento.getTipoAcceso());
        return ((mismaDependencia(documento, municipio) && listaVacia.contains(accesoLimpio)) || ("publico".equals(accesoLimpio)));
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
    
    @Override
    public boolean tieneAccesoCategoria() {
        return false;
    }
    
    @Override
    public boolean tieneAccesoUsuario() {
        return false;
    }
    
    @Override
    public boolean tieneAccesoEliminarUsuario() {
        return false;
    }
    
    @Override
    public boolean tieneAccesoPDF() {
        return false;
    }
    
}
