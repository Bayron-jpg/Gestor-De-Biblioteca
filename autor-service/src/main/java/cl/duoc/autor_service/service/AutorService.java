package cl.duoc.autor_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.autor_service.dto.AutorRequest;
import cl.duoc.autor_service.dto.AutorResponse;
import cl.duoc.autor_service.mapper.AutorMapper;
import cl.duoc.autor_service.model.Autor;
import cl.duoc.autor_service.repository.AutorRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AutorService {
    @Autowired
    private AutorRepository repository;

    @Autowired
    private AutorMapper mapper;

    // Controller V1
    public List<AutorResponse> getAll() {
        log.info("Obteniendo autores...");
        return repository.findAll().stream()
                .map(mapper::toResponse).toList();
    }

    public AutorResponse findById(Long id) {
        log.info("Buscando autor con id: {}", id);
        Autor autor = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró autor con ID: " + id));
        return mapper.toResponse(autor);
    }

    public AutorResponse crear(AutorRequest request){
        log.info("Creando autor(a): {}",request.getNombre());
        Autor autor = repository.save(mapper.toEntity(request));
        return mapper.toResponse(autor);
    }

    public AutorResponse actualizar(Long id, AutorRequest request){
        log.info("Actualizando autor(a) {}", request.getNombre());
        Autor autor = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("No se encontró autor(a) con id "+ id));

        autor.setNombre(request.getNombre());
        autor.setFechaNacimiento(request.getFechaNacimiento());

        Autor autorActualizado = repository.save(autor);
        return mapper.toResponse(autorActualizado);
    }

    // Controller V2
    public List<Autor> getAllEntidad() {
        log.info("Obteniendo autores...");
        return repository.findAll();
    }

    public Autor findByIdEntidad(Long id) {
        log.info("Buscando autor(a) con id: {}", id);
        Autor autor = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró autor(a) con ID: " + id));
        return autor;
    }

    public Autor crearEntidad (AutorRequest autor){
        log.info("Creando autor(a): {}",autor.getNombre());
        Autor nuevoAutor = mapper.toEntity(autor);
        return repository.save(nuevoAutor);
    }


    public Autor actualizarEntidad (Long id, AutorRequest request){
        log.info("Actualizando autor(a): {}", request.getNombre());
        Autor autor = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("No se encontró autor(a) con id "+ id));

        autor.setNombre(request.getNombre());
        autor.setFechaNacimiento(request.getFechaNacimiento());

        Autor autorActualizado = repository.save(autor);
        return autorActualizado;
    }

    // Para ambos controladores
    public void eliminar (Long id){
        log.info("Eliminando autor(a) con id: {}", id);
        if (!repository.existsById(id)){
            throw new NoSuchElementException("Autor(a) con id" + id + " no encontrado(a).");
        }
        repository.deleteById(id);
    }
}