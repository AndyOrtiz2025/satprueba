// TramiteCreateDTO.java - DTO para crear trámite
package com.gestor.dto;

import jakarta.validation.constraints.*;

public class TramiteCreateDTO {

    @NotBlank(message = "El nombre del trámite es requerido")
    @Size(max = 50, message = "El nombre del trámite no puede exceder 50 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcion;

    @NotNull(message = "El ID del tipo de trámite es requerido")
    private Long idTipoTramite;

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Long getIdTipoTramite() { return idTipoTramite; }
    public void setIdTipoTramite(Long idTipoTramite) { this.idTipoTramite = idTipoTramite; }
}
