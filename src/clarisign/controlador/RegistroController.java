/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.controlador;

import clarisign.DB.DBConnection;
import clarisign.DB.*;
import clarisign.modelo.Paciente;
import java.sql.Connection;

/**
 *
 * @author MARCE
 */
public class RegistroController {
        
    public boolean registrarPaciente(Paciente paciente) {
        try (Connection conn = DBConnection.getConnection()) {
            PacienteDAO dao = new PacienteDAO(conn);
            dao.agregarPaciente(paciente);
            return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
}
