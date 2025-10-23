// ConsultaTramiteRepository.java
package com.gestor.repository;

import com.gestor.entity.ConsultaTramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para ConsultaTramite
 * Extiende JpaSpecificationExecutor para CU-SAT011: Buscador Avanzado
 */
public interface ConsultaTramiteRepository extends JpaRepository<ConsultaTramite, Long>,
                                                    JpaSpecificationExecutor<ConsultaTramite> {
    Page<ConsultaTramite> findByCliente_IdCliente(Long idCliente, Pageable pageable);
    List<ConsultaTramite> findByEstado(String estado);

    /**
     * Query personalizado para CU-SAT005: Historial de Trámites
     * Obtiene el historial completo de trámites de un cliente con toda la información necesaria
     * Ordenado por fecha descendente (más recientes primero)
     */
    @Query("SELECT ct FROM ConsultaTramite ct " +
           "JOIN FETCH ct.tramite t " +
           "JOIN FETCH t.tipoTramite tt " +
           "WHERE ct.cliente.idCliente = :idCliente " +
           "ORDER BY ct.createdAt DESC")
    Page<ConsultaTramite> findHistorialByClienteId(@Param("idCliente") Long idCliente, Pageable pageable);

    // ===== CU-SAT012: Queries para Dashboard =====

    /**
     * Cuenta trámites por estado
     */
    long countByEstado(String estado);

    /**
     * Cuenta trámites creados en un rango de fechas
     */
    @Query("SELECT COUNT(ct) FROM ConsultaTramite ct WHERE ct.createdAt BETWEEN :inicio AND :fin")
    long countByCreatedAtBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    /**
     * Obtiene top N tipos de trámites más solicitados
     */
    @Query("SELECT t.tipoTramite.portal as tipoTramite, t.nombre as nombreTramite, COUNT(ct) as cantidad " +
           "FROM ConsultaTramite ct " +
           "JOIN ct.tramite t " +
           "GROUP BY t.tipoTramite.portal, t.nombre " +
           "ORDER BY cantidad DESC")
    List<Object[]> findTopTiposTramites(Pageable pageable);

    /**
     * Obtiene cantidad de trámites por mes
     * Formato: año-mes (ej: 2025-10)
     */
    @Query("SELECT FUNCTION('TO_CHAR', ct.createdAt, 'YYYY-MM') as mes, COUNT(ct) as cantidad " +
           "FROM ConsultaTramite ct " +
           "WHERE ct.createdAt >= :fechaInicio " +
           "GROUP BY FUNCTION('TO_CHAR', ct.createdAt, 'YYYY-MM') " +
           "ORDER BY mes DESC")
    List<Object[]> findTramitesPorMes(@Param("fechaInicio") LocalDateTime fechaInicio);
}
