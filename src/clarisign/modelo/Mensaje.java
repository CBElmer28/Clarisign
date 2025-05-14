/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clarisign.modelo;

import java.time.LocalDateTime;

public class Mensaje {
    private int id;
    private int idSesion;
    private int idRemitente;
    private String contenido;
    private LocalDateTime fechaHora;

    public Mensaje(int id, int idSesion, int idRemitente, String contenido, LocalDateTime fechaHora) {
        this.id = id;
        this.idSesion = idSesion;
        this.idRemitente = idRemitente;
        this.contenido = contenido;
        this.fechaHora = fechaHora;
    }

    // Getters y setters
}