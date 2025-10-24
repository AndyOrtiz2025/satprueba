package com.gestor.dto;

import java.time.LocalDateTime;

public class ArchivoDTO {
    private Long idArchivos;
    private Long idCliente;
    private Long idTramite;
    private String nombreArchivo;
    private String ruta;
    private LocalDateTime fechaSubida;
    private Long tamanio; // en bytes
    private String tipoContenido;

    // Getters y Setters
    public Long getIdArchivos() { return idArchivos; }
    public void setIdArchivos(Long idArchivos) { this.idArchivos = idArchivos; }

    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public Long getIdTramite() { return idTramite; }
    public void setIdTramite(Long idTramite) { this.idTramite = idTramite; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }

    public LocalDateTime getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDateTime fechaSubida) { this.fechaSubida = fechaSubida; }

    public Long getTamanio() { return tamanio; }
    public void setTamanio(Long tamanio) { this.tamanio = tamanio; }

    public String getTipoContenido() { return tipoContenido; }
    public void setTipoContenido(String tipoContenido) { this.tipoContenido = tipoContenido; }
}
