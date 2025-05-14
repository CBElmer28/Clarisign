/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.modelo;

public class Paciente extends Usuario {
    private String diagnostico;

    public Paciente(int id, String nombre, String correo, String contrasena, String diagnostico) {
        super(id, nombre, correo, contrasena);
        this.diagnostico = diagnostico;
    }

    /**
     * @return the diagnostico
     */
    public String getDiagnostico() {
        return diagnostico;
    }

    /**
     * @param diagnostico the diagnostico to set
     */
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
    
    @Override
    public String toString() {
        return getNombre(); // O getCorreo(), según lo que prefieras mostrar
    }
}
