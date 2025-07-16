/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.DB;

import clarisign.modelo.Paciente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class PacienteDAO {
    private Connection conn;

    public PacienteDAO(Connection conn) {
        this.conn = conn;
    }

    public void agregarPaciente(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO pacientes (nombre, correo, contrasena, diagnostico) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paciente.getNombre());
            stmt.setString(2, paciente.getCorreo());
            stmt.setString(3, paciente.getContrasena());
            stmt.setString(4, paciente.getDiagnostico());
            stmt.executeUpdate();
        }
    }

    public Paciente obtenerPorCorreo(String correo) throws SQLException {
        String sql = "SELECT * FROM pacientes WHERE correo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Paciente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("contrasena"),
                    rs.getString("diagnostico")
                );
            }
        }
        return null;
    }

    public List<Paciente> listarTodos() throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientes";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Paciente p = new Paciente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("contrasena"),
                    rs.getString("diagnostico")
                );
                lista.add(p);
            }
        }
        return lista;
    }
    
    public Paciente obtenerPorId(int id) throws Exception {
    String sql = "SELECT * FROM pacientes WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Paciente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("contrasena"),
                    rs.getString("diagnostico")
            );
        }
    }
    return null;
    }
 
    public void migrarContraseñasPlanasABcrypt() throws SQLException {
    String selectSql = "SELECT id, contrasena FROM pacientes";
    String updateSql = "UPDATE pacientes SET contrasena = ? WHERE id = ?";

    try (Statement selectStmt = conn.createStatement();
         ResultSet rs = selectStmt.executeQuery(selectSql);
         PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

        while (rs.next()) {
            int id = rs.getInt("id");
            String contrasenaActual = rs.getString("contrasena");

            if (contrasenaActual == null || contrasenaActual.isEmpty()) continue;

            // Detectar si ya está en formato bcrypt (no volver a hashear)
            if (contrasenaActual.startsWith("$2a$") || contrasenaActual.startsWith("$2b$")) {
                continue;
            }

            // Hashear contraseña con formato $2a$ (JBCrypt usa este por defecto)
            String hash = BCrypt.hashpw(contrasenaActual, BCrypt.gensalt(10));

            // Actualizar en la base
            updateStmt.setString(1, hash);
            updateStmt.setInt(2, id);
            updateStmt.executeUpdate();
        }
    }
}

    // Puedes agregar actualizarPaciente() y eliminarPaciente() si lo necesitas
}