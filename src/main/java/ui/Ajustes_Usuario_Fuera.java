package ui;

import logica.RegistroManager;
import persistencia.Usuario;
import logica.UtilidadesDeArchivos;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import logica.EnviadorCorreos;
import persistencia.RolSuperAdministrador;
import persistencia.SesionSingleton;


public class Ajustes_Usuario_Fuera extends javax.swing.JFrame {
    
    private Map<String, String[]> departamentosMunicipios;  
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Ajustes_Usuario_Fuera.class.getName());
    Usuario usuario = new Usuario();
    private EnviadorCorreos enviarCorreo = new EnviadorCorreos();
    Usuario usuarioLoUsuario = SesionSingleton.getInstance().getUsuarioLogueado();

    
    
    public Ajustes_Usuario_Fuera(String emailUsuario) {
        initComponents();
        
        configurarComboBoxes();
        initDepartamentosMunicipios();
        setupDepartamentoListener();
        
        usuario = UtilidadesDeArchivos.buscarUsuarioPorEmail(emailUsuario);
        
        TextoCambiable.setText(usuario.getNombre() + " " + usuario.getApellido());
        
        if (usuario == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el usuario con email: " + emailUsuario,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            
            // Volver al gestor y cerrar esta ventana
            this.dispose();
            Gesto_De_Usuarios ventanaGestor = new Gesto_De_Usuarios("");
            ventanaGestor.setLocationRelativeTo(null);
            ventanaGestor.setVisible(true);
            return;
        }
        
        TextNombre.setText(usuario.getNombre());
        TextApellido.setText(usuario.getApellido());
        TextEmail.setText(usuario.getEmail());
        TextNumero.setText(usuario.getTelefono());
        TextCargo.setText(usuario.getCargo());
        TextoCambiable.setText(usuario.getNombre() + " " + usuario.getApellido());
        TextContrase.setText(usuario.getHashContrasena());

        SelectDepart.setSelectedItem(usuario.getDepartamento());
        SelectMunicipio.setSelectedItem(usuario.getMunicipio());
        SelectDependencia.setSelectedItem(usuario.getDependencia());
        SelectRol.setSelectedItem(usuario.getRol().getClass().getSimpleName());
    }
    
    
    
    private void configurarComboBoxes() {
        // Configurar el JComboBox de departamentos
        SelectDepart.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { 
            "Cundinamarca", "Santander", "Antioquia", "ValledelCauca", 
            "Atlántico", "Bolívar", "Meta", "Boyacá"
        }));

        // Configurar el JComboBox de municipios inicialmente vacío
        SelectMunicipio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));

        // Deshabilitar SelectMunicipio inicialmente
        SelectMunicipio.setEnabled(false);

        // Configurar el JComboBox de dependencias
        SelectDependencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { 
            "Secretaría General", "Secretaría de Gobierno", "Secretaría de Hacienda", 
            "Secretaría de Planeación", "Secretaría de Salud", "Secretaría de Educación", 
            "Secretaría de Infraestructura", "Secretaría de Desarrollo Social", 
            "Secretaría de Desarrollo Económico", "Secretaría de Agricultura", "Secretaría de Ambiente", 
            "Secretaría de Tránsito y Transporte", "Secretaría de Cultura y Turismo", 
            "Secretaría de Deporte y Recreación", "Oficina Jurídica", "Oficina de Control Interno", 
            "Oficina TIC / Sistemas", "Gestión Documental / Archivo", "Oficina de Prensa y Comunicaciones", 
            "Gestión del Riesgo", "Inspección de Policía", "Comisaría de Familia", "Personería"
        }));


        if (usuarioLoUsuario != null && usuarioLoUsuario.getRol() instanceof RolSuperAdministrador) {

            // Si es superadministrador → Solo puede asignar estos roles
            SelectRol.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "RolAdministrador", "RolSuperAdministrador" }
            ));

        } else {

            // Si NO es superadmin → mostrar todos los roles
            SelectRol.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] {
                    "RolCiudadano",
                    "RolFuncionario",
                    "RolSecretario",
                    "RolAdministrador",
                    "RolSuperAdministrador"
                }
            ));
        }
    }

    
    private void initDepartamentosMunicipios() {
        departamentosMunicipios = new HashMap<>();

        departamentosMunicipios.put("Cundinamarca", new String[]{
            "Bogotá", "Soacha", "Chía", "Cajicá", "La Calera"
        });

        departamentosMunicipios.put("Antioquia", new String[]{
            "Medellín", "Bello", "Itagüí", "Envigado", "Girardota"
        });

        departamentosMunicipios.put("Santander", new String[]{
            "Bucaramanga", "Floridablanca", "Girón", "Piedecuesta", "San Alberto"
        });

        // Otros departamentos con municipios
        departamentosMunicipios.put("ValledelCauca", new String[]{
            "Cali", "Palmira", "Yumbo", "Jamundí", "Buga"
        });
    }

    
    
    private void setupDepartamentoListener() {
        SelectDepart.addActionListener(evt -> updateMunicipios());
    }

    private void updateMunicipios() {
        String departamentoSeleccionado = (String) SelectDepart.getSelectedItem();
        String[] municipios;

        if (departamentosMunicipios.containsKey(departamentoSeleccionado)) {
            municipios = departamentosMunicipios.get(departamentoSeleccionado);
        } else {
            municipios = new String[]{};
        }

        SelectMunicipio.setModel(new javax.swing.DefaultComboBoxModel<>(municipios));
        SelectMunicipio.setEnabled(true);
    }

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        TextVisualizacion = new javax.swing.JLabel();
        TextoCambiable = new javax.swing.JLabel();
        TextCargo = new javax.swing.JTextField();
        SelectMunicipio = new javax.swing.JComboBox<>();
        SelectDependencia = new javax.swing.JComboBox<>();
        SelectDepart = new javax.swing.JComboBox<>();
        Apellidos = new javax.swing.JLabel();
        Municipio = new javax.swing.JLabel();
        Nombre = new javax.swing.JLabel();
        Email = new javax.swing.JLabel();
        Numero = new javax.swing.JLabel();
        Cargo = new javax.swing.JLabel();
        Departamento = new javax.swing.JLabel();
        Dependencia = new javax.swing.JLabel();
        Contraseña = new javax.swing.JLabel();
        JBotonSalir = new javax.swing.JButton();
        JBotonGuardar = new javax.swing.JButton();
        BarraHorizontal = new javax.swing.JPanel();
        SelectRol = new javax.swing.JComboBox<>();
        Municipio1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        TextContrase = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        TextNombre = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        TextEmail = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        TextApellido = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        TextNumero = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 251, 248));
        jPanel1.setForeground(new java.awt.Color(255, 251, 248));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TextVisualizacion.setFont(new java.awt.Font("Inter", 1, 30)); // NOI18N
        TextVisualizacion.setText("Ajustes de Usuario");
        jPanel1.add(TextVisualizacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        TextoCambiable.setFont(new java.awt.Font("Inter", 1, 30)); // NOI18N
        TextoCambiable.setForeground(new java.awt.Color(53, 91, 62));
        TextoCambiable.setText("dasdsadas");
        jPanel1.add(TextoCambiable, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, -1));

        TextCargo.setFont(new java.awt.Font("Roboto SemiCondensed", 0, 14)); // NOI18N
        TextCargo.setForeground(new java.awt.Color(102, 102, 102));
        TextCargo.setText("Ingrese el cargo");
        TextCargo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TextCargoMousePressed(evt);
            }
        });
        TextCargo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextCargoActionPerformed(evt);
            }
        });
        jPanel1.add(TextCargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 490, 270, 40));

        SelectMunicipio.setFont(new java.awt.Font("Roboto SemiCondensed", 0, 14)); // NOI18N
        SelectMunicipio.setForeground(new java.awt.Color(102, 102, 102));
        SelectMunicipio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectMunicipioActionPerformed(evt);
            }
        });
        jPanel1.add(SelectMunicipio, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 330, 270, 40));

        SelectDependencia.setFont(new java.awt.Font("Roboto Condensed", 0, 14)); // NOI18N
        SelectDependencia.setForeground(new java.awt.Color(102, 102, 102));
        SelectDependencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Secretaría General", "Secretaría de Gobierno", "Secretaría de Hacienda", "Secretaría de Planeación", "Secretaría de Salud", "Secretaría de Educación", "Secretaría de Infraestructura", "Secretaría de Desarrollo Social", "Secretaría de Desarrollo Económico", "Secretaría de Agricultura", "Secretaría de Ambiente", "Secretaría de Tránsito y Transporte", "Secretaría de Cultura y Turismo", "Secretaría de Deporte y Recreación", "Oficina Jurídica", "Oficina de Control Interno", "Oficina TIC / Sistemas", "Gestión Documental / Archivo", "Oficina de Prensa y Comunicaciones", "Gestión del Riesgo", "Inspección de Policía", "Comisaría de Familia", "Personería (si aplica)" }));
        SelectDependencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectDependenciaActionPerformed(evt);
            }
        });
        jPanel1.add(SelectDependencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, 270, 40));

        SelectDepart.setFont(new java.awt.Font("Roboto SemiCondensed", 0, 14)); // NOI18N
        SelectDepart.setForeground(new java.awt.Color(102, 102, 102));
        SelectDepart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectDepartActionPerformed(evt);
            }
        });
        jPanel1.add(SelectDepart, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 270, 40));

        Apellidos.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        Apellidos.setText("Apellidos");
        jPanel1.add(Apellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 140, -1, 20));

        Municipio.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        Municipio.setText("Rol");
        jPanel1.add(Municipio, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 380, -1, 30));

        Nombre.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        Nombre.setText("Nombre");
        jPanel1.add(Nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, 20));

        Email.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        Email.setText("Email");
        jPanel1.add(Email, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, -1, 20));

        Numero.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        Numero.setText("Numero");
        jPanel1.add(Numero, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 220, -1, 20));

        Cargo.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        Cargo.setText("Cargo");
        jPanel1.add(Cargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 460, -1, 30));

        Departamento.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        Departamento.setText("Departamento");
        jPanel1.add(Departamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, 30));

        Dependencia.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        Dependencia.setText("Dependencia Perteneciente");
        jPanel1.add(Dependencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, -1, 30));

        Contraseña.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        Contraseña.setText("Contraseña");
        jPanel1.add(Contraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 460, -1, 30));

        JBotonSalir.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        JBotonSalir.setText("Salir");
        JBotonSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JBotonSalirMouseClicked(evt);
            }
        });
        jPanel1.add(JBotonSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 560, 110, 50));

        JBotonGuardar.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        JBotonGuardar.setText("Guardar");
        JBotonGuardar.setActionCommand("GUARDAR");
        JBotonGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JBotonGuardarMouseClicked(evt);
            }
        });
        jPanel1.add(JBotonGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 560, 110, 50));

        BarraHorizontal.setBackground(new java.awt.Color(219, 219, 219));

        javax.swing.GroupLayout BarraHorizontalLayout = new javax.swing.GroupLayout(BarraHorizontal);
        BarraHorizontal.setLayout(BarraHorizontalLayout);
        BarraHorizontalLayout.setHorizontalGroup(
            BarraHorizontalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        BarraHorizontalLayout.setVerticalGroup(
            BarraHorizontalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.add(BarraHorizontal, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 560, 4));

        jPanel1.add(SelectRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 410, 270, 40));

        Municipio1.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        Municipio1.setText("Municipio");
        jPanel1.add(Municipio1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 300, -1, 30));

        TextContrase.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        TextContrase.setText("Contraseña del usuario");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(TextContrase, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TextContrase, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, 267, 36));

        TextNombre.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        TextNombre.setText("Nombre del usuario");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(TextNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TextNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 267, 36));

        TextEmail.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        TextEmail.setText("Email de usuario");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(TextEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(101, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TextEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 267, 36));

        TextApellido.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        TextApellido.setText("Apellidos de usuario");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(TextApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TextApellido, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 170, 267, 36));

        TextNumero.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        TextNumero.setText("Numero del usuario");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(TextNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TextNumero, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 250, 267, 36));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 636, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 634, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TextCargoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TextCargoMousePressed


    }//GEN-LAST:event_TextCargoMousePressed

    private void TextCargoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextCargoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextCargoActionPerformed

    private void SelectMunicipioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectMunicipioActionPerformed

    }//GEN-LAST:event_SelectMunicipioActionPerformed

    private void SelectDependenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectDependenciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SelectDependenciaActionPerformed

    private void SelectDepartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectDepartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SelectDepartActionPerformed

    
    private void JBotonSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JBotonSalirMouseClicked
        this.dispose();

        Gesto_De_Usuarios ventanaGestorDeUsuario = new Gesto_De_Usuarios("");
        ventanaGestorDeUsuario.setLocationRelativeTo(null);
        ventanaGestorDeUsuario.setVisible(true);
    }//GEN-LAST:event_JBotonSalirMouseClicked

    
    private void JBotonGuardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JBotonGuardarMouseClicked
        // Obtener los datos de los campos del formulario
        String email = TextEmail.getText().trim();
        String nuevoDepartamento = SelectDepart.getSelectedItem().toString();
        String nuevoMunicipio = SelectMunicipio.getSelectedItem().toString();
        String nuevaDependencia = SelectDependencia.getSelectedItem().toString();
        String nuevoCargo = TextCargo.getText().trim();
        String nuevoRol = SelectRol.getSelectedItem().toString();

        // Validación básica
        if (email.isEmpty() || nuevoDepartamento.isEmpty() || nuevoMunicipio.isEmpty()
                || nuevaDependencia.isEmpty() || nuevoCargo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, complete todos los campos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Guardamos el rol anterior ANTES de modificar
        String rolAnterior = (usuario.getRol() != null)
                ? usuario.getRol().getClass().getSimpleName()
                : "N/D";

        RegistroManager manager = new RegistroManager("usuarios.csv");

        logica.ValidationResult resultado = manager.modificarUsuario(
                email,
                nuevoDepartamento,
                nuevoMunicipio,
                nuevaDependencia,
                nuevoRol,   
                nuevoCargo  
        );

        if (resultado.isSuccess()) {
            JOptionPane.showMessageDialog(this,
                    "Usuario modificado correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            
        // ====== CORREO DE NOTIFICACIÓN DE CAMBIO DE ROL ======
        // Solo si el rol realmente cambió
        new Thread(() -> {
            try {
                if (!rolAnterior.equals(nuevoRol)) {
                    enviarCorreo.notificarCambioRolUsuario(usuario, rolAnterior, nuevoRol);
                }
            } catch (Exception exCorreo) {
                System.err.println("No se pudo enviar la notificación de cambio de rol: "
                        + exCorreo.getMessage());
            }
        }).start();
        // =====================================================

            // volver al gestor de usuarios
            this.dispose();
            Gesto_De_Usuarios ventanaGestor = new Gesto_De_Usuarios("");
            ventanaGestor.setLocationRelativeTo(null);
            ventanaGestor.setVisible(true);

        } else {
            // Mostrar los errores devueltos por ValidationResult
            StringBuilder sb = new StringBuilder();
            for (String msg : resultado.getMessages()) {
                sb.append(msg).append("\n");
            }
            JOptionPane.showMessageDialog(this,
                    sb.toString(),
                    "Error al modificar usuario",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_JBotonGuardarMouseClicked

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
        Usuario usuario = null;

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Ajustes_Usuario_Fuera("").setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Apellidos;
    private javax.swing.JPanel BarraHorizontal;
    private javax.swing.JLabel Cargo;
    private javax.swing.JLabel Contraseña;
    private javax.swing.JLabel Departamento;
    private javax.swing.JLabel Dependencia;
    private javax.swing.JLabel Email;
    private javax.swing.JButton JBotonGuardar;
    private javax.swing.JButton JBotonSalir;
    private javax.swing.JLabel Municipio;
    private javax.swing.JLabel Municipio1;
    private javax.swing.JLabel Nombre;
    private javax.swing.JLabel Numero;
    private javax.swing.JComboBox<String> SelectDepart;
    private javax.swing.JComboBox<String> SelectDependencia;
    private javax.swing.JComboBox<String> SelectMunicipio;
    private javax.swing.JComboBox<String> SelectRol;
    private javax.swing.JLabel TextApellido;
    private javax.swing.JTextField TextCargo;
    private javax.swing.JLabel TextContrase;
    private javax.swing.JLabel TextEmail;
    private javax.swing.JLabel TextNombre;
    private javax.swing.JLabel TextNumero;
    private javax.swing.JLabel TextVisualizacion;
    private javax.swing.JLabel TextoCambiable;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    // End of variables declaration//GEN-END:variables
}
