/**
 * Gestión de Autenticación y Sesión
 * Funciones compartidas para login, logout y verificación de sesión
 */

// Obtener datos del usuario actual
function obtenerUsuarioActual() {
  const idUsuario = localStorage.getItem('idUsuario');
  const username = localStorage.getItem('username');
  const email = localStorage.getItem('email');

  if (idUsuario) {
    return {
      id: parseInt(idUsuario),
      idUsuario: parseInt(idUsuario),
      username: username || 'Usuario',
      email: email || ''
    };
  }
  return null;
}

// Verificar si hay sesión activa
function verificarSesion() {
  const usuario = obtenerUsuarioActual();
  if (!usuario) {
    window.location.href = 'index.html';
    return false;
  }
  return true;
}

// Actualizar perfil de usuario en el sidebar
function actualizarPerfilUsuario() {
  const usuario = obtenerUsuarioActual();
  if (usuario) {
    const userNameEl = document.getElementById('user-name');
    const userEmailEl = document.getElementById('user-email');

    if (userNameEl) {
      userNameEl.textContent = usuario.username.toUpperCase();
    }
    if (userEmailEl) {
      userEmailEl.textContent = usuario.email;
    }
  }
}

// Función de logout con mensaje personalizado
function logout(event) {
  if (event) {
    event.preventDefault();
  }

  const usuario = obtenerUsuarioActual();
  const nombre = usuario ? usuario.username : 'Usuario';

  // Crear modal personalizado
  mostrarModalDespedida(nombre);
}

// Mostrar modal de despedida bonito
function mostrarModalDespedida(nombre) {
  // Crear elementos del modal
  const overlay = document.createElement('div');
  overlay.style.cssText = `
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.6);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 10000;
    animation: fadeIn 0.3s ease;
  `;

  const modal = document.createElement('div');
  modal.style.cssText = `
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 20px;
    padding: 40px;
    text-align: center;
    max-width: 400px;
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
    animation: slideIn 0.3s ease;
    color: white;
  `;

  modal.innerHTML = `
    <div style="font-size: 64px; margin-bottom: 20px; animation: wave 0.5s ease;">
      👋
    </div>
    <h2 style="margin: 0 0 15px 0; font-size: 28px; font-weight: 800; color: white;">
      ¡Nos vemos pronto, ${nombre}!
    </h2>
    <p style="margin: 0 0 30px 0; font-size: 16px; opacity: 0.95; line-height: 1.6; color: white;">
      Tu sesión se cerrará de forma segura.<br>
      Esperamos verte de nuevo muy pronto.
    </p>
    <button id="btn-confirmar-logout" style="
      background: white;
      color: #667eea;
      border: none;
      border-radius: 12px;
      padding: 14px 40px;
      font-size: 16px;
      font-weight: 700;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
    ">
      Aceptar
    </button>
  `;

  // Agregar estilos de animación
  const style = document.createElement('style');
  style.textContent = `
    @keyframes fadeIn {
      from { opacity: 0; }
      to { opacity: 1; }
    }
    @keyframes slideIn {
      from {
        transform: translateY(-50px);
        opacity: 0;
      }
      to {
        transform: translateY(0);
        opacity: 1;
      }
    }
    @keyframes wave {
      0%, 100% { transform: rotate(0deg); }
      25% { transform: rotate(-15deg); }
      75% { transform: rotate(15deg); }
    }
    #btn-confirmar-logout:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
    }
  `;
  document.head.appendChild(style);

  overlay.appendChild(modal);
  document.body.appendChild(overlay);

  // Evento para cerrar sesión
  document.getElementById('btn-confirmar-logout').addEventListener('click', function() {
    // Efecto de fade out
    overlay.style.animation = 'fadeOut 0.3s ease';
    modal.style.animation = 'slideOut 0.3s ease';

    // Agregar animación de salida
    const styleOut = document.createElement('style');
    styleOut.textContent = `
      @keyframes fadeOut {
        from { opacity: 1; }
        to { opacity: 0; }
      }
      @keyframes slideOut {
        from {
          transform: translateY(0);
          opacity: 1;
        }
        to {
          transform: translateY(-50px);
          opacity: 0;
        }
      }
    `;
    document.head.appendChild(styleOut);

    setTimeout(() => {
      // Limpiar localStorage
      localStorage.removeItem('auth_token');
      localStorage.removeItem('username');
      localStorage.removeItem('idUsuario');
      localStorage.removeItem('email');

      // Redirigir al login
      window.location.href = 'index.html';
    }, 300);
  });

  // Permitir cerrar con ESC
  document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
      document.getElementById('btn-confirmar-logout').click();
    }
  }, { once: true });
}

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
  // Verificar sesión (excepto en index.html)
  if (!window.location.pathname.includes('index.html') &&
      !window.location.pathname.includes('register.html')) {
    if (verificarSesion()) {
      actualizarPerfilUsuario();
    }
  }
});
