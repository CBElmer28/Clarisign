/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.modelo;

import java.time.LocalDateTime;

public class Sesion {
    private int id;
    private int idPaciente;
    private int idTerapeuta;
    private Integer idInterprete;
    private LocalDateTime fechaHora;
    private String estado;

    public Sesion(int id, int idPaciente, int idTerapeuta, Integer idInterprete, LocalDateTime fechaHora, String estado) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idTerapeuta = idTerapeuta;
        this.idInterprete = idInterprete;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }

    // Getters y setters

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the idPaciente
     */
    public int getIdPaciente() {
        return idPaciente;
    }

    /**
     * @param idPaciente the idPaciente to set
     */
    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    /**
     * @return the idTerapeuta
     */
    public int getIdTerapeuta() {
        return idTerapeuta;
    }

    /**
     * @param idTerapeuta the idTerapeuta to set
     */
    public void setIdTerapeuta(int idTerapeuta) {
        this.idTerapeuta = idTerapeuta;
    }

    /**
     * @return the idInterprete
     */
    public Integer getIdInterprete() {
        return idInterprete;
    }

    /**
     * @param idInterprete the idInterprete to set
     */
    public void setIdInterprete(Integer idInterprete) {
        this.idInterprete = idInterprete;
    }

    /**
     * @return the fechaHora
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * @param fechaHora the fechaHora to set
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
    return "Paciente ID: " + idPaciente;
    }
}