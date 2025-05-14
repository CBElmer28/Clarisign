/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.modelo;

/**
 *
 * @author MARCE
 */
public class Interprete extends Usuario {
    private String lenguajeSenias;

    public Interprete(int id, String nombre, String correo, String contrasena, String lenguajeSenias) {
        super(id, nombre, correo, contrasena);
        this.lenguajeSenias = lenguajeSenias;
    }

    // Getters y setters

    /**
     * @return the lenguajeSenias
     */
    public String getLenguajeSenias() {
        return lenguajeSenias;
    }

    /**
     * @param lenguajeSenias the lenguajeSenias to set
     */
    public void setLenguajeSenias(String lenguajeSenias) {
        this.lenguajeSenias = lenguajeSenias;
    }
    
    @Override
    public String toString() {
        return getNombre();
    }
}