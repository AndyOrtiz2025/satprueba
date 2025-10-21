// ClienteRepository.java
package com.gestor.repository;

import com.gestor.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Page<Cliente> findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(String n, String a, Pageable p);
}
