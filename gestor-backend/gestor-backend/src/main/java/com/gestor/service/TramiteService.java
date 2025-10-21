// TramiteService.java
package com.gestor.service;

import com.gestor.dto.TramiteDTO;
import com.gestor.util.PageResponse;

public interface TramiteService {
    TramiteDTO create(TramiteDTO dto);
    TramiteDTO update(Long id, TramiteDTO dto);
    void delete(Long id);
    TramiteDTO getById(Long id);
    PageResponse<TramiteDTO> search(String q, int page, int size);
}
