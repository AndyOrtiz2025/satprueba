// TramiteDTO.java
package com.gestor.dto;

public class TramiteDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long tipoTramiteId;
    private String tipoTramitePortal;
    private String tipoTramiteLink;

    // getters/setters
    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public String getNombre(){ return nombre; }
    public void setNombre(String nombre){ this.nombre = nombre; }

    public String getDescripcion(){ return descripcion; }
    public void setDescripcion(String descripcion){ this.descripcion = descripcion; }

    public Long getTipoTramiteId(){ return tipoTramiteId; }
    public void setTipoTramiteId(Long tipoTramiteId){ this.tipoTramiteId = tipoTramiteId; }

    public String getTipoTramitePortal(){ return tipoTramitePortal; }
    public void setTipoTramitePortal(String tipoTramitePortal){ this.tipoTramitePortal = tipoTramitePortal; }

    public String getTipoTramiteLink(){ return tipoTramiteLink; }
    public void setTipoTramiteLink(String tipoTramiteLink){ this.tipoTramiteLink = tipoTramiteLink; }
}
