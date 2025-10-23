// RestablecerPasswordRequestDTO.java
package com.gestor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * DTO para CU-SAT013: Restablecer contraseña con token
 */
public class RestablecerPasswordRequestDTO {

    @NotNull(message = "El token es requerido")
    private UUID token;

    @NotBlank(message = "La nueva contraseña es requerida")
    private String nuevaContrasena;

    public RestablecerPasswordRequestDTO() {}

    public RestablecerPasswordRequestDTO(UUID token, String nuevaContrasena) {
        this.token = token;
        this.nuevaContrasena = nuevaContrasena;
    }

    public UUID getToken() { return token; }
    public void setToken(UUID token) { this.token = token; }

    public String getNuevaContrasena() { return nuevaContrasena; }
    public void setNuevaContrasena(String nuevaContrasena) { this.nuevaContrasena = nuevaContrasena; }
}
