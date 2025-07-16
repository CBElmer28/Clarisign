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
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DashboardTerapeutaView extends JFrame {

    private JTable tablaSesiones;
    private int idTerapeuta;
    private boolean mostrandoHistorial = false;

    public DashboardTerapeutaView(int idTerapeuta) {
        this.idTerapeuta = idTerapeuta;
        setTitle("Panel del Terapeuta");
        setSize(1200, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tabla para sesiones
        tablaSesiones = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablaSesiones);

        // Botones
        JButton btnRevisarSolicitudes = new JButton("Revisar Solicitudes");
        btnRevisarSolicitudes.addActionListener(e -> { 
            new RevisarSolicitudesTerapeutaView(idTerapeuta, this);
            cargarSesiones();});
        
        JButton btnRegistrarPaciente = new JButton("Registrar Paciente");
        btnRegistrarPaciente.addActionListener(e -> { 
            new RegistroView();
            cargarSesiones();
        });

        JButton btnMarcarEnCurso = new JButton("Marcar como En Curso");
        btnMarcarEnCurso.addActionListener(e -> { 
            marcarSesionEnCurso();
            cargarSesiones();});

        JButton btnIngresarSesion = new JButton("Ingresar a la Sesión");
        btnIngresarSesion.addActionListener(e -> { 
            ingresarSesion();
            cargarSesiones();}
        );
        
        JButton btnFinalizarSesion = new JButton("Finalizar Sesión");
        btnFinalizarSesion.addActionListener(e -> {
            finalizarSesion();
            cargarSesiones();
        });
        
        JButton btnEliminarSesion = new JButton("Eliminar Sesión");
        btnEliminarSesion.addActionListener(e -> {
            eliminarSesionFinalizada();
            cargarSesiones();
        });
        
        JButton btnAlternarVista = new JButton("Ver Historial");        
        JButton btnAccionSesion = new JButton();
        btnAlternarVista.addActionListener(e -> {
        mostrandoHistorial = !mostrandoHistorial;
        actualizarBotonAccion(btnAccionSesion); // ← ¡Esto lo actualiza dinámicamente!

        if (mostrandoHistorial) {
            btnAlternarVista.setText("Ver Todas");
            cargarHistorial();
        } else {
            btnAlternarVista.setText("Ver Historial");
            cargarSesiones();
        }
        actualizarBotonAccion(btnAccionSesion); 
    });
        
        JButton btnCancelarSesion = new JButton("Cancelar Sesión");
        btnCancelarSesion.addActionListener(e -> {
            cancelarSesion();
            cargarSesiones();
});
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> {
            cargarSesiones();
            dispose();
            new LoginView().setVisible(true);
        });

// Filtro por nombre y fecha arriba de la tabla
JPanel panelFiltros = new JPanel();
panelFiltros.add(new JLabel("Sesiones Asignadas", SwingConstants.CENTER));
panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtrar sesiones"));
panelFiltros.add(new JLabel("Paciente:"));
JTextField txtPaciente = new JTextField(15);
panelFiltros.add(txtPaciente);

panelFiltros.add(new JLabel("Fecha (yyyy-MM-dd):"));
JTextField txtFecha = new JTextField(10);
panelFiltros.add(txtFecha);

JButton btnBuscar = new JButton("Buscar");
btnBuscar.addActionListener(e -> filtrarSesiones(txtPaciente.getText(), txtFecha.getText()));
panelFiltros.add(btnBuscar);


// Paneles temáticos de botones
JPanel panelAccionesSesion = new JPanel(new GridLayout(2, 2));
panelAccionesSesion.setBorder(BorderFactory.createTitledBorder("Gestión de Sesiones"));
panelAccionesSesion.add(btnMarcarEnCurso);
panelAccionesSesion.add(btnIngresarSesion);
panelAccionesSesion.add(btnFinalizarSesion);
panelAccionesSesion.add(btnAccionSesion);

JPanel panelUtilidades = new JPanel(new GridLayout(1, 2));
panelUtilidades.setBorder(BorderFactory.createTitledBorder("Utilidades"));
panelUtilidades.add(btnAlternarVista);
panelUtilidades.add(btnCerrarSesion);

JPanel panelRegistros = new JPanel();
panelRegistros.setBorder(BorderFactory.createTitledBorder("Flujo"));
panelRegistros.add(btnRevisarSolicitudes);
panelRegistros.add(btnRegistrarPaciente);

// Unificar panel de botones
JPanel panelBotones = new JPanel(new GridLayout(3, 1));
panelBotones.add(panelAccionesSesion);
panelBotones.add(panelUtilidades);
panelBotones.add(panelRegistros);

// Agregar al layout principal
add(panelFiltros, BorderLayout.NORTH);
add(scrollPane, BorderLayout.CENTER);
add(panelBotones, BorderLayout.SOUTH);

        verificarRecordatorios();
        verificarSolicitudesPendientes();
        cargarSesiones();
        actualizarBotonAccion(btnAccionSesion);
        aplicarEstilos();        
        setVisible(true);

    }
    
    private void actualizarBotonAccion(JButton boton) {
        boton.setText("Cancelar Sesión");
    for (ActionListener al : boton.getActionListeners()) {
        boton.removeActionListener(al); // Limpia los anteriores
    }
    
    if (mostrandoHistorial) {
        boton.setText("Eliminar Sesión");
        boton.addActionListener(e -> {
            eliminarSesionFinalizada();
            cargarHistorial(); // ← ¡esta es la clave!
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
    
    private void finalizarSesion() {
        int fila = tablaSesiones.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una sesión.");
            return;
        }

        String estado = tablaSesiones.getValueAt(fila, 4).toString();
        if (!"en curso".equalsIgnoreCase(estado)) {
            JOptionPane.showMessageDialog(this, "Solo puedes finalizar sesiones 'en curso'.");
            return;
        }

        int idSesion = (int) tablaSesiones.getValueAt(fila, 0);

        try (Connection conn = DBConnection.getConnection()) {
            SesionDAO dao = new SesionDAO(conn);
            dao.actualizarEstado(idSesion, "finalizado");
            JOptionPane.showMessageDialog(this, "Sesión marcada como 'finalizado'.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al finalizar sesión: " + e.getMessage());
        }
    }
    
private void eliminarSesionFinalizada() {
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
    private void verificarRecordatorios() {
    SesionController controller = new SesionController();
    List<Sesion> proximas = controller.obtenerRecordatoriosPorUsuario(idTerapeuta);

    if (!proximas.isEmpty()) {
        StringBuilder mensaje = new StringBuilder("Tienes sesiones programadas:\n");
        for (Sesion s : proximas) {
            mensaje.append("- ").append(s.getFechaHora()).append(" con paciente ID: ").append(s.getIdPaciente()).append("\n");
        }
        JOptionPane.showMessageDialog(this, mensaje.toString(), "Recordatorio de sesiones", JOptionPane.INFORMATION_MESSAGE);
    }
}
    private void verificarSolicitudesPendientes() {
    try (Connection conn = DBConnection.getConnection()) {
        SesionDAO dao = new SesionDAO(conn);
        boolean tieneSolicitudes = dao.tieneSolicitudesPendientes(idTerapeuta);
        if (tieneSolicitudes) {
            JOptionPane.showMessageDialog(this,
                "Tienes solicitudes de sesión pendientes por revisar.",
                "Solicitudes pendientes",
                JOptionPane.WARNING_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al verificar solicitudes: " + e.getMessage());
    }
}

    private void cargarHistorial() {
    try (Connection conn = DBConnection.getConnection()) {
        SesionDAO dao = new SesionDAO(conn);
        PacienteDAO pacienteDAO = new PacienteDAO(conn);
        InterpreteDAO interpreteDAO = new InterpreteDAO(conn);

        List<Sesion> sesiones = dao.listarHistorialPorUsuario(idTerapeuta);

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
private void filtrarSesiones(String pacienteNombre, String fechaStr) {
    TableRowSorter<TableModel> sorter = new TableRowSorter<>(tablaSesiones.getModel());
    tablaSesiones.setRowSorter(sorter);

    List<RowFilter<Object, Object>> filters = new ArrayList<>();

    if (!pacienteNombre.isBlank()) {
        filters.add(RowFilter.regexFilter("(?i)" + pacienteNombre, 1)); // columna de nombre paciente
    }

    if (!fechaStr.isBlank()) {
        filters.add(RowFilter.regexFilter(fechaStr, 3)); // columna de fecha
    }

    if (filters.isEmpty()) {
        sorter.setRowFilter(null);
    } else {
        sorter.setRowFilter(RowFilter.andFilter(filters));
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
        JOptionPane.showMessageDialog(this, "Esta sesión ya fue cancelada.");
        return;
    }

    int idSesion = (int) tablaSesiones.getValueAt(fila, 0);
    String motivo = JOptionPane.showInputDialog(this, "Motivo de cancelación:");

    if (motivo == null || motivo.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Cancelación abortada. No se ingresó motivo.");
        return;
    }

    try (Connection conn = DBConnection.getConnection()) {
        SesionDAO dao = new SesionDAO(conn);
        dao.cancelarSesion(idSesion, motivo); // Este método cambia el estado a "cancelada"
        JOptionPane.showMessageDialog(this, "Sesión cancelada.\nMotivo: " + motivo);
        // Si querés guardar el motivo en la base, deberías agregar una columna "motivoCancelacion" y extender el DAO
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
    // Paleta de colores
    Color blanco = Color.WHITE;
    Color grisClaro = new Color(245, 245, 245);
    Color moradoPastel = new Color(180, 140, 200);
    Color textoMorado = new Color(100, 60, 130);
    Color bordeLavanda = new Color(230, 230, 250); // para rejilla de la tabla

    // Tipografías
    Font fuenteBoton = new Font("SansSerif", Font.BOLD, 13);
    Font fuenteTabla = new Font("SansSerif", Font.PLAIN, 12);
    Font fuenteTitulo = new Font("SansSerif", Font.BOLD, 18);
    Font fuenteHeaderTabla = new Font("SansSerif", Font.BOLD, 13);

    // Fondo general de la ventana
    getContentPane().setBackground(grisClaro);

    // Estilizar botones en el panel inferior
    Component[] componentes = ((JPanel) getContentPane().getComponent(2)).getComponents();
    for (Component c : componentes) {
        if (c instanceof JButton btn) {
            btn.setBackground(moradoPastel);
            btn.setForeground(blanco);
            btn.setFont(fuenteBoton);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12)); // más cómodo visualmente
        }
    }

    // Estilizar el título en la parte superior
    Component tituloComp = getContentPane().getComponent(0);
    if (tituloComp instanceof JLabel titulo) {
        titulo.setForeground(textoMorado);
        titulo.setFont(fuenteTitulo);
        titulo.setOpaque(true);
        titulo.setBackground(grisClaro);
        titulo.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
    }

    // Estilizar la tabla
    if (tablaSesiones != null) {
        tablaSesiones.setFont(fuenteTabla);
        tablaSesiones.setRowHeight(24);
        tablaSesiones.setGridColor(bordeLavanda);
        tablaSesiones.setShowGrid(true);

        JTableHeader header = tablaSesiones.getTableHeader();
        header.setFont(fuenteHeaderTabla);
        header.setBackground(moradoPastel);
        header.setForeground(blanco);
    }
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
