// =====================
// Config
// =====================
const API_BASE = "http://localhost:8080/api"; // ajusta si cambiaste puerto
const LOGIN_URL = `${API_BASE}/auth/login`;
const REGISTER_URL = `${API_BASE}/auth/register`;
const CLIENTES_URL = `${API_BASE}/clientes`;
const GRUPOS_URL = `${API_BASE}/grupos`;
const TRAMITES_URL = `${API_BASE}/tramites`;
const ESTADOS_URL = `${API_BASE}/estados`;

// Helpers
const $  = (sel, root=document) => root.querySelector(sel);
const $$ = (sel, root=document) => Array.from(root.querySelectorAll(sel));
const go = (url) => location.href = url;
const authHeaders = () => {
  const t = localStorage.getItem("token");
  return t ? { Authorization: `Bearer ${t}` } : {};
};

// =====================
// NAV / ENLACES
// =====================
$("#goRegister")?.addEventListener("click", (e) => {
  e.preventDefault();
  go("register.html");
});
document.querySelectorAll('a[href="#"][data-go]')?.forEach(a => {
  a.addEventListener("click", (e) => {
    e.preventDefault();
    const url = a.dataset.go;
    if (url) go(url);
  });
});

// =====================
// LOGIN
// =====================
if (document.body.dataset.page === "login") {
  $("#loginForm")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const inputs = $$(".login-left #loginForm .input");
    const username = inputs[0]?.value?.trim() ?? "";
    const password = inputs[1]?.value?.trim() ?? "";
    if (!username || !password) return alert("Usuario y contraseña son requeridos");

    try {
      const r = await fetch(LOGIN_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
      });
      if (!r.ok) throw new Error("Credenciales inválidas");
      const data = await r.json();
      if (data?.token) localStorage.setItem("token", data.token);
      go("dashboard.html");
    } catch (err) {
      console.error(err);
      alert("No se pudo iniciar sesión");
    }
  });
}

// =====================
// REGISTRO (register.html)
// =====================
if (document.body.dataset.page === "register") {
  $("#registerForm")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const inputs = $$("#registerForm .form-grid .input");
    const nombre   = inputs[0]?.value?.trim() ?? "";
    const apellido = inputs[1]?.value?.trim() ?? "";
    const username = inputs[2]?.value?.trim() ?? "";
    const email    = inputs[3]?.value?.trim() ?? "";
    const password = inputs[4]?.value ?? "";
    const confirm  = inputs[5]?.value ?? "";
    if (!nombre || !apellido || !username || !email || !password || !confirm) {
      return alert("Completa todos los campos");
    }
    if (password !== confirm) {
      return alert("La confirmación no coincide");
    }

    try {
      const r = await fetch(REGISTER_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json", ...authHeaders() },
        body: JSON.stringify({ nombre, apellido, username, email, password })
      });
      if (!r.ok) throw new Error("No se pudo registrar");
      const modal = $("#modal-registered");
      if (modal) {
        modal.classList.add("show");
        $("#registered-ok")?.addEventListener("click", () => {
          modal.classList.remove("show");
          go("login.html");
        }, { once: true });
      } else {
        alert("Usuario registrado");
        go("login.html");
      }
    } catch (err) {
      console.error(err);
      alert("Error al registrar");
    }
  });
}

// =====================
// CLIENTES - AGREGAR (clientes.html)
// =====================
if (document.body.dataset.page === "clientes") {
  $("#clientesForm")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const inputs = $$("#clientesForm .form-col .input");
    const payload = {
      // 0..11 según tu formulario
      nombre:         inputs[0]?.value?.trim() ?? "",
      apellidos:      inputs[1]?.value?.trim() ?? "",
      telefono:       inputs[2]?.value?.trim() ?? "",
      direccion:      inputs[3]?.value?.trim() ?? "",
      nis:            inputs[4]?.value?.trim() ?? "",
      fechaNacimiento: inputs[5]?.value?.trim() || null, // si no usas F/N, tu backend lo ignora
      dpi:            inputs[6]?.value?.trim() ?? "",
      nit:            inputs[7]?.value?.trim() ?? "",
      cuentaBancaria: inputs[8]?.value?.trim() ?? "",
      passNit:        inputs[9]?.value ?? "",
      passCgc:        inputs[10]?.value ?? "",
      passGeneral:    "", // no lo usas visualmente
      passReghae:     inputs[11]?.value ?? ""
    };

    if (!payload.nombre || !payload.apellidos) {
      return alert("Nombre y Apellidos son requeridos");
    }

    try {
      const r = await fetch(CLIENTES_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json", ...authHeaders() },
        body: JSON.stringify(payload)
      });
      if (!r.ok) throw new Error("Error al guardar");

      const modal = $("#modal-clientes-ok");
      if (modal) {
        modal.classList.add("show");
        $("#clientes-ok")?.addEventListener("click", () => {
          modal.classList.remove("show");
        }, { once: true });
      } else {
        alert("Cliente guardado");
      }
      inputs.forEach(i => (i.value = ""));
    } catch (err) {
      console.error(err);
      alert("No se pudo guardar el cliente");
    }
  });
}

