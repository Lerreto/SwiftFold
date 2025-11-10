/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 *
 * @author adria
 */

/**
 * Clase de usuario de ejemplo, solo la utilizo para pruebas.
 * Cuando se vaya a crear la clase Usuario real, se puede cambiar/adaptar esta clase para su uso.
 **/

public class Usuario {
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String departamento;
    private String municipio;
    private String dependencia;
    private String cargo;
    private Rol rol;  // Se asigna por defecto a RolCiudadano
    private String hashContrasena;

    // Constructor completo
    public Usuario(String nombre, String apellido, String email, String telefono,
                   String departamento, String municipio, String dependencia,
                   String cargo, String hashContrasena, Rol rol) {

        this.nombre = normalizarTexto(nombre);
        this.apellido = normalizarTexto(apellido);
        this.email = email.toLowerCase();
        this.telefono = telefono.replaceAll("\\s+", "");
        this.departamento = normalizarTexto(departamento);
        this.municipio = normalizarTexto(municipio);
        this.dependencia = normalizarTexto(dependencia);
        this.cargo = normalizarTexto(cargo);
        this.hashContrasena = hashContrasena;
        this.rol = (rol != null) ? rol : new RolCiudadano();
    }
    
     // Constructor utilizado para crear nuevos usuarios desde el registro
    public Usuario(String nombre, String apellido, String email, String telefono,
                   String departamento, String municipio, String dependencia,
                   String cargo, String hashContrasena) {
        this(nombre, apellido, email, telefono, departamento, municipio, dependencia,
             cargo, hashContrasena, new RolCiudadano());
    }
    
    // Constructor vacío (para carga desde archivo o modificación)
    public Usuario() {
        this.rol = new RolCiudadano();
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = normalizarTexto(nombre); }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = normalizarTexto(apellido); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email.toLowerCase(); }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono.replaceAll("\\s+", ""); }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }

    public String getDependencia() { return dependencia; }
    public void setDependencia(String dependencia) { this.dependencia = dependencia; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = normalizarTexto(cargo); }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public String getHashContrasena() { return hashContrasena; }
    public void setHashContrasena(String hashContrasena) { this.hashContrasena = hashContrasena; }
    
    public String getStringRol() { return rol.getClass().getSimpleName(); }

    // Métodos auxiliares
    private String normalizarTexto(String texto) {
        if (texto == null) return "";
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD);
        sinTildes = sinTildes.replaceAll("\\p{M}", "");
        return sinTildes.trim();
    }

    // Validación de correo electrónico
    public static boolean validarEmail(String email) {
        String patron = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.matches(patron, email);
    }

    // Validación de número telefónico
    public static boolean validarTelefono(String telefono) {
        String limpio = telefono.replaceAll("\\s+", "");
        return limpio.matches("^3\\d{9}$");
    }

    // Validación de contraseña
    public static boolean validarContrasena(String contrasena) {
        return contrasena.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
    }

    // Nombre de usuario (para login)
    public String obtenerNombreUsuario() {
        return (nombre + " " + apellido).trim().toLowerCase();
    }

    // Conversión a línea CSV (para persistencia)
    public String toCSV() {
        return String.join(",",
                nombre,
                apellido,
                email,
                telefono,
                departamento,
                municipio,
                dependencia,
                cargo,
                rol.getClass().getSimpleName(),
                hashContrasena);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", departamento='" + departamento + '\'' +
                ", municipio='" + municipio + '\'' +
                ", dependencia='" + dependencia + '\'' +
                ", cargo='" + cargo + '\'' +
                ", rol=" + rol.getClass().getSimpleName() +
                '}';
    }
}