// ClienteService.java
package com.gestor.service;

import com.gestor.dto.ClienteDTO;
import com.gestor.util.PageResponse;

public interface ClienteService {
    ClienteDTO create(ClienteDTO dto);
    ClienteDTO update(Long id, ClienteDTO dto);
    void delete(Long id);
    ClienteDTO getById(Long id);
    PageResponse<ClienteDTO> search(String q, int page, int size);
}
