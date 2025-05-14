/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package clarisign.vista;

import clarisign.DB.*;
import clarisign.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class DashboardTerapeutaView extends JFrame {

    private JTable tablaSesiones;
    private int idTerapeuta;

    public DashboardTerapeutaView(int idTerapeuta) {
        this.idTerapeuta = idTerapeuta;
        setTitle("Panel del Terapeuta");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tabla para sesiones
        tablaSesiones = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablaSesiones);

        // Botones
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarSesiones());

        JButton btnRevisarSolicitudes = new JButton("Revisar Solicitudes");
        btnRevisarSolicitudes.addActionListener(e -> new RevisarSolicitudesTerapeutaView(idTerapeuta));

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });

        JButton btnRegistrarPaciente = new JButton("Registrar Paciente");
        btnRegistrarPaciente.addActionListener(e -> new RegistroView());

        JButton btnMarcarEnCurso = new JButton("Marcar como En Curso");
        btnMarcarEnCurso.addActionListener(e -> marcarSesionEnCurso());

        JButton btnIngresarSesion = new JButton("Ingresar a la Sesión");
        btnIngresarSesion.addActionListener(e -> ingresarSesion());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnRevisarSolicitudes);
        panelBotones.add(btnRegistrarPaciente);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnMarcarEnCurso);
        panelBotones.add(btnIngresarSesion);
        panelBotones.add(btnCerrarSesion);

        add(new JLabel("Sesiones Asignadas", SwingConstants.CENTER), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        cargarSesiones();
        setVisible(true);
    }

    private void cargarSesiones() {
        try (Connection conn = DBConnection.getConnection()) {
            SesionDAO dao = new SesionDAO(conn);
            PacienteDAO pacienteDAO = new PacienteDAO(conn);
            InterpreteDAO interpreteDAO = new InterpreteDAO(conn);

            List<Sesion> sesiones = dao.listarSesionesPorTerapeuta(idTerapeuta);

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"ID", "Paciente", "Intérprete", "Fecha", "Estado"});

            for (Sesion s : sesiones) {
                Paciente paciente = pacienteDAO.obtenerPorId(s.getIdPaciente());
                String nombrePaciente = (paciente != null) ? paciente.getNombre() : "Desconocido";

                // Obtener intérprete (si está asignado)
                String nombreInterprete = "No asignado";
                Integer idInterprete = s.getIdInterprete();
                if (idInterprete != null && idInterprete != 0) {
                Interprete interprete = interpreteDAO.obtenerPorId(idInterprete);
                nombreInterprete = (interprete != null) ? interprete.getNombre() : "Desconocido";
                }

                model.addRow(new Object[]{
                    s.getId(),
                    nombrePaciente,
                    nombreInterprete,
                    s.getFechaHora(),
                    s.getEstado()
                });
            }

            tablaSesiones.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar sesiones: " + e.getMessage());
        }
    }

    private void marcarSesionEnCurso() {
        int fila = tablaSesiones.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una sesión.");
            return;
        }

        String estado = tablaSesiones.getValueAt(fila, 4).toString();
        if (!"en espera".equalsIgnoreCase(estado)) {
            JOptionPane.showMessageDialog(this, "La sesión no está en estado 'en espera'.");
            return;
        }

        int idSesion = (int) tablaSesiones.getValueAt(fila, 0);

        try (Connection conn = DBConnection.getConnection()) {
            SesionDAO dao = new SesionDAO(conn);
            dao.actualizarEstado(idSesion, "en curso");
            cargarSesiones();
            JOptionPane.showMessageDialog(this, "Sesión marcada como 'en curso'.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar estado: " + e.getMessage());
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
