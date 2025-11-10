/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;
import com.formdev.flatlaf.FlatLightLaf;
import core.Rol;
import core.RolAdministrador;
import core.RolCiudadano;
import core.RolSecretario;
import core.UsuarioPrueba;
import javax.swing.UIManager;
import ui.*;

/**
 *
 * @author Juan Pablo
 */
public class Main {
    
    public static void main(String[] args) {
        
        String nombre = "Juan";
        String apellido = "Pérez";
        String email = "juan.perez@example.com";
        String telefono = "1234567890";
        String departamento = "Santander";
        String municipio = "Bucaramanga";
        String dependencia = "Tecnología";
        String cargo = "Desarrollador";
        String password = "password123";
        String stringRol = "administrador";  
        Rol rol = new RolSecretario();  
        
        // Crear el usuario con el constructor
        UsuarioPrueba usuario_actual = new UsuarioPrueba(
            nombre, apellido, email, telefono,
            departamento, municipio, dependencia, cargo,
            password, stringRol, rol
        );
        
        SesionSingleton.getInstance().setUsuarioLogueado(usuario_actual);


        // ACTIVAR FLATLAF
        FlatLightLaf.setup();

        // BORDES MÁS SUAVES (puedes ajustar el 6 a 4 o 8)
        UIManager.put("Component.arc", 10);
        UIManager.put("TextComponent.arc", 9);
        UIManager.put("Button.arc", 9);
        UIManager.put("ProgressBar.arc", 9);
        UIManager.put("ScrollBar.thumbArc", 9);

        System.out.println("LAF activo: " + UIManager.getLookAndFeel().getName());

        // MOSTRAR VENTANA DE LOGIN
        var Window = new Login();
        Window.setLocationRelativeTo(null);
        Window.setVisible(true);
        
    }

    
}
