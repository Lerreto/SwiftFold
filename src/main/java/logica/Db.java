package logica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Db {

    // Conexión mantenida en la instancia
    private Connection conexion = null;

    // Archivo en la carpeta del proyecto
    private final String BD  = "BDDocumentos.db";
    private final String URL = "jdbc:sqlite:" + System.getProperty("user.dir") + "/" + BD;

    
    
    // ======= ESTABLECER CONEXION A LA BASE DE DATOS ======= //
    
    public Connection establecerConexion() {
        try {
            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection(URL);

            // Activar llaves foráneas
            try (Statement st = conexion.createStatement()) {
                st.execute("PRAGMA foreign_keys=ON");
            }

            System.out.println("[DB] Conectado a: " + URL);
            return conexion;
        } catch (Exception e) {
            System.err.println("[DB] Error de conexión: " + e.getMessage());
            return null;
        }
    }

    
    // ======= CIERRRA LA CONEXION DE LA BASE DE DATOS ======= //
    
    public void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("[DB] Conexión cerrada.");
            } catch (SQLException e) {
                System.err.println("[DB] Error al cerrar: " + e.getMessage());
            } finally {
                conexion = null;
            }
        }
    }

    
    // ======= EN DADO CASO PARA OBTENER LA URL ======= //
    
    public String getUrl() { return URL; }
    
}