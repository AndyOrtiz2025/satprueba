package com.gestor.service;

import com.gestor.dto.ArchivoDTO;
import com.gestor.entity.Archivos;
import com.gestor.entity.Cliente;
import com.gestor.repository.ArchivosRepository;
import com.gestor.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArchivosService {

    private final ArchivosRepository archivosRepository;
    private final ClienteRepository clienteRepository;

    // Directorio donde se guardarán los archivos (puedes configurarlo en application.properties)
    @Value("${app.archivos.path:uploads}")
    private String uploadDir;

    public ArchivosService(ArchivosRepository archivosRepository, ClienteRepository clienteRepository) {
        this.archivosRepository = archivosRepository;
        this.clienteRepository = clienteRepository;
    }

    /**
     * Subir archivo para un cliente
     */
    public ArchivoDTO subirArchivo(Long idCliente, MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }

        // Validar cliente existe
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        try {
            // Crear directorio si no existe
            Path uploadPath = Paths.get(uploadDir, "cliente_" + idCliente);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generar nombre único para el archivo
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String nuevoNombre = UUID.randomUUID().toString() + extension;
            Path filePath = uploadPath.resolve(nuevoNombre);

            // Guardar archivo en disco
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Guardar registro en BD
            Archivos archivo = new Archivos();
            archivo.setCliente(cliente);
            archivo.setNombreArchivo(originalFilename);
            archivo.setRuta(filePath.toString());
            archivo.setFechaSubida(LocalDateTime.now());
            archivo.setCreatedBy("Sistema"); // TODO: Obtener usuario actual

            archivo = archivosRepository.save(archivo);

            // Convertir a DTO
            return convertirADTO(archivo, file.getSize(), file.getContentType());

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage());
        }
    }

    /**
     * Listar archivos de un cliente
     */
    public List<ArchivoDTO> listarArchivosPorCliente(Long idCliente) {
        List<Archivos> archivos = archivosRepository.findByCliente_IdCliente(idCliente);

        return archivos.stream()
                .map(archivo -> {
                    try {
                        Path path = Paths.get(archivo.getRuta());
                        long tamanio = Files.exists(path) ? Files.size(path) : 0;
                        String contentType = Files.probeContentType(path);
                        return convertirADTO(archivo, tamanio, contentType);
                    } catch (IOException e) {
                        return convertirADTO(archivo, 0L, "application/octet-stream");
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Obtener archivo para descarga
     */
    public Resource cargarArchivo(Long idArchivo) {
        Archivos archivo = archivosRepository.findById(idArchivo)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));

        try {
            Path filePath = Paths.get(archivo.getRuta());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se pudo leer el archivo");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar el archivo: " + e.getMessage());
        }
    }

    /**
     * Obtener información del archivo
     */
    public Archivos obtenerArchivo(Long idArchivo) {
        return archivosRepository.findById(idArchivo)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));
    }

    /**
     * Eliminar archivo
     */
    public void eliminarArchivo(Long idArchivo) {
        Archivos archivo = archivosRepository.findById(idArchivo)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));

        try {
            // Eliminar archivo físico
            Path filePath = Paths.get(archivo.getRuta());
            Files.deleteIfExists(filePath);

            // Eliminar registro de BD
            archivosRepository.delete(archivo);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo: " + e.getMessage());
        }
    }

    /**
     * Convertir entidad a DTO
     */
    private ArchivoDTO convertirADTO(Archivos archivo, Long tamanio, String tipoContenido) {
        ArchivoDTO dto = new ArchivoDTO();
        dto.setIdArchivos(archivo.getIdArchivos());
        dto.setIdCliente(archivo.getCliente().getIdCliente());
        dto.setIdTramite(archivo.getTramite() != null ? archivo.getTramite().getIdTramites() : null);
        dto.setNombreArchivo(archivo.getNombreArchivo());
        dto.setRuta(archivo.getRuta());
        dto.setFechaSubida(archivo.getFechaSubida());
        dto.setTamanio(tamanio);
        dto.setTipoContenido(tipoContenido);
        return dto;
    }
}
