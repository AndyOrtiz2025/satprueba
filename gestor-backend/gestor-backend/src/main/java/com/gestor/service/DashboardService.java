// DashboardService.java
package com.gestor.service;

import com.gestor.dto.EstadisticasDTO;

/**
 * Servicio para CU-SAT012: Panel de Control
 */
public interface DashboardService {

    /**
     * Obtiene todas las estadísticas del sistema para el dashboard
     * @return EstadisticasDTO con todas las métricas
     */
    EstadisticasDTO obtenerEstadisticas();
}
