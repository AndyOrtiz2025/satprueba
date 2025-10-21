// EstadoTramite.java
package com.gestor.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity @Table(name="estado_tramite")
public class EstadoTramite {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="tramite_id")
    private Tramite tramite;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="cliente_id")
    private Cliente cliente;

    @Column(name="fecha_inicio")
    private LocalDate fechaInicio;

    @Column(nullable=false, length=20)
    private String estado; // PENDIENTE | EN_PROCESO | FINALIZADO | RECHAZADO

    // getters/setters
    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }
    public Tramite getTramite(){ return tramite; }
    public void setTramite(Tramite tramite){ this.tramite = tramite; }
    public Cliente getCliente(){ return cliente; }
    public void setCliente(Cliente cliente){ this.cliente = cliente; }
    public LocalDate getFechaInicio(){ return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio){ this.fechaInicio = fechaInicio; }
    public String getEstado(){ return estado; }
    public void setEstado(String estado){ this.estado = estado; }
}
