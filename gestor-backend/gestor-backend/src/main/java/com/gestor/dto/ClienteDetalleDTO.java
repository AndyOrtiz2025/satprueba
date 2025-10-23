// ClienteDetalleDTO.java
package com.gestor.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para CU-SAT002: Consultar Cliente
 * Contiene información completa del cliente incluyendo trámites asociados
 */
public class ClienteDetalleDTO {

    // Información básica del cliente
    private Long idCliente;
    private String nombreCompleto;
    private String dpi;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String direccion;

    // Información de datos sensibles
    private String nit;
    private String nis;
    private String email;
    private String cuentaBancaria;

    // Información del usuario asociado
    private Long idUsuario;
    private String nombreUsuario;

    // Auditoría
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    // Trámites asociados
    private List<TramiteResumenDTO> tramites;

    // Estadísticas del cliente
    private Long totalTramites;
    private Long tramitesIniciados;
    private Long tramitesPendientes;
    private Long tramitesTerminados;

    // Constructor vacío
    public ClienteDetalleDTO() {}

    // Getters y Setters
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

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

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public List<TramiteResumenDTO> getTramites() { return tramites; }
    public void setTramites(List<TramiteResumenDTO> tramites) { this.tramites = tramites; }

    public Long getTotalTramites() { return totalTramites; }
    public void setTotalTramites(Long totalTramites) { this.totalTramites = totalTramites; }

    public Long getTramitesIniciados() { return tramitesIniciados; }
    public void setTramitesIniciados(Long tramitesIniciados) { this.tramitesIniciados = tramitesIniciados; }

    public Long getTramitesPendientes() { return tramitesPendientes; }
    public void setTramitesPendientes(Long tramitesPendientes) { this.tramitesPendientes = tramitesPendientes; }

    public Long getTramitesTerminados() { return tramitesTerminados; }
    public void setTramitesTerminados(Long tramitesTerminados) { this.tramitesTerminados = tramitesTerminados; }

    // Clase interna para resumen de trámites
    public static class TramiteResumenDTO {
        private Long idConsultaTramite;
        private Long idTramite;
        private String nombreTramite;
        private String tipoTramite;
        private LocalDate fechaTramite;
        private String estado;
        private LocalDateTime createdAt;

        public TramiteResumenDTO() {}

        public TramiteResumenDTO(Long idConsultaTramite, Long idTramite, String nombreTramite,
                                 String tipoTramite, LocalDate fechaTramite, String estado,
                                 LocalDateTime createdAt) {
            this.idConsultaTramite = idConsultaTramite;
            this.idTramite = idTramite;
            this.nombreTramite = nombreTramite;
            this.tipoTramite = tipoTramite;
            this.fechaTramite = fechaTramite;
            this.estado = estado;
            this.createdAt = createdAt;
        }

        public Long getIdConsultaTramite() { return idConsultaTramite; }
        public void setIdConsultaTramite(Long idConsultaTramite) { this.idConsultaTramite = idConsultaTramite; }

        public Long getIdTramite() { return idTramite; }
        public void setIdTramite(Long idTramite) { this.idTramite = idTramite; }

        public String getNombreTramite() { return nombreTramite; }
        public void setNombreTramite(String nombreTramite) { this.nombreTramite = nombreTramite; }

        public String getTipoTramite() { return tipoTramite; }
        public void setTipoTramite(String tipoTramite) { this.tipoTramite = tipoTramite; }

        public LocalDate getFechaTramite() { return fechaTramite; }
        public void setFechaTramite(LocalDate fechaTramite) { this.fechaTramite = fechaTramite; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }
}
