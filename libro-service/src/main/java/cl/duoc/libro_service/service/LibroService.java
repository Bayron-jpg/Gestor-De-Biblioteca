package cl.duoc.libro_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.libro_service.client.AutorClient;
import cl.duoc.libro_service.client.GeneroClient;
import cl.duoc.libro_service.dto.AutorResponse;
import cl.duoc.libro_service.dto.GeneroResponse;
import cl.duoc.libro_service.dto.LibroRequest;
import cl.duoc.libro_service.dto.LibroResponse;
import cl.duoc.libro_service.mapper.LibroMapper;
import cl.duoc.libro_service.model.Libro;
import cl.duoc.libro_service.repository.LibroRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LibroService {

    @Autowired
    private LibroRepository repository;

    @Autowired
    private AutorClient autorClient;

    @Autowired
    private GeneroClient generoClient;

    @Autowired
    private LibroMapper mapper;

    // Controller V1
    public List<LibroResponse> getAll() {
        log.info("Obteniendo libros...");
        return repository.findAll().stream()
                .map(libro -> {
                    AutorResponse autor = autorClient.buscarPorId(libro.getIdAutor());
                    GeneroResponse genero = generoClient.buscarPorId(libro.getIdGenero());
                    return mapper.toResponse(libro, autor, genero);
                }).toList();
    }

    public LibroResponse findById(Long id) {
        log.info("Buscando libro con id: {}", id);
        Libro libro = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró libro con ID: " + id));

        AutorResponse autor = autorClient.buscarPorId(libro.getIdAutor());
        GeneroResponse genero = generoClient.buscarPorId(libro.getIdGenero());
        return mapper.toResponse(libro, autor, genero);
    }

    public LibroResponse crear(LibroRequest request) {
        log.info("Creando libro: {}", request.getTitulo());

        AutorResponse autor = autorClient.buscarPorId(request.getIdAutor());
        if (autor == null) {
            log.warn("Autor con id: {} no encontrado", request.getIdAutor());
            throw new NoSuchElementException("Autor con id " + request.getIdAutor() + " no encontrado");
        }

        GeneroResponse genero = generoClient.buscarPorId(request.getIdGenero());
        if (genero == null) {
            log.warn("Genero con id: {} no encontrado", request.getIdGenero());
            throw new NoSuchElementException("Genero con id " + request.getIdGenero() + " no encontrado");
        }

        Libro libro = repository.save(mapper.toEntity(request));
        return mapper.toResponse(libro, autor, genero);
    }

    public LibroResponse actualizar(Long id, LibroRequest request) {
        log.info("Actualizando libro: {}", request.getTitulo());

        Libro libro = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró libro con id " + id));

        AutorResponse autor = autorClient.buscarPorId(request.getIdAutor());
        if (autor == null) {
            log.warn("Autor con id: {} no encontrado", request.getIdAutor());
            throw new NoSuchElementException("Autor con id " + request.getIdAutor() + " no encontrado");
        }

        GeneroResponse genero = generoClient.buscarPorId(request.getIdGenero());
        if (genero == null) {
            log.warn("Genero con id: {} no encontrado", request.getIdGenero());
            throw new NoSuchElementException("Genero con id " + request.getIdGenero() + " no encontrado");
        }

        libro.setTitulo(request.getTitulo());
        libro.setIsbn(request.getIsbn());
        libro.setFechaPublicacion(request.getFechaPublicacion());
        libro.setIdAutor(request.getIdAutor());
        libro.setIdGenero(request.getIdGenero());

        Libro libroActualizado = repository.save(libro);
        return mapper.toResponse(libroActualizado, autor, genero);
    }

    // Controller V2
    public List<Libro> getAllEntidad() {
        log.info("Obteniendo libros...");
        return repository.findAll();
    }

    public Libro findByIdEntidad(Long id) {
        log.info("Buscando libro con id: {}", id);
        Libro libro = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró libro con ID: " + id));
        return libro;
    }

    public Libro crearEntidad(LibroRequest request) {
        log.info("Creando libro: {}", request.getTitulo());

        AutorResponse autor = autorClient.buscarPorId(request.getIdAutor());
        if (autor == null) {
            throw new NoSuchElementException("Autor con id " + request.getIdAutor() + " no encontrado");
        }

        GeneroResponse genero = generoClient.buscarPorId(request.getIdGenero());
        if (genero == null) {
            throw new NoSuchElementException("Genero con id " + request.getIdGenero() + " no encontrado");
        }

        Libro nuevoLibro = mapper.toEntity(request);
        return repository.save(nuevoLibro);
    }

    public Libro actualizarEntidad(Long id, LibroRequest request) {
        log.info("Actualizando libro: {}", request.getTitulo());

        Libro libro = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró libro con id " + id));

        AutorResponse autor = autorClient.buscarPorId(request.getIdAutor());
        if (autor == null) {
            throw new NoSuchElementException("Autor con id " + request.getIdAutor() + " no encontrado");
        }

        GeneroResponse genero = generoClient.buscarPorId(request.getIdGenero());
        if (genero == null) {
            throw new NoSuchElementException("Genero con id " + request.getIdGenero() + " no encontrado");
        }

        libro.setTitulo(request.getTitulo());
        libro.setIsbn(request.getIsbn());
        libro.setFechaPublicacion(request.getFechaPublicacion());
        libro.setIdAutor(request.getIdAutor());
        libro.setIdGenero(request.getIdGenero());

        return repository.save(libro);
    }

    // Para ambos controladores
    public void eliminar(Long id) {
        log.info("Eliminando libro con id: {}", id);
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Libro con id " + id + " no encontrado.");
        }
        repository.deleteById(id);
    }
}