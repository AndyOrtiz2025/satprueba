// BusquedaPaginadaResponseDTO.java
package com.gestor.dto;

import java.util.List;

/**
 * DTO para CU-SAT011: Respuesta paginada del buscador avanzado
 */
public class BusquedaPaginadaResponseDTO {

    private List<BusquedaResultadoDTO> resultados;
    private int paginaActual;
    private int tamanioPagina;
    private long totalElementos;
    private int totalPaginas;
    private boolean esUltimaPagina;

    // Constructores
    public BusquedaPaginadaResponseDTO() {}

    public BusquedaPaginadaResponseDTO(List<BusquedaResultadoDTO> resultados, int paginaActual,
                                       int tamanioPagina, long totalElementos, int totalPaginas) {
        this.resultados = resultados;
        this.paginaActual = paginaActual;
        this.tamanioPagina = tamanioPagina;
        this.totalElementos = totalElementos;
        this.totalPaginas = totalPaginas;
        this.esUltimaPagina = paginaActual >= totalPaginas - 1;
    }

    // Getters y Setters
    public List<BusquedaResultadoDTO> getResultados() { return resultados; }
    public void setResultados(List<BusquedaResultadoDTO> resultados) { this.resultados = resultados; }

    public int getPaginaActual() { return paginaActual; }
    public void setPaginaActual(int paginaActual) { this.paginaActual = paginaActual; }

    public int getTamanioPagina() { return tamanioPagina; }
    public void setTamanioPagina(int tamanioPagina) { this.tamanioPagina = tamanioPagina; }

    public long getTotalElementos() { return totalElementos; }
    public void setTotalElementos(long totalElementos) { this.totalElementos = totalElementos; }

    public int getTotalPaginas() { return totalPaginas; }
    public void setTotalPaginas(int totalPaginas) { this.totalPaginas = totalPaginas; }

    public boolean isEsUltimaPagina() { return esUltimaPagina; }
    public void setEsUltimaPagina(boolean esUltimaPagina) { this.esUltimaPagina = esUltimaPagina; }
}
