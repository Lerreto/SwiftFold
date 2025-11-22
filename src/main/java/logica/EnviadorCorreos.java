package logica;

import persistencia.SesionSingleton;
import persistencia.Documento;
import persistencia.RolAdministrador;
import persistencia.RolFuncionario;
import persistencia.RolSecretario;
import persistencia.RolSuperAdministrador;
import persistencia.Usuario;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.List;
import java.util.Properties;
import persistencia.PlantillasCorreo;

public class EnviadorCorreos {

    // --- Datos del remitente ---
    private static final String REMITENTE = "swiftfold.proyecto@gmail.com";
    private static final String CLAVE_APP = "gbpq avtt icju csih"; // clave de aplicación, no tu contraseña normal
    
    
    private String obtenerNombreCategoriaSeguro(Documento d) {
        if (d == null) return "N/D";
        try {
            CategoriaDao categoriaDao = new CategoriaDao();
            return categoriaDao.nombrePorId(d.getIdCategoria());
        } catch (Exception e) {
            return "N/D"; // por si la categoría no existe o falla la BD
        }
    }

    

    // --- Crea la sesión SMTP con autenticación ---
    private static Session crearSesionSMTP() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");                 // el servidor requiere autenticación
        props.put("mail.smtp.starttls.enable", "true");      // usa TLS (cifrado seguro)
        props.put("mail.smtp.host", "smtp.gmail.com");       // host SMTP de Gmail
        props.put("mail.smtp.port", "587");                  // puerto TLS

