package persistencia;

public class Documento {
    private Long   idDocumento;
    private String nombre;
    private String codigo;        // opcional
    private String descripcion;   // opcional
    private long   idCategoria;
    private String tipoAcceso;        // PUBLICO | INTERNO | RESERVADO
    private String disposicionFinal;  // CONSERVAR | TRANSFERIR | ELIMINAR
    private String fileName;
    private String fileType;      // PDF | WORD | EXCEL | OTRO
    private long   fileSizeBytes;
    private String departamento;
    private String municipio;
    private String usuario_Demo;
    private String correo_demo;
    private String fecha_creacion;
    private String fecha_actualizacion;

    // --- Getters/Setters mínimos usados por el DAO ---
    
    public Boolean esPublico() { return "publico".equals(this.tipoAcceso.toLowerCase()); }
    
    public Long getIdDocumento() { return idDocumento; }
    public void setIdDocumento(long v) { this.idDocumento = v; }

    public String getNombre() { return nombre; }
    public void setNombre(String v) { this.nombre = v; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String v) { this.codigo = v; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String v) { this.descripcion = v; }

    public long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(long v) { this.idCategoria = v; }

    public String getTipoAcceso() { return tipoAcceso; }
    public void setTipoAcceso(String v) { this.tipoAcceso = v; }

    public String getDisposicionFinal() { return disposicionFinal; }
    public void setDisposicionFinal(String v) { this.disposicionFinal = v; }

    public String getFileName() { return fileName; }
    public void setFileName(String v) { this.fileName = v; }

    public String getFileType() { return fileType; }
    public void setFileType(String v) { this.fileType = v; }

    public long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(long v) { this.fileSizeBytes = v; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String v) { this.departamento = v; }

    public String getMunicipio() { return municipio; }
    public void setMunicipio(String v) { this.municipio = v; }

    public String getUsuario_Demo() { return usuario_Demo;}
    public void setUsuario_Demo(String usuario_Demo) { this.usuario_Demo = usuario_Demo; }
    
    public String getCorreo_demo() { return correo_demo; }
    public void setCorreo_demo(String correo_demo) { this.correo_demo = correo_demo; }
    
    public String getFechaCreacion() { return fecha_creacion;}
    public void setFechaCreacion(String fecha_creacion) { this.fecha_creacion = fecha_creacion; }
    
    public String getFechaActualizacion() { return fecha_actualizacion; }
    public void setFechaActualizacion(String fecha_actualizacion) { this.fecha_actualizacion = fecha_actualizacion; }
    
}
