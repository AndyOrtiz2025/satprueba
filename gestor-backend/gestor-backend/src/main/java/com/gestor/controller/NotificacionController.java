// NotificacionController.java
package com.gestor.controller;

import com.gestor.dto.NotificacionCreateDTO;
import com.gestor.dto.NotificacionDTO;
import com.gestor.service.NotificacionService;
import com.gestor.util.ApiResponse;
import com.gestor.util.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para CU-SAT004: Generar Notificaciones
 */
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    /**
     * Crea una nueva notificación
     * POST /api/notificaciones
     */
    @PostMapping
    public ResponseEntity<ApiResponse<NotificacionDTO>> crearNotificacion(
            @Valid @RequestBody NotificacionCreateDTO dto) {
        NotificacionDTO notificacion = notificacionService.crearNotificacion(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Notificación creada exitosamente", notificacion));
    }

    /**
     * Obtiene las notificaciones de un usuario con paginación
     * GET /api/notificaciones/usuario/{id}
     */
    @GetMapping("/usuario/{id}")
    public ResponseEntity<ApiResponse<PageResponse<NotificacionDTO>>> obtenerNotificacionesUsuario(
            @PathVariable Long id,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        PageResponse<NotificacionDTO> notificaciones = notificacionService
                .obtenerNotificacionesUsuario(id, page, size);
        return ResponseEntity.ok(ApiResponse.ok("Notificaciones obtenidas exitosamente", notificaciones));
    }

    /**
     * Obtiene las notificaciones no leídas de un usuario
     * GET /api/notificaciones/usuario/{id}/no-leidas
     */
    @GetMapping("/usuario/{id}/no-leidas")
    public ResponseEntity<ApiResponse<List<NotificacionDTO>>> obtenerNotificacionesNoLeidas(
            @PathVariable Long id) {
        List<NotificacionDTO> notificaciones = notificacionService.obtenerNotificacionesNoLeidas(id);
        return ResponseEntity.ok(ApiResponse.ok("Notificaciones no leídas obtenidas", notificaciones));
    }

    /**
     * Cuenta las notificaciones no leídas de un usuario
     * GET /api/notificaciones/usuario/{id}/count
     */
    @GetMapping("/usuario/{id}/count")
    public ResponseEntity<ApiResponse<Long>> contarNotificacionesNoLeidas(@PathVariable Long id) {
        long count = notificacionService.contarNotificacionesNoLeidas(id);
        return ResponseEntity.ok(ApiResponse.ok("Cantidad de notificaciones no leídas", count));
    }

    /**
     * Marca una notificación como leída
     * PUT /api/notificaciones/{id}/marcar-leida
     */
    @PutMapping("/{id}/marcar-leida")
    public ResponseEntity<ApiResponse<NotificacionDTO>> marcarComoLeida(@PathVariable Long id) {
        NotificacionDTO notificacion = notificacionService.marcarComoLeida(id);
        return ResponseEntity.ok(ApiResponse.ok("Notificación marcada como leída", notificacion));
    }

    /**
     * Envía una notificación por email
     * POST /api/notificaciones/{id}/enviar-email
     */
    @PostMapping("/{id}/enviar-email")
    public ResponseEntity<ApiResponse<Boolean>> enviarPorEmail(@PathVariable Long id) {
        boolean enviado = notificacionService.enviarNotificacionPorEmail(id);
        if (enviado) {
            return ResponseEntity.ok(ApiResponse.ok("Email enviado exitosamente", true));
        } else {
            return ResponseEntity.ok(ApiResponse.ok("No se pudo enviar el email (verifica la configuración)", false));
        }
    }
}
