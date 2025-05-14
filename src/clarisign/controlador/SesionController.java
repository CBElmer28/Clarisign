/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.controlador;

import clarisign.DB.DBConnection;
import clarisign.DB.SesionDAO;
import clarisign.modelo.Sesion;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author MARCE
 */
public class SesionController {
    public boolean programarSesion(Sesion sesion) {
        try (Connection conn = DBConnection.getConnection()) {
            SesionDAO dao = new SesionDAO(conn);
            dao.programarSesion(sesion);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para listar las sesiones de un paciente (por ID)
    public List<Sesion> obtenerSesionesPorPaciente(int idPaciente) {
        try (Connection conn = DBConnection.getConnection()) {
            SesionDAO dao = new SesionDAO(conn);
            return dao.listarSesionesPorPaciente(idPaciente);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para actualizar el estado de una sesión
    public boolean actualizarEstadoSesion(int idSesion, String nuevoEstado) {
        try (Connection conn = DBConnection.getConnection()) {
            SesionDAO dao = new SesionDAO(conn);
            dao.actualizarEstado(idSesion, nuevoEstado);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean asignarDetallesSesion(Sesion sesion) {
    try (Connection conn = DBConnection.getConnection()) {
        SesionDAO dao = new SesionDAO(conn);
        dao.actualizarDetallesSesion(sesion);
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
    }
    public boolean actualizarEstadoSesion(Sesion sesion) {
    try (Connection conn = DBConnection.getConnection()) {
        SesionDAO dao = new SesionDAO(conn);
        dao.actualizarEstado(sesion.getId(), sesion.getEstado());
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    
}
