// impl/TramiteServiceImpl.java
package com.gestor.service.impl;

import com.gestor.dto.TramiteDTO;
import com.gestor.dto.HistorialTramiteDTO;
import com.gestor.entity.*;
import com.gestor.exception.NotFoundException;
import com.gestor.mapper.TramiteMapper;
import com.gestor.repository.TipoTramiteRepository;
import com.gestor.repository.TramiteRepository;
import com.gestor.repository.ConsultaTramiteRepository;
import com.gestor.repository.ArchivosRepository;
import com.gestor.service.TramiteService;
import com.gestor.util.PageResponse;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TramiteServiceImpl implements TramiteService {

    private final TramiteRepository repo;
    private final TipoTramiteRepository tipoTramiteRepo;
    private final ConsultaTramiteRepository consultaTramiteRepo;
    private final ArchivosRepository archivosRepo;

    public TramiteServiceImpl(TramiteRepository repo,
                              TipoTramiteRepository tipoTramiteRepo,
                              ConsultaTramiteRepository consultaTramiteRepo,
                              ArchivosRepository archivosRepo){
        this.repo = repo;
        this.tipoTramiteRepo = tipoTramiteRepo;
        this.consultaTramiteRepo = consultaTramiteRepo;
        this.archivosRepo = archivosRepo;
    }

    @Override
    public TramiteDTO create(TramiteDTO dto) {
        TipoTramite tt = tipoTramiteRepo.findById(dto.getTipoTramiteId())
                .orElseThrow(() -> new NotFoundException("TipoTramite no encontrado"));
        Tramite t = new Tramite();
        t.setNombre(dto.getNombre());
        t.setDescripcion(dto.getDescripcion());
        t.setTipoTramite(tt);
        t = repo.save(t);
        return TramiteMapper.toDTO(t);
    }

    @Override
    public TramiteDTO update(Long id, TramiteDTO dto) {
        Tramite t = repo.findById(id).orElseThrow(() -> new NotFoundException("Trámite no encontrado"));
        if(dto.getTipoTramiteId()!=null){
            TipoTramite tt = tipoTramiteRepo.findById(dto.getTipoTramiteId())
                    .orElseThrow(() -> new NotFoundException("TipoTramite no encontrado"));
            t.setTipoTramite(tt);
        }
        t.setNombre(dto.getNombre());
        t.setDescripcion(dto.getDescripcion());
        t = repo.save(t);
        return TramiteMapper.toDTO(t);
    }

    @Override
    public void delete(Long id) {
        if(!repo.existsById(id)) throw new NotFoundException("Trámite no encontrado");
        repo.deleteById(id);
    }

    @Override
    public TramiteDTO getById(Long id) {
        return repo.findById(id).map(TramiteMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Trámite no encontrado"));
    }

    @Override
    public PageResponse<TramiteDTO> search(String q, int page, int size) {
        Pageable p = PageRequest.of(Math.max(page,0), Math.max(size,1), Sort.by("idTramites").descending());
        Page<Tramite> res = (q==null || q.isBlank()) ? repo.findAll(p) : repo.findByNombreContainingIgnoreCase(q, p);
        return new PageResponse<>(
                res.getContent().stream().map(TramiteMapper::toDTO).collect(Collectors.toList()),
                res.getTotalElements(), res.getTotalPages(), res.getNumber(), res.getSize()
        );
    }

    /**
     * CU-SAT005: Historial de Trámites
     * Implementación del método para obtener el historial completo de trámites de un cliente
     */
    @Override
    public PageResponse<HistorialTramiteDTO> obtenerHistorialCliente(Long idCliente, int page, int size) {
        // Validar y ajustar parámetros de paginación
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));

        // Obtener consultas de trámites del cliente con información relacionada (JOIN FETCH)
        Page<ConsultaTramite> consultas = consultaTramiteRepo.findHistorialByClienteId(idCliente, pageable);

        // Mapear cada consulta a HistorialTramiteDTO con sus archivos asociados
        List<HistorialTramiteDTO> historialList = consultas.getContent().stream().map(consulta -> {
            // Obtener datos del trámite y tipo de trámite
            Tramite tramite = consulta.getTramite();
            TipoTramite tipoTramite = tramite.getTipoTramite();

            // Crear el DTO principal
            HistorialTramiteDTO dto = new HistorialTramiteDTO(
                    consulta.getIdConsultaTramite(),
                    consulta.getFechaTramite(),
                    consulta.getEstado(),
                    consulta.getCreatedAt(),
                    consulta.getUpdatedAt(),
                    tramite.getIdTramites(),
                    tramite.getNombre(),
                    tramite.getDescripcion(),
                    tipoTramite.getPortal(),
                    tipoTramite.getLink()
            );

            // Obtener archivos asociados a este trámite y cliente
            List<Archivos> archivos = archivosRepo.findByCliente_IdClienteAndTramite_IdTramites(
                    idCliente,
                    tramite.getIdTramites(),
                    Pageable.unpaged()
            ).getContent();

            // Mapear archivos a DTOs
            List<HistorialTramiteDTO.ArchivoDTO> archivoDTOs = archivos.stream()
                    .map(archivo -> new HistorialTramiteDTO.ArchivoDTO(
                            archivo.getIdArchivos(),
                            archivo.getNombreArchivo(),
                            archivo.getRuta(),
                            archivo.getFechaSubida()
                    ))
                    .collect(Collectors.toList());

            dto.setArchivos(archivoDTOs);

            return dto;
        }).collect(Collectors.toList());

        // Retornar respuesta paginada
        return new PageResponse<>(
                historialList,
                consultas.getTotalElements(),
                consultas.getTotalPages(),
                consultas.getNumber(),
                consultas.getSize()
        );
    }
}
