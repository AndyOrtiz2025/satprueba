// TramiteService.java
package com.gestor.service;

import com.gestor.dto.TramiteDTO;
import com.gestor.dto.HistorialTramiteDTO;
import com.gestor.util.PageResponse;

public interface TramiteService {
    TramiteDTO create(TramiteDTO dto);
    TramiteDTO update(Long id, TramiteDTO dto);
    void delete(Long id);
    TramiteDTO getById(Long id);
    PageResponse<TramiteDTO> search(String q, int page, int size);

    /**
     * CU-SAT005: Historial de Trámites
     * Obtiene el historial completo de trámites de un cliente con archivos asociados
     * @param idCliente ID del cliente
     * @param page número de página (0-indexed)
     * @param size tamaño de página
     * @return PageResponse con el historial de trámites
     */
    PageResponse<HistorialTramiteDTO> obtenerHistorialCliente(Long idCliente, int page, int size);
}
