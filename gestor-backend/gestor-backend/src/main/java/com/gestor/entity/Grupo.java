// Grupo.java
package com.gestor.entity;

import jakarta.persistence.*;

@Entity @Table(name="grupos")
public class Grupo {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=120)
    private String nombre;

    @Column(length=1024)
    private String link;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id){ this.id = id; }
    public String getNombre(){ return nombre; }
    public void setNombre(String nombre){ this.nombre = nombre; }
    public String getLink(){ return link; }
    public void setLink(String link){ this.link = link; }
}
