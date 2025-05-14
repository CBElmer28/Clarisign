/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.DB;

import clarisign.modelo.Interprete;
import java.sql.*;
import java.util.*;

public class InterpreteDAO {
    private Connection conn;

    public InterpreteDAO(Connection conn) {
        this.conn = conn;
    }

    public void agregarInterprete(Interprete i) throws SQLException {
        String sql = "INSERT INTO interpretes (nombre, correo, contrasena, lenguajeSenias) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, i.getNombre());
            stmt.setString(2, i.getCorreo());
            stmt.setString(3, i.getContrasena());
            stmt.setString(4, i.getLenguajeSenias());
            stmt.executeUpdate();
        }
    }

    public List<Interprete> listarTodos() throws SQLException {
        List<Interprete> lista = new ArrayList<>();
        String sql = "SELECT * FROM interpretes";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Interprete(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("contrasena"),
                    rs.getString("lenguajeSenias")
                ));
            }
        }
        return lista;
    }
    
    public Interprete obtenerPorId(int id) throws Exception {
    String sql = "SELECT * FROM interpretes WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Interprete(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("contrasena"),
                    rs.getString("lenguajeSenias")
            );
        }
    }
    return null;
}
}