        // Autenticación con el correo y la clave
        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMITENTE, CLAVE_APP);
            }
        });
    }
    
    

    // --- Método público para enviar un correo ---
    public static boolean enviarCorreo(String destinatario, String asunto, String mensaje) {
        try {
            Session session = crearSesionSMTP();

            // Crear el mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(REMITENTE));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(mensaje);

            // Enviar el correo
            Transport.send(message);

            System.out.println("Correo enviado correctamente a: " + destinatario);
            return true;

        } catch (MessagingException e) {
            System.err.println("Error al enviar correo a " + destinatario + ": " + e.getMessage());
            e.printStackTrace();
            return false;

        } catch (Throwable t) {
            // Cubre cosas como NoClassDefFoundError, etc., para que NO tumben la app
            System.err.println("Fallo inesperado al enviar correo a " + destinatario);
            t.printStackTrace();
            return false;
        }
    }


    
    
    // --- Método sobrecargado para enviar a varios destinatarios ---
    public static void enviarCorreoMultiple(String[] destinatarios, String asunto, String mensaje) {
        for (String correo : destinatarios) {
            enviarCorreo(correo, asunto, mensaje);
        }
    }
    
    
    
    // ============================================================
    // ENVIOS DE CORREOS DEPENDIENDO DE LAS ACCIONES CON CONDICIONES
    // ============================================================
    
    
    public void enviarCorreosAUsuariosConRoles(String asunto, String mensaje, Documento d) {
        List<Usuario> listaUsuarios = new UtilidadesDeArchivos().cargarUsuarios(); 

        boolean esPublico = d.esPublico();

        for (Usuario usuario : listaUsuarios) {
            if (esPublico) {
                // Documento público: secretarios, administradores y superadmins, sin importar municipio
                if (usuario.getRol() instanceof RolSecretario || 
                    usuario.getRol() instanceof RolAdministrador || 
                    usuario.getRol() instanceof RolSuperAdministrador) {

                    String destinatario = usuario.getEmail();
                    enviarCorreo(destinatario, asunto, mensaje);
                }
            } else {
                // Documento NO público: filtrar por municipio + rol, excepto superadmin (siempre recibe)
                boolean mismoMunicipio = usuario.getMunicipio()
                        .equals(SesionSingleton.getInstance().getUsuarioLogueado().getMunicipio());

                if ((usuario.getRol() instanceof RolSecretario && mismoMunicipio) || 
                    (usuario.getRol() instanceof RolAdministrador && mismoMunicipio) || 
                    (usuario.getRol() instanceof RolFuncionario && mismoMunicipio) ||
                     usuario.getRol() instanceof RolSuperAdministrador) {

                    String destinatario = usuario.getEmail();
                    enviarCorreo(destinatario, asunto, mensaje);
                }
            }
        }
    }
    
    // --- Notificaciones cuando se elimina o modifica un DOCUMENTO ---
    public void eliminarModificar(String mensaje, String asunto) {
        List<Usuario> listaUsuarios = new UtilidadesDeArchivos().cargarUsuarios(); 

        for (Usuario usuario : listaUsuarios) {
            boolean mismoMunicipio = usuario.getMunicipio()
                    .equals(SesionSingleton.getInstance().getUsuarioLogueado().getMunicipio());

            if ((usuario.getRol() instanceof RolSecretario && mismoMunicipio) || 
                (usuario.getRol() instanceof RolAdministrador && mismoMunicipio) || 
                (usuario.getRol() instanceof RolFuncionario && mismoMunicipio) ||
                 usuario.getRol() instanceof RolSuperAdministrador) {

                String destinatario = usuario.getEmail(); 
                enviarCorreo(destinatario, asunto, mensaje);
            }
        }
    }
    
    
    // --- Notificaciones cuando se elimina / modifica / registra un USUARIO ---
    public void nuevoUsuarioEliminarModificar(String mensaje, String asunto) {
        List<Usuario> listaUsuarios = new UtilidadesDeArchivos().cargarUsuarios(); 

        for (Usuario usuario : listaUsuarios) {
            if (usuario.getRol() instanceof RolSuperAdministrador) {
                String destinatario = usuario.getEmail(); 
                enviarCorreo(destinatario, asunto, mensaje);
            }
        }
    }
    
    
    
    // ============================================================
    // ENVIAR NOTIFICACIONES DEPENDIENDO DE LAS ACCIONES
    // ============================================================
    
    
    public void notificarDocumentoEliminado(Documento d) {
        Usuario actor = SesionSingleton.getInstance().getUsuarioLogueado();
        String nombreCategoria = obtenerNombreCategoriaSeguro(d);

        String asunto = PlantillasCorreo.Asunto.DOCUMENTO_ELIMINADO;
        String cuerpo = PlantillasCorreo.cuerpoDocumentoEliminado(actor, d, nombreCategoria);

        // Va a los mismos destinatarios que una modificación o eliminación de documento
        eliminarModificar(cuerpo, asunto);
    }

    
    public void notificarNuevoDocumento(Documento d) {
        Usuario actor = SesionSingleton.getInstance().getUsuarioLogueado();
        String nombreCategoria = obtenerNombreCategoriaSeguro(d);

        String asunto = PlantillasCorreo.Asunto.NUEVO_DOCUMENTO_CARGADO;
        String cuerpo = PlantillasCorreo.cuerpoNuevoDocumento(d, actor, nombreCategoria);

        // Aquí usamos la versión nueva con asunto + mensaje
        enviarCorreosAUsuariosConRoles(asunto, cuerpo, d);
    }

    
    public void notificarDocumentoActualizado(Documento antes, Documento despues) {
        Usuario actor = SesionSingleton.getInstance().getUsuarioLogueado();
        String categoriaAntes   = obtenerNombreCategoriaSeguro(antes);
        String categoriaDespues = obtenerNombreCategoriaSeguro(despues);

        String asunto = PlantillasCorreo.Asunto.DOCUMENTO_ACTUALIZADO;
        String cuerpo = PlantillasCorreo.cuerpoDocumentoActualizado(
                antes, despues, actor, categoriaAntes, categoriaDespues
        );

        // Reutilizas la lógica de "documento modificado" (roles + municipio)
        eliminarModificar(cuerpo, asunto);
    }

    // Nuevo usuario
    public void notificarNuevoUsuarioRegistrado(Usuario nuevo) {
        String asunto = PlantillasCorreo.Asunto.NUEVO_USUARIO_REGISTRADO;
        String cuerpo = PlantillasCorreo.cuerpoNuevoUsuarioRegistradoAdmin(nuevo);
        nuevoUsuarioEliminarModificar(cuerpo, asunto);
    }

    // Usuario eliminado
    public void notificarUsuarioEliminado(String emailEliminado) {
        Usuario actor = SesionSingleton.getInstance().getUsuarioLogueado();

        String asunto = PlantillasCorreo.Asunto.USUARIO_ELIMINADO;
        String cuerpo = PlantillasCorreo.cuerpoUsuarioEliminado(actor, emailEliminado);

        nuevoUsuarioEliminarModificar(cuerpo, asunto);
    }

    // Cambio de rol
    public void notificarCambioRolUsuario(Usuario afectado, String rolAnterior, String rolNuevo) {
        Usuario actor = SesionSingleton.getInstance().getUsuarioLogueado();

        String asunto = PlantillasCorreo.Asunto.CAMBIO_ROL_USUARIO;
        String cuerpo = PlantillasCorreo.cuerpoCambioRolUsuario(actor, afectado, rolAnterior, rolNuevo);

        nuevoUsuarioEliminarModificar(cuerpo, asunto);
    }


       
}
