// BitacoraRepository.java
package com.gestor.repository;

import com.gestor.entity.Bitacora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {
    Page<Bitacora> findByUsuario(String usuario, Pageable pageable);
    Page<Bitacora> findByAccion(String accion, Pageable pageable);
    List<Bitacora> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
