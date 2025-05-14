/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.DB;

import clarisign.modelo.Terapeuta;
import java.sql.*;
import java.util.*;

public class TerapeutaDAO {
    private Connection conn;

    public TerapeutaDAO(Connection conn) {
        this.conn = conn;
    }

    public void agregarTerapeuta(Terapeuta t) throws SQLException {
        String sql = "INSERT INTO terapeutas (nombre, correo, contrasena, especialidad) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getNombre());
            stmt.setString(2, t.getCorreo());
            stmt.setString(3, t.getContrasena());
            stmt.setString(4, t.getEspecialidad());
            stmt.executeUpdate();
        }
    }

    public List<Terapeuta> listarTodos() throws SQLException {
        List<Terapeuta> lista = new ArrayList<>();
        String sql = "SELECT * FROM terapeutas";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Terapeuta(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("contrasena"),
                    rs.getString("especialidad")
                ));
            }
        }
        return lista;
    }
    
    public Terapeuta obtenerPorId(int id) throws Exception {
    String sql = "SELECT * FROM terapeutas WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Terapeuta(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("contrasena"),
                    rs.getString("especialidad")
            );
        }
    }
    return null;
}
}