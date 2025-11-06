package ui;

import java.awt.Color;

public class Registrarse extends javax.swing.JFrame {
    
    int xMouse, yMouse;
    

    
    public Registrarse() {
        initComponents();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField6 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        bg = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
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

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setBackground(new java.awt.Color(0, 0, 102));
        jButton1.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Registrarse");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        bg.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 470, 180, 40));

        citybg.setBackground(new java.awt.Color(0, 134, 190));
        citybg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ciudad_verde.png"))); // NOI18N
        bg.add(citybg, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 0, 330, 540));

        header.setBackground(new java.awt.Color(255, 255, 255));
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

        exitBtn.setBackground(new java.awt.Color(255, 255, 255));

        exitTxt.setFont(new java.awt.Font("Roboto Light", 0, 24)); // NOI18N
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
            .addComponent(exitTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        exitBtnLayout.setVerticalGroup(
            exitBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(exitTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        bg.add(texto_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 350, -1));

        texto_2.setFont(new java.awt.Font("Roboto Black", 1, 30)); // NOI18N
        texto_2.setForeground(new java.awt.Color(0, 102, 0));
        texto_2.setText("LLENA TODOS LOS CAMPOS");
        bg.add(texto_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        CasillaCargo.setFont(new java.awt.Font("Roboto SemiCondensed", 0, 14)); // NOI18N
        CasillaCargo.setForeground(new java.awt.Color(102, 102, 102));
        CasillaCargo.setText("Ingrese el cargo");
        CasillaCargo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CasillaCargoActionPerformed(evt);
            }
        });
        bg.add(CasillaCargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 400, 270, 40));

        SelectMunicipio.setFont(new java.awt.Font("Roboto SemiCondensed", 0, 14)); // NOI18N
        SelectMunicipio.setForeground(new java.awt.Color(102, 102, 102));
        SelectMunicipio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
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
        ConfirmPassword.setText("jPasswordField1");
        ConfirmPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfirmPasswordActionPerformed(evt);
            }
        });
        bg.add(ConfirmPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 480, 270, 40));

        CasillaApellido.setFont(new java.awt.Font("Roboto SemiCondensed", 0, 14)); // NOI18N
        CasillaApellido.setForeground(new java.awt.Color(102, 102, 102));
        CasillaApellido.setText("Ingrese su apellido");
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
        CasillaEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CasillaEmailActionPerformed(evt);
            }
        });
        bg.add(CasillaEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 270, 40));

        SelectDepart.setFont(new java.awt.Font("Roboto SemiCondensed", 0, 14)); // NOI18N
        SelectDepart.setForeground(new java.awt.Color(102, 102, 102));
        SelectDepart.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Amazonas", "Antioquia", "Arauca", "Atlántico", "Bogotá, D.C.", "Bolívar", "Boyacá", "Caldas", "Caquetá", "Casanare", "Cauca", "Cesar", "Chocó", "Córdoba", "Cundinamarca", "Guainía", "Guaviare", "Huila", "La Guajira", "Magdalena", "Meta", "Nariño", "Norte de Santander", "Putumayo", "Quindío", "Risaralda", "San Andrés y Providencia", "Santander", "Sucre", "Tolima", "Valle del Cauca", "Vaupés", "Vichada" }));
        SelectDepart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectDepartActionPerformed(evt);
            }
        });
        bg.add(SelectDepart, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 270, 40));

        CasillaNumero.setFont(new java.awt.Font("Roboto SemiCondensed", 0, 14)); // NOI18N
        CasillaNumero.setForeground(new java.awt.Color(102, 102, 102));
        CasillaNumero.setText("+57 --- ----- ----");
        CasillaNumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CasillaNumeroActionPerformed(evt);
            }
        });
        bg.add(CasillaNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 240, 270, 40));

        Password.setFont(new java.awt.Font("Roboto Condensed", 2, 12)); // NOI18N
        Password.setForeground(new java.awt.Color(102, 102, 102));
        Password.setText("jPasswordField1");
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
        exitBtn.setBackground(Color.white);
        exitTxt.setForeground(Color.black);
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
    }//GEN-LAST:event_CasillaNombreMousePressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Registrarse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Registrarse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Registrarse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Registrarse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Registrarse().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Apellidos;
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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel texto_1;
    private javax.swing.JLabel texto_2;
    // End of variables declaration//GEN-END:variables
}
