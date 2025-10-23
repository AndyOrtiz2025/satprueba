// TramiteMapper.java
package com.gestor.mapper;

import com.gestor.dto.TramiteDTO;
import com.gestor.entity.Tramite;

public class TramiteMapper {
    public static TramiteDTO toDTO(Tramite e){
        if(e==null) return null;
        TramiteDTO d = new TramiteDTO();
        d.setId(e.getIdTramites());
        d.setNombre(e.getNombre());
        d.setDescripcion(e.getDescripcion());
        d.setTipoTramiteId(e.getTipoTramite()!=null? e.getTipoTramite().getIdTipoTramite(): null);
        d.setTipoTramitePortal(e.getTipoTramite()!=null? e.getTipoTramite().getPortal(): null);
        d.setTipoTramiteLink(e.getTipoTramite()!=null? e.getTipoTramite().getLink(): null);
        return d;
    }
}
