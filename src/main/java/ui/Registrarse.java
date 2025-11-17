package ui;

import java.awt.Color;
import com.formdev.flatlaf.FlatLightLaf;
import logica.RegistroManager;
import persistencia.Usuario;
import logica.ValidationResult;
import java.util.HashMap;
import java.util.Map;
import javax.swing.UIManager;
import logica.EnviadorCorreos;

public class Registrarse extends javax.swing.JFrame {
    
    private Map<String, String[]> departamentosMunicipios;
    private final RegistroManager registroManager = new RegistroManager("usuarios.csv");
    private EnviadorCorreos enviarCorreo = new EnviadorCorreos();

    
    int xMouse, yMouse;   

    
    public Registrarse() {
        initComponents();
        initDepartamentosMunicipios();
        configurarComboBoxes();
        setupDepartamentoListener();
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
    }
    
    private void initDepartamentosMunicipios() {
        departamentosMunicipios = new HashMap<>();
        
        departamentosMunicipios.put("Cundinamarca", new String[]{
            "Bogotá", "Soacha", "Chía", "Cajicá", "La Calera"
        });

        departamentosMunicipios.put("Antioquia", new String[]{
            "Medellín", "Bello", "Itagüí", "Envigado", "Girardota"
        });

        departamentosMunicipios.put("Santander", new String[]{
            "Bucaramanga", "Floridablanca", "Girón", "Piedecuesta", "San Alberto"
        });

        // Otros 5 departamentos con algunos municipios
        departamentosMunicipios.put("ValledelCauca", new String[]{
            "Cali", "Palmira", "Yumbo", "Jamundí", "Buga"
        });

        departamentosMunicipios.put("Atlántico", new String[]{
            "Barranquilla", "Soledad", "Malambo", "Galapa", "Puerto Colombia"
        });

        departamentosMunicipios.put("Bolívar", new String[]{
            "Cartagena", "Turbaná", "Magangué", "Mahates", "San Fernando"
        });

        departamentosMunicipios.put("Meta", new String[]{
            "Villavicencio", "Acacías", "Granada", "Cumaral", "Guamal"
        });

        departamentosMunicipios.put("Boyacá", new String[]{
            "Tunja", "Duitama", "Sogamoso", "Paipa", "Villa de Leyva"
        });
    }
    
    private void setupDepartamentoListener() {
        SelectDepart.addActionListener(evt -> updateMunicipios());
    }
    
