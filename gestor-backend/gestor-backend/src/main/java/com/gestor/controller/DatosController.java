package com.gestor.controller;

import com.gestor.dto.DatosDTO;
import com.gestor.service.DatosService;
import com.gestor.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/datos")
public class DatosController {

    private final DatosService datosService;

    public DatosController(DatosService datosService) {
        this.datosService = datosService;
    }

    /**
     * Obtener datos sensibles de un cliente
     * GET /api/datos/{idCliente}/contrasenas
     */
    @GetMapping("/{idCliente}/contrasenas")
    public ResponseEntity<ApiResponse<DatosDTO>> obtenerDatos(@PathVariable Long idCliente) {
        Optional<DatosDTO> datosOpt = datosService.obtenerPorCliente(idCliente);

        if (datosOpt.isPresent()) {
            return ResponseEntity.ok(ApiResponse.ok("Datos obtenidos exitosamente", datosOpt.get()));
        } else {
            // Si no hay datos, devolver un objeto vacío con éxito
            DatosDTO datosVacio = new DatosDTO();
            datosVacio.setIdCliente(idCliente);
            return ResponseEntity.ok(ApiResponse.ok("No hay datos registrados para este cliente", datosVacio));
        }
    }

    /**
     * Guardar o actualizar datos sensibles de un cliente
     * POST /api/datos/{idCliente}/contrasenas
     */
    @PostMapping("/{idCliente}/contrasenas")
    public ResponseEntity<ApiResponse<DatosDTO>> guardarDatos(
            @PathVariable Long idCliente,
            @RequestBody DatosDTO datosDTO) {

        DatosDTO datoGuardado = datosService.guardarDatos(idCliente, datosDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Datos guardados exitosamente", datoGuardado));
    }
}
