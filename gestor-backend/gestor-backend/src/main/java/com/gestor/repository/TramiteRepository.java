// TramiteRepository.java
package com.gestor.repository;

import com.gestor.entity.Tramite;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TramiteRepository extends JpaRepository<Tramite, Long> {
    Page<Tramite> findByNombreContainingIgnoreCase(String q, Pageable p);

    // repository/TramiteRepository.java
long countByGrupo_Id(Long grupoId);

}
