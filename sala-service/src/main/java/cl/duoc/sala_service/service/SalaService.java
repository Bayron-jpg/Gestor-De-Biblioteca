package cl.duoc.sala_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.sala_service.client.PisoClient;
import cl.duoc.sala_service.dto.PisoResponse;
import cl.duoc.sala_service.dto.SalaRequest;
import cl.duoc.sala_service.dto.SalaResponse;
import cl.duoc.sala_service.mapper.SalaMapper;
import cl.duoc.sala_service.model.Sala;
import cl.duoc.sala_service.repository.SalaRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SalaService {

    @Autowired
    private SalaRepository repository;

    @Autowired
    private PisoClient pisoClient;

    @Autowired
    private SalaMapper mapper;

    // Controller V1
    public List<SalaResponse> getAll() {
        log.info("Obteniendo salas...");
        return repository.findAll().stream()
                .map(sala -> {
                    PisoResponse piso = pisoClient.buscarPorId(sala.getIdPiso());
                    return mapper.toResponse(sala, piso);
                }).toList();
    }

    public SalaResponse findById(Long id) {
        log.info("Buscando sala con id: {}", id);
        Sala sala = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró sala con ID: " + id));

        PisoResponse piso = pisoClient.buscarPorId(sala.getIdPiso());
        return mapper.toResponse(sala, piso);
    }

    public SalaResponse crear(SalaRequest request) {
        log.info("Creando sala: {}", request.getNombre());

        PisoResponse piso = pisoClient.buscarPorId(request.getIdPiso());
        if (piso == null) {
            log.warn("Piso con id: {} no encontrado", request.getIdPiso());
            throw new NoSuchElementException("Piso con id " + request.getIdPiso() + " no encontrado");
        }

        Sala sala = repository.save(mapper.toEntity(request));
        return mapper.toResponse(sala, piso);
    }

    public SalaResponse actualizar(Long id, SalaRequest request) {
        log.info("Actualizando sala: {}", request.getNombre());

        Sala sala = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró sala con id " + id));

        PisoResponse piso = pisoClient.buscarPorId(request.getIdPiso());
        if (piso == null) {
            log.warn("Piso con id: {} no encontrado", request.getIdPiso());
            throw new NoSuchElementException("Piso con id " + request.getIdPiso() + " no encontrado");
        }

        sala.setNombre(request.getNombre());
        sala.setCapacidad(request.getCapacidad());
        sala.setIdPiso(request.getIdPiso());

        Sala salaActualizada = repository.save(sala);
        return mapper.toResponse(salaActualizada, piso);
    }

    // Controller V2
    public List<Sala> getAllEntidad() {
        log.info("Obteniendo salas...");
        return repository.findAll();
    }

    public Sala findByIdEntidad(Long id) {
        log.info("Buscando sala con id: {}", id);
        Sala sala = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró sala con ID: " + id));
        return sala;
    }

    public Sala crearEntidad(SalaRequest request) {
        log.info("Creando sala: {}", request.getNombre());

        PisoResponse piso = pisoClient.buscarPorId(request.getIdPiso());
        if (piso == null) {
            throw new NoSuchElementException("Piso con id " + request.getIdPiso() + " no encontrado");
        }

        Sala nuevaSala = mapper.toEntity(request);
        return repository.save(nuevaSala);
    }

    public Sala actualizarEntidad(Long id, SalaRequest request) {
        log.info("Actualizando sala: {}", request.getNombre());

        Sala sala = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró sala con id " + id));

        PisoResponse piso = pisoClient.buscarPorId(request.getIdPiso());
        if (piso == null) {
            throw new NoSuchElementException("Piso con id " + request.getIdPiso() + " no encontrado");
        }

        sala.setNombre(request.getNombre());
        sala.setCapacidad(request.getCapacidad());
        sala.setIdPiso(request.getIdPiso());

        return repository.save(sala);
    }

    // Para ambos controladores
    public void eliminar(Long id) {
        log.info("Eliminando sala con id: {}", id);
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Sala con id " + id + " no encontrada.");
        }
        repository.deleteById(id);
    }
}