// Datos.java
package com.gestor.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "datos")
public class Datos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_datos")
    private Long idDatos;

    @OneToOne
    @JoinColumn(name = "id_cliente", nullable = false, unique = true)
    private Cliente cliente;

    @Column(name = "nit", length = 15)
    private String nit;

    @Column(name = "nis")
    private Integer nis;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "dpi", length = 13)
    private String dpi;

    @Column(name = "cuenta_bancaria", length = 20)
    private String cuentaBancaria;

    // Contraseñas encriptadas con AES-256
    @Column(name = "contrasena_agencia_virtual", columnDefinition = "TEXT")
    private String contrasenaAgenciaVirtual;

    @Column(name = "contrasena_correo", columnDefinition = "TEXT")
    private String contrasenaCorreo;

    @Column(name = "contrasena_cgc", columnDefinition = "TEXT")
    private String contrasenaCgc;

    @Column(name = "contrasena_consulta_general", columnDefinition = "TEXT")
    private String contrasenaConsultaGeneral;

    @Column(name = "contrasena_regahe", columnDefinition = "TEXT")
    private String contrasenaRegahe;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "created_by", length = 150)
    private String createdBy;

    @Column(name = "updated_by", length = 150)
    private String updatedBy;

    // Lifecycle callback
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getIdDatos() { return idDatos; }
    public void setIdDatos(Long idDatos) { this.idDatos = idDatos; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public Integer getNis() { return nis; }
    public void setNis(Integer nis) { this.nis = nis; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDpi() { return dpi; }
    public void setDpi(String dpi) { this.dpi = dpi; }

    public String getCuentaBancaria() { return cuentaBancaria; }
    public void setCuentaBancaria(String cuentaBancaria) { this.cuentaBancaria = cuentaBancaria; }

    public String getContrasenaAgenciaVirtual() { return contrasenaAgenciaVirtual; }
    public void setContrasenaAgenciaVirtual(String contrasenaAgenciaVirtual) {
        this.contrasenaAgenciaVirtual = contrasenaAgenciaVirtual;
    }

    public String getContrasenaCorreo() { return contrasenaCorreo; }
    public void setContrasenaCorreo(String contrasenaCorreo) { this.contrasenaCorreo = contrasenaCorreo; }

    public String getContrasenaCgc() { return contrasenaCgc; }
    public void setContrasenaCgc(String contrasenaCgc) { this.contrasenaCgc = contrasenaCgc; }

    public String getContrasenaConsultaGeneral() { return contrasenaConsultaGeneral; }
    public void setContrasenaConsultaGeneral(String contrasenaConsultaGeneral) {
        this.contrasenaConsultaGeneral = contrasenaConsultaGeneral;
    }

    public String getContrasenaRegahe() { return contrasenaRegahe; }
    public void setContrasenaRegahe(String contrasenaRegahe) { this.contrasenaRegahe = contrasenaRegahe; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}
