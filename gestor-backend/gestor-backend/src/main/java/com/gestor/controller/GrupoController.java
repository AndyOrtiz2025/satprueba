package com.gestor.controller;

import com.gestor.dto.GrupoDTO;
import com.gestor.service.GrupoService;
import com.gestor.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {
    private final GrupoService service;
    public GrupoController(GrupoService service){ this.service = service; }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GrupoDTO>>> list(){
        return ResponseEntity.ok(ApiResponse.ok(service.listAll()));
    }

    @GetMapping("/{id}") // <- NUEVO (opcional, útil para depurar)
    public ResponseEntity<ApiResponse<GrupoDTO>> get(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GrupoDTO>> create(@RequestBody GrupoDTO dto){
        return ResponseEntity.ok(ApiResponse.ok(service.create(dto)));
    }

    @PutMapping("/{id}") // <- NUEVO
    public ResponseEntity<ApiResponse<GrupoDTO>> update(@PathVariable Long id, @RequestBody GrupoDTO dto){
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, dto)));
    }

    @DeleteMapping("/{id}") // <- NUEVO
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Eliminado", null));
    }
}
