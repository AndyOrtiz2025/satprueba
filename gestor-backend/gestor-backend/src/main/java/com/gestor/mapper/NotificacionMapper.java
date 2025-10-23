// NotificacionMapper.java
package com.gestor.mapper;

import com.gestor.dto.NotificacionDTO;
import com.gestor.entity.Notificacion;

/**
 * Mapper para CU-SAT004: Generar Notificaciones
 * Convierte entre entidad Notificacion y NotificacionDTO
 */
public class NotificacionMapper {

    public static NotificacionDTO toDTO(Notificacion entity) {
        if (entity == null) return null;

        return new NotificacionDTO(
                entity.getIdNotificacion(),
                entity.getUsuario().getIdUsuario(),
                entity.getUsuario().getUsuario(), // nombre de usuario
                entity.getMensaje(),
                entity.getTipo(),
                entity.getLeida(),
                entity.getFecha(),
                entity.getFechaExpiracion(),
                entity.getExpirada()
        );
    }
}
