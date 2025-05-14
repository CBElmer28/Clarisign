/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.controlador;

import clarisign.DB.*;
import clarisign.vista.*;
import java.sql.Connection;
import javax.swing.*;

public class LoginController {
    public void autenticar(String correo, String contrasena) {
        try (Connection conn = DBConnection.getConnection()) {
            UsuarioDAO dao = new UsuarioDAO(conn);
            UsuarioDAO.UsuarioAutenticado usuario = dao.detectarTipoUsuario(correo, contrasena);

            if (usuario == null) {
                JOptionPane.showMessageDialog(null, "Credenciales inválidas.");
                return;
            }

            switch (usuario.tipo) {
                case "paciente" -> new DashboardPacienteView(usuario.id);
                case "terapeuta" -> new DashboardTerapeutaView(usuario.id);
                case "interprete" -> new DashboardInterpreteView(usuario.id);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar: " + e.getMessage());
        }
    }
}
