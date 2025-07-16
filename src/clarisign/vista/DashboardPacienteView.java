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
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.table.JTableHeader;

public class DashboardPacienteView extends JFrame {

    private JTable tablaSesiones;
    private int idPaciente;
    private boolean mostrandoHistorial = false;

    public DashboardPacienteView(int idPaciente) {
        this.idPaciente = idPaciente;
        setTitle("Panel del Paciente");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tabla de sesiones
        tablaSesiones = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablaSesiones);

        // Botones
        JButton btnSolicitarSesion = new JButton("Solicitar Sesión");
        btnSolicitarSesion.addActionListener(e -> { new SolicitudSesionPacienteView(idPaciente, this);
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
            dispose(); // Cierra el panel actual
            new LoginView().setVisible(true);
                    cargarSesiones();
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


        JPanel panelBotones = new JPanel();
        panelBotones.add(btnSolicitarSesion);
        panelBotones.add(btnAlternarVista);
        panelBotones.add(btnIngresarSesion);
        panelBotones.add(btnAccionSesion);
        panelBotones.add(btnCerrarSesion);

        add(new JLabel("Sesiones Programadas", SwingConstants.CENTER), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        cargarSesiones();
        aplicarEstilos();
        setVisible(true);        
    }

    public void cargarSesiones() {
        try (Connection conn = DBConnection.getConnection()) {
            SesionDAO dao = new SesionDAO(conn);
            InterpreteDAO interpreteDAO = new InterpreteDAO(conn);
            TerapeutaDAO terapeutaDAO = new TerapeutaDAO(conn);
            
            List<Sesion> sesiones = dao.listarSesionesPorPaciente(idPaciente);
            
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"ID", "Terapeuta", "Intérprete", "Fecha", "Estado"});

            for (Sesion s : sesiones) {
            Interprete interprete = s.getIdInterprete() != null ? interpreteDAO.obtenerPorId(s.getIdInterprete()) : null;
            Terapeuta terapeuta = terapeutaDAO.obtenerPorId(s.getIdTerapeuta());

            // Si no hay intérprete, asignamos "Desconocido"
            String nombreInterprete = (interprete != null) ? interprete.getNombre() : "Desconocido";
            // Si no hay terapeuta, asignamos "Desconocido"
            String nombreTerapeuta = (terapeuta != null) ? terapeuta.getNombre() : "Desconocido";
            
            // Agregamos la fila a la tabla
            model.addRow(new Object[]{
                s.getId(),
                nombreTerapeuta,
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

        List<Sesion> sesiones = dao.listarHistorialPorUsuario(idPaciente);

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

    Object estadoObj = tablaSesiones.getValueAt(fila, 4);
    Object fechaObj = tablaSesiones.getValueAt(fila, 3);

    String estado = estadoObj != null ? estadoObj.toString() : "";
    String fechaStr = fechaObj != null ? fechaObj.toString() : null;

    boolean puedeEliminar = false;

    if ("cancelada".equalsIgnoreCase(estado)) {
        puedeEliminar = true; // canceladas se pueden eliminar sin fecha
    } else if (fechaStr != null) {
        try {
            LocalDateTime fechaHora = LocalDateTime.parse(fechaStr);
            if (fechaHora.isBefore(LocalDateTime.now())) {
                puedeEliminar = true;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. No se puede interpretar.");
            return;
        }
    }

    if (!puedeEliminar) {
        JOptionPane.showMessageDialog(this, "Solo puedes eliminar sesiones canceladas o pasadas.");
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
    Color blanco = Color.WHITE;
    Color moradoPastel = new Color(204, 153, 255); // morado claro pastel
    Color grisClaro = new Color(245, 245, 245);    // para el fondo general

    getContentPane().setBackground(grisClaro); // Evita que sea blanco puro

    // Estilo para tabla
    tablaSesiones.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    tablaSesiones.setBackground(Color.WHITE);
    tablaSesiones.setGridColor(moradoPastel);
    tablaSesiones.setRowHeight(28);
    tablaSesiones.setShowHorizontalLines(true);
    tablaSesiones.setShowVerticalLines(false);
    tablaSesiones.setSelectionBackground(moradoPastel);
    tablaSesiones.setSelectionForeground(Color.WHITE);

    JTableHeader header = tablaSesiones.getTableHeader();
    header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    header.setBackground(moradoPastel);
    header.setForeground(Color.WHITE);

    // Estilo para botones
    Component[] botones = ((JPanel) getContentPane().getComponent(2)).getComponents();
    for (Component c : botones) {
        if (c instanceof JButton btn) {
            btn.setBackground(moradoPastel);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }
    }

    // Estilo para el título
    JLabel titulo = (JLabel) getContentPane().getComponent(0);
    titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
    titulo.setForeground(new Color(100, 0, 150)); // tono más fuerte de morado
    titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    titulo.setOpaque(true);
    titulo.setBackground(grisClaro);
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
