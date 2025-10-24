# 📋 GESTOR SAT - Frontend

Sistema de gestión de trámites SAT con datos MOCK para desarrollo independiente.

---

## 🚀 Cómo Usar

### Abrir el Proyecto

Simplemente abre `index.html` en tu navegador favorito:

```
1. Haz doble clic en index.html
2. O haz clic derecho → Abrir con → Google Chrome/Firefox/Edge
```

**NO necesitas servidor ni backend para probar!** 🎉

---

## 🎯 Funcionalidades Disponibles

### ✅ Pantallas Funcionales

1. **Login** (`index.html`)
   - Login básico (sin autenticación por ahora)
   - Navegación a registro

2. **Registro** (`register.html`)
   - ✅ Validación de campos
   - ✅ Contraseña mínimo 8 caracteres
   - ✅ Verificación de contraseñas coincidentes
   - ✅ Guarda en localStorage (modo MOCK)

3. **Dashboard** (`dashboard.html`)
   - Video de presentación
   - Navegación a todas las secciones

4. **Agregar Clientes** (`clientes.html`)
   - ✅ Validación de DPI (13 dígitos)
   - ✅ Validación de teléfono (8 dígitos)
   - ✅ Verificación de mayor de edad (18+)
   - ✅ Guarda en localStorage

5. **Listado de Clientes** (`listado-clientes.html`) ⭐ NUEVO
   - ✅ Muestra todos los clientes registrados
   - ✅ Tabla con diseño moderno
   - ✅ Datos desde localStorage

6. **Agregar Trámites** (`tramites.html`)
   - ✅ Validación de campos
   - ✅ Selector de tipo de trámite
   - ✅ Guarda en localStorage

7. **Gestión de Datos Sensibles** (`datos.html`) ⭐ NUEVO
   - ✅ Selector de cliente
   - ✅ NIT, NIS, Email, DPI, Cuenta Bancaria
   - ✅ 5 contraseñas de plataformas
   - ✅ Contraseñas encriptadas con AES-256 (en backend)
   - ✅ Toggle mostrar/ocultar contraseñas (👁️/🙈)
   - ✅ Carga automática de datos existentes al seleccionar cliente
   - ✅ Actualización de datos

---

## 🔧 Configuración USAR_MOCK

El archivo `js/config.js` controla el modo de funcionamiento:

```javascript
const CONFIG = {
  USAR_MOCK: true,  // ⬅️ CAMBIAR AQUÍ
  API_BASE_URL: 'http://localhost:8080/api'
};
```

### 🟢 USAR_MOCK = true (Actual)
- ✅ Usa datos simulados (localStorage)
- ✅ NO requiere backend
- ✅ Perfecto para desarrollo
- ✅ Los datos persisten en tu navegador

### 🔴 USAR_MOCK = false (Para Franck)
- Conecta al backend real en `localhost:8080`
- Requiere Spring Boot levantado
- Usa base de datos PostgreSQL real

---

## 📂 Estructura de Archivos

```
frontend/
├── index.html              # Login
├── register.html           # Registro de usuarios
├── dashboard.html          # Dashboard principal
├── clientes.html           # Agregar clientes
├── listado-clientes.html   # Ver clientes ⭐ NUEVO
├── tramites.html           # Agregar trámites
├── css/
│   └── styles.css
├── js/
│   ├── config.js           # ⚙️ CONFIGURACIÓN (USAR_MOCK)
│   ├── api-service.js      # Servicios MOCK y reales
│   └── app.js              # Lógica de la aplicación
└── assets/
    ├── images/
    └── videos/
```

---

## 🔍 Validaciones Implementadas

### Registro de Usuarios
- ✅ Nombre de usuario (max 150 caracteres)
- ✅ Email válido
- ✅ Contraseña mínimo 8 caracteres
- ✅ Contraseñas coincidentes
- ✅ Usuario/email únicos

### Registro de Clientes
- ✅ Nombre completo obligatorio (max 150 caracteres)
- ✅ DPI 13 dígitos (obligatorio)
- ✅ Fecha de nacimiento (obligatorio, +18 años)
- ✅ Teléfono 8 dígitos (opcional)
- ✅ Dirección (max 500 caracteres, opcional)
- ✅ DPI único

### Registro de Trámites
- ✅ Nombre obligatorio (max 50 caracteres)
- ✅ Descripción (max 500 caracteres, opcional)
- ✅ Tipo de trámite obligatorio (SAT, CGC, CONSULTA RAE, RENAS)

---

## 💾 Datos MOCK Incluidos

El sistema ya incluye 2 clientes de ejemplo:

1. **Juan Pérez García**
   - DPI: 1234567890123
   - Teléfono: 12345678
   - Dirección: Zona 1, Ciudad de Guatemala

2. **María López Hernández**
   - DPI: 9876543210987
   - Teléfono: 87654321
   - Dirección: Zona 10, Guatemala

