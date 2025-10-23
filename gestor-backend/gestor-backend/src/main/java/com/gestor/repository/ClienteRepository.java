// ClienteRepository.java
package com.gestor.repository;

import com.gestor.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Page<Cliente> findByNombreCompletoContainingIgnoreCase(String nombreCompleto, Pageable p);
    Optional<Cliente> findByDpi(String dpi);
    boolean existsByDpi(String dpi);
    boolean existsByUsuario_IdUsuario(Long idUsuario);

    // ===== CU-SAT012: Queries para Dashboard =====

    /**
     * Cuenta clientes creados en un rango de fechas
     */
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.createdAt BETWEEN :inicio AND :fin")
    long countByCreatedAtBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
