// DatosRepository.java
package com.gestor.repository;

import com.gestor.entity.Datos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DatosRepository extends JpaRepository<Datos, Long> {
    Optional<Datos> findByCliente_IdCliente(Long idCliente);
    Optional<Datos> findByNit(String nit);
}
