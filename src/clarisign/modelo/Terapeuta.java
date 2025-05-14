/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.modelo;

public class Terapeuta extends Usuario {
    private String especialidad;

    public Terapeuta(int id, String nombre, String correo, String contrasena, String especialidad) {
        super(id, nombre, correo, contrasena);
        this.especialidad = especialidad;
    }

    /**
     * @return the especialidad
     */
    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * @param especialidad the especialidad to set
     */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    
    @Override
    public String toString() {
        return getNombre();
    }
    
}