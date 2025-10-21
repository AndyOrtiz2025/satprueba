// EstadoTramiteDTO.java
package com.gestor.dto;

import java.time.LocalDate;

public class EstadoTramiteDTO {
    private Long id;
    private Long tramiteId;
    private Long clienteId;
    private String nombreTramite;
    private String nombreCliente;
    private LocalDate fechaInicio;
    private String estado; // PENDIENTE | EN_PROCESO | FINALIZADO | RECHAZADO

    // getters/setters
    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }
    public Long getTramiteId(){ return tramiteId; }
    public void setTramiteId(Long tramiteId){ this.tramiteId = tramiteId; }
    public Long getClienteId(){ return clienteId; }
    public void setClienteId(Long clienteId){ this.clienteId = clienteId; }
    public String getNombreTramite(){ return nombreTramite; }
    public void setNombreTramite(String nombreTramite){ this.nombreTramite = nombreTramite; }
    public String getNombreCliente(){ return nombreCliente; }
    public void setNombreCliente(String nombreCliente){ this.nombreCliente = nombreCliente; }
    public LocalDate getFechaInicio(){ return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio){ this.fechaInicio = fechaInicio; }
    public String getEstado(){ return estado; }
    public void setEstado(String estado){ this.estado = estado; }
}
