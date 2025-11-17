package ui;


import persistencia.SesionSingleton;
import logica.RegistroManager;
import persistencia.Usuario;
import logica.UtilidadesDeArchivos;
import logica.ValidationResult;
import javax.swing.JOptionPane;
import logica.DocumentoDao;
import logica.EnviadorCorreos;


public class Gesto_De_Usuarios extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Gesto_De_Usuarios.class.getName());
    private String emailSeleccionado;  
    public Usuario usuario = SesionSingleton.getInstance().getUsuarioLogueado();
    private EnviadorCorreos enviarCorreo = new EnviadorCorreos();


    /**
     * Creates new form Gestor_De_Documentos
     */
    
     public Gesto_De_Usuarios() {         
        initComponents();
        establecerEstadisticas();
        
        new UtilidadesDeArchivos().crearTablaUsuarios(JTablaUsuarios);
        
        JLabelNombreUsuario.setText(usuario.getNombreCompleto());
        JLabelRolCargo.setText( usuario.getStringRol() + " - " + usuario.getCargo() + " - " + usuario.getDependencia());
        
        JButonEliminar.addActionListener(e -> {
            if (emailSeleccionado != null) {
                eliminarUsuario(emailSeleccionado);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un usuario primero", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButonEditar.addActionListener(e -> {
            if (emailSeleccionado != null) {
                modificarUsuario(emailSeleccionado);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un usuario primero", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        
    }
     
     
        // Metodos extras para simplificar acciones
     
    public void establecerEstadisticas() {
        try {
            int totalDocumentos = new DocumentoDao().obtenerNumeroDocumentos();
            int documentosPorUsuario = new DocumentoDao().obtenerNumeroDocumentosPorUsuario();
            int usuariosTotales = new UtilidadesDeArchivos().contarUsuarios();

            System.out.println("Total de documentos: " + totalDocumentos);
            System.out.println("Documentos por usuario: " + documentosPorUsuario);
            System.out.println("Usuarios totales: " + usuariosTotales);

            TextIntDocumentos.setText(Integer.toString(totalDocumentos));
            TextDocumentosUsuario.setText(Integer.toString(documentosPorUsuario));
            TextContadorUsuarios.setText(Integer.toString(usuariosTotales));
        } catch (Exception e) {
            System.out.println("Error en la carga: " + e.getMessage());
        }
    } 
    
    private void eliminarUsuario(String email) {
        if (email == null || email.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "No se recibió un email válido para eliminar.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar al usuario con email:\n" + email + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion != JOptionPane.YES_OPTION) {
            return; // canceló
        }

        RegistroManager manager = new RegistroManager("usuarios.csv");
        ValidationResult resultado = manager.eliminarUsuario(email);

        if (resultado.isSuccess()) {
            JOptionPane.showMessageDialog(this,
                    "Usuario eliminado con éxito",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            
            
        // ====== CORREO DE NOTIFICACIÓN ======
        new Thread(() -> {
            try {
                String asunto = "SwiftFold: usuario eliminado del sistema";

                String nombreEjecutor =
                        (usuario != null ? usuario.getNombreCompleto() : "Usuario del sistema");
                String correoEjecutor =
                        (usuario != null ? usuario.getEmail() : "N/D");

                String mensaje =
                        "Estimado usuario,\n\n" +
                        "Se ha eliminado un usuario del sistema de gestión documental SwiftFold.\n\n" +
                        "Detalles de la acción:\n" +
                        "• Usuario eliminado: " + email + "\n" +
                        "• Acción realizada por: " + nombreEjecutor + " (" + correoEjecutor + ")\n\n" +
                        "Si esta acción no fue autorizada, revise la administración de usuarios en SwiftFold.\n\n" +
                        "Atentamente,\n" +
                        "SwiftFold – Sistema de Gestión Documental";

                // Ajusta este llamado al nombre real de tu clase/método de correo
                enviarCorreo.nuevoUsuarioEliminarModificar(mensaje, asunto);

            } catch (Exception exCorreo) {
                System.err.println("No se pudo enviar el correo de notificación: " + exCorreo.getMessage());
            }
        }).start();
        // ================================
            

            // Recargar la tabla de usuarios
            new UtilidadesDeArchivos().crearTablaUsuarios(JTablaUsuarios);
            emailSeleccionado = null; // limpiar selección

        } else {
            // Puedes mostrar los mensajes de error que trae ValidationResult
            StringBuilder sb = new StringBuilder();
            for (String msg : resultado.getMessages()) {
                sb.append(msg).append("\n");
            }
            JOptionPane.showMessageDialog(this,
                    sb.toString(),
                    "Error al eliminar usuario",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void modificarUsuario(String email) {
        this.dispose();
        Ajustes_Usuario_Fuera ventanaModificacion = new Ajustes_Usuario_Fuera(email);
        ventanaModificacion.setLocationRelativeTo(null);
        ventanaModificacion.setVisible(true);
    }


    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Backgraund = new javax.swing.JPanel();
        BarraSuperior = new javax.swing.JPanel();
        TextSwiftFold = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        Icono = new javax.swing.JLabel();
        JLabelNombreUsuario = new javax.swing.JLabel();
        JLabelRolCargo = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        BarraVertical01 = new javax.swing.JPanel();
        BarraVertical02 = new javax.swing.JPanel();
        TituloGrande = new javax.swing.JLabel();
        DescripcionMain = new javax.swing.JLabel();
        PanelEstadisticas = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        TextIntDocumentos = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        TextDocumentosUsuario = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        TextContadorUsuarios = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        LineaHorizontal01 = new javax.swing.JPanel();
        LineaHorizontal02 = new javax.swing.JPanel();
        JPanelAjustes = new javax.swing.JPanel();
        TextAjustes = new javax.swing.JLabel();
        ImgAjustes = new javax.swing.JLabel();
        JPanelMisDocumentos = new javax.swing.JPanel();
        TextDocumentos = new javax.swing.JLabel();
        ImgMisDocumentos = new javax.swing.JLabel();
        JPanelCargarDocumento = new javax.swing.JPanel();
        TextCargar = new javax.swing.JLabel();
        ImgCargar = new javax.swing.JLabel();
        JPanelUsuarios = new javax.swing.JPanel();
        TextUsuarios = new javax.swing.JLabel();
        ImgUsuarios = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTablaUsuarios = new javax.swing.JTable();
        JPanelBarraDeAcciones = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        JButonBuscar = new javax.swing.JButton();
        JButonEditar = new javax.swing.JButton();
        JButonEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        Backgraund.setBackground(new java.awt.Color(255, 251, 248));
        Backgraund.setPreferredSize(new java.awt.Dimension(1400, 700));
        Backgraund.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BarraSuperior.setBackground(new java.awt.Color(53, 91, 62));

        TextSwiftFold.setBackground(new java.awt.Color(255, 255, 255));
        TextSwiftFold.setFont(new java.awt.Font("Inter", 1, 20)); // NOI18N
        TextSwiftFold.setForeground(new java.awt.Color(255, 255, 255));
        TextSwiftFold.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TextSwiftFold.setText("SwiftFold");

        jPanel1.setBackground(new java.awt.Color(180, 176, 194));
        jPanel1.setPreferredSize(new java.awt.Dimension(1, 40));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        Icono.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Icono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Anonimo.png"))); // NOI18N
        Icono.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IconoMouseClicked(evt);
            }
        });

        JLabelNombreUsuario.setFont(new java.awt.Font("Inter", 1, 13)); // NOI18N
        JLabelNombreUsuario.setForeground(new java.awt.Color(255, 255, 255));
        JLabelNombreUsuario.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        JLabelNombreUsuario.setText("[Usuarios del actual con el cual trabaja]");

        JLabelRolCargo.setFont(new java.awt.Font("Inter", 1, 13)); // NOI18N
        JLabelRolCargo.setForeground(new java.awt.Color(255, 255, 255));
        JLabelRolCargo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        JLabelRolCargo.setText("[Rol - Cargo del usuario]");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Logo_pequeño.png"))); // NOI18N

        javax.swing.GroupLayout BarraSuperiorLayout = new javax.swing.GroupLayout(BarraSuperior);
        BarraSuperior.setLayout(BarraSuperiorLayout);
        BarraSuperiorLayout.setHorizontalGroup(
            BarraSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BarraSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextSwiftFold, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 540, Short.MAX_VALUE)
                .addGroup(BarraSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JLabelRolCargo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JLabelNombreUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Icono, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99))
        );
        BarraSuperiorLayout.setVerticalGroup(
            BarraSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BarraSuperiorLayout.createSequentialGroup()
                .addGroup(BarraSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(BarraSuperiorLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(JLabelNombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))
                    .addComponent(TextSwiftFold, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                    .addComponent(Icono, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(BarraSuperiorLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(BarraSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JLabelRolCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        Backgraund.add(BarraSuperior, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1400, 60));

        BarraVertical01.setBackground(new java.awt.Color(219, 219, 219));
        BarraVertical01.setPreferredSize(new java.awt.Dimension(3, 610));

        javax.swing.GroupLayout BarraVertical01Layout = new javax.swing.GroupLayout(BarraVertical01);
        BarraVertical01.setLayout(BarraVertical01Layout);
        BarraVertical01Layout.setHorizontalGroup(
            BarraVertical01Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        BarraVertical01Layout.setVerticalGroup(
            BarraVertical01Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 610, Short.MAX_VALUE)
        );

        Backgraund.add(BarraVertical01, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 2, 610));

        BarraVertical02.setBackground(new java.awt.Color(219, 219, 219));
        BarraVertical02.setPreferredSize(new java.awt.Dimension(2, 610));

        javax.swing.GroupLayout BarraVertical02Layout = new javax.swing.GroupLayout(BarraVertical02);
        BarraVertical02.setLayout(BarraVertical02Layout);
        BarraVertical02Layout.setHorizontalGroup(
            BarraVertical02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        BarraVertical02Layout.setVerticalGroup(
            BarraVertical02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 610, Short.MAX_VALUE)
        );

        Backgraund.add(BarraVertical02, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 80, -1, -1));

        TituloGrande.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        TituloGrande.setForeground(new java.awt.Color(13, 6, 45));
        TituloGrande.setText("Gestor de Usuarios");
        Backgraund.add(TituloGrande, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, 440, 30));

        DescripcionMain.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        DescripcionMain.setText("Administre los documentos de la Secretaria General");
        Backgraund.add(DescripcionMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 120, 490, -1));

        PanelEstadisticas.setBackground(new java.awt.Color(245, 245, 245));

        jLabel2.setBackground(new java.awt.Color(13, 6, 45));
        jLabel2.setFont(new java.awt.Font("Inter", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(13, 6, 45));
        jLabel2.setText("Estadisticas del Sistema");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel6.setBackground(new java.awt.Color(204, 255, 204));

        TextIntDocumentos.setBackground(new java.awt.Color(73, 213, 89));
        TextIntDocumentos.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        TextIntDocumentos.setForeground(new java.awt.Color(51, 102, 0));
        TextIntDocumentos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TextIntDocumentos.setText("48");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TextIntDocumentos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TextIntDocumentos, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
        );

        jLabel7.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("MIS");

        jLabel8.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("DOCUMENTOS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(204, 255, 204));

        TextDocumentosUsuario.setBackground(new java.awt.Color(73, 213, 89));
        TextDocumentosUsuario.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        TextDocumentosUsuario.setForeground(new java.awt.Color(51, 102, 0));
        TextDocumentosUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TextDocumentosUsuario.setText("48");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TextDocumentosUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TextDocumentosUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
        );

        jLabel10.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("TOTALES");

        jLabel9.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("DOCUMENTOS");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(204, 255, 204));

        TextContadorUsuarios.setBackground(new java.awt.Color(73, 213, 89));
        TextContadorUsuarios.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        TextContadorUsuarios.setForeground(new java.awt.Color(51, 102, 0));
        TextContadorUsuarios.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TextContadorUsuarios.setText("48");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TextContadorUsuarios, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TextContadorUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
        );

        jLabel11.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("USUARIOS");

        jLabel12.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("TOTALES");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(122, 197, 85));
        jPanel5.setPreferredSize(new java.awt.Dimension(201, 4));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 201, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout PanelEstadisticasLayout = new javax.swing.GroupLayout(PanelEstadisticas);
        PanelEstadisticas.setLayout(PanelEstadisticasLayout);
        PanelEstadisticasLayout.setHorizontalGroup(
            PanelEstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelEstadisticasLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(PanelEstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        PanelEstadisticasLayout.setVerticalGroup(
            PanelEstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelEstadisticasLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        Backgraund.add(PanelEstadisticas, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 80, 240, 610));

        LineaHorizontal01.setBackground(new java.awt.Color(219, 219, 219));
        LineaHorizontal01.setPreferredSize(new java.awt.Dimension(740, 2));

        javax.swing.GroupLayout LineaHorizontal01Layout = new javax.swing.GroupLayout(LineaHorizontal01);
        LineaHorizontal01.setLayout(LineaHorizontal01Layout);
        LineaHorizontal01Layout.setHorizontalGroup(
            LineaHorizontal01Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 740, Short.MAX_VALUE)
        );
        LineaHorizontal01Layout.setVerticalGroup(
            LineaHorizontal01Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        Backgraund.add(LineaHorizontal01, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 150, 740, 4));

        LineaHorizontal02.setBackground(new java.awt.Color(219, 219, 219));
        LineaHorizontal02.setPreferredSize(new java.awt.Dimension(220, 2));

        javax.swing.GroupLayout LineaHorizontal02Layout = new javax.swing.GroupLayout(LineaHorizontal02);
        LineaHorizontal02.setLayout(LineaHorizontal02Layout);
        LineaHorizontal02Layout.setHorizontalGroup(
            LineaHorizontal02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );
        LineaHorizontal02Layout.setVerticalGroup(
            LineaHorizontal02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        Backgraund.add(LineaHorizontal02, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 220, 2));

        JPanelAjustes.setBackground(new java.awt.Color(255, 251, 248));
        JPanelAjustes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JPanelAjustesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                JPanelAjustesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                JPanelAjustesMouseExited(evt);
            }
        });
        JPanelAjustes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TextAjustes.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        TextAjustes.setForeground(new java.awt.Color(120, 116, 134));
        TextAjustes.setText("Ajustes");
        JPanelAjustes.add(TextAjustes, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, -2, 90, 40));

        ImgAjustes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ImgAjustes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Ajustes.png"))); // NOI18N
        JPanelAjustes.add(ImgAjustes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 40, 40));

        Backgraund.add(JPanelAjustes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 200, 40));

        JPanelMisDocumentos.setBackground(new java.awt.Color(255, 251, 248));
        JPanelMisDocumentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JPanelMisDocumentosMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                JPanelMisDocumentosMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                JPanelMisDocumentosMouseExited(evt);
            }
        });
        JPanelMisDocumentos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TextDocumentos.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        TextDocumentos.setForeground(new java.awt.Color(120, 116, 134));
        TextDocumentos.setText("Ver Documentos");
        JPanelMisDocumentos.add(TextDocumentos, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 130, 40));

        ImgMisDocumentos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ImgMisDocumentos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Mis_documentos.png"))); // NOI18N
        JPanelMisDocumentos.add(ImgMisDocumentos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 40, 40));

        Backgraund.add(JPanelMisDocumentos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 200, 40));

        JPanelCargarDocumento.setBackground(new java.awt.Color(255, 251, 248));
        JPanelCargarDocumento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JPanelCargarDocumentoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                JPanelCargarDocumentoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                JPanelCargarDocumentoMouseExited(evt);
            }
        });
        JPanelCargarDocumento.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TextCargar.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        TextCargar.setForeground(new java.awt.Color(120, 116, 134));
        TextCargar.setText("Cargar Documento");
        JPanelCargarDocumento.add(TextCargar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 140, 40));

        ImgCargar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ImgCargar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Subir_documento.png"))); // NOI18N
        JPanelCargarDocumento.add(ImgCargar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 40, 40));

        Backgraund.add(JPanelCargarDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 200, 40));

        JPanelUsuarios.setBackground(new java.awt.Color(255, 251, 248));
        JPanelUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JPanelUsuariosMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                JPanelUsuariosMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                JPanelUsuariosMouseExited(evt);
            }
        });
        JPanelUsuarios.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TextUsuarios.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        TextUsuarios.setForeground(new java.awt.Color(120, 116, 134));
        TextUsuarios.setText("Usuarios");
        JPanelUsuarios.add(TextUsuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 100, 40));

        ImgUsuarios.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ImgUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Usuarios.png"))); // NOI18N
        JPanelUsuarios.add(ImgUsuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 40, 40));

        Backgraund.add(JPanelUsuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 200, 40));

        JTablaUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTablaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTablaUsuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(JTablaUsuarios);

        Backgraund.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 250, 740, 440));

        jTextField1.setText("jTextField1");

        JButonBuscar.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        JButonBuscar.setText("Buscar");

        JButonEditar.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        JButonEditar.setText("Editar");

        JButonEliminar.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        JButonEliminar.setText("Eliminar");

        javax.swing.GroupLayout JPanelBarraDeAccionesLayout = new javax.swing.GroupLayout(JPanelBarraDeAcciones);
        JPanelBarraDeAcciones.setLayout(JPanelBarraDeAccionesLayout);
        JPanelBarraDeAccionesLayout.setHorizontalGroup(
            JPanelBarraDeAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelBarraDeAccionesLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(JButonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 167, Short.MAX_VALUE)
                .addComponent(JButonEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        JPanelBarraDeAccionesLayout.setVerticalGroup(
            JPanelBarraDeAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelBarraDeAccionesLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(JPanelBarraDeAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JButonBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(jTextField1)
                    .addComponent(JButonEditar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JButonEliminar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9))
        );

        Backgraund.add(JPanelBarraDeAcciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 180, 740, 60));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Backgraund, javax.swing.GroupLayout.PREFERRED_SIZE, 1310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Backgraund, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JPanelCargarDocumentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JPanelCargarDocumentoMouseClicked
        if (usuario.getRol().tieneAccesoSubir()){

            this.dispose();

            // Abrir la ventana de gestor de documentos
            Cargar_Documento ventanaCargarDocumento= new Cargar_Documento();
            ventanaCargarDocumento.setLocationRelativeTo(null);
            ventanaCargarDocumento.setVisible(true);

         } else {
            JOptionPane.showMessageDialog(this, "No tienes acceso para eliminar este documento.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_JPanelCargarDocumentoMouseClicked

    private void JPanelUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JPanelUsuariosMouseClicked
        // Cerrar login
        this.dispose();

        // Abrir la ventana de gestor de documentos
        Ajustes_Usuario ventanaGestorDeUsuario = new Ajustes_Usuario();
        ventanaGestorDeUsuario.setLocationRelativeTo(null);
        ventanaGestorDeUsuario.setVisible(true);
    }//GEN-LAST:event_JPanelUsuariosMouseClicked

    private void JPanelMisDocumentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JPanelMisDocumentosMouseClicked
        // Cerrar login
        this.dispose();

        // Abrir la ventana de gestor de documentos
        Gestor_De_Documentos ventanaGestorDeDocumentos;
        ventanaGestorDeDocumentos = new Gestor_De_Documentos("", 0L);
        ventanaGestorDeDocumentos.setLocationRelativeTo(null);
        ventanaGestorDeDocumentos.setVisible(true);
    }//GEN-LAST:event_JPanelMisDocumentosMouseClicked

    private void JPanelCargarDocumentoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JPanelCargarDocumentoMouseEntered
       JPanelCargarDocumento.setBackground(new java.awt.Color(211, 211, 211)); 
    }//GEN-LAST:event_JPanelCargarDocumentoMouseEntered

    private void JPanelCargarDocumentoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JPanelCargarDocumentoMouseExited
       JPanelCargarDocumento.setBackground(new java.awt.Color(255, 251, 248));
    }//GEN-LAST:event_JPanelCargarDocumentoMouseExited

    private void JPanelMisDocumentosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JPanelMisDocumentosMouseEntered
       JPanelMisDocumentos.setBackground(new java.awt.Color(211, 211, 211)); 
    }//GEN-LAST:event_JPanelMisDocumentosMouseEntered

    private void JPanelMisDocumentosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JPanelMisDocumentosMouseExited
       JPanelMisDocumentos.setBackground(new java.awt.Color(255, 251, 248));
    }//GEN-LAST:event_JPanelMisDocumentosMouseExited

    private void JPanelUsuariosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JPanelUsuariosMouseEntered
       JPanelUsuarios.setBackground(new java.awt.Color(211, 211, 211)); 
    }//GEN-LAST:event_JPanelUsuariosMouseEntered

    private void JPanelUsuariosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JPanelUsuariosMouseExited
       JPanelUsuarios.setBackground(new java.awt.Color(255, 251, 248));
    }//GEN-LAST:event_JPanelUsuariosMouseExited

    private void JPanelAjustesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JPanelAjustesMouseEntered
       JPanelAjustes.setBackground(new java.awt.Color(211, 211, 211)); 
    }//GEN-LAST:event_JPanelAjustesMouseEntered

    private void JPanelAjustesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JPanelAjustesMouseExited
       JPanelAjustes.setBackground(new java.awt.Color(255, 251, 248));
    }//GEN-LAST:event_JPanelAjustesMouseExited

    private void IconoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IconoMouseClicked
        this.dispose();
        Login ventanaLogin = new Login();
        ventanaLogin.setLocationRelativeTo(null);
        ventanaLogin.setVisible(true);
    }//GEN-LAST:event_IconoMouseClicked

    private void JPanelAjustesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JPanelAjustesMouseClicked
        this.dispose();
        Ajustes_Usuario ajustesUSuarioPropio = new Ajustes_Usuario();
        ajustesUSuarioPropio.setLocationRelativeTo(null);
        ajustesUSuarioPropio.setVisible(true);
    }//GEN-LAST:event_JPanelAjustesMouseClicked

    private void JTablaUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTablaUsuariosMouseClicked
        int row = JTablaUsuarios.getSelectedRow(); 
        if (row != -1) {
            emailSeleccionado = String.valueOf(JTablaUsuarios.getValueAt(row, 1));
            System.out.println("Email seleccionado: " + emailSeleccionado);
        } else {
            emailSeleccionado = null;  // por si acaso
        }
    }//GEN-LAST:event_JTablaUsuariosMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Gesto_De_Usuarios().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Backgraund;
    private javax.swing.JPanel BarraSuperior;
    private javax.swing.JPanel BarraVertical01;
    private javax.swing.JPanel BarraVertical02;
    private javax.swing.JLabel DescripcionMain;
    private javax.swing.JLabel Icono;
    private javax.swing.JLabel ImgAjustes;
    private javax.swing.JLabel ImgCargar;
    private javax.swing.JLabel ImgMisDocumentos;
    private javax.swing.JLabel ImgUsuarios;
    private javax.swing.JButton JButonBuscar;
    private javax.swing.JButton JButonEditar;
    private javax.swing.JButton JButonEliminar;
    private javax.swing.JLabel JLabelNombreUsuario;
    private javax.swing.JLabel JLabelRolCargo;
    private javax.swing.JPanel JPanelAjustes;
    private javax.swing.JPanel JPanelBarraDeAcciones;
    private javax.swing.JPanel JPanelCargarDocumento;
    private javax.swing.JPanel JPanelMisDocumentos;
    private javax.swing.JPanel JPanelUsuarios;
    private javax.swing.JTable JTablaUsuarios;
    private javax.swing.JPanel LineaHorizontal01;
    private javax.swing.JPanel LineaHorizontal02;
    private javax.swing.JPanel PanelEstadisticas;
    private javax.swing.JLabel TextAjustes;
    private javax.swing.JLabel TextCargar;
    private javax.swing.JLabel TextContadorUsuarios;
    private javax.swing.JLabel TextDocumentos;
    private javax.swing.JLabel TextDocumentosUsuario;
    private javax.swing.JLabel TextIntDocumentos;
    private javax.swing.JLabel TextSwiftFold;
    private javax.swing.JLabel TextUsuarios;
    private javax.swing.JLabel TituloGrande;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
