/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.controlador;

import clarisign.DB.DBConnection;
import clarisign.DB.SesionDAO;
import clarisign.modelo.Sesion;

import java.sql.Connection;

public class SolicitudSesionController {
    
    public boolean enviarSolicitud(Sesion sesion) {
        try (Connection conn = DBConnection.getConnection()) {
            SesionDAO dao = new SesionDAO(conn);
            dao.crearSolicitudSesion(sesion);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