// =====================
// ESTADO DE TRÁMITES (estado-tramites.html)
// =====================
window.cambiarEstado = async function(selectEl) {
  const tr = selectEl.closest("tr");
  const id = tr?.getAttribute("data-id");
  if (!id) return;
  const mapa = {
    "Pendiente": "PENDIENTE",
    "En Proceso": "EN_PROCESO",
    "Finalizado": "FINALIZADO",
    "Rechazado": "RECHAZADO"
  };
  const nuevo = mapa[selectEl.value] || "PENDIENTE";
  try {
    const r = await fetch(`${ESTADOS_URL}/${id}?estado=${encodeURIComponent(nuevo)}`, {
      method: "PATCH",
      headers: { ...authHeaders() }
    });
    if (!r.ok) throw new Error("No se pudo actualizar");
  } catch (err) {
    console.error(err);
    alert("Error al cambiar estado");
  }
};

// =====================
// LISTADO DE CLIENTES (buscar + paginar + editar/guardar/eliminar)
// =====================
(() => {
  if (document.body.dataset.page !== "listado-clientes") return;

  const API_B = window.API_BASE ?? "http://localhost:8080/api";
  const CLIENTES_URL_B = `${API_B}/clientes`;

  const $local = (s, r=document) => r.querySelector(s);
  const searchInput = $local("#searchInput");
  const tbody       = $local("#tableBody");
  const prevBtn     = $local("#prevPage");
  const nextBtn     = $local("#nextPage");
  const pagText     = $local("#paginationText");
  const rowsSel     = $local("#rowsPerPage");

  let q = "";
  let page = 0;
  let size = parseInt(rowsSel?.value || "10", 10);
  let totalPages = 0;
  let totalElements = 0;

  const esc = v => (v ?? "").toString()
    .replace(/&/g,"&amp;").replace(/</g,"&lt;")
    .replace(/>/g,"&gt;").replace(/"/g,"&quot;").replace(/'/g,"&#39;");

  const parseApi = (json) => {
    const data = json?.data ?? json; // ApiResponse<PageResponse<T>> o Page<T>
    return {
      items: data?.items ?? data?.content ?? [],
      totalPages: data?.totalPages ?? 1,
      totalElements: data?.totalElements ?? 0,
      number: data?.page ?? data?.number ?? 0,
      size: data?.size ?? size
    };
  };

  // -------- helpers de edición y modales --------
  function setRowEdit(tr, editing) {
    // No editar columna 6 (F/N) ni la última (acciones)
    const spans = tr.querySelectorAll('td:not(:nth-child(6)):not(:last-child) .cell-value');
    spans.forEach(sp => {
      if ((sp.dataset.key || '') === 'fechaNacimiento') return;
      if (editing) {
        sp.setAttribute('contenteditable', 'true');
        sp.classList.add('editing');
      } else {
        sp.removeAttribute('contenteditable');
        sp.classList.remove('editing');
      }
    });

    const btnEdit   = tr.querySelector('[data-act="edit"]');
    const btnSave   = tr.querySelector('[data-act="save"]');
    const btnDelete = tr.querySelector('[data-act="delete"]');
    if (btnEdit)   btnEdit.hidden   = editing;
    if (btnSave)   btnSave.hidden   = !editing;
    if (btnDelete) btnDelete.hidden = !editing;
  }

  function getRowPayload(tr) {
    const read = k => (tr.querySelector(`.cell-value[data-key="${k}"]`)?.textContent || '').trim();
    return {
      nombre:         read('nombre'),
      apellidos:      read('apellidos'),
      telefono:       read('telefono'),
      direccion:      read('direccion'),
      nis:            read('nis'),
      dpi:            read('dpi'),
      nit:            read('nit'),
      cuentaBancaria: read('cuentaBancaria'),
      passNit:        read('passNit'),
      passCgc:        read('passCgc'),
      passReghae:     read('passReghae')
    };
  }

  function showModal(id, onOk) {
    const m = document.getElementById(id);
    if (!m) return onOk?.();
    m.classList.add('show');
    m.style.display = 'block';

    const okBtn   = m.querySelector('#save-ok, #delete-ok');
    const closeEl = m.querySelector('[data-close]');

    const handleClose = () => {
      m.classList.remove('show');
      m.style.display = 'none';
      okBtn?.removeEventListener('click', handleOk);
      closeEl?.removeEventListener('click', handleClose);
    };
    const handleOk = async () => {
      try { await onOk?.(); } finally { handleClose(); }
    };

    okBtn?.addEventListener('click', handleOk, { once: true });
    closeEl?.addEventListener('click', handleClose, { once: true });
  }

  async function loadClientes() {
    const url = `${CLIENTES_URL_B}?q=${encodeURIComponent(q)}&page=${page}&size=${size}`;
    const r = await fetch(url, { headers: { "Accept": "application/json" } });
    if (!r.ok) throw new Error(`HTTP ${r.status}`);

    const json   = await r.json();
    const parsed = parseApi(json);
    totalPages     = parsed.totalPages;
    totalElements  = parsed.totalElements;

    tbody.innerHTML = parsed.items.map(c => `
      <tr data-id="${c.id ?? ""}">
        <td><span class="cell-value" data-key="nombre">${esc(c.nombre)}</span></td>
        <td><span class="cell-value" data-key="apellidos">${esc(c.apellidos)}</span></td>
        <td><span class="cell-value" data-key="telefono">${esc(c.telefono)}</span></td>
        <td><span class="cell-value" data-key="direccion">${esc(c.direccion)}</span></td>
        <td><span class="cell-value" data-key="nis">${esc(c.nis)}</span></td>
        <td><span class="cell-value" data-key="fechaNacimiento"></span></td>
        <td><span class="cell-value" data-key="dpi">${esc(c.dpi ?? "")}</span></td>
        <td><span class="cell-value" data-key="nit">${esc(c.nit ?? "")}</span></td>
        <td><span class="cell-value" data-key="cuentaBancaria">${esc(c.cuentaBancaria ?? "")}</span></td>
        <td><span class="cell-value" data-key="passNit">${esc(c.passNit ?? "")}</span></td>
        <td><span class="cell-value" data-key="passCgc">${esc(c.passCgc ?? "")}</span></td>
        <td><span class="cell-value" data-key="passReghae">${esc(c.passReghae ?? "")}</span></td>
        <td class="actions-cell">
          <button class="btn-table btn-modify" data-act="edit">MODIFICAR</button>
          <button class="btn-table btn-save"   data-act="save"   hidden>GUARDAR</button>
          <button class="btn-table btn-delete" data-act="delete" hidden>ELIMINAR</button>
        </td>
      </tr>
    `).join("");

    const from = totalElements === 0 ? 0 : (page * size + 1);
    const to   = Math.min((page + 1) * size, totalElements);
    if (pagText) pagText.textContent = `${from}-${to} of ${totalElements}`;
    if (prevBtn) prevBtn.disabled = page <= 0;
    if (nextBtn) nextBtn.disabled = page >= (totalPages - 1);
  }

  // Delegación de acciones
  tbody.addEventListener("click", async (ev) => {
    const btn = ev.target.closest("button[data-act]");
    if (!btn) return;
    const tr  = btn.closest("tr");
    const id  = tr.getAttribute('data-id');
    const act = btn.dataset.act;

    if (act === "edit") {
      setRowEdit(tr, true);
      return;
    }

    if (act === "save") {
      try {
        const payload = getRowPayload(tr);
        const r = await fetch(`${CLIENTES_URL_B}/${id}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload)
        });
        if (!r.ok) throw new Error(`HTTP ${r.status}`);
        showModal('modal-save', async () => { await loadClientes(); });
      } catch (e) {
        console.error(e);
        alert("No se pudo guardar los cambios");
      }
      return;
    }

    if (act === "delete") {
      try {
        const r = await fetch(`${CLIENTES_URL_B}/${id}`, { method: "DELETE" });
        if (!r.ok) throw new Error(`HTTP ${r.status}`);
        showModal('modal-delete', async () => {
          if (page > 0 && (totalElements - 1) <= page * size) page--;
          await loadClientes();
        });
      } catch (e) {
        console.error(e);
        alert("No se pudo eliminar");
      }
      return;
    }
  });

  // Buscar (debounce)
  let t;
  searchInput?.addEventListener("input", () => {
    clearTimeout(t);
    t = setTimeout(() => {
      q = searchInput.value.trim();
      page = 0;
      loadClientes().catch(console.error);
    }, 300);
  });

  // Paginación
  prevBtn?.addEventListener("click", () => { if (page > 0) { page--; loadClientes().catch(console.error); } });
  nextBtn?.addEventListener("click", () => { if (page < totalPages - 1) { page++; loadClientes().catch(console.error); } });
  rowsSel?.addEventListener("change", () => {
    size = parseInt(rowsSel.value, 10) || 10;
    page = 0;
    loadClientes().catch(console.error);
  });

  // 1a carga
  loadClientes().catch(e => {
    console.error("Error cargando clientes:", e);
    tbody.innerHTML = `<tr><td colspan="13">No se pudo cargar el listado.</td></tr>`;
  });
})();
// ======= LISTADO DE TRÁMITES: cargar, modificar, agregar (con grupoId y CRUD de grupos) =======
(() => {
  if (document.body.dataset.page !== "listado-tramites") return;

  // ---------- utils modal ----------
  const openModal  = (id) => document.getElementById(id)?.classList.add("show");
  const closeModal = (id) => document.getElementById(id)?.classList.remove("show");

  // cerrar con [data-close], backdrop y ESC
  document.querySelectorAll(".modal [data-close]").forEach(b=>{
    b.addEventListener("click", ()=> b.closest(".modal")?.classList.remove("show"));
  });
  document.querySelectorAll(".modal").forEach(m=>{
    m.addEventListener("click", (e)=> { if(e.target === m) m.classList.remove("show"); });
  });
  document.addEventListener("keydown", (e)=> {
    if (e.key === "Escape") document.querySelectorAll(".modal.show").forEach(m=>m.classList.remove("show"));
  });

  const esc = v => (v ?? "").toString()
    .replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")
    .replace(/"/g,"&quot;").replace(/'/g,"&#39;");

  // ---------- refs ----------
  const tbody   = document.getElementById("tramitesTableBody");
  const rowsSel = document.getElementById("rowsPerPageTramites");
  const prevBtn = document.getElementById("prevPageTramites");
  const nextBtn = document.getElementById("nextPageTramites");
  const pagTxt  = document.getElementById("paginationTextTramites");
  const searchI = document.getElementById("searchInputTramites");

  const selAddGrupo  = document.getElementById("tramiteGrupo");
  const selEditGrupo = document.getElementById("editTramiteGrupo");

  // modal grupos
  const gruposTBody = document.getElementById("gruposTableBody");

  // ---------- estado ----------
  let q = ""; let page = 0; let size = parseInt(rowsSel?.value || "10", 10);
  let totalPages = 1; let totalElements = 0;
  let filaSeleccionada = null;
  let filaGrupoSeleccionada = null;
  const gruposIndex = new Map(); // id -> nombre

  // FIX ruta absoluta para evitar “No static resource …”
  const GRUPOS_API = (typeof GRUPOS_URL === "string" && GRUPOS_URL.startsWith("http"))
    ? GRUPOS_URL
    : "http://localhost:8080/api/grupos";

  // Helper para leer error del backend de forma útil
  async function throwIfNotOk(res, context="") {
    if (res.ok) return;
    let msg = `HTTP ${res.status}`;
    try {
      const ct = res.headers.get("content-type") || "";
      if (ct.includes("application/json")) {
        const j = await res.json();
        msg += j?.message ? ` – ${j.message}` : j?.error ? ` – ${j.error}` : "";
      } else {
        const t = await res.text();
        if (t) msg += ` – ${t}`;
      }
    } catch (_) {}
    throw new Error(context ? `${context}: ${msg}` : msg);
  }

  // ---------- cargar grupos (para selects y tabla) ----------
  async function loadGrupos() {
    try {
      const r = await fetch(GRUPOS_API, { headers: { Accept:"application/json", ...authHeaders() } });
      await throwIfNotOk(r, "Listar grupos");
      const json = await r.json();
      const data = json?.data ?? json;
      const items = Array.isArray(data) ? data : (data?.items ?? data?.content ?? []);

      gruposIndex.clear();
      if (selAddGrupo)  selAddGrupo.innerHTML  = `<option value="">Seleccione un grupo</option>`;
      if (selEditGrupo) selEditGrupo.innerHTML = `<option value="">Seleccione un grupo</option>`;
      items.forEach(g=>{
        gruposIndex.set(String(g.id), g.nombre);
        if (selAddGrupo)  selAddGrupo.add(new Option(g.nombre, g.id));
        if (selEditGrupo) selEditGrupo.add(new Option(g.nombre, g.id));
      });
      return items;
    } catch (e) {
      console.error(e);
      return [];
    }
  }

  // ---------- trámites ----------
  async function fetchListaTramites() {
    const url = `${TRAMITES_URL}${q ? `?q=${encodeURIComponent(q)}` : ""}`;
    const r = await fetch(url, { headers: { "Accept": "application/json", ...authHeaders() } });
    await throwIfNotOk(r, "Listar trámites");
    return r.json();
  }
  function parseLista(json) {
    const data = json?.data ?? json;
    const items = Array.isArray(data) ? data : (data?.items ?? data?.content ?? []);
    totalPages    = Array.isArray(data) ? 1 : (data?.totalPages ?? 1);
    totalElements = Array.isArray(data) ? items.length : (data?.totalElements ?? items.length);
    return items;
  }
  async function loadTramites() {
    try {
      const json  = await fetchListaTramites();
      const items = parseLista(json);
      tbody.innerHTML = items.map(t => {
        const grupoNombre = t.grupoNombre ?? t.grupo?.nombre ?? (t.grupoId ? (gruposIndex.get(String(t.grupoId)) || "") : "");
        return `
          <tr data-id="${esc(t.id)}" data-grupo-id="${esc(t.grupoId ?? t.grupo?.id ?? "")}" class="row-white">
            <td><span class="cell-value">${esc(t.nombre)}</span></td>
            <td><span class="cell-value">${esc(t.descripcion)}</span></td>
            <td><span class="cell-value">${esc(t.requisitos)}</span></td>
            <td><span class="cell-value">${esc(grupoNombre)}</span></td>
            <td><span class="cell-value">${esc(t.link ?? "")}</span></td>
            <td><button class="btn-table btn-modify">MODIFICAR</button></td>
          </tr>
        `;
      }).join("");

      const from = totalElements === 0 ? 0 : (page * size + 1);
      const to   = Math.min((page + 1) * size, totalElements);
      pagTxt && (pagTxt.textContent = `${from}-${to} of ${totalElements}`);
      prevBtn && (prevBtn.disabled = page <= 0);
      nextBtn && (nextBtn.disabled = page >= (totalPages - 1));
    } catch (e) {
      console.error("No se pudo cargar trámites:", e);
      tbody.innerHTML = `<tr><td colspan="6">No se pudo cargar el listado.</td></tr>`;
    }
  }
  window.loadTramites = loadTramites;

  // ---------- click "MODIFICAR" (tramite) ----------
  tbody.addEventListener("click", (ev) => {
    const btn = ev.target.closest(".btn-modify");
    if (!btn) return;
    filaSeleccionada = btn.closest("tr");
    openModal("modal-modificar-choice");
  });

  // Abrir modal para editar trámite
  window.abrirModificarTramite = function () {
    closeModal("modal-modificar-choice");
    if (!filaSeleccionada) return;

    const cells = [...filaSeleccionada.querySelectorAll(".cell-value")];
    const nombre      = cells[0]?.textContent.trim() || "";
    const descripcion = cells[1]?.textContent.trim() || "";
    const requisitos  = cells[2]?.textContent.trim() || "";
    const link        = cells[4]?.textContent.trim() || "";
    const grupoId     = filaSeleccionada.getAttribute("data-grupo-id") || "";

    document.getElementById("editTramiteNombre").value      = nombre;
    document.getElementById("editTramiteDescripcion").value = descripcion;
    document.getElementById("editTramiteRequisitos").value  = requisitos;
    document.getElementById("editTramiteLink").value        = link;
    selEditGrupo.value = grupoId || "";

    openModal("modal-modificar-tramite");
  };

  // Guardar (PUT /tramites/{id})
  document.getElementById("formModificarTramite")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    if (!filaSeleccionada) return;
    const id = filaSeleccionada.getAttribute("data-id");
    if (!id) return alert("No se encontró el ID del trámite.");

    const payload = {
      nombre:      document.getElementById("editTramiteNombre").value.trim(),
      descripcion: document.getElementById("editTramiteDescripcion").value.trim(),
      requisitos:  document.getElementById("editTramiteRequisitos").value.trim(),
      link:        document.getElementById("editTramiteLink").value.trim(),
      grupoId:     selEditGrupo.value ? Number(selEditGrupo.value) : null
    };

    try {
      const r = await fetch(`${TRAMITES_URL}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json", ...authHeaders() },
        body: JSON.stringify(payload)
      });
      await throwIfNotOk(r, `Actualizar trámite ${id}`);

      closeModal("modal-modificar-tramite");
      openModal("modal-tramite-modificado");
      document.getElementById("btn-aceptar-modificar-tramite").onclick = async () => {
        closeModal("modal-tramite-modificado");
        await loadTramites();
        filaSeleccionada = null;
      };
    } catch (err) {
      console.error(err);
      alert(String(err.message || err));
    }
  });

  // Botón verde (+)
  window.showMainModal = () => openModal("modal-main-choice");
  window.showAgregarTramiteModal = () => { closeModal("modal-main-choice"); openModal("modal-agregar-tramite"); };
  window.showAgregarTipoModal    = () => { closeModal("modal-main-choice"); openModal("modal-agregar-tipo");    };

  // Agregar Trámite (POST /tramites)
  document.getElementById("formAgregarTramite")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const payload = {
      nombre:      document.getElementById("tramiteNombre").value.trim(),
      descripcion: document.getElementById("tramiteDescripcion").value.trim(),
      requisitos:  document.getElementById("tramiteRequisitos").value.trim(),
      link:        document.getElementById("tramiteLink").value.trim(),
      grupoId:     selAddGrupo.value ? Number(selAddGrupo.value) : null
    };
    try {
      const r = await fetch(TRAMITES_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json", ...authHeaders() },
        body: JSON.stringify(payload)
      });
      await throwIfNotOk(r, "Crear trámite");

      closeModal("modal-agregar-tramite");
      openModal("modal-tramite-guardado");
      document.getElementById("btn-aceptar-tramite").onclick = async () => {
        closeModal("modal-tramite-guardado");
        await loadTramites();
        document.getElementById("formAgregarTramite").reset();
      };
    } catch (e2) {
      console.error(e2);
      alert(String(e2.message || e2));
    }
  });

  // ============================================================
  // ============  MODIFICAR TIPO/GRUPO (CRUD en BD) ============
  // ============================================================

  // Abrir modal → cargar grupos → pintar filas editables
  window.abrirModificarTipo = async function () {
    closeModal("modal-modificar-choice");
    const grupos = await loadGrupos();
    renderListadoGrupos(grupos);
    openModal("modal-listado-grupos");
  };

  // Render editable: SOLO se edita la fila seleccionada
  function renderListadoGrupos(items=[]) {
    if (!gruposTBody) return;
    gruposTBody.innerHTML = items.map((g, i) => `
      <tr class="${i%2 ? 'row-white' : 'row-pink'}" data-id="${esc(g.id)}"
          data-original-nombre="${esc(g.nombre ?? "")}"
          data-original-link="${esc(g.link ?? "")}">
        <td>
          <span class="cell-value-grupo grupo-nombre" contenteditable="false">${esc(g.nombre ?? "")}</span>
        </td>
        <td>
          <span class="cell-value-grupo grupo-link" contenteditable="false">${esc(g.link ?? "")}</span>
        </td>
      </tr>
    `).join("");

    // limpiar selección
    filaGrupoSeleccionada = null;
  }

  // Delegación: seleccionar UNA fila y habilitar edición SOLO en esa fila
  if (!window.__gruposDelegadoInstalado) {
    window.__gruposDelegadoInstalado = true;

    gruposTBody?.addEventListener("click", (e) => {
      const tr = e.target.closest("tr");
      if (!tr || !gruposTBody.contains(tr)) return;

      // quitar highlight y deshabilitar edición en todas
      gruposTBody.querySelectorAll("tr").forEach(r => {
        r.classList.remove("selected");
        r.style.outline = "";
        r.style.background = "";
        r.querySelectorAll(".cell-value-grupo").forEach(s => s.setAttribute("contenteditable","false"));
      });

      // marcar seleccionada y habilitar contenteditable SOLO en ella
      tr.classList.add("selected");
      tr.style.outline = "3px solid #2e6ff2";
      tr.style.background = "#d8ecff";
      tr.querySelectorAll(".cell-value-grupo").forEach(s => s.setAttribute("contenteditable","true"));

      filaGrupoSeleccionada = tr;
    });
  }

  // Validación simple de URL (permite vacío)
  function urlValidaOrEmpty(s) {
    if (!s) return true;
    try { new URL(s); return true; } catch { return false; }
  }

  // GUARDAR: solo la fila seleccionada
  window.guardarCambiosGrupos = async function () {
    try {
      if (!filaGrupoSeleccionada) {
        alert("Selecciona un grupo primero");
        return;
      }
      const tr = filaGrupoSeleccionada;
      const id     = tr.getAttribute("data-id");
      const nombre = tr.querySelector(".grupo-nombre")?.textContent.trim() || "";
      const link   = tr.querySelector(".grupo-link")?.textContent.trim() || "";

      if (!nombre) throw new Error(`El nombre no puede estar vacío`);
      if (!urlValidaOrEmpty(link)) throw new Error(`El link no es una URL válida`);

      const origNombre = tr.getAttribute("data-original-nombre") ?? "";
      const origLink   = tr.getAttribute("data-original-link") ?? "";
      if (nombre === origNombre && link === origLink) {
        alert("No hay cambios para guardar");
        return;
      }

      if (id && id !== "null" && id !== "undefined" && id !== "") {
        const r = await fetch(`${GRUPOS_API}/${id}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json", ...authHeaders() },
          body: JSON.stringify({ id: Number(id), nombre, link })
        });
        await throwIfNotOk(r, `Actualizar grupo id=${id}`);
      } else {
        const r = await fetch(GRUPOS_API, {
          method: "POST",
          headers: { "Content-Type": "application/json", ...authHeaders() },
          body: JSON.stringify({ nombre, link })
        });
        await throwIfNotOk(r, "Crear grupo");
      }

      const nuevos = await loadGrupos();
      renderListadoGrupos(nuevos);

      closeModal("modal-listado-grupos");
      openModal("modal-grupos-guardados");
      document.getElementById("btn-aceptar-guardar-grupos").onclick = () => closeModal("modal-grupos-guardados");
    } catch (e) {
      console.error(e);
      alert(e.message || "No se pudieron guardar los cambios de grupos");
    }
  };

  // ELIMINAR grupo seleccionado (DELETE /grupos/{id})
  window.eliminarGrupoSeleccionado = async function () {
    try {
      if (!filaGrupoSeleccionada) {
        alert("Selecciona un grupo primero");
        return;
      }
      const id = filaGrupoSeleccionada.getAttribute("data-id");

      if (!id) {
        filaGrupoSeleccionada.remove();
      } else {
        const r = await fetch(`${GRUPOS_API}/${id}`, {
          method: "DELETE",
          headers: { ...authHeaders() }
        });
        await throwIfNotOk(r, `Eliminar grupo id=${id}`);
        filaGrupoSeleccionada.remove();
      }

      await loadGrupos();

      closeModal("modal-listado-grupos");
      openModal("modal-grupo-eliminado");
      document.getElementById("btn-aceptar-eliminar-grupo").onclick = () => closeModal("modal-grupo-eliminado");
    } catch (e) {
      console.error(e);
      alert(e.message || "No se pudo eliminar el grupo");
    } finally {
      filaGrupoSeleccionada = null;
    }
  };

  // ---------- búsqueda/paginación visual ----------
  let t;
  searchI?.addEventListener("input", () => {
    clearTimeout(t);
    t = setTimeout(() => { q = searchI.value.trim(); loadTramites(); }, 300);
  });
  rowsSel?.addEventListener("change", () => { size = parseInt(rowsSel.value,10) || 10; page = 0; loadTramites(); });
  prevBtn?.addEventListener("click", () => { if (page>0){ page--; loadTramites(); } });
  nextBtn?.addEventListener("click", () => { if (page<totalPages-1){ page++; loadTramites(); } });

  // primera carga
  (async () => {
    await loadGrupos();    // primero grupos → para resolver nombres
    await loadTramites();  // luego trámites
  })();
})();

// ======= ESTADO DE TRÁMITES: cargar, agregar, buscar, cambiar y eliminar (con BD) =======
(() => {
  if (document.body.dataset.page !== "estado-tramites") return;

  // --- URL absoluta para evitar "No static resource ..."
  const ESTADOS_API = (typeof ESTADOS_URL === "string" && ESTADOS_URL.startsWith("http"))
    ? ESTADOS_URL
    : "http://localhost:8080/api/estados";

  // --- utils ---
  const openModal  = (id) => document.getElementById(id)?.classList.add("show");
  const closeModal = (id) => document.getElementById(id)?.classList.remove("show");

  // cerrar con [data-close], backdrop y ESC
  document.querySelectorAll(".modal [data-close]").forEach(b=>{
    b.addEventListener("click", ()=> b.closest(".modal")?.classList.remove("show"));
  });
  document.querySelectorAll(".modal").forEach(m=>{
    m.addEventListener("click", (e)=> { if (e.target === m) m.classList.remove("show"); });
  });
  document.addEventListener("keydown", (e)=> {
    if (e.key === "Escape") document.querySelectorAll(".modal.show").forEach(m=>m.classList.remove("show"));
  });

  const $ = (s, r=document) => r.querySelector(s);
  const $$ = (s, r=document) => Array.from(r.querySelectorAll(s));
  const esc = (v)=> (v ?? "").toString()
    .replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")
    .replace(/"/g,"&quot;").replace(/'/g,"&#39;");

  // --- refs UI ---
  const tbody    = $("#estadoTableBody");
  const searchI  = $("#searchInputEstado");
  const rowsSel  = $("#rowsPerPageEstado");
  const prevBtn  = $("#prevPageEstado");
  const nextBtn  = $("#nextPageEstado");
  const pagTxt   = $("#paginationTextEstado");

  const btnEliminarSel = $("#btnEliminarEstadoSel");

  const selCliente = $("#estadoCliente");
  const selTramite = $("#estadoTramite");
  const inpFecha   = $("#estadoFechaInicio");
  const selEstado  = $("#estadoEstado");

  // --- estado de la tabla ---
  let q = ""; let page = 0; let size = parseInt(rowsSel?.value || "10", 10);
  let totalPages = 1; let totalElements = 0;

  // cachés para pintar nombres desde ids
  const clientesIdx = new Map();  // id -> nombre
  const tramitesIdx = new Map();  // id -> nombre

  // selección actual (para eliminar)
  let selectedEstadoId = null;

  // --- helpers ---
  async function throwIfNotOk(res, ctx="") {
    if (res.ok) return;
    let msg = `HTTP ${res.status}`;
    try {
      const ct = res.headers.get("content-type") || "";
      if (ct.includes("json")) {
        const j = await res.json();
        msg += j?.message ? ` – ${j.message}` : j?.error ? ` – ${j.error}` : "";
      } else {
        const t = await res.text(); if (t) msg += ` – ${t}`;
      }
    } catch {}
    throw new Error(ctx ? `${ctx}: ${msg}` : msg);
  }
  const fmtFecha = (iso) => {
    if (!iso) return "";
    const d = new Date(iso);
    const dd = String(d.getDate()).padStart(2,"0");
    const mm = String(d.getMonth()+1).padStart(2,"0");
    const yyyy = d.getFullYear();
    return `${dd}/${mm}/${yyyy}`;
  };

  function norm(s){ return (s ?? "").toString().toLowerCase().normalize("NFD").replace(/\p{Diacritic}/gu,""); }

  // --- cargar selects (clientes y trámites) ---
  async function loadClientes() {
    try {
      const r = await fetch(CLIENTES_URL + "?page=0&size=1000", { headers: { Accept:"application/json", ...authHeaders() } });
      await throwIfNotOk(r, "Listar clientes");
      const j = await r.json();
      const data = j?.data ?? j;
      const items = Array.isArray(data) ? data : (data?.items ?? data?.content ?? []);
      clientesIdx.clear();
      if (selCliente) selCliente.innerHTML = `<option value="">Seleccione un cliente</option>`;
      items.forEach(c=>{
        const nombre = `${c.nombre ?? ""} ${c.apellidos ?? ""}`.trim() || c.nombre || "";
        clientesIdx.set(String(c.id), nombre);
        selCliente?.add(new Option(nombre, c.id));
      });
    } catch (e) { console.error(e); }
  }
  async function loadTramitesSelect() {
    try {
      const r = await fetch(TRAMITES_URL + "?page=0&size=1000", { headers: { Accept:"application/json", ...authHeaders() } });
      await throwIfNotOk(r, "Listar trámites");
      const j = await r.json();
      const data = j?.data ?? j;
      const items = Array.isArray(data) ? data : (data?.items ?? data?.content ?? []);
      tramitesIdx.clear();
      if (selTramite) selTramite.innerHTML = `<option value="">Seleccione un trámite</option>`;
      items.forEach(t=>{
        tramitesIdx.set(String(t.id), t.nombre ?? "");
        selTramite?.add(new Option(t.nombre ?? "", t.id));
      });
    } catch (e) { console.error(e); }
  }

  // --- listar estados ---
  async function fetchEstados() {
    // Si tu backend soporta ?q=, lo aprovecha; si no, filtraremos localmente
    const url = `${ESTADOS_API}${q ? `?q=${encodeURIComponent(q)}` : ""}`;
    const r = await fetch(url, { headers: { Accept:"application/json", ...authHeaders() } });
    await throwIfNotOk(r, "Listar estados de trámite");
    return r.json();
  }
  function parseLista(json) {
    const data = json?.data ?? json;
    const items = Array.isArray(data) ? data : (data?.items ?? data?.content ?? []);
    totalPages    = Array.isArray(data) ? 1 : (data?.totalPages ?? 1);
    totalElements = Array.isArray(data) ? items.length : (data?.totalElements ?? items.length);
    return items;
  }

  function pintarTabla(items) {
    tbody.innerHTML = items.map(e => {
      const cliNombre = e.cliente?.nombre
        ? `${e.cliente.nombre ?? ""} ${e.cliente.apellidos ?? ""}`.trim()
        : (clientesIdx.get(String(e.clienteId)) || e.clienteNombre || "");
      const traNombre = e.tramite?.nombre ?? (tramitesIdx.get(String(e.tramiteId)) || e.tramiteNombre || "");
      const selectedClass = (String(selectedEstadoId) === String(e.id)) ? "selected" : "";
      return `
        <tr data-id="${esc(e.id)}" class="${selectedClass}">
          <td>${esc(traNombre)}</td>
          <td>${esc(cliNombre)}</td>
          <td>${esc(fmtFecha(e.fechaInicio))}</td>
          <td>
            <select class="estado-select" data-id="${esc(e.id)}">
              <option value="Pendiente"  ${(e.estado==="PENDIENTE"||e.estado==="Pendiente")?"selected":""}>Pendiente</option>
              <option value="En Proceso" ${(e.estado==="EN_PROCESO"||e.estado==="En Proceso")?"selected":""}>En Proceso</option>
              <option value="Finalizado" ${(e.estado==="FINALIZADO"||e.estado==="Finalizado")?"selected":""}>Finalizado</option>
              <option value="Rechazado"  ${(e.estado==="RECHAZADO"||e.estado==="Rechazado")?"selected":""}>Rechazado</option>
            </select>
          </td>
        </tr>
      `;
    }).join("");

    // selección por click
    $$("#estadoTableBody tr").forEach(tr=>{
      tr.addEventListener("click", (ev) => {
        // evitar que el click en el <select> robe la selección
        if (ev.target.closest("select")) return;
        $$("#estadoTableBody tr").forEach(x=>x.classList.remove("selected"));
        tr.classList.add("selected");
        selectedEstadoId = tr.getAttribute("data-id");
      });
    });

    // manejar cambios de estado (PATCH)
    tbody.querySelectorAll(".estado-select").forEach(sel => {
      sel.addEventListener("change", async () => {
        const id = sel.getAttribute("data-id");
        const mapa = { "Pendiente":"PENDIENTE", "En Proceso":"EN_PROCESO", "Finalizado":"FINALIZADO", "Rechazado":"RECHAZADO" };
        const nuevo = mapa[sel.value] || "PENDIENTE";
        try {
          const r = await fetch(`${ESTADOS_API}/${id}?estado=${encodeURIComponent(nuevo)}`, {
            method: "PATCH",
            headers: { ...authHeaders() }
          });
          await throwIfNotOk(r, `Cambiar estado ${id}`);
        } catch (e) {
          console.error(e);
          alert(e.message || "Error al cambiar estado");
        }
      });
    });
  }

  async function loadEstados() {
    try {
      const json  = await fetchEstados();
      let items = parseLista(json);

      // Filtro local (por si el backend NO soporta ?q=)
      if (q) {
        const nq = norm(q);
        items = items.filter(e=>{
          const cli = e.cliente?.nombre
            ? `${e.cliente.nombre ?? ""} ${e.cliente.apellidos ?? ""}`
            : (e.clienteNombre ?? clientesIdx.get(String(e.clienteId)) ?? "");
          const tra = e.tramite?.nombre ?? (e.tramiteNombre ?? tramitesIdx.get(String(e.tramiteId)) ?? "");
          return norm(cli).includes(nq) || norm(tra).includes(nq);
        });
      }

      // paginación visual simple
      totalElements = items.length;
      totalPages = Math.max(1, Math.ceil(totalElements / size));
      page = Math.min(page, totalPages - 1);
      const start = page * size;
      const end   = Math.min(start + size, totalElements);
      const pageItems = items.slice(start, end);

      pintarTabla(pageItems);

      const from = totalElements === 0 ? 0 : (start + 1);
      const to   = end;
      pagTxt && (pagTxt.textContent = `${from}-${to} of ${totalElements}`);
      prevBtn && (prevBtn.disabled = page <= 0);
      nextBtn && (nextBtn.disabled = page >= (totalPages - 1));
    } catch (e) {
      console.error(e);
      tbody.innerHTML = `<tr><td colspan="4">No se pudo cargar el listado.</td></tr>`;
    }
  }

  // haz visible si luego quieres recargar desde otra parte
  window.loadEstados = loadEstados;

  // --- botón "+ Agregar" ---
  window.abrirModalAgregarEstado = async () => {
    await Promise.all([loadClientes(), loadTramitesSelect()]);
    $("#formAgregarEstado")?.reset();
    openModal("modal-agregar-estado");
  };

  // --- guardar nuevo estado (POST /estados) ---
  $("#formAgregarEstado")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const clienteId = selCliente?.value ? Number(selCliente.value) : null;
    const tramiteId = selTramite?.value ? Number(selTramite.value) : null;
    const fechaISO  = inpFecha?.value; // yyyy-mm-dd
    const estadoSel = selEstado?.value;

    if (!clienteId || !tramiteId || !fechaISO || !estadoSel) {
      alert("Completa todos los campos");
      return;
    }

    const mapa = { "Pendiente":"PENDIENTE", "En Proceso":"EN_PROCESO", "Finalizado":"FINALIZADO", "Rechazado":"RECHAZADO" };
    const payload = {
      clienteId,
      tramiteId,
      fechaInicio: fechaISO,
      estado: mapa[estadoSel] || "PENDIENTE"
    };

    try {
      const r = await fetch(ESTADOS_API, {
        method: "POST",
        headers: { "Content-Type": "application/json", ...authHeaders() },
        body: JSON.stringify(payload)
      });
      await throwIfNotOk(r, "Crear estado");

      closeModal("modal-agregar-estado");
      openModal("modal-estado-guardado");
      $("#btn-aceptar-estado").onclick = async () => {
        closeModal("modal-estado-guardado");
        await loadEstados();
      };
    } catch (e2) {
      console.error(e2);
      alert(e2.message || "No se pudo guardar el estado");
    }
  });

  // --- eliminar seleccionado (DELETE /estados/{id}) ---
  btnEliminarSel?.addEventListener("click", async () => {
    try {
      if (!selectedEstadoId) {
        alert("Primero selecciona una fila del listado.");
        return;
      }
      if (!confirm("¿Eliminar el estado seleccionado?")) return;

      const r = await fetch(`${ESTADOS_API}/${selectedEstadoId}`, {
        method: "DELETE",
        headers: { ...authHeaders() }
      });
      await throwIfNotOk(r, `Eliminar estado id=${selectedEstadoId}`);

      selectedEstadoId = null;
      await loadEstados();
    } catch (e) {
      console.error(e);
      alert(e.message || "No se pudo eliminar el estado");
    }
  });

  // --- búsqueda/paginación (visual) ---
  let t;
  searchI?.addEventListener("input", () => {
    clearTimeout(t);
    t = setTimeout(() => { q = searchI.value.trim(); page = 0; loadEstados(); }, 300);
  });
  rowsSel?.addEventListener("change", () => { size = parseInt(rowsSel.value,10) || 10; page = 0; loadEstados(); });
  prevBtn?.addEventListener("click", () => { if (page>0){ page--; loadEstados(); } });
  nextBtn?.addEventListener("click", () => { if (page<totalPages-1){ page++; loadEstados(); } });

  // --- primera carga ---
  (async () => {
    await Promise.all([loadClientes(), loadTramitesSelect()]);
    await loadEstados();
  })();
})();
