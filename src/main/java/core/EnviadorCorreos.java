package core;

import app.SesionSingleton;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.List;
import java.util.Properties;

public class EnviadorCorreos {

    // --- Datos del remitente ---
    private static final String REMITENTE = "swiftfold.proyecto@gmail.com";
    private static final String CLAVE_APP = "gbpq avtt icju csih"; // clave de aplicación, no tu contraseña normal

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
    public static void enviarCorreo(String destinatario, String asunto, String mensaje) {
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

        } catch (MessagingException e) {
            System.err.println("Error al enviar correo a " + destinatario);
            e.printStackTrace();
        }
    }

    // --- Método sobrecargado para enviar a varios destinatarios ---
    public static void enviarCorreoMultiple(String[] destinatarios, String asunto, String mensaje) {
        for (String correo : destinatarios) {
            enviarCorreo(correo, asunto, mensaje);
        }
    }
    
    public void enviarCorreosAUsuariosConRoles(String mensaje, Documento d) {
        List<Usuario> listaUsuarios = new UtilidadesDeArchivos().cargarUsuarios(); 

        boolean esPublico = d.esPublico();
        String asunto = "Se subió un nuevo archivo";

        for (Usuario usuario : listaUsuarios) {
            if (esPublico) {
                if (usuario.getRol() instanceof RolSecretario || 
                    usuario.getRol() instanceof RolAdministrador || 
                    usuario.getRol() instanceof RolSuperAdministrador) {

                    String destinatario = usuario.getEmail();

                    enviarCorreo(destinatario, asunto, mensaje); // Enviar el correo
                }
            } else {
                // Si el documento NO es público, aplicar restricciones de municipio y rol
                if ((usuario.getRol() instanceof RolSecretario && usuario.getMunicipio().equals(SesionSingleton.getInstance().getUsuarioLogueado().getMunicipio())) || 
                    (usuario.getRol() instanceof RolAdministrador && usuario.getMunicipio().equals(SesionSingleton.getInstance().getUsuarioLogueado().getMunicipio())) || 
                    (usuario.getRol() instanceof RolFuncionario && usuario.getMunicipio().equals(SesionSingleton.getInstance().getUsuarioLogueado().getMunicipio())) ||
                    usuario.getRol() instanceof RolSuperAdministrador) {

                    String destinatario = usuario.getEmail();
                    enviarCorreo(destinatario, asunto, mensaje); // Enviar el correo
                }
            }
        }
    }

    
    public void eliminarModificar (String mensaje, String asunto) {
        List<Usuario> listaUsuarios = new UtilidadesDeArchivos().cargarUsuarios(); 

        for (Usuario usuario : listaUsuarios) {
            if ((usuario.getRol() instanceof RolSecretario && usuario.getMunicipio().equals(SesionSingleton.getInstance().getUsuarioLogueado().getMunicipio())) || 
                (usuario.getRol() instanceof RolAdministrador && usuario.getMunicipio().equals(SesionSingleton.getInstance().getUsuarioLogueado().getMunicipio())) || 
                (usuario.getRol() instanceof RolFuncionario && usuario.getMunicipio().equals(SesionSingleton.getInstance().getUsuarioLogueado().getMunicipio())) ||
                usuario.getRol() instanceof RolSuperAdministrador) {

                String destinatario = usuario.getEmail(); 

                enviarCorreo(destinatario, asunto, mensaje);
            }
        }
    }
    
    public void nuevoUsuarioEliminarModificar (String mensaje, String asunto) {
        List<Usuario> listaUsuarios = new UtilidadesDeArchivos().cargarUsuarios(); 

        for (Usuario usuario : listaUsuarios) {
            if (usuario.getRol() instanceof RolSuperAdministrador) {

                String destinatario = usuario.getEmail(); 

                enviarCorreo(destinatario, asunto, mensaje);
            }
        }
    }
    
    
    
}
