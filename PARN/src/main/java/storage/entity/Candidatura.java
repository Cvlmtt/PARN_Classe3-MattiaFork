package storage.entity;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Candidatura {
    private Persona persona;
    private Annuncio annuncio;
    private Curriculum curriculum;
    private LocalDate data;

    public Candidatura(Persona persona, Annuncio annuncio, Curriculum curriculum, LocalDate data) {
        this.persona = persona;
        this.annuncio = annuncio;
        this.curriculum = curriculum;
        this.data = data;
    }

    public Candidatura() {

    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Annuncio getAnnuncio() {
        return annuncio;
    }

    public void setAnnuncio(Annuncio annuncio) {
        this.annuncio = annuncio;
    }

    public Curriculum getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
