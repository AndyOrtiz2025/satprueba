// BusquedaService.java
package com.gestor.service;

import com.gestor.dto.BusquedaAvanzadaDTO;
import com.gestor.dto.BusquedaPaginadaResponseDTO;

/**
 * Servicio para CU-SAT011: Buscador Avanzado
 */
public interface BusquedaService {

    /**
     * Busca con filtros dinámicos y devuelve resultados paginados
     * @param filtros DTO con los filtros de búsqueda
     * @return Respuesta paginada con resultados
     */
    BusquedaPaginadaResponseDTO buscarConFiltros(BusquedaAvanzadaDTO filtros);
}
