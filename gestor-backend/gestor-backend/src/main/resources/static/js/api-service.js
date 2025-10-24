/**
 * API Service - Gestor SAT
 * Maneja comunicación con backend o datos mock según CONFIG.USAR_MOCK
 */

// ============================================================
// UTILIDADES
// ============================================================

// Generar ID único para datos mock
function generarId() {
  return Date.now() + Math.random().toString(36).substr(2, 9);
}

// Simular delay de red
function delay(ms = 500) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

// ============================================================
// MOCK DATA STORAGE (localStorage)
// ============================================================

const STORAGE_KEYS = {
  USUARIOS: 'gestor_sat_usuarios',
  CLIENTES: 'gestor_sat_clientes',
  TRAMITES: 'gestor_sat_tramites'
};

// Inicializar datos mock si no existen
function inicializarMockData() {
  if (!localStorage.getItem(STORAGE_KEYS.USUARIOS)) {
    localStorage.setItem(STORAGE_KEYS.USUARIOS, JSON.stringify([]));
  }
  if (!localStorage.getItem(STORAGE_KEYS.CLIENTES)) {
    localStorage.setItem(STORAGE_KEYS.CLIENTES, JSON.stringify([
      {
        id: '1',
        nombreCompleto: 'Juan Pérez García',
        dpi: '1234567890123',
        fechaNacimiento: '1990-05-15',
        telefono: '12345678',
        direccion: 'Zona 1, Ciudad de Guatemala',
        fechaCreacion: '2025-10-15T10:30:00'
      },
      {
        id: '2',
        nombreCompleto: 'María López Hernández',
        dpi: '9876543210987',
        fechaNacimiento: '1985-08-20',
        telefono: '87654321',
        direccion: 'Zona 10, Guatemala',
        fechaCreacion: '2025-10-16T14:20:00'
      }
    ]));
  }
  if (!localStorage.getItem(STORAGE_KEYS.TRAMITES)) {
    localStorage.setItem(STORAGE_KEYS.TRAMITES, JSON.stringify([
      {
        id: '1',
        nombre: 'Inscripción RTU',
        descripcion: 'Registro Tributario Unificado',
        idTipoTramite: '1',
        tipoTramiteNombre: 'SAT',
        fechaCreacion: '2025-10-15T11:00:00'
      }
    ]));
  }
}

// Obtener datos de localStorage
function obtenerDatos(key) {
  try {
    return JSON.parse(localStorage.getItem(key)) || [];
  } catch (e) {
    console.error('Error al obtener datos:', e);
    return [];
  }
}

// Guardar datos en localStorage
function guardarDatos(key, datos) {
  try {
    localStorage.setItem(key, JSON.stringify(datos));
    return true;
  } catch (e) {
    console.error('Error al guardar datos:', e);
    return false;
  }
}

// ============================================================
// API SERVICE - AUTENTICACIÓN
// ============================================================

const AuthService = {
  // Login de usuario
  async login(username, password) {
    if (CONFIG.USAR_MOCK) {
      await delay(800);

      const usuarios = obtenerDatos(STORAGE_KEYS.USUARIOS);

      // Buscar usuario por username
      const usuario = usuarios.find(u => u.usuario === username);

      if (!usuario || usuario.password !== password) {
        throw new Error('Credenciales inválidas');
      }

      return {
        success: true,
        data: { token: 'mock-token-' + usuario.id }
      };
    } else {
      // Conexión real al backend
      const response = await fetch(`${CONFIG.API_BASE_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.error || 'Credenciales inválidas');
      }

      const data = await response.json();
      return { success: true, data };
    }
  }
};

// ============================================================
// API SERVICE - USUARIOS
// ============================================================

const UsuariosService = {
  // Registrar usuario
  async registrar(datosUsuario) {
    if (CONFIG.USAR_MOCK) {
      await delay(800);

      const usuarios = obtenerDatos(STORAGE_KEYS.USUARIOS);

      // Validar si el usuario ya existe
      if (usuarios.find(u => u.usuario === datosUsuario.usuario)) {
        throw new Error('El nombre de usuario ya existe');
      }
      if (usuarios.find(u => u.email === datosUsuario.email)) {
        throw new Error('El email ya está registrado');
      }

      const nuevoUsuario = {
        id: generarId(),
        ...datosUsuario,
        fechaCreacion: new Date().toISOString()
      };

      usuarios.push(nuevoUsuario);
      guardarDatos(STORAGE_KEYS.USUARIOS, usuarios);

      return { success: true, data: nuevoUsuario };
    } else {
      // Conexión real al backend - ENDPOINT CORRECTO
      const response = await fetch(`${CONFIG.API_BASE_URL}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datosUsuario)
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.error || error.message || 'Error al registrar usuario');
      }

      return await response.json();
    }
  }
};

