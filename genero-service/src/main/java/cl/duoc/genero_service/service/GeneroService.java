package cl.duoc.genero_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.genero_service.dto.GeneroRequest;
import cl.duoc.genero_service.dto.GeneroResponse;
import cl.duoc.genero_service.mapper.GeneroMapper;
import cl.duoc.genero_service.model.Genero;
import cl.duoc.genero_service.repository.GeneroRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GeneroService {
    @Autowired
    private GeneroRepository repository;

    @Autowired
    private GeneroMapper mapper;

    // Controller V1
    public List<GeneroResponse> getAll() {
        log.info("Obteniendo generos...");
        return repository.findAll().stream()
                .map(mapper::toResponse).toList();
    }

    public GeneroResponse findById(Long id) {
        log.info("Buscando genero con id: {}", id);
        Genero genero = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro el genero con ID: " + id));
        return mapper.toResponse(genero);
    }

    public GeneroResponse crear(GeneroRequest request) {
        log.info("Creando genero: {}", request.getNombre());
        Genero genero = repository.save(mapper.toEntity(request));
        return mapper.toResponse(genero);
    }

    public GeneroResponse actualizar(Long id, GeneroRequest request) {
        log.info("Actualizando genero: {}", request.getNombre());
        Genero genero = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro el genero con id " + id));

        genero.setNombre(request.getNombre());
        genero.setDescripcion(request.getDescripcion());

        Genero generoActualizado = repository.save(genero);
        return mapper.toResponse(generoActualizado);
    }

    // Controller V2
    public List<Genero> getAllEntidad() {
        log.info("Obteniendo generos...");
        return repository.findAll();
    }

    public Genero findByIdEntidad(Long id) {
        log.info("Buscando el genero con id: {}", id);
        Genero genero = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro el genero con ID: " + id));
        return genero;
    }

    public Genero crearEntidad(GeneroRequest genero) {
        log.info("Creando el genero: {}", genero.getNombre());
        Genero nuevoGenero = mapper.toEntity(genero);
        return repository.save(nuevoGenero);
    }

    public Genero actualizarEntidad(Long id, GeneroRequest request) {
        log.info("Actualizando el genero: {}", request.getNombre());
        Genero genero = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro el genero con id " + id));

        genero.setNombre(request.getNombre());
        genero.setDescripcion(request.getDescripcion());

        Genero generoActualizado = repository.save(genero);
        return generoActualizado;
    }

    // Para ambos controladores
    public void eliminar(Long id) {
        log.info("Eliminando genero con id: {}", id);
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Genero con id" + id + " no encontrado.");
        }
        repository.deleteById(id);
    }
}