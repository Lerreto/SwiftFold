package persistencia;


public final class PlantillasCorreo {


    /** Constantes de asuntos para cada tipo de notificación. */
    public static final class Asunto {
        public static final String NUEVO_USUARIO_REGISTRADO   = "SwiftFold: nuevo usuario registrado";
        public static final String USUARIO_ELIMINADO          = "SwiftFold: usuario eliminado del sistema";
        public static final String NUEVO_DOCUMENTO_CARGADO    = "SwiftFold: nuevo documento cargado";
        public static final String DOCUMENTO_ACTUALIZADO      = "SwiftFold: documento actualizado";
        public static final String CAMBIO_ROL_USUARIO         = "SwiftFold: cambio de rol de usuario";
        public static final String DOCUMENTO_ELIMINADO        = "SwiftFold: documento eliminado";
        public static String tituloDecorado() {
        return  """
                ████████████████████████████████████████████████████
                                                   ███████  SWIFTFOLD  ███████                                          
                                                         Sistema de Gestión Documental
                ████████████████████████████████████████████████████

                """;
        }
    }

    
    // ============================================================
    // 1. REGISTRO DE NUEVO USUARIO
    // ============================================================

    
    public static String cuerpoNuevoUsuarioRegistradoAdmin(Usuario nuevo) {
        StringBuilder sb = new StringBuilder();

        sb.append(Asunto.tituloDecorado())
          .append("Se ha registrado un NUEVO USUARIO en el sistema SwiftFold.\n\n")
          .append("Datos del usuario registrado:\n")
          .append("• Nombre completo: ").append(nuevo.getNombreCompleto()).append("\n")
          .append("• Correo electrónico: ").append(nuevo.getEmail()).append("\n")
          .append("• Número de contacto: ").append(nullToNA(nuevo.getTelefono())).append("\n")
          .append("• Cargo: ").append(nullToNA(nuevo.getCargo())).append("\n")
          .append("• Dependencia: ").append(nullToNA(nuevo.getDependencia())).append("\n")
          .append("• Municipio / Departamento: ")
             .append(nullToNA(nuevo.getMunicipio()))
             .append(" - ")
             .append(nullToNA(nuevo.getDepartamento()))
             .append("\n\n")
          .append("Origen del registro: formulario público de registro.\n\n")
          .append("Este mensaje es únicamente informativo para el administrador del sistema.");

        return sb.toString();
    }

    
    
    // ============================================================
    // 2. ELIMINACIÓN DE USUARIO
    // ============================================================


    public static String cuerpoUsuarioEliminado(Usuario actor, String emailEliminado) {
        String nombreActor = (actor != null) ? actor.getNombreCompleto() : "Usuario del sistema";
        String correoActor = (actor != null) ? actor.getEmail() : "N/D";

        StringBuilder sb = new StringBuilder();

        sb.append(Asunto.tituloDecorado())
          .append("Estimado administrador,\n\n")
          .append("Se ha eliminado un usuario del sistema de gestión documental SwiftFold.\n\n")
          .append("Detalles de la acción:\n")
          .append("• Usuario eliminado: ").append(emailEliminado).append("\n")
          .append("• Acción realizada por: ").append(nombreActor)
             .append(" (").append(correoActor).append(")\n\n")
          .append("Si esta acción no fue autorizada, revise la administración de usuarios en SwiftFold.\n\n")
          .append("Atentamente,\n")
          .append("SwiftFold – Sistema de Gestión Documental");

        return sb.toString();
    }

    
    
    
    // ============================================================
    // 3. NUEVO DOCUMENTO CARGADO
    // ============================================================


    public static String cuerpoNuevoDocumento(Documento d, Usuario actor, String nombreCategoria) {
        String nombreActor = (actor != null) ? actor.getNombreCompleto() : null;
        String correoActor = (actor != null) ? actor.getEmail() : null;

        StringBuilder sb = new StringBuilder();
        
        sb.append(Asunto.tituloDecorado())
          .append("Estimado usuario,\n\n")
          .append("Se ha cargado un nuevo documento en el sistema de gestión documental SwiftFold.\n\n")
          .append("Resumen del documento:\n")
          .append(resumenDocumentoBasico(d, nombreCategoria))
          .append("\n");

        if (nombreActor != null || correoActor != null) {
            sb.append("Acción realizada por:\n")
              .append("• ");
            if (nombreActor != null) {
                sb.append(nombreActor);
            } else {
                sb.append("Usuario del sistema");
            }
            if (correoActor != null) {
                sb.append(" (").append(correoActor).append(")");
            }
            sb.append("\n\n");
        }

        sb.append("Si requiere revisar el contenido o gestionar este documento,\n")
          .append("puede hacerlo desde el módulo de documentos de SwiftFold.\n\n")
          .append("Atentamente,\n")
          .append("SwiftFold – Sistema de Gestión Documental");

        return sb.toString();
    }

    
    
    // ============================================================
    // 4. DOCUMENTO ACTUALIZADO (ANTES / DESPUÉS)
    // ============================================================


