/**
 * Configuración del Frontend - Gestor SAT
 *
 * USAR_MOCK:
 * - true: Usa datos simulados (localStorage) - Para desarrollo sin backend
 * - false: Conecta al backend real (Spring Boot) - Para producción
 */

const CONFIG = {
  // ⚙️ Cambiado a false para conectar con el backend real
  USAR_MOCK: false,

  // URL del backend (solo se usa cuando USAR_MOCK = false)
  API_BASE_URL: 'http://localhost:8080/api',

  // Configuración de la app
  APP_NAME: 'Gestor SAT',
  VERSION: '1.0.0'
};

// Exportar configuración
window.CONFIG = CONFIG;
