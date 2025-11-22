package ui;

// LIBRERIAS UTILIZADAS


import logica.CategoriaDao;
import java.awt.Color;
import java.nio.file.Path;
import javax.swing.JFileChooser;

import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import logica.EnviadorCorreos;
import persistencia.Documento;
import persistencia.PlantillasCorreo;
import persistencia.SesionSingleton;
import persistencia.Usuario;


// CLASE PRINCIPAL

public class Cargar_Documento extends javax.swing.JFrame {
    
    private Path rutaSeleccionada;
    private logica.Metadatos.Info metaSeleccionada; 
    private EnviadorCorreos enviarCorreo = new EnviadorCorreos();
    public Usuario usuario = SesionSingleton.getInstance().getUsuarioLogueado();


    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Cargar_Documento.class.getName());

    
    // METODO DE DEFAULT POR PARTE DE SWING
    public Cargar_Documento() {
        initComponents();
        cargarCombos();
        
        // ======= ME PERMITE TENER UN TEXTO BASE EN LOS JFIELDS =======
        
        JTextTituloDocumento1.setText("Ingrese el título del documento");
        JTextTituloDocumento1.setForeground(Color.GRAY);

        JTextCodigo.setText("Ingrese el código");
        JTextCodigo.setForeground(Color.GRAY);

        JTextDescripcion.setText("Agrega una pequeña descripción del documento");
        JTextDescripcion.setForeground(Color.GRAY);
        
    }
    
    
    
    // ======= PARA VALIDACIONES DE CAMPO DE TEXTO =======
    
    private boolean validarCampos() {
        // Verificar si los campos no tienen los valores por defecto
        if (JTextTituloDocumento1.getText().equals("Ingrese el título del documento") || JTextTituloDocumento1.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del documento es obligatorio", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (JTextCodigo.getText().equals("Ingrese el código") || JTextCodigo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El código es obligatorio", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (JTextDescripcion.getText().equals("Agrega una pequeña descripción del documento") || JTextDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción es obligatoria", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validar que se haya seleccionado una categoría
        if (JComboBoxCategorias.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validar tipo de acceso
        if (JComboBoxTipoAcceso.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un tipo de acceso", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validar disposición final
        if (JFechaDisposicionFinal.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una disposición final", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validar si el archivo ha sido seleccionado
        if (rutaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un archivo", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    
    // ======= CARGAR COMBOS PARA QUE SE VEAN =======
    
    private void cargarCombos() {
        // Tipo de acceso
        JComboBoxTipoAcceso.removeAllItems();
        JComboBoxTipoAcceso.addItem("Seleccione tipo…");
        JComboBoxTipoAcceso.addItem("PUBLICO");
        JComboBoxTipoAcceso.addItem("INTERNO");
        JComboBoxTipoAcceso.addItem("RESERVADO");

        // Disposición final
        JFechaDisposicionFinal.removeAllItems();
        JFechaDisposicionFinal.addItem("Seleccione disposición…");
        JFechaDisposicionFinal.addItem("CONSERVAR");
        JFechaDisposicionFinal.addItem("TRANSFERIR");
        JFechaDisposicionFinal.addItem("ELIMINAR");

        // Categorías desde BD
        JComboBoxCategorias.removeAllItems();
        JComboBoxCategorias.addItem("Seleccione una categoría…");
        
        // Me lee la bases de datos y aparte los agrega al combobox
        try {
            for (String nombre : new CategoriaDao().listarNombres()) {
                JComboBoxCategorias.addItem(nombre);
            }
        } catch (Exception ex) {
            System.err.println("No se pudieron cargar categorías: " + ex.getMessage());
        }

        // Deja seleccionados los placeholders
        JComboBoxTipoAcceso.setSelectedIndex(0);
        JFechaDisposicionFinal.setSelectedIndex(0);
        JComboBoxCategorias.setSelectedIndex(0);
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
        TituloCargarDocumento = new javax.swing.JLabel();
        DescripcionCargar = new javax.swing.JLabel();
        JFechaDisposicionFinal = new javax.swing.JComboBox<>();
        JComboBoxCategorias = new javax.swing.JComboBox<>();
        JComboBoxTipoAcceso = new javax.swing.JComboBox<>();
        JTextCodigo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTextDescripcion = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        JBotonLimpiar = new javax.swing.JButton();
        JBottomCancelar = new javax.swing.JButton();
        JBotonGuardar = new javax.swing.JButton();
        JTextTituloDocumento1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        JLabelArchivoRuta = new javax.swing.JLabel();
        BarraHorizontal = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 251, 248));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TituloCargarDocumento.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        TituloCargarDocumento.setForeground(new java.awt.Color(13, 6, 45));
        TituloCargarDocumento.setText("Cargar Documento");
        jPanel1.add(TituloCargarDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        DescripcionCargar.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        DescripcionCargar.setForeground(new java.awt.Color(120, 116, 134));
        DescripcionCargar.setText("Guarda y crea un nuevo documento para guardarlo en el archivador");
        jPanel1.add(DescripcionCargar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 490, -1));

        JFechaDisposicionFinal.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel1.add(JFechaDisposicionFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 310, 370, 40));

        JComboBoxCategorias.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel1.add(JComboBoxCategorias, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 170, 370, 40));

        JComboBoxTipoAcceso.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel1.add(JComboBoxTipoAcceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 370, 40));

        JTextCodigo.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        JTextCodigo.setText("jTextField1");
        JTextCodigo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                JTextCodigoMousePressed(evt);
            }
        });
        jPanel1.add(JTextCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 370, 40));

        JTextDescripcion.setColumns(20);
        JTextDescripcion.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        JTextDescripcion.setRows(5);
        JTextDescripcion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                JTextDescripcionMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(JTextDescripcion);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, 780, -1));

        jLabel3.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel3.setText("Categorias:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 150, -1, -1));

        jLabel4.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel4.setText("Tipo de Acceso:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, -1, -1));

        jLabel5.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel5.setText("Nombre de Documento:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, -1));

        jLabel7.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel7.setText("Descripcion:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, -1, -1));

        jLabel8.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel8.setText("Archivo:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 220, -1, -1));

        jLabel10.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel10.setText("Codigo:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, -1, -1));

        jLabel11.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel11.setText("Disposicion Final:");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 290, -1, -1));

        JBotonLimpiar.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        JBotonLimpiar.setText("Limpiar Documento");
        JBotonLimpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JBotonLimpiarMouseClicked(evt);
            }
        });
        JBotonLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBotonLimpiarActionPerformed(evt);
            }
        });
        jPanel1.add(JBotonLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 500, -1, 40));

        JBottomCancelar.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        JBottomCancelar.setText("Cancelar");
        JBottomCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JBottomCancelarMouseClicked(evt);
            }
        });
        JBottomCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBottomCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(JBottomCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, -1, 40));

        JBotonGuardar.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        JBotonGuardar.setText("Guardar Documento");
        JBotonGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JBotonGuardarMouseClicked(evt);
            }
        });
        JBotonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBotonGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(JBotonGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 500, -1, 40));

        JTextTituloDocumento1.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        JTextTituloDocumento1.setText("jTextField1");
        JTextTituloDocumento1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                JTextTituloDocumento1MousePressed(evt);
            }
        });
        jPanel1.add(JTextTituloDocumento1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 370, 40));

        JLabelArchivoRuta.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        JLabelArchivoRuta.setText("Ingrese el archivo");
        JLabelArchivoRuta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLabelArchivoRutaMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JLabelArchivoRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JLabelArchivoRuta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 240, 370, 40));

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

        jPanel1.add(BarraHorizontal, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 790, 4));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 861, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JBotonLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBotonLimpiarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JBotonLimpiarActionPerformed

    private void JBottomCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBottomCancelarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JBottomCancelarActionPerformed

    
    
    private void JLabelArchivoRutaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLabelArchivoRutaMouseClicked
       
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccionar archivo");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        fc.setAcceptAllFileFilterUsed(true);
        fc.addChoosableFileFilter(
            new FileNameExtensionFilter(
                "Documentos (PDF, Word, Excel, Imágenes, TXT)",
                "pdf","doc","docx","xls","xlsx","csv","ppt","pptx","jpg","jpeg","png","gif","txt"
            )
        );

        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File f = fc.getSelectedFile();
        rutaSeleccionada = f.toPath();
        JLabelArchivoRuta.setText(rutaSeleccionada.toString());

        try {
            // <<< GUARDAMOS los metadatos para usarlos en Guardar >>>
            metaSeleccionada = logica.Metadatos.leer(rutaSeleccionada);
        } catch (RuntimeException ex) {
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "No se pudieron leer metadatos:\n" + ex.getMessage(),
                "Archivo",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
            metaSeleccionada = null;
            rutaSeleccionada = null;
            JLabelArchivoRuta.setText("Ingrese el archivo");
        }
    }//GEN-LAST:event_JLabelArchivoRutaMouseClicked

    private void JTextTituloDocumento1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTextTituloDocumento1MousePressed
         if (JTextTituloDocumento1.getText().equals("Ingrese el título del documento")) {
            JTextTituloDocumento1.setText("");
            JTextTituloDocumento1.setForeground(Color.black);
        }

        // Restaurar otros si están vacíos
        if (JTextCodigo.getText().isEmpty()) {
            JTextCodigo.setText("Ingrese el código");
            JTextCodigo.setForeground(Color.GRAY);
        }
        if (JTextDescripcion.getText().isEmpty()) {
            JTextDescripcion.setText("Agrega una pequeña descripción del documento");
            JTextDescripcion.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_JTextTituloDocumento1MousePressed

    private void JTextCodigoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTextCodigoMousePressed
        if (JTextCodigo.getText().equals("Ingrese el código")) {
            JTextCodigo.setText("");
            JTextCodigo.setForeground(Color.black);
        }

        if (JTextTituloDocumento1.getText().isEmpty()) {
            JTextTituloDocumento1.setText("Ingrese el título del documento");
            JTextTituloDocumento1.setForeground(Color.GRAY);
        }
        if (JTextDescripcion.getText().isEmpty()) {
            JTextDescripcion.setText("Agrega una pequeña descripción del documento");
            JTextDescripcion.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_JTextCodigoMousePressed

    private void JTextDescripcionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTextDescripcionMousePressed
        if (JTextDescripcion.getText().equals("Agrega una pequeña descripción del documento")) {
            JTextDescripcion.setText("");
            JTextDescripcion.setForeground(Color.black);
        }

        if (JTextTituloDocumento1.getText().isEmpty()) {
            JTextTituloDocumento1.setText("Ingrese el título del documento");
            JTextTituloDocumento1.setForeground(Color.GRAY);
        }
        if (JTextCodigo.getText().isEmpty()) {
            JTextCodigo.setText("Ingrese el código");
            JTextCodigo.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_JTextDescripcionMousePressed

    private void JBotonGuardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JBotonGuardarMouseClicked
        try {
            // Validar campos
            if (!validarCampos()) {
                return; // Si no se pasa la validación, no continuar
            }

            // Si los campos son válidos, proceder a guardar los datos
            persistencia.Documento d = new persistencia.Documento();
            d.setNombre(JTextTituloDocumento1.getText().trim());
            d.setCodigo(JTextCodigo.getText().trim());
            d.setDescripcion(JTextDescripcion.getText().trim());
            d.setIdCategoria(new logica.CategoriaDao().idPorNombre((String) JComboBoxCategorias.getSelectedItem()));
            d.setTipoAcceso((String) JComboBoxTipoAcceso.getSelectedItem());
            d.setDisposicionFinal((String) JFechaDisposicionFinal.getSelectedItem());

            // Metadatos del archivo
            d.setFileName(metaSeleccionada.originalName());
            d.setFileType(metaSeleccionada.tipo());
            d.setFileSizeBytes(metaSeleccionada.sizeBytes());

            // Casi se me olvida cambiar esto
            d.setUsuario_Demo(usuario.getNombreCompleto());
            d.setCorreo_demo(usuario.getEmail());
            d.setDepartamento(usuario.getDepartamento());
            d.setMunicipio(usuario.getMunicipio());

            // Guardar documento
            long id = new logica.DocumentoDao().guardarDocumento(d, rutaSeleccionada);
            javax.swing.JOptionPane.showMessageDialog(this, "Guardado. ID: " + id);

            
            // =================== CORREO DE NOTIFICACIÓN ===================
            
            new Thread(() -> {
                try {
                    enviarCorreo.notificarNuevoDocumento(d);
                } catch (Exception exCorreo) {
                    System.err.println(
                        "No se pudo enviar el aviso de nuevo documento: " + exCorreo.getMessage()
                    );
                }
            }).start();

            // =============================================================
            
            // Cerrar la ventana de carga de documento
            this.dispose();

            // Abrir la ventana de gestor de documentos
            Gestor_De_Documentos ventanaGestorDeDocumentos = new Gestor_De_Documentos("", 0L);
            ventanaGestorDeDocumentos.setLocationRelativeTo(null);
            ventanaGestorDeDocumentos.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_JBotonGuardarMouseClicked
    
    // En dado caso se cancele se lleva a la principal
    
    private void JBottomCancelarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JBottomCancelarMouseClicked
        // Cerrar login
        this.dispose();

        // Abrir la ventana de gestor de documentos
        Gestor_De_Documentos ventanaGestorDeDocumentos;
        ventanaGestorDeDocumentos = new Gestor_De_Documentos("", 0L);
        ventanaGestorDeDocumentos.setLocationRelativeTo(null);
        ventanaGestorDeDocumentos.setVisible(true);
    }//GEN-LAST:event_JBottomCancelarMouseClicked

    private void JBotonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBotonGuardarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JBotonGuardarActionPerformed

    private void JBotonLimpiarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JBotonLimpiarMouseClicked
        // Limpiar los campos de texto
        JTextTituloDocumento1.setText("Ingrese el título del documento");
        JTextTituloDocumento1.setForeground(Color.GRAY);

        JTextCodigo.setText("Ingrese el código");
        JTextCodigo.setForeground(Color.GRAY);

        JTextDescripcion.setText("Agrega una pequeña descripción del documento");
        JTextDescripcion.setForeground(Color.GRAY);

        // Limpiar los JComboBox y restablecer valores predeterminados
        JComboBoxCategorias.setSelectedIndex(0); // Valor por defecto (Primera opción)
        JComboBoxTipoAcceso.setSelectedIndex(0); // Valor por defecto (Primera opción)
        JFechaDisposicionFinal.setSelectedIndex(0); // Valor por defecto (Primera opción)

        // Limpiar la ruta del archivo seleccionada
        JLabelArchivoRuta.setText("Ingrese el archivo");
        rutaSeleccionada = null; // Restablecer el archivo seleccionado

        // Mostrar un mensaje de confirmación
        JOptionPane.showMessageDialog(this, "Los campos han sido limpiados.");
    }//GEN-LAST:event_JBotonLimpiarMouseClicked

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
        java.awt.EventQueue.invokeLater(() -> new Cargar_Documento().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BarraHorizontal;
    private javax.swing.JLabel DescripcionCargar;
    private javax.swing.JButton JBotonGuardar;
    private javax.swing.JButton JBotonLimpiar;
    private javax.swing.JButton JBottomCancelar;
    private javax.swing.JComboBox<String> JComboBoxCategorias;
    private javax.swing.JComboBox<String> JComboBoxTipoAcceso;
    private javax.swing.JComboBox<String> JFechaDisposicionFinal;
    private javax.swing.JLabel JLabelArchivoRuta;
    private javax.swing.JTextField JTextCodigo;
    private javax.swing.JTextArea JTextDescripcion;
    private javax.swing.JTextField JTextTituloDocumento1;
    private javax.swing.JLabel TituloCargarDocumento;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
