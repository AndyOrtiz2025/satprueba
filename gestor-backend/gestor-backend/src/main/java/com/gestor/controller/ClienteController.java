// ClienteController.java
package com.gestor.controller;

import com.gestor.dto.ClienteDTO;
import com.gestor.service.ClienteService;
import com.gestor.util.ApiResponse;
import com.gestor.util.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService service;
    public ClienteController(ClienteService service){ this.service = service; }

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
}