    public static String cuerpoDocumentoActualizado(
            Documento antes,
            Documento despues,
            Usuario actor,
            String categoriaAntes,
            String categoriaDespues
    ) {
        String nombreActor = (actor != null) ? actor.getNombreCompleto() : null;
        String correoActor = (actor != null) ? actor.getEmail() : null;

        StringBuilder sb = new StringBuilder();

        sb.append(Asunto.tituloDecorado())
          .append("Estimado usuario,\n\n")
          .append("Se ha actualizado un documento en el sistema de gestión documental SwiftFold.\n\n")
          .append("Estado ANTES de la modificación:\n")
          .append(resumenDocumentoBasico(antes, categoriaAntes))
          .append("\n")
          .append("Estado DESPUÉS de la modificación:\n")
          .append(resumenDocumentoBasico(despues, categoriaDespues))
          .append("\n");

        if (nombreActor != null || correoActor != null) {
            sb.append("Acción realizada por:\n")
              .append("• ");
            if (nombreActor != null) {
                sb.append(nombreActor);
            } else {
                sb.append("Usuario del sistema");
            }
            if (correoActor != null) {
                sb.append(" (").append(correoActor).append(")");
            }
            sb.append("\n\n");
        }

        sb.append("Si necesita revisar el detalle, por favor ingrese al módulo de documentos en SwiftFold.\n\n")
          .append("Atentamente,\n")
          .append("SwiftFold – Sistema de Gestión Documental");

        return sb.toString();
    }

    
    
    // ============================================================
    // 5. CAMBIO DE ROL DE USUARIO
    // ============================================================


    public static String cuerpoCambioRolUsuario(
            Usuario actor,
            Usuario afectado,
            String rolAnterior,
            String rolNuevo
    ) {
        String nombreActor    = (actor != null) ? actor.getNombreCompleto() : "Usuario del sistema";
        String correoActor    = (actor != null) ? actor.getEmail() : "N/D";
        String nombreAfectado = (afectado != null) ? afectado.getNombreCompleto() : "Usuario afectado";
        String correoAfectado = (afectado != null) ? afectado.getEmail() : "N/D";

        StringBuilder sb = new StringBuilder();

        sb.append(Asunto.tituloDecorado())
        
          .append("Hola,\n\n")
          .append("Se ha realizado un CAMBIO DE ROL en el sistema de gestión documental SwiftFold.\n\n")
          .append("Usuario afectado:\n")
          .append("• Nombre: ").append(nombreAfectado).append("\n")
          .append("• Correo: ").append(correoAfectado).append("\n\n")
          .append("Cambio de rol:\n")
          .append("• Rol anterior: ").append(nullToNA(rolAnterior)).append("\n")
          .append("• Nuevo rol: ").append(nullToNA(rolNuevo)).append("\n\n")
          .append("Acción realizada por:\n")
          .append("• ").append(nombreActor).append(" (").append(correoActor).append(")\n\n")
          .append("Si no reconoces esta acción, revisa la administración de usuarios en SwiftFold.\n\n")
          .append("Atentamente,\n")
          .append("SwiftFold – Sistema de Gestión Documental");

        return sb.toString();
    }
    

    // ============================================================
    // 6. ELIMINAR DOCUMENTO
    // ============================================================
    
    
    public static String cuerpoDocumentoEliminado(Usuario actor, Documento d, String nombreCategoria) {

        String nombreActor = (actor != null) ? actor.getNombreCompleto() : "Usuario del sistema";
        String correoActor = (actor != null) ? actor.getEmail() : "N/D";

        StringBuilder sb = new StringBuilder();

        sb.append(Asunto.tituloDecorado())
          .append("Estimado usuario,\n\n")
          .append("Se ha eliminado un documento del sistema de gestión documental SwiftFold.\n\n")
          .append("Documento eliminado:\n")
          .append(resumenDocumentoBasico(d, nombreCategoria))
          .append("\n")
          .append("Acción realizada por:\n")
          .append("• ").append(nombreActor).append(" (").append(correoActor).append(")\n\n")
          .append("Si esta acción no fue autorizada o considera que pudo ser un error,\n")
          .append("por favor comuníquese con el administrador del sistema.\n\n")
          .append("Atentamente,\n")
          .append("SwiftFold – Sistema de Gestión Documental");

        return sb.toString();
    }

    
    

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    /** Resumen genérico de un documento (nombre, código, categoría, acceso, disposición, archivo y tamaño). */
    public static String resumenDocumentoBasico(Documento d, String nombreCategoria) {
        if (d == null) {
            return "• (Documento no disponible)\n";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("• Nombre: ").append(nullToNA(d.getNombre())).append("\n")
          .append("• Código: ");

        String codigo = d.getCodigo();
        if (codigo == null || codigo.isBlank()) {
            sb.append("Sin código");
        } else {
            sb.append(codigo);
        }
        sb.append("\n");

        sb.append("• Categoría: ").append(nullToNA(nombreCategoria)).append("\n")
          .append("• Tipo de acceso: ").append(nullToNA(d.getTipoAcceso())).append("\n")
          .append("• Disposición final: ").append(nullToNA(d.getDisposicionFinal())).append("\n")
          .append("• Archivo: ").append(nullToNA(d.getFileName()))
             .append(" (").append(nullToNA(d.getFileType())).append(")\n")
          .append("• Tamaño: ").append(d.getFileSizeBytes()).append(" bytes\n");

        return sb.toString();
    }

    private static String nullToNA(String value) {
        return (value == null || value.isBlank()) ? "N/D" : value;
    }
}