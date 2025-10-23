package com.gestor.controller;

import com.gestor.dto.AuthRequestDTO;
import com.gestor.dto.AuthResponseDTO;
import com.gestor.dto.RegisterRequestDTO;
import com.gestor.dto.RecuperarPasswordRequestDTO;
import com.gestor.dto.RestablecerPasswordRequestDTO;
import com.gestor.entity.Usuario;
import com.gestor.repository.UsuarioRepository;
import com.gestor.service.RecuperacionPasswordService;
import com.gestor.util.ApiResponse;
import com.gestor.util.PasswordUtil;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UsuarioRepository repo;
  private final RecuperacionPasswordService recuperacionPasswordService;

  public AuthController(UsuarioRepository repo, RecuperacionPasswordService recuperacionPasswordService) {
    this.repo = repo;
    this.recuperacionPasswordService = recuperacionPasswordService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthRequestDTO req){
    Usuario u = repo.findByUsuario(req.username()).orElse(null);
    if (u != null && PasswordUtil.matches(req.password(), u.getPassword())) {
      return ResponseEntity.ok(new AuthResponseDTO("dummy-token"));
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(Map.of("error","Credenciales inválidas"));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequestDTO req){
    if (req.username()==null || req.username().isBlank()
        || req.password()==null || req.password().isBlank()) {
      return ResponseEntity.badRequest()
          .body(Map.of("error","username y password son requeridos"));
    }
    if (repo.findByUsuario(req.username().trim()).isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(Map.of("error","username ya existe"));
    }
    Usuario u = new Usuario();
    u.setUsuario(req.username().trim());
    u.setPassword(PasswordUtil.hash(req.password()));
    // Establecer email por defecto (requerido en la nueva estructura)
    u.setEmail(req.username().trim() + "@temp.com");
    repo.save(u);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new AuthResponseDTO("dummy-token"));
  }

  // === NUEVOS ENDPOINTS PARA CU-SAT013: Recuperación de Contraseña ===

  /**
   * Solicita la recuperación de contraseña (envía email con token)
   * POST /api/auth/recuperar-contrasena
   */
  @PostMapping("/recuperar-contrasena")
  public ResponseEntity<ApiResponse<String>> recuperarContrasena(@Valid @RequestBody RecuperarPasswordRequestDTO dto) {
    try {
      // Generar token
      UUID token = recuperacionPasswordService.generarTokenRecuperacion(dto.getEmail());

      // Intentar enviar email
      boolean emailEnviado = recuperacionPasswordService.enviarEmailRecuperacion(dto.getEmail(), token);

      if (emailEnviado) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Se ha enviado un email con las instrucciones para recuperar tu contraseña",
                null
        ));
      } else {
        // Si no se pudo enviar el email, devolver el token en la respuesta (solo para desarrollo)
        return ResponseEntity.ok(ApiResponse.ok(
                "Token generado (email no configurado): " + token,
                token.toString()
        ));
      }
    } catch (Exception e) {
      return ResponseEntity.ok(ApiResponse.ok(
              "Si el email existe en el sistema, recibirás las instrucciones de recuperación",
              null
      ));
    }
  }

  /**
   * Valida si un token de recuperación es válido
   * GET /api/auth/validar-token?token={uuid}
   */
  @GetMapping("/validar-token")
  public ResponseEntity<ApiResponse<Boolean>> validarToken(@RequestParam UUID token) {
    boolean esValido = recuperacionPasswordService.validarToken(token);
    if (esValido) {
      return ResponseEntity.ok(ApiResponse.ok("Token válido", true));
    } else {
      return ResponseEntity.ok(ApiResponse.ok("Token inválido, expirado o ya usado", false));
    }
  }

  /**
   * Restablece la contraseña usando un token válido
   * POST /api/auth/restablecer-contrasena
   */
  @PostMapping("/restablecer-contrasena")
  public ResponseEntity<ApiResponse<Void>> restablecerContrasena(@Valid @RequestBody RestablecerPasswordRequestDTO dto) {
    recuperacionPasswordService.restablecerContrasena(dto.getToken(), dto.getNuevaContrasena());
    return ResponseEntity.ok(ApiResponse.ok("Contraseña restablecida exitosamente", null));
  }
}
