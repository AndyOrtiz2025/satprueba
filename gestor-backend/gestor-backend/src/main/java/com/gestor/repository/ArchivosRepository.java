// ArchivosRepository.java
package com.gestor.repository;

import com.gestor.entity.Archivos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ArchivosRepository extends JpaRepository<Archivos, Long> {
    List<Archivos> findByCliente_IdCliente(Long idCliente);
    List<Archivos> findByTramite_IdTramites(Long idTramites);
    Page<Archivos> findByCliente_IdClienteAndTramite_IdTramites(Long idCliente, Long idTramites, Pageable pageable);
}
