// PasswordService.java
package com.gestor.service.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio para CU-SAT013: Recuperación de Contraseña
 * Manejo seguro de contraseñas usando BCrypt
 */
@Service
public class PasswordService {

    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordService() {
        // BCrypt con strength 12 (más seguro que el default 10)
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    /**
     * Hashea una contraseña usando BCrypt
     * @param plainPassword contraseña en texto plano
     * @return contraseña hasheada
     */
    public String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        return passwordEncoder.encode(plainPassword);
    }

    /**
     * Verifica si una contraseña coincide con su hash
     * @param plainPassword contraseña en texto plano
     * @param hashedPassword contraseña hasheada
     * @return true si coinciden
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    /**
     * Valida la fortaleza de una contraseña
     * Debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número
     * @param password contraseña a validar
     * @return true si es válida
     */
    public boolean esContrasenaValida(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean tieneMayuscula = password.chars().anyMatch(Character::isUpperCase);
        boolean tieneMinuscula = password.chars().anyMatch(Character::isLowerCase);
        boolean tieneNumero = password.chars().anyMatch(Character::isDigit);

        return tieneMayuscula && tieneMinuscula && tieneNumero;
    }

    /**
     * Obtiene un mensaje descriptivo de los requisitos de contraseña
     */
    public String obtenerRequisitosContrasena() {
        return "La contraseña debe tener al menos 8 caracteres, una letra mayúscula, una minúscula y un número";
    }
}
