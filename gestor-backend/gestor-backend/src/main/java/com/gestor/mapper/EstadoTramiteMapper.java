// EstadoTramiteMapper.java (actualizado para ConsultaTramite)
package com.gestor.mapper;

import com.gestor.dto.EstadoTramiteDTO;
import com.gestor.entity.ConsultaTramite;

public class EstadoTramiteMapper {
    public static EstadoTramiteDTO toDTO(ConsultaTramite e){
        if(e==null) return null;
        EstadoTramiteDTO d = new EstadoTramiteDTO();
        d.setId(e.getIdConsultaTramite());
        d.setTramiteId(e.getTramite()!=null? e.getTramite().getIdTramites(): null);
        d.setClienteId(e.getCliente()!=null? e.getCliente().getIdCliente(): null);
        d.setNombreTramite(e.getTramite()!=null? e.getTramite().getNombre(): null);
        d.setNombreCliente(e.getCliente()!=null? e.getCliente().getNombreCompleto(): null);
        d.setFechaInicio(e.getFechaTramite());
        d.setEstado(e.getEstado());
        return d;
    }
}
