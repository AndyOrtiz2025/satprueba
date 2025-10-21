package com.gestor.entity;

import jakarta.persistence.*;

@Entity
@Table(name="clientes")
public class Cliente {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=120)
    private String nombre;

    @Column(nullable=false, length=150)
    private String apellidos;

    @Column(length=50)
    private String telefono;

    @Column(length=255)
    private String direccion;

    @Column(length=100)
    private String nis;

    // ❌ eliminado: @Column(name="fecha_nacimiento") private LocalDate fechaNacimiento;

    @Column(length=50)
    private String dpi;

    @Column(length=50)
    private String nit;

    @Column(name="cuenta_bancaria", length=100)
    private String cuentaBancaria;

    @Column(name="pass_nit", length=120)
    private String passNit;

    @Column(name="pass_cgc", length=120)
    private String passCgc;

    @Column(name="pass_reghae", length=120)
    private String passReghae;

    @Column(name="pass_general", length=120)
    private String passGeneral;

    // getters/setters
    public Long getId(){ return id; } public void setId(Long id){ this.id = id; }
    public String getNombre(){ return nombre; } public void setNombre(String v){ this.nombre = v; }
    public String getApellidos(){ return apellidos; } public void setApellidos(String v){ this.apellidos = v; }
    public String getTelefono(){ return telefono; } public void setTelefono(String v){ this.telefono = v; }
    public String getDireccion(){ return direccion; } public void setDireccion(String v){ this.direccion = v; }
    public String getNis(){ return nis; } public void setNis(String v){ this.nis = v; }
    public String getDpi(){ return dpi; } public void setDpi(String v){ this.dpi = v; }
    public String getNit(){ return nit; } public void setNit(String v){ this.nit = v; }
    public String getCuentaBancaria(){ return cuentaBancaria; } public void setCuentaBancaria(String v){ this.cuentaBancaria = v; }
    public String getPassNit(){ return passNit; } public void setPassNit(String v){ this.passNit = v; }
    public String getPassCgc(){ return passCgc; } public void setPassCgc(String v){ this.passCgc = v; }
    public String getPassReghae(){ return passReghae; } public void setPassReghae(String v){ this.passReghae = v; }
    public String getPassGeneral(){ return passGeneral; } public void setPassGeneral(String v){ this.passGeneral = v; }
}
