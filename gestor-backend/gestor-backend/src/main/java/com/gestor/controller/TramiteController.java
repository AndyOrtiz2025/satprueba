// TramiteController.java
package com.gestor.controller;

import com.gestor.dto.TramiteDTO;
import com.gestor.service.TramiteService;
import com.gestor.util.ApiResponse;
import com.gestor.util.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tramites")
public class TramiteController {
    private final TramiteService service;
    public TramiteController(TramiteService service){ this.service = service; }

    @PostMapping
    public ResponseEntity<ApiResponse<TramiteDTO>> create(@RequestBody TramiteDTO dto){
        return ResponseEntity.ok(ApiResponse.ok(service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TramiteDTO>> update(@PathVariable Long id, @RequestBody TramiteDTO dto){
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Eliminado", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TramiteDTO>> get(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TramiteDTO>>> search(
            @RequestParam(value="q", required=false) String q,
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value="size", defaultValue="10") int size){
        return ResponseEntity.ok(ApiResponse.ok(service.search(q, page, size)));
    }
}
