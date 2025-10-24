/**
 * App.js - Gestor SAT
 * Lógica principal de la aplicación
 */

// ============================================================
// UTILIDADES
// ============================================================

function navTo(url) {
  window.location.href = url;
}

function showModal(id) {
  const el = document.getElementById(id);
  if (!el) return;
  el.classList.add('show');
  el.querySelector('[data-close]')?.addEventListener('click', () => el.classList.remove('show'));
}

function showError(message) {
  alert('❌ Error: ' + message);
}

function showSuccess(message) {
  alert('✅ ' + message);
}

// Mostrar indicador de carga
function setLoading(formEl, isLoading) {
  const btn = formEl.querySelector('button[type="submit"]');
  if (!btn) return;

  if (isLoading) {
    btn.disabled = true;
    btn.dataset.originalText = btn.textContent;
    btn.textContent = 'Cargando...';
  } else {
    btn.disabled = false;
    btn.textContent = btn.dataset.originalText || btn.textContent;
  }
}

// Obtener datos del formulario
function getFormData(formEl) {
  const formData = new FormData(formEl);
  const data = {};
  for (let [key, value] of formData.entries()) {
    data[key] = value;
  }
  return data;
}

// ============================================================
// LOGIN
// ============================================================

function initLogin() {
  const form = document.getElementById('loginForm');
  const registerLink = document.getElementById('goRegister');

  if (registerLink) {
    registerLink.addEventListener('click', (e) => {
      e.preventDefault();
      navTo('register.html');
    });
  }

  if (form) {
    form.addEventListener('submit', async (e) => {
      e.preventDefault();

      const datos = getFormData(form);

      // Validar campos
      if (!datos.username || !datos.password) {
        showError('Usuario y contraseña son requeridos');
        return;
      }

      try {
        setLoading(form, true);

        // Llamar al servicio de autenticación
        const resultado = await API.Auth.login(datos.username, datos.password);

        console.log('Login exitoso:', resultado);

        // Guardar token y datos del usuario en localStorage
        if (resultado.data && resultado.data.token) {
          localStorage.setItem('auth_token', resultado.data.token);
          localStorage.setItem('username', resultado.data.usuario || datos.username);
          localStorage.setItem('idUsuario', resultado.data.idUsuario);
          localStorage.setItem('email', resultado.data.email || '');
        }

        showSuccess('Inicio de sesión exitoso');

        // Redirigir al dashboard
        setTimeout(() => {
          navTo('dashboard.html');
        }, 500);

      } catch (error) {
        console.error('Error en login:', error);
        showError(error.message || 'Error al iniciar sesión');
      } finally {
        setLoading(form, false);
      }
    });
  }
}

// ============================================================
// REGISTER
// ============================================================

function initRegister() {
  const form = document.getElementById('registerForm');
  if (!form) return;

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const datos = getFormData(form);

    // Validar contraseñas coinciden
    if (datos.password !== datos.confirmPassword) {
      showError('Las contraseñas no coinciden');
      return;
    }

    // Validar longitud mínima de contraseña
    if (datos.password.length < 8) {
      showError('La contraseña debe tener al menos 8 caracteres');
      return;
    }

    // Preparar datos para el backend (sin confirmPassword)
    const datosUsuario = {
      username: datos.usuario,  // El backend espera 'username'
      email: datos.email,
      password: datos.password
    };

    try {
      setLoading(form, true);

      const resultado = await API.Usuarios.registrar(datosUsuario);

      console.log('Usuario registrado:', resultado);
      showModal('modal-registered');

    } catch (error) {
      console.error('Error al registrar:', error);
      showError(error.message || 'Error al registrar usuario');
    } finally {
      setLoading(form, false);
    }
  });

  document.getElementById('registered-ok')?.addEventListener('click', () => {
    navTo('index.html');  // Redirigir al login después del registro
  });
}

// ============================================================
// DASHBOARD
// ============================================================

function initDashboard() {
  document.getElementById('goClientes')?.addEventListener('click', (e) => {
    e.preventDefault();
    navTo('clientes.html');
  });

  // Mostrar modo actual
  const modoTexto = CONFIG.USAR_MOCK ? 'MODO DESARROLLO (Datos Mock)' : 'MODO PRODUCCIÓN (Backend Real)';
  console.log('🔧 ' + modoTexto);
}

// ============================================================
// CLIENTES
// ============================================================

function initClientes() {
  const form = document.getElementById('clientesForm');
  if (!form) return;

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const datos = getFormData(form);

    // Validaciones adicionales
    if (datos.dpi && datos.dpi.length !== 13) {
      showError('El DPI debe tener exactamente 13 dígitos');
      return;
    }

    if (datos.telefono && datos.telefono.length !== 8) {
      showError('El teléfono debe tener exactamente 8 dígitos');
      return;
    }

    // Validar fecha de nacimiento (debe ser mayor de edad)
    if (datos.fechaNacimiento) {
      const fechaNac = new Date(datos.fechaNacimiento);
      const hoy = new Date();
      const edad = hoy.getFullYear() - fechaNac.getFullYear();

      if (edad < 18) {
        showError('El cliente debe ser mayor de edad (18 años)');
        return;
      }
    }

    // Obtener el ID del usuario logueado
    const idUsuario = localStorage.getItem('idUsuario');
    if (!idUsuario) {
      showError('No se encontró la sesión del usuario. Por favor inicia sesión nuevamente.');
      setTimeout(() => navTo('index.html'), 2000);
      return;
    }

    // Preparar datos para el backend
    const datosCliente = {
      idUsuario: parseInt(idUsuario),
      nombreCompleto: datos.nombreCompleto,
      dpi: datos.dpi,
      fechaNacimiento: datos.fechaNacimiento,
      telefono: datos.telefono || null,
      direccion: datos.direccion || null
    };

    try {
      setLoading(form, true);

      const resultado = await API.Clientes.crear(datosCliente);

      console.log('Cliente creado:', resultado);
      showModal('modal-clientes-ok');

      // Limpiar formulario
      form.reset();

    } catch (error) {
      console.error('Error al crear cliente:', error);
      showError(error.message || 'Error al guardar cliente');
    } finally {
      setLoading(form, false);
    }
  });

  document.getElementById('clientes-ok')?.addEventListener('click', () => {
    navTo('tramites.html');
  });
}

// ============================================================
// TRÁMITES
// ============================================================

