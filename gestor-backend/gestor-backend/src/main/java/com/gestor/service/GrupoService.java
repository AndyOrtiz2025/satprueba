// GrupoService.java
package com.gestor.service;

import com.gestor.dto.GrupoDTO;
import java.util.List;

public interface GrupoService {
    GrupoDTO create(GrupoDTO dto);
    List<GrupoDTO> listAll();

      GrupoDTO update(Long id, GrupoDTO dto);
    void delete(Long id);
    GrupoDTO getById(Long id);
}
