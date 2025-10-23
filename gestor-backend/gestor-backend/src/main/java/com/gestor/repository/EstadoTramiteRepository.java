// EstadoTramiteRepository.java
package com.gestor.repository;

import com.gestor.entity.EstadoTramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository para EstadoTramite
 * Extiende JpaSpecificationExecutor para CU-SAT011: Buscador Avanzado
 */
public interface EstadoTramiteRepository extends JpaRepository<EstadoTramite, Long>,
                                                  JpaSpecificationExecutor<EstadoTramite> {
}
