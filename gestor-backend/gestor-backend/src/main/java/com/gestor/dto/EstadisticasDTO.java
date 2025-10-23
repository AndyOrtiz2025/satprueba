// EstadisticasDTO.java
package com.gestor.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO para CU-SAT012: Panel de Control
 * Contiene todas las métricas y estadísticas del sistema
 */
public class EstadisticasDTO {

    // Totales generales
    private Long totalClientes;
    private Long totalTramites;
    private Long totalNotificacionesNoLeidas;

    // Trámites por estado
    private Long tramitesIniciados;
    private Long tramitesPendientes;
    private Long tramitesTerminados;

    // Trámites del mes actual
    private Long tramitesMesActual;

    // Clientes nuevos del mes
    private Long clientesNuevosMes;

    // Top 5 tipos de trámites más solicitados
    private List<TipoTramiteEstadistica> topTiposTramites;

    // Trámites por mes (últimos 6 meses)
    private List<TramitesPorMes> tramitesPorMes;

    // Distribución de trámites por estado (para gráficas)
    private Map<String, Long> distribucionPorEstado;

    // Constructor vacío
    public EstadisticasDTO() {}

    // Getters y Setters
    public Long getTotalClientes() { return totalClientes; }
    public void setTotalClientes(Long totalClientes) { this.totalClientes = totalClientes; }

    public Long getTotalTramites() { return totalTramites; }
    public void setTotalTramites(Long totalTramites) { this.totalTramites = totalTramites; }

    public Long getTotalNotificacionesNoLeidas() { return totalNotificacionesNoLeidas; }
    public void setTotalNotificacionesNoLeidas(Long totalNotificacionesNoLeidas) {
        this.totalNotificacionesNoLeidas = totalNotificacionesNoLeidas;
    }

    public Long getTramitesIniciados() { return tramitesIniciados; }
    public void setTramitesIniciados(Long tramitesIniciados) { this.tramitesIniciados = tramitesIniciados; }

    public Long getTramitesPendientes() { return tramitesPendientes; }
    public void setTramitesPendientes(Long tramitesPendientes) { this.tramitesPendientes = tramitesPendientes; }

    public Long getTramitesTerminados() { return tramitesTerminados; }
    public void setTramitesTerminados(Long tramitesTerminados) { this.tramitesTerminados = tramitesTerminados; }

    public Long getTramitesMesActual() { return tramitesMesActual; }
    public void setTramitesMesActual(Long tramitesMesActual) { this.tramitesMesActual = tramitesMesActual; }

    public Long getClientesNuevosMes() { return clientesNuevosMes; }
    public void setClientesNuevosMes(Long clientesNuevosMes) { this.clientesNuevosMes = clientesNuevosMes; }

    public List<TipoTramiteEstadistica> getTopTiposTramites() { return topTiposTramites; }
    public void setTopTiposTramites(List<TipoTramiteEstadistica> topTiposTramites) {
        this.topTiposTramites = topTiposTramites;
    }

    public List<TramitesPorMes> getTramitesPorMes() { return tramitesPorMes; }
    public void setTramitesPorMes(List<TramitesPorMes> tramitesPorMes) {
        this.tramitesPorMes = tramitesPorMes;
    }

    public Map<String, Long> getDistribucionPorEstado() { return distribucionPorEstado; }
    public void setDistribucionPorEstado(Map<String, Long> distribucionPorEstado) {
        this.distribucionPorEstado = distribucionPorEstado;
    }

    // Clase interna para estadísticas de tipos de trámites
    public static class TipoTramiteEstadistica {
        private String tipoTramite;
        private String nombreTramite;
        private Long cantidad;

        public TipoTramiteEstadistica() {}

        public TipoTramiteEstadistica(String tipoTramite, String nombreTramite, Long cantidad) {
            this.tipoTramite = tipoTramite;
            this.nombreTramite = nombreTramite;
            this.cantidad = cantidad;
        }

        public String getTipoTramite() { return tipoTramite; }
        public void setTipoTramite(String tipoTramite) { this.tipoTramite = tipoTramite; }

        public String getNombreTramite() { return nombreTramite; }
        public void setNombreTramite(String nombreTramite) { this.nombreTramite = nombreTramite; }

        public Long getCantidad() { return cantidad; }
        public void setCantidad(Long cantidad) { this.cantidad = cantidad; }
    }

    // Clase interna para trámites por mes
    public static class TramitesPorMes {
        private String mes; // Ej: "2025-10"
        private String mesNombre; // Ej: "Octubre 2025"
        private Long cantidad;

        public TramitesPorMes() {}

        public TramitesPorMes(String mes, String mesNombre, Long cantidad) {
            this.mes = mes;
            this.mesNombre = mesNombre;
            this.cantidad = cantidad;
        }

        public String getMes() { return mes; }
        public void setMes(String mes) { this.mes = mes; }

        public String getMesNombre() { return mesNombre; }
        public void setMesNombre(String mesNombre) { this.mesNombre = mesNombre; }

        public Long getCantidad() { return cantidad; }
        public void setCantidad(Long cantidad) { this.cantidad = cantidad; }
    }
}
