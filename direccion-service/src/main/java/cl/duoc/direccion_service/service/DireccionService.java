package cl.duoc.direccion_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.direccion_service.dto.DireccionRequest;
import cl.duoc.direccion_service.dto.DireccionResponse;
import cl.duoc.direccion_service.mapper.DireccionMapper;
import cl.duoc.direccion_service.model.Direccion;
import cl.duoc.direccion_service.repository.DireccionRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DireccionService {
    @Autowired
    private DireccionRepository repository;

    @Autowired
    private DireccionMapper mapper;

    // Controller V1
    public List<DireccionResponse> getAll() {
        log.info("Obteniendo direcciones...");
        return repository.findAll().stream()
                .map(mapper::toResponse).toList();
    }

    public DireccionResponse findById(Long id) {
        log.info("Buscando direccion con id: {}", id);
        Direccion direccion = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró direccion con ID: " + id));
        return mapper.toResponse(direccion);
    }

    public DireccionResponse crear(DireccionRequest request) {
        log.info("Creando direccion: {}", request.getDireccion());
        Direccion direccion = repository.save(mapper.toEntity(request));
        return mapper.toResponse(direccion);
    }

    public DireccionResponse actualizar(Long id, DireccionRequest request) {
        log.info("Actualizando direccion: {}", request.getDireccion());
        Direccion direccion = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró direccion con id " + id));

        direccion.setRegion(request.getRegion());
        direccion.setComuna(request.getComuna());
        direccion.setDireccion(request.getDireccion());

        Direccion direccionActualizada = repository.save(direccion);
        return mapper.toResponse(direccionActualizada);
    }

    // Controller V2
    public List<Direccion> getAllEntidad() {
        log.info("Obteniendo direcciones...");
        return repository.findAll();
    }

    public Direccion findByIdEntidad(Long id) {
        log.info("Buscando direccion con id: {}", id);
        Direccion direccion = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró direccion con ID: " + id));
        return direccion;
    }

    public Direccion crearEntidad(DireccionRequest request) {
        log.info("Creando direccion: {}", request.getDireccion());
        Direccion nuevaDireccion = mapper.toEntity(request);
        return repository.save(nuevaDireccion);
    }

    public Direccion actualizarEntidad(Long id, DireccionRequest request) {
        log.info("Actualizando direccion: {}", request.getDireccion());
        Direccion direccion = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró direccion con id " + id));

        direccion.setRegion(request.getRegion());
        direccion.setComuna(request.getComuna());
        direccion.setDireccion(request.getDireccion());

        Direccion direccionActualizada = repository.save(direccion);
        return direccionActualizada;
    }

    // Para ambos controladores
    public void eliminar(Long id) {
        log.info("Eliminando direccion con id: {}", id);
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Direccion con id " + id + " no encontrada.");
        }
        repository.deleteById(id);
    }
}