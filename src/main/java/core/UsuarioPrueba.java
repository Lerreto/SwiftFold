/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;
import java.util.Objects; 

/**
 *
 * @author adria
 */

/**
 * Clase de usuario de ejemplo, solo la utilizo para pruebas.
 * Cuando se vaya a crear la clase Usuario real, se puede cambiar/adaptar esta clase para su uso.
 **/

public class UsuarioPrueba {
    private String nombre;
    private String apellido;
    private String email;
    private String telefono; // Se guarda como String
    private String departamento;
    private String municipio;
    private String dependencia;
    private String cargo;
    private String contraseña;
    private String stringRol;
    private Rol rol;
    // La contraseña por ahora no se guarda aca en el usuario, se guarda el hash en el almacenamiento persistente.

    public UsuarioPrueba() {} 

    public UsuarioPrueba(String nombre, String apellido, String email, String telefono,
                   String departamento, String municipio, String dependencia, String cargo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.departamento = departamento;
        this.municipio = municipio;
        this.dependencia = dependencia;
        this.cargo = cargo;
    } // Constructores de la clase
    
    
    public UsuarioPrueba(String nombre, String apellido, String email, String telefono,
                   String departamento, String municipio, String dependencia, String cargo, String password, String stringRol, Rol rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.departamento = departamento;
        this.municipio = municipio;
        this.dependencia = dependencia;
        this.cargo = cargo;
        this.contraseña = password;
        this.stringRol = stringRol;
        this.rol = rol;
    } // Constructores de la clase

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }

    public String getDependencia() { return dependencia; }
    public void setDependencia(String dependencia) { this.dependencia = dependencia; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

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
                '}';
    } //Override al metodo toString para imprimir el estado de un objeto.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsuarioPrueba usuario = (UsuarioPrueba) o;
        return Objects.equals(nombre, usuario.nombre) &&
               Objects.equals(apellido, usuario.apellido) &&
               Objects.equals(email, usuario.email) &&
               Objects.equals(telefono, usuario.telefono);
    } 

    @Override
    public int hashCode() {
        return Objects.hash(nombre, apellido, email, telefono);
    } // Metodo para obtener el hashCode de ciertos parametros, se usara para el registro.
}