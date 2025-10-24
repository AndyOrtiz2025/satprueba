package com.gestor.service;

import com.gestor.dto.DatosDTO;
import com.gestor.entity.Cliente;
import com.gestor.entity.Datos;
import com.gestor.repository.ClienteRepository;
import com.gestor.repository.DatosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class DatosService {

    private final DatosRepository datosRepository;
    private final ClienteRepository clienteRepository;

    public DatosService(DatosRepository datosRepository, ClienteRepository clienteRepository) {
        this.datosRepository = datosRepository;
        this.clienteRepository = clienteRepository;
    }

    /**
     * Obtener datos sensibles por ID de cliente
     */
    public Optional<DatosDTO> obtenerPorCliente(Long idCliente) {
        Optional<Datos> datosOpt = datosRepository.findByCliente_IdCliente(idCliente);
        return datosOpt.map(this::convertirADTO);
    }

    /**
     * Guardar o actualizar datos sensibles
     */
    public DatosDTO guardarDatos(Long idCliente, DatosDTO dto) {
        // Validar que el cliente existe
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Buscar si ya existen datos para este cliente
        Datos datos = datosRepository.findByCliente_IdCliente(idCliente)
                .orElseGet(() -> {
                    Datos nuevo = new Datos();
                    nuevo.setCliente(cliente);
                    nuevo.setCreatedBy("Sistema"); // TODO: Obtener usuario actual
                    return nuevo;
                });

        // Actualizar campos
        datos.setNit(dto.getNit());
        datos.setNis(dto.getNis());
        datos.setEmail(dto.getEmail());
        datos.setDpi(dto.getDpi());
        datos.setCuentaBancaria(dto.getCuentaBancaria());
        datos.setContrasenaAgenciaVirtual(dto.getContrasenaAgenciaVirtual());
        datos.setContrasenaCorreo(dto.getContrasenaCorreo());
        datos.setContrasenaCgc(dto.getContrasenaCgc());
        datos.setContrasenaConsultaGeneral(dto.getContrasenaConsultaGeneral());
        datos.setContrasenaRegahe(dto.getContrasenaRegahe());
        datos.setUpdatedBy("Sistema"); // TODO: Obtener usuario actual
        datos.setUpdatedAt(LocalDateTime.now());

        // Guardar
        datos = datosRepository.save(datos);

        return convertirADTO(datos);
    }

    /**
     * Convertir entidad a DTO
     */
    private DatosDTO convertirADTO(Datos datos) {
        DatosDTO dto = new DatosDTO();
        dto.setIdDatos(datos.getIdDatos());
        dto.setIdCliente(datos.getCliente().getIdCliente());
        dto.setNit(datos.getNit());
        dto.setNis(datos.getNis());
        dto.setEmail(datos.getEmail());
        dto.setDpi(datos.getDpi());
        dto.setCuentaBancaria(datos.getCuentaBancaria());
        dto.setContrasenaAgenciaVirtual(datos.getContrasenaAgenciaVirtual());
        dto.setContrasenaCorreo(datos.getContrasenaCorreo());
        dto.setContrasenaCgc(datos.getContrasenaCgc());
        dto.setContrasenaConsultaGeneral(datos.getContrasenaConsultaGeneral());
        dto.setContrasenaRegahe(datos.getContrasenaRegahe());
        dto.setCreatedAt(datos.getCreatedAt());
        dto.setUpdatedAt(datos.getUpdatedAt());
        return dto;
    }
}
