/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author adria
 */

public class HashUtil {

    /**
     * Esta clase se encarga de generar el hash SHA-256 de la cadena de entrada (para encriptar contraseñas).
     *
     * @param texto Texto plano (por ejemplo, la contraseña)
     * @return Hash hexadecimal correspondiente al texto
     */
    
    public static String generarHash(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(texto.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error: Algoritmo SHA-256 no encontrado.");
            return null;
        }
    }

    /**
     * Compara una contraseña en texto plano con su hash.
     *
     * @param textoPlano Contraseña ingresada por el usuario
     * @param hashExistente Hash almacenado previamente
     * @return true si la contraseña coincide, false en caso contrario
     */
    
    public static boolean verificarHash(String textoPlano, String hashExistente) {
        String hashNuevo = generarHash(textoPlano);
        return hashNuevo != null && hashNuevo.equals(hashExistente);
    }
}