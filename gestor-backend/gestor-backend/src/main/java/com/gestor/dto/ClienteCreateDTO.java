// ClienteCreateDTO.java - DTO para crear cliente
package com.gestor.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ClienteCreateDTO {

    @NotNull(message = "El ID de usuario es requerido")
    private Long idUsuario;

    @NotBlank(message = "El nombre completo es requerido")
    @Size(max = 150, message = "El nombre completo no puede exceder 150 caracteres")
    private String nombreCompleto;

    @NotBlank(message = "El DPI es requerido")
    @Pattern(regexp = "\\d{13}", message = "El DPI debe tener exactamente 13 dígitos")
    private String dpi;

    @NotNull(message = "La fecha de nacimiento es requerida")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @Size(max = 8, message = "El teléfono debe tener máximo 8 caracteres")
    @Pattern(regexp = "\\d{8}", message = "El teléfono debe tener exactamente 8 dígitos")
    private String telefono;

    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    private String direccion;

    // Datos sensibles opcionales
    private String nit;
    private String nis;
    private String email;
    private String cuentaBancaria;
    private String passAgenciaVirtual;
    private String passCorreo;
    private String passCgc;
    private String passConsultaGeneral;
    private String passReghae;

    // Getters y Setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getDpi() { return dpi; }
    public void setDpi(String dpi) { this.dpi = dpi; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getNis() { return nis; }
    public void setNis(String nis) { this.nis = nis; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCuentaBancaria() { return cuentaBancaria; }
    public void setCuentaBancaria(String cuentaBancaria) { this.cuentaBancaria = cuentaBancaria; }

    public String getPassAgenciaVirtual() { return passAgenciaVirtual; }
    public void setPassAgenciaVirtual(String passAgenciaVirtual) { this.passAgenciaVirtual = passAgenciaVirtual; }

    public String getPassCorreo() { return passCorreo; }
    public void setPassCorreo(String passCorreo) { this.passCorreo = passCorreo; }

    public String getPassCgc() { return passCgc; }
    public void setPassCgc(String passCgc) { this.passCgc = passCgc; }

    public String getPassConsultaGeneral() { return passConsultaGeneral; }
    public void setPassConsultaGeneral(String passConsultaGeneral) { this.passConsultaGeneral = passConsultaGeneral; }

    public String getPassReghae() { return passReghae; }
    public void setPassReghae(String passReghae) { this.passReghae = passReghae; }
}
