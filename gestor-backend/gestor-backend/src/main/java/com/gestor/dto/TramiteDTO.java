// TramiteDTO.java
package com.gestor.dto;

public class TramiteDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String requisitos;
    private Long grupoId;
    private String grupoNombre;
    private String link;

    // getters/setters
    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }
    public String getNombre(){ return nombre; }
    public void setNombre(String nombre){ this.nombre = nombre; }
    public String getDescripcion(){ return descripcion; }
    public void setDescripcion(String descripcion){ this.descripcion = descripcion; }
    public String getRequisitos(){ return requisitos; }
    public void setRequisitos(String requisitos){ this.requisitos = requisitos; }
    public Long getGrupoId(){ return grupoId; }
    public void setGrupoId(Long grupoId){ this.grupoId = grupoId; }
    public String getGrupoNombre(){ return grupoNombre; }
    public void setGrupoNombre(String grupoNombre){ this.grupoNombre = grupoNombre; }
    public String getLink(){ return link; }
    public void setLink(String link){ this.link = link; }
}