Puedes verlos en `listado-clientes.html`

---

## 🧪 Cómo Probar

### 1. Registrar Usuario
```
1. Abre index.html
2. Haz clic en "Regístrate"
3. Llena el formulario
4. Haz clic en "REGISTRARSE"
5. ✅ Se guarda en localStorage
```

### 2. Agregar Cliente
```
1. Dashboard → AGREGAR CLIENTES
2. Llena el formulario:
   - Nombre Completo: "Carlos Gómez"
   - DPI: "1111222233344" (13 dígitos)
   - Fecha Nacimiento: "1995-05-15"
   - Teléfono: "55551234" (opcional)
3. Haz clic en "GUARDAR"
4. ✅ Se guarda en localStorage
```

### 3. Ver Clientes
```
1. Dashboard → LISTADO DE CLIENTES
2. ✅ Verás todos los clientes registrados en una tabla
```

### 4. Agregar Trámite
```
1. Dashboard → TRÁMITES
2. Llena el formulario:
   - Nombre: "Inscripción CGC"
   - Descripción: "Registro en CGC"
   - Tipo: Selecciona "CGC"
3. Haz clic en "GUARDAR TRÁMITE"
4. ✅ Se guarda en localStorage
```

---

## 🔄 Cambiar a Backend Real (Para Franck)

**Pasos:**

1. **Levantar el backend Spring Boot**
   ```bash
   cd pruebasat
   mvnw spring-boot:run
   ```

2. **Cambiar configuración**

   Edita `js/config.js`:
   ```javascript
   USAR_MOCK: false  // ⬅️ Cambiar de true a false
   ```

3. **Refrescar el navegador**

   ¡Listo! Ahora se conectará al backend real en `localhost:8080`

---

## 🗄️ Datos en localStorage

Los datos MOCK se guardan en localStorage del navegador:

```javascript
// Ver datos guardados (Consola del navegador F12)
localStorage.getItem('gestor_sat_clientes')
localStorage.getItem('gestor_sat_usuarios')
localStorage.getItem('gestor_sat_tramites')

// Limpiar datos (si quieres resetear)
localStorage.clear()
```

---

## 🐛 Solución de Problemas

### Los datos no se guardan
- ✅ Verifica que `USAR_MOCK = true` en `config.js`
- ✅ Abre la consola del navegador (F12) y busca errores

### No veo los clientes de ejemplo
- ✅ Recarga la página (F5)
- ✅ Verifica que los scripts se carguen en orden (config.js → api-service.js → app.js)

### El formulario no valida
- ✅ Asegúrate de llenar todos los campos obligatorios (*)
- ✅ Verifica los formatos (DPI 13 dígitos, teléfono 8 dígitos)

---

## 📊 Endpoints del Backend (cuando USAR_MOCK = false)

```
POST /api/usuarios          - Registrar usuario
GET  /api/clientes          - Listar clientes
POST /api/clientes          - Crear cliente
GET  /api/clientes/dpi/{dpi} - Buscar por DPI
POST /api/tramites          - Crear trámite
GET  /api/tramites          - Listar trámites
```

---

## ✅ Checklist Pre-Commit

Antes de subir tu rama, verifica:

- ✅ `USAR_MOCK = true` en `config.js`
- ✅ Todas las pantallas funcionan (registro, clientes, trámites)
- ✅ Las validaciones funcionan correctamente
- ✅ El listado de clientes muestra datos
- ✅ No hay errores en la consola (F12)

---

## 📝 Notas para Franck

Cuando recibas esta rama:

1. ✅ Verifica que todo funcione con MOCK
2. ✅ Cambia `USAR_MOCK` a `false` en `config.js`
3. ✅ Levanta el backend Spring Boot
4. ✅ Verifica la conexión con la BD PostgreSQL
5. ✅ Prueba que los formularios se conecten al backend

**Los DTOs del frontend coinciden EXACTAMENTE con los del backend!** 🎯

---

## 🎨 Cambios Realizados vs Diseño Original

### Formulario de Registro
- ❌ Eliminado: Nombre y Apellido separados
- ✅ Solo: Username, Email, Password

### Formulario de Clientes
- ❌ Eliminado: NIS, NIT, F/N, Contraseñas
- ✅ Agregado: Fecha de Nacimiento (obligatorio)
- ✅ Modificado: Nombre/Apellido → Nombre Completo

### Formulario de Trámites
- ❌ Eliminado: Requisitos, Checkboxes de grupos
- ✅ Agregado: Selector de Tipo de Trámite (SAT, CGC, etc.)

**Todos los cambios se hicieron para coincidir con el backend!**

---

## 📞 Contacto

Si tienes dudas, revisa la consola del navegador (F12) para ver logs detallados.

---

**¡Listo para desarrollar! 🚀**
