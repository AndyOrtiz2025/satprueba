// impl/ClienteServiceImpl.java
package com.gestor.service.impl;

import com.gestor.dto.ClienteDTO;
import com.gestor.entity.Cliente;
import com.gestor.exception.NotFoundException;
import com.gestor.mapper.ClienteMapper;
import com.gestor.repository.ClienteRepository;
import com.gestor.service.ClienteService;
import com.gestor.util.PageResponse;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repo;

    public ClienteServiceImpl(ClienteRepository repo){
        this.repo = repo;
    }

    @Override
    public ClienteDTO create(ClienteDTO dto) {
        Cliente e = ClienteMapper.toEntity(dto);
        e.setId(null);
        e = repo.save(e);
        return ClienteMapper.toDTO(e);
    }

    @Override
    public ClienteDTO update(Long id, ClienteDTO dto) {
        Cliente e = repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        // actualizar campos
        ClienteMapper.merge(dto, e); 
        e = repo.save(e);
        e.setNombre(dto.getNombre());
        e.setApellidos(dto.getApellidos());
        e.setTelefono(dto.getTelefono());
        e.setDireccion(dto.getDireccion());
        e.setNis(dto.getNis());
       
        e.setDpi(dto.getDpi());
        e.setNit(dto.getNit());
        e.setCuentaBancaria(dto.getCuentaBancaria());
        e.setPassNit(dto.getPassNit());
        e.setPassCgc(dto.getPassCgc());
        e.setPassReghae(dto.getPassReghae());
        e.setPassGeneral(dto.getPassGeneral());
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
        Pageable p = PageRequest.of(Math.max(page,0), Math.max(size,1), Sort.by("id").descending());
        Page<Cliente> res;
        if(q==null || q.isBlank()){
            res = repo.findAll(p);
        } else {
            res = repo.findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(q, q, p);
        }
        return new PageResponse<>(
                res.getContent().stream().map(ClienteMapper::toDTO).collect(Collectors.toList()),
                res.getTotalElements(), res.getTotalPages(), res.getNumber(), res.getSize()
        );
    }
}
