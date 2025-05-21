/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package clarisign.vista;

import clarisign.DB.*;
import clarisign.controlador.SesionController;
import clarisign.modelo.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RevisarSolicitudesTerapeutaView extends JFrame {

    private JComboBox<ComboItemSesion> cbSolicitudes;
    private JComboBox<Interprete> cbInterpretes;
    private JTextField txtFechaHora;
    private JButton btnAsignar;
    private DashboardTerapeutaView dashboard;

    public RevisarSolicitudesTerapeutaView(int idTerapeuta, DashboardTerapeutaView dashboard) {
        this.dashboard=dashboard;
        
        setTitle("Revisar Solicitudes de Sesión");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 5, 5));

        cbSolicitudes = new JComboBox<>();
        cbInterpretes = new JComboBox<>();
        txtFechaHora = new JTextField();
        btnAsignar = new JButton("Asignar intérprete y programar");

        add(new JLabel("Solicitudes pendientes:"));
        add(cbSolicitudes);
        add(new JLabel("Seleccionar intérprete:"));
        add(cbInterpretes);
        add(new JLabel("Fecha y Hora (yyyy-MM-dd HH:mm):"));
        add(txtFechaHora);
        add(new JLabel());
        add(btnAsignar);

        btnAsignar.addActionListener(e -> asignarSesion());

        cargarSolicitudes(idTerapeuta);
        cargarInterpretes();

        setVisible(true);
    }

    private void cargarSolicitudes(int idTerapeuta) {
        try (Connection conn = DBConnection.getConnection()) {
            SesionDAO dao = new SesionDAO(conn);
            PacienteDAO pacienteDAO = new PacienteDAO(conn);

            List<Sesion> solicitudes = dao.obtenerSolicitudesPorTerapeuta(idTerapeuta);

            for (Sesion s : solicitudes) {
                Paciente paciente = pacienteDAO.obtenerPorId(s.getIdPaciente());
                String nombrePaciente = (paciente != null) ? paciente.getNombre() : "Desconocido";
                cbSolicitudes.addItem(new ComboItemSesion(s, nombrePaciente));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando solicitudes: " + e.getMessage());
        }
    }

    private void cargarInterpretes() {
        try (Connection conn = DBConnection.getConnection()) {
            InterpreteDAO dao = new InterpreteDAO(conn);
            List<Interprete> interpretes = dao.listarTodos();
            for (Interprete i : interpretes) {
                cbInterpretes.addItem(i);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando intérpretes: " + e.getMessage());
        }
    }

    private void asignarSesion() {
    try {
        ComboItemSesion item = (ComboItemSesion) cbSolicitudes.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una solicitud.");
            return;
        }

        Sesion sesion = item.getSesion();
        Interprete interprete = (Interprete) cbInterpretes.getSelectedItem();

        String fechaHoraStr = txtFechaHora.getText().trim();
        LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        sesion.setIdInterprete(interprete.getId());
        sesion.setFechaHora(fechaHora);
        sesion.setEstado("en revision");

        SesionController controller = new SesionController();
        if (controller.asignarDetallesSesion(sesion)) {
            JOptionPane.showMessageDialog(this, "Sesión programada en revisión.");
            cbSolicitudes.removeItem(item);
            txtFechaHora.setText("");

            if (dashboard != null) {
                dashboard.cargarSesiones(); // ✅ Actualiza el dashboard
            }

        } else {
            JOptionPane.showMessageDialog(this, "No se pudo asignar.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al asignar: " + e.getMessage());
    }
}

    // Clase auxiliar para mostrar sesiones con nombre del paciente
    private class ComboItemSesion {
        private Sesion sesion;
        private String display;

        public ComboItemSesion(Sesion sesion, String nombrePaciente) {
            this.sesion = sesion;
            this.display = "Paciente: " + nombrePaciente + " | ID Sesión: " + sesion.getId();
        }

        @Override
        public String toString() {
            return display;
        }

        public Sesion getSesion() {
            return sesion;
        }
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
