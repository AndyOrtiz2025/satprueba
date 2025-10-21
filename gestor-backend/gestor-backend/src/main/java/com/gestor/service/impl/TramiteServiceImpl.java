// impl/TramiteServiceImpl.java
package com.gestor.service.impl;

import com.gestor.dto.TramiteDTO;
import com.gestor.entity.*;
import com.gestor.exception.NotFoundException;
import com.gestor.mapper.TramiteMapper;
import com.gestor.repository.GrupoRepository;
import com.gestor.repository.TramiteRepository;
import com.gestor.service.TramiteService;
import com.gestor.util.PageResponse;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class TramiteServiceImpl implements TramiteService {

    private final TramiteRepository repo;
    private final GrupoRepository grupoRepo;

    public TramiteServiceImpl(TramiteRepository repo, GrupoRepository grupoRepo){
        this.repo = repo;
        this.grupoRepo = grupoRepo;
    }

    @Override
    public TramiteDTO create(TramiteDTO dto) {
        Grupo g = grupoRepo.findById(dto.getGrupoId())
                .orElseThrow(() -> new NotFoundException("Grupo no encontrado"));
        Tramite t = new Tramite();
        t.setNombre(dto.getNombre());
        t.setDescripcion(dto.getDescripcion());
        t.setRequisitos(dto.getRequisitos());
        t.setGrupo(g);
        t.setLink(dto.getLink());
        t = repo.save(t);
        return TramiteMapper.toDTO(t);
    }

    @Override
    public TramiteDTO update(Long id, TramiteDTO dto) {
        Tramite t = repo.findById(id).orElseThrow(() -> new NotFoundException("Trámite no encontrado"));
        if(dto.getGrupoId()!=null){
            Grupo g = grupoRepo.findById(dto.getGrupoId())
                    .orElseThrow(() -> new NotFoundException("Grupo no encontrado"));
            t.setGrupo(g);
        }
        t.setNombre(dto.getNombre());
        t.setDescripcion(dto.getDescripcion());
        t.setRequisitos(dto.getRequisitos());
        t.setLink(dto.getLink());
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
        Pageable p = PageRequest.of(Math.max(page,0), Math.max(size,1), Sort.by("id").descending());
        Page<Tramite> res = (q==null || q.isBlank()) ? repo.findAll(p) : repo.findByNombreContainingIgnoreCase(q, p);
        return new PageResponse<>(
                res.getContent().stream().map(TramiteMapper::toDTO).collect(Collectors.toList()),
                res.getTotalElements(), res.getTotalPages(), res.getNumber(), res.getSize()
        );
    }
}
