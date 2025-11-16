package logica;

import persistencia.SesionSingleton;
import persistencia.Rol;
import persistencia.RolAdministrador;
import persistencia.RolCiudadano;
import persistencia.RolFuncionario;
import persistencia.RolSecretario;
import persistencia.RolSuperAdministrador;
import persistencia.Usuario;
import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class UtilidadesDeArchivos {

    private static final String ARCHIVO_USUARIOS = "usuarios.csv";

    /**
     * Guarda una lista de usuarios en el archivo CSV.
     * 
     * @param usuarios lista de usuarios a guardar
     */
    
    public static void guardarUsuarios(List<Usuario> usuarios) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ARCHIVO_USUARIOS), StandardCharsets.UTF_8))) {

            // Encabezado opcional
            writer.newLine();

            for (Usuario u : usuarios) {
                writer.write(String.join(",",
                        escapar(u.getNombre()),
                        escapar(u.getApellido()),
                        escapar(u.getEmail()),
                        escapar(u.getTelefono()),
                        escapar(u.getDepartamento()),
                        escapar(u.getMunicipio()),
                        escapar(u.getDependencia()),
                        escapar(u.getCargo()),
                        escapar(u.getRol().getClass().getSimpleName()),
                        escapar(u.getHashContrasena())
                ));
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Error al guardar usuarios en archivo: " + e.getMessage());
        }
    }

    /**
     * Carga los usuarios desde el archivo CSV.
     * 
     * @return lista de usuarios cargados
     */
    
    public static List<Usuario> cargarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();

        File archivo = new File(ARCHIVO_USUARIOS);
        if (!archivo.exists()) {
            System.out.println("No se encontró el archivo de usuarios, se creará uno nuevo.");
            return usuarios;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARCHIVO_USUARIOS), StandardCharsets.UTF_8))) {

            String linea;
            boolean primera = true;

            while ((linea = reader.readLine()) != null) {
                if (primera) { // omitir encabezado
                    primera = false;
                    continue;
                }

                String[] partes = linea.split(",", -1);
                if (partes.length < 10) continue;

                String nombre = desescapar(partes[0]);
                String apellido = desescapar(partes[1]);
                String email = desescapar(partes[2]);
                String telefono = desescapar(partes[3]);
                String departamento = desescapar(partes[4]);
                String municipio = desescapar(partes[5]);
                String dependencia = desescapar(partes[6]);
                String cargo = desescapar(partes[7]);
                String hashContrasena = desescapar(partes[9]);
                String rolNombre = desescapar(partes[8]);

                Rol rol = crearRolDesdeNombre(rolNombre);

                Usuario usuario = new Usuario(nombre, apellido, email, telefono, departamento,
                        municipio, dependencia, cargo, hashContrasena, rol);

                usuarios.add(usuario);
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo de usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    /**
     * Escapa comas y caracteres especiales al guardar en CSV.
     */
    
    private static String escapar(String texto) {
        if (texto == null) return "";
        if (texto.contains(",") || texto.contains("\"")) {
            texto = texto.replace("\"", "\"\"");
            return "\"" + texto + "\"";
        }
        return texto;
    }

    /**
     * Quita las comillas y desescapa caracteres al leer desde CSV.
     */
    
    private static String desescapar(String texto) {
        if (texto.startsWith("\"") && texto.endsWith("\"")) {
            texto = texto.substring(1, texto.length() - 1).replace("\"\"", "\"");
        }
        return texto;
    }

    /**
     * Crea una instancia de Rol según el nombre guardado en el CSV.
     */
    
    private static Rol crearRolDesdeNombre(String nombreRol) {
        switch (nombreRol.toLowerCase()) {
            case "roladministrador":
                return new RolAdministrador();
            case "rolciudadano":
                return new RolCiudadano();
            case "rolfuncionario":
                return new RolFuncionario();
            case "rolsecretario":
                return new RolSecretario();
            case "rolsuperadministrador":
                return new RolSuperAdministrador();
            default:
                return new RolCiudadano(); // rol por defecto
        }
    }
    
     /**
     * Crea la tabla para la tabla de usuarios.
     */

    
    public void crearTablaUsuarios(JTable jTableUsuarios) {
        List<Usuario> listaUsuarios = cargarUsuarios(); 

        String[] columnas = {"Nombre y Apellido", "Email", "Departamento", "Municipio", "Rol"};

        // Inicializar la lista de datos con un tamaño máximo
        List<Object[]> datosList = new ArrayList<>();

        // Iterar sobre los usuarios y llenar los datos
        for (Usuario usuario : listaUsuarios) {
            // Verificar si el usuario no es el mismo que el usuario logueado
            if (!usuario.getEmail().equals(SesionSingleton.getInstance().getUsuarioLogueado().getEmail())) {
                Object[] datos = new Object[5];
                datos[0] = usuario.getNombre() + " " + usuario.getApellido();
                datos[1] = usuario.getEmail(); 
                datos[2] = usuario.getDepartamento(); 
                datos[3] = usuario.getMunicipio(); 
                datos[4] = usuario.getRol().getClass().getSimpleName(); 
                datosList.add(datos);
            }
        }

        // Convertir la lista a un arreglo de objetos
        Object[][] datos = datosList.toArray(new Object[0][0]);

        // Crear el modelo de la tabla con los datos y las columnas
        DefaultTableModel model = new DefaultTableModel(datos, columnas);

        // Asignar el modelo de la tabla al JTable
        jTableUsuarios.setModel(model);
    }
    
    
     /**
     * Busca usuario por Email.
     */
    
    public static Usuario buscarUsuarioPorEmail(String emailBuscado) {
        if (emailBuscado == null || emailBuscado.isBlank()) {
            return null;
        }

        // Normalizar un poquito el correo
        String normalizado = emailBuscado.trim().toLowerCase();

        // Reutilizamos el método que ya tienes
        List<Usuario> usuarios = cargarUsuarios();

        for (Usuario u : usuarios) {
            if (u.getEmail() != null && u.getEmail().trim().toLowerCase().equals(normalizado)) {
                return u;  // lo encontramos
            }
        }

        // Si no lo encuentra, devuelve null
        return null;
    }
    
    
     /**
     * Cuenta los usuarios existentes
     */
    
    public static int contarUsuarios() {
        List<Usuario> usuarios = cargarUsuarios();
        return usuarios.size();
    }

}