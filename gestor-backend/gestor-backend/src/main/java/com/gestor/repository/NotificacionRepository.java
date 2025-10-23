// NotificacionRepository.java
package com.gestor.repository;

import com.gestor.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    Page<Notificacion> findByUsuario_IdUsuarioAndExpiradaFalse(Long idUsuario, Pageable pageable);
    List<Notificacion> findByUsuario_IdUsuarioAndLeidaFalseAndExpiradaFalse(Long idUsuario);
    long countByUsuario_IdUsuarioAndLeidaFalseAndExpiradaFalse(Long idUsuario);
}
