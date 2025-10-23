// ClienteService.java
package com.gestor.service;

import com.gestor.dto.ClienteDTO;
import com.gestor.dto.ClienteCreateDTO;
import com.gestor.dto.ClienteResponseDTO;
import com.gestor.dto.ClienteDetalleDTO;
import com.gestor.util.PageResponse;

public interface ClienteService {
    // === NUEVO MÉTODO PARA CU-SAT001 ===
    ClienteResponseDTO createCliente(ClienteCreateDTO dto);

    // === NUEVOS MÉTODOS PARA CU-SAT002: Consultar Cliente ===

    /**
     * Obtiene el detalle completo de un cliente incluyendo trámites asociados
     * @param id ID del cliente
     * @return ClienteDetalleDTO con toda la información
     */
    ClienteDetalleDTO obtenerClienteDetalle(Long id);

    /**
     * Busca un cliente por su DPI
     * @param dpi DPI del cliente
     * @return ClienteDetalleDTO con toda la información
     */
    ClienteDetalleDTO buscarClientePorDpi(String dpi);

    // === MÉTODOS ANTIGUOS (Compatibilidad) ===
    ClienteDTO create(ClienteDTO dto);
    ClienteDTO update(Long id, ClienteDTO dto);
    void delete(Long id);
    ClienteDTO getById(Long id);
    PageResponse<ClienteDTO> search(String q, int page, int size);
}
