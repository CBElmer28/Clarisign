/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.modelo;

public class ValidarDatos {
    public static boolean esCorreoValido(String correo) {
        return correo != null && correo.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean esContrasenaSegura(String contrasena) {
        return contrasena != null && contrasena.length() >= 6;
    }

    // Otros validadores...
}
