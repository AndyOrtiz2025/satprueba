// EstadoTramiteService.java
package com.gestor.service;

import com.gestor.dto.EstadoTramiteDTO;
import java.util.List;

public interface EstadoTramiteService {
    EstadoTramiteDTO create(EstadoTramiteDTO dto);
    EstadoTramiteDTO updateEstado(Long id, String nuevoEstado);
    List<EstadoTramiteDTO> listAll();
}
