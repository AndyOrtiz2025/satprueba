// EstadoTramiteController.java
package com.gestor.controller;

import com.gestor.dto.EstadoTramiteDTO;
import com.gestor.service.EstadoTramiteService;
import com.gestor.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados")
public class EstadoTramiteController {
    private final EstadoTramiteService service;
    public EstadoTramiteController(EstadoTramiteService service){ this.service = service; }

    // para tu modal "+ Agregar" en Estado de Trámites si lo conectas
    @PostMapping
    public ResponseEntity<ApiResponse<EstadoTramiteDTO>> create(@RequestBody EstadoTramiteDTO dto){
        return ResponseEntity.ok(ApiResponse.ok(service.create(dto)));
    }

    // usado por el combo <select> para cambiar estado (PATCH /api/estados/{id}?estado=EN_PROCESO)
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<EstadoTramiteDTO>> patchEstado(@PathVariable Long id,
                                                                     @RequestParam("estado") String estado){
        return ResponseEntity.ok(ApiResponse.ok(service.updateEstado(id, estado)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EstadoTramiteDTO>>> list(){
        return ResponseEntity.ok(ApiResponse.ok(service.listAll()));
    }
}
