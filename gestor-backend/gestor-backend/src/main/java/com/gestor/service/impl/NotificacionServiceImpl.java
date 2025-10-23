// NotificacionServiceImpl.java
package com.gestor.service.impl;

import com.gestor.dto.NotificacionCreateDTO;
import com.gestor.dto.NotificacionDTO;
import com.gestor.entity.Notificacion;
import com.gestor.entity.TipoNotificacion;
import com.gestor.entity.Usuario;
import com.gestor.exception.BadRequestException;
import com.gestor.exception.NotFoundException;
import com.gestor.mapper.NotificacionMapper;
import com.gestor.repository.NotificacionRepository;
import com.gestor.repository.UsuarioRepository;
import com.gestor.service.NotificacionService;
import com.gestor.util.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepo;
    private final UsuarioRepository usuarioRepo;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepo,
                                   UsuarioRepository usuarioRepo) {
        this.notificacionRepo = notificacionRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public NotificacionDTO crearNotificacion(Long idUsuario, String mensaje, TipoNotificacion tipo) {
        // Validar usuario
        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Validar tipo
        if (tipo == null) {
            throw new BadRequestException("El tipo de notificación es requerido");
        }

        // Crear notificación
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setMensaje(mensaje);
        notificacion.setTipo(tipo.name());
        notificacion.setLeida(false);
        notificacion.setFecha(LocalDateTime.now());
        notificacion.setFechaExpiracion(LocalDateTime.now().plusDays(30));
        notificacion.setExpirada(false);

        // Guardar en BD
        notificacion = notificacionRepo.save(notificacion);

        return NotificacionMapper.toDTO(notificacion);
    }

    @Override
    public NotificacionDTO crearNotificacion(NotificacionCreateDTO dto) {
        // Validar tipo de notificación
        if (!TipoNotificacion.esValido(dto.getTipo())) {
            throw new BadRequestException("Tipo de notificación inválido. Valores permitidos: INFO, ADVERTENCIA, ERROR, EXITO");
        }

        // Crear notificación
        TipoNotificacion tipo = TipoNotificacion.valueOf(dto.getTipo().toUpperCase());
        NotificacionDTO notificacionCreada = crearNotificacion(dto.getIdUsuario(), dto.getMensaje(), tipo);

        // Enviar por email si se solicita
        if (dto.getEnviarEmail() != null && dto.getEnviarEmail()) {
            enviarNotificacionPorEmail(notificacionCreada.getId());
        }

        return notificacionCreada;
    }

    @Override
    public boolean enviarNotificacionPorEmail(Long idNotificacion) {
        // Verificar si JavaMailSender está configurado
        if (mailSender == null) {
            System.out.println("⚠️ JavaMailSender no está configurado. No se puede enviar email.");
            return false;
        }

        // Obtener notificación
        Notificacion notificacion = notificacionRepo.findById(idNotificacion)
                .orElseThrow(() -> new NotFoundException("Notificación no encontrada"));

        Usuario usuario = notificacion.getUsuario();

        // Validar que el usuario tenga email
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            System.out.println("⚠️ El usuario no tiene email configurado.");
            return false;
        }

        try {
            // Crear mensaje
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(usuario.getEmail());
            message.setSubject("Gestor SAT - " + notificacion.getTipo());
            message.setText(
                    "Hola " + usuario.getUsuario() + ",\n\n" +
                    "Tienes una nueva notificación:\n\n" +
                    notificacion.getMensaje() + "\n\n" +
                    "Tipo: " + notificacion.getTipo() + "\n" +
                    "Fecha: " + notificacion.getFecha() + "\n\n" +
                    "---\n" +
                    "Gestor SAT - Sistema de Gestión de Trámites"
            );

            // Enviar email
            mailSender.send(message);
            System.out.println("✅ Email enviado a: " + usuario.getEmail());
            return true;

        } catch (Exception e) {
            System.err.println("❌ Error al enviar email: " + e.getMessage());
            return false;
        }
    }

    @Override
    public PageResponse<NotificacionDTO> obtenerNotificacionesUsuario(Long idUsuario, int page, int size) {
        // Validar usuario
        if (!usuarioRepo.existsById(idUsuario)) {
            throw new NotFoundException("Usuario no encontrado");
        }

        // Crear paginación (ordenar por fecha descendente)
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by("fecha").descending()
        );

        // Obtener notificaciones (solo no expiradas)
        Page<Notificacion> notificaciones = notificacionRepo
                .findByUsuario_IdUsuarioAndExpiradaFalse(idUsuario, pageable);

        // Mapear a DTO
        List<NotificacionDTO> notificacionDTOs = notificaciones.getContent().stream()
                .map(NotificacionMapper::toDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                notificacionDTOs,
                notificaciones.getTotalElements(),
                notificaciones.getTotalPages(),
                notificaciones.getNumber(),
                notificaciones.getSize()
        );
    }

    @Override
    public List<NotificacionDTO> obtenerNotificacionesNoLeidas(Long idUsuario) {
        // Validar usuario
        if (!usuarioRepo.existsById(idUsuario)) {
            throw new NotFoundException("Usuario no encontrado");
        }

        // Obtener notificaciones no leídas y no expiradas
        List<Notificacion> notificaciones = notificacionRepo
                .findByUsuario_IdUsuarioAndLeidaFalseAndExpiradaFalse(idUsuario);

        return notificaciones.stream()
                .map(NotificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificacionDTO marcarComoLeida(Long idNotificacion) {
        // Obtener notificación
        Notificacion notificacion = notificacionRepo.findById(idNotificacion)
                .orElseThrow(() -> new NotFoundException("Notificación no encontrada"));

        // Marcar como leída
        notificacion.setLeida(true);
        notificacion = notificacionRepo.save(notificacion);

        return NotificacionMapper.toDTO(notificacion);
    }

    @Override
    public long contarNotificacionesNoLeidas(Long idUsuario) {
        // Validar usuario
        if (!usuarioRepo.existsById(idUsuario)) {
            throw new NotFoundException("Usuario no encontrado");
        }

        return notificacionRepo.countByUsuario_IdUsuarioAndLeidaFalseAndExpiradaFalse(idUsuario);
    }
}
