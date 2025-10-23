// impl/EstadoTramiteServiceImpl.java
package com.gestor.service.impl;

import com.gestor.dto.EstadoTramiteDTO;
import com.gestor.entity.*;
import com.gestor.exception.BadRequestException;
import com.gestor.exception.NotFoundException;
import com.gestor.mapper.EstadoTramiteMapper;
import com.gestor.repository.*;
import com.gestor.service.EstadoTramiteService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoTramiteServiceImpl implements EstadoTramiteService {

    private final ConsultaTramiteRepository repo;
    private final TramiteRepository tramiteRepo;
    private final ClienteRepository clienteRepo;

    public EstadoTramiteServiceImpl(ConsultaTramiteRepository repo, TramiteRepository tramiteRepo, ClienteRepository clienteRepo){
        this.repo = repo;
        this.tramiteRepo = tramiteRepo;
        this.clienteRepo = clienteRepo;
    }

    @Override
    public EstadoTramiteDTO create(EstadoTramiteDTO dto) {
        Tramite t = tramiteRepo.findById(dto.getTramiteId())
                .orElseThrow(() -> new NotFoundException("Trámite no encontrado"));
        Cliente c = clienteRepo.findById(dto.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        validarEstado(dto.getEstado());
        ConsultaTramite e = new ConsultaTramite();
        e.setTramite(t);
        e.setCliente(c);
        e.setFechaTramite(dto.getFechaInicio()!=null ? dto.getFechaInicio() : LocalDate.now());
        e.setEstado(dto.getEstado());
        e = repo.save(e);
        return EstadoTramiteMapper.toDTO(e);
    }

    @Override
    public EstadoTramiteDTO updateEstado(Long id, String nuevoEstado) {
        ConsultaTramite e = repo.findById(id).orElseThrow(() -> new NotFoundException("Estado de trámite no encontrado"));
        validarEstado(nuevoEstado);
        e.setEstado(nuevoEstado);
        e = repo.save(e);
        return EstadoTramiteMapper.toDTO(e);
    }

    @Override
    public List<EstadoTramiteDTO> listAll() {
        return repo.findAll().stream().map(EstadoTramiteMapper::toDTO).collect(Collectors.toList());
    }

    private void validarEstado(String estado){
        if(estado==null) throw new BadRequestException("Estado requerido");
        switch (estado){
            case "INICIADO":
            case "PENDIENTE":
            case "TERMINADO": break;
            default: throw new BadRequestException("Estado inválido");
        }
    }
}
