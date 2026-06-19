package cl.duoc.bibliotecario_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.bibliotecario_service.dto.BibliotecarioRequest;
import cl.duoc.bibliotecario_service.dto.BibliotecarioResponse;
import cl.duoc.bibliotecario_service.mapper.BibliotecarioMapper;
import cl.duoc.bibliotecario_service.model.Bibliotecario;
import cl.duoc.bibliotecario_service.repository.BibliotecarioRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BibliotecarioService {
    @Autowired
    private BibliotecarioRepository repository;

    @Autowired
    private BibliotecarioMapper mapper;

    // Controller V1
    public List<BibliotecarioResponse> getAll() {
        log.info("Obteniendo bibliotecarios...");
        return repository.findAll().stream()
                .map(mapper::toResponse).toList();
    }

    public BibliotecarioResponse findById(Long id) {
        log.info("Buscando bibliotecario(a) con id: {}", id);
        Bibliotecario bibliotecario = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró bibliotecario(a) con ID: " + id));
        return mapper.toResponse(bibliotecario);
    }

    public BibliotecarioResponse crear(BibliotecarioRequest request){
        log.info("Creando bibliotecario(a): {}",request.getNombre());
        Bibliotecario bibliotecario = repository.save(mapper.toEntity(request));
        return mapper.toResponse(bibliotecario);
    }

    public BibliotecarioResponse actualizar(Long id, BibliotecarioRequest request){
        log.info("Actualizando bibliotecario(a): {}", request.getNombre());
        Bibliotecario bibliotecario = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("No se encontró bibliotecario(a) con id "+ id));

        bibliotecario.setNombre(request.getNombre());
        bibliotecario.setApellido(request.getApellido());
        bibliotecario.setEdad(request.getEdad());

        Bibliotecario bibliotecarioActualizado = repository.save(bibliotecario);
        return mapper.toResponse(bibliotecarioActualizado);
    }

    // Controller V2
    public List<Bibliotecario> getAllEntidad() {
        log.info("Obteniendo bibliotecarios...");
        return repository.findAll();
    }

    public Bibliotecario findByIdEntidad(Long id) {
        log.info("Buscando bibliotecario(a) con id: {}", id);
        Bibliotecario bibliotecario = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró bibliotecario(a) con ID: " + id));
        return bibliotecario;
    }

    public Bibliotecario crearEntidad (BibliotecarioRequest bibliotecario){
        log.info("Creando bibliotecario(a): {}",bibliotecario.getNombre());
        Bibliotecario nuevoBibliotecario = mapper.toEntity(bibliotecario);
        return repository.save(nuevoBibliotecario);
    }

    public Bibliotecario actualizarEntidad (Long id, BibliotecarioRequest request){
        log.info("Actualizando bibliotecario(a): {}", request.getNombre());
        Bibliotecario bibliotecario = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("No se encontró bibliotecario(a) con id "+ id));

        bibliotecario.setNombre(request.getNombre());
        bibliotecario.setApellido(request.getApellido());
        bibliotecario.setEdad(request.getEdad());

        Bibliotecario bibliotecarioActualizado = repository.save(bibliotecario);
        return bibliotecarioActualizado;
    }

    // Para ambos controladores
    public void eliminar (Long id){
        log.info("Eliminando bibliotecario(a) con id: {}", id);
        if (!repository.existsById(id)){
            throw new NoSuchElementException("Bibliotecario(a) con id" + id + " no encontrado(a).");
        }
        repository.deleteById(id);
    }
}