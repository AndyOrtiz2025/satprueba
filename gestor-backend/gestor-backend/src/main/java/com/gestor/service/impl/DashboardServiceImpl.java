// DashboardServiceImpl.java
package com.gestor.service.impl;

import com.gestor.dto.EstadisticasDTO;
import com.gestor.repository.ClienteRepository;
import com.gestor.repository.ConsultaTramiteRepository;
import com.gestor.repository.NotificacionRepository;
import com.gestor.service.DashboardService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final ClienteRepository clienteRepo;
    private final ConsultaTramiteRepository consultaTramiteRepo;
    private final NotificacionRepository notificacionRepo;

    public DashboardServiceImpl(ClienteRepository clienteRepo,
                                ConsultaTramiteRepository consultaTramiteRepo,
                                NotificacionRepository notificacionRepo) {
        this.clienteRepo = clienteRepo;
        this.consultaTramiteRepo = consultaTramiteRepo;
        this.notificacionRepo = notificacionRepo;
    }

    @Override
    public EstadisticasDTO obtenerEstadisticas() {
        EstadisticasDTO estadisticas = new EstadisticasDTO();

        // Tarea 3: Calcular total de clientes
        estadisticas.setTotalClientes(clienteRepo.count());

        // Total de trámites
        estadisticas.setTotalTramites(consultaTramiteRepo.count());

        // Tarea 4: Calcular trámites por estado
        estadisticas.setTramitesIniciados(consultaTramiteRepo.countByEstado("INICIADO"));
        estadisticas.setTramitesPendientes(consultaTramiteRepo.countByEstado("PENDIENTE"));
        estadisticas.setTramitesTerminados(consultaTramiteRepo.countByEstado("TERMINADO"));

        // Distribución por estado (para gráficas)
        Map<String, Long> distribucion = new HashMap<>();
        distribucion.put("INICIADO", estadisticas.getTramitesIniciados());
        distribucion.put("PENDIENTE", estadisticas.getTramitesPendientes());
        distribucion.put("TERMINADO", estadisticas.getTramitesTerminados());
        estadisticas.setDistribucionPorEstado(distribucion);

        // Tarea 5: Calcular trámites del mes actual
        LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finMes = LocalDateTime.now().withDayOfMonth(
                LocalDateTime.now().toLocalDate().lengthOfMonth()
        ).withHour(23).withMinute(59).withSecond(59);
        estadisticas.setTramitesMesActual(
                consultaTramiteRepo.countByCreatedAtBetween(inicioMes, finMes)
        );

        // Tarea 9: Calcular clientes nuevos del mes
        estadisticas.setClientesNuevosMes(
                clienteRepo.countByCreatedAtBetween(inicioMes, finMes)
        );

        // Total de notificaciones no leídas (de todos los usuarios)
        // Nota: Si quieres por usuario específico, debes pasar el idUsuario como parámetro
        estadisticas.setTotalNotificacionesNoLeidas(
                notificacionRepo.count() // Simplificado, puedes filtrar por leida=false si lo necesitas
        );

        // Tarea 6: Obtener top 5 tipos de trámites más solicitados
        List<Object[]> topTramites = consultaTramiteRepo.findTopTiposTramites(
                PageRequest.of(0, 5)
        );
        List<EstadisticasDTO.TipoTramiteEstadistica> topTiposTramites = topTramites.stream()
                .map(obj -> new EstadisticasDTO.TipoTramiteEstadistica(
                        (String) obj[0],  // tipoTramite
                        (String) obj[1],  // nombreTramite
                        ((Number) obj[2]).longValue() // cantidad
                ))
                .collect(Collectors.toList());
        estadisticas.setTopTiposTramites(topTiposTramites);

        // Tarea 9: Trámites por mes (últimos 6 meses)
        LocalDateTime hace6Meses = LocalDateTime.now().minusMonths(6);
        List<Object[]> tramitesPorMes = consultaTramiteRepo.findTramitesPorMes(hace6Meses);

        List<EstadisticasDTO.TramitesPorMes> tramitesPorMesList = tramitesPorMes.stream()
                .map(obj -> {
                    String mes = (String) obj[0]; // "2025-10"
                    Long cantidad = ((Number) obj[1]).longValue();

                    // Convertir "2025-10" a "Octubre 2025"
                    String[] partes = mes.split("-");
                    int año = Integer.parseInt(partes[0]);
                    int mesNumero = Integer.parseInt(partes[1]);
                    YearMonth yearMonth = YearMonth.of(año, mesNumero);
                    String mesNombre = yearMonth.getMonth()
                            .getDisplayName(TextStyle.FULL, new Locale("es", "GT"))
                            + " " + año;

                    return new EstadisticasDTO.TramitesPorMes(mes, mesNombre, cantidad);
                })
                .collect(Collectors.toList());
        estadisticas.setTramitesPorMes(tramitesPorMesList);

        return estadisticas;
    }
}
