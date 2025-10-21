// GrupoRepository.java
package com.gestor.repository;

import com.gestor.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    boolean existsByNombreIgnoreCase(String nombre);

    // para UPDATE: mismo nombre en otro id
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}

