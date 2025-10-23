// RecuperacionPasswordService.java
package com.gestor.service;

import java.util.UUID;

/**
 * Servicio para CU-SAT013: Recuperación de Contraseña
 */
public interface RecuperacionPasswordService {

    /**
     * Genera un token de recuperación para un email
     * @param email email del usuario
     * @return UUID del token generado
     */
    UUID generarTokenRecuperacion(String email);

    /**
     * Valida que un token sea válido (no expirado ni usado)
     * @param token UUID del token
     * @return true si el token es válido
     */
    boolean validarToken(UUID token);

    /**
     * Restablece la contraseña de un usuario usando un token válido
     * @param token UUID del token
     * @param nuevaContrasena nueva contraseña
     */
    void restablecerContrasena(UUID token, String nuevaContrasena);

    /**
     * Envía un email de recuperación con el token
     * @param email email del usuario
     * @param token UUID del token
     * @return true si se envió correctamente
     */
    boolean enviarEmailRecuperacion(String email, UUID token);
}
