package cl.duoc.piso_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.piso_service.dto.PisoRequest;
import cl.duoc.piso_service.dto.PisoResponse;
import cl.duoc.piso_service.mapper.PisoMapper;
import cl.duoc.piso_service.model.Piso;
import cl.duoc.piso_service.repository.PisoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PisoService {
    @Autowired
    private PisoRepository repository;

    @Autowired
    private PisoMapper mapper;

    // Controller V1
    public List<PisoResponse> getAll() {
        log.info("Obteniendo pisos...");
        return repository.findAll().stream()
                .map(mapper::toResponse).toList();
    }

    public PisoResponse findById(Long id) {
        log.info("Buscando piso con id: {}", id);
        Piso piso = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró piso con ID: " + id));
        return mapper.toResponse(piso);
    }

    public PisoResponse crear(PisoRequest request){
        log.info("Creando piso: {}",request.getNumero());
        Piso piso = repository.save(mapper.toEntity(request));
        return mapper.toResponse(piso);
    }

    public PisoResponse actualizar(Long id, PisoRequest request){
        log.info("Actualizando piso: {}", request.getNumero());
        Piso piso = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("No se encontró piso con id "+ id));

        piso.setNumero(request.getNumero());
        piso.setDescripcion(request.getDescripcion());

        Piso pisoActualizado = repository.save(piso);
        return mapper.toResponse(pisoActualizado);
    }

    // Controller V2
    public List<Piso> getAllEntidad() {
        log.info("Obteniendo pisos...");
        return repository.findAll();
    }

    public Piso findByIdEntidad(Long id) {
        log.info("Buscando piso con id: {}", id);
        Piso piso = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró piso con ID: " + id));
        return piso;
    }

    public Piso crearEntidad (PisoRequest piso){
        log.info("Creando piso: {}",piso.getNumero());
        Piso nuevoPiso = mapper.toEntity(piso);
        return repository.save(nuevoPiso);
    }


    public Piso actualizarEntidad (Long id, PisoRequest request){
        log.info("Actualizando piso: {}", request.getNumero());
        Piso piso = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("No se encontró piso con id "+ id));

        piso.setNumero(request.getNumero());
        piso.setDescripcion(request.getDescripcion());

        Piso pisoActualizado = repository.save(piso);
        return pisoActualizado;
    }

    // Para ambos controladores
    public void eliminar (Long id){
        log.info("Eliminando piso con id: {}", id);
        if (!repository.existsById(id)){
            throw new NoSuchElementException("Piso con id" + id + " no encontrado.");
        }
        repository.deleteById(id);
    }
}