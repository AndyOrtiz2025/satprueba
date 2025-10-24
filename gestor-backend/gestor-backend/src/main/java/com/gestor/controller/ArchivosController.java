package com.gestor.controller;

import com.gestor.dto.ArchivoDTO;
import com.gestor.entity.Archivos;
import com.gestor.service.ArchivosService;
import com.gestor.util.ApiResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/archivos")
public class ArchivosController {

    private final ArchivosService archivosService;

    public ArchivosController(ArchivosService archivosService) {
        this.archivosService = archivosService;
    }

    /**
     * Subir archivo para un cliente
     * POST /api/archivos/cliente/{idCliente}
     */
    @PostMapping("/cliente/{idCliente}")
    public ResponseEntity<ApiResponse<ArchivoDTO>> subirArchivo(
            @PathVariable Long idCliente,
            @RequestParam("file") MultipartFile file) {

        ArchivoDTO archivo = archivosService.subirArchivo(idCliente, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Archivo subido exitosamente", archivo));
    }

    /**
     * Listar archivos de un cliente
     * GET /api/archivos/cliente/{idCliente}
     */
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<ApiResponse<List<ArchivoDTO>>> listarArchivosPorCliente(
            @PathVariable Long idCliente) {

        List<ArchivoDTO> archivos = archivosService.listarArchivosPorCliente(idCliente);
        return ResponseEntity.ok(ApiResponse.ok("Archivos obtenidos exitosamente", archivos));
    }

    /**
     * Descargar archivo
     * GET /api/archivos/{idArchivo}/download
     */
    @GetMapping("/{idArchivo}/download")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable Long idArchivo) {

        Resource resource = archivosService.cargarArchivo(idArchivo);
        Archivos archivo = archivosService.obtenerArchivo(idArchivo);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + archivo.getNombreArchivo() + "\"")
                .body(resource);
    }

    /**
     * Eliminar archivo
     * DELETE /api/archivos/{idArchivo}
     */
    @DeleteMapping("/{idArchivo}")
    public ResponseEntity<ApiResponse<Void>> eliminarArchivo(@PathVariable Long idArchivo) {

        archivosService.eliminarArchivo(idArchivo);
        return ResponseEntity.ok(ApiResponse.ok("Archivo eliminado exitosamente", null));
    }
}
