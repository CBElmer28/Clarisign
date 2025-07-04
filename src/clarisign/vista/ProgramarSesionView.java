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
/**
 *
 * @author MARCE
 */
public class ProgramarSesionView extends JFrame {

    private JComboBox<Paciente> cbPacientes;
    private JComboBox<Terapeuta> cbTerapeutas;
    private JComboBox<Interprete> cbInterpretes;
    private JTextField txtFechaHora;
    private JButton btnProgramar;
    
    public ProgramarSesionView() {
        setTitle("Programar Sesión");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 5, 5));

        cbPacientes = new JComboBox<>();
        cbTerapeutas = new JComboBox<>();
        cbInterpretes = new JComboBox<>();
        txtFechaHora = new JTextField();
        btnProgramar = new JButton("Programar Sesión");

        add(new JLabel("Paciente:")); add(cbPacientes);
        add(new JLabel("Terapeuta:")); add(cbTerapeutas);
        add(new JLabel("Intérprete:")); add(cbInterpretes);
        add(new JLabel("Fecha y Hora (yyyy-MM-dd HH:mm):")); add(txtFechaHora);
        add(new JLabel()); add(btnProgramar);

        btnProgramar.addActionListener(e -> programarSesion());

        cargarCombos();

        setVisible(true);
    }
    
    private void cargarCombos() {
        try (Connection conn = DBConnection.getConnection()) {
            List<Paciente> pacientes = new PacienteDAO(conn).listarTodos();
            for (Paciente p : pacientes) cbPacientes.addItem(p);

            List<Terapeuta> terapeutas = new TerapeutaDAO(conn).listarTodos();
            for (Terapeuta t : terapeutas) cbTerapeutas.addItem(t);

            List<Interprete> interpretes = new InterpreteDAO(conn).listarTodos();
            for (Interprete i : interpretes) cbInterpretes.addItem(i);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando datos: " + e.getMessage());
        }
    }
    
    private void programarSesion() {
        try {
            Paciente paciente = (Paciente) cbPacientes.getSelectedItem();
            Terapeuta terapeuta = (Terapeuta) cbTerapeutas.getSelectedItem();
            Interprete interprete = (Interprete) cbInterpretes.getSelectedItem();

            String fechaHoraStr = txtFechaHora.getText().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr, formatter);

            Sesion sesion = new Sesion(
                0,
                paciente.getId(),
                terapeuta.getId(),
                interprete.getId(),
                fechaHora,
                "pendiente"
            );

            SesionController controller = new SesionController();
            if (controller.programarSesion(sesion)) {
                JOptionPane.showMessageDialog(this, "Sesión programada correctamente.");
                txtFechaHora.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al programar sesión.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
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
            .addGap(0, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
