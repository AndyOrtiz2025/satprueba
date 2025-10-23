// BusquedaAvanzadaDTO.java
package com.gestor.dto;

import jakarta.validation.constraints.Min;
import java.time.LocalDate;

/**
 * DTO para CU-SAT011: Buscador Avanzado
 * Contiene filtros opcionales para búsqueda dinámica
 */
public class BusquedaAvanzadaDTO {

    // Filtro por nombre del cliente (búsqueda parcial)
    private String nombre;

    // Filtro por DPI del cliente (búsqueda exacta)
    private String dpi;

    // Filtro por rango de fechas (fecha inicio del trámite)
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // Filtro por estado del trámite (PENDIENTE, EN_PROCESO, FINALIZADO, RECHAZADO)
    private String estado;

    // Paginación
    @Min(value = 0, message = "La página debe ser mayor o igual a 0")
    private Integer page = 0;

    @Min(value = 1, message = "El tamaño debe ser al menos 1")
    private Integer size = 10;

    // Ordenamiento (campo por el que ordenar)
    private String sortBy = "fechaTramite"; // Por defecto ordena por fecha

    // Dirección del ordenamiento (ASC o DESC)
    private String sortDir = "DESC"; // Por defecto descendente (más recientes primero)

    // Constructores
    public BusquedaAvanzadaDTO() {}

    public BusquedaAvanzadaDTO(String nombre, String dpi, LocalDate fechaInicio,
                               LocalDate fechaFin, String estado) {
        this.nombre = nombre;
        this.dpi = dpi;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    // Método para validar que al menos un filtro esté presente
    public boolean tieneAlMenosUnFiltro() {
        return (nombre != null && !nombre.isBlank()) ||
               (dpi != null && !dpi.isBlank()) ||
               fechaInicio != null ||
               fechaFin != null ||
               (estado != null && !estado.isBlank());
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDpi() { return dpi; }
    public void setDpi(String dpi) { this.dpi = dpi; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortDir() { return sortDir; }
    public void setSortDir(String sortDir) { this.sortDir = sortDir; }

    @Override
    public String toString() {
        return "BusquedaAvanzadaDTO{" +
                "nombre='" + nombre + '\'' +
                ", dpi='" + dpi + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estado='" + estado + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", sortBy='" + sortBy + '\'' +
                ", sortDir='" + sortDir + '\'' +
                '}';
    }
}
