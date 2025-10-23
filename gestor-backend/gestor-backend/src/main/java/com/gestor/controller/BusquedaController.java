// BusquedaController.java
package com.gestor.controller;

import com.gestor.dto.BusquedaAvanzadaDTO;
import com.gestor.dto.BusquedaPaginadaResponseDTO;
import com.gestor.exception.BadRequestException;
import com.gestor.service.BusquedaService;
import com.gestor.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para CU-SAT011: Buscador Avanzado
 */
@RestController
@RequestMapping("/api/busqueda")
public class BusquedaController {

    private final BusquedaService busquedaService;

    public BusquedaController(BusquedaService busquedaService) {
        this.busquedaService = busquedaService;
    }

    /**
     * Endpoint para búsqueda avanzada con filtros dinámicos
     * POST /api/busqueda/avanzada
     *
     * Tarea 6: Crear endpoint POST /api/busqueda/avanzada
     * Tarea 7: Validar que al menos un filtro esté presente
     */
    @PostMapping("/avanzada")
    public ResponseEntity<ApiResponse<BusquedaPaginadaResponseDTO>> busquedaAvanzada(
            @Valid @RequestBody BusquedaAvanzadaDTO filtros) {

        // Tarea 7: Validar que al menos un filtro esté presente
        if (!filtros.tieneAlMenosUnFiltro()) {
            throw new BadRequestException(
                "Debe proporcionar al menos un filtro de búsqueda " +
                "(nombre, dpi, fecha_inicio, fecha_fin o estado)"
            );
        }

        // Ejecutar búsqueda con filtros
        BusquedaPaginadaResponseDTO resultados = busquedaService.buscarConFiltros(filtros);

        // Respuesta exitosa
        return ResponseEntity.ok(ApiResponse.ok(
            "Búsqueda realizada exitosamente. " +
            "Encontrados " + resultados.getTotalElementos() + " resultados",
            resultados
        ));
    }

    /**
     * Endpoint alternativo con GET para búsquedas simples
     * GET /api/busqueda/avanzada?nombre=Juan&estado=PENDIENTE&page=0&size=10
     */
    @GetMapping("/avanzada")
    public ResponseEntity<ApiResponse<BusquedaPaginadaResponseDTO>> busquedaAvanzadaGet(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String dpi,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "fechaInicio") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        // Construir DTO desde query params
        BusquedaAvanzadaDTO filtros = new BusquedaAvanzadaDTO();
        filtros.setNombre(nombre);
        filtros.setDpi(dpi);
        filtros.setEstado(estado);
        filtros.setPage(page);
        filtros.setSize(size);
        filtros.setSortBy(sortBy);
        filtros.setSortDir(sortDir);

        // Parsear fechas si están presentes
        if (fechaInicio != null && !fechaInicio.isBlank()) {
            try {
                filtros.setFechaInicio(java.time.LocalDate.parse(fechaInicio));
            } catch (Exception e) {
                throw new BadRequestException("Formato de fecha_inicio inválido. Use YYYY-MM-DD");
            }
        }

        if (fechaFin != null && !fechaFin.isBlank()) {
            try {
                filtros.setFechaFin(java.time.LocalDate.parse(fechaFin));
            } catch (Exception e) {
                throw new BadRequestException("Formato de fecha_fin inválido. Use YYYY-MM-DD");
            }
        }

        // Validar que al menos un filtro esté presente
        if (!filtros.tieneAlMenosUnFiltro()) {
            throw new BadRequestException(
                "Debe proporcionar al menos un filtro de búsqueda " +
                "(nombre, dpi, fechaInicio, fechaFin o estado)"
            );
        }

        // Ejecutar búsqueda
        BusquedaPaginadaResponseDTO resultados = busquedaService.buscarConFiltros(filtros);

        return ResponseEntity.ok(ApiResponse.ok(
            "Búsqueda realizada exitosamente. " +
            "Encontrados " + resultados.getTotalElementos() + " resultados",
            resultados
        ));
    }
}
