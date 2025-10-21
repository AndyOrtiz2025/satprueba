// GrupoMapper.java
package com.gestor.mapper;

import com.gestor.dto.GrupoDTO;
import com.gestor.entity.Grupo;

public class GrupoMapper {
    public static GrupoDTO toDTO(Grupo e){
        if(e==null) return null;
        GrupoDTO d = new GrupoDTO();
        d.setId(e.getId());
        d.setNombre(e.getNombre());
        d.setLink(e.getLink());
        return d;
    }
}
