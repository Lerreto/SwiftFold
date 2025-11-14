package core;

import java.text.Normalizer;


public abstract class Rol {
    
    
    public String limpiarTexto(String texto) {
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD);
        sinTildes = sinTildes.replaceAll("\\p{M}", "");

        return sinTildes.toLowerCase();
    }
    
    public boolean mismaDependencia(Documento documento, String municipio) {
        String municipioDocumento = limpiarTexto(documento.getMunicipio());
        String municipioEntrada = limpiarTexto(municipio);

        return municipioDocumento.equals(municipioEntrada);    }

    public abstract boolean tieneAccesoVer(Documento documento, String municipio);
    public abstract boolean tieneAccesoEditar();
    public abstract boolean tieneAccesoEliminar();
    public abstract boolean tieneAccesoSubir();
}
