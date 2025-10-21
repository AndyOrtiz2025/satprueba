package com.gestor.service.impl;

import com.gestor.dto.GrupoDTO;
import com.gestor.entity.Grupo;
import com.gestor.exception.BadRequestException;
import com.gestor.exception.NotFoundException;
import com.gestor.mapper.GrupoMapper;
import com.gestor.repository.GrupoRepository;
import com.gestor.service.GrupoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrupoServiceImpl implements GrupoService {
    private final GrupoRepository repo;
    public GrupoServiceImpl(GrupoRepository repo){ this.repo = repo; }

    @Override
    @Transactional
    public GrupoDTO create(GrupoDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank())
            throw new BadRequestException("El nombre del grupo es obligatorio");
        if (repo.existsByNombreIgnoreCase(dto.getNombre()))
            throw new BadRequestException("Ya existe un grupo con ese nombre");

        Grupo g = new Grupo();
        g.setNombre(dto.getNombre().trim());
        g.setLink(dto.getLink());
        g = repo.save(g);
        return GrupoMapper.toDTO(g);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoDTO> listAll() {
        return repo.findAll().stream().map(GrupoMapper::toDTO).collect(Collectors.toList());
    }

    // ====== NUEVOS ======

    @Override
    @Transactional(readOnly = true)
    public GrupoDTO getById(Long id) {
        Grupo g = repo.findById(id).orElseThrow(() -> new NotFoundException("Grupo no encontrado"));
        return GrupoMapper.toDTO(g);
    }

    @Override
    @Transactional
    public GrupoDTO update(Long id, GrupoDTO dto) {
        Grupo g = repo.findById(id).orElseThrow(() -> new NotFoundException("Grupo no encontrado"));

        String nuevoNombre = dto.getNombre() == null ? "" : dto.getNombre().trim();
        if (nuevoNombre.isBlank())
            throw new BadRequestException("El nombre del grupo es obligatorio");

        if (repo.existsByNombreIgnoreCaseAndIdNot(nuevoNombre, id))
            throw new BadRequestException("Ya existe otro grupo con ese nombre");

        g.setNombre(nuevoNombre);
        g.setLink(dto.getLink());
        g = repo.save(g);
        return GrupoMapper.toDTO(g);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new NotFoundException("Grupo no encontrado");
        repo.deleteById(id);
    }
}
