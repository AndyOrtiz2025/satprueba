// TipoNotificacion.java
package com.gestor.entity;

/**
 * Enum para CU-SAT004: Generar Notificaciones
 * Define los tipos de notificaciones que se pueden crear en el sistema
 */
public enum TipoNotificacion {
    INFO,
    ADVERTENCIA,
    ERROR,
    EXITO;

    /**
     * Verifica si un valor de texto corresponde a un tipo válido
     * @param tipo texto a validar
     * @return true si es válido
     */
    public static boolean esValido(String tipo) {
        if (tipo == null) return false;
        try {
            TipoNotificacion.valueOf(tipo.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
