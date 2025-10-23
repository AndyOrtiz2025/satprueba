// BusquedaResultadoDTO.java
package com.gestor.dto;

import java.time.LocalDate;

/**
 * DTO para CU-SAT011: Resultado de búsqueda avanzada
 * Combina información de Cliente, Tramite y EstadoTramite
 */
public class BusquedaResultadoDTO {

    // Información del Estado Tramite
    private Long estadoTramiteId;
    private LocalDate fechaInicio;
    private String estado;

    // Información del Cliente
    private Long clienteId;
    private String nombreCliente;
    private String dpiCliente;
    private String telefonoCliente;

    // Información del Tramite
    private Long tramiteId;
    private String nombreTramite;
    private String descripcionTramite;
    private String tipoTramite; // Nombre del tipo de trámite (SAT, RENAP, etc.)

    // Constructores
    public BusquedaResultadoDTO() {}

    public BusquedaResultadoDTO(Long estadoTramiteId, LocalDate fechaInicio, String estado,
                                Long clienteId, String nombreCliente, String dpiCliente,
                                String telefonoCliente, Long tramiteId, String nombreTramite,
                                String descripcionTramite, String tipoTramite) {
        this.estadoTramiteId = estadoTramiteId;
        this.fechaInicio = fechaInicio;
        this.estado = estado;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.dpiCliente = dpiCliente;
        this.telefonoCliente = telefonoCliente;
        this.tramiteId = tramiteId;
        this.nombreTramite = nombreTramite;
        this.descripcionTramite = descripcionTramite;
        this.tipoTramite = tipoTramite;
    }

    // Getters y Setters
    public Long getEstadoTramiteId() { return estadoTramiteId; }
    public void setEstadoTramiteId(Long estadoTramiteId) { this.estadoTramiteId = estadoTramiteId; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getDpiCliente() { return dpiCliente; }
    public void setDpiCliente(String dpiCliente) { this.dpiCliente = dpiCliente; }

    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }

    public Long getTramiteId() { return tramiteId; }
    public void setTramiteId(Long tramiteId) { this.tramiteId = tramiteId; }

    public String getNombreTramite() { return nombreTramite; }
    public void setNombreTramite(String nombreTramite) { this.nombreTramite = nombreTramite; }

    public String getDescripcionTramite() { return descripcionTramite; }
    public void setDescripcionTramite(String descripcionTramite) { this.descripcionTramite = descripcionTramite; }

    public String getTipoTramite() { return tipoTramite; }
    public void setTipoTramite(String tipoTramite) { this.tipoTramite = tipoTramite; }
}
