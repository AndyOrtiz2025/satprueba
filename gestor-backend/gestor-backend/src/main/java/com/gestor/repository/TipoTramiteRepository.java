// TipoTramiteRepository.java
package com.gestor.repository;

import com.gestor.entity.TipoTramite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TipoTramiteRepository extends JpaRepository<TipoTramite, Long> {
    Optional<TipoTramite> findByPortal(String portal);
    boolean existsByPortalIgnoreCase(String portal);
    boolean existsByPortalIgnoreCaseAndIdTipoTramiteNot(String portal, Long idTipoTramite);
}
