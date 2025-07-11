/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package clarisign.vista;

import clarisign.controlador.VideoLlamadaController;
import javax.swing.*;
import java.awt.*;

public class VideoLlamadaSimuladaView extends JFrame {

    private JButton btnFinalizar;
    private JTextArea areaSimulacion;
    private JTextField txtMensaje;
    private JButton btnEnviar;
    private JButton btnSalir;

    private VideoLlamadaController controller;

    public VideoLlamadaSimuladaView() {
        setTitle("Videollamada Simulada");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        controller = new VideoLlamadaController(this);

        // Panel de cámaras simuladas
        JPanel panelCamaras = new JPanel(new GridLayout(1, 2, 10, 10));
        panelCamaras.setBorder(BorderFactory.createTitledBorder("Cámaras"));

        JLabel camaraTerapeuta = crearPanelCamara("Cámara del Terapeuta");
        JLabel camaraInterprete = crearPanelCamara("Cámara del Intérprete");

        panelCamaras.add(camaraTerapeuta);
        panelCamaras.add(camaraInterprete);

        add(panelCamaras, BorderLayout.NORTH);

        // Área de chat
        areaSimulacion = new JTextArea(10, 30);
        areaSimulacion.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaSimulacion);

        // Panel inferior con chat y botones
        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));

        JPanel panelChat = new JPanel(new BorderLayout());
        txtMensaje = new JTextField();
        btnEnviar = new JButton("Enviar");
        panelChat.add(txtMensaje, BorderLayout.CENTER);
        panelChat.add(btnEnviar, BorderLayout.EAST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnFinalizar = new JButton("Finalizar");
        btnSalir = new JButton("Salir al Dashboard");
        panelBotones.add(btnFinalizar);
        panelBotones.add(btnSalir);

        panelInferior.add(panelChat, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        add(scroll, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // Eventos
        btnFinalizar.addActionListener(e -> controller.finalizar());
        btnEnviar.addActionListener(e -> controller.enviarMensaje());
        btnSalir.addActionListener(e -> {
            dispose(); // Cierra esta ventana
            // Puedes agregar aquí el retorno al dashboard si se pasa referencia o contexto
        });

        setVisible(true);
    }

    private JLabel crearPanelCamara(String titulo) {
        JLabel camara = new JLabel(titulo, SwingConstants.CENTER);
        camara.setOpaque(true);
        camara.setBackground(Color.LIGHT_GRAY);
        camara.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        camara.setPreferredSize(new Dimension(300, 200));
        return camara;
    }

    public void mostrarMensaje(String mensaje) {
        areaSimulacion.append(mensaje + "\n");
    }

    public String getMensaje() {
        return txtMensaje.getText();
    }

    public void limpiarMensaje() {
        txtMensaje.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
