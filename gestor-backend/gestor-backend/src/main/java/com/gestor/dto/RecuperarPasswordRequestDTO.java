// RecuperarPasswordRequestDTO.java
package com.gestor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para CU-SAT013: Solicitar recuperación de contraseña
 */
public class RecuperarPasswordRequestDTO {

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    private String email;

    public RecuperarPasswordRequestDTO() {}

    public RecuperarPasswordRequestDTO(String email) {
        this.email = email;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
