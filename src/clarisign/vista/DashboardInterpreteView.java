/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package clarisign.vista;

import clarisign.DB.*;
import clarisign.controlador.SesionController;
import clarisign.modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;


public class DashboardInterpreteView extends javax.swing.JFrame {
    
    private JTable tablaSesiones;
    private int idInterprete;
    private DefaultTableModel model;

    public DashboardInterpreteView(int idInterprete) {
        this.idInterprete = idInterprete;
        setTitle("Panel del Intérprete");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tablaSesiones = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablaSesiones);
                
        JButton btnAceptarSolicitud = new JButton("Aceptar Solicitud");
        btnAceptarSolicitud.addActionListener(e -> {aceptarSolicitud();
        cargarSesiones();});
        
        JButton btnIngresarSesion = new JButton("Ingresar a la Sesión");
        btnIngresarSesion.addActionListener(e -> { ingresarSesion();
        cargarSesiones();});

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> {
            cargarSesiones();
            dispose(); // Cierra el panel actual
            new LoginView().setVisible(true);
        });
        
        
        
        JPanel botones = new JPanel();
        botones.add(btnAceptarSolicitud);
        botones.add(btnIngresarSesion);
        botones.add(btnCerrarSesion);

        add(new JLabel("Sesiones asignadas al Intérprete", SwingConstants.CENTER), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        cargarSesiones();
        setVisible(true);
    }

    public void cargarSesiones() {
        try (Connection conn = DBConnection.getConnection()) {
            SesionDAO dao = new SesionDAO(conn);
            PacienteDAO pacienteDAO = new PacienteDAO(conn);
            TerapeutaDAO terapeutaDAO = new TerapeutaDAO(conn);
            
            List<Sesion> sesiones = dao.listarSesionesPorInterprete(idInterprete);

            model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"ID", "Paciente", "Terapeuta", "Fecha", "Estado"});
            
            for (Sesion s : sesiones) {
                Paciente paciente = pacienteDAO.obtenerPorId(s.getIdPaciente());
            Terapeuta terapeuta = terapeutaDAO.obtenerPorId(s.getIdTerapeuta());
                
                model.addRow(new Object[]{
                    s.getId(),
                    paciente != null ? paciente.getNombre() : "Desconocido",
                    terapeuta != null ? terapeuta.getNombre() : "Desconocido",
                    s.getFechaHora(),
                    s.getEstado()
                });
            }
            tablaSesiones.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar sesiones: " + e.getMessage());
        }
    }

    private void aceptarSolicitud() {
        int fila = tablaSesiones.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una sesión.");
            return;
        }

        int idSesion = (int) model.getValueAt(fila, 0);
        String estado = (String) model.getValueAt(fila, 4);

        if (!estado.equalsIgnoreCase("en revision")) {
            JOptionPane.showMessageDialog(this, "Solo puedes aceptar sesiones en estado 'en revision'.");
            return;
        }

        try {
            SesionController controller = new SesionController();
            if (controller.actualizarEstadoSesion(idSesion, "en espera")) {
                JOptionPane.showMessageDialog(this, "Solicitud aceptada. Sesión en espera.");
                cargarSesiones();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el estado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al aceptar la solicitud: " + e.getMessage());
        }
    }
    
        private void ingresarSesion() {
        int fila = tablaSesiones.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una sesión.");
            return;
        }

        String estado = tablaSesiones.getValueAt(fila, 4).toString();
        if (!"en curso".equalsIgnoreCase(estado)) {
            JOptionPane.showMessageDialog(this, "Solo puedes ingresar a sesiones 'en curso'.");
            return;
        }
        

        int idSesion = (int) tablaSesiones.getValueAt(fila, 0);
        // Aquí podrías abrir una nueva vista que simule el desarrollo de la sesión
        JOptionPane.showMessageDialog(this, "Ingresando a la sesión ID: " + idSesion);
        new VideoLlamadaSimuladaView();
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
