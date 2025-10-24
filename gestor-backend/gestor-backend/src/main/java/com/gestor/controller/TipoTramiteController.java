// TipoTramiteController.java
package com.gestor.controller;

import com.gestor.dto.TipoTramiteDTO;
import com.gestor.entity.TipoTramite;
import com.gestor.exception.NotFoundException;
import com.gestor.repository.TipoTramiteRepository;
import com.gestor.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tipos-tramites")
public class TipoTramiteController {

    private final TipoTramiteRepository repository;

    public TipoTramiteController(TipoTramiteRepository repository) {
        this.repository = repository;
    }

    // Listar todos los tipos de trámites
    @GetMapping
    public ResponseEntity<ApiResponse<List<TipoTramiteDTO>>> listar() {
        List<TipoTramiteDTO> tipos = repository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok("Tipos obtenidos", tipos));
    }

    // Obtener un tipo por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoTramiteDTO>> obtener(@PathVariable Long id) {
        TipoTramite tipo = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Tipo de trámite no encontrado"));
        return ResponseEntity.ok(ApiResponse.ok(toDTO(tipo)));
    }

    // Crear nuevo tipo
    @PostMapping
    public ResponseEntity<ApiResponse<TipoTramiteDTO>> crear(@RequestBody TipoTramiteDTO dto) {
        TipoTramite tipo = new TipoTramite();
        tipo.setPortal(dto.getPortal());
        tipo.setLink(dto.getLink());
        tipo = repository.save(tipo);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok("Tipo creado exitosamente", toDTO(tipo)));
    }

    // Actualizar tipo
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoTramiteDTO>> actualizar(@PathVariable Long id, @RequestBody TipoTramiteDTO dto) {
        TipoTramite tipo = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Tipo de trámite no encontrado"));
        tipo.setPortal(dto.getPortal());
        tipo.setLink(dto.getLink());
        tipo = repository.save(tipo);
        return ResponseEntity.ok(ApiResponse.ok("Tipo actualizado exitosamente", toDTO(tipo)));
    }

    // Eliminar tipo
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Tipo de trámite no encontrado");
        }
        repository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok("Tipo eliminado exitosamente", null));
    }

    // Mapper manual
    private TipoTramiteDTO toDTO(TipoTramite entity) {
        TipoTramiteDTO dto = new TipoTramiteDTO();
        dto.setId(entity.getIdTipoTramite());
        dto.setPortal(entity.getPortal());
        dto.setLink(entity.getLink());
        return dto;
    }
}
