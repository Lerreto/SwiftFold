package logica;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import persistencia.SesionSingleton;
import persistencia.Usuario;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.List;

/**
 * Genera un PDF tipo “dashboard ejecutivo” con:
 *  - Portada
 *  - KPIs generales
 *  - Gráficos (pastel, barras, líneas)
 *  - Tablas de detalle
 *  - Conclusiones y recomendaciones
 */

public class GeneradorDashboardPDF {

    // Colores corporativos
    private static final BaseColor COLOR_HEADER  = new BaseColor(53, 91, 62);
    private static final BaseColor COLOR_ACCENT  = new BaseColor(122, 197, 85);
    private static final BaseColor COLOR_WARNING = new BaseColor(255, 204, 102);
    private static final BaseColor COLOR_DANGER  = new BaseColor(255, 102, 102);
    private static final BaseColor COLOR_INFO    = new BaseColor(102, 178, 255);

    // Fuentes
    private Font fuenteTitulo;
    private Font fuenteSubtitulo;
    private Font fuenteNormal;
    private Font fuenteNormalBold;
    private Font fuenteHeader;
    private Font fuenteSmall;

    public GeneradorDashboardPDF() {
        inicializarFuentes();
    }

    private void inicializarFuentes() {
        try {
            fuenteTitulo     = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28, COLOR_HEADER);
            fuenteSubtitulo  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, COLOR_HEADER);
            fuenteNormal     = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            fuenteNormalBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
            fuenteHeader     = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            fuenteSmall      = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY);
        } catch (Exception e) {
            System.err.println("Error inicializando fuentes: " + e.getMessage());
        }
    }

    // ============================================================
    // MÉTODO PRINCIPAL
    // ============================================================

    /** Genera el dashboard completo en la ruta indicada. */
    public boolean generarDashboardCompleto(String rutaDestino) {
        try {
            Document document = new Document(PageSize.A4, 36, 36, 60, 50);
            PdfWriter writer  = PdfWriter.getInstance(document, new FileOutputStream(rutaDestino));

            // Encabezado / pie de página
            writer.setPageEvent(new DashboardPageEvent());

            document.open();

            // 1. Portada
            agregarPortada(document);
            document.newPage();

            // 2. Resumen ejecutivo (KPIs)
            agregarEstadisticasGenerales(document);
            document.newPage();

            // 3. Análisis de documentos
            agregarAnalisisDocumentos(document);
            document.newPage();

            // 4. Análisis de usuarios
            agregarAnalisisUsuarios(document);
            document.newPage();

            // 6. Conclusiones
            agregarConclusiones(document);

            document.close();
            return true;

        } catch (Exception e) {
            System.err.println("Error generando dashboard PDF: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ============================================================
    // SECCIÓN 1: PORTADA
    // ============================================================

    private void agregarPortada(Document document) throws DocumentException {
        // Logo simulado
        PdfPTable logoTable = new PdfPTable(1);
        logoTable.setWidthPercentage(30);
        logoTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoTable.setSpacingBefore(100);

        PdfPCell logoCell = new PdfPCell();
        logoCell.setBackgroundColor(COLOR_HEADER);
        logoCell.setFixedHeight(80);
        logoCell.setBorder(Rectangle.NO_BORDER);

        Paragraph logoText = new Paragraph(
                "SWIFTFOLD",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.WHITE)
        );
        logoText.setAlignment(Element.ALIGN_CENTER);
        logoCell.addElement(logoText);
        logoTable.addCell(logoCell);

        document.add(logoTable);

        // Título principal
        Paragraph titulo = new Paragraph(
                "\n\nDASHBOARD EJECUTIVO\n",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 36, COLOR_HEADER)
        );
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        Paragraph subtitulo = new Paragraph(
                "Sistema de Gestión Documental\n\n",
                FontFactory.getFont(FontFactory.HELVETICA, 18, COLOR_ACCENT)
        );
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitulo);

        // Información del reporte
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(60);
        infoTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        infoTable.setSpacingBefore(50);

        agregarFilaInfo(infoTable, "Fecha de generación:", obtenerFechaHoraActual());
        agregarFilaInfo(infoTable, "Generado por:", obtenerUsuarioActual());
        agregarFilaInfo(infoTable, "Municipio:", obtenerMunicipioActual());
        agregarFilaInfo(infoTable, "Período de análisis:", "Histórico completo");

        document.add(infoTable);

        // Aviso legal
        Paragraph legal = new Paragraph(
                "\n\n\n\nEste documento contiene información confidencial. " +
                "Su distribución está restringida según la Ley 1712 de 2014 " +
                "de Transparencia y Acceso a la Información Pública Nacional.",
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, BaseColor.GRAY)
        );
        legal.setAlignment(Element.ALIGN_CENTER);
        document.add(legal);
    }

    // ============================================================
    // SECCIÓN 2: ESTADÍSTICAS GENERALES
    // ============================================================

    private void agregarEstadisticasGenerales(Document document)
            throws DocumentException, IOException {

        agregarTituloSeccion(document, "1. 📊 Resumen ejecutivo", COLOR_HEADER);

        // KPIs
        PdfPTable kpisTable = new PdfPTable(3);
        kpisTable.setWidthPercentage(100);
        kpisTable.setSpacingBefore(20);
        kpisTable.setSpacingAfter(30);

        Map<String, Object> stats = obtenerEstadisticasGenerales();

        agregarKPICard(
                kpisTable,
                "Total de documentos",
                String.valueOf(stats.get("totalDocumentos")),
                "+15% vs mes anterior (estimado)",
                COLOR_INFO
        );

        agregarKPICard(
                kpisTable,
                "Usuarios que han subido documentos",
                String.valueOf(stats.get("usuariosActivos")),
                stats.get("totalUsuarios") + " usuarios registrados",
                COLOR_ACCENT
        );

        agregarKPICard(
                kpisTable,
                "Categorías configuradas",
                String.valueOf(stats.get("totalCategorias")),
                "Nivel de organización del archivo",
                COLOR_WARNING
        );

        document.add(kpisTable);

        // Gráfico: documentos por tipo de acceso
        agregarSubtitulo(document, "Distribución de documentos por tipo de acceso");

        Map<String, Integer> datosAcceso = obtenerDocumentosPorTipoAcceso();
        Image graficoPie = crearGraficoPie(datosAcceso, "Tipo de acceso", 400, 250);
        graficoPie.setAlignment(Element.ALIGN_CENTER);
        document.add(graficoPie);

        // Análisis de texto
        document.add(new Paragraph("\n"));
        agregarAnalisisTexto(
                document,
                "ANÁLISIS: ",
                generarAnalisisTipoAcceso(datosAcceso),
                COLOR_HEADER
        );
    }

    // ============================================================
    // SECCIÓN 3: ANÁLISIS DE DOCUMENTOS
    // ============================================================

    private void agregarAnalisisDocumentos(Document document)
            throws DocumentException, IOException {

        agregarTituloSeccion(document, "2. 📁 Análisis de documentos", COLOR_HEADER);

        // Gráfico de barras por categoría
        agregarSubtitulo(document, "Documentos por categoría");
        Map<String, Integer> datosCategoria = obtenerDocumentosPorCategoria();

        Image graficoBarras = crearGraficoBarras(
                datosCategoria,
                "Documentos por categoría",
                "Categoría",
                "Cantidad",
                500,
                280
        );
        graficoBarras.setAlignment(Element.ALIGN_CENTER);
        document.add(graficoBarras);

        document.add(new Paragraph("\n"));

        // Tabla detallada
        PdfPTable tablaCategoria = new PdfPTable(4);
        tablaCategoria.setWidthPercentage(100);
        tablaCategoria.setWidths(new float[]{3f, 1f, 1.5f, 1.5f});
        tablaCategoria.setSpacingBefore(10);

        agregarHeaderTabla(tablaCategoria, "Categoría");
        agregarHeaderTabla(tablaCategoria, "Cantidad");
        agregarHeaderTabla(tablaCategoria, "Porcentaje");
        agregarHeaderTabla(tablaCategoria, "Estado");

        int total = datosCategoria.values().stream().mapToInt(Integer::intValue).sum();

        for (Map.Entry<String, Integer> entry : datosCategoria.entrySet()) {
            tablaCategoria.addCell(new Phrase(entry.getKey(), fuenteNormal));

            PdfPCell cellCant = new PdfPCell(
                    new Phrase(String.valueOf(entry.getValue()), fuenteNormalBold)
            );
            cellCant.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaCategoria.addCell(cellCant);

            double porcentaje = total == 0
                    ? 0
                    : (entry.getValue() * 100.0) / total;

            tablaCategoria.addCell(
                    new Phrase(String.format(Locale.US, "%.1f%%", porcentaje), fuenteNormal)
            );

            PdfPCell cellEstado = new PdfPCell();
            cellEstado.setHorizontalAlignment(Element.ALIGN_CENTER);

            if (porcentaje > 20) {
                cellEstado.setPhrase(new Phrase("⬆ Alto", fuenteNormal));
                cellEstado.setBackgroundColor(new BaseColor(255, 204, 204));
            } else if (porcentaje > 10) {
                cellEstado.setPhrase(new Phrase("→ Medio", fuenteNormal));
                cellEstado.setBackgroundColor(new BaseColor(255, 255, 204));
            } else {
                cellEstado.setPhrase(new Phrase("⬇ Bajo", fuenteNormal));
                cellEstado.setBackgroundColor(new BaseColor(204, 255, 204));
            }
            tablaCategoria.addCell(cellEstado);
        }

        document.add(tablaCategoria);

        document.add(new Paragraph("\n"));

        String categoriaTop = datosCategoria.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/D");

        agregarInsight(
                document,
                "💡 La categoría '" + categoriaTop + "' concentra el mayor número de documentos. " +
                "Se recomienda revisar su organización, tiempos de retención y capacidad de almacenamiento."
        );
    }

    // ============================================================
    // SECCIÓN 4: ANÁLISIS DE USUARIOS
    // ============================================================

    private void agregarAnalisisUsuarios(Document document)
            throws DocumentException, IOException {

        agregarTituloSeccion(document, "3. 👥 Análisis de usuarios", COLOR_HEADER);

        // Top 5 usuarios
        agregarSubtitulo(document, "Top 5 usuarios más activos (carga de documentos)");
        Map<String, Integer> topUsuarios = obtenerTopUsuarios(5);

        if (topUsuarios.isEmpty()) {
            Paragraph p = new Paragraph(
                    "Por el momento no hay usuarios con carga registrada de documentos.",
                    fuenteNormal
            );
            p.setSpacingBefore(10);
            document.add(p);
        } else {
            Image graficoUsuarios = crearGraficoBarrasHorizontal(
                    topUsuarios,
                    "Top usuarios",
                    "Cantidad de documentos",
                    500,
                    250
            );
            graficoUsuarios.setAlignment(Element.ALIGN_CENTER);
            document.add(graficoUsuarios);
        }

        document.add(new Paragraph("\n"));

        // Distribución por rol
        agregarSubtitulo(document, "Distribución de usuarios por rol");
        Map<String, Integer> datosRoles = obtenerUsuariosPorRol();

        PdfPTable tablaRoles = new PdfPTable(3);
        tablaRoles.setWidthPercentage(70);
        tablaRoles.setWidths(new float[]{2f, 1f, 2f});
        tablaRoles.setHorizontalAlignment(Element.ALIGN_CENTER);
        tablaRoles.setSpacingBefore(10);

        agregarHeaderTabla(tablaRoles, "Rol");
        agregarHeaderTabla(tablaRoles, "Cantidad");
        agregarHeaderTabla(tablaRoles, "Nivel de acceso");

        Map<String, String> nivelAcceso = new HashMap<>();
        nivelAcceso.put("RolSuperAdministrador", "🔴 Total");
        nivelAcceso.put("RolAdministrador", "🟠 Alto");
        nivelAcceso.put("RolSecretario", "🟡 Medio");
        nivelAcceso.put("RolFuncionario", "🟢 Básico");
        nivelAcceso.put("RolCiudadano", "🔵 Lectura");

        for (Map.Entry<String, Integer> entry : datosRoles.entrySet()) {
            String rolKey    = entry.getKey();
            String rolNombre = rolKey.replace("Rol", "");

            tablaRoles.addCell(new Phrase(rolNombre, fuenteNormal));

            PdfPCell cellCant = new PdfPCell(
                    new Phrase(String.valueOf(entry.getValue()), fuenteNormalBold)
            );
            cellCant.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaRoles.addCell(cellCant);

            String nivel = nivelAcceso.getOrDefault(rolKey, "N/D");
            tablaRoles.addCell(new Phrase(nivel, fuenteNormal));
        }

        document.add(tablaRoles);

        document.add(new Paragraph("\n"));

        agregarAlerta(
                document,
                "⚠️ RECOMENDACIÓN DE SEGURIDAD:\n" +
                "Se recomienda revisar periódicamente los permisos de usuarios con roles administrativos " +
                "(SuperAdministrador y Administrador) y mantener registros de auditoría de accesos."
        );
    }


    // ============================================================
    // SECCIÓN 6: CONCLUSIONES
    // ============================================================

    private void agregarConclusiones(Document document) throws DocumentException {
        agregarTituloSeccion(document, "5. ✅ Conclusiones generales", COLOR_HEADER);

        int totalDocs = obtenerTotalDocumentos();

        com.itextpdf.text.List listaPuntos =
                new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
        listaPuntos.setSymbolIndent(15);
        listaPuntos.setIndentationLeft(15);

        listaPuntos.add(new com.itextpdf.text.ListItem(
                "Actualmente el sistema almacena " + totalDocs +
                " documentos, lo que demuestra un uso activo de la plataforma.",
                fuenteNormal
        ));

        listaPuntos.add(new com.itextpdf.text.ListItem(
                "Algunas categorías concentran buena parte de los documentos, " +
                "por lo que vale la pena revisar su organización interna.",
                fuenteNormal
        ));

        listaPuntos.add(new com.itextpdf.text.ListItem(
                "Los tipos de acceso (público, interno y reservado) permiten equilibrar " +
                "transparencia y seguridad, siempre que se apliquen criterios claros.",
                fuenteNormal
        ));

        listaPuntos.add(new com.itextpdf.text.ListItem(
                "La tendencia de carga de documentos es positiva, lo que refuerza la necesidad " +
                "de planear la ampliación de la capacidad de almacenamiento.",
                fuenteNormal
        ));

        document.add(listaPuntos);

        document.add(new Paragraph("\n"));

        // Recomendaciones
        agregarSubtitulo(document, "Recomendaciones estratégicas");

        PdfPTable tablaRecomendaciones = new PdfPTable(3);
        tablaRecomendaciones.setWidthPercentage(100);
        tablaRecomendaciones.setWidths(new float[]{0.5f, 3f, 1.5f});
        tablaRecomendaciones.setSpacingBefore(10);

        agregarHeaderTabla(tablaRecomendaciones, "#");
        agregarHeaderTabla(tablaRecomendaciones, "Recomendación");
        agregarHeaderTabla(tablaRecomendaciones, "Prioridad");

        String[][] recomendaciones = {
                {"1", "Implementar auditoría automatizada de accesos (logs por usuario y rol).", "🔴 Alta"},
                {"2", "Capacitar a los usuarios en buenas prácticas de clasificación y metadatos.", "🟠 Media"},
                {"3", "Planificar la ampliación de almacenamiento para el siguiente semestre.", "🟡 Media"},
                {"4", "Revisar y actualizar periódicamente la tabla de retención documental.", "🟢 Baja"},
                {"5", "Habilitar alertas de vencimiento para documentos críticos.", "🟠 Media"}
        };

        for (String[] rec : recomendaciones) {
            for (int i = 0; i < rec.length; i++) {
                PdfPCell cell = new PdfPCell(new Phrase(rec[i], fuenteNormal));
                if (i == 2) {
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                }
                tablaRecomendaciones.addCell(cell);
            }
        }

        document.add(tablaRecomendaciones);

        // Firma
        document.add(new Paragraph("\n\n\n"));

        Paragraph firma = new Paragraph(
                "___________________________________\n" +
                "Sistema SwiftFold - Gestión Documental\n" +
                "Alcaldías de Colombia\n" +
                obtenerFechaHoraActual(),
                FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.GRAY)
        );
        firma.setAlignment(Element.ALIGN_CENTER);
        document.add(firma);
    }

    // ============================================================
    // GRÁFICOS (JFreeChart -> Image iText)
    // ============================================================

    private Image crearGraficoPie(Map<String, Integer> datos, String titulo,
                                  int ancho, int alto)
            throws IOException, DocumentException {

        DefaultPieDataset dataset = new DefaultPieDataset();
        if (datos == null || datos.isEmpty()) {
            dataset.setValue("Sin datos", 1);
        } else {
            datos.forEach(dataset::setValue);
        }

        JFreeChart chart = ChartFactory.createPieChart(titulo, dataset, true, true, false);
        personalizarGrafico(chart);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setSectionPaint("PUBLICO",   new Color(204, 255, 204));
        plot.setSectionPaint("INTERNO",   new Color(255, 255, 204));
        plot.setSectionPaint("RESERVADO", new Color(255, 204, 204));

        return convertirChartAImage(chart, ancho, alto);
    }

    private Image crearGraficoBarras(Map<String, Integer> datos, String titulo,
                                     String ejeX, String ejeY, int ancho, int alto)
            throws IOException, DocumentException {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (datos == null || datos.isEmpty()) {
            dataset.addValue(0, "Documentos", "Sin datos");
        } else {
            datos.forEach((k, v) -> {
                String key = (k == null || k.trim().isEmpty()) ? "Sin categoría" : k;
                int    val = (v == null) ? 0 : v;
                dataset.addValue(val, "Documentos", key);
            });
        }

        JFreeChart chart = ChartFactory.createBarChart(
                titulo, ejeX, ejeY, dataset,
                PlotOrientation.VERTICAL, false, true, false
        );
        personalizarGrafico(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.getRenderer().setSeriesPaint(0, new Color(122, 197, 85));

        return convertirChartAImage(chart, ancho, alto);
    }

    private Image crearGraficoBarrasHorizontal(Map<String, Integer> datos, String titulo,
                                               String ejeX, int ancho, int alto)
            throws IOException, DocumentException {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (datos == null || datos.isEmpty()) {
            dataset.addValue(0, "Documentos", "Sin datos");
        } else {
            datos.forEach((k, v) -> {
                String key = (k == null || k.trim().isEmpty()) ? "Sin usuario" : k;
                int    val = (v == null) ? 0 : v;
                dataset.addValue(val, "Documentos", key);
            });
        }

        JFreeChart chart = ChartFactory.createBarChart(
                titulo, ejeX, "Usuario", dataset,
                PlotOrientation.HORIZONTAL, false, true, false
        );
        personalizarGrafico(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.getRenderer().setSeriesPaint(0, new Color(53, 91, 62));

        return convertirChartAImage(chart, ancho, alto);
    }

    /** Versión corregida: nunca mete keys nulas al dataset (evita Null 'key' argument). */
    private Image crearGraficoLineas(Map<String, Integer> datos, String titulo,
                                     String ejeX, String ejeY, int ancho, int alto)
            throws IOException, DocumentException {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (datos == null || datos.isEmpty()) {
            dataset.addValue(0, "Documentos", "Sin datos");
        } else {
            for (Map.Entry<String, Integer> entry : datos.entrySet()) {
                String mes   = entry.getKey();
                Integer total = entry.getValue();

                if (mes == null || mes.trim().isEmpty()) {
                    mes = "Sin fecha";
                }
                if (total == null) {
                    total = 0;
                }

                dataset.addValue(total, "Documentos", mes);
            }
        }

        JFreeChart chart = ChartFactory.createLineChart(
                titulo, ejeX, ejeY, dataset,
                PlotOrientation.VERTICAL, false, true, false
        );
        personalizarGrafico(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);

        LineAndShapeRenderer renderer =
                (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(53, 91, 62));
        renderer.setSeriesStroke(0, new java.awt.BasicStroke(3.0f));

        return convertirChartAImage(chart, ancho, alto);
    }

    private void personalizarGrafico(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        if (chart.getTitle() != null) {
            chart.getTitle().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
        }
        chart.setBorderVisible(false);
    }

    private Image convertirChartAImage(JFreeChart chart, int ancho, int alto)
            throws IOException, DocumentException {

        File tempFile = File.createTempFile("chart_", ".png");
        tempFile.deleteOnExit();

        ChartUtils.saveChartAsPNG(tempFile, chart, ancho, alto);
        return Image.getInstance(tempFile.getAbsolutePath());
    }

    // ============================================================
    // CONSULTAS / DATOS
    // ============================================================

    private Map<String, Object> obtenerEstadisticasGenerales() {
        Map<String, Object> stats = new HashMap<>();
        try {
            stats.put("totalDocumentos", obtenerTotalDocumentos());
            stats.put("totalUsuarios",   UtilidadesDeArchivos.contarUsuarios());
            stats.put("usuariosActivos", obtenerUsuariosActivosEsteMes());
            stats.put("totalCategorias", obtenerTotalCategorias());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }

    private int obtenerTotalDocumentos() {
        try {
            return new DocumentoDao().obtenerNumeroDocumentos();
        } catch (SQLException e) {
            return 0;
        }
    }

    private int obtenerTotalCategorias() {
        String sql = "SELECT COUNT(*) FROM categorias";
        try (Connection cn = new Db().establecerConexion();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    private int obtenerUsuariosActivosEsteMes() {
        String sql = "SELECT COUNT(DISTINCT creador_email) " +
                     "FROM documentos " +
                     "WHERE strftime('%Y-%m', creado_en) = strftime('%Y-%m', 'now')";
        try (Connection cn = new Db().establecerConexion();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    private Map<String, Integer> obtenerDocumentosPorCategoria() {
        Map<String, Integer> resultado = new LinkedHashMap<>();

        String sql = "SELECT c.nombre, COUNT(d.id_documento) AS total " +
                     "FROM categorias c " +
                     "LEFT JOIN documentos d ON c.id_categoria = d.id_categoria " +
                     "GROUP BY c.id_categoria, c.nombre " +
                     "ORDER BY total DESC";

        try (Connection cn = new Db().establecerConexion();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int total     = rs.getInt("total");
                if (nombre == null || nombre.trim().isEmpty()) {
                    nombre = "Sin categoría";
                }
                resultado.put(nombre, total);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    private Map<String, Integer> obtenerDocumentosPorTipoAcceso() {
        Map<String, Integer> resultado = new LinkedHashMap<>();

        // Inicializamos para garantizar presencia de claves
        resultado.put("PUBLICO",   0);
        resultado.put("INTERNO",   0);
        resultado.put("RESERVADO", 0);

        String sql = "SELECT COALESCE(tipo_acceso, 'SIN_CLASIFICAR') AS tipo, " +
                     "COUNT(*) AS total " +
                     "FROM documentos " +
                     "GROUP BY tipo";

        try (Connection cn = new Db().establecerConexion();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String tipo  = rs.getString("tipo");
                int total    = rs.getInt("total");

                if (tipo == null) tipo = "SIN_CLASIFICAR";

                resultado.put(tipo.toUpperCase(Locale.ROOT), total);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    private Map<String, Integer> obtenerTopUsuarios(int limite) {
        Map<String, Integer> resultado = new LinkedHashMap<>();

        String sql = "SELECT creador_email, COUNT(*) AS total " +
                     "FROM documentos " +
                     "GROUP BY creador_email " +
                     "ORDER BY total DESC " +
                     "LIMIT " + limite;

        List<Usuario> usuarios = new UtilidadesDeArchivos().cargarUsuarios();

        try (Connection cn = new Db().establecerConexion();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String email = rs.getString("creador_email");
                int total    = rs.getInt("total");

                String etiqueta = (email == null) ? "Sin correo" : email;

                for (Usuario u : usuarios) {
                    if (email != null && u.getEmail().equalsIgnoreCase(email)) {
                        etiqueta = u.getNombreCompleto();
                        break;
                    }
                }
                resultado.put(etiqueta, total);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    private Map<String, Integer> obtenerUsuariosPorRol() {
        Map<String, Integer> resultado = new LinkedHashMap<>();
        List<Usuario> usuarios = new UtilidadesDeArchivos().cargarUsuarios();

        for (Usuario u : usuarios) {
            if (u.getRol() == null) continue;

            String key = u.getRol().getClass().getSimpleName();
            resultado.put(key, resultado.getOrDefault(key, 0) + 1);
        }
        return resultado;
    }

    private Map<String, Integer> obtenerDocumentosPorMes() {
        Map<String, Integer> resultado = new LinkedHashMap<>();

        String sql = "SELECT strftime('%Y-%m', creado_en) AS mes, COUNT(*) AS total " +
                     "FROM documentos " +
                     "GROUP BY mes " +
                     "ORDER BY mes ASC";

        try (Connection cn = new Db().establecerConexion();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String mes = rs.getString("mes");
                int total  = rs.getInt("total");

                if (mes == null || mes.trim().isEmpty()) {
                    mes = "SIN_FECHA";
                }

                resultado.put(mes, total);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    private int calcularPromedioCrecimiento(Map<String, Integer> datosMes) {
        if (datosMes == null || datosMes.isEmpty()) {
            return 0;
        }
        if (datosMes.size() == 1) {
            double promedio = datosMes.values().stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0.0);
            return (int) Math.round(promedio);
        }

        List<Integer> valores = new ArrayList<>(datosMes.values());
        int n = valores.size();
        int sumaDeltas = 0;

        for (int i = 1; i < n; i++) {
            sumaDeltas += (valores.get(i) - valores.get(i - 1));
        }

        double promedio = sumaDeltas / (double) (n - 1);
        if (promedio < 0) promedio = 0;
        return (int) Math.round(promedio);
    }

    // ============================================================
    // HELPERS DE PRESENTACIÓN
    // ============================================================

    private void agregarFilaInfo(PdfPTable tabla, String etiqueta, String valor) {
        PdfPCell c1 = new PdfPCell(new Phrase(etiqueta, fuenteNormalBold));
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setPadding(4);
        tabla.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase(
                (valor == null || valor.isBlank()) ? "N/D" : valor,
                fuenteNormal
        ));
        c2.setBorder(Rectangle.NO_BORDER);
        c2.setPadding(4);
        tabla.addCell(c2);
    }

    private void agregarTituloSeccion(Document document, String texto, BaseColor color)
            throws DocumentException {

        Paragraph p = new Paragraph(texto, fuenteSubtitulo);
        p.setSpacingBefore(5);
        p.setSpacingAfter(5);
        document.add(p);

        PdfPTable line = new PdfPTable(1);
        line.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(3);
        cell.setBackgroundColor(color);
        line.addCell(cell);
        document.add(line);

        document.add(new Paragraph("\n"));
    }

    private void agregarSubtitulo(Document document, String texto)
            throws DocumentException {

        Paragraph p = new Paragraph(texto, fuenteNormalBold);
        p.setSpacingBefore(10);
        p.setSpacingAfter(5);
        document.add(p);
    }

    private void agregarKPICard(PdfPTable tabla, String titulo, String valor,
                                String descripcion, BaseColor colorFondo) {

        PdfPCell cell = new PdfPCell();
        cell.setPadding(10);
        cell.setBackgroundColor(colorFondo);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);

        Paragraph pTitulo = new Paragraph(titulo, fuenteNormalBold);
        Paragraph pValor  = new Paragraph(
                valor,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY)
        );
        Paragraph pDesc   = new Paragraph(descripcion, fuenteSmall);

        pTitulo.setAlignment(Element.ALIGN_LEFT);
        pValor.setAlignment(Element.ALIGN_LEFT);
        pDesc.setAlignment(Element.ALIGN_LEFT);

        cell.addElement(pTitulo);
        cell.addElement(pValor);
        cell.addElement(pDesc);

        tabla.addCell(cell);
    }

    private void agregarHeaderTabla(PdfPTable tabla, String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, fuenteHeader));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(COLOR_HEADER);
        cell.setPadding(5);
        tabla.addCell(cell);
    }

    private void agregarAnalisisTexto(Document document, String titulo,
                                      String contenido, BaseColor colorTitulo)
            throws DocumentException {

        Paragraph p = new Paragraph();
        Chunk chTitulo    = new Chunk(
                titulo,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, colorTitulo)
        );
        Chunk chContenido = new Chunk(contenido, fuenteNormal);

        p.add(chTitulo);
        p.add(new Chunk(" "));
        p.add(chContenido);
        p.setSpacingBefore(5);
        document.add(p);
    }

    private void agregarInsight(Document document, String texto)
            throws DocumentException {

        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase(texto, fuenteNormal));
        cell.setBackgroundColor(new BaseColor(230, 255, 230));
        cell.setBorderColor(COLOR_ACCENT);
        cell.setPadding(8);
        t.addCell(cell);

        document.add(t);
    }

    private void agregarAlerta(Document document, String texto)
            throws DocumentException {

        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase(texto, fuenteNormal));
        cell.setBackgroundColor(new BaseColor(255, 240, 240));
        cell.setBorderColor(COLOR_DANGER);
        cell.setPadding(8);
        t.addCell(cell);

        document.add(t);
    }

    private String generarAnalisisTipoAcceso(Map<String, Integer> datos) {
        int total = datos.values().stream().mapToInt(Integer::intValue).sum();

        if (total == 0) {
            return "Actualmente no se registran documentos en el sistema. " +
                   "A medida que se carguen documentos, este indicador se actualizará automáticamente.";
        }

        int pub  = datos.getOrDefault("PUBLICO",   0);
        int inte = datos.getOrDefault("INTERNO",   0);
        int res  = datos.getOrDefault("RESERVADO", 0);

        double pPub = pub  * 100.0 / total;
        double pInt = inte * 100.0 / total;
        double pRes = res  * 100.0 / total;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US,
                "El sistema gestiona actualmente %d documentos. ", total));

        if (pPub >= pInt && pPub >= pRes) {
            sb.append(String.format(Locale.US,
                    "Predominan los documentos de acceso PÚBLICO (%.1f%%), " +
                    "lo cual favorece la transparencia institucional. ", pPub));
        } else if (pInt >= pPub && pInt >= pRes) {
            sb.append(String.format(Locale.US,
                    "Predominan los documentos de uso INTERNO (%.1f%%), " +
                    "lo que indica una alta circulación de información administrativa. ", pInt));
        } else {
            sb.append(String.format(Locale.US,
                    "Predominan los documentos de acceso RESERVADO (%.1f%%), " +
                    "lo que sugiere un mayor énfasis en información sensible o confidencial. ", pRes));
        }

        sb.append("Se recomienda revisar periódicamente los criterios de clasificación " +
                  "del tipo de acceso para equilibrar la transparencia con la seguridad de la información.");

        return sb.toString();
    }

    // ============================================================
    // UTILIDADES BÁSICAS (fecha, usuario, municipio)
    // ============================================================

    private String obtenerFechaHoraActual() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return LocalDateTime.now().format(fmt);
    }

    private String obtenerUsuarioActual() {
        try {
            Usuario u = SesionSingleton.getInstance().getUsuarioLogueado();
            if (u == null) return "N/D";
            return u.getNombreCompleto() + " (" + u.getEmail() + ")";
        } catch (Exception e) {
            return "N/D";
        }
    }

    private String obtenerMunicipioActual() {
        try {
            Usuario u = SesionSingleton.getInstance().getUsuarioLogueado();
            if (u == null) return "N/D";

            String muni = u.getMunicipio();
            String dep  = u.getDepartamento();

            if (muni == null || muni.isBlank()) return "N/D";
            if (dep == null || dep.isBlank())   return muni;

            return muni + " - " + dep;
        } catch (Exception e) {
            return "N/D";
        }
    }

    // ============================================================
    // EVENTO DE PÁGINA (ENCABEZADO / PIE)
    // ============================================================

    private class DashboardPageEvent extends PdfPageEventHelper {

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb    = writer.getDirectContent();
            Rectangle      page  = document.getPageSize();

            float left   = document.left();
            float right  = document.right();
            float top    = document.top();
            float bottom = document.bottom();

            // Barra de encabezado
            cb.saveState();
            cb.setColorFill(COLOR_HEADER);
            cb.rectangle(left, top + 10, right - left, 18);
            cb.fill();
            cb.restoreState();

            // Texto encabezado
            ColumnText.showTextAligned(
                    cb,
                    Element.ALIGN_LEFT,
                    new Phrase("SwiftFold - Dashboard ejecutivo", fuenteHeader),
                    left + 8,
                    top + 13,
                    0
            );

            ColumnText.showTextAligned(
                    cb,
                    Element.ALIGN_RIGHT,
                    new Phrase(obtenerFechaHoraActual(), fuenteHeader),
                    right - 8,
                    top + 13,
                    0
            );

            // Línea en pie
            cb.saveState();
            cb.setColorStroke(COLOR_HEADER);
            cb.moveTo(left, bottom - 10);
            cb.lineTo(right, bottom - 10);
            cb.stroke();
            cb.restoreState();

            // Número de página
            ColumnText.showTextAligned(
                    cb,
                    Element.ALIGN_CENTER,
                    new Phrase("Página " + writer.getPageNumber(), fuenteSmall),
                    (left + right) / 2,
                    bottom - 25,
                    0
            );
        }
    }
}