// ============================================================
// API SERVICE - CLIENTES
// ============================================================

const ClientesService = {
  // Crear cliente
  async crear(datosCliente) {
    if (CONFIG.USAR_MOCK) {
      await delay(800);

      const clientes = obtenerDatos(STORAGE_KEYS.CLIENTES);

      // Validar DPI único
      if (clientes.find(c => c.dpi === datosCliente.dpi)) {
        throw new Error('Ya existe un cliente con ese DPI');
      }

      const nuevoCliente = {
        id: generarId(),
        ...datosCliente,
        fechaCreacion: new Date().toISOString()
      };

      clientes.push(nuevoCliente);
      guardarDatos(STORAGE_KEYS.CLIENTES, clientes);

      return { success: true, data: nuevoCliente };
    } else {
      // Conexión real al backend - usar endpoint /registrar
      const response = await fetch(`${CONFIG.API_BASE_URL}/clientes/registrar`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datosCliente)
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.mensaje || error.message || 'Error al crear cliente');
      }

      return await response.json();
    }
  },

  // Listar todos los clientes (con búsqueda opcional)
  async listar(query = '') {
    if (CONFIG.USAR_MOCK) {
      await delay(500);
      let clientes = obtenerDatos(STORAGE_KEYS.CLIENTES);

      // Filtrar si hay query
      if (query && query.trim() !== '') {
        const q = query.toLowerCase();
        clientes = clientes.filter(c =>
          (c.nombreCompleto && c.nombreCompleto.toLowerCase().includes(q)) ||
          (c.dpi && c.dpi.includes(q)) ||
          (c.telefono && c.telefono.includes(q))
        );
      }

      return { success: true, data: clientes };
    } else {
      // Conexión real al backend
      let url = `${CONFIG.API_BASE_URL}/clientes`;

      // Agregar parámetro de búsqueda si existe
      if (query && query.trim() !== '') {
        url += `?q=${encodeURIComponent(query)}`;
      }

      const response = await fetch(url);

      if (!response.ok) {
        throw new Error('Error al obtener clientes');
      }

      return await response.json();
    }
  },

  // Eliminar cliente
  async eliminar(idCliente) {
    if (CONFIG.USAR_MOCK) {
      await delay(500);
      const STORAGE_KEY = STORAGE_KEYS.CLIENTES;
      let clientes = obtenerDatos(STORAGE_KEY);
      clientes = clientes.filter(c => c.id !== idCliente && c.idCliente !== idCliente);
      guardarDatos(STORAGE_KEY, clientes);
      return { success: true };
    } else {
      // Conexión real al backend
      const response = await fetch(`${CONFIG.API_BASE_URL}/clientes/${idCliente}`, {
        method: 'DELETE'
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.mensaje || error.message || 'Error al eliminar cliente');
      }

      return await response.json();
    }
  },

  // Buscar cliente por DPI
  async buscarPorDpi(dpi) {
    if (CONFIG.USAR_MOCK) {
      await delay(400);
      const clientes = obtenerDatos(STORAGE_KEYS.CLIENTES);
      const cliente = clientes.find(c => c.dpi === dpi);

      if (!cliente) {
        throw new Error('Cliente no encontrado');
      }

      return { success: true, data: cliente };
    } else {
      // Conexión real al backend
      const response = await fetch(`${CONFIG.API_BASE_URL}/clientes/dpi/${dpi}`);

      if (!response.ok) {
        throw new Error('Cliente no encontrado');
      }

      return await response.json();
    }
  },

  // Actualizar cliente
  async actualizar(idCliente, datosCliente) {
    if (CONFIG.USAR_MOCK) {
      await delay(500);
      const STORAGE_KEY = STORAGE_KEYS.CLIENTES;
      let clientes = obtenerDatos(STORAGE_KEY);

      const index = clientes.findIndex(c => c.id === idCliente || c.idCliente === idCliente);

      if (index === -1) {
        throw new Error('Cliente no encontrado');
      }

      // Actualizar cliente
      clientes[index] = {
        ...clientes[index],
        ...datosCliente,
        fechaActualizacion: new Date().toISOString()
      };

      guardarDatos(STORAGE_KEY, clientes);
      return { success: true, data: clientes[index] };
    } else {
      // Conexión real al backend
      const response = await fetch(`${CONFIG.API_BASE_URL}/clientes/${idCliente}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datosCliente)
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.mensaje || error.message || 'Error al actualizar cliente');
      }

      return await response.json();
    }
  }
};

// ============================================================
// API SERVICE - TRÁMITES
// ============================================================

const TramitesService = {
  // Crear trámite
  async crear(datosTramite) {
    if (CONFIG.USAR_MOCK) {
      await delay(800);

      const tramites = obtenerDatos(STORAGE_KEYS.TRAMITES);

      // Mapear ID de tipo de trámite a nombre
      const tiposMap = {
        '1': 'SAT',
        '2': 'CGC',
        '3': 'CONSULTA RAE',
        '4': 'RENAS'
      };

      const nuevoTramite = {
        id: generarId(),
        ...datosTramite,
        tipoTramiteNombre: tiposMap[datosTramite.idTipoTramite] || 'Desconocido',
        fechaCreacion: new Date().toISOString()
      };

      tramites.push(nuevoTramite);
      guardarDatos(STORAGE_KEYS.TRAMITES, tramites);

      return { success: true, data: nuevoTramite };
    } else {
      // Conexión real al backend
      const response = await fetch(`${CONFIG.API_BASE_URL}/tramites`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datosTramite)
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Error al crear trámite');
      }

      return await response.json();
    }
  },

  // Listar todos los trámites
  async listar() {
    if (CONFIG.USAR_MOCK) {
      await delay(500);
      const tramites = obtenerDatos(STORAGE_KEYS.TRAMITES);
      return { success: true, data: tramites };
    } else {
      // Conexión real al backend
      const response = await fetch(`${CONFIG.API_BASE_URL}/tramites`);

      if (!response.ok) {
        throw new Error('Error al obtener trámites');
      }

      return await response.json();
    }
  }
};

// ============================================================
// INICIALIZAR
// ============================================================

// Inicializar datos mock al cargar
if (CONFIG.USAR_MOCK) {
  inicializarMockData();
}

// ============================================================
// API SERVICE - DATOS SENSIBLES
// ============================================================

const DatosService = {
  // Guardar datos sensibles de un cliente
  async guardar(idCliente, datosSensibles) {
    if (CONFIG.USAR_MOCK) {
      await delay(800);

      const STORAGE_KEY = 'gestor_sat_datos_sensibles';
      let datos = obtenerDatos(STORAGE_KEY);

      // Buscar si ya existen datos para este cliente
      const index = datos.findIndex(d => d.idCliente === idCliente);

      const nuevosDatos = {
        id: index >= 0 ? datos[index].id : generarId(),
        idCliente: idCliente,
        ...datosSensibles,
        fechaCreacion: index >= 0 ? datos[index].fechaCreacion : new Date().toISOString(),
        fechaActualizacion: new Date().toISOString()
      };

      if (index >= 0) {
        // Actualizar existente
        datos[index] = nuevosDatos;
      } else {
        // Crear nuevo
        datos.push(nuevosDatos);
      }

      guardarDatos(STORAGE_KEY, datos);

      return { success: true, data: nuevosDatos };
    } else {
      // Conexión real al backend
      const response = await fetch(`${CONFIG.API_BASE_URL}/datos/${idCliente}/contrasenas`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datosSensibles)
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Error al guardar datos sensibles');
      }

      return await response.json();
    }
  },

  // Obtener datos sensibles de un cliente
  async obtener(idCliente) {
    if (CONFIG.USAR_MOCK) {
      await delay(500);

      const STORAGE_KEY = 'gestor_sat_datos_sensibles';
      const datos = obtenerDatos(STORAGE_KEY);
      const datosCliente = datos.find(d => d.idCliente === idCliente);

      if (!datosCliente) {
        throw new Error('No se encontraron datos sensibles para este cliente');
      }

      return { success: true, data: datosCliente };
    } else {
      // Conexión real al backend
      const response = await fetch(`${CONFIG.API_BASE_URL}/datos/${idCliente}/contrasenas`);

      if (!response.ok) {
        throw new Error('No se encontraron datos sensibles');
      }

      return await response.json();
    }
  },

  // Verificar si existen datos para un cliente
  async existe(idCliente) {
    if (CONFIG.USAR_MOCK) {
      await delay(300);

      const STORAGE_KEY = 'gestor_sat_datos_sensibles';
      const datos = obtenerDatos(STORAGE_KEY);
      const existe = datos.some(d => d.idCliente === idCliente);

      return { success: true, data: existe };
    } else {
      // Conexión real al backend
      const response = await fetch(`${CONFIG.API_BASE_URL}/datos/${idCliente}/existe`);

      if (!response.ok) {
        return { success: true, data: false };
      }

      const existe = await response.json();
      return { success: true, data: existe };
    }
  }
};

// ============================================================
// API SERVICE - ARCHIVOS
// ============================================================

const ArchivosService = {
  // Subir archivo para un cliente
  async subir(idCliente, file) {
    if (CONFIG.USAR_MOCK) {
      await delay(1000);
      // Modo mock - simular subida
      return {
        success: true,
        data: {
          idArchivos: generarId(),
          idCliente: idCliente,
          nombreArchivo: file.name,
          tamanio: file.size,
          fechaSubida: new Date().toISOString(),
          tipoContenido: file.type
        }
      };
    } else {
      // Conexión real al backend
      const formData = new FormData();
      formData.append('file', file);

      const response = await fetch(`${CONFIG.API_BASE_URL}/archivos/cliente/${idCliente}`, {
        method: 'POST',
        body: formData
        // NO enviar Content-Type header, el navegador lo hace automáticamente con multipart/form-data
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.mensaje || error.message || 'Error al subir archivo');
      }

      return await response.json();
    }
  },

  // Listar archivos de un cliente
  async listar(idCliente) {
    if (CONFIG.USAR_MOCK) {
      await delay(500);
      // Modo mock - datos de ejemplo
      const STORAGE_KEY = 'gestor_sat_archivos';
      let archivos = obtenerDatos(STORAGE_KEY);
      archivos = archivos.filter(a => a.idCliente === idCliente);
      return { success: true, data: archivos };
    } else {
      // Conexión real al backend
      const response = await fetch(`${CONFIG.API_BASE_URL}/archivos/cliente/${idCliente}`);

      if (!response.ok) {
        throw new Error('Error al obtener archivos');
      }

      return await response.json();
    }
  },

  // Descargar archivo
  async descargar(idArchivo, nombreArchivo) {
    if (CONFIG.USAR_MOCK) {
      await delay(500);
      alert('Modo mock: No se puede descargar archivos en modo desarrollo');
      return;
    } else {
      // Conexión real al backend
      const response = await fetch(`${CONFIG.API_BASE_URL}/archivos/${idArchivo}/download`);

      if (!response.ok) {
        throw new Error('Error al descargar archivo');
      }

      // Descargar archivo
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = nombreArchivo;
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    }
  },

  // Eliminar archivo
  async eliminar(idArchivo) {
    if (CONFIG.USAR_MOCK) {
      await delay(500);
      // Modo mock - simular eliminación
      const STORAGE_KEY = 'gestor_sat_archivos';
      let archivos = obtenerDatos(STORAGE_KEY);
      archivos = archivos.filter(a => a.idArchivos !== idArchivo);
      guardarDatos(STORAGE_KEY, archivos);
      return { success: true };
    } else {
      // Conexión real al backend
      const response = await fetch(`${CONFIG.API_BASE_URL}/archivos/${idArchivo}`, {
        method: 'DELETE'
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.mensaje || error.message || 'Error al eliminar archivo');
      }

      return await response.json();
    }
  }
};

// ============================================================
// API SERVICE - ESTADO TRÁMITES
// ============================================================

const EstadoTramitesService = {
  // Crear estado de trámite (asociar trámite a cliente)
  async crear(datosEstado) {
    if (CONFIG.USAR_MOCK) {
      await delay(800);

      const STORAGE_KEY = 'gestor_sat_estado_tramites';
      let estados = obtenerDatos(STORAGE_KEY);

      const nuevoEstado = {
        id: generarId(),
        ...datosEstado,
        fechaCreacion: new Date().toISOString()
      };

      estados.push(nuevoEstado);
      guardarDatos(STORAGE_KEY, estados);

      return { success: true, data: nuevoEstado };
    } else {
      // Conexión real al backend
      const response = await fetch(`${CONFIG.API_BASE_URL}/estados`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datosEstado)
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Error al crear estado de trámite');
      }

      return await response.json();
    }
  },

  // Listar todos los estados de trámites
  async listar() {
    if (CONFIG.USAR_MOCK) {
      await delay(500);
      const STORAGE_KEY = 'gestor_sat_estado_tramites';
      const estados = obtenerDatos(STORAGE_KEY);
      return { success: true, data: estados };
    } else {
      // Conexión real al backend
      const response = await fetch(`${CONFIG.API_BASE_URL}/estados`);

      if (!response.ok) {
        throw new Error('Error al obtener estados de trámites');
      }

      return await response.json();
    }
  },

  // Actualizar estado de un trámite
  async actualizarEstado(idEstado, nuevoEstado) {
    if (CONFIG.USAR_MOCK) {
      await delay(600);
      const STORAGE_KEY = 'gestor_sat_estado_tramites';
      let estados = obtenerDatos(STORAGE_KEY);

      const index = estados.findIndex(e => e.id === idEstado);
      if (index === -1) {
        throw new Error('Estado de trámite no encontrado');
      }

      estados[index].estado = nuevoEstado;
      guardarDatos(STORAGE_KEY, estados);

      return { success: true, data: estados[index] };
    } else {
      // Conexión real al backend
      const response = await fetch(`${CONFIG.API_BASE_URL}/estados/${idEstado}?estado=${nuevoEstado}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' }
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Error al actualizar estado');
      }

      return await response.json();
    }
  }
};

// ============================================================
// API SERVICE - TIPOS DE TRÁMITES
// ============================================================

const TiposTramitesService = {
  // Listar todos los tipos de trámites
  async listar() {
    if (CONFIG.USAR_MOCK) {
      await delay(500);
      const STORAGE_KEY = 'gestor_sat_tipos_tramites';
      const tipos = obtenerDatos(STORAGE_KEY);
      return { success: true, data: tipos };
    } else {
      const response = await fetch(`${CONFIG.API_BASE_URL}/tipos-tramites`);
      if (!response.ok) throw new Error('Error al obtener tipos de trámites');
      return await response.json();
    }
  },

  // Crear nuevo tipo
  async crear(datos) {
    if (CONFIG.USAR_MOCK) {
      await delay(600);
      const STORAGE_KEY = 'gestor_sat_tipos_tramites';
      let tipos = obtenerDatos(STORAGE_KEY);
      const nuevoTipo = {
        id: generarId(),
        ...datos,
        fechaCreacion: new Date().toISOString()
      };
      tipos.push(nuevoTipo);
      guardarDatos(STORAGE_KEY, tipos);
      return { success: true, data: nuevoTipo };
    } else {
      const response = await fetch(`${CONFIG.API_BASE_URL}/tipos-tramites`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
      });
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Error al crear tipo de trámite');
      }
      return await response.json();
    }
  },

  // Actualizar tipo
  async actualizar(id, datos) {
    if (CONFIG.USAR_MOCK) {
      await delay(600);
      const STORAGE_KEY = 'gestor_sat_tipos_tramites';
      let tipos = obtenerDatos(STORAGE_KEY);
      const index = tipos.findIndex(t => t.id === id);
      if (index === -1) throw new Error('Tipo no encontrado');
      tipos[index] = { ...tipos[index], ...datos };
      guardarDatos(STORAGE_KEY, tipos);
      return { success: true, data: tipos[index] };
    } else {
      const response = await fetch(`${CONFIG.API_BASE_URL}/tipos-tramites/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
      });
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Error al actualizar tipo de trámite');
      }
      return await response.json();
    }
  },

  // Eliminar tipo
  async eliminar(id) {
    if (CONFIG.USAR_MOCK) {
      await delay(500);
      const STORAGE_KEY = 'gestor_sat_tipos_tramites';
      let tipos = obtenerDatos(STORAGE_KEY);
      tipos = tipos.filter(t => t.id !== id);
      guardarDatos(STORAGE_KEY, tipos);
      return { success: true };
    } else {
      const response = await fetch(`${CONFIG.API_BASE_URL}/tipos-tramites/${id}`, {
        method: 'DELETE'
      });
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Error al eliminar tipo de trámite');
      }
      return await response.json();
    }
  }
};

// ============================================================
// API SERVICE - NOTIFICACIONES
// ============================================================

const NotificacionesService = {
  // Obtener notificaciones del usuario con paginación
  async obtenerPorUsuario(idUsuario, page = 0, size = 10) {
    if (CONFIG.USAR_MOCK) {
      await delay(500);
      const STORAGE_KEY = 'gestor_sat_notificaciones';
      let notificaciones = obtenerDatos(STORAGE_KEY);
      notificaciones = notificaciones.filter(n => n.idUsuario === idUsuario);
      return {
        success: true,
        data: {
          content: notificaciones,
          totalElements: notificaciones.length,
          totalPages: Math.ceil(notificaciones.length / size)
        }
      };
    } else {
      const response = await fetch(`${CONFIG.API_BASE_URL}/notificaciones/usuario/${idUsuario}?page=${page}&size=${size}`);
      if (!response.ok) throw new Error('Error al obtener notificaciones');
      return await response.json();
    }
  },

  // Obtener notificaciones no leídas
  async obtenerNoLeidas(idUsuario) {
    if (CONFIG.USAR_MOCK) {
      await delay(400);
      const STORAGE_KEY = 'gestor_sat_notificaciones';
      let notificaciones = obtenerDatos(STORAGE_KEY);
      notificaciones = notificaciones.filter(n => n.idUsuario === idUsuario && !n.leida);
      return { success: true, data: notificaciones };
    } else {
      const response = await fetch(`${CONFIG.API_BASE_URL}/notificaciones/usuario/${idUsuario}/no-leidas`);
      if (!response.ok) throw new Error('Error al obtener notificaciones no leídas');
      return await response.json();
    }
  },

  // Contar notificaciones no leídas
  async contarNoLeidas(idUsuario) {
    if (CONFIG.USAR_MOCK) {
      await delay(300);
      const STORAGE_KEY = 'gestor_sat_notificaciones';
      let notificaciones = obtenerDatos(STORAGE_KEY);
      const count = notificaciones.filter(n => n.idUsuario === idUsuario && !n.leida).length;
      return { success: true, data: count };
    } else {
      const response = await fetch(`${CONFIG.API_BASE_URL}/notificaciones/usuario/${idUsuario}/count`);
      if (!response.ok) throw new Error('Error al contar notificaciones');
      return await response.json();
    }
  },

  // Marcar notificación como leída
  async marcarComoLeida(idNotificacion) {
    if (CONFIG.USAR_MOCK) {
      await delay(400);
      const STORAGE_KEY = 'gestor_sat_notificaciones';
      let notificaciones = obtenerDatos(STORAGE_KEY);
      const index = notificaciones.findIndex(n => n.id === idNotificacion);
      if (index !== -1) {
        notificaciones[index].leida = true;
        guardarDatos(STORAGE_KEY, notificaciones);
        return { success: true, data: notificaciones[index] };
      }
      throw new Error('Notificación no encontrada');
    } else {
      const response = await fetch(`${CONFIG.API_BASE_URL}/notificaciones/${idNotificacion}/marcar-leida`, {
        method: 'PUT'
      });
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Error al marcar como leída');
      }
      return await response.json();
    }
  },

  // Crear notificación
  async crear(datos) {
    if (CONFIG.USAR_MOCK) {
      await delay(500);
      const STORAGE_KEY = 'gestor_sat_notificaciones';
      let notificaciones = obtenerDatos(STORAGE_KEY);
      const nuevaNotificacion = {
        id: generarId(),
        ...datos,
        leida: false,
        expirada: false,
        fecha: new Date().toISOString(),
        fechaExpiracion: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString()
      };
      notificaciones.push(nuevaNotificacion);
      guardarDatos(STORAGE_KEY, notificaciones);
      return { success: true, data: nuevaNotificacion };
    } else {
      const response = await fetch(`${CONFIG.API_BASE_URL}/notificaciones`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
      });
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Error al crear notificación');
      }
      return await response.json();
    }
  }
};

// Exportar servicios
window.API = {
  Auth: AuthService,
  Usuarios: UsuariosService,
  Clientes: ClientesService,
  Tramites: TramitesService,
  Datos: DatosService,
  Archivos: ArchivosService,
  EstadoTramites: EstadoTramitesService,
  TiposTramites: TiposTramitesService,
  Notificaciones: NotificacionesService
};
