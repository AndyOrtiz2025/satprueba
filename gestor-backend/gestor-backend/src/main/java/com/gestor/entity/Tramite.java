// Tramite.java
package com.gestor.entity;

import jakarta.persistence.*;

@Entity @Table(name="tramites")
public class Tramite {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=160)
    private String nombre;

    @Column(columnDefinition="text")
    private String descripcion;

    @Column(columnDefinition="text")
    private String requisitos;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="grupo_id")
    private Grupo grupo;

    @Column(length=1024)
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
    public Grupo getGrupo(){ return grupo; }
    public void setGrupo(Grupo grupo){ this.grupo = grupo; }
    public String getLink(){ return link; }
    public void setLink(String link){ this.link = link; }
}
