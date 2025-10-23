// ClienteController.java
package com.gestor.controller;

import com.gestor.dto.ClienteDTO;
import com.gestor.dto.ClienteCreateDTO;
import com.gestor.dto.ClienteResponseDTO;
import com.gestor.dto.ClienteDetalleDTO;
import com.gestor.dto.HistorialTramiteDTO;
import com.gestor.service.ClienteService;
import com.gestor.service.TramiteService;
import com.gestor.util.ApiResponse;
import com.gestor.util.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService service;
    private final TramiteService tramiteService;

    public ClienteController(ClienteService service, TramiteService tramiteService){
        this.service = service;
        this.tramiteService = tramiteService;
    }

    // === NUEVO ENDPOINT PARA CU-SAT001: Registrar Cliente ===

    @PostMapping("/registrar")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> registrarCliente(@Valid @RequestBody ClienteCreateDTO dto){
        ClienteResponseDTO response = service.createCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Cliente registrado exitosamente", response));
    }

    // === ENDPOINTS ANTIGUOS (Compatibilidad) ===

    @PostMapping
    public ResponseEntity<ApiResponse<ClienteDTO>> create(@RequestBody ClienteDTO dto){
        return ResponseEntity.ok(ApiResponse.ok(service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> update(@PathVariable Long id, @RequestBody ClienteDTO dto){
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Eliminado", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> get(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ClienteDTO>>> search(
            @RequestParam(value="q", required=false) String q,
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value="size", defaultValue="10") int size){
        return ResponseEntity.ok(ApiResponse.ok(service.search(q, page, size)));
    }

    // === NUEVOS ENDPOINTS PARA CU-SAT002: Consultar Cliente ===

    /**
     * Obtiene el detalle completo de un cliente incluyendo trámites asociados
     * GET /api/clientes/{id}/detalle
     */
    @GetMapping("/{id}/detalle")
    public ResponseEntity<ApiResponse<ClienteDetalleDTO>> obtenerDetalle(@PathVariable Long id){
        ClienteDetalleDTO detalle = service.obtenerClienteDetalle(id);
        return ResponseEntity.ok(ApiResponse.ok("Cliente obtenido exitosamente", detalle));
    }

    /**
     * Busca un cliente por su DPI
     * GET /api/clientes/buscar?dpi={dpi}
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<ClienteDetalleDTO>> buscarPorDpi(@RequestParam String dpi){
        ClienteDetalleDTO detalle = service.buscarClientePorDpi(dpi);
        return ResponseEntity.ok(ApiResponse.ok("Cliente encontrado exitosamente", detalle));
    }

    // === NUEVO ENDPOINT PARA CU-SAT005: Historial de Trámites ===

    /**
     * Obtiene el historial completo de trámites de un cliente con archivos asociados
     * Incluye paginación y ordenamiento por fecha descendente
     * @param id ID del cliente
     * @param page número de página (0-indexed)
     * @param size tamaño de página (por defecto 10)
     * @return PageResponse con el historial de trámites
     */
    @GetMapping("/{id}/historial")
    public ResponseEntity<ApiResponse<PageResponse<HistorialTramiteDTO>>> obtenerHistorial(
            @PathVariable Long id,
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value="size", defaultValue="10") int size){
        PageResponse<HistorialTramiteDTO> historial = tramiteService.obtenerHistorialCliente(id, page, size);
        return ResponseEntity.ok(ApiResponse.ok("Historial obtenido exitosamente", historial));
    }
}
