/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.DB;

import clarisign.modelo.Sesion;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class SesionDAO {
    private Connection conn;

    public SesionDAO(Connection conn) {
        this.conn = conn;
    }

    public void programarSesion(Sesion s) throws SQLException {
        String sql = "INSERT INTO sesiones (idPaciente, idTerapeuta, idInterprete, fechaHora, estado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, s.getIdPaciente());
            stmt.setInt(2, s.getIdTerapeuta());
            stmt.setInt(3, s.getIdInterprete());
            stmt.setTimestamp(4, Timestamp.valueOf(s.getFechaHora()));
            stmt.setString(5, s.getEstado());
            stmt.executeUpdate();
        }
    }

    public List<Sesion> listarSesionesPorPaciente(int idPaciente) throws SQLException {
        return listarSesiones("idPaciente", idPaciente);
    }

    // Listar sesiones por terapeuta
    public List<Sesion> listarSesionesPorTerapeuta(int idTerapeuta) throws SQLException {
        return listarSesiones("idTerapeuta", idTerapeuta);
    }

    // Listar sesiones por intérprete
    public List<Sesion> listarSesionesPorInterprete(int idInterprete) throws SQLException {
        return listarSesiones("idInterprete", idInterprete);
    }

    // Método general para evitar código repetido
    private List<Sesion> listarSesiones(String campo, int id) throws SQLException {
        List<Sesion> lista = new ArrayList<>();
        String sql = "SELECT * FROM sesiones WHERE " + campo + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("fechaHora");
                LocalDateTime fechaHora = convertirTimestampSeguro(ts);

                Integer idInterprete = rs.getObject("idInterprete") != null ? rs.getInt("idInterprete") : null;

                
                lista.add(new Sesion(
                    rs.getInt("id"),
                    rs.getInt("idPaciente"),
                    rs.getInt("idTerapeuta"),
                    idInterprete,
                    fechaHora,
                    rs.getString("estado")
                ));
            }
        }
        return lista;
    }
    
    public void actualizarEstado(int sesionId, String nuevoEstado) throws SQLException {
        String sql = "UPDATE sesiones SET estado = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, sesionId);
            stmt.executeUpdate();
        }
    }
    
    public void crearSolicitudSesion(Sesion s) throws SQLException {
    String sql = "INSERT INTO sesiones (idPaciente, idTerapeuta, idInterprete, fechaHora, estado) VALUES (?, ?, ?, NULL, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, s.getIdPaciente());
        stmt.setInt(2, s.getIdTerapeuta());

        if (s.getIdInterprete() == null) {
            stmt.setNull(3, java.sql.Types.INTEGER);
        } else {
            stmt.setInt(3, s.getIdInterprete());
        }

        stmt.setString(4, s.getEstado()); // "solicitando"
        stmt.executeUpdate();
    }
}
    public List<Sesion> obtenerSolicitudesPorTerapeuta(int idTerapeuta) throws SQLException {
    List<Sesion> lista = new ArrayList<>();
    String sql = "SELECT * FROM sesiones WHERE idTerapeuta = ? AND estado = 'solicitando'";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idTerapeuta);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new Sesion(
                rs.getInt("id"),
                rs.getInt("idPaciente"),
                rs.getInt("idTerapeuta"),
                rs.getInt("idInterprete"),
                rs.getTimestamp("fechaHora") != null ? rs.getTimestamp("fechaHora").toLocalDateTime() : null,
                rs.getString("estado")
            ));
            }
        }
    return lista;
    }
    
    public void actualizarDetallesSesion(Sesion s) throws SQLException {
    String sql = "UPDATE sesiones SET idInterprete = ?, fechaHora = ?, estado = ? WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, s.getIdInterprete());
        stmt.setTimestamp(2, Timestamp.valueOf(s.getFechaHora()));
        stmt.setString(3, s.getEstado());
        stmt.setInt(4, s.getId());
        stmt.executeUpdate();
        }
    }
    
    public void eliminarSesion(int idSesion) throws SQLException {
    String sql = "DELETE FROM sesiones WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idSesion);
        stmt.executeUpdate();
        }
    }
    
    public List<Sesion> obtenerSolicitudesParaInterprete(int idInterprete) throws SQLException {
    List<Sesion> lista = new ArrayList<>();
    String sql = "SELECT * FROM sesiones WHERE idInterprete = ? AND estado = 'en revision'";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idInterprete);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new Sesion(
                rs.getInt("id"),
                rs.getInt("idPaciente"),
                rs.getInt("idTerapeuta"),
                rs.getInt("idInterprete"),
                rs.getTimestamp("fechaHora") != null ? rs.getTimestamp("fechaHora").toLocalDateTime() : null,
                rs.getString("estado")
            ));
            }
        }
    return lista;
    }
    private LocalDateTime convertirTimestampSeguro(Timestamp ts) {
    return ts != null ? ts.toLocalDateTime() : null;
}
}