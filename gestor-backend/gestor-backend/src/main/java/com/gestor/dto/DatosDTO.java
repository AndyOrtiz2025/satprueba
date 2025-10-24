package com.gestor.dto;

import java.time.LocalDateTime;

public class DatosDTO {

    private Long idDatos;
    private Long idCliente;
    private String nit;
    private Integer nis;
    private String email;
    private String dpi;
    private String cuentaBancaria;
    private String contrasenaAgenciaVirtual;
    private String contrasenaCorreo;
    private String contrasenaCgc;
    private String contrasenaConsultaGeneral;
    private String contrasenaRegahe;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters y Setters
    public Long getIdDatos() {
        return idDatos;
    }

    public void setIdDatos(Long idDatos) {
        this.idDatos = idDatos;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public Integer getNis() {
        return nis;
    }

    public void setNis(Integer nis) {
        this.nis = nis;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public String getContrasenaAgenciaVirtual() {
        return contrasenaAgenciaVirtual;
    }

    public void setContrasenaAgenciaVirtual(String contrasenaAgenciaVirtual) {
        this.contrasenaAgenciaVirtual = contrasenaAgenciaVirtual;
    }

    public String getContrasenaCorreo() {
        return contrasenaCorreo;
    }

    public void setContrasenaCorreo(String contrasenaCorreo) {
        this.contrasenaCorreo = contrasenaCorreo;
    }

    public String getContrasenaCgc() {
        return contrasenaCgc;
    }

    public void setContrasenaCgc(String contrasenaCgc) {
        this.contrasenaCgc = contrasenaCgc;
    }

    public String getContrasenaConsultaGeneral() {
        return contrasenaConsultaGeneral;
    }

    public void setContrasenaConsultaGeneral(String contrasenaConsultaGeneral) {
        this.contrasenaConsultaGeneral = contrasenaConsultaGeneral;
    }

    public String getContrasenaRegahe() {
        return contrasenaRegahe;
    }

    public void setContrasenaRegahe(String contrasenaRegahe) {
        this.contrasenaRegahe = contrasenaRegahe;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
