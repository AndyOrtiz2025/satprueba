package com.gestor.mapper;

import com.gestor.dto.ClienteDTO;
import com.gestor.entity.Cliente;

public class ClienteMapper {

    public static ClienteDTO toDTO(Cliente e){
        if(e==null) return null;
        ClienteDTO d = new ClienteDTO();
        d.setId(e.getId());
        d.setNombre(e.getNombre());
        d.setApellidos(e.getApellidos());
        d.setTelefono(e.getTelefono());
        d.setDireccion(e.getDireccion());
        d.setNis(e.getNis());
        d.setDpi(e.getDpi());
        d.setNit(e.getNit());
        d.setCuentaBancaria(e.getCuentaBancaria());
        d.setPassNit(e.getPassNit());
        d.setPassCgc(e.getPassCgc());
        d.setPassReghae(e.getPassReghae());
        d.setPassGeneral(e.getPassGeneral());
        return d;
    }

    public static Cliente toEntity(ClienteDTO d){
        if(d==null) return null;
        Cliente e = new Cliente();
        e.setId(d.getId());
        e.setNombre(nullIfBlank(d.getNombre()));
        e.setApellidos(nullIfBlank(d.getApellidos()));
        e.setTelefono(nullIfBlank(d.getTelefono()));
        e.setDireccion(nullIfBlank(d.getDireccion()));
        e.setNis(nullIfBlank(d.getNis()));
        // e.setFechaNacimiento(...)  // eliminado
        e.setDpi(nullIfBlank(d.getDpi()));
        e.setNit(nullIfBlank(d.getNit()));
        e.setCuentaBancaria(nullIfBlank(d.getCuentaBancaria()));
        e.setPassNit(nullIfBlank(d.getPassNit()));
        e.setPassCgc(nullIfBlank(d.getPassCgc()));
        e.setPassReghae(nullIfBlank(d.getPassReghae()));
        e.setPassGeneral(nullIfBlank(d.getPassGeneral()));
        return e;
    }

    public static void merge(ClienteDTO d, Cliente e){
        if (d==null || e==null) return;
        e.setNombre(nullIfBlank(d.getNombre()));
        e.setApellidos(nullIfBlank(d.getApellidos()));
        e.setTelefono(nullIfBlank(d.getTelefono()));
        e.setDireccion(nullIfBlank(d.getDireccion()));
        e.setNis(nullIfBlank(d.getNis()));
        // e.setFechaNacimiento(...)  // eliminado
        e.setDpi(nullIfBlank(d.getDpi()));
        e.setNit(nullIfBlank(d.getNit()));
        e.setCuentaBancaria(nullIfBlank(d.getCuentaBancaria()));
        e.setPassNit(nullIfBlank(d.getPassNit()));
        e.setPassCgc(nullIfBlank(d.getPassCgc()));
        e.setPassReghae(nullIfBlank(d.getPassReghae()));
        e.setPassGeneral(nullIfBlank(d.getPassGeneral()));
    }

    private static String nullIfBlank(String s){
        return (s==null || s.trim().isEmpty()) ? null : s.trim();
    }
}
