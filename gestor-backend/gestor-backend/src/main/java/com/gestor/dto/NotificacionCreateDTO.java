// NotificacionCreateDTO.java
package com.gestor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para CU-SAT004: Generar Notificaciones
 * Se usa para crear nuevas notificaciones
 */
public class NotificacionCreateDTO {

    @NotNull(message = "El ID del usuario es requerido")
    private Long idUsuario;

    @NotBlank(message = "El mensaje es requerido")
    private String mensaje;

    @NotBlank(message = "El tipo de notificación es requerido")
    private String tipo; // INFO, ADVERTENCIA, ERROR, EXITO

    private Boolean enviarEmail = false; // Si se debe enviar por email

    // Constructor vacío
    public NotificacionCreateDTO() {}

    // Constructor
    public NotificacionCreateDTO(Long idUsuario, String mensaje, String tipo) {
        this.idUsuario = idUsuario;
        this.mensaje = mensaje;
        this.tipo = tipo;
    }

    // Getters y Setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Boolean getEnviarEmail() { return enviarEmail; }
    public void setEnviarEmail(Boolean enviarEmail) { this.enviarEmail = enviarEmail; }
}
