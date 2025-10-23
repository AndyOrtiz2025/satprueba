// BusquedaServiceImpl.java
package com.gestor.service.impl;

import com.gestor.dto.BusquedaAvanzadaDTO;
import com.gestor.dto.BusquedaPaginadaResponseDTO;
import com.gestor.dto.BusquedaResultadoDTO;
import com.gestor.entity.ConsultaTramite;
import com.gestor.repository.ConsultaTramiteRepository;
import com.gestor.service.BusquedaService;
import com.gestor.specification.ConsultaTramiteSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de búsqueda avanzada (CU-SAT011)
 * Adaptado para usar ConsultaTramite
 */
@Service
public class BusquedaServiceImpl implements BusquedaService {

    private final ConsultaTramiteRepository consultaTramiteRepo;

    public BusquedaServiceImpl(ConsultaTramiteRepository consultaTramiteRepo) {
        this.consultaTramiteRepo = consultaTramiteRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public BusquedaPaginadaResponseDTO buscarConFiltros(BusquedaAvanzadaDTO filtros) {

        System.out.println("🔍 Búsqueda avanzada con filtros: " + filtros);

        // Tarea 4: Usar JPA Specifications para construir consultas dinámicas
        Specification<ConsultaTramite> spec = ConsultaTramiteSpecification.conFiltros(filtros);

        // Tarea 5: Implementar paginación y ordenamiento
        Pageable pageable = crearPageable(filtros);

        // Ejecutar consulta con Specification y paginación
        Page<ConsultaTramite> pageResult = consultaTramiteRepo.findAll(spec, pageable);

        // Convertir entidades a DTOs
        List<BusquedaResultadoDTO> resultados = pageResult.getContent().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());

        System.out.println("✅ Encontrados " + pageResult.getTotalElements() + " resultados");

        // Crear respuesta paginada
        return new BusquedaPaginadaResponseDTO(
                resultados,
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages()
        );
    }

    /**
     * Crea un objeto Pageable con paginación y ordenamiento
     */
    private Pageable crearPageable(BusquedaAvanzadaDTO filtros) {
        // Validar y construir el ordenamiento
        Sort sort = crearSort(filtros.getSortBy(), filtros.getSortDir());

        // Crear Pageable con paginación y ordenamiento
        return PageRequest.of(
                filtros.getPage(),
                filtros.getSize(),
                sort
        );
    }

    /**
     * Crea el objeto Sort para ordenamiento
     */
    private Sort crearSort(String sortBy, String sortDir) {
        // Validar campo de ordenamiento (evitar inyección)
        String campoOrdenamiento = validarCampoOrdenamiento(sortBy);

        // Determinar dirección (ASC o DESC)
        Sort.Direction direction = "DESC".equalsIgnoreCase(sortDir)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(direction, campoOrdenamiento);
    }

    /**
     * Valida que el campo de ordenamiento sea seguro
     * Evita SQL injection
     */
    private String validarCampoOrdenamiento(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return "fechaTramite"; // Default
        }

        // Lista blanca de campos permitidos
        return switch (sortBy.toLowerCase()) {
            case "fechatramite", "fecha_tramite", "fechainicio", "fecha_inicio" -> "fechaTramite";
            case "estado" -> "estado";
            case "nombre", "nombrecliente" -> "cliente.nombreCompleto";
            case "dpi" -> "cliente.dpi";
            default -> "fechaTramite"; // Default seguro
        };
    }

    /**
     * Convierte ConsultaTramite a BusquedaResultadoDTO
     */
    private BusquedaResultadoDTO convertirADTO(ConsultaTramite consultaTramite) {
        BusquedaResultadoDTO dto = new BusquedaResultadoDTO();

        // Información del ConsultaTramite
        dto.setEstadoTramiteId(consultaTramite.getIdConsultaTramite());
        dto.setFechaInicio(consultaTramite.getFechaTramite());
        dto.setEstado(consultaTramite.getEstado());

        // Información del Cliente
        if (consultaTramite.getCliente() != null) {
            dto.setClienteId(consultaTramite.getCliente().getIdCliente());
            dto.setNombreCliente(consultaTramite.getCliente().getNombreCompleto());
            dto.setDpiCliente(consultaTramite.getCliente().getDpi());
            dto.setTelefonoCliente(consultaTramite.getCliente().getTelefono());
        }

        // Información del Tramite
        if (consultaTramite.getTramite() != null) {
            dto.setTramiteId(consultaTramite.getTramite().getIdTramites());
            dto.setNombreTramite(consultaTramite.getTramite().getNombre());
            dto.setDescripcionTramite(consultaTramite.getTramite().getDescripcion());

            // Tipo de trámite
            if (consultaTramite.getTramite().getTipoTramite() != null) {
                dto.setTipoTramite(consultaTramite.getTramite().getTipoTramite().getPortal());
            }
        }

        return dto;
    }
}
