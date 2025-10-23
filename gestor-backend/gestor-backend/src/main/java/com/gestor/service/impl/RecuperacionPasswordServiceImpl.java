// RecuperacionPasswordServiceImpl.java
package com.gestor.service.impl;

import com.gestor.entity.PasswordResetToken;
import com.gestor.entity.Usuario;
import com.gestor.exception.BadRequestException;
import com.gestor.exception.NotFoundException;
import com.gestor.repository.PasswordResetTokenRepository;
import com.gestor.repository.UsuarioRepository;
import com.gestor.service.RecuperacionPasswordService;
import com.gestor.service.security.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class RecuperacionPasswordServiceImpl implements RecuperacionPasswordService {

    private final UsuarioRepository usuarioRepo;
    private final PasswordResetTokenRepository tokenRepo;
    private final PasswordService passwordService;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public RecuperacionPasswordServiceImpl(UsuarioRepository usuarioRepo,
                                           PasswordResetTokenRepository tokenRepo,
                                           PasswordService passwordService) {
        this.usuarioRepo = usuarioRepo;
        this.tokenRepo = tokenRepo;
        this.passwordService = passwordService;
    }

    @Override
    @Transactional
    public UUID generarTokenRecuperacion(String email) {
        // Tarea 5: Validar que el email exista en la BD
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No existe un usuario con ese email"));

        // Eliminar tokens anteriores del usuario (por seguridad)
        tokenRepo.deleteByUsuario_IdUsuario(usuario.getIdUsuario());

        // Tarea 3: Generar UUID como token y guardarlo con expiración de 1 hora
        PasswordResetToken resetToken = new PasswordResetToken();
        UUID nuevoToken = UUID.randomUUID();
        resetToken.setUsuario(usuario);
        resetToken.setToken(nuevoToken);
        resetToken.setExpiresAt(OffsetDateTime.now().plusHours(1)); // Expira en 1 hora
        resetToken.setCreatedAt(OffsetDateTime.now());

        System.out.println("🔍 DEBUG - Token generado: " + nuevoToken);
        System.out.println("🔍 DEBUG - Expira en: " + resetToken.getExpiresAt());
        System.out.println("🔍 DEBUG - Usuario ID: " + usuario.getIdUsuario());

        resetToken = tokenRepo.save(resetToken);

        System.out.println("🔍 DEBUG - Token guardado en BD: " + resetToken.getToken());
        System.out.println("🔍 DEBUG - Token ID en BD: " + resetToken.getIdPasswordResetToken());

        return resetToken.getToken();
    }

    @Override
    public boolean validarToken(UUID token) {
        // Tarea 6: Validar que el token no esté expirado ni usado
        System.out.println("🔍 DEBUG - Buscando token: " + token);

        PasswordResetToken resetToken = tokenRepo.findByToken(token)
                .orElse(null);

        if (resetToken == null) {
            System.out.println("❌ DEBUG - Token NO encontrado en BD");
            return false;
        }

        System.out.println("✅ DEBUG - Token encontrado en BD");
        System.out.println("🔍 DEBUG - UsedAt: " + resetToken.getUsedAt());
        System.out.println("🔍 DEBUG - ExpiresAt: " + resetToken.getExpiresAt());
        System.out.println("🔍 DEBUG - Hora actual: " + OffsetDateTime.now());

        // Verificar si ya fue usado
        if (resetToken.getUsedAt() != null) {
            System.out.println("❌ DEBUG - Token ya fue usado");
            return false;
        }

        // Verificar si está expirado
        if (resetToken.getExpiresAt().isBefore(OffsetDateTime.now())) {
            System.out.println("❌ DEBUG - Token expirado");
            return false;
        }

        System.out.println("✅ DEBUG - Token válido");
        return true;
    }

    @Override
    @Transactional
    public void restablecerContrasena(UUID token, String nuevaContrasena) {
        // Validar el token
        if (!validarToken(token)) {
            throw new BadRequestException("El token es inválido, está expirado o ya fue usado");
        }

        // Validar fortaleza de la contraseña
        if (!passwordService.esContrasenaValida(nuevaContrasena)) {
            throw new BadRequestException(passwordService.obtenerRequisitosContrasena());
        }

        // Obtener el token
        PasswordResetToken resetToken = tokenRepo.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Token no encontrado"));

        // Obtener el usuario
        Usuario usuario = resetToken.getUsuario();

        // Tarea 8: Hashear la nueva contraseña con BCrypt antes de guardarla
        String passwordHasheada = passwordService.hashPassword(nuevaContrasena);
        usuario.setPassword(passwordHasheada);
        usuarioRepo.save(usuario);

        // Tarea 9: Marcar el token como usado
        resetToken.setUsedAt(OffsetDateTime.now());
        tokenRepo.save(resetToken);

        System.out.println("✅ Contraseña restablecida exitosamente para usuario: " + usuario.getUsuario());
    }

    @Override
    public boolean enviarEmailRecuperacion(String email, UUID token) {
        // Tarea 4: Implementar envío de email
        if (mailSender == null) {
            System.out.println("⚠️ JavaMailSender no configurado. No se puede enviar email.");
            System.out.println("📧 Token generado: " + token);
            System.out.println("🔗 URL de recuperación: http://localhost:8080/recuperar-contrasena?token=" + token);
            return false;
        }

        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Gestor SAT - Recuperación de Contraseña");
            message.setText(
                    "Hola " + usuario.getUsuario() + ",\n\n" +
                    "Has solicitado restablecer tu contraseña.\n\n" +
                    "Usa el siguiente token para restablecer tu contraseña:\n" +
                    token + "\n\n" +
                    "O accede a este enlace:\n" +
                    "http://localhost:8080/recuperar-contrasena?token=" + token + "\n\n" +
                    "Este token expirará en 1 hora.\n\n" +
                    "Si no solicitaste este cambio, ignora este mensaje.\n\n" +
                    "---\n" +
                    "Gestor SAT - Sistema de Gestión de Trámites"
            );

            mailSender.send(message);
            System.out.println("✅ Email de recuperación enviado a: " + email);
            return true;

        } catch (Exception e) {
            System.err.println("❌ Error al enviar email: " + e.getMessage());
            System.out.println("📧 Token generado: " + token);
            return false;
        }
    }
}
