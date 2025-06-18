package com.example.clinic.ui.appointment;
public class Appointment {
    Integer idCita;
    Integer idPaciente;
    Integer idDoctor;
    String paciente;
    String doctor;
    String especialidad;
    String fecha;
    String hora;
    Integer estado;
    String estadoCita;
    String motivoConsulta;

    public Appointment(String motivoConsulta, String estadoCita, Integer estado, String hora, String fecha, String especialidad, String doctor, String paciente, Integer idPaciente, Integer idCita, Integer idDoctor) {
        this.motivoConsulta = motivoConsulta;
        this.estadoCita = estadoCita;
        this.estado = estado;
        this.hora = hora;
        this.fecha = fecha;
        this.especialidad = especialidad;
        this.doctor = doctor;
        this.paciente = paciente;
        this.idPaciente = idPaciente;
        this.idCita = idCita;
        this.idDoctor = idDoctor;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Integer getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(Integer idDoctor) {
        this.idDoctor = idDoctor;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(String estadoCita) {
        this.estadoCita = estadoCita;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }
}