    private void updateMunicipios() {
        // Obtener el departamento seleccionado
        String departamentoSeleccionado = (String) SelectDepart.getSelectedItem();
        String[] municipios;

        // Obtener los municipios correspondientes al departamento
        if (departamentosMunicipios.containsKey(departamentoSeleccionado)) {
            municipios = departamentosMunicipios.get(departamentoSeleccionado);
        } else {
            municipios = new String[]{};
        }

        // Actualizar el JComboBox de municipios
        SelectMunicipio.setModel(new javax.swing.DefaultComboBoxModel<>(municipios));
        SelectMunicipio.setEnabled(true);  // Habilitar el JComboBox de municipios
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField6 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        bg = new javax.swing.JPanel();
        BotonRegistrarse = new javax.swing.JButton();
        BotonDevolverse = new javax.swing.JButton();
        citybg = new javax.swing.JLabel();
        header = new javax.swing.JPanel();
        exitBtn = new javax.swing.JPanel();
        exitTxt = new javax.swing.JLabel();
        texto_1 = new javax.swing.JLabel();
        texto_2 = new javax.swing.JLabel();
        CasillaCargo = new javax.swing.JTextField();
        SelectMunicipio = new javax.swing.JComboBox<>();
        SelectDependencia = new javax.swing.JComboBox<>();
        ConfirmPassword = new javax.swing.JPasswordField();
        CasillaApellido = new javax.swing.JTextField();
        CasillaNombre = new javax.swing.JTextField();
        CasillaEmail = new javax.swing.JTextField();
        SelectDepart = new javax.swing.JComboBox<>();
        CasillaNumero = new javax.swing.JTextField();
        Password = new javax.swing.JPasswordField();
        Apellidos = new javax.swing.JLabel();
        Municipio = new javax.swing.JLabel();
        Nombre = new javax.swing.JLabel();
        Email = new javax.swing.JLabel();
        Numero = new javax.swing.JLabel();
        Cargo = new javax.swing.JLabel();
        Departamento = new javax.swing.JLabel();
        Confirmar_contraseña = new javax.swing.JLabel();
        Dependencia = new javax.swing.JLabel();
        Contraseña = new javax.swing.JLabel();

        jTextField6.setText("jTextField1");
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        jLabel5.setText("Numero");

        jLabel10.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        jLabel10.setText("Dependencia Perteneciente");

        jLabel11.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        jLabel11.setText("Cargo");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setUndecorated(true);
        setResizable(false);

        bg.setBackground(new java.awt.Color(255, 251, 248));
        bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BotonRegistrarse.setBackground(new java.awt.Color(0, 0, 102));
        BotonRegistrarse.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        BotonRegistrarse.setForeground(new java.awt.Color(255, 255, 255));
        BotonRegistrarse.setText("REGISTRARSE");
        BotonRegistrarse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BotonRegistrarseMouseClicked(evt);
            }
        });
        BotonRegistrarse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonRegistrarseActionPerformed(evt);
            }
        });
        bg.add(BotonRegistrarse, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 470, 180, 40));

        BotonDevolverse.setBackground(new java.awt.Color(0, 0, 102));
        BotonDevolverse.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        BotonDevolverse.setForeground(new java.awt.Color(255, 255, 255));
        BotonDevolverse.setText("DEVOLVERSE");
        BotonDevolverse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BotonDevolverseMouseClicked(evt);
            }
        });
        BotonDevolverse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonDevolverseActionPerformed(evt);
            }
        });
        bg.add(BotonDevolverse, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 420, 180, 40));

        citybg.setBackground(new java.awt.Color(0, 134, 190));
        citybg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ciudad_verde.png"))); // NOI18N
        bg.add(citybg, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 0, 330, 550));

        header.setBackground(new java.awt.Color(150, 150, 150));
        header.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                headerMouseDragged(evt);
            }
        });
        header.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                headerMousePressed(evt);
            }
        });

        exitBtn.setBackground(new java.awt.Color(150, 150, 150));

        exitTxt.setFont(new java.awt.Font("Roboto Light", 0, 24)); // NOI18N
        exitTxt.setForeground(new java.awt.Color(255, 255, 255));
        exitTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        exitTxt.setText("X");
        exitTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        exitTxt.setPreferredSize(new java.awt.Dimension(40, 40));
        exitTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitTxtMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitTxtMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitTxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout exitBtnLayout = new javax.swing.GroupLayout(exitBtn);
        exitBtn.setLayout(exitBtnLayout);
        exitBtnLayout.setHorizontalGroup(
            exitBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, exitBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(exitTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        exitBtnLayout.setVerticalGroup(
            exitBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(exitBtnLayout.createSequentialGroup()
                .addComponent(exitTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(exitBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        bg.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 840, 40));

        texto_1.setFont(new java.awt.Font("Roboto Black", 1, 30)); // NOI18N
        texto_1.setText("REGÍSTRATE AQUÍ");
        bg.add(texto_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 350, -1));

        texto_2.setFont(new java.awt.Font("Roboto Black", 1, 30)); // NOI18N
        texto_2.setForeground(new java.awt.Color(0, 102, 0));
        texto_2.setText("LLENA TODOS LOS CAMPOS");
        bg.add(texto_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        CasillaCargo.setFont(new java.awt.Font("Roboto SemiCondensed", 0, 14)); // NOI18N
        CasillaCargo.setForeground(new java.awt.Color(102, 102, 102));
        CasillaCargo.setText("Ingrese el cargo");
        CasillaCargo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                CasillaCargoMousePressed(evt);
            }
        });
        CasillaCargo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CasillaCargoActionPerformed(evt);
            }
        });
        bg.add(CasillaCargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 400, 270, 40));

        SelectMunicipio.setFont(new java.awt.Font("Roboto SemiCondensed", 0, 14)); // NOI18N
        SelectMunicipio.setForeground(new java.awt.Color(102, 102, 102));
        SelectMunicipio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectMunicipioActionPerformed(evt);
            }
        });
        bg.add(SelectMunicipio, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 320, 270, 40));

        SelectDependencia.setFont(new java.awt.Font("Roboto Condensed", 0, 14)); // NOI18N
        SelectDependencia.setForeground(new java.awt.Color(102, 102, 102));
        SelectDependencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Secretaría General", "Secretaría de Gobierno", "Secretaría de Hacienda", "Secretaría de Planeación", "Secretaría de Salud", "Secretaría de Educación", "Secretaría de Infraestructura", "Secretaría de Desarrollo Social", "Secretaría de Desarrollo Económico", "Secretaría de Agricultura", "Secretaría de Ambiente", "Secretaría de Tránsito y Transporte", "Secretaría de Cultura y Turismo", "Secretaría de Deporte y Recreación", "Oficina Jurídica", "Oficina de Control Interno", "Oficina TIC / Sistemas", "Gestión Documental / Archivo", "Oficina de Prensa y Comunicaciones", "Gestión del Riesgo", "Inspección de Policía", "Comisaría de Familia", "Personería (si aplica)" }));
        SelectDependencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectDependenciaActionPerformed(evt);
            }
        });
        bg.add(SelectDependencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 270, 40));

        ConfirmPassword.setFont(new java.awt.Font("Roboto Condensed", 2, 12)); // NOI18N
        ConfirmPassword.setForeground(new java.awt.Color(102, 102, 102));
        ConfirmPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ConfirmPasswordMousePressed(evt);
            }
        });
        ConfirmPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfirmPasswordActionPerformed(evt);
            }
        });
        bg.add(ConfirmPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 480, 270, 40));

        CasillaApellido.setFont(new java.awt.Font("Roboto SemiCondensed", 0, 14)); // NOI18N
        CasillaApellido.setForeground(new java.awt.Color(102, 102, 102));
        CasillaApellido.setText("Ingrese su apellido");
        CasillaApellido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                CasillaApellidoMousePressed(evt);
            }
        });
        CasillaApellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CasillaApellidoActionPerformed(evt);
            }
        });
        bg.add(CasillaApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 160, 270, 40));

        CasillaNombre.setFont(new java.awt.Font("Roboto SemiCondensed", 0, 14)); // NOI18N
        CasillaNombre.setForeground(new java.awt.Color(102, 102, 102));
        CasillaNombre.setText("Ingrese su nombre");
        CasillaNombre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                CasillaNombreMousePressed(evt);
            }
        });
        CasillaNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CasillaNombreActionPerformed(evt);
            }
        });
        bg.add(CasillaNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 270, 40));

        CasillaEmail.setFont(new java.awt.Font("Roboto SemiCondensed", 0, 14)); // NOI18N
        CasillaEmail.setForeground(new java.awt.Color(102, 102, 102));
        CasillaEmail.setText("correo@gmail.com");
        CasillaEmail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                CasillaEmailMousePressed(evt);
            }
        });
        CasillaEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CasillaEmailActionPerformed(evt);
            }
        });
        bg.add(CasillaEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 270, 40));

        SelectDepart.setFont(new java.awt.Font("Roboto SemiCondensed", 0, 14)); // NOI18N
        SelectDepart.setForeground(new java.awt.Color(102, 102, 102));
        SelectDepart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectDepartActionPerformed(evt);
            }
        });
        bg.add(SelectDepart, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 270, 40));

        CasillaNumero.setFont(new java.awt.Font("Roboto SemiCondensed", 0, 14)); // NOI18N
        CasillaNumero.setForeground(new java.awt.Color(102, 102, 102));
        CasillaNumero.setText("+57 --- ----- ----");
        CasillaNumero.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                CasillaNumeroMousePressed(evt);
            }
        });
        CasillaNumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CasillaNumeroActionPerformed(evt);
            }
        });
        bg.add(CasillaNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 240, 270, 40));

        Password.setFont(new java.awt.Font("Roboto Condensed", 2, 12)); // NOI18N
        Password.setForeground(new java.awt.Color(102, 102, 102));
        Password.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                PasswordMousePressed(evt);
            }
        });
        Password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PasswordActionPerformed(evt);
            }
        });
        bg.add(Password, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 270, 40));

        Apellidos.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        Apellidos.setText("Apellidos");
        bg.add(Apellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, -1, 20));

        Municipio.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        Municipio.setText("Municipio");
        bg.add(Municipio, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 290, -1, 30));

        Nombre.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        Nombre.setText("Nombre");
        bg.add(Nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, 20));

        Email.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        Email.setText("Email");
        bg.add(Email, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, 20));

        Numero.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        Numero.setText("Numero");
        bg.add(Numero, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 210, -1, 20));

        Cargo.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        Cargo.setText("Cargo");
        bg.add(Cargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 370, -1, 30));

        Departamento.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        Departamento.setText("Departamento");
        bg.add(Departamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, 30));

        Confirmar_contraseña.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        Confirmar_contraseña.setText("Confirmar Contraseña");
        bg.add(Confirmar_contraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 450, -1, 30));

        Dependencia.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        Dependencia.setText("Dependencia Perteneciente");
        bg.add(Dependencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, -1, 30));

        Contraseña.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        Contraseña.setText("Contraseña");
        bg.add(Contraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, -1, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void headerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_headerMousePressed

    private void headerMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_headerMouseDragged

    private void exitTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitTxtMouseClicked
        System.exit(0);
    }//GEN-LAST:event_exitTxtMouseClicked

    private void exitTxtMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitTxtMouseEntered
        exitBtn.setBackground(Color.red);
        exitTxt.setForeground(Color.white);
    }//GEN-LAST:event_exitTxtMouseEntered

    private void exitTxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitTxtMouseExited
        exitBtn.setBackground(new Color(150, 150, 150));
        exitTxt.setForeground(Color.white);
    }//GEN-LAST:event_exitTxtMouseExited

    private void CasillaCargoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CasillaCargoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CasillaCargoActionPerformed

    private void SelectMunicipioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectMunicipioActionPerformed
        
    }//GEN-LAST:event_SelectMunicipioActionPerformed

    private void SelectDependenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectDependenciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SelectDependenciaActionPerformed

    private void ConfirmPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConfirmPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ConfirmPasswordActionPerformed

    private void CasillaApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CasillaApellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CasillaApellidoActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void CasillaEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CasillaEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CasillaEmailActionPerformed

    private void SelectDepartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectDepartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SelectDepartActionPerformed

    private void CasillaNumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CasillaNumeroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CasillaNumeroActionPerformed

    private void PasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PasswordActionPerformed

    private void CasillaNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CasillaNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CasillaNombreActionPerformed

    private void CasillaNombreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CasillaNombreMousePressed
        if (CasillaNombre.getText().equals("Ingrese su nombre")) {
            CasillaNombre.setText("");
            CasillaNombre.setForeground(Color.black);
        }
        // Restaurar placeholders en los demás si están vacíos
        if (CasillaApellido.getText().isEmpty()) {
            CasillaApellido.setText("Ingrese su apellido");
            CasillaApellido.setForeground(Color.gray);
        }
        if (CasillaEmail.getText().isEmpty()) {
            CasillaEmail.setText("correo@gmail.com");
            CasillaEmail.setForeground(Color.gray);
        }
        if (CasillaNumero.getText().isEmpty()) {
            CasillaNumero.setText("+57 --- ----- ----");
            CasillaNumero.setForeground(Color.gray);
        }
        if (CasillaCargo.getText().isEmpty()) {
            CasillaCargo.setText("Ingrese el cargo");
            CasillaCargo.setForeground(Color.gray);
        }
        if (Password.getPassword().length == 0) {
            Password.setText("********");
            Password.setForeground(Color.gray);
            Password.setEchoChar((char)0);
        }
        if (ConfirmPassword.getPassword().length == 0) {
            ConfirmPassword.setText("********");
            ConfirmPassword.setForeground(Color.gray);
            ConfirmPassword.setEchoChar((char)0);
        }
    }//GEN-LAST:event_CasillaNombreMousePressed

    private void BotonRegistrarseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonRegistrarseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BotonRegistrarseActionPerformed

    private void CasillaApellidoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CasillaApellidoMousePressed
        if (CasillaApellido.getText().equals("Ingrese su apellido")) {
            CasillaApellido.setText("");
            CasillaApellido.setForeground(Color.black);
        }
        if (CasillaNombre.getText().isEmpty()) {
            CasillaNombre.setText("Ingrese su nombre");
            CasillaNombre.setForeground(Color.gray);
        }
        if (CasillaEmail.getText().isEmpty()) {
            CasillaEmail.setText("correo@gmail.com");
            CasillaEmail.setForeground(Color.gray);
        }
        if (CasillaNumero.getText().isEmpty()) {
            CasillaNumero.setText("+57 --- ----- ----");
            CasillaNumero.setForeground(Color.gray);
        }
        if (CasillaCargo.getText().isEmpty()) {
            CasillaCargo.setText("Ingrese el cargo");
            CasillaCargo.setForeground(Color.gray);
        }
        if (Password.getPassword().length == 0) {
            Password.setText("********");
            Password.setForeground(Color.gray);
            Password.setEchoChar((char)0);
        }
        if (ConfirmPassword.getPassword().length == 0) {
            ConfirmPassword.setText("********");
            ConfirmPassword.setForeground(Color.gray);
            ConfirmPassword.setEchoChar((char)0);
        }
    }//GEN-LAST:event_CasillaApellidoMousePressed

    private void CasillaEmailMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CasillaEmailMousePressed
        if (CasillaEmail.getText().equals("correo@gmail.com")) {
            CasillaEmail.setText("");
            CasillaEmail.setForeground(Color.black);
        }
        if (CasillaNombre.getText().isEmpty()) {
            CasillaNombre.setText("Ingrese su nombre");
            CasillaNombre.setForeground(Color.gray);
        }
        if (CasillaApellido.getText().isEmpty()) {
            CasillaApellido.setText("Ingrese su apellido");
            CasillaApellido.setForeground(Color.gray);
        }
        if (CasillaNumero.getText().isEmpty()) {
            CasillaNumero.setText("+57 --- ----- ----");
            CasillaNumero.setForeground(Color.gray);
        }
        if (CasillaCargo.getText().isEmpty()) {
            CasillaCargo.setText("Ingrese el cargo");
            CasillaCargo.setForeground(Color.gray);
        }
        if (Password.getPassword().length == 0) {
            Password.setText("********");
            Password.setForeground(Color.gray);
            Password.setEchoChar((char)0);
        }
        if (ConfirmPassword.getPassword().length == 0) {
            ConfirmPassword.setText("********");
            ConfirmPassword.setForeground(Color.gray);
            ConfirmPassword.setEchoChar((char)0);
        }
    }//GEN-LAST:event_CasillaEmailMousePressed

    private void CasillaNumeroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CasillaNumeroMousePressed
        if (CasillaNumero.getText().equals("+57 --- ----- ----")) {
            CasillaNumero.setText("");
            CasillaNumero.setForeground(Color.black);
        }
        if (CasillaNombre.getText().isEmpty()) {
            CasillaNombre.setText("Ingrese su nombre");
            CasillaNombre.setForeground(Color.gray);
        }
        if (CasillaApellido.getText().isEmpty()) {
            CasillaApellido.setText("Ingrese su apellido");
            CasillaApellido.setForeground(Color.gray);
        }
        if (CasillaEmail.getText().isEmpty()) {
            CasillaEmail.setText("correo@gmail.com");
            CasillaEmail.setForeground(Color.gray);
        }
        if (CasillaCargo.getText().isEmpty()) {
            CasillaCargo.setText("Ingrese el cargo");
            CasillaCargo.setForeground(Color.gray);
        }
        if (Password.getPassword().length == 0) {
            Password.setText("********");
            Password.setForeground(Color.gray);
            Password.setEchoChar((char)0);
        }
        if (ConfirmPassword.getPassword().length == 0) {
            ConfirmPassword.setText("********");
            ConfirmPassword.setForeground(Color.gray);
            ConfirmPassword.setEchoChar((char)0);
        }
    }//GEN-LAST:event_CasillaNumeroMousePressed

    private void CasillaCargoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CasillaCargoMousePressed
        if (CasillaCargo.getText().equals("Ingrese el cargo")) {
            CasillaCargo.setText("");
            CasillaCargo.setForeground(Color.black);
        }
        if (CasillaNombre.getText().isEmpty()) {
            CasillaNombre.setText("Ingrese su nombre");
            CasillaNombre.setForeground(Color.gray);
        }
        if (CasillaApellido.getText().isEmpty()) {
            CasillaApellido.setText("Ingrese su apellido");
            CasillaApellido.setForeground(Color.gray);
        }
        if (CasillaEmail.getText().isEmpty()) {
            CasillaEmail.setText("correo@gmail.com");
            CasillaEmail.setForeground(Color.gray);
        }
        if (CasillaNumero.getText().isEmpty()) {
            CasillaNumero.setText("+57 --- ----- ----");
            CasillaNumero.setForeground(Color.gray);
        }
        if (Password.getPassword().length == 0) {
            Password.setText("********");
            Password.setForeground(Color.gray);
            Password.setEchoChar((char)0);
        }
        if (ConfirmPassword.getPassword().length == 0) {
            ConfirmPassword.setText("********");
            ConfirmPassword.setForeground(Color.gray);
            ConfirmPassword.setEchoChar((char)0);
        }
    }//GEN-LAST:event_CasillaCargoMousePressed

    private void PasswordMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PasswordMousePressed
        String pwd = new String(Password.getPassword());
        if (pwd.equals("********")) {
            Password.setText("");
            Password.setForeground(Color.black);
            Password.setEchoChar('\u2022'); // vuelve a ocultar
        }
        // Placeholders en los demás
        if (CasillaNombre.getText().isEmpty()) {
            CasillaNombre.setText("Ingrese su nombre");
            CasillaNombre.setForeground(Color.gray);
        }
        if (CasillaApellido.getText().isEmpty()) {
            CasillaApellido.setText("Ingrese su apellido");
            CasillaApellido.setForeground(Color.gray);
        }
        if (CasillaEmail.getText().isEmpty()) {
            CasillaEmail.setText("correo@gmail.com");
            CasillaEmail.setForeground(Color.gray);
        }
        if (CasillaNumero.getText().isEmpty()) {
            CasillaNumero.setText("+57 --- ----- ----");
            CasillaNumero.setForeground(Color.gray);
        }
        if (CasillaCargo.getText().isEmpty()) {
            CasillaCargo.setText("Ingrese el cargo");
            CasillaCargo.setForeground(Color.gray);
        }
        if (ConfirmPassword.getPassword().length == 0) {
            ConfirmPassword.setText("********");
            ConfirmPassword.setForeground(Color.gray);
            ConfirmPassword.setEchoChar((char)0);
        }
    }//GEN-LAST:event_PasswordMousePressed

    private void ConfirmPasswordMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ConfirmPasswordMousePressed
        String pwd = new String(ConfirmPassword.getPassword());
        if (pwd.equals("********")) {
            ConfirmPassword.setText("");
            ConfirmPassword.setForeground(Color.black);
            ConfirmPassword.setEchoChar('\u2022');
        }
        if (CasillaNombre.getText().isEmpty()) {
            CasillaNombre.setText("Ingrese su nombre");
            CasillaNombre.setForeground(Color.gray);
        }
        if (CasillaApellido.getText().isEmpty()) {
            CasillaApellido.setText("Ingrese su apellido");
            CasillaApellido.setForeground(Color.gray);
        }
        if (CasillaEmail.getText().isEmpty()) {
            CasillaEmail.setText("correo@gmail.com");
            CasillaEmail.setForeground(Color.gray);
        }
        if (CasillaNumero.getText().isEmpty()) {
            CasillaNumero.setText("+57 --- ----- ----");
            CasillaNumero.setForeground(Color.gray);
        }
        if (CasillaCargo.getText().isEmpty()) {
            CasillaCargo.setText("Ingrese el cargo");
            CasillaCargo.setForeground(Color.gray);
        }
        if (Password.getPassword().length == 0) {
            Password.setText("********");
            Password.setForeground(Color.gray);
            Password.setEchoChar((char)0);
        }
    }//GEN-LAST:event_ConfirmPasswordMousePressed

    private void BotonRegistrarseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotonRegistrarseMouseClicked
        // 1) Leer campos
        String nombre       = CasillaNombre.getText().trim();
        String apellido     = CasillaApellido.getText().trim();
        String email        = CasillaEmail.getText().trim().toLowerCase();
        String telefono     = CasillaNumero.getText().replaceAll("\\s+", "").trim();
        String departamento = (String) SelectDepart.getSelectedItem();
        String municipio    = (String) SelectMunicipio.getSelectedItem();
        String dependencia  = (String) SelectDependencia.getSelectedItem();
        String cargo        = CasillaCargo.getText().trim();

        // 2) Contraseñas
        String pwd1 = new String(Password.getPassword());
        String pwd2 = new String(ConfirmPassword.getPassword());

        // ========== DEBUG (IMPRIMIR TODO EN CONSOLA) ==========
        System.out.println("===== DATOS CAPTURADOS EN FORMULARIO =====");
        System.out.println("Nombre: " + nombre);
        System.out.println("Apellidos: " + apellido);
        System.out.println("Email: " + email);
        System.out.println("Teléfono: " + telefono);
        System.out.println("Departamento: " + departamento);
        System.out.println("Municipio: " + municipio);
        System.out.println("Dependencia: " + dependencia);
        System.out.println("Cargo: " + cargo);
        System.out.println("Contraseña (pwd1): " + pwd1);
        System.out.println("Confirmar Contraseña (pwd2): " + pwd2);
        System.out.println("==========================================");

        // 3) Validación UI: contraseñas coinciden
        if (!pwd1.equals(pwd2)) {
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Las contraseñas no coinciden.",
                "Errores de validación",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 4) Construir usuario
        Usuario nuevo = new Usuario(
            nombre, apellido, email, telefono,
            departamento, municipio, dependencia, cargo,
            "" // hashContrasena lo asigna RegistroManager
        );

        // 5) Registrar
        RegistroManager manager = new RegistroManager("usuarios.csv");
        ValidationResult res = manager.registrarUsuario(nuevo, pwd1);

        // 6) Mostrar resultado
        if (res.isSuccess()) {
            
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "¡Usuario registrado correctamente!",
                "Registro",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

            this.dispose();
            Login ventanaLogin = new Login();
            ventanaLogin.setLocationRelativeTo(null);
            ventanaLogin.setVisible(true);
            
            // ====== AVISO AL ADMINISTRADOR: NUEVO USUARIO REGISTRADO ======
            new Thread(() -> {
                try {
                    String asunto = "SwiftFold: nuevo usuario registrado";

                    StringBuilder cuerpo = new StringBuilder();
                    cuerpo.append("Se ha registrado un NUEVO USUARIO en el sistema SwiftFold.\n\n")
                          .append("Datos del usuario registrado:\n")
                          .append("• Nombre completo: ").append(nuevo.getNombreCompleto()).append("\n")
                          .append("• Correo electrónico: ").append(nuevo.getEmail()).append("\n")
                          .append("• Número de contacto: ").append(nuevo.getTelefono()).append("\n")
                          .append("• Cargo: ").append(nuevo.getCargo()).append("\n")
                          .append("• Dependencia: ").append(nuevo.getDependencia()).append("\n")
                          .append("• Municipio / Departamento: ")
                             .append(nuevo.getMunicipio()).append(" - ").append(nuevo.getDepartamento()).append("\n\n")
                          .append("Origen del registro: formulario público de registro.\n\n")
                          .append("Este mensaje es únicamente informativo para el administrador del sistema.");

                    // Envía el correo al/los administradores (direcciones están dentro de EnviadorCorreos)
                    enviarCorreo.nuevoUsuarioEliminarModificar(cuerpo.toString(), asunto);

                } catch (Exception exCorreo) {
                    System.err.println("No se pudo enviar el aviso de nuevo registro al administrador: " + exCorreo.getMessage());
                }
            }).start();
            // =======================================================

        } else {
            StringBuilder sb = new StringBuilder("No se pudo registrar el usuario:\n");
            for (String m : res.getMessages()) {
                sb.append("• ").append(m).append("\n");
            }
            javax.swing.JOptionPane.showMessageDialog(
                this,
                sb.toString(),
                "Errores de validación",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }

        // 7) Limpiar variables sensibles
        java.util.Arrays.fill(pwd1.toCharArray(), '\0');
        java.util.Arrays.fill(pwd2.toCharArray(), '\0');
    }//GEN-LAST:event_BotonRegistrarseMouseClicked

    private void BotonDevolverseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotonDevolverseMouseClicked
        this.dispose();
        Login ventanaLogin = new Login();
        ventanaLogin.setLocationRelativeTo(null);
        ventanaLogin.setVisible(true);
    }//GEN-LAST:event_BotonDevolverseMouseClicked

    private void BotonDevolverseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonDevolverseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BotonDevolverseActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        // Activa FlatLaf (claro)
        FlatLightLaf.setup();

        // Bordes suaves (ajusta el radio si quieres)
        UIManager.put("Component.arc", 3);
        UIManager.put("TextComponent.arc", 3);
        UIManager.put("Button.arc", 3);
        UIManager.put("ProgressBar.arc", 3);
        UIManager.put("ScrollBar.thumbArc", 3);

        // Lanza la ventana
        java.awt.EventQueue.invokeLater(() -> {
            new Registrarse().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Apellidos;
    private javax.swing.JButton BotonDevolverse;
    private javax.swing.JButton BotonRegistrarse;
    private javax.swing.JLabel Cargo;
    private javax.swing.JTextField CasillaApellido;
    private javax.swing.JTextField CasillaCargo;
    private javax.swing.JTextField CasillaEmail;
    private javax.swing.JTextField CasillaNombre;
    private javax.swing.JTextField CasillaNumero;
    private javax.swing.JPasswordField ConfirmPassword;
    private javax.swing.JLabel Confirmar_contraseña;
    private javax.swing.JLabel Contraseña;
    private javax.swing.JLabel Departamento;
    private javax.swing.JLabel Dependencia;
    private javax.swing.JLabel Email;
    private javax.swing.JLabel Municipio;
    private javax.swing.JLabel Nombre;
    private javax.swing.JLabel Numero;
    private javax.swing.JPasswordField Password;
    private javax.swing.JComboBox<String> SelectDepart;
    private javax.swing.JComboBox<String> SelectDependencia;
    private javax.swing.JComboBox<String> SelectMunicipio;
    private javax.swing.JPanel bg;
    private javax.swing.JLabel citybg;
    private javax.swing.JPanel exitBtn;
    private javax.swing.JLabel exitTxt;
    private javax.swing.JPanel header;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel texto_1;
    private javax.swing.JLabel texto_2;
    // End of variables declaration//GEN-END:variables
}
