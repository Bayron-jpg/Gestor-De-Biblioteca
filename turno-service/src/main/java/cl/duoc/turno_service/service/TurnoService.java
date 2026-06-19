package cl.duoc.turno_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.turno_service.client.BibliotecarioClient;
import cl.duoc.turno_service.dto.BibliotecarioResponse;
import cl.duoc.turno_service.dto.TurnoRequest;
import cl.duoc.turno_service.dto.TurnoResponse;
import cl.duoc.turno_service.mapper.TurnoMapper;
import cl.duoc.turno_service.model.Turno;
import cl.duoc.turno_service.repository.TurnoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TurnoService {

    @Autowired
    private TurnoRepository repository;

    @Autowired
    private BibliotecarioClient bibliotecarioClient;

    @Autowired
    private TurnoMapper mapper;

    // Controller V1
    public List<TurnoResponse> getAll() {
        log.info("Obteniendo todos los turnos...");
        return repository.findAll().stream()
                .map(turno -> {
                    BibliotecarioResponse bibliotecario = bibliotecarioClient.buscarPorId(turno.getIdBibliotecario());
                    return mapper.toResponse(turno, bibliotecario);
                }).toList();
    }

    public TurnoResponse findById(Long id) {
        log.info("Buscando turno con id: {}", id);
        Turno turno = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el turno con ID: " + id));

        BibliotecarioResponse bibliotecario = bibliotecarioClient.buscarPorId(turno.getIdBibliotecario());

        return mapper.toResponse(turno, bibliotecario);
    }

    public TurnoResponse crear(TurnoRequest request) {
        log.info("Creando turno para el bibliotecario(a) con ID: {}", request.getIdBibliotecario());

        BibliotecarioResponse bibliotecario = bibliotecarioClient.buscarPorId(request.getIdBibliotecario());
        if (bibliotecario == null) {
            log.warn("Bibliotecario(a) con id: {} no encontrado(a)", request.getIdBibliotecario());
            throw new NoSuchElementException("Bibliotecario(a) con id " + request.getIdBibliotecario() + " no encontrado(a)");
        }

        Turno turno = repository.save(mapper.toEntity(request));
        return mapper.toResponse(turno, bibliotecario);
    }

    public TurnoResponse actualizar(Long id, TurnoRequest request) {
        log.info("Actualizando turno ID: {}", id);

        Turno turno = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el turno con id " + id));

        BibliotecarioResponse bibliotecario = bibliotecarioClient.buscarPorId(request.getIdBibliotecario());
        if (bibliotecario == null) {
            log.warn("Bibliotecario(a) con id: {} no encontrado(a)", request.getIdBibliotecario());
            throw new NoSuchElementException("Bibliotecario(a) con id " + request.getIdBibliotecario() + " no encontrado(a)");
        }

        turno.setTurno(request.getTurno()); 
        turno.setHoraEntrada(request.getHoraEntrada());
        turno.setHoraSalida(request.getHoraSalida());
        turno.setIdBibliotecario(request.getIdBibliotecario());

        Turno turnoActualizado = repository.save(turno);
        return mapper.toResponse(turnoActualizado, bibliotecario);
    }

    // Controller V2
    public List<Turno> getAllEntidad() {
        log.info("Obteniendo entidades de turnos...");
        return repository.findAll();
    }

    public Turno findByIdEntidad(Long id) {
        log.info("Buscando entidad turno con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el turno con ID: " + id));
    }

    public Turno crearEntidad(TurnoRequest request) {
        log.info("Creando entidad turno...");
        
        BibliotecarioResponse bibliotecario = bibliotecarioClient.buscarPorId(request.getIdBibliotecario());
        if (bibliotecario == null) {
            throw new NoSuchElementException("Bibliotecario(a) con id " + request.getIdBibliotecario() + " no encontrado(a)");
        }

        Turno nuevoTurno = mapper.toEntity(request);
        return repository.save(nuevoTurno);
    }

    public Turno actualizarEntidad(Long id, TurnoRequest request) {
        log.info("Actualizando entidad turno con id: {}", id);

        Turno turno = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el turno con id " + id));

        BibliotecarioResponse bibliotecario = bibliotecarioClient.buscarPorId(request.getIdBibliotecario());
        if (bibliotecario == null) {
            throw new NoSuchElementException("Bibliotecario(a) con id " + request.getIdBibliotecario() + " no encontrado(a)");
        }

        turno.setTurno(request.getTurno()); 
        turno.setHoraEntrada(request.getHoraEntrada());
        turno.setHoraSalida(request.getHoraSalida());
        turno.setIdBibliotecario(request.getIdBibliotecario());

        return repository.save(turno);
    }

    // Para ambos controladores
    public void eliminar(Long id) {
        log.info("Eliminando turno con id: {}", id);
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Turno con id " + id + " no encontrado.");
        }
        repository.deleteById(id);
    }
}