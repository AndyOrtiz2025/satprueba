// TramiteMapper.java
package com.gestor.mapper;

import com.gestor.dto.TramiteDTO;
import com.gestor.entity.Tramite;

public class TramiteMapper {
    public static TramiteDTO toDTO(Tramite e){
        if(e==null) return null;
        TramiteDTO d = new TramiteDTO();
        d.setId(e.getId());
        d.setNombre(e.getNombre());
        d.setDescripcion(e.getDescripcion());
        d.setRequisitos(e.getRequisitos());
        d.setGrupoId(e.getGrupo()!=null? e.getGrupo().getId(): null);
        d.setGrupoNombre(e.getGrupo()!=null? e.getGrupo().getNombre(): null);
        d.setLink(e.getLink());
        return d;
    }
}
