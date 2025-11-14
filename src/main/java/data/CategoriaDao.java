package data;

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
        String sql = "SELECT nombre FROM categorias ORDER BY nombre";
        List<String> out = new ArrayList<>();
        try (Connection cn = new Db().establecerConexion();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT nombre FROM categorias ORDER BY nombre")) {
            while (rs.next()) out.add(rs.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException("Listando categorías: " + e.getMessage(), e);
        }
        
        System.out.println("Lista Categorias = " + out);
        
        return out;
    }
    
    // ======= PASA DE NOMBRE DE CATEGORIA A ID PARA GUARDARLO EN LA BASE DE DATOS ======= //
    
    public long idPorNombre(String nombre) throws java.sql.SQLException {
        String sql = "SELECT id_categoria FROM categorias WHERE nombre = ?";
        try (java.sql.Connection cn = new data.Db().establecerConexion();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        throw new java.sql.SQLException("Categoría no encontrada: " + nombre);
    }
    
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


    
}