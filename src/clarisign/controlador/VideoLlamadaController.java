/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.controlador;

import clarisign.vista.VideoLlamadaSimuladaView;

public class VideoLlamadaController {

    private VideoLlamadaSimuladaView vista;

    public VideoLlamadaController(VideoLlamadaSimuladaView vista) {
        this.vista = vista;
    }

    
    public void conectar() {
        getVista().mostrarMensaje("[Sistema] Conectado a la videollamada.");
    }

    public void finalizar() {
        getVista().mostrarMensaje("[Sistema] Llamada finalizada.");
    }

    public void enviarMensaje() {
        String mensaje = getVista().getMensaje().trim();
        if (!mensaje.isEmpty()) {
            getVista().mostrarMensaje("[Tú] " + mensaje);
            getVista().limpiarMensaje();
        }
    }

    /**
     * @return the vista
     */
    public VideoLlamadaSimuladaView getVista() {
        return vista;
    }

    /**
     * @param vista the vista to set
     */
    public void setVista(VideoLlamadaSimuladaView vista) {
        this.vista = vista;
    }
}
