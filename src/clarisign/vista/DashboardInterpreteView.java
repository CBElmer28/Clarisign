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
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.table.JTableHeader;


public class DashboardInterpreteView extends javax.swing.JFrame {
    
    private JTable tablaSesiones;
    private int idInterprete;
    private DefaultTableModel model;
    private boolean mostrandoHistorial = false;

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
        
                        JButton btnEliminarSesion = new JButton("Eliminar Sesión");
        btnEliminarSesion.addActionListener(e -> {
            eliminarSesionPasada();
            cargarSesiones();
        });

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> {
            cargarSesiones();
            dispose(); // Cierra el panel actual
            new LoginView().setVisible(true);
        });
        
JButton btnAccionSesion = new JButton();
actualizarBotonAccion(btnAccionSesion); // Inicializa el botón dinámico

JButton btnAlternarVista = new JButton("Ver Historial");
btnAlternarVista.addActionListener(e -> {
    mostrandoHistorial = !mostrandoHistorial;

    if (mostrandoHistorial) {
        btnAlternarVista.setText("Ver Todas");
        cargarHistorial();
    } else {
        btnAlternarVista.setText("Ver Historial");
        cargarSesiones();
    }

    actualizarBotonAccion(btnAccionSesion); // Actualiza el botón después de cambiar la vista
});
                
        
        JPanel botones = new JPanel();
        botones.add(btnAceptarSolicitud);
        botones.add(btnAlternarVista);
        botones.add(btnIngresarSesion);
        botones.add(btnAccionSesion);
        botones.add(btnCerrarSesion);

        add(new JLabel("Sesiones asignadas al Intérprete", SwingConstants.CENTER), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        cargarSesiones();
        aplicarEstilos();
        setVisible(true);
    }

    private void actualizarBotonAccion(JButton boton) {
    for (ActionListener al : boton.getActionListeners()) {
        boton.removeActionListener(al);
    }

    if (mostrandoHistorial) {
        boton.setText("Eliminar Sesión");
        boton.addActionListener(e -> {
            eliminarSesionPasada();
            cargarHistorial();
        });
    } else {
        boton.setText("Cancelar Sesión");
        boton.addActionListener(e -> {
            cancelarSesion();
            cargarSesiones();
        });
    }
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
        private void cargarHistorial() {
    try (Connection conn = DBConnection.getConnection()) {
        SesionDAO dao = new SesionDAO(conn);
        PacienteDAO pacienteDAO = new PacienteDAO(conn);
        InterpreteDAO interpreteDAO = new InterpreteDAO(conn);

        List<Sesion> sesiones = dao.listarHistorialPorUsuario(idInterprete);

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Paciente", "Intérprete", "Fecha", "Estado"});

        for (Sesion s : sesiones) {
            Paciente paciente = pacienteDAO.obtenerPorId(s.getIdPaciente());
            String nombrePaciente = (paciente != null) ? paciente.getNombre() : "Desconocido";

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
        JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage());
    }
}
private void eliminarSesionPasada() {
    int fila = tablaSesiones.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Selecciona una sesión.");
        return;
    }

    String estado = tablaSesiones.getValueAt(fila, 4).toString();
    String fechaStr = tablaSesiones.getValueAt(fila, 3).toString();

    LocalDateTime fechaHora;
    try {
        fechaHora = LocalDateTime.parse(fechaStr); // validá formato
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al interpretar la fecha de la sesión.");
        return;
    }

    // Acepta si está cancelada o si ya pasó
    if (!"cancelada".equalsIgnoreCase(estado) && !fechaHora.isBefore(LocalDateTime.now())) {
        JOptionPane.showMessageDialog(this, "Solo puedes eliminar sesiones canceladas o ya pasadas.");
        return;
    }

    int idSesion = (int) tablaSesiones.getValueAt(fila, 0);
    int confirm = JOptionPane.showConfirmDialog(this,
        "¿Eliminar sesión ID: " + idSesion + "? Esta acción es irreversible.",
        "Confirmar Eliminación",
        JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) return;

    try (Connection conn = DBConnection.getConnection()) {
        SesionDAO dao = new SesionDAO(conn);
        dao.eliminarSesion(idSesion);
        JOptionPane.showMessageDialog(this, "Sesión eliminada correctamente.");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al eliminar sesión: " + e.getMessage());
    }
}

            private void cancelarSesion() {
    int fila = tablaSesiones.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Selecciona una sesión.");
        return;
    }

    String estado = tablaSesiones.getValueAt(fila, 4).toString();
    if ("cancelada".equalsIgnoreCase(estado)) {
        JOptionPane.showMessageDialog(this, "La sesión ya está cancelada.");
        return;
    }

    int idSesion = (int) tablaSesiones.getValueAt(fila, 0);
    String motivo = JOptionPane.showInputDialog(this, "Motivo de cancelación:");

    if (motivo == null || motivo.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No se ingresó motivo. Cancelación abortada.");
        return;
    }

    try (Connection conn = DBConnection.getConnection()) {
        SesionDAO dao = new SesionDAO(conn);
        dao.cancelarSesion(idSesion, motivo); // Asegurate de tener este método en el DAO
        JOptionPane.showMessageDialog(this, "Sesión cancelada.\nMotivo: " + motivo);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cancelar sesión: " + e.getMessage());
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

    private void aplicarEstilos() {
        // Colores base
    Color moradoPastel = new Color(178, 156, 255);
    Color blancoGrisaceo = new Color(245, 245, 250); // Blanco con tono suave

    // Fondo general
    getContentPane().setBackground(blancoGrisaceo);

    // Fuente general
    Font fuente = new Font("Segoe UI", Font.PLAIN, 14);

    // Estilizar tabla
    tablaSesiones.setFont(fuente);
    tablaSesiones.setRowHeight(24);

    JTableHeader header = tablaSesiones.getTableHeader();
    header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    header.setBackground(moradoPastel);
    header.setForeground(Color.WHITE);

    // Estilizar botones
    for (Component comp : ((JPanel) getContentPane().getComponent(2)).getComponents()) {
        if (comp instanceof JButton) {
            JButton btn = (JButton) comp;
            btn.setFont(fuente);
            btn.setBackground(moradoPastel);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        }
    }

    // Estilizar título
    Component titulo = getContentPane().getComponent(0);
    if (titulo instanceof JLabel) {
        JLabel lblTitulo = (JLabel) titulo;
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(moradoPastel);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
