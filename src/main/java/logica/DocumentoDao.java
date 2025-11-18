package logica;

import logica.Db;
import persistencia.SesionSingleton;
import persistencia.Documento;
import persistencia.Usuario;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


// Encargada de la parte de la bases de datos con Documentos

public class DocumentoDao {
    
    
    // PARTE DE TITULOS DE LA TABLA DE DOCUMENTOS // 
    
    private static final String[] COLS = {
       "ID", "Tipo", "Documento", "Categoría", "Creado / Ubicación", "Estado"
    };

    private static final String SQL =
        "SELECT d.id_documento, d.file_type AS tipo, d.nombre, d.descripcion, d.file_size_bytes, " +
        "       COALESCE(d.creador_nombre, d.creador_email) AS creador, d.codigo, " +
        "       COALESCE(c.nombre,'(sin categoría)') AS categoria, " +
        "       d.creado_en, d.departamento, d.municipio, d.tipo_acceso " +
        "FROM documentos d " +
        "LEFT JOIN categorias c ON c.id_categoria = d.id_categoria " +
        "ORDER BY d.creado_en DESC";

    
    
    // ======= TABULACION EN 5 COLUMNAS DE LA TABLA DOCUMENTOS (INCLUYENDO PARAMETROS BUSQUEDAD) ======= //
        
