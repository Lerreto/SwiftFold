package logica;

import java.sql.*;
import java.util.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CategoriaDao {
    
    private static final String[] COLS = {
       "ID", "Nombre"
    };

    // ======= ENLISTAMIENTO DE TODAS LAS CATEGORIAS ======= //
    
    public List<String> listarNombres() {

        // "Sin categoría" va primero, luego el resto en orden alfabético
        String sql = 
            "SELECT nombre " +
            "FROM categorias " +
            "ORDER BY " +
            "  CASE WHEN LOWER(nombre) = 'sin categoría' THEN 0 ELSE 1 END, " +
            "  nombre ASC";

        List<String> out = new ArrayList<>();
        try (Connection cn = new Db().establecerConexion();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                out.add(rs.getString(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Listando categorías: " + e.getMessage(), e);
        }

        System.out.println("Lista Categorias = " + out);
        return out;
    }

    
    // ======= PASA DE NOMBRE DE CATEGORIA A ID PARA GUARDARLO EN LA BASE DE DATOS ======= //
    
    public long idPorNombre(String nombre) throws java.sql.SQLException {
        String sql = "SELECT id_categoria FROM categorias WHERE nombre = ?";
        try (java.sql.Connection cn = new logica.Db().establecerConexion();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        throw new java.sql.SQLException("Categoría no encontrada: " + nombre);
    }
    
    
    
    // ======= PASA DE ID DE CATEGORIA A NOMBRE PARA GUARDARLO EN LA BASE DE DATOS ======= //
   
    public String nombrePorId(long id) throws SQLException {
        final String sql = "SELECT nombre FROM categorias WHERE id_categoria = ?";
        try (Connection cn = new Db().establecerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("nombre") : null;
            }
        }
    }
    
    
    // ======= LISTAR LAS CATEGORIAS PARA PERMITIR TABULAR ======= //
    
    public void listarCategorias(JTable tabla) {      

        DefaultTableModel m = new DefaultTableModel(null, COLS) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        String sql = "SELECT id_categoria, nombre FROM categorias ORDER BY nombre ASC";

        try (Connection cn = new Db().establecerConexion();
             PreparedStatement st = (cn != null ? cn.prepareStatement(sql) : null)) {

            if (st == null) {
                tabla.setModel(m);
                return;
            }

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    long id   = rs.getLong("id_categoria");
                    String nombre = rs.getString("nombre");

                    m.addRow(new Object[]{ id, nombre });
                }
            }

        } catch (SQLException e) {
            System.err.println("[CategoriaDao] " + e.getMessage());
        }

        tabla.setModel(m);
    }
    
    
    // ======= CREAR UNA NUEVA CATEGORIA ======= //
    
    public boolean crearCategoria(String nombre, String descripcion) {

        // Primero verificar si el nombre ya existe (case-insensitive)
        final String sqlVerificar = "SELECT COUNT(*) FROM categorias WHERE LOWER(nombre) = LOWER(?)";

        try (Connection cn = new Db().establecerConexion();
             PreparedStatement psCheck = cn.prepareStatement(sqlVerificar)) {

            psCheck.setString(1, nombre.trim());
            ResultSet rs = psCheck.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.err.println("La categoría '" + nombre + "' ya existe.");
                return false;  // NO insertamos
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar categoría: " + e.getMessage());
            return false;
        }

        // Insertar ahora SÍ existe
        final String sqlInsert = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";

        try (Connection cn = new Db().establecerConexion();
             PreparedStatement psInsert = cn.prepareStatement(sqlInsert)) {

            psInsert.setString(1, nombre.trim());
            psInsert.setString(2, descripcion == null ? null : descripcion.trim());

            psInsert.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al crear categoría: " + e.getMessage());
            return false;
        }
    }

    
    
    // ======= ELIMINAR UNA CATEGORIA POR NOMBRE ======= //

    public boolean eliminarCategoria(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("Nombre de categoría vacío.");
            return false;
        }

        // No permitir eliminar la categoría por defecto
        if (nombre.trim().equalsIgnoreCase("Sin categoría")) {
            System.out.println("No se puede eliminar la categoría 'Sin categoría'.");
            return false;
        }

        try (Connection cn = new Db().establecerConexion()) {

            cn.setAutoCommit(false); // iniciamos transacción

            // 1. Buscar id_categoria de la categoría a eliminar
            long idAEliminar = -1;
            String sqlBuscar = "SELECT id_categoria FROM categorias WHERE LOWER(nombre) = LOWER(?)";

            try (PreparedStatement psBuscar = cn.prepareStatement(sqlBuscar)) {
                psBuscar.setString(1, nombre.trim());
                try (ResultSet rs = psBuscar.executeQuery()) {
                    if (rs.next()) {
                        idAEliminar = rs.getLong("id_categoria");
                    } else {
                        System.out.println("No se encontró la categoría: " + nombre);
                        cn.rollback();
                        return false;
                    }
                }
            }

            // 2. Obtener id de "Sin categoría" (asegúrate de que exista en la tabla)
            long idDefecto;
            String sqlDefecto = "SELECT id_categoria FROM categorias WHERE LOWER(nombre) = LOWER('sin categoría')";
            try (PreparedStatement psDef = cn.prepareStatement(sqlDefecto);
                 ResultSet rsDef = psDef.executeQuery()) {

                if (rsDef.next()) {
                    idDefecto = rsDef.getLong("id_categoria");
                } else {
                    cn.rollback();
                    throw new SQLException("No existe la categoría 'Sin categoría' en la tabla categorias.");
                }
            }

            // 3. Reasignar documentos: todos los que tienen id_categoria = idAEliminar pasan a idDefecto
            String sqlUpdateDocs = "UPDATE documentos SET id_categoria = ? WHERE id_categoria = ?";
            try (PreparedStatement psUpd = cn.prepareStatement(sqlUpdateDocs)) {
                psUpd.setLong(1, idDefecto);
                psUpd.setLong(2, idAEliminar);
                psUpd.executeUpdate();  // puede devolver 0 si ningún documento usaba esa categoría
            }

            // 4. Eliminar la categoría
            String sqlDelete = "DELETE FROM categorias WHERE id_categoria = ?";
            int filasBorradas;
            try (PreparedStatement psDel = cn.prepareStatement(sqlDelete)) {
                psDel.setLong(1, idAEliminar);
                filasBorradas = psDel.executeUpdate();
            }

            cn.commit();
            return filasBorradas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar categoría reasignando: " + e.getMessage());
            return false;
        }
    }
    
    
    
    // ======= MODIFICAR DESCRIPCION DE UNA CATEGORIA POR NOMBRE ======= //

    public boolean modificarCategoria(String nombre, String nuevaDescripcion) {
        final String sql = "UPDATE categorias SET descripcion = ? WHERE LOWER(nombre) = LOWER(?)";

        try (Connection cn = new Db().establecerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, (nuevaDescripcion == null ? null : nuevaDescripcion.trim()));
            ps.setString(2, nombre.trim());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Categoría modificada: " + nombre);
                return true;
            } else {
                System.out.println("No se encontró la categoría a modificar: " + nombre);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error al modificar categoría: " + e.getMessage());
            return false;
        }
    }
    
    
    
    // ======= OBTENER DESCRIPCION DE UNA CATEGORIA POR NOMBRE ======= //

    public String descripcionPorNombre(String nombre) {
        final String sql = "SELECT descripcion FROM categorias WHERE LOWER(nombre) = LOWER(?)";

        try (Connection cn = new Db().establecerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombre.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("descripcion");
                } else {
                    System.out.println("No se encontró la categoría: " + nombre);
                    return null;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener descripción de categoría: " + e.getMessage());
            return null;
        }
    }

    
    public long obtenerIdCategoriaPorDefecto() throws SQLException {
        return idPorNombre("Sin categoría");
    }
    
}