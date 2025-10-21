package com.gestor.controller;

import com.gestor.dto.AuthRequestDTO;
import com.gestor.dto.AuthResponseDTO;
import com.gestor.dto.RegisterRequestDTO;
import com.gestor.entity.Usuario;
import com.gestor.repository.UsuarioRepository;
import com.gestor.util.PasswordUtil;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UsuarioRepository repo;

  // Constructor explícito (por si no usas Lombok)
  public AuthController(UsuarioRepository repo) {
    this.repo = repo;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthRequestDTO req){
    Usuario u = repo.findByUsername(req.username()).orElse(null);
    if (u != null && PasswordUtil.matches(req.password(), u.getPasswordHash())) {
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
    if (repo.findByUsername(req.username().trim()).isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(Map.of("error","username ya existe"));
    }
    Usuario u = new Usuario();
    u.setUsername(req.username().trim());
    u.setPasswordHash(PasswordUtil.hash(req.password()));
    repo.save(u);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new AuthResponseDTO("dummy-token"));
  }
}