    public void listar5Cols(JTable tabla, String nombreFiltro, Long idCategoriaFiltro) {

        Usuario usuario = SesionSingleton.getInstance().getUsuarioLogueado();

        DefaultTableModel m = new DefaultTableModel(null, COLS) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        String sql =
            "SELECT d.id_documento, d.file_type AS tipo, d.nombre, d.descripcion, d.file_size_bytes, " +
            "       COALESCE(d.creador_nombre, d.creador_email) AS creador, d.codigo, " +
            "       COALESCE(c.nombre,'(sin categoría)') AS categoria, " +
            "       d.creado_en, d.departamento, d.municipio, d.tipo_acceso, d.id_categoria " +
            "FROM documentos d " +
            "LEFT JOIN categorias c ON c.id_categoria = d.id_categoria " +
            "WHERE 1=1 ";

        // -----------------------------
        // FILTRO POR NOMBRE (OPCIONAL)
        // -----------------------------
        
        if (nombreFiltro != null && !nombreFiltro.trim().isEmpty()) {
            sql += "AND LOWER(d.nombre) LIKE LOWER(?) ";
        }

        // ---------------------------------------------
        // FILTRO POR CATEGORÍA:
        // 0 → mostrar todas
        // >0 → filtrar
        // ---------------------------------------------
        
        if (idCategoriaFiltro != null && idCategoriaFiltro > 0) {
            sql += "AND d.id_categoria = ? ";
        }

        sql += "ORDER BY d.creado_en DESC";

        try (Connection cn = new Db().establecerConexion();
             PreparedStatement st = (cn != null ? cn.prepareStatement(sql) : null)) {

            if (st == null) {
                tabla.setModel(m);
                return;
            }

            int idx = 1;

            // Parametro nombre
            if (nombreFiltro != null && !nombreFiltro.trim().isEmpty()) {
                st.setString(idx++, "%" + nombreFiltro + "%");
            }

            // Parametro categoría (solo si > 0)
            if (idCategoriaFiltro != null && idCategoriaFiltro > 0) {
                st.setLong(idx++, idCategoriaFiltro);
            }

            System.out.println("SQL Ejecutada: " + st.toString());

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {

                    String idDocumento = rs.getString("id_documento");
                    String tipo   = nz(rs.getString("tipo"));
                    String nombre = nz(rs.getString("nombre"));
                    String desc   = cut(nz(rs.getString("descripcion")), 90);
                    String peso   = human(rs.getLong("file_size_bytes"));
                    String creador= nz(rs.getString("creador"));
                    String codigo = nz(rs.getString("codigo"));

                    String documento = String.format(
                            "%s — %s • %s • %s • %s",
                            nombre, desc, peso, creador, codigo
                    );

                    String categoria = nz(rs.getString("categoria"));
                    String fecha = nz(rs.getString("creado_en"));
                    if (fecha.length() > 16) fecha = fecha.substring(0, 16);

                    String dep  = nz(rs.getString("departamento"));
                    String mun  = nz(rs.getString("municipio"));
                    String creadoUbic = String.format("%s — %s, %s", fecha, dep, mun);
                    String estado = nz(rs.getString("tipo_acceso"));

                    Documento docTemp = new Documento();
                    docTemp.setTipoAcceso(estado);
                    docTemp.setMunicipio(mun);

                    if (usuario.getRol().tieneAccesoVer(docTemp, usuario.getMunicipio())) {
                        m.addRow(new Object[]{ idDocumento, tipo, documento, categoria, creadoUbic, estado });
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("[DocumentoDao] " + e.getMessage());
        }

        tabla.setModel(m);
    }
    
    
    
    // ======= ACCION DE GUARDAR DOCUMENTO EN LA BASE DE DATOS ======= //
    
    public long guardarDocumento(Documento d, Path rutaOrigen) throws Exception {
    final String SQL = """
        INSERT INTO documentos
        (nombre, codigo, descripcion,
         id_categoria, tipo_acceso, disposicion_final,
         file_name, file_type, file_size_bytes,
         creador_email, creador_nombre,
         departamento, municipio,
         creado_en, actualizado_en)
        VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?, ?, ?)
        """;

        long id;

        // ====== ASEGURAR CATEGORÍA ======
        long idCategoria = d.getIdCategoria();
        if (idCategoria <= 0) {
            // Si no viene categoría, usar "Sin categoría"
            CategoriaDao catDao = new CategoriaDao();
            try {
                idCategoria = catDao.obtenerIdCategoriaPorDefecto();
            } catch (SQLException e) {
                throw new Exception("No se encontró la categoría por defecto 'Sin categoría'.", e);
            }
        }

        try (Connection cn = new Db().establecerConexion();
             PreparedStatement ps = cn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            int i = 1;
            ps.setString(i++, d.getNombre());
            ps.setString(i++, d.getCodigo());
            ps.setString(i++, d.getDescripcion());
            ps.setLong  (i++, idCategoria);
            ps.setString(i++, d.getTipoAcceso());
            ps.setString(i++, d.getDisposicionFinal());
            ps.setString(i++, d.getFileName());
            ps.setString(i++, d.getFileType());
            ps.setLong  (i++, d.getFileSizeBytes());
            ps.setString(i++, d.getCorreo_demo());
            ps.setString(i++, d.getUsuario_Demo());
            ps.setString(i++, d.getDepartamento());
            ps.setString(i++, d.getMunicipio());

            // Si tus columnas son DATETIME/TIMESTAMP:
            ps.setString(i++, TimeUtils.ahoraBogota()); // creado_en
            ps.setString(i++, TimeUtils.ahoraBogota()); // actualizado_en

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                id = rs.getLong(1);
            }
        }

        // === DUPLICAR ARCHIVO ===
        Path dirDestino = Paths.get(System.getProperty("user.dir"), "src", "main", "java", "archivos");
        Files.createDirectories(dirDestino);

        Path destino = dirDestino.resolve(d.getFileName());
        Files.copy(rutaOrigen, destino, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        return id;
    }


    
    
    // ======= ACCION DE ACTUALIZAR DOCUMENTO EN BASE A LOS CAMBIOS ======= //
    
    public boolean actualizarDocumento(persistencia.Documento documento) throws SQLException {
        String sql = """
            UPDATE documentos
               SET nombre = ?,
                   codigo = ?,
                   descripcion = ?,
                   id_categoria = ?,
                   tipo_acceso = ?,
                   disposicion_final = ?,
                   file_name = ?,
                   actualizado_en = ?
             WHERE id_documento = ?
        """;

        try (Connection cn = new Db().establecerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, documento.getNombre());
            ps.setString(2, documento.getCodigo());
            ps.setString(3, documento.getDescripcion());
            ps.setLong(4, documento.getIdCategoria());
            ps.setString(5, documento.getTipoAcceso());
            ps.setString(6, documento.getDisposicionFinal());
            ps.setString(7, documento.getFileName());

            ps.setString(8, TimeUtils.ahoraBogota()); // creado_en
//            ps.setString(9, documento.getFechaCreacion()); // creado_en

            ps.setLong(9, documento.getIdDocumento());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar el documento: " + ex.getMessage(), ex);
        }
    }

    
    
    // ======= ACCION DE ELIMINAR DOCUMENTO DE LA BASES DE DATOS ======= //
    
    public boolean eliminarDocumento(String idDocumento) throws IOException {
        String consulta = "SELECT file_name FROM documentos WHERE id_documento = ?";
        String fileName = null;

        try (Connection cn = new Db().establecerConexion();
             PreparedStatement ps = cn.prepareStatement(consulta)) {

            ps.setString(1, idDocumento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    fileName = rs.getString("file_name");
                } else {
                    System.err.println("No se encontró el documento con ID: " + idDocumento);
                    return false;  
                }
            }
            
            //  Esto permite eliminar el documento de la carpeta de archivos, del cual fue que duplicamos
            
            if (fileName != null) {
                Path filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "java", "archivos", fileName);

                try {
                    Files.deleteIfExists(filePath);
                    System.out.println("Archivo eliminado correctamente: " + filePath);
                } catch (IOException e) {
                    System.err.println("Error al eliminar el archivo: " + e.getMessage());
                    return false;
                }
            }
            
            
            // Seguidamente eliminamos de la base de datos el documento guardado
            
            String deleteConsulta = "DELETE FROM documentos WHERE id_documento = ?";
            try (PreparedStatement deletePs = cn.prepareStatement(deleteConsulta)) {
                deletePs.setString(1, idDocumento);
                int filasAfectadas = deletePs.executeUpdate();
                return filasAfectadas > 0; 
            }

        } catch (SQLException ex) {
            System.err.println("Error al eliminar documento: " + ex.getMessage());
            return false;
        }
    }

    
    
    // ======= ACCION DE OBTENER EL DOCUMENTO POR MEDIO DEL ID INGRESADO ======= //
    
    public Documento obtenerDocumentoPorId(String idDocumento) throws SQLException {

        String sql = "SELECT id_documento, nombre, codigo, descripcion, id_categoria, tipo_acceso, disposicion_final, file_name, file_type, file_size_bytes, creador_email, creador_nombre, departamento, municipio, creado_en, actualizado_en "
                   + "FROM documentos WHERE id_documento = ?";

        try (Connection cn = new Db().establecerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, idDocumento);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    persistencia.Documento doc = new persistencia.Documento();

                    // Asignar valores del ResultSet al objeto
                    doc.setIdDocumento(rs.getLong("id_documento"));
                    doc.setNombre(rs.getString("nombre"));
                    doc.setCodigo(rs.getString("codigo"));
                    doc.setDescripcion(rs.getString("descripcion"));
                    doc.setIdCategoria(rs.getLong("id_categoria"));
                    doc.setTipoAcceso(rs.getString("tipo_acceso"));
                    doc.setDisposicionFinal(rs.getString("disposicion_final"));
                    doc.setFileName(rs.getString("file_name"));
                    doc.setFileType(rs.getString("file_type"));
                    doc.setFileSizeBytes(rs.getLong("file_size_bytes"));
                    doc.setDepartamento(rs.getString("departamento"));
                    doc.setMunicipio(rs.getString("municipio"));
                    doc.setUsuario_Demo(rs.getString("creador_nombre"));
                    doc.setCorreo_demo(rs.getString("creador_email"));
                    doc.setFechaActualizacion(rs.getString("creado_en"));
                    doc.setFechaCreacion(rs.getString("actualizado_en"));

                    return doc; // Aquí retorna el documento listo
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener documento por ID: " + e.getMessage(), e);
        }

        return null; 
    }
    
    
    
    
    // ======= OBTENER EL NUMERO DE DOCUMENTOS DISPONIBLES ======= //
    
    public int obtenerNumeroDocumentos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM documentos";

        try (Connection cn = new Db().establecerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);  // Retorna el número total de documentos
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener el número de documentos: " + e.getMessage(), e);
        }

        return 0; // En caso de error o no haber documentos
    }

    
    
    // ======= OBTENER EL NUMERO DE DOCUMENTOS SUBIDO POR EL USUARIO ======= //
    
    public int obtenerNumeroDocumentosPorUsuario() throws SQLException {
        String correoUsuario = SesionSingleton.getInstance()
                .getUsuarioLogueado()
                .getEmail();

        // Por si acaso, limpiamos desde Java también
        if (correoUsuario != null) {
            correoUsuario = correoUsuario.trim().toLowerCase();
        }

        String sql = """
            SELECT COUNT(*) 
            FROM documentos 
            WHERE LOWER(TRIM(creador_email)) = ?
        """;

        try (Connection cn = new Db().establecerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, correoUsuario);   // ya va limpio y en minúsculas

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("Documentos del usuario [" + correoUsuario + "] = " + count);
                    return count;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener el número de documentos por usuario: " + e.getMessage(), e);
        }

        return 0;
    }

    
    // --- utilidades cortas ---
    private static String nz(String s) { return s == null ? "" : s; }

    private static String cut(String s, int max) {
        s = s.trim();
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    private static String human(long bytes) {
        if (bytes < 1024) return bytes + " B";
        double kb = bytes / 1024.0;
        if (kb < 1024) return String.format("%.1f KB", kb);
        double mb = kb / 1024.0;
        if (mb < 1024) return String.format("%.1f MB", mb);
        double gb = mb / 1024.0;
        return String.format("%.2f GB", gb);
    }
    
    public class TimeUtils {

        private static final DateTimeFormatter FORMATO =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        public static String ahoraBogota() {
            return LocalDateTime
                    .now(ZoneId.of("America/Bogota"))
                    .format(FORMATO);
        }
    }

}
