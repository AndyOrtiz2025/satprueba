// ConsultaTramiteSpecification.java
package com.gestor.specification;

import com.gestor.dto.BusquedaAvanzadaDTO;
import com.gestor.entity.Cliente;
import com.gestor.entity.ConsultaTramite;
import com.gestor.entity.Tramite;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Specification para CU-SAT011: Buscador Avanzado
 * Construye consultas dinámicas para ConsultaTramite
 */
public class ConsultaTramiteSpecification {

    /**
     * Construye una Specification dinámica basada en los filtros del DTO
     */
    public static Specification<ConsultaTramite> conFiltros(BusquedaAvanzadaDTO filtros) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Join con Cliente (para buscar por nombre y DPI)
            Join<ConsultaTramite, Cliente> clienteJoin = root.join("cliente");

            // Join con Tramite (para incluir información del trámite)
            Join<ConsultaTramite, Tramite> tramiteJoin = root.join("tramite");

            // Filtro por nombre del cliente (búsqueda parcial, case-insensitive)
            if (filtros.getNombre() != null && !filtros.getNombre().isBlank()) {
                String nombrePattern = "%" + filtros.getNombre().toLowerCase() + "%";
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(clienteJoin.get("nombreCompleto")),
                        nombrePattern
                    )
                );
            }

            // Filtro por DPI (búsqueda exacta)
            if (filtros.getDpi() != null && !filtros.getDpi().isBlank()) {
                predicates.add(
                    criteriaBuilder.equal(clienteJoin.get("dpi"), filtros.getDpi())
                );
            }

            // Filtro por rango de fechas (fecha_tramite)
            if (filtros.getFechaInicio() != null) {
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("fechaTramite"),
                        filtros.getFechaInicio()
                    )
                );
            }

            if (filtros.getFechaFin() != null) {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("fechaTramite"),
                        filtros.getFechaFin()
                    )
                );
            }

            // Filtro por estado (búsqueda exacta, case-insensitive)
            // Estados válidos: INICIADO, PENDIENTE, TERMINADO
            if (filtros.getEstado() != null && !filtros.getEstado().isBlank()) {
                predicates.add(
                    criteriaBuilder.equal(
                        criteriaBuilder.upper(root.get("estado")),
                        filtros.getEstado().toUpperCase()
                    )
                );
            }

            // Combinar todos los predicados con AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Specification para filtrar por nombre de cliente
     */
    public static Specification<ConsultaTramite> conNombreCliente(String nombre) {
        return (root, query, criteriaBuilder) -> {
            if (nombre == null || nombre.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            Join<ConsultaTramite, Cliente> clienteJoin = root.join("cliente");
            String nombrePattern = "%" + nombre.toLowerCase() + "%";
            return criteriaBuilder.like(
                criteriaBuilder.lower(clienteJoin.get("nombreCompleto")),
                nombrePattern
            );
        };
    }

    /**
     * Specification para filtrar por DPI
     */
    public static Specification<ConsultaTramite> conDpi(String dpi) {
        return (root, query, criteriaBuilder) -> {
            if (dpi == null || dpi.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            Join<ConsultaTramite, Cliente> clienteJoin = root.join("cliente");
            return criteriaBuilder.equal(clienteJoin.get("dpi"), dpi);
        };
    }

    /**
     * Specification para filtrar por rango de fechas
     */
    public static Specification<ConsultaTramite> entreFechas(LocalDate inicio, LocalDate fin) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (inicio != null) {
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("fechaTramite"), inicio)
                );
            }

            if (fin != null) {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(root.get("fechaTramite"), fin)
                );
            }

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Specification para filtrar por estado
     * Estados válidos: INICIADO, PENDIENTE, TERMINADO
     */
    public static Specification<ConsultaTramite> conEstado(String estado) {
        return (root, query, criteriaBuilder) -> {
            if (estado == null || estado.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(
                criteriaBuilder.upper(root.get("estado")),
                estado.toUpperCase()
            );
        };
    }
}
