package com.gestor.dto;

import java.time.LocalDate;

public class ClienteDTO {
    private Long id;
    private Long idUsuario; // Requerido para crear cliente
    private String nombreCompleto;
    private String telefono;
    private String direccion;
    private String dpi;
    private LocalDate fechaNacimiento;

    // Datos sensibles (de la tabla 'datos')
    private String nis;
    private String nit;
    private String email;
    private String cuentaBancaria;
    private String passAgenciaVirtual;
    private String passCorreo;
    private String passCgc;
    private String passConsultaGeneral;
    private String passReghae;

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public Long getIdUsuario(){ return idUsuario; }
    public void setIdUsuario(Long idUsuario){ this.idUsuario = idUsuario; }

    public String getNombreCompleto(){ return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto){ this.nombreCompleto = nombreCompleto; }

    public String getTelefono(){ return telefono; }
    public void setTelefono(String telefono){ this.telefono = telefono; }

    public String getDireccion(){ return direccion; }
    public void setDireccion(String direccion){ this.direccion = direccion; }

    public String getDpi(){ return dpi; }
    public void setDpi(String dpi){ this.dpi = dpi; }

    public LocalDate getFechaNacimiento(){ return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento){ this.fechaNacimiento = fechaNacimiento; }

    public String getNis(){ return nis; }
    public void setNis(String nis){ this.nis = nis; }

    public String getNit(){ return nit; }
    public void setNit(String nit){ this.nit = nit; }

    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email = email; }

    public String getCuentaBancaria(){ return cuentaBancaria; }
    public void setCuentaBancaria(String cuentaBancaria){ this.cuentaBancaria = cuentaBancaria; }

    public String getPassAgenciaVirtual(){ return passAgenciaVirtual; }
    public void setPassAgenciaVirtual(String passAgenciaVirtual){ this.passAgenciaVirtual = passAgenciaVirtual; }

    public String getPassCorreo(){ return passCorreo; }
    public void setPassCorreo(String passCorreo){ this.passCorreo = passCorreo; }

    public String getPassCgc(){ return passCgc; }
    public void setPassCgc(String passCgc){ this.passCgc = passCgc; }

    public String getPassConsultaGeneral(){ return passConsultaGeneral; }
    public void setPassConsultaGeneral(String passConsultaGeneral){ this.passConsultaGeneral = passConsultaGeneral; }

    public String getPassReghae(){ return passReghae; }
    public void setPassReghae(String passReghae){ this.passReghae = passReghae; }
}
