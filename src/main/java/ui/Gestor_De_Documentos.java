package ui;


import persistencia.SesionSingleton;
import persistencia.Usuario;
import logica.CategoriaDao;
import logica.DocumentoDao;
import java.awt.Color;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import logica.EnviadorCorreos;
import logica.GeneradorDashboardPDF;
import logica.UtilidadesDeArchivos;
import persistencia.Documento;


public class Gestor_De_Documentos extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Gestor_De_Documentos.class.getName());
    private String idSeleccionado;
    private String idSeleccionadoCategoria;
    public Usuario usuario = SesionSingleton.getInstance().getUsuarioLogueado();
    private Long categoriaSelect;
    private EnviadorCorreos enviarCorreo = new EnviadorCorreos();
    private static final String PLACEHOLDER_BUSQUEDA = "Buscar nombre del documento";
    

    /**
     * Creates new form Gestor_De_Documentos
     */
    
     public Gestor_De_Documentos(String nombre, Long Idcategoria) {
            
        initComponents();
        establecerEstadisticas();
        
        JTextBusquedad.setText(PLACEHOLDER_BUSQUEDA);
        JTextBusquedad.setForeground(Color.GRAY);
        
        new DocumentoDao().listar5Cols(this.TablaDocumentos, nombre, Idcategoria);
        new CategoriaDao().listarCategorias(this.TablaCategorias);
        
        estilizarTablaDocumentos();
        estilizarTablaCategorias();
        
        JLabelNombreUsuario.setText(usuario.getNombreCompleto());
        JLabelRolCargo.setText( usuario.getStringRol() + " - " + usuario.getCargo() + " - " + usuario.getDependencia());
        
        // Acciones de los botones, esto es para evitar algunos bugs
        
        JButtomEditar.addActionListener(e -> {
            if (usuario.getRol().tieneAccesoEditar()){
                if (idSeleccionado == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona un documento.", "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                this.dispose();
                editarDocumento(idSeleccionado);
            } else {
                JOptionPane.showMessageDialog(this, "No tienes acceso para editar este documento.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            }

        });

        JButtonEliminar.addActionListener(e -> {
            
            if (usuario.getRol().tieneAccesoEliminar()){
                
                if (idSeleccionado == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona un documento.", "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                eliminarDocumento(idSeleccionado);
                idSeleccionado = null;

             } else {
                JOptionPane.showMessageDialog(this, "No tienes acceso para eliminar este documento.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        
        JButtonVer.addActionListener(e -> {
                          
            if (idSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un documento.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            } 
            
            this.dispose();
            verDocumento(idSeleccionado);

        });
        
        BtnDashboardPDF.addActionListener(e -> {
            
            if (usuario.getRol().tieneAccesoPDF()){
                generarReportePDF();
            } else {
                JOptionPane.showMessageDialog(this, "No tienes acceso para generar el reporte PDF.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            }
        
        });

        
    }
    
    // Metodos extras para simplificar acciones
     
    private void generarReportePDF() {
        try {
            // Sugerir un nombre de archivo
            String nombreSugerido = "SwiftFold_Dashboard_" + 
                    java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) 
                    + ".pdf";

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Guardar reporte de estadísticas en PDF");
            chooser.setSelectedFile(new File(nombreSugerido));

            int opcion = chooser.showSaveDialog(this);
            if (opcion != JFileChooser.APPROVE_OPTION) {
                return; // Usuario canceló
            }

            File archivo = chooser.getSelectedFile();
            // Asegurar extensión .pdf
            if (!archivo.getName().toLowerCase().endsWith(".pdf")) {
                archivo = new File(archivo.getParentFile(), archivo.getName() + ".pdf");
            }

            GeneradorDashboardPDF generador = new GeneradorDashboardPDF();
            boolean ok = generador.generarDashboardCompleto(archivo.getAbsolutePath());

            if (ok) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Reporte generado correctamente:\n" + archivo.getAbsolutePath(),
                        "PDF generado",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Ocurrió un error al generar el reporte.",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Error inesperado: " + ex.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }

     
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

    
    private void verDocumento(String idDocumento){
        try {
            Ver_Documento verDocumentico = new Ver_Documento(idDocumento);
            verDocumentico.setLocationRelativeTo(null);
            verDocumentico.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hubo un error al abrir la ventana de ver documento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     
    private void eliminarDocumento(String idDocumento) {
        try {

            boolean eliminado = new logica.DocumentoDao().eliminarDocumento(idDocumento);

            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Documento eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                new DocumentoDao().listar5Cols(this.TablaDocumentos, "", 0L);
                
                
                // =================== CORREO DE NOTIFICACIÓN ===================
                new Thread(() -> {
                    try {
                        Documento d = new DocumentoDao().obtenerDocumentoPorId(idDocumento);
                        enviarCorreo.notificarDocumentoEliminado(d);
                    } catch (Exception exCorreo) {
                        System.err.println("No se pudo enviar la notificación de documento eliminado: " + exCorreo.getMessage());
                    }
                }).start();
                // =============================================================
            
                estilizarTablaDocumentos();
                
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el documento.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hubo un error al eliminar el documento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void editarDocumento(String idDocumento) {
        try {
            Modificar_Documento ventanaModificar = new Modificar_Documento(idDocumento);
            ventanaModificar.setLocationRelativeTo(null);
            ventanaModificar.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hubo un error al abrir la ventana de modificación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void estilizarTablaDocumentos() {
        // Si aún no hay columnas, no hacemos nada
        if (TablaDocumentos.getColumnModel().getColumnCount() == 0) {
            return;
        }

        // 🎨 Paleta consistente con Gestor de Usuarios
        Color verdeEncabezado = new Color(39, 84, 61);    // verde oscuro SwiftFold
        Color verdeZebra      = new Color(247, 250, 247); // verde casi blanco
        Color verdeSeleccion  = new Color(204, 232, 207); // verde pastel suave
        Color grisGrid        = new Color(230, 230, 230);

        // ====== CONFIGURACIÓN GENERAL DE LA TABLA ======
        TablaDocumentos.setRowHeight(28);
        TablaDocumentos.setShowHorizontalLines(false);
        TablaDocumentos.setShowVerticalLines(false);
        TablaDocumentos.setFillsViewportHeight(true);
        TablaDocumentos.setGridColor(grisGrid);
        TablaDocumentos.setSelectionBackground(verdeSeleccion);
        TablaDocumentos.setSelectionForeground(new Color(20, 61, 36)); // texto selección

        // ====== ENCABEZADO ======
        java.awt.Font headerFont = new java.awt.Font("Inter", java.awt.Font.BOLD, 13);
        javax.swing.table.JTableHeader header = TablaDocumentos.getTableHeader();
        header.setFont(headerFont);
        header.setOpaque(false);
        header.setBackground(verdeEncabezado);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        // ====== ANCHOS SUGERIDOS (según tus columnas: ID, Tipo, Documento, Categoría, Creado/Ubicación, Estado) ======
        try {
            TablaDocumentos.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
            TablaDocumentos.getColumnModel().getColumn(1).setPreferredWidth(60);   // Tipo
            TablaDocumentos.getColumnModel().getColumn(2).setPreferredWidth(280);  // Documento
            TablaDocumentos.getColumnModel().getColumn(3).setPreferredWidth(110);  // Categoría
            TablaDocumentos.getColumnModel().getColumn(4).setPreferredWidth(170);  // Creado / Ubicación
            TablaDocumentos.getColumnModel().getColumn(5).setPreferredWidth(80);   // Estado
        } catch (Exception e) {
            // por si cambia el número de columnas en algún momento
        }

        // ====== RENDERER FILAS ZEBRA + ALINEACIONES ======
        javax.swing.table.DefaultTableCellRenderer renderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                java.awt.Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Colores de fondo y texto
                if (isSelected) {
                    c.setBackground(verdeSeleccion);
                    c.setForeground(new Color(20, 61, 36));
                } else {
                    if (row % 2 == 0) {
                        c.setBackground(verdeZebra); // fila par
                    } else {
                        c.setBackground(Color.WHITE); // fila impar
                    }
                    c.setForeground(new Color(40, 40, 40));
                }

                // Fuente general
                c.setFont(new java.awt.Font("Inter", java.awt.Font.PLAIN, 13));

                // Alineaciones por columna
                if (column == 0) { // ID
                    setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    setFont(getFont().deriveFont(java.awt.Font.BOLD));
                } else if (column == 1) { // Tipo (PDF, DOCX, etc.)
                    setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                } else if (column == 5) { // Estado (tipo_acceso)
                    setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    setFont(getFont().deriveFont(java.awt.Font.BOLD));
                } else {
                    // Documento, Categoría, Creado / Ubicación
                    setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                }

                return c;
            }
        };

        for (int i = 0; i < TablaDocumentos.getColumnModel().getColumnCount(); i++) {
            TablaDocumentos.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }
    
    
    private void estilizarTablaCategorias() {
        TablaCategorias.setRowHeight(24);

        TablaCategorias.setShowHorizontalLines(true);
        TablaCategorias.setShowVerticalLines(false);
        TablaCategorias.setIntercellSpacing(new java.awt.Dimension(0, 1));
        TablaCategorias.setGridColor(new Color(220, 220, 220));

        // Encabezado gris elegante
        java.awt.Font headerFont = new java.awt.Font("Inter", java.awt.Font.BOLD, 12);
        TablaCategorias.getTableHeader().setFont(headerFont);
        TablaCategorias.getTableHeader().setOpaque(true);
        TablaCategorias.getTableHeader().setBackground(new Color(100, 100, 100)); // gris oscuro
        TablaCategorias.getTableHeader().setForeground(Color.WHITE);
        TablaCategorias.getTableHeader().setReorderingAllowed(false);

        TablaCategorias.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                java.awt.Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(new Color(210, 220, 240)); // azul/gris suave
                    c.setForeground(Color.BLACK);
                } else {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(245, 245, 245)); // gris claro
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });
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
        BtnDashboardPDF = new javax.swing.JButton();
        BarraVertical01 = new javax.swing.JPanel();
        BarraVertical02 = new javax.swing.JPanel();
        JScrollDocumentos = new javax.swing.JScrollPane();
        TablaDocumentos = new javax.swing.JTable();
        TituloGrande = new javax.swing.JLabel();
        DescripcionMain = new javax.swing.JLabel();
        PanelOpcionesDocumentos = new javax.swing.JPanel();
        JTextBusquedad = new javax.swing.JTextField();
        JButtonBuscar = new javax.swing.JButton();
        JButtomEditar = new javax.swing.JButton();
        JButtonEliminar = new javax.swing.JButton();
        JButtonVer = new javax.swing.JButton();
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
        TItuloDependencia = new javax.swing.JLabel();
        JScrollDependencias = new javax.swing.JScrollPane();
        TablaCategorias = new javax.swing.JTable();
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
        JLabelCrearCategoria = new javax.swing.JLabel();
        JPanelAjustesCategorias = new javax.swing.JPanel();
        TextAjustesCategorias = new javax.swing.JLabel();

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

        BtnDashboardPDF.setBackground(new java.awt.Color(255, 251, 248));
        BtnDashboardPDF.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        BtnDashboardPDF.setForeground(new java.awt.Color(0, 0, 0));
        BtnDashboardPDF.setText("Generar Informe");

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
                .addGap(18, 18, 18)
                .addComponent(BtnDashboardPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 322, Short.MAX_VALUE)
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
                .addGroup(BarraSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(BarraSuperiorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(BtnDashboardPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, BarraSuperiorLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(BarraSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(JLabelRolCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
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

        TablaDocumentos.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        TablaDocumentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TablaDocumentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaDocumentosMouseClicked(evt);
            }
        });
        JScrollDocumentos.setViewportView(TablaDocumentos);

        Backgraund.add(JScrollDocumentos, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 260, 740, 430));

        TituloGrande.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        TituloGrande.setForeground(new java.awt.Color(13, 6, 45));
        TituloGrande.setText("Gestor de Documentos");
        Backgraund.add(TituloGrande, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, 440, 30));

        DescripcionMain.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        DescripcionMain.setText("Administre los documentos de la Secretaria General");
        Backgraund.add(DescripcionMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 130, 490, -1));

        PanelOpcionesDocumentos.setBackground(new java.awt.Color(245, 245, 245));

        JTextBusquedad.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        JTextBusquedad.setText("Buscar nombre del documento");
        JTextBusquedad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                JTextBusquedadFocusLost(evt);
            }
        });
        JTextBusquedad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                JTextBusquedadMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                JTextBusquedadMousePressed(evt);
            }
        });
        JTextBusquedad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTextBusquedadActionPerformed(evt);
            }
        });

        JButtonBuscar.setBackground(new java.awt.Color(204, 204, 255));
        JButtonBuscar.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        JButtonBuscar.setForeground(new java.awt.Color(0, 0, 153));
        JButtonBuscar.setText("Buscar");
        JButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButtonBuscarActionPerformed(evt);
            }
        });

        JButtomEditar.setBackground(new java.awt.Color(255, 255, 204));
        JButtomEditar.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        JButtomEditar.setForeground(new java.awt.Color(102, 51, 0));
        JButtomEditar.setText("Editar");
        JButtomEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JButtomEditarMouseClicked(evt);
            }
        });
        JButtomEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButtomEditarActionPerformed(evt);
            }
        });

        JButtonEliminar.setBackground(new java.awt.Color(255, 204, 204));
        JButtonEliminar.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        JButtonEliminar.setForeground(new java.awt.Color(204, 0, 51));
        JButtonEliminar.setText("Eliminar");
        JButtonEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JButtonEliminarMouseClicked(evt);
            }
        });
        JButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButtonEliminarActionPerformed(evt);
            }
        });

        JButtonVer.setBackground(new java.awt.Color(204, 255, 204));
        JButtonVer.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        JButtonVer.setForeground(new java.awt.Color(0, 51, 51));
        JButtonVer.setText("Ver");
        JButtonVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButtonVerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelOpcionesDocumentosLayout = new javax.swing.GroupLayout(PanelOpcionesDocumentos);
        PanelOpcionesDocumentos.setLayout(PanelOpcionesDocumentosLayout);
        PanelOpcionesDocumentosLayout.setHorizontalGroup(
            PanelOpcionesDocumentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelOpcionesDocumentosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(JTextBusquedad, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(JButtonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                .addComponent(JButtomEditar)
                .addGap(20, 20, 20)
                .addComponent(JButtonEliminar)
                .addGap(20, 20, 20)
                .addComponent(JButtonVer)
                .addGap(20, 20, 20))
        );
        PanelOpcionesDocumentosLayout.setVerticalGroup(
            PanelOpcionesDocumentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelOpcionesDocumentosLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(PanelOpcionesDocumentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelOpcionesDocumentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JTextBusquedad, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JButtonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(JButtonVer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(JButtonEliminar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JButtomEditar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );

        Backgraund.add(PanelOpcionesDocumentos, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 180, 740, 70));

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
        jLabel7.setText("DOCUMENTOS");

        jLabel8.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("TOTALES");

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
        jLabel10.setText("DOCUMENTOS");

        jLabel9.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("MIS");

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
            .addGap(0, 2, Short.MAX_VALUE)
        );

        Backgraund.add(LineaHorizontal01, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 160, 740, 2));

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

        TItuloDependencia.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        TItuloDependencia.setText("POR CATEGORIAS");
        Backgraund.add(TItuloDependencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, -1));

        TablaCategorias.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        TablaCategorias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TablaCategorias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaCategoriasMouseClicked(evt);
            }
        });
        JScrollDependencias.setViewportView(TablaCategorias);

        Backgraund.add(JScrollDependencias, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 200, 280));

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
        TextDocumentos.setText("Panel Principal");
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

        JLabelCrearCategoria.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        JLabelCrearCategoria.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLabelCrearCategoria.setText("+");
        JLabelCrearCategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLabelCrearCategoriaMouseClicked(evt);
            }
        });
        Backgraund.add(JLabelCrearCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 30, 50));

        JPanelAjustesCategorias.setBackground(new java.awt.Color(211, 211, 211));
        JPanelAjustesCategorias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JPanelAjustesCategoriasMouseClicked(evt);
            }
        });

        TextAjustesCategorias.setBackground(new java.awt.Color(0, 0, 0));
        TextAjustesCategorias.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        TextAjustesCategorias.setForeground(new java.awt.Color(0, 0, 0));
        TextAjustesCategorias.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TextAjustesCategorias.setText("Ajustes Categorias");

        javax.swing.GroupLayout JPanelAjustesCategoriasLayout = new javax.swing.GroupLayout(JPanelAjustesCategorias);
        JPanelAjustesCategorias.setLayout(JPanelAjustesCategoriasLayout);
        JPanelAjustesCategoriasLayout.setHorizontalGroup(
            JPanelAjustesCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TextAjustesCategorias, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
        );
        JPanelAjustesCategoriasLayout.setVerticalGroup(
            JPanelAjustesCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TextAjustesCategorias, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        Backgraund.add(JPanelAjustesCategorias, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 640, 195, 40));

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

    private void JButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButtonBuscarActionPerformed
        String textoBusqueda = JTextBusquedad.getText().trim();

        if (textoBusqueda.equals(PLACEHOLDER_BUSQUEDA)) {
            textoBusqueda = "";  
        }

        this.dispose();

        Gestor_De_Documentos ventanaGestor = new Gestor_De_Documentos(textoBusqueda, categoriaSelect);
        ventanaGestor.setLocationRelativeTo(null);
        ventanaGestor.setVisible(true);
    }//GEN-LAST:event_JButtonBuscarActionPerformed

    private void JButtomEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButtomEditarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JButtomEditarActionPerformed

    private void JButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButtonEliminarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JButtonEliminarActionPerformed

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
        if (usuario.getRol().tieneAccesoUsuario()){

            this.dispose();

            Gesto_De_Usuarios ventanaGestorDeUsuario = new Gesto_De_Usuarios("");
            ventanaGestorDeUsuario.setLocationRelativeTo(null);
            ventanaGestorDeUsuario.setVisible(true);

         } else {
            JOptionPane.showMessageDialog(this, "No tienes acceso para los usuarios.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_JPanelUsuariosMouseClicked

    private void JPanelMisDocumentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JPanelMisDocumentosMouseClicked
        // Cerrar login
        this.dispose();

        // Abrir la ventana de gestor de documentos
        Gestor_De_Documentos ventanaGestorDeDocumentos = new Gestor_De_Documentos("", 0L);
        ventanaGestorDeDocumentos.setLocationRelativeTo(null);
        ventanaGestorDeDocumentos.setVisible(true);
    }//GEN-LAST:event_JPanelMisDocumentosMouseClicked

    private void JButtonEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JButtonEliminarMouseClicked

    }//GEN-LAST:event_JButtonEliminarMouseClicked

    private void TablaDocumentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaDocumentosMouseClicked
        int row = TablaDocumentos.getSelectedRow();
        if (row != -1) {
            idSeleccionado = String.valueOf(TablaDocumentos.getValueAt(row, 0));
        } else {
            idSeleccionado = null;
        }
    }//GEN-LAST:event_TablaDocumentosMouseClicked

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

    private void JButtomEditarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JButtomEditarMouseClicked
        
    }//GEN-LAST:event_JButtomEditarMouseClicked

    private void JTextBusquedadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTextBusquedadActionPerformed

    }//GEN-LAST:event_JTextBusquedadActionPerformed

    private void JTextBusquedadMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTextBusquedadMousePressed
        if (JTextBusquedad.getText().equals(PLACEHOLDER_BUSQUEDA)) {
            JTextBusquedad.setText("");              // Limpiar el texto base
            JTextBusquedad.setForeground(Color.BLACK);  // Cambiar el color del texto a negro
        }
    }//GEN-LAST:event_JTextBusquedadMousePressed

    private void JTextBusquedadMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTextBusquedadMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_JTextBusquedadMouseExited

    private void JTextBusquedadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_JTextBusquedadFocusLost
        if (JTextBusquedad.getText().trim().isEmpty()) {
            JTextBusquedad.setText(PLACEHOLDER_BUSQUEDA); // Colocar el texto base
            JTextBusquedad.setForeground(Color.GRAY);      // Cambiar el color a gris
        }
    }//GEN-LAST:event_JTextBusquedadFocusLost

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

    private void JButtonVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButtonVerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JButtonVerActionPerformed

    private void TablaCategoriasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaCategoriasMouseClicked
        int row = TablaCategorias.getSelectedRow();
        if (row != -1) {
            idSeleccionadoCategoria = String.valueOf(TablaCategorias.getValueAt(row, 0));
            
            this.dispose();
            Gestor_De_Documentos gestorDocumentos = new Gestor_De_Documentos("", Long.valueOf(idSeleccionadoCategoria));
            gestorDocumentos.setLocationRelativeTo(null);
            gestorDocumentos.setVisible(true);
            
        } else {
            idSeleccionadoCategoria = null;
        }
    }//GEN-LAST:event_TablaCategoriasMouseClicked

    private void JLabelCrearCategoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLabelCrearCategoriaMouseClicked
        
        if (this.usuario.getRol().tieneAccesoCategoria()) {
            this.dispose();
            Nueva_Categoria nuevaCategoria = new Nueva_Categoria();
            nuevaCategoria.setLocationRelativeTo(null);
            nuevaCategoria.setVisible(true);
        }
        
    }//GEN-LAST:event_JLabelCrearCategoriaMouseClicked

    private void JPanelAjustesCategoriasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JPanelAjustesCategoriasMouseClicked
        
        if (this.usuario.getRol().tieneAccesoCategoria()) {
            this.dispose();
            Eliminar_Categoria ventanaCategoriaAjustes = new Eliminar_Categoria();
            ventanaCategoriaAjustes.setLocationRelativeTo(null);
            ventanaCategoriaAjustes.setVisible(true);
        }
        
    }//GEN-LAST:event_JPanelAjustesCategoriasMouseClicked

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
        java.awt.EventQueue.invokeLater(() -> new Gestor_De_Documentos("", 0L).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Backgraund;
    private javax.swing.JPanel BarraSuperior;
    private javax.swing.JPanel BarraVertical01;
    private javax.swing.JPanel BarraVertical02;
    private javax.swing.JButton BtnDashboardPDF;
    private javax.swing.JLabel DescripcionMain;
    private javax.swing.JLabel Icono;
    private javax.swing.JLabel ImgAjustes;
    private javax.swing.JLabel ImgCargar;
    private javax.swing.JLabel ImgMisDocumentos;
    private javax.swing.JLabel ImgUsuarios;
    private javax.swing.JButton JButtomEditar;
    private javax.swing.JButton JButtonBuscar;
    private javax.swing.JButton JButtonEliminar;
    private javax.swing.JButton JButtonVer;
    private javax.swing.JLabel JLabelCrearCategoria;
    private javax.swing.JLabel JLabelNombreUsuario;
    private javax.swing.JLabel JLabelRolCargo;
    private javax.swing.JPanel JPanelAjustes;
    private javax.swing.JPanel JPanelAjustesCategorias;
    private javax.swing.JPanel JPanelCargarDocumento;
    private javax.swing.JPanel JPanelMisDocumentos;
    private javax.swing.JPanel JPanelUsuarios;
    private javax.swing.JScrollPane JScrollDependencias;
    private javax.swing.JScrollPane JScrollDocumentos;
    private javax.swing.JTextField JTextBusquedad;
    private javax.swing.JPanel LineaHorizontal01;
    private javax.swing.JPanel LineaHorizontal02;
    private javax.swing.JPanel PanelEstadisticas;
    private javax.swing.JPanel PanelOpcionesDocumentos;
    private javax.swing.JLabel TItuloDependencia;
    private javax.swing.JTable TablaCategorias;
    private javax.swing.JTable TablaDocumentos;
    private javax.swing.JLabel TextAjustes;
    private javax.swing.JLabel TextAjustesCategorias;
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
    // End of variables declaration//GEN-END:variables
}
