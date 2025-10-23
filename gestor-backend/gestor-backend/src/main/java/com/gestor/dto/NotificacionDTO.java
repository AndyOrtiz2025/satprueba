// NotificacionDTO.java
package com.gestor.dto;

import java.time.LocalDateTime;

/**
 * DTO para CU-SAT004: Generar Notificaciones
 * Se usa para transferir información de notificaciones hacia el cliente
 */
public class NotificacionDTO {
    private Long id;
    private Long idUsuario;
    private String nombreUsuario;
    private String mensaje;
    private String tipo; // INFO, ADVERTENCIA, ERROR, EXITO
    private Boolean leida;
    private LocalDateTime fecha;
    private LocalDateTime fechaExpiracion;
    private Boolean expirada;

    // Constructor vacío
    public NotificacionDTO() {}

    // Constructor completo
    public NotificacionDTO(Long id, Long idUsuario, String nombreUsuario, String mensaje,
                           String tipo, Boolean leida, LocalDateTime fecha,
                           LocalDateTime fechaExpiracion, Boolean expirada) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.leida = leida;
        this.fecha = fecha;
        this.fechaExpiracion = fechaExpiracion;
        this.expirada = expirada;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Boolean getLeida() { return leida; }
    public void setLeida(Boolean leida) { this.leida = leida; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDateTime fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    public Boolean getExpirada() { return expirada; }
    public void setExpirada(Boolean expirada) { this.expirada = expirada; }
}
