/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author adria
 */

/**
 * Esta es la clase central que gestiona el registro y login de usuarios.
 *
 * Estructuras:
 *  - LinkedList<UsuarioPrueba> usuarios  -> almacenamiento en memoria, persistencia de los datos.
 *  - HashMap<String, String> credenciales -> clave = usernameNormalized OR emailNormalized, valor = hashContraseña, para efectos del login. El usuario puede hacer login tanto con su nombre de usuario como su correo.
 *
 * CSV en la raíz del proyecto: usuarios.csv
 * Formato CSV:
 *  nombre;apellido;email;telefono;departamento;municipio;dependencia;cargo;hash_contraseña
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

    private final LinkedList<UsuarioPrueba> usuarios;
    private final HashMap<String, String> credenciales;
    private final Path archivoUsuarios; // usuarios.csv

    // patrones de validación
    private static final Pattern NOMBRE_PATTERN = Pattern.compile("^[A-Za-zÀ-ÿ\\s]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern CARGO_PATTERN = Pattern.compile("^[A-Za-zÀ-ÿ\\s]+$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    public RegistroManager() {
        this.usuarios = new LinkedList<>();
        this.credenciales = new HashMap<>();
        this.archivoUsuarios = Paths.get("usuarios.csv"); // ruta relativa en la raíz del proyecto, luego puede ser cambiado si el archivo se almacena en bases de datos o algo.

        // Carga inicial: si el archivo no existe, lo crea; si existe, lee y carga usuarios.
        inicializar();
    }
    /*
    Este metodo,, junto al constructor de la clase, se encargan de que una vez iniciado el programa, se pasen los usuarios almacenados en el archivo al LinkedList y al HashMap para realizar el correcto inicio de sesion y registro.
    */

    private void inicializar() {
        System.out.println("Inicializando RegistroManager...");
        System.out.println("Working dir (user.dir): " + System.getProperty("user.dir"));
        System.out.println("Ruta absoluta archivo usuarios: " + archivoUsuarios.toAbsolutePath());

        if (Files.notExists(archivoUsuarios)) {
            try {
                Files.createFile(archivoUsuarios);
                System.out.println("Archivo usuarios.csv no encontrado. Se ha creado uno nuevo en: " + archivoUsuarios.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error al crear usuarios.csv: " + e.getMessage());
            }
            return;
        }

        // Si existe, se lee y llenan las estructuras.
        try (BufferedReader reader = Files.newBufferedReader(archivoUsuarios, StandardCharsets.UTF_8)) {
            String linea;
            int lineaNum = 0;
            while ((linea = reader.readLine()) != null) {
                lineaNum++;
                linea = linea.trim();
                if (linea.isEmpty()) continue; // salta líneas vacías.

                // Espera exactamente 9 campos separados por ';'
                String[] partes = linea.split(";", -1);
                if (partes.length != 9) {
                    System.err.println("Línea " + lineaNum + " con formato inválido (esperados 9 campos), se omite: " + linea);
                    continue;
                }

                String nombre = partes[0];
                String apellido = partes[1];
                String email = partes[2];
                String telefono = partes[3];
                String departamento = partes[4];
                String municipio = partes[5];
                String dependencia = partes[6];
                String cargo = partes[7];
                String hashContrasena = partes[8];

                // Crear usuario y añadir
                UsuarioPrueba usuario1 = new UsuarioPrueba(nombre, apellido, email, telefono, departamento, municipio, dependencia, cargo);
                usuarios.add(usuario1);

                // Normalizar claves y poblar credenciales
                String usernameKey = normalizeForKey(nombre + " " + apellido);
                String emailKey = normalizeForKey(email);
                // Si ya existe, imprimimos advertencia pero sobrescribimos (último prevalece en memoria)
                credenciales.put(usernameKey, hashContrasena);
                credenciales.put(emailKey, hashContrasena);
            }

            System.out.println("Carga desde CSV completada. Usuarios cargados: " + usuarios.size());
        } catch (IOException e) {
            System.err.println("Error leyendo usuarios.csv: " + e.getMessage());
        }
    }

    /**
     * Este metodo se encarga de hacer el registro de un nuevo usuario.
     * - Valida los campos.
     * - Comprueba que el email y teléfono no se repiten entre usuarios (unicidad).
     * - Hashea la contraseña.
     * - Guarda en el archivo CSV y actualiza las estructuras en memoria.
     *
     * @param nuevo Usuario construido por la UI, para tenerlo de guia cuando se enlace con la UI.
     * @param confirmarContrasena confirmación de la contraseña (texto plano recibido de la UI).
     * @param contrasenaTexto la contraseña (texto plano) ingresada por el usuario.
     * @return ValidationResult con éxito/errores.
     */
    
    public ValidationResult registrarUsuario(UsuarioPrueba nuevo, String contrasenaTexto, String confirmarContrasena) {
        ValidationResult resultado = new ValidationResult();

        // Validaciones de campos
        List<String> errores = validarCampos(nuevo, contrasenaTexto, confirmarContrasena);
        if (!errores.isEmpty()) {
            resultado.addMessages(errores);
            resultado.setSuccess(false);
            return resultado;
        }

        // Se normalizan valores para comparaciones
        String emailNormalized = normalizeForKey(nuevo.getEmail());
        String telefonoClean = cleanPhone(nuevo.getTelefono()); // sin espacios ni símbolos

        // Comprobación de unicidad email y telefono
        if (emailExiste(emailNormalized)) {
            resultado.addMessage("El email ya existe en el sistema.");
        }
        if (telefonoExiste(telefonoClean)) {
            resultado.addMessage("El número de teléfono ya está registrado.");
        }
        if (!resultado.getMessages().isEmpty()) {
            resultado.setSuccess(false);
            return resultado;
        }

        // Si todo esta bien -> hashear contraseña y persistir
        String hashContrasena;
        try {
            hashContrasena = hashPassword(contrasenaTexto);
        } catch (NoSuchAlgorithmException e) {
            resultado.addMessage("Error interno al procesar la contraseña.");
            resultado.setSuccess(false);
            return resultado;
        }

        // Se normaliza y actualiza Usuario con teléfono limpio (para almacenamiento)
        nuevo.setTelefono(telefonoClean);

        // Escribir al CSV (append)
        String lineaCSV = String.join(";",
                escapeField(nuevo.getNombre()),
                escapeField(nuevo.getApellido()),
                escapeField(nuevo.getEmail()),
                escapeField(nuevo.getTelefono()),
                escapeField(nuevo.getDepartamento()),
                escapeField(nuevo.getMunicipio()),
                escapeField(nuevo.getDependencia()),
                escapeField(nuevo.getCargo()),
                hashContrasena
        );

        try (BufferedWriter writer = Files.newBufferedWriter(archivoUsuarios, StandardCharsets.UTF_8,
                StandardOpenOption.APPEND)) {
            writer.write(lineaCSV);
            writer.newLine();
            writer.flush();
            
        } catch (IOException e) {
            resultado.addMessage("Error guardando el usuario en el archivo: " + e.getMessage());
            resultado.setSuccess(false);
            return resultado;
            
        }

        // Para actualizar estructuras en memoria
        usuarios.add(nuevo);
        String usernameKey = normalizeForKey(nuevo.getNombre() + " " + nuevo.getApellido());
        credenciales.put(usernameKey, hashContrasena);
        credenciales.put(normalizeForKey(nuevo.getEmail()), hashContrasena);

        resultado.addMessage("Registro exitoso.");
        resultado.setSuccess(true);
        return resultado;
    }

    /**
     * Este metodo se encarga del inicio de sesión.
     * Permite ingreso por nombre, apellido, nombre+apellido o email.
     * Búsqueda case-insensitive (normalizada, no sensible a mayusculas/minusculas).
     */
    
    public ValidationResult iniciarSesion(String identificador, String contrasenaTexto) {
        ValidationResult resultado = new ValidationResult();
        if (identificador == null || identificador.trim().isEmpty()) {
            resultado.addMessage("Ingrese nombre, apellido, nombre+apellido o email.");
            resultado.setSuccess(false);
            return resultado;
        }
        if (contrasenaTexto == null || contrasenaTexto.isEmpty()) {
            resultado.addMessage("Ingrese la contraseña.");
            resultado.setSuccess(false);
            return resultado;
        }

        String idNorm = normalizeForKey(identificador);

        // 1. Verifica si coincide exactamente con una clave en credenciales (email o nombre+apellido)
        if (credenciales.containsKey(idNorm)) {
            String hashGuardado = credenciales.get(idNorm);
            try {
                if (hashGuardado.equals(hashPassword(contrasenaTexto))) {
                    resultado.addMessage("Inicio de sesión correcto.");
                    resultado.setSuccess(true);
                } else {
                    resultado.addMessage("Contraseña incorrecta.");
                    resultado.setSuccess(false);
                }
                return resultado;
            } catch (NoSuchAlgorithmException e) {
                resultado.addMessage("Error interno al verificar la contraseña.");
                resultado.setSuccess(false);
                return resultado;
            }
        }

        // 2. Si no está en credenciales, buscar coincidencias por nombre solo o apellido solo.
        List<UsuarioPrueba> coincidencias = new ArrayList<>();
        for (UsuarioPrueba usuario : usuarios) {
            String nombreNorm = normalizeForKey(usuario.getNombre());
            String apellidoNorm = normalizeForKey(usuario.getApellido());
            String nombreCompletoNorm = normalizeForKey(usuario.getNombre() + " " + usuario.getApellido());

            if (idNorm.equals(nombreNorm) || idNorm.equals(apellidoNorm) || idNorm.equals(nombreCompletoNorm)) {
                coincidencias.add(usuario);
            }
        }

        if (coincidencias.isEmpty()) {
            resultado.addMessage("Usuario no encontrado.");
            resultado.setSuccess(false);
            return resultado;
        } else if (coincidencias.size() > 1) {
            // Ambigüedad: varios usuarios con ese nombre/apellido -> pedir usar email o nombre+apellido
            resultado.addMessage("Identificador ambiguo: existen varios usuarios con ese nombre/apellido. Por favor inicie sesión con email o con nombre y apellido completos.");
            resultado.setSuccess(false);
            return resultado;
        } else {
            // Encontramos exactamente 1 usuario -> verificar contraseña
            UsuarioPrueba encontrado = coincidencias.get(0);
            String usernameKey = normalizeForKey(encontrado.getNombre() + " " + encontrado.getApellido());
            String hashGuardado = credenciales.get(usernameKey);
            if (hashGuardado == null) {
                resultado.addMessage("Error interno: credenciales no encontradas para el usuario.");
                resultado.setSuccess(false);
                return resultado;
            }
            try {
                if (hashGuardado.equals(hashPassword(contrasenaTexto))) {
                    resultado.addMessage("Inicio de sesión correcto.");
                    resultado.setSuccess(true);
                } else {
                    resultado.addMessage("Contraseña incorrecta.");
                    resultado.setSuccess(false);
                }
                return resultado;
            } catch (NoSuchAlgorithmException e) {
                resultado.addMessage("Error interno al verificar la contraseña.");
                resultado.setSuccess(false);
                return resultado;
            }
        }
    }

    /* ======================
       Métodos de ayuda para la validación y demas funciones
       ====================== */
    
    /* 
    Esta parte de validacion de campos luego se enlazara con la UI, en especial en los campos de departamento/municipio/dependencia, que son listas que se despliegan.
    */

    private List<String> validarCampos(UsuarioPrueba usuario, String contrasena, String confirmarContrasena) {
        List<String> errores = new ArrayList<>();

        // Nombre y apellido
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            errores.add("El nombre no puede estar vacío.");
        } else {
            if (!NOMBRE_PATTERN.matcher(usuario.getNombre().trim()).matches()) {
                errores.add("El nombre solo puede contener letras y espacios (se permiten tildes).");
            }
        }

        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            errores.add("El apellido no puede estar vacío.");
        } else {
            if (!NOMBRE_PATTERN.matcher(usuario.getApellido().trim()).matches()) {
                errores.add("El apellido solo puede contener letras y espacios (se permiten tildes).");
            }
        }

        // Email
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            errores.add("El email no puede estar vacío.");
        } else {
            Matcher match = EMAIL_PATTERN.matcher(usuario.getEmail().trim());
            if (!match.matches()) {
                errores.add("El email tiene un formato inválido.");
            }
        }

        // Telefono
        if (usuario.getTelefono() == null || usuario.getTelefono().trim().isEmpty()) {
            errores.add("El teléfono no puede estar vacío.");
        } else {
            String clean = cleanPhone(usuario.getTelefono());
            if (clean.length() != 10) {
                errores.add("El teléfono debe contener 10 dígitos (sin el prefijo +57).");
            } else if (!clean.startsWith("3")) {
                errores.add("El teléfono debe ser un número móvil colombiano que inicie por '3'.");
            } else {
                // OK
            }
        }

        // Departamento / municipio / dependencia
        if (usuario.getDepartamento() == null || usuario.getDepartamento().trim().isEmpty()) {
            errores.add("Debe seleccionar un departamento.");
        }
        if (usuario.getMunicipio() == null || usuario.getMunicipio().trim().isEmpty()) {
            errores.add("Debe seleccionar un municipio.");
        }
        if (usuario.getDependencia() == null || usuario.getDependencia().trim().isEmpty()) {
            errores.add("Debe seleccionar una dependencia.");
        }

        // Cargo: no vacío y solo letras y espacios
        if (usuario.getCargo() == null || usuario.getCargo().trim().isEmpty()) {
            errores.add("El cargo no puede estar vacío.");
        } else {
            if (!CARGO_PATTERN.matcher(usuario.getCargo().trim()).matches()) {
                errores.add("El cargo solo puede contener letras y espacios.");
            }
        }

        // Contraseña
        if (contrasena == null || contrasena.isEmpty()) {
            errores.add("La contraseña no puede estar vacía.");
        } else {
            Matcher pm = PASSWORD_PATTERN.matcher(contrasena);
            if (!pm.matches()) {
                errores.add("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.");
            }
            if (!contrasena.equals(confirmarContrasena)) {
                errores.add("La confirmación de la contraseña no coincide.");
            }
        }

        return errores;
    }

    /**
     * Este metodo se encarga de normalizar cadenas para usarlas como claves/para comparación:
     *  - trim
     *  - lowercase
     *  - elimina tildes para que no afecten comparaciones.
     */
    
    private static String normalizeForKey(String s) {
        if (s == null) return "";
        String trimmed = s.trim().toLowerCase(Locale.ROOT);
        String normalized = Normalizer.normalize(trimmed, Normalizer.Form.NFD);
        // elimina tildes
        return normalized.replaceAll("\\p{M}", "");
    }

    /**
     * Este metodo elimina todo excepto dígitos del teléfono para normalizar.
     */
    
    private static String cleanPhone(String phone) {
        if (phone == null) return "";
        return phone.replaceAll("\\D", "");
    }
    
    private boolean emailExiste(String emailNormalized) {
        return credenciales.containsKey(emailNormalized);
    }

    private boolean telefonoExiste(String telefonoClean) {
        for (UsuarioPrueba u : usuarios) {
            String t = cleanPhone(u.getTelefono());
            if (t.equals(telefonoClean)) return true;
        }
        return false;
    }

    private static String escapeField(String field) {
        if (field == null) return "";
        // Evita que un ';' dentro de un campo rompa el CSV.
        // Se reemplaza ';' por ',' para simplificar.
        return field.replace(";", ",");
    }

    /**
     * Este metodo hashea la contraseña con SHA-256 y devuelve el hex string.
     * @param password texto plano
     * @return hex del hash SHA-256
     * @throws NoSuchAlgorithmException si no se encuentra SHA-256 (no debería ocurrir).
     */
    
    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashed);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    /* Getters para acceder a estructuras internas (útiles para tests o UI) */
    public LinkedList<UsuarioPrueba> getUsuarios() { return usuarios; }
    public HashMap<String, String> getCredenciales() { return credenciales; }
    public Path getArchivoUsuarios() { return archivoUsuarios; }
    
}