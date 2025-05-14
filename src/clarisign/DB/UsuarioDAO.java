/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.DB;

import java.sql.*;

public class UsuarioDAO {
    private Connection conn;

    public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }

    public int autenticar(String correo, String contrasena, String tabla) throws SQLException {
        String sql = "SELECT * FROM " + tabla + " WHERE correo = ? AND contrasena = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt("id");
            }
        }
        return -1;
    }
    
    public UsuarioAutenticado detectarTipoUsuario(String correo, String contrasena) throws SQLException {
        int id;
        
        id = autenticar(correo, contrasena, "pacientes");
        if (id != -1) return new UsuarioAutenticado("paciente", id);

        id = autenticar(correo, contrasena, "terapeutas");
        if (id != -1) return new UsuarioAutenticado("terapeuta", id);

        id = autenticar(correo, contrasena, "interpretes");
        if (id != -1) return new UsuarioAutenticado("interprete", id);

        return null; // No coincide con ninguna tabla
    }
    
    public static class UsuarioAutenticado {
        public String tipo;
        public int id;

        public UsuarioAutenticado(String tipo, int id) {
            this.tipo = tipo;
            this.id = id;
        }
    }
}