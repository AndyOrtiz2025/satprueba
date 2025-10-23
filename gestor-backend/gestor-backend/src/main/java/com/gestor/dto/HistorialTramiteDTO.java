// HistorialTramiteDTO.java
package com.gestor.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para CU-SAT005: Historial de Trámites
 * Contiene información completa del trámite, consulta y archivos asociados
 */
public class HistorialTramiteDTO {

    // Información de la consulta
    private Long idConsultaTramite;
    private LocalDate fechaTramite;
    private String estado; // INICIADO, PENDIENTE, TERMINADO
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Información del trámite
    private Long idTramite;
    private String nombreTramite;
    private String descripcionTramite;

    // Información del tipo de trámite
    private String tipoTramitePortal; // SAT, RENAP, IGSS, MINTRAB, OTRO
    private String tipoTramiteLink;

    // Archivos asociados
    private List<ArchivoDTO> archivos;

    // Constructor vacío
    public HistorialTramiteDTO() {}

    // Constructor completo
    public HistorialTramiteDTO(Long idConsultaTramite, LocalDate fechaTramite, String estado,
                               LocalDateTime createdAt, LocalDateTime updatedAt,
                               Long idTramite, String nombreTramite, String descripcionTramite,
                               String tipoTramitePortal, String tipoTramiteLink) {
        this.idConsultaTramite = idConsultaTramite;
        this.fechaTramite = fechaTramite;
        this.estado = estado;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.idTramite = idTramite;
        this.nombreTramite = nombreTramite;
        this.descripcionTramite = descripcionTramite;
        this.tipoTramitePortal = tipoTramitePortal;
        this.tipoTramiteLink = tipoTramiteLink;
    }

    // Getters y Setters
    public Long getIdConsultaTramite() { return idConsultaTramite; }
    public void setIdConsultaTramite(Long idConsultaTramite) { this.idConsultaTramite = idConsultaTramite; }

    public LocalDate getFechaTramite() { return fechaTramite; }
    public void setFechaTramite(LocalDate fechaTramite) { this.fechaTramite = fechaTramite; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getIdTramite() { return idTramite; }
    public void setIdTramite(Long idTramite) { this.idTramite = idTramite; }

    public String getNombreTramite() { return nombreTramite; }
    public void setNombreTramite(String nombreTramite) { this.nombreTramite = nombreTramite; }

    public String getDescripcionTramite() { return descripcionTramite; }
    public void setDescripcionTramite(String descripcionTramite) { this.descripcionTramite = descripcionTramite; }

    public String getTipoTramitePortal() { return tipoTramitePortal; }
    public void setTipoTramitePortal(String tipoTramitePortal) { this.tipoTramitePortal = tipoTramitePortal; }

    public String getTipoTramiteLink() { return tipoTramiteLink; }
    public void setTipoTramiteLink(String tipoTramiteLink) { this.tipoTramiteLink = tipoTramiteLink; }

    public List<ArchivoDTO> getArchivos() { return archivos; }
    public void setArchivos(List<ArchivoDTO> archivos) { this.archivos = archivos; }

    // Clase interna para representar archivos asociados
    public static class ArchivoDTO {
        private Long idArchivo;
        private String nombreArchivo;
        private String ruta;
        private LocalDateTime fechaSubida;

        public ArchivoDTO() {}

        public ArchivoDTO(Long idArchivo, String nombreArchivo, String ruta, LocalDateTime fechaSubida) {
            this.idArchivo = idArchivo;
            this.nombreArchivo = nombreArchivo;
            this.ruta = ruta;
            this.fechaSubida = fechaSubida;
        }

        public Long getIdArchivo() { return idArchivo; }
        public void setIdArchivo(Long idArchivo) { this.idArchivo = idArchivo; }

        public String getNombreArchivo() { return nombreArchivo; }
        public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

        public String getRuta() { return ruta; }
        public void setRuta(String ruta) { this.ruta = ruta; }

        public LocalDateTime getFechaSubida() { return fechaSubida; }
        public void setFechaSubida(LocalDateTime fechaSubida) { this.fechaSubida = fechaSubida; }
    }
}
