package persistencia;

import java.text.Normalizer;


public abstract class Rol {
    
    // Permite limpiar la cadena de texto para que sea valido
    
    public String limpiarTexto(String texto) {
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD);
        sinTildes = sinTildes.replaceAll("\\p{M}", "");

        return sinTildes.toLowerCase();
    }
    
    // Establecer el municipio del documento y el documento a tratar para verificar 
    
    public boolean mismaDependencia(Documento documento, String municipio) {
        String municipioDocumento = limpiarTexto(documento.getMunicipio());
        String municipioEntrada = limpiarTexto(municipio);

        return municipioDocumento.equals(municipioEntrada);    }

    public abstract boolean tieneAccesoVer(Documento documento, String municipio);
    public abstract boolean tieneAccesoEditar();
    public abstract boolean tieneAccesoEliminar();
    public abstract boolean tieneAccesoSubir();
    public abstract boolean tieneAccesoCategoria();
}
