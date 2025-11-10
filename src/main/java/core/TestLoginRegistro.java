/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;
import java.util.Scanner;
/**
 *
 * @author adria
 */

/*
Esta clase es para realizar pruebas del login y registro. Puede ser eliminada una vez se incorpore completamente con la UI y el resto del programa.
*/

public class TestLoginRegistro {
        public static void main(String[] args) {
        RegistroManager manager = new RegistroManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("========== TestApp - Registro/Login ==========");
        while (true) {
            System.out.println("\nSeleccione opción:");
            System.out.println("1) Registrar usuario");
            System.out.println("2) Iniciar sesión");
            System.out.println("3) Mostrar usuarios cargados (solo para test)");
            System.out.println("0) Salir");
            System.out.print("Opción: ");
            String opt = scanner.nextLine().trim();

            if ("0".equals(opt)) {
                System.out.println("Saliendo...");
                break;
            } else if ("1".equals(opt)) {
                // Pedir datos
                UsuarioPrueba usuario1 = new UsuarioPrueba();
                System.out.print("Nombre: "); usuario1.setNombre(scanner.nextLine());
                System.out.print("Apellido: "); usuario1.setApellido(scanner.nextLine());
                System.out.print("Email: "); usuario1.setEmail(scanner.nextLine());
                System.out.print("Telefono (sin +57, ej 3101234567 o con espacios): "); usuario1.setTelefono(scanner.nextLine());
                System.out.print("Departamento: "); usuario1.setDepartamento(scanner.nextLine());
                System.out.print("Municipio: "); usuario1.setMunicipio(scanner.nextLine());
                System.out.print("Dependencia: "); usuario1.setDependencia(scanner.nextLine());
                System.out.print("Cargo: "); usuario1.setCargo(scanner.nextLine());
                System.out.print("Contraseña: "); String password = scanner.nextLine();
                System.out.print("Confirmar contraseña: "); String password2 = scanner.nextLine();

                var res = manager.registrarUsuario(usuario1, password, password2);
                for (String m : res.getMessages()) System.out.println(m);

            } else if ("2".equals(opt)) {
                System.out.print("Ingrese nombre, apellido, nombre+apellido o email: ");
                String id = scanner.nextLine();
                System.out.print("Contraseña: ");
                String password = scanner.nextLine();

                var res = manager.iniciarSesion(id, password);
                for (String m : res.getMessages()) System.out.println(m);

            } else if ("3".equals(opt)) {
                System.out.println("Usuarios en memoria:");
                int i = 1;
                for (UsuarioPrueba u : manager.getUsuarios()) {
                    System.out.println(i++ + ") " + u);
                }
            } else {
                System.out.println("Opción no válida.");
            }
        }

        scanner.close();
    }
}
