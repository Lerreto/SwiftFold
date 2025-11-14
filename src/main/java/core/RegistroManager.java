package core;
import java.io.*;
import java.util.*;


/**
 * Esta es la clase central que gestiona el registro y login de usuarios.
 *
 * Estructuras:
 *  - LinkedList<Usuario> usuarios  -> almacenamiento en memoria, persistencia de los datos.
 *  - HashMap<String, String> credenciales -> clave = usernameNormalized OR emailNormalized, valor = hashContraseña, para efectos del login. El usuario puede hacer login tanto con su nombre de usuario como su correo.
 *
 * CSV en la raíz del proyecto: usuarios.csv
 * Formato CSV:
 *  nombre;apellido;email;telefono;departamento;municipio;dependencia;cargo;rol;hash_contraseña
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
            case "rolciudadano" -> new RolCiudadano();
            case "roladministrador" -> new RolAdministrador();
            case "rolfuncionario" -> new RolFuncionario();
            case "rolsecretario" -> new RolSecretario();
            case "rolsuperadministrador" -> new RolSuperAdministrador();
            default -> new RolCiudadano();
        };
    }
    
    /*
    Metodo para eliminar los usuarios tanto de las listas, como del archivo.
    */
    
    public ValidationResult eliminarUsuario(String email) {
    ValidationResult resultado = new ValidationResult();

    if (email == null || email.isBlank()) {
        resultado.addMessage("El email no puede estar vacío.");
        return resultado;
    }

    Usuario usuario = mapaUsuariosPorEmail.get(email);
    if (usuario == null) {
        resultado.addMessage("No existe un usuario registrado con ese correo electrónico.");
        return resultado;
    }

    // Eliminar de memoria
    listaUsuarios.remove(usuario);
    mapaUsuariosPorEmail.remove(email);

    // Persistir cambios
    guardarUsuariosEnArchivo();

    resultado.setSuccess(true);
    return resultado;
    }
    
    /*
    Metodo para modificar todos los campos del usuario (excepto la contraseña).
    */
    
    public ValidationResult modificarUsuario(String email, String nuevoDepartamento, String nuevoMunicipio,
                                         String nuevaDependencia, String nuevoRol, String nuevoCargo) {

        ValidationResult resultado = new ValidationResult();

        Usuario usuario = mapaUsuariosPorEmail.get(email);
        if (usuario == null) {
            resultado.addMessage("No existe un usuario con ese correo electrónico.");
            return resultado;
        }

        // Validaciones básicas
        if (nuevoDepartamento == null || nuevoDepartamento.isBlank()) {
            resultado.addMessage("El nombre no puede estar vacío.");
        }
        if (nuevoMunicipio == null || nuevoMunicipio.isBlank()) {
            resultado.addMessage("El apellido no puede estar vacío.");
        }
        if (nuevaDependencia == null || nuevaDependencia.isBlank()) {
            resultado.addMessage("La dependencia no puede estar vacía.");
        }
        if (nuevoCargo == null || nuevoCargo.isBlank()) {
            resultado.addMessage("El cargo no puede estar vacío.");
        }

        // Validación del nuevo rol 
        if (nuevoRol != null && !nuevoRol.isBlank()) {

            // Si el rol actual es diferente, se intenta cambiar
            String rolActual = usuario.getRol().getClass().getSimpleName();

            if (!nuevoRol.equalsIgnoreCase(rolActual)) {
                Rol rolGenerado = crearRolDesdeNombre(nuevoRol);

                if (rolGenerado == null) {
                    resultado.addMessage("El rol indicado no es válido: " + nuevoRol);
                } else {
                    usuario.setRol(rolGenerado);
                }
            }
        }

        if (!resultado.isSuccess()) return resultado;

        // Aplicar cambios
        usuario.setDepartamento(nuevoDepartamento);
        usuario.setMunicipio(nuevoMunicipio);
        usuario.setDependencia(nuevaDependencia);
        usuario.setCargo(nuevoCargo);

        // Persistir cambios
        guardarUsuariosEnArchivo();

        resultado.setSuccess(true);
        return resultado;
    }

    /*
    Metodo para modificar los campos del usuario, incluyendo la contraseña. Se reciben dos parametros, uno de nueva contraseña, y otro de confirmarNuevaContraseña.
    */
    public ValidationResult modificarUsuario(String email, String nuevoNombre, String nuevoApellido,
                                         String nuevoNumero, String nuevoCargo,
                                         String nuevaContrasena, String confirmarNuevaContrasena) {

        ValidationResult resultado = new ValidationResult();

        Usuario usuario = mapaUsuariosPorEmail.get(email);
        if (usuario == null) {
            resultado.addMessage("No existe un usuario con ese correo electrónico.");
            return resultado;
        }

        // Validaciones de campos normales
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            resultado.addMessage("El nombre no puede estar vacío.");
        }
        if (nuevoApellido == null || nuevoApellido.isBlank()) {
            resultado.addMessage("El apellido no puede estar vacío.");
        }
        if (nuevoNumero == null || nuevoNumero.isBlank()) {
            resultado.addMessage("El número de teléfono no puede estar vacío.");
        }
        if (nuevoCargo == null || nuevoCargo.isBlank()) {
            resultado.addMessage("El cargo no puede estar vacío.");
        }

        // ---------------------------------------------------
        // LÓGICA CLAVE: ¿se quiere cambiar la contraseña?
        // ---------------------------------------------------
        boolean quiereCambiarClave =
                (nuevaContrasena != null && !nuevaContrasena.isBlank()) ||
                (confirmarNuevaContrasena != null && !confirmarNuevaContrasena.isBlank());

        if (quiereCambiarClave) {
            // Validaciones de contraseña SOLO si quiere cambiarla
            if (nuevaContrasena == null || nuevaContrasena.isBlank()) {
                resultado.addMessage("La nueva contraseña no puede estar vacía.");
            } else if (!nuevaContrasena.equals(confirmarNuevaContrasena)) {
                resultado.addMessage("La confirmación de la contraseña no coincide.");
            } else if (!Usuario.validarContrasena(nuevaContrasena)) {
                resultado.addMessage("La nueva contraseña no cumple los requisitos mínimos.");
            }
        }

        if (!resultado.isSuccess()) return resultado;

        // Aplicar cambios básicos
        usuario.setNombre(nuevoNombre);
        usuario.setApellido(nuevoApellido);
        usuario.setTelefono(nuevoNumero);
        usuario.setCargo(nuevoCargo);

        // Solo cambiamos el hash si realmente quiere cambiar la clave
        if (quiereCambiarClave) {
            String hash = HashUtil.generarHash(nuevaContrasena);
            usuario.setHashContrasena(hash);
        }

        // Persistir cambios
        guardarUsuariosEnArchivo();

        resultado.setSuccess(true);
        return resultado;
    }

    // Métodos de utilidad
    public LinkedList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public HashMap<String, Usuario> getMapaUsuarios() {
        return mapaUsuariosPorEmail;
    }
    
    
    
}