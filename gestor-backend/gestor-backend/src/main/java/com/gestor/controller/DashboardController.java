// DashboardController.java
package com.gestor.controller;

import com.gestor.dto.EstadisticasDTO;
import com.gestor.service.DashboardService;
import com.gestor.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para CU-SAT012: Panel de Control
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Obtiene todas las estadísticas del sistema
     * GET /api/dashboard/estadisticas
     *
     * Retorna:
     * - Total de clientes
     * - Total de trámites
     * - Trámites por estado (INICIADO, PENDIENTE, TERMINADO)
     * - Trámites del mes actual
     * - Clientes nuevos del mes
     * - Top 5 tipos de trámites más solicitados
     * - Trámites por mes (últimos 6 meses)
     * - Distribución de trámites por estado (para gráficas)
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<ApiResponse<EstadisticasDTO>> obtenerEstadisticas() {
        EstadisticasDTO estadisticas = dashboardService.obtenerEstadisticas();
        return ResponseEntity.ok(ApiResponse.ok("Estadísticas obtenidas exitosamente", estadisticas));
    }
}
