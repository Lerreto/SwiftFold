/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;
import java.io.*;
import java.util.*;
import java.nio.file.*;

/**
 *
 * @author adria
 */

/**
 * Esta es la clase central que gestiona el registro y login de usuarios.
 *
 * Estructuras:
 *  - LinkedList<Usuario> usuarios  -> almacenamiento en memoria, persistencia de los datos.
 *  - HashMap<String, String> credenciales -> clave = usernameNormalized OR emailNormalized, valor = hashContraseña, para efectos del login. El usuario puede hacer login tanto con su nombre de usuario como su correo.
 *
 * CSV en la raíz del proyecto: usuarios.csv
 * Formato CSV:
 *  nombre;apellido;email;telefono;departamento;municipio;dependencia;cargo;hash_contraseña;rol
 *
 * Validaciones realizadas:
 *  - nombre/apellido: letras y espacios (se permiten tildes; internalmente se normaliza)
 *  - email: regex básico
 *  - telefono: se normaliza (se eliminan no-dígitos), debe tener 10 dígitos y empezar por '3'
 *  - cargo: no vacío y solo letras/espacios
 *  - contraseña: min 8 caracteres, al menos 1 mayúscula, 1 minúscula y 1 número
 *
 * Para el hash de la contraseña, solo se guarda el hash SHA-256. Todas las expresiones que tienen System.out.println("") y demas son para efectos de pruebas. Pueden ser modificadas luego para encadenarlas con la UI.
 */

public class RegistroManager {

    private LinkedList<Usuario> listaUsuarios;
    private HashMap<String, Usuario> mapaUsuariosPorEmail;
    private File archivoUsuarios;

    // Constructor: carga automáticamente los usuarios desde el CSV
    public RegistroManager(String rutaArchivo) {
        this.listaUsuarios = new LinkedList<>();
        this.mapaUsuariosPorEmail = new HashMap<>();
        this.archivoUsuarios = new File(rutaArchivo);

        if (archivoUsuarios.exists()) {
            cargarUsuariosDesdeArchivo();
        } else {
            try {
                archivoUsuarios.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creando el archivo CSV: " + e.getMessage());
            }
        }
    }

    /**
     * Registra un nuevo usuario en memoria y en el archivo CSV.
     * Realiza las validaciones correspondientes.
     */
    
    public ValidationResult registrarUsuario(Usuario nuevoUsuario, String confirmarContrasena) {
        ValidationResult resultado = new ValidationResult();

        // Validaciones
        if (nuevoUsuario.getNombre().isEmpty() || nuevoUsuario.getApellido().isEmpty()) {
            resultado.addMessage("El nombre y apellido no pueden estar vacíos.");
        }

        if (!Usuario.validarEmail(nuevoUsuario.getEmail())) {
            resultado.addMessage("El formato del correo electrónico es inválido.");
        }

        if (mapaUsuariosPorEmail.containsKey(nuevoUsuario.getEmail())) {
            resultado.addMessage("Ya existe un usuario registrado con ese correo electrónico.");
        }

        if (!Usuario.validarTelefono(nuevoUsuario.getTelefono())) {
            resultado.addMessage("El número de teléfono no tiene un formato válido (ejemplo: 3001234567).");
        }

        if (!Usuario.validarContrasena(confirmarContrasena)) {
            resultado.addMessage("La contraseña no cumple con los requisitos mínimos.");
        }

        // Si hay errores, no continuar
        if (!resultado.isSuccess()) {
            return resultado;
        }

        // Hashear contraseña antes de guardar
        String hash = HashUtil.generarHash(confirmarContrasena);
        nuevoUsuario.setHashContrasena(hash);

        // Asignar rol por defecto (Ciudadano)
        nuevoUsuario.setRol(new RolCiudadano());

        // Agregar a estructuras en memoria
        listaUsuarios.add(nuevoUsuario);
        mapaUsuariosPorEmail.put(nuevoUsuario.getEmail(), nuevoUsuario);

        // Guardar en archivo
        guardarUsuariosEnArchivo();

        resultado.setSuccess(true);
        return resultado;
    }

    /**
     * Permite iniciar sesión verificando email y contraseña.
     */
    
    public ValidationResult iniciarSesion(String email, String contrasena) {
        ValidationResult resultado = new ValidationResult();

        if (!mapaUsuariosPorEmail.containsKey(email)) {
            resultado.addMessage("No existe un usuario con ese correo electrónico.");
            return resultado;
        }

        Usuario usuario = mapaUsuariosPorEmail.get(email);
        String hashIngresado = HashUtil.generarHash(contrasena);

        if (!usuario.getHashContrasena().equals(hashIngresado)) {
            resultado.addMessage("Contraseña incorrecta.");
            return resultado;
        }

        resultado.setSuccess(true);
        return resultado;
    }

    /**
     * Carga los usuarios desde el archivo CSV a memoria (LinkedList + HashMap).
     */
    private void cargarUsuariosDesdeArchivo() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoUsuarios))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(",");

                if (campos.length < 10) continue; // formato incompleto

                Usuario usuario = new Usuario(
                        campos[0], campos[1], campos[2], campos[3],
                        campos[4], campos[5], campos[6], campos[7],
                        campos[9]
                );

                // Asignar rol basado en el nombre leído
                usuario.setRol(crearRolDesdeNombre(campos[8]));

                listaUsuarios.add(usuario);
                mapaUsuariosPorEmail.put(usuario.getEmail(), usuario);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    /**
     * Guarda todos los usuarios actuales en el archivo CSV.
     * Reescribe completamente el archivo.
     */
    
    public void guardarUsuariosEnArchivo() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoUsuarios))) {
            for (Usuario u : listaUsuarios) {
                bw.write(u.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    /**
     * Crea una instancia del rol correcto según su nombre en el archivo CSV.
     */
    
    private Rol crearRolDesdeNombre(String nombreRol) {
        nombreRol = nombreRol.toLowerCase();
        return switch (nombreRol) {
            case "roladministrador" -> new RolAdministrador();
            case "rolfuncionario" -> new RolFuncionario();
            case "rolsecretario" -> new RolSecretario();
            case "rolsuperadministrador" -> new RolSuperAdministrador();
            default -> new RolCiudadano();
        };
    }

    // Métodos de utilidad
    public LinkedList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public HashMap<String, Usuario> getMapaUsuarios() {
        return mapaUsuariosPorEmail;
    }
}