function initTramites() {
  const form = document.getElementById('tramitesForm');
  const selectCliente = document.getElementById('selectCliente');

  if (!form || !selectCliente) return;

  // Cargar clientes en el selector
  cargarClientesParaTramites();

  // Mostrar preview del cliente cuando se selecciona
  selectCliente.addEventListener('change', mostrarPreviewCliente);

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const datos = getFormData(form);

    // Validaciones
    if (!datos.clienteId || datos.clienteId === '') {
      showError('Debe seleccionar un cliente');
      return;
    }

    if (!datos.idTipoTramite || datos.idTipoTramite === '') {
      showError('Debe seleccionar un tipo de trámite');
      return;
    }

    if (!datos.estadoInicial || datos.estadoInicial === '') {
      showError('Debe seleccionar un estado inicial');
      return;
    }

    if (!datos.fechaInicio) {
      showError('Debe ingresar la fecha de inicio');
      return;
    }

    try {
      setLoading(form, true);

      // PASO 1: Crear el trámite
      const datosTramite = {
        nombre: datos.nombre,
        descripcion: datos.descripcion || '',
        tipoTramiteId: parseInt(datos.idTipoTramite)
      };

      console.log('Creando trámite:', datosTramite);
      const resultadoTramite = await API.Tramites.crear(datosTramite);
      const tramite = resultadoTramite.data || resultadoTramite;

      console.log('Trámite creado:', tramite);

      // PASO 2: Crear el EstadoTramite (asociar trámite a cliente)
      const datosEstado = {
        tramiteId: tramite.id,
        clienteId: parseInt(datos.clienteId),
        estado: datos.estadoInicial,
        fechaInicio: datos.fechaInicio
      };

      console.log('Creando estado de trámite:', datosEstado);
      const resultadoEstado = await API.EstadoTramites.crear(datosEstado);

      console.log('Estado de trámite creado:', resultadoEstado);

      // Mostrar modal de éxito
      showModal('modal-tramite-ok');

      // Limpiar formulario
      form.reset();
      document.getElementById('cliente-preview').classList.remove('show');

      // Resetear fecha a hoy
      const fechaInput = document.querySelector('[name="fechaInicio"]');
      if (fechaInput) {
        fechaInput.value = new Date().toISOString().split('T')[0];
      }

    } catch (error) {
      console.error('Error al crear trámite:', error);
      showError(error.message || 'Error al crear trámite');
    } finally {
      setLoading(form, false);
    }
  });
}

async function cargarClientesParaTramites() {
  const selector = document.getElementById('selectCliente');
  if (!selector) return;

  try {
    selector.innerHTML = '<option value="">Cargando clientes...</option>';

    const resultado = await API.Clientes.listar();

    // Manejar diferentes estructuras de respuesta
    let clientes = [];
    if (resultado.data) {
      if (resultado.data.content) {
        clientes = resultado.data.content;
      } else if (Array.isArray(resultado.data)) {
        clientes = resultado.data;
      }
    } else if (Array.isArray(resultado)) {
      clientes = resultado;
    }

    if (!clientes || clientes.length === 0) {
      selector.innerHTML = '<option value="">No hay clientes registrados</option>';
      return;
    }

    // Generar opciones
    let html = '<option value="">-- Seleccionar cliente --</option>';
    clientes.forEach(cliente => {
      const id = cliente.id || cliente.idCliente;
      const nombre = cliente.nombreCompleto || 'Sin nombre';
      const dpi = cliente.dpi || 'Sin DPI';
      html += `<option value="${id}"
                data-nombre="${nombre}"
                data-dpi="${dpi}"
                data-telefono="${cliente.telefono || 'N/A'}"
                data-fecha="${cliente.fechaNacimiento || 'N/A'}">
                ${nombre} (DPI: ${dpi})
              </option>`;
    });

    selector.innerHTML = html;

  } catch (error) {
    console.error('Error al cargar clientes:', error);
    selector.innerHTML = '<option value="">Error al cargar clientes</option>';
  }
}

function mostrarPreviewCliente(event) {
  const selectedOption = event.target.selectedOptions[0];
  const preview = document.getElementById('cliente-preview');

  if (!selectedOption || !selectedOption.value) {
    preview.classList.remove('show');
    return;
  }

  // Rellenar datos del preview
  document.getElementById('preview-nombre').textContent = selectedOption.dataset.nombre || 'N/A';
  document.getElementById('preview-dpi').textContent = selectedOption.dataset.dpi || 'N/A';
  document.getElementById('preview-telefono').textContent = selectedOption.dataset.telefono || 'N/A';
  document.getElementById('preview-fecha').textContent = selectedOption.dataset.fecha || 'N/A';

  preview.classList.add('show');
}

// ============================================================
// LISTADO CLIENTES
// ============================================================

function initListadoClientes() {
  const searchInput = document.getElementById('searchInput');
  const btnBuscar = document.getElementById('btnBuscar');
  const btnLimpiar = document.getElementById('btnLimpiar');

  // Cargar todos los clientes al inicio
  cargarClientes();

  // Evento de búsqueda
  if (btnBuscar) {
    btnBuscar.addEventListener('click', () => {
      const query = searchInput.value.trim();
      cargarClientes(query);
    });
  }

  // Permitir buscar con Enter
  if (searchInput) {
    searchInput.addEventListener('keypress', (e) => {
      if (e.key === 'Enter') {
        const query = searchInput.value.trim();
        cargarClientes(query);
      }
    });
  }

  // Limpiar búsqueda
  if (btnLimpiar) {
    btnLimpiar.addEventListener('click', () => {
      searchInput.value = '';
      cargarClientes();
    });
  }
}

async function cargarClientes(query = '') {
  const container = document.getElementById('clientes-container');
  if (!container) return;

  try {
    container.innerHTML = '<p style="text-align:center;color:#fff;">Cargando clientes...</p>';

    const resultado = await API.Clientes.listar(query);

    // El backend devuelve: { data: { content: [...], totalElements, totalPages, ... }, success, mensaje }
    let clientes = [];

    if (resultado.data) {
      // Si viene del backend real (estructura paginada)
      if (resultado.data.content) {
        clientes = resultado.data.content;
      } else if (Array.isArray(resultado.data)) {
        // Si viene como array directo
        clientes = resultado.data;
      }
    } else if (Array.isArray(resultado)) {
      // Si viene directo como array (modo mock)
      clientes = resultado;
    }

    console.log('Clientes cargados:', clientes);

    if (!clientes || clientes.length === 0) {
      container.innerHTML = '<p style="text-align:center;color:#fff;">No hay clientes registrados</p>';
      return;
    }

    // Generar tabla de clientes
    let html = `
      <table class="tabla-clientes">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre Completo</th>
            <th>DPI</th>
            <th>Fecha Nacimiento</th>
            <th>Teléfono</th>
            <th>Dirección</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
    `;

    clientes.forEach((cliente, index) => {
      const clienteId = cliente.id || cliente.idCliente;
      const nombreCompleto = cliente.nombreCompleto || '';
      const dpi = cliente.dpi || '';
      const fechaNacimiento = cliente.fechaNacimiento || '';
      const telefono = cliente.telefono || '';
      const direccion = cliente.direccion || '';

      html += `
        <tr class="cliente-row" id="cliente-row-${clienteId}"
            data-id="${clienteId}"
            data-nombre="${nombreCompleto}"
            data-dpi="${dpi}"
            data-fecha="${fechaNacimiento}"
            data-telefono="${telefono}"
            data-direccion="${direccion}">
          <td>${clienteId || 'N/A'}</td>
          <td>${nombreCompleto || 'N/A'}</td>
          <td>${dpi || 'N/A'}</td>
          <td>${fechaNacimiento || 'N/A'}</td>
          <td>${telefono || 'N/A'}</td>
          <td>${direccion || 'N/A'}</td>
          <td>
            <div style="display:flex;gap:8px;justify-content:center;">
              <button class="btn-ver-mas" onclick="toggleDetalle(${clienteId}, '${nombreCompleto}', this)">
                Ver más ▼
              </button>
              <button class="btn-eliminar-cliente" onclick="eliminarCliente(${clienteId}, '${nombreCompleto}')" title="Eliminar cliente">
                🗑️
              </button>
            </div>
          </td>
        </tr>
        <tr class="detalle-row" id="detalle-${clienteId}">
          <td colspan="7">
            <div class="detalle-content">
              <div class="detalle-header">
                <h3>👤 Más sobre: ${nombreCompleto}</h3>
                <button class="btn-editar-cliente" onclick="editarCliente(${clienteId})">
                  ✏️ Editar Cliente
                </button>
              </div>

              <!-- Tabs de navegación -->
              <div class="tabs-container">
                <button class="tab-btn active" onclick="cambiarTab(${clienteId}, 'datos')">🔐 Datos Sensibles</button>
                <button class="tab-btn" onclick="cambiarTab(${clienteId}, 'archivos')">📎 Archivos</button>
              </div>

              <!-- Tab: Datos Sensibles -->
              <div id="tab-datos-${clienteId}" class="tab-content active">
                <form id="form-datos-${clienteId}" class="form-grid-datos">
                <div class="form-col-datos">
                  <label>NIT</label>
                  <input type="text" name="nit" placeholder="Ingrese NIT" maxlength="15">
                </div>
                <div class="form-col-datos">
                  <label>NIS</label>
                  <input type="text" name="nis" placeholder="Ingrese NIS" maxlength="20">
                </div>
                <div class="form-col-datos">
                  <label>EMAIL</label>
                  <input type="email" name="email" placeholder="correo@ejemplo.com">
                </div>
                <div class="form-col-datos">
                  <label>DPI (copia)</label>
                  <input type="text" name="dpi" placeholder="13 dígitos" maxlength="13">
                </div>
                <div class="form-col-datos">
                  <label>CUENTA BANCARIA</label>
                  <input type="text" name="cuentaBancaria" placeholder="Número de cuenta">
                </div>
                <div class="form-col-datos password-field">
                  <label>CONTRASEÑA AGENCIA VIRTUAL</label>
                  <input type="password" name="contrasenaAgenciaVirtual" placeholder="********">
                  <button type="button" class="password-toggle-btn" onclick="togglePassword(this)">👁️</button>
                </div>
                <div class="form-col-datos password-field">
                  <label>CONTRASEÑA CORREO</label>
                  <input type="password" name="contrasenaCorreo" placeholder="********">
                  <button type="button" class="password-toggle-btn" onclick="togglePassword(this)">👁️</button>
                </div>
                <div class="form-col-datos password-field">
                  <label>CONTRASEÑA CGC</label>
                  <input type="password" name="contrasenaCgc" placeholder="********">
                  <button type="button" class="password-toggle-btn" onclick="togglePassword(this)">👁️</button>
                </div>
                <div class="form-col-datos password-field">
                  <label>CONTRASEÑA CONSULTA GENERAL</label>
                  <input type="password" name="contrasenaConsultaGeneral" placeholder="********">
                  <button type="button" class="password-toggle-btn" onclick="togglePassword(this)">👁️</button>
                </div>
                <div class="form-col-datos password-field">
                  <label>CONTRASEÑA REGAHE</label>
                  <input type="password" name="contrasenaRegahe" placeholder="********">
                  <button type="button" class="password-toggle-btn" onclick="togglePassword(this)">👁️</button>
                </div>
                </form>
                <div style="margin-top:20px;">
                  <button class="btn-guardar-datos" onclick="guardarDatosSensibles(${clienteId})">💾 Guardar Datos</button>
                </div>
                <div id="loading-${clienteId}" style="display:none;text-align:center;margin-top:12px;color:#5a0fb2;">
                  Cargando datos...
                </div>
              </div>

              <!-- Tab: Archivos -->
              <div id="tab-archivos-${clienteId}" class="tab-content">
                <div class="archivos-section">
                  <div class="upload-area">
                    <h4>📤 Subir Archivo</h4>
                    <input type="file" id="file-input-${clienteId}" class="file-input" multiple>
                    <label for="file-input-${clienteId}" class="file-label">
                      <span class="file-icon">📁</span>
                      <span>Click para seleccionar archivos o arrastra aquí</span>
                    </label>
                    <button class="btn-subir-archivo" onclick="subirArchivo(${clienteId})">
                      ⬆️ Subir Archivo
                    </button>
                  </div>

                  <div class="archivos-lista">
                    <h4>📋 Archivos del Cliente</h4>
                    <div id="lista-archivos-${clienteId}" class="archivos-grid">
                      <p style="text-align:center;color:#999;padding:20px;">No hay archivos cargados aún</p>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Botón Cerrar (común para todos los tabs) -->
              <div style="margin-top:20px;padding-top:20px;border-top:1px solid #ddd;">
                <button class="btn-cerrar" onclick="toggleDetalle(${clienteId}, '', document.querySelector('[onclick*=\\'${clienteId}\\']'))">✖️ Cerrar</button>
              </div>
            </div>
          </td>
        </tr>
      `;
    });

    html += `
        </tbody>
      </table>
    `;

    // Agregar información de paginación si existe
    if (resultado.data && resultado.data.totalElements !== undefined) {
      html += `
        <p style="text-align:center;color:#fff;margin-top:16px;font-size:14px;">
          Total de clientes: ${resultado.data.totalElements}
        </p>
      `;
    }

    container.innerHTML = html;

  } catch (error) {
    console.error('Error al cargar clientes:', error);
    container.innerHTML = '<p style="text-align:center;color:red;">❌ Error al cargar clientes: ' + error.message + '</p>';
  }
}

// ============================================================
// FUNCIONES PARA EXPANDIR/COLAPSAR DETALLES DE CLIENTE
// ============================================================

function toggleDetalle(clienteId, nombreCliente, btnElement) {
  const detalleRow = document.getElementById(`detalle-${clienteId}`);

  if (!detalleRow) return;

  if (detalleRow.classList.contains('show')) {
    // Cerrar
    detalleRow.classList.remove('show');
    btnElement.textContent = 'Ver más ▼';
    btnElement.classList.remove('activo');
  } else {
    // Cerrar todos los demás abiertos
    document.querySelectorAll('.detalle-row.show').forEach(row => {
      row.classList.remove('show');
    });
    document.querySelectorAll('.btn-ver-mas.activo').forEach(btn => {
      btn.textContent = 'Ver más ▼';
      btn.classList.remove('activo');
    });

    // Abrir este
    detalleRow.classList.add('show');
    btnElement.textContent = 'Cerrar ▲';
    btnElement.classList.add('activo');

    // Cargar datos sensibles si existen
    cargarDatosSensiblesCliente(clienteId);
  }
}

// Cargar datos sensibles de un cliente
async function cargarDatosSensiblesCliente(clienteId) {
  const loadingEl = document.getElementById(`loading-${clienteId}`);
  const form = document.getElementById(`form-datos-${clienteId}`);

  if (!form) return;

  try {
    if (loadingEl) loadingEl.style.display = 'block';

    const resultado = await API.Datos.obtener(clienteId);
    const datos = resultado.data || resultado;

    // Rellenar formulario
    form.querySelector('[name="nit"]').value = datos.nit || '';
    form.querySelector('[name="nis"]').value = datos.nis || '';
    form.querySelector('[name="email"]').value = datos.email || '';
    form.querySelector('[name="dpi"]').value = datos.dpi || '';
    form.querySelector('[name="cuentaBancaria"]').value = datos.cuentaBancaria || '';
    form.querySelector('[name="contrasenaAgenciaVirtual"]').value = datos.contrasenaAgenciaVirtual || datos.passAgenciaVirtual || '';
    form.querySelector('[name="contrasenaCorreo"]').value = datos.contrasenaCorreo || datos.passCorreo || '';
    form.querySelector('[name="contrasenaCgc"]').value = datos.contrasenaCgc || datos.passCgc || '';
    form.querySelector('[name="contrasenaConsultaGeneral"]').value = datos.contrasenaConsultaGeneral || datos.passConsultaGeneral || '';
    form.querySelector('[name="contrasenaRegahe"]').value = datos.contrasenaRegahe || datos.passReghae || '';

    console.log('✅ Datos sensibles cargados para cliente', clienteId);
  } catch (error) {
    console.log('ℹ️ No hay datos sensibles previos para este cliente');
    // No mostrar error, es normal que no existan datos aún
  } finally {
    if (loadingEl) loadingEl.style.display = 'none';
  }
}

// Guardar datos sensibles
async function guardarDatosSensibles(clienteId) {
  const form = document.getElementById(`form-datos-${clienteId}`);
  if (!form) return;

  const datos = getFormData(form);

  // Preparar datos para enviar (usando los nombres correctos del backend)
  const datosSensibles = {
    nit: datos.nit || null,
    nis: datos.nis || null,
    email: datos.email || null,
    dpi: datos.dpi || null,
    cuentaBancaria: datos.cuentaBancaria || null,
    passAgenciaVirtual: datos.contrasenaAgenciaVirtual || null,
    passCorreo: datos.contrasenaCorreo || null,
    passCgc: datos.contrasenaCgc || null,
    passConsultaGeneral: datos.contrasenaConsultaGeneral || null,
    passReghae: datos.contrasenaRegahe || null
  };

  try {
    const btnGuardar = event.target;
    btnGuardar.disabled = true;
    btnGuardar.textContent = '⏳ Guardando...';

    const resultado = await API.Datos.guardar(clienteId, datosSensibles);

    console.log('Datos sensibles guardados:', resultado);
    showSuccess('✅ Datos sensibles guardados exitosamente');

    btnGuardar.textContent = '✔️ Guardado';
    setTimeout(() => {
      btnGuardar.disabled = false;
      btnGuardar.textContent = '💾 Guardar Datos';
    }, 2000);

  } catch (error) {
    console.error('Error al guardar datos sensibles:', error);
    showError(error.message || 'Error al guardar datos sensibles');
    const btnGuardar = event.target;
    btnGuardar.disabled = false;
    btnGuardar.textContent = '💾 Guardar Datos';
  }
}

// Toggle para mostrar/ocultar contraseñas
function togglePassword(btnElement) {
  const passwordField = btnElement.closest('.password-field');
  const input = passwordField.querySelector('input');

  if (input.type === 'password') {
    input.type = 'text';
    btnElement.textContent = '🙈';
  } else {
    input.type = 'password';
    btnElement.textContent = '👁️';
  }
}

// ============================================================
// FUNCIONES PARA TABS (DATOS SENSIBLES / ARCHIVOS)
// ============================================================

function cambiarTab(clienteId, tab) {
  // Remover active de todos los tabs
  document.querySelectorAll(`#detalle-${clienteId} .tab-btn`).forEach(btn => {
    btn.classList.remove('active');
  });
  document.querySelectorAll(`#detalle-${clienteId} .tab-content`).forEach(content => {
    content.classList.remove('active');
  });

  // Activar el tab seleccionado
  event.target.classList.add('active');
  document.getElementById(`tab-${tab}-${clienteId}`).classList.add('active');

  // Si es tab de archivos, cargar archivos
  if (tab === 'archivos') {
    cargarArchivosCliente(clienteId);
  }
}

// ============================================================
// FUNCIONES PARA EDITAR Y ELIMINAR CLIENTE
// ============================================================

function editarCliente(id) {
  // Obtener los datos del cliente desde los data attributes
  const clienteRow = document.getElementById(`cliente-row-${id}`);
  if (!clienteRow) return;

  const nombre = clienteRow.dataset.nombre || '';
  const dpi = clienteRow.dataset.dpi || '';
  const fechaNac = clienteRow.dataset.fecha || '';
  const telefono = clienteRow.dataset.telefono || '';
  const direccion = clienteRow.dataset.direccion || '';

  console.log('Editando cliente:', { id, nombre, dpi, fechaNac, telefono, direccion });

  const detalleHeader = document.querySelector(`#detalle-${id} .detalle-header`);
  if (!detalleHeader) return;

  // Crear formulario de edición
  detalleHeader.innerHTML = `
    <h3>✏️ Editando cliente</h3>
    <div style="display: flex; gap: 8px;">
      <button class="btn-guardar-datos" onclick="guardarEdicionCliente(${id})" style="background: linear-gradient(135deg, #4caf50 0%, #45a049 100%);">
        💾 Guardar Cambios
      </button>
      <button class="btn-cerrar" onclick="cancelarEdicionCliente()">
        ✖️ Cancelar
      </button>
    </div>
  `;

  // Reemplazar la información del cliente por un formulario editable
  // Escapar comillas en los valores para evitar que rompan el HTML
  const nombreEscaped = nombre.replace(/"/g, '&quot;');
  const direccionEscaped = direccion.replace(/"/g, '&quot;');

  clienteRow.innerHTML = `
    <td>${id}</td>
    <td><input type="text" id="edit-nombre-${id}" value="${nombreEscaped}" class="input" style="width:100%;padding:8px;"></td>
    <td><input type="text" id="edit-dpi-${id}" value="${dpi}" class="input" style="width:100%;padding:8px;" maxlength="13"></td>
    <td><input type="date" id="edit-fechaNac-${id}" value="${fechaNac}" class="input" style="width:100%;padding:8px;"></td>
    <td><input type="text" id="edit-telefono-${id}" value="${telefono}" class="input" style="width:100%;padding:8px;" maxlength="8"></td>
    <td><input type="text" id="edit-direccion-${id}" value="${direccionEscaped}" class="input" style="width:100%;padding:8px;"></td>
    <td>
      <div style="display:flex;gap:8px;justify-content:center;">
        <button class="btn-ver-mas activo" onclick="toggleDetalle(${id}, '', this)">
          Cerrar ▲
        </button>
        <button class="btn-eliminar-cliente" onclick="eliminarCliente(${id}, '${nombreEscaped}')" title="Eliminar cliente">
          🗑️
        </button>
      </div>
    </td>
  `;
}

async function guardarEdicionCliente(idCliente) {
  // Obtener los valores del formulario
  const nombreCompleto = document.getElementById(`edit-nombre-${idCliente}`)?.value.trim();
  const dpi = document.getElementById(`edit-dpi-${idCliente}`)?.value.trim();
  const fechaNacimiento = document.getElementById(`edit-fechaNac-${idCliente}`)?.value;
  const telefono = document.getElementById(`edit-telefono-${idCliente}`)?.value.trim();
  const direccion = document.getElementById(`edit-direccion-${idCliente}`)?.value.trim();

  // Validaciones
  if (!nombreCompleto) {
    showError('El nombre completo es requerido');
    return;
  }

  if (!dpi || dpi.length !== 13) {
    showError('El DPI debe tener exactamente 13 dígitos');
    return;
  }

  if (!fechaNacimiento) {
    showError('La fecha de nacimiento es requerida');
    return;
  }

  if (telefono && telefono.length !== 8) {
    showError('El teléfono debe tener exactamente 8 dígitos');
    return;
  }

  // Validar edad (mayor de 18)
  const fechaNac = new Date(fechaNacimiento);
  const hoy = new Date();
  const edad = hoy.getFullYear() - fechaNac.getFullYear();

  if (edad < 18) {
    showError('El cliente debe ser mayor de edad (18 años)');
    return;
  }

  // Preparar datos para actualizar
  const datosActualizados = {
    nombreCompleto,
    dpi,
    fechaNacimiento,
    telefono: telefono || null,
    direccion: direccion || null
  };

  try {
    // Deshabilitar botón
    const btnGuardar = event.target;
    btnGuardar.disabled = true;
    btnGuardar.textContent = '⏳ Guardando...';

    // Llamar al servicio de actualización
    const resultado = await API.Clientes.actualizar(idCliente, datosActualizados);

    console.log('Cliente actualizado:', resultado);
    showSuccess('✅ Cliente actualizado exitosamente');

    // Recargar la lista de clientes para reflejar los cambios
    await cargarClientes();

  } catch (error) {
    console.error('Error al actualizar cliente:', error);
    showError(error.message || 'Error al actualizar cliente');

    // Restaurar botón
    const btnGuardar = event.target;
    if (btnGuardar) {
      btnGuardar.disabled = false;
      btnGuardar.textContent = '💾 Guardar Cambios';
    }
  }
}

function cancelarEdicionCliente() {
  // Recargar la lista completa para restaurar el estado original
  cargarClientes();
}

async function eliminarCliente(idCliente, nombreCliente) {
  // Confirmación con nombre del cliente
  const confirmar = confirm(
    `⚠️ ¿Estás seguro de eliminar este cliente?\n\n` +
    `Cliente: ${nombreCliente}\n\n` +
    `Esta acción eliminará:\n` +
    `• Los datos básicos del cliente\n` +
    `• Los datos sensibles asociados\n` +
    `• Los archivos del cliente\n` +
    `• Los trámites asociados\n\n` +
    `Esta acción NO se puede deshacer.`
  );

  if (!confirmar) return;

  try {
    // Mostrar feedback visual
    const row = document.getElementById(`cliente-row-${idCliente}`);
    const detalleRow = document.getElementById(`detalle-${idCliente}`);

    if (row) {
      row.style.opacity = '0.5';
      row.style.pointerEvents = 'none';
    }

    await API.Clientes.eliminar(idCliente);

    showSuccess('✅ Cliente eliminado exitosamente');

    // Remover visualmente las filas
    if (row) {
      row.style.transition = 'all 0.3s ease';
      row.style.transform = 'translateX(-100%)';
      row.style.opacity = '0';

      setTimeout(() => {
        row.remove();
        if (detalleRow) detalleRow.remove();
      }, 300);
    }

    // Recargar lista completa después de un momento
    setTimeout(() => {
      cargarClientes();
    }, 500);

  } catch (error) {
    console.error('Error al eliminar cliente:', error);
    showError(error.message || 'Error al eliminar cliente');

    // Restaurar estado si hay error
    const row = document.getElementById(`cliente-row-${idCliente}`);
    if (row) {
      row.style.opacity = '1';
      row.style.pointerEvents = 'auto';
    }
  }
}

// ============================================================
// FUNCIONES PARA ARCHIVOS (UI LISTA - BACKEND PENDIENTE)
// ============================================================

async function cargarArchivosCliente(clienteId) {
  const listaContainer = document.getElementById(`lista-archivos-${clienteId}`);
  if (!listaContainer) return;

  try {
    listaContainer.innerHTML = '<p style="text-align:center;color:#999;padding:20px;">Cargando archivos...</p>';

    const resultado = await API.Archivos.listar(clienteId);
    const archivos = resultado.data || resultado;

    if (!archivos || archivos.length === 0) {
      listaContainer.innerHTML = '<p style="text-align:center;color:#999;padding:20px;">No hay archivos cargados aún</p>';
      return;
    }

    let html = '';
    archivos.forEach((archivo) => {
      // Determinar icono según tipo de contenido
      let icon = '📄';
      if (archivo.tipoContenido) {
        if (archivo.tipoContenido.includes('image')) icon = '🖼️';
        else if (archivo.tipoContenido.includes('pdf')) icon = '📄';
        else if (archivo.tipoContenido.includes('word')) icon = '📝';
        else if (archivo.tipoContenido.includes('excel') || archivo.tipoContenido.includes('spreadsheet')) icon = '📊';
        else if (archivo.tipoContenido.includes('zip') || archivo.tipoContenido.includes('rar')) icon = '📦';
      }

      // Formatear tamaño
      const tamanioMB = (archivo.tamanio / 1024 / 1024).toFixed(2);
      const tamanioStr = tamanioMB > 0.01 ? tamanioMB + ' MB' : (archivo.tamanio / 1024).toFixed(2) + ' KB';

      // Formatear fecha
      const fecha = archivo.fechaSubida ? new Date(archivo.fechaSubida).toLocaleDateString('es-GT') : 'N/A';

      html += `
        <div class="archivo-item">
          <div class="archivo-info">
            <span class="archivo-icon">${icon}</span>
            <div class="archivo-detalles">
              <div class="archivo-nombre">${archivo.nombreArchivo}</div>
              <div class="archivo-meta">${tamanioStr} • ${fecha}</div>
            </div>
          </div>
          <div class="archivo-acciones">
            <button class="btn-archivo" onclick="descargarArchivo(${archivo.idArchivos}, '${archivo.nombreArchivo}')" title="Descargar">⬇️</button>
            <button class="btn-archivo" onclick="eliminarArchivo(${clienteId}, ${archivo.idArchivos})" title="Eliminar">🗑️</button>
          </div>
        </div>
      `;
    });

    listaContainer.innerHTML = html;

  } catch (error) {
    console.error('Error al cargar archivos:', error);
    listaContainer.innerHTML = '<p style="text-align:center;color:red;padding:20px;">Error al cargar archivos</p>';
  }
}

async function subirArchivo(clienteId) {
  const fileInput = document.getElementById(`file-input-${clienteId}`);
  const files = fileInput.files;

  if (files.length === 0) {
    showError('Por favor selecciona al menos un archivo');
    return;
  }

  const btnSubir = event.target;
  const originalText = btnSubir.textContent;

  try {
    btnSubir.disabled = true;
    btnSubir.textContent = '⏳ Subiendo...';

    // Subir cada archivo
    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      btnSubir.textContent = `⏳ Subiendo ${i + 1}/${files.length}...`;

      await API.Archivos.subir(clienteId, file);
    }

    showSuccess(`✅ ${files.length} archivo(s) subido(s) exitosamente`);

    // Limpiar input
    fileInput.value = '';

    // Recargar lista de archivos
    await cargarArchivosCliente(clienteId);

  } catch (error) {
    console.error('Error al subir archivos:', error);
    showError(error.message || 'Error al subir archivos');
  } finally {
    btnSubir.disabled = false;
    btnSubir.textContent = originalText;
  }
}

async function descargarArchivo(idArchivo, nombreArchivo) {
  try {
    await API.Archivos.descargar(idArchivo, nombreArchivo);
    showSuccess('✅ Archivo descargado');
  } catch (error) {
    console.error('Error al descargar archivo:', error);
    showError(error.message || 'Error al descargar archivo');
  }
}

async function eliminarArchivo(clienteId, idArchivo) {
  if (!confirm('¿Estás seguro de eliminar este archivo?')) return;

  try {
    await API.Archivos.eliminar(idArchivo);
    showSuccess('✅ Archivo eliminado exitosamente');

    // Recargar lista de archivos
    await cargarArchivosCliente(clienteId);
  } catch (error) {
    console.error('Error al eliminar archivo:', error);
    showError(error.message || 'Error al eliminar archivo');
  }
}

// ============================================================
// CONSULTA DE TRÁMITES
// ============================================================

let todosLosTramites = []; // Variable global para almacenar todos los trámites

function initConsultaTramites() {
  cargarTodosLosTramites();
}

async function cargarTodosLosTramites() {
  const container = document.getElementById('tramites-list');
  if (!container) return;

  try {
    container.innerHTML = '<div class="loading">⏳ Cargando trámites...</div>';

    const resultado = await API.EstadoTramites.listar();
    todosLosTramites = resultado.data || resultado || [];

    console.log('Trámites cargados:', todosLosTramites);

    if (todosLosTramites.length === 0) {
      container.innerHTML = `
        <div class="no-results">
          <div class="no-results-icon">📋</div>
          <p style="color:#666;">No hay trámites registrados aún</p>
          <a href="tramites.html" class="btn btn-primary" style="margin-top:16px;">➕ Asignar Primer Trámite</a>
        </div>
      `;
      actualizarEstadisticas([]);
      return;
    }

    mostrarTramites(todosLosTramites);

  } catch (error) {
    console.error('Error al cargar trámites:', error);
    container.innerHTML = `
      <div class="no-results">
        <div class="no-results-icon">❌</div>
        <p style="color:#999;">Error al cargar los trámites</p>
        <p style="color:#666;font-size:14px;">${error.message}</p>
      </div>
    `;
  }
}

function mostrarTramites(tramites) {
  const container = document.getElementById('tramites-list');
  if (!container) return;

  if (tramites.length === 0) {
    container.innerHTML = `
      <div class="no-results">
        <div class="no-results-icon">🔍</div>
        <p style="color:#666;">No se encontraron trámites con los filtros aplicados</p>
        <button class="btn btn-outline" onclick="limpiarFiltros()" style="margin-top:16px;">Limpiar Filtros</button>
      </div>
    `;
    document.getElementById('results-count').textContent = '0';
    return;
  }

  let html = '';

  tramites.forEach((tramite) => {
    // Determinar clase de estado
    let estadoClass = 'status-pending';
    let estadoIcon = '⏳';

    if (tramite.estado === 'INICIADO') {
      estadoClass = 'status-progress';
      estadoIcon = '🔄';
    } else if (tramite.estado === 'TERMINADO') {
      estadoClass = 'status-completed';
      estadoIcon = '✅';
    } else if (tramite.estado === 'PENDIENTE') {
      estadoClass = 'status-pending';
      estadoIcon = '⏳';
    }

    // Formatear fecha
    const fecha = tramite.fechaInicio ? new Date(tramite.fechaInicio).toLocaleDateString('es-GT') : 'N/A';

    html += `
      <div class="tramite-card">
        <div class="tramite-header">
          <div>
            <span class="tramite-id">ID-${tramite.id || 'N/A'}</span>
            <h4 class="tramite-title">${tramite.nombreTramite || 'Sin nombre'}</h4>
          </div>
          <span class="status-badge ${estadoClass}">${estadoIcon} ${tramite.estado || 'N/A'}</span>
        </div>
        <div class="tramite-body">
          <div class="tramite-info">
            <div class="info-item">
              <span class="info-label">Cliente</span>
              <span class="info-value">${tramite.nombreCliente || 'N/A'}</span>
            </div>
            <div class="info-item">
              <span class="info-label">ID Cliente</span>
              <span class="info-value">#${tramite.clienteId || 'N/A'}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Fecha Inicio</span>
              <span class="info-value">${fecha}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Estado</span>
              <span class="info-value">${tramite.estado || 'N/A'}</span>
            </div>
          </div>
          <div class="tramite-actions">
            <button class="btn btn-navy" onclick="verDetalleCliente(${tramite.clienteId})">👤 Ver Cliente</button>
            <button class="btn btn-outline" onclick="cambiarEstadoTramite(${tramite.id}, '${tramite.estado}')">🔄 Cambiar Estado</button>
          </div>
        </div>
      </div>
    `;
  });

  container.innerHTML = html;
  document.getElementById('results-count').textContent = tramites.length;
  actualizarEstadisticas(tramites);
}

function buscarPorNombre() {
  const searchText = document.getElementById('searchInput').value.toLowerCase().trim();

  let tramitesFiltrados = [...todosLosTramites];

  // Filtro por nombre de cliente
  if (searchText) {
    tramitesFiltrados = tramitesFiltrados.filter(t =>
      t.nombreCliente && t.nombreCliente.toLowerCase().includes(searchText)
    );
  }

  console.log('Trámites filtrados:', tramitesFiltrados);
  mostrarTramites(tramitesFiltrados);
}

function limpiarBusqueda() {
  document.getElementById('searchInput').value = '';
  mostrarTramites(todosLosTramites);
}

function actualizarEstadisticas(tramites) {
  const total = tramites.length;
  const pendientes = tramites.filter(t => t.estado === 'PENDIENTE').length;
  const iniciados = tramites.filter(t => t.estado === 'INICIADO').length;
  const terminados = tramites.filter(t => t.estado === 'TERMINADO').length;

  document.getElementById('stat-total').textContent = total;
  document.getElementById('stat-pendiente').textContent = pendientes;
  document.getElementById('stat-iniciado').textContent = iniciados;
  document.getElementById('stat-terminado').textContent = terminados;
}

function verDetalleCliente(idCliente) {
  // Redirigir al listado de clientes
  window.location.href = 'listado-clientes.html';
}

// Variables globales para el modal de cambio de estado
let tramiteIdActual = null;
let tramiteEstadoActual = null;

function cambiarEstadoTramite(idTramite, estadoActual) {
  // Buscar el trámite en la lista
  const tramite = todosLosTramites.find(t => t.id === idTramite);
  if (!tramite) {
    showError('Trámite no encontrado');
    return;
  }

  // Guardar el ID y estado actual
  tramiteIdActual = idTramite;
  tramiteEstadoActual = estadoActual;

  // Actualizar el texto del modal
  document.getElementById('modal-tramite-info').textContent =
    `Trámite: ${tramite.nombreTramite} | Cliente: ${tramite.nombreCliente} | Estado actual: ${estadoActual}`;

  // Pre-seleccionar el estado actual en el selector
  document.getElementById('modal-nuevo-estado').value = estadoActual;

  // Mostrar el modal
  showModal('modal-cambiar-estado');
}

async function confirmarCambioEstado() {
  const nuevoEstado = document.getElementById('modal-nuevo-estado').value;

  if (!nuevoEstado) {
    showError('Debe seleccionar un estado');
    return;
  }

  if (nuevoEstado === tramiteEstadoActual) {
    showError('El estado seleccionado es el mismo que el actual');
    return;
  }

  try {
    // Llamar al servicio para actualizar el estado
    const resultado = await API.EstadoTramites.actualizarEstado(tramiteIdActual, nuevoEstado);

    console.log('Estado actualizado:', resultado);
    showSuccess(`✅ Estado actualizado a: ${nuevoEstado}`);

    // Cerrar modal
    cerrarModalEstado();

    // Recargar los trámites para mostrar el cambio
    await cargarTodosLosTramites();

  } catch (error) {
    console.error('Error al cambiar estado:', error);
    showError(error.message || 'Error al cambiar el estado del trámite');
  }
}

function cerrarModalEstado() {
  const modal = document.getElementById('modal-cambiar-estado');
  if (modal) {
    modal.classList.remove('show');
  }
  tramiteIdActual = null;
  tramiteEstadoActual = null;
}

// ============================================================
// DATOS SENSIBLES
// ============================================================

function initDatos() {
  cargarClientesEnSelector();
  configurarTogglePasswords();
  configurarFormularioDatos();
}

async function cargarClientesEnSelector() {
  const selector = document.getElementById('selectCliente');
  if (!selector) return;

  try {
    selector.innerHTML = '<option value="">Cargando clientes...</option>';

    const resultado = await API.Clientes.listar();
    const clientes = resultado.data || resultado;

    if (!clientes || clientes.length === 0) {
      selector.innerHTML = '<option value="">No hay clientes registrados</option>';
      return;
    }

    // Generar opciones
    let html = '<option value="">-- Seleccionar cliente --</option>';
    clientes.forEach(cliente => {
      html += `<option value="${cliente.id || cliente.idCliente}">${cliente.nombreCompleto} (DPI: ${cliente.dpi})</option>`;
    });

    selector.innerHTML = html;

    // Evento al seleccionar un cliente
    selector.addEventListener('change', async (e) => {
      const idCliente = e.target.value;
      if (idCliente) {
        await cargarDatosExistentes(idCliente);
      } else {
        limpiarFormularioDatos();
      }
    });

  } catch (error) {
    console.error('Error al cargar clientes:', error);
    selector.innerHTML = '<option value="">Error al cargar clientes</option>';
  }
}

async function cargarDatosExistentes(idCliente) {
  try {
    const resultado = await API.Datos.obtener(idCliente);
    const datos = resultado.data || resultado;

    // Rellenar formulario con datos existentes
    document.querySelector('[name="nit"]').value = datos.nit || '';
    document.querySelector('[name="nis"]').value = datos.nis || '';
    document.querySelector('[name="email"]').value = datos.email || '';
    document.querySelector('[name="dpi"]').value = datos.dpi || '';
    document.querySelector('[name="cuentaBancaria"]').value = datos.cuentaBancaria || '';
    document.querySelector('[name="contrasenaAgenciaVirtual"]').value = datos.contrasenaAgenciaVirtual || '';
    document.querySelector('[name="contrasenaCorreo"]').value = datos.contrasenaCorreo || '';
    document.querySelector('[name="contrasenaCgc"]').value = datos.contrasenaCgc || '';
    document.querySelector('[name="contrasenaConsultaGeneral"]').value = datos.contrasenaConsultaGeneral || '';
    document.querySelector('[name="contrasenaRegahe"]').value = datos.contrasenaRegahe || '';

    console.log('✅ Datos cargados para el cliente');

  } catch (error) {
    // No hay datos previos, está bien
    console.log('ℹ️ No hay datos previos para este cliente');
    limpiarFormularioDatos();
  }
}

function limpiarFormularioDatos() {
  const campos = ['nit', 'nis', 'email', 'dpi', 'cuentaBancaria',
    'contrasenaAgenciaVirtual', 'contrasenaCorreo', 'contrasenaCgc',
    'contrasenaConsultaGeneral', 'contrasenaRegahe'];

  campos.forEach(campo => {
    const input = document.querySelector(`[name="${campo}"]`);
    if (input) input.value = '';
  });
}

function configurarTogglePasswords() {
  const toggleButtons = document.querySelectorAll('.password-toggle');

  toggleButtons.forEach(btn => {
    btn.addEventListener('click', (e) => {
      const targetName = e.target.dataset.target;
      const input = document.querySelector(`[name="${targetName}"]`);

      if (input) {
        if (input.type === 'password') {
          input.type = 'text';
          e.target.textContent = '🙈';
        } else {
          input.type = 'password';
          e.target.textContent = '👁️';
        }
      }
    });
  });
}

function configurarFormularioDatos() {
  const form = document.getElementById('datosForm');
  if (!form) return;

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const datos = getFormData(form);

    // Validar que se haya seleccionado un cliente
    if (!datos.idCliente || datos.idCliente === '') {
      showError('Debe seleccionar un cliente');
      return;
    }

    // Preparar datos para enviar
    const datosSensibles = {
      nit: datos.nit || null,
      nis: datos.nis ? parseInt(datos.nis) : null,
      email: datos.email || null,
      dpi: datos.dpi || null,
      cuentaBancaria: datos.cuentaBancaria || null,
      contrasenaAgenciaVirtual: datos.contrasenaAgenciaVirtual || null,
      contrasenaCorreo: datos.contrasenaCorreo || null,
      contrasenaCgc: datos.contrasenaCgc || null,
      contrasenaConsultaGeneral: datos.contrasenaConsultaGeneral || null,
      contrasenaRegahe: datos.contrasenaRegahe || null
    };

    try {
      setLoading(form, true);

      const resultado = await API.Datos.guardar(datos.idCliente, datosSensibles);

      console.log('Datos sensibles guardados:', resultado);
      showModal('modal-datos-ok');

    } catch (error) {
      console.error('Error al guardar datos:', error);
      showError(error.message || 'Error al guardar datos sensibles');
    } finally {
      setLoading(form, false);
    }
  });

  document.getElementById('datos-ok')?.addEventListener('click', () => {
    // Mantener el formulario con los datos guardados
    document.getElementById('modal-datos-ok').classList.remove('show');
  });
}

// ============================================================
// BOOT
// ============================================================

document.addEventListener('DOMContentLoaded', () => {
  const page = document.body.dataset.page;

  console.log('📄 Página actual:', page);
  console.log('⚙️ Modo:', CONFIG.USAR_MOCK ? 'MOCK (Desarrollo)' : 'BACKEND REAL');

  switch (page) {
    case 'login':
      initLogin();
      break;
    case 'register':
      initRegister();
      break;
    case 'dashboard':
      initDashboard();
      break;
    case 'clientes':
      initClientes();
      break;
    case 'tramites':
      initTramites();
      break;
    case 'listado-clientes':
      initListadoClientes();
      break;
    case 'consulta-tramites':
      initConsultaTramites();
      break;
    case 'tipos-tramites':
      initTiposTramites();
      break;
    case 'datos':
      initDatos();
      break;
  }
});

// ============================================================
// TIPOS DE TRÁMITES
// ============================================================

let tipoEditandoId = null;

function initTiposTramites() {
  cargarTiposTramites();
  configurarFormTipo();
}

async function cargarTiposTramites() {
  const container = document.getElementById('tipos-list');
  if (!container) return;

  try {
    container.innerHTML = '<div style="text-align:center;padding:40px;color:#5a0fb2;">⏳ Cargando tipos...</div>';

    const resultado = await API.TiposTramites.listar();
    const tipos = resultado.data || resultado || [];

    if (tipos.length === 0) {
      container.innerHTML = `
        <div class="no-data">
          <div style="font-size:48px;margin-bottom:16px;">📋</div>
          <p>No hay tipos de trámites registrados</p>
          <button class="btn btn-primary" onclick="abrirModalNuevoTipo()" style="margin-top:16px;">➕ Agregar Primer Tipo</button>
        </div>
      `;
      return;
    }

    mostrarTiposTramites(tipos);

  } catch (error) {
    console.error('Error al cargar tipos:', error);
    container.innerHTML = `
      <div class="no-data">
        <div style="font-size:48px;margin-bottom:16px;">❌</div>
        <p style="color:#d32f2f;">Error al cargar tipos de trámites</p>
        <p style="color:#666;font-size:14px;">${error.message}</p>
      </div>
    `;
  }
}

function mostrarTiposTramites(tipos) {
  const container = document.getElementById('tipos-list');
  if (!container) return;

  let html = `
    <table class="tipos-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>CATEGORÍA/PORTAL</th>
          <th>ENLACE</th>
          <th style="width:100px;text-align:center;">ACCIONES</th>
        </tr>
      </thead>
      <tbody>
  `;

  tipos.forEach(tipo => {
    const badgeClass = getBadgeClass(tipo.portal);

    html += `
      <tr>
        <td>${tipo.id}</td>
        <td><span class="tipo-badge ${badgeClass}">${tipo.portal || 'N/A'}</span></td>
        <td class="link-cell">
          ${tipo.link ? `<a href="${tipo.link}" target="_blank">${tipo.link}</a>` : '<span style="color:#999;">Sin enlace</span>'}
        </td>
        <td>
          <div class="actions-cell">
            <button class="btn-icon btn-edit" onclick="editarTipo(${tipo.id})" title="Editar">✏️</button>
            <button class="btn-icon btn-delete" onclick="eliminarTipo(${tipo.id}, '${tipo.portal}')" title="Eliminar">🗑️</button>
          </div>
        </td>
      </tr>
    `;
  });

  html += `
      </tbody>
    </table>
  `;

  container.innerHTML = html;
}

function getBadgeClass(portal) {
  if (!portal) return 'badge-otro';
  const portalUpper = portal.toUpperCase();
  if (portalUpper.includes('SAT')) return 'badge-sat';
  if (portalUpper.includes('CGC')) return 'badge-cgc';
  if (portalUpper.includes('RAE') || portalUpper.includes('CONSULTA')) return 'badge-rae';
  if (portalUpper.includes('RENAS') || portalUpper.includes('RENAP')) return 'badge-renas';
  return 'badge-otro';
}

function abrirModalNuevoTipo() {
  tipoEditandoId = null;
  document.getElementById('modal-title').textContent = 'Agregar Nuevo Tipo de Trámite';
  document.getElementById('form-tipo').reset();
  showModal('modal-tipo');
}

async function editarTipo(id) {
  try {
    const resultado = await API.TiposTramites.listar();
    const tipos = resultado.data || resultado || [];
    const tipo = tipos.find(t => t.id === id);

    if (!tipo) {
      showError('Tipo no encontrado');
      return;
    }

    tipoEditandoId = id;
    document.getElementById('modal-title').textContent = 'Editar Tipo de Trámite';
    document.getElementById('input-portal').value = tipo.portal || '';
    document.getElementById('input-link').value = tipo.link || '';
    showModal('modal-tipo');

  } catch (error) {
    console.error('Error al cargar tipo:', error);
    showError('Error al cargar los datos del tipo');
  }
}

async function eliminarTipo(id, portal) {
  const confirmar = confirm(`¿Está seguro de eliminar el tipo "${portal}"?\n\nEsta acción no se puede deshacer.`);
  if (!confirmar) return;

  try {
    await API.TiposTramites.eliminar(id);
    showSuccess('✅ Tipo eliminado exitosamente');
    cargarTiposTramites();

  } catch (error) {
    console.error('Error al eliminar tipo:', error);
    showError(error.message || 'Error al eliminar el tipo');
  }
}

function cerrarModalTipo() {
  const modal = document.getElementById('modal-tipo');
  if (modal) {
    modal.classList.remove('show');
  }
  tipoEditandoId = null;
  document.getElementById('form-tipo').reset();
}

function configurarFormTipo() {
  const form = document.getElementById('form-tipo');
  if (!form) return;

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const datos = {
      portal: document.getElementById('input-portal').value.trim(),
      link: document.getElementById('input-link').value.trim() || null
    };

    if (!datos.portal) {
      showError('La categoría/portal es requerida');
      return;
    }

    try {
      if (tipoEditandoId) {
        // Actualizar
        await API.TiposTramites.actualizar(tipoEditandoId, datos);
        showSuccess('✅ Tipo actualizado exitosamente');
      } else {
        // Crear
        await API.TiposTramites.crear(datos);
        showSuccess('✅ Tipo creado exitosamente');
      }

      cerrarModalTipo();
      cargarTiposTramites();

    } catch (error) {
      console.error('Error al guardar tipo:', error);
      showError(error.message || 'Error al guardar el tipo');
    }
  });
}
