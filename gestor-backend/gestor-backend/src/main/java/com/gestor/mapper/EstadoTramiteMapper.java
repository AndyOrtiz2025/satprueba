// EstadoTramiteMapper.java
package com.gestor.mapper;

import com.gestor.dto.EstadoTramiteDTO;
import com.gestor.entity.EstadoTramite;

public class EstadoTramiteMapper {
    public static EstadoTramiteDTO toDTO(EstadoTramite e){
        if(e==null) return null;
        EstadoTramiteDTO d = new EstadoTramiteDTO();
        d.setId(e.getId());
        d.setTramiteId(e.getTramite()!=null? e.getTramite().getId(): null);
        d.setClienteId(e.getCliente()!=null? e.getCliente().getId(): null);
        d.setNombreTramite(e.getTramite()!=null? e.getTramite().getNombre(): null);
        d.setNombreCliente(e.getCliente()!=null? e.getCliente().getNombre()+" "+e.getCliente().getApellidos(): null);
        d.setFechaInicio(e.getFechaInicio());
        d.setEstado(e.getEstado());
        return d;
    }
}
