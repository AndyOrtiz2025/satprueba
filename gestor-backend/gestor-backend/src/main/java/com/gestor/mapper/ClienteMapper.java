package com.gestor.mapper;

import com.gestor.dto.ClienteDTO;
import com.gestor.dto.ClienteCreateDTO;
import com.gestor.dto.ClienteResponseDTO;
import com.gestor.entity.Cliente;
import com.gestor.entity.Datos;

public class ClienteMapper {

    // === NUEVOS MÉTODOS PARA CU-SAT001 ===

    /**
     * Convierte ClienteCreateDTO a entidad Cliente (sin Usuario ni Datos)
     * El Usuario y Datos se establecen en el Service
     */
    public static Cliente toEntity(ClienteCreateDTO dto) {
        if(dto == null) return null;
        Cliente cliente = new Cliente();
        cliente.setNombreCompleto(nullIfBlank(dto.getNombreCompleto()));
        cliente.setDpi(nullIfBlank(dto.getDpi()));
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
        cliente.setTelefono(nullIfBlank(dto.getTelefono()));
        cliente.setDireccion(nullIfBlank(dto.getDireccion()));
        return cliente;
    }

    /**
     * Convierte entidad Cliente a ClienteResponseDTO (sin información sensible)
     */
    public static ClienteResponseDTO toResponseDTO(Cliente e) {
        if(e == null) return null;
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(e.getIdCliente());
        dto.setIdUsuario(e.getUsuario() != null ? e.getUsuario().getIdUsuario() : null);
        dto.setNombreCompleto(e.getNombreCompleto());
        dto.setDpi(e.getDpi());
        dto.setFechaNacimiento(e.getFechaNacimiento());
        dto.setTelefono(e.getTelefono());
        dto.setDireccion(e.getDireccion());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());

        // Solo incluir NIT y email (sin contraseñas)
        if(e.getDatos() != null) {
            dto.setNit(e.getDatos().getNit());
            dto.setEmail(e.getDatos().getEmail());
        }
        return dto;
    }

    // === MÉTODOS ANTIGUOS (Compatibilidad) ===

    public static ClienteDTO toDTO(Cliente e){
        if(e==null) return null;
        ClienteDTO d = new ClienteDTO();
        d.setId(e.getIdCliente());
        d.setIdUsuario(e.getUsuario() != null ? e.getUsuario().getIdUsuario() : null);
        d.setNombreCompleto(e.getNombreCompleto());
        d.setTelefono(e.getTelefono());
        d.setDireccion(e.getDireccion());
        d.setDpi(e.getDpi());
        d.setFechaNacimiento(e.getFechaNacimiento());

        // Mapear datos sensibles si existen
        if(e.getDatos() != null) {
            Datos datos = e.getDatos();
            d.setNis(datos.getNis() != null ? String.valueOf(datos.getNis()) : null);
            d.setNit(datos.getNit());
            d.setEmail(datos.getEmail());
            d.setCuentaBancaria(datos.getCuentaBancaria());
            d.setPassAgenciaVirtual(datos.getContrasenaAgenciaVirtual());
            d.setPassCorreo(datos.getContrasenaCorreo());
            d.setPassCgc(datos.getContrasenaCgc());
            d.setPassConsultaGeneral(datos.getContrasenaConsultaGeneral());
            d.setPassReghae(datos.getContrasenaRegahe());
        }
        return d;
    }

    public static Cliente toEntity(ClienteDTO d){
        if(d==null) return null;
        Cliente e = new Cliente();
        e.setIdCliente(d.getId());
        // Usuario se establece en el Service
        e.setNombreCompleto(nullIfBlank(d.getNombreCompleto()));
        e.setTelefono(nullIfBlank(d.getTelefono()));
        e.setDireccion(nullIfBlank(d.getDireccion()));
        e.setDpi(nullIfBlank(d.getDpi()));
        e.setFechaNacimiento(d.getFechaNacimiento());
        return e;
    }

    public static void merge(ClienteDTO d, Cliente e){
        if (d==null || e==null) return;
        e.setNombreCompleto(nullIfBlank(d.getNombreCompleto()));
        e.setTelefono(nullIfBlank(d.getTelefono()));
        e.setDireccion(nullIfBlank(d.getDireccion()));
        e.setDpi(nullIfBlank(d.getDpi()));
        e.setFechaNacimiento(d.getFechaNacimiento());

        // Actualizar datos sensibles
        if(e.getDatos() != null) {
            Datos datos = e.getDatos();
            if(d.getNis() != null && !d.getNis().isBlank()) {
                try {
                    datos.setNis(Integer.parseInt(d.getNis().trim()));
                } catch(NumberFormatException ex) {
                    // Ignorar si no es un número válido
                }
            }
            datos.setNit(nullIfBlank(d.getNit()));
            datos.setEmail(nullIfBlank(d.getEmail()));
            datos.setCuentaBancaria(nullIfBlank(d.getCuentaBancaria()));
            datos.setContrasenaAgenciaVirtual(nullIfBlank(d.getPassAgenciaVirtual()));
            datos.setContrasenaCorreo(nullIfBlank(d.getPassCorreo()));
            datos.setContrasenaCgc(nullIfBlank(d.getPassCgc()));
            datos.setContrasenaConsultaGeneral(nullIfBlank(d.getPassConsultaGeneral()));
            datos.setContrasenaRegahe(nullIfBlank(d.getPassReghae()));
        }
    }

    private static String nullIfBlank(String s){
        return (s==null || s.trim().isEmpty()) ? null : s.trim();
    }
}
