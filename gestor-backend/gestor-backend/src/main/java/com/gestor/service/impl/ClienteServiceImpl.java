// impl/ClienteServiceImpl.java
package com.gestor.service.impl;

import com.gestor.dto.ClienteDTO;
import com.gestor.dto.ClienteCreateDTO;
import com.gestor.dto.ClienteResponseDTO;
import com.gestor.dto.ClienteDetalleDTO;
import com.gestor.entity.Cliente;
import com.gestor.entity.Usuario;
import com.gestor.entity.Datos;
import com.gestor.entity.ConsultaTramite;
import com.gestor.exception.NotFoundException;
import com.gestor.exception.BadRequestException;
import com.gestor.mapper.ClienteMapper;
import com.gestor.repository.ClienteRepository;
import com.gestor.repository.UsuarioRepository;
import com.gestor.repository.DatosRepository;
import com.gestor.repository.ConsultaTramiteRepository;
import com.gestor.service.ClienteService;
import com.gestor.util.PageResponse;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repo;
    private final UsuarioRepository usuarioRepo;
    private final DatosRepository datosRepo;
    private final ConsultaTramiteRepository consultaTramiteRepo;

    public ClienteServiceImpl(ClienteRepository repo,
                             UsuarioRepository usuarioRepo,
                             DatosRepository datosRepo,
                             ConsultaTramiteRepository consultaTramiteRepo){
        this.repo = repo;
        this.usuarioRepo = usuarioRepo;
        this.datosRepo = datosRepo;
        this.consultaTramiteRepo = consultaTramiteRepo;
    }

    // === NUEVO MÉTODO PARA CU-SAT001: Registrar Cliente ===

    @Override
    @Transactional
    public ClienteResponseDTO createCliente(ClienteCreateDTO dto) {
        // Validación 1: DPI único
        if(repo.existsByDpi(dto.getDpi())) {
            throw new BadRequestException("El DPI ya está registrado en el sistema");
        }

        // Validación 2: Mayor de 18 años
        LocalDate fechaLimite = LocalDate.now().minusYears(18);
        if(dto.getFechaNacimiento().isAfter(fechaLimite)) {
            throw new BadRequestException("El cliente debe ser mayor de 18 años");
        }

        // Buscar el usuario
        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + dto.getIdUsuario()));

        // Crear el cliente usando el mapper
        Cliente cliente = ClienteMapper.toEntity(dto);
        cliente.setUsuario(usuario);
        cliente = repo.save(cliente);

        // Crear datos sensibles si vienen en el DTO
        if(dto.getNit() != null || dto.getNis() != null || dto.getEmail() != null ||
           dto.getCuentaBancaria() != null || dto.getPassAgenciaVirtual() != null ||
           dto.getPassCorreo() != null || dto.getPassCgc() != null ||
           dto.getPassConsultaGeneral() != null || dto.getPassReghae() != null) {

            Datos datos = new Datos();
            datos.setCliente(cliente);
            datos.setDpi(dto.getDpi());
            datos.setNit(dto.getNit());
            datos.setEmail(dto.getEmail());
            datos.setCuentaBancaria(dto.getCuentaBancaria());
            datos.setContrasenaAgenciaVirtual(dto.getPassAgenciaVirtual());
            datos.setContrasenaCorreo(dto.getPassCorreo());
            datos.setContrasenaCgc(dto.getPassCgc());
            datos.setContrasenaConsultaGeneral(dto.getPassConsultaGeneral());
            datos.setContrasenaRegahe(dto.getPassReghae());

            if(dto.getNis() != null && !dto.getNis().isBlank()) {
                try {
                    datos.setNis(Integer.parseInt(dto.getNis().trim()));
                } catch(NumberFormatException ex) {
                    throw new BadRequestException("El NIS debe ser un número válido");
                }
            }

            datosRepo.save(datos);
        }

        return ClienteMapper.toResponseDTO(cliente);
    }

    // === MÉTODOS ANTIGUOS (Compatibilidad) ===

    @Override
    @Transactional
    public ClienteDTO create(ClienteDTO dto) {
        System.out.println("=== DEBUG: idUsuario recibido: " + dto.getIdUsuario());

        if(dto.getIdUsuario() == null) {
            throw new BadRequestException("idUsuario es requerido");
        }

        // Buscar el usuario
        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + dto.getIdUsuario()));

        System.out.println("=== DEBUG: Usuario encontrado: " + usuario.getIdUsuario() + " - " + usuario.getUsuario());

        Cliente e = new Cliente();
        e.setNombreCompleto(dto.getNombreCompleto());
        e.setTelefono(dto.getTelefono());
        e.setDireccion(dto.getDireccion());
        e.setDpi(dto.getDpi());
        e.setFechaNacimiento(dto.getFechaNacimiento());
        e.setUsuario(usuario);

        System.out.println("=== DEBUG: Antes de guardar - Usuario en Cliente: " +
            (e.getUsuario() != null ? e.getUsuario().getIdUsuario() : "NULL"));

        e = repo.save(e);

        System.out.println("=== DEBUG: Cliente guardado con ID: " + e.getIdCliente());

        // Crear datos sensibles si vienen en el DTO
        if(dto.getNit() != null || dto.getNis() != null || dto.getEmail() != null) {
            Datos datos = new Datos();
            datos.setCliente(e);
            if(dto.getNis() != null && !dto.getNis().isBlank()) {
                try {
                    datos.setNis(Integer.parseInt(dto.getNis().trim()));
                } catch(NumberFormatException ex) {
                    // Ignorar si no es válido
                }
            }
            datos.setNit(dto.getNit());
            datos.setEmail(dto.getEmail());
            datos.setDpi(dto.getDpi());
            datos.setCuentaBancaria(dto.getCuentaBancaria());
            datos.setContrasenaAgenciaVirtual(dto.getPassAgenciaVirtual());
            datos.setContrasenaCorreo(dto.getPassCorreo());
            datos.setContrasenaCgc(dto.getPassCgc());
            datos.setContrasenaConsultaGeneral(dto.getPassConsultaGeneral());
            datos.setContrasenaRegahe(dto.getPassReghae());
            datosRepo.save(datos);
        }

        return ClienteMapper.toDTO(e);
    }

    @Override
    public ClienteDTO update(Long id, ClienteDTO dto) {
        Cliente e = repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        // actualizar campos
        ClienteMapper.merge(dto, e);
        e = repo.save(e);
        return ClienteMapper.toDTO(e);
    }

    @Override
    public void delete(Long id) {
        if(!repo.existsById(id)) throw new NotFoundException("Cliente no encontrado");
        repo.deleteById(id);
    }

    @Override
    public ClienteDTO getById(Long id) {
        return repo.findById(id).map(ClienteMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
    }

    @Override
    public PageResponse<ClienteDTO> search(String q, int page, int size) {
        Pageable p = PageRequest.of(Math.max(page,0), Math.max(size,1), Sort.by("idCliente").descending());
        Page<Cliente> res;
        if(q==null || q.isBlank()){
            res = repo.findAll(p);
        } else {
            res = repo.findByNombreCompletoContainingIgnoreCase(q, p);
        }
        return new PageResponse<>(
                res.getContent().stream().map(ClienteMapper::toDTO).collect(Collectors.toList()),
                res.getTotalElements(), res.getTotalPages(), res.getNumber(), res.getSize()
        );
    }

    // === NUEVOS MÉTODOS PARA CU-SAT002: Consultar Cliente ===

    @Override
    public ClienteDetalleDTO obtenerClienteDetalle(Long id) {
        // Validar que el cliente existe
        Cliente cliente = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con ID: " + id));

        return construirClienteDetalle(cliente);
    }

    @Override
    public ClienteDetalleDTO buscarClientePorDpi(String dpi) {
        // Validar que el DPI no sea null o vacío
        if (dpi == null || dpi.isBlank()) {
            throw new BadRequestException("El DPI es requerido");
        }

        // Buscar cliente por DPI
        Cliente cliente = repo.findByDpi(dpi)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con DPI: " + dpi));

        return construirClienteDetalle(cliente);
    }

    /**
     * Método auxiliar para construir ClienteDetalleDTO con trámites asociados
     */
    private ClienteDetalleDTO construirClienteDetalle(Cliente cliente) {
        ClienteDetalleDTO detalle = new ClienteDetalleDTO();

        // Información básica del cliente
        detalle.setIdCliente(cliente.getIdCliente());
        detalle.setNombreCompleto(cliente.getNombreCompleto());
        detalle.setDpi(cliente.getDpi());
        detalle.setFechaNacimiento(cliente.getFechaNacimiento());
        detalle.setTelefono(cliente.getTelefono());
        detalle.setDireccion(cliente.getDireccion());

        // Información del usuario
        detalle.setIdUsuario(cliente.getUsuario().getIdUsuario());
        detalle.setNombreUsuario(cliente.getUsuario().getUsuario());

        // Auditoría
        detalle.setCreatedAt(cliente.getCreatedAt());
        detalle.setUpdatedAt(cliente.getUpdatedAt());
        detalle.setCreatedBy(cliente.getCreatedBy());
        detalle.setUpdatedBy(cliente.getUpdatedBy());

        // Datos sensibles (si existen)
        Datos datos = cliente.getDatos();
        if (datos != null) {
            detalle.setNit(datos.getNit());
            detalle.setNis(datos.getNis() != null ? datos.getNis().toString() : null);
            detalle.setEmail(datos.getEmail());
            detalle.setCuentaBancaria(datos.getCuentaBancaria());
        }

        // Obtener trámites asociados
        List<ConsultaTramite> consultas = consultaTramiteRepo.findByCliente_IdCliente(
                cliente.getIdCliente(),
                Pageable.unpaged()
        ).getContent();

        // Mapear trámites a DTO
        List<ClienteDetalleDTO.TramiteResumenDTO> tramites = consultas.stream()
                .map(ct -> new ClienteDetalleDTO.TramiteResumenDTO(
                        ct.getIdConsultaTramite(),
                        ct.getTramite().getIdTramites(),
                        ct.getTramite().getNombre(),
                        ct.getTramite().getTipoTramite().getPortal(),
                        ct.getFechaTramite(),
                        ct.getEstado(),
                        ct.getCreatedAt()
                ))
                .collect(Collectors.toList());
        detalle.setTramites(tramites);

        // Estadísticas de trámites
        detalle.setTotalTramites((long) tramites.size());
        detalle.setTramitesIniciados(
                tramites.stream().filter(t -> "INICIADO".equals(t.getEstado())).count()
        );
        detalle.setTramitesPendientes(
                tramites.stream().filter(t -> "PENDIENTE".equals(t.getEstado())).count()
        );
        detalle.setTramitesTerminados(
                tramites.stream().filter(t -> "TERMINADO".equals(t.getEstado())).count()
        );

        return detalle;
    }
}
