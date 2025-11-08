package data;

import core.Documento;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Juan Pablo
 */
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

    
    
    
    // ACCION DE LEER PARA LA PRIMERA TABLA //
    
    public void listar5Cols(JTable tabla) {
        DefaultTableModel m = new DefaultTableModel(null, COLS) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        try (Connection cn = new Db().establecerConexion();
             Statement st = cn != null ? cn.createStatement() : null;
             ResultSet rs = st != null ? st.executeQuery(SQL) : null) {

            if (rs == null) { tabla.setModel(m); return; }

            while (rs.next()) {
                String idDocumento = rs.getString("id_documento"); // Obtenemos el id_documen
                String tipo   = nz(rs.getString("tipo"));
                String nombre = nz(rs.getString("nombre"));
                String desc   = cut(nz(rs.getString("descripcion")), 90);
                String peso   = human(rs.getLong("file_size_bytes"));
                String creador= nz(rs.getString("creador"));
                String codigo = nz(rs.getString("codigo"));

                String documento = String.format(
                    "%s — %s • %s • %s • %s", nombre, desc, peso, creador, codigo
                );

                String categoria = nz(rs.getString("categoria"));

                String fecha = nz(rs.getString("creado_en"));
                if (fecha.length() > 16) fecha = fecha.substring(0,16); // yyyy-MM-dd HH:mm
                String dep  = nz(rs.getString("departamento"));
                String mun  = nz(rs.getString("municipio"));
                String creadoUbic = String.format("%s — %s, %s", fecha, dep, mun);

                String estado = nz(rs.getString("tipo_acceso"));

                m.addRow(new Object[]{ idDocumento, tipo, documento, categoria, creadoUbic, estado });
            }
        } catch (SQLException e) {
            System.err.println("[DocumentoDao] " + e.getMessage());
        }

        tabla.setModel(m); 
    }

  
    
    
    // ACCION PARA GUARDAR DOCUMENTO //
    
    public long guardarDocumento(Documento d, Path rutaOrigen) throws Exception {
        final String SQL = """
            INSERT INTO documentos
            (nombre, codigo, descripcion,
             id_categoria, tipo_acceso, disposicion_final,
             file_name, file_type, file_size_bytes,
             creador_email, creador_nombre,
             departamento, municipio,
             creado_en, actualizado_en)
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?, datetime('now'), datetime('now'))
            """;

        long id;

        try (Connection cn = new Db().establecerConexion();
             PreparedStatement ps = cn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            int i = 1;
            ps.setString(i++, d.getNombre());
            ps.setString(i++, d.getCodigo());
            ps.setString(i++, d.getDescripcion());
            ps.setLong  (i++, d.getIdCategoria());
            ps.setString(i++, d.getTipoAcceso());
            ps.setString(i++, d.getDisposicionFinal());
            ps.setString(i++, d.getFileName());      
            ps.setString(i++, d.getFileType());
            ps.setLong  (i++, d.getFileSizeBytes());
            ps.setString(i++, d.getCorreo_demo());
            ps.setString(i++, d.getUsuario_Demo());
            ps.setString(i++, d.getDepartamento());
            ps.setString(i++, d.getMunicipio());

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

    
    
    // ACCION DE ACTUALIZAR DOCUMENTO //
    
    public boolean actualizarDocumento(Documento documento) throws SQLException {
        String sql = "UPDATE documentos SET nombre = ?, codigo = ?, descripcion = ?, id_categoria = ?, tipo_acceso = ?, disposicion_final = ?, file_name = ? WHERE id_documento = ?";

        try (Connection cn = new Db().establecerConexion(); 
             PreparedStatement ps = cn.prepareStatement(sql)) {

            // Establecer los parámetros de la consulta con los datos del documento
            ps.setString(1, documento.getNombre());
            ps.setString(2, documento.getCodigo());
            ps.setString(3, documento.getDescripcion());
            ps.setInt(4, documento.getIdCategoria());
            ps.setString(5, documento.getTipoAcceso());
            ps.setString(6, documento.getDisposicionFinal());
            ps.setString(7, documento.getFileName());
            ps.setLong(8, documento.getIdDocumento()); // Aquí es donde usamos el id_documento

            // Ejecutar la actualización
            int filasActualizadas = ps.executeUpdate();

            return filasActualizadas > 0; // Devuelve true si al menos una fila fue actualizada
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar el documento: " + ex.getMessage(), ex);
        }
    }

    
    
    // ACCION DE ELIMINAR DOCUMENTO //
    
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
}
