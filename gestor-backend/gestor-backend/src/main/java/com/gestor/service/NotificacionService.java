// NotificacionService.java
package com.gestor.service;

import com.gestor.dto.NotificacionCreateDTO;
import com.gestor.dto.NotificacionDTO;
import com.gestor.entity.TipoNotificacion;
import com.gestor.util.PageResponse;

import java.util.List;

/**
 * Servicio para CU-SAT004: Generar Notificaciones
 */
public interface NotificacionService {

    /**
     * Crea una nueva notificación para un usuario
     * @param idUsuario ID del usuario
     * @param mensaje Mensaje de la notificación
     * @param tipo Tipo de notificación
     * @return NotificacionDTO creada
     */
    NotificacionDTO crearNotificacion(Long idUsuario, String mensaje, TipoNotificacion tipo);

    /**
     * Crea una nueva notificación desde un DTO
     * @param dto datos de la notificación
     * @return NotificacionDTO creada
     */
    NotificacionDTO crearNotificacion(NotificacionCreateDTO dto);

    /**
     * Envía una notificación por email
     * @param idNotificacion ID de la notificación a enviar
     * @return true si se envió correctamente
     */
    boolean enviarNotificacionPorEmail(Long idNotificacion);

    /**
     * Obtiene las notificaciones de un usuario (no expiradas)
     * @param idUsuario ID del usuario
     * @param page página
     * @param size tamaño de página
     * @return PageResponse con notificaciones
     */
    PageResponse<NotificacionDTO> obtenerNotificacionesUsuario(Long idUsuario, int page, int size);

    /**
     * Obtiene las notificaciones no leídas de un usuario
     * @param idUsuario ID del usuario
     * @return lista de notificaciones no leídas
     */
    List<NotificacionDTO> obtenerNotificacionesNoLeidas(Long idUsuario);

    /**
     * Marca una notificación como leída
     * @param idNotificacion ID de la notificación
     * @return NotificacionDTO actualizada
     */
    NotificacionDTO marcarComoLeida(Long idNotificacion);

    /**
     * Cuenta las notificaciones no leídas de un usuario
     * @param idUsuario ID del usuario
     * @return cantidad de notificaciones no leídas
     */
    long contarNotificacionesNoLeidas(Long idUsuario);
}
