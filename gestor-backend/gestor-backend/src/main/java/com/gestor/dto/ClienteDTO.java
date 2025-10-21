package com.gestor.dto;

public class ClienteDTO {
    private Long id;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String direccion;
    private String nis;
    private String dpi;
    private String nit;
    private String cuentaBancaria;
    private String passNit;
    private String passCgc;
    private String passReghae;
    private String passGeneral;

    public Long getId(){ return id; }             public void setId(Long id){ this.id = id; }
    public String getNombre(){ return nombre; }    public void setNombre(String v){ this.nombre = v; }
    public String getApellidos(){ return apellidos; } public void setApellidos(String v){ this.apellidos = v; }
    public String getTelefono(){ return telefono; } public void setTelefono(String v){ this.telefono = v; }
    public String getDireccion(){ return direccion; } public void setDireccion(String v){ this.direccion = v; }
    public String getNis(){ return nis; }          public void setNis(String v){ this.nis = v; }
    public String getDpi(){ return dpi; }          public void setDpi(String v){ this.dpi = v; }
    public String getNit(){ return nit; }          public void setNit(String v){ this.nit = v; }
    public String getCuentaBancaria(){ return cuentaBancaria; } public void setCuentaBancaria(String v){ this.cuentaBancaria = v; }
    public String getPassNit(){ return passNit; }  public void setPassNit(String v){ this.passNit = v; }
    public String getPassCgc(){ return passCgc; }  public void setPassCgc(String v){ this.passCgc = v; }
    public String getPassReghae(){ return passReghae; } public void setPassReghae(String v){ this.passReghae = v; }
    public String getPassGeneral(){ return passGeneral; } public void setPassGeneral(String v){ this.passGeneral = v; }
}
