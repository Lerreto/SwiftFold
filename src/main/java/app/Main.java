/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
import ui.*;

/**
 *
 * @author Juan Pablo
 */
public class Main {
    
    public static void main(String[] args) {


        // ACTIVAR FLATLAF
        FlatLightLaf.setup();

        // BORDES MÁS SUAVES (puedes ajustar el 6 a 4 o 8)
        UIManager.put("Component.arc", 9);
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
