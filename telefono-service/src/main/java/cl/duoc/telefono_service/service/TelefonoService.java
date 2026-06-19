package cl.duoc.telefono_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.telefono_service.dto.TelefonoRequest;
import cl.duoc.telefono_service.dto.TelefonoResponse;
import cl.duoc.telefono_service.mapper.TelefonoMapper;
import cl.duoc.telefono_service.model.Telefono;
import cl.duoc.telefono_service.repository.TelefonoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TelefonoService {
    @Autowired
    private TelefonoRepository repository;

    @Autowired
    private TelefonoMapper mapper;

    // Controller V1
    public List<TelefonoResponse> getAll() {
        log.info("Obteniendo telefonos...");
        return repository.findAll().stream()
                .map(mapper::toResponse).toList();
    }

    public TelefonoResponse findById(Long id) {
        log.info("Buscando telefono con id: {}", id);
        Telefono telefono = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró telefono con ID: " + id));
        return mapper.toResponse(telefono);
    }

    public TelefonoResponse crear(TelefonoRequest request){
        log.info("Creando telefono: {}",request.getNumero());
        Telefono telefono = repository.save(mapper.toEntity(request));
        return mapper.toResponse(telefono);
    }

    public TelefonoResponse actualizar(Long id, TelefonoRequest request){
        log.info("Actualizando telefono: {}", request.getNumero());
        Telefono telefono = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("No se encontró telefono con id "+ id));

        telefono.setNumero(request.getNumero());
        telefono.setTipo(request.getTipo());

        Telefono telefonoActualizado = repository.save(telefono);
        return mapper.toResponse(telefonoActualizado);
    }

    // Controller V2
    public List<Telefono> getAllEntidad() {
        log.info("Obteniendo telefonos...");
        return repository.findAll();
    }

    public Telefono findByIdEntidad(Long id) {
        log.info("Buscando telefono con id: {}", id);
        Telefono telefono = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró telefono con ID: " + id));
        return telefono;
    }

    public Telefono crearEntidad (TelefonoRequest telefono){
        log.info("Creando telefono: {}",telefono.getNumero());
        Telefono nuevoTelefono = mapper.toEntity(telefono);
        return repository.save(nuevoTelefono);
    }


    public Telefono actualizarEntidad (Long id, TelefonoRequest request){
        log.info("Actualizando telefono: {}", request.getNumero());
        Telefono telefono = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("No se encontró telefono con id "+ id));

        telefono.setNumero(request.getNumero());
        telefono.setTipo(request.getTipo());

        Telefono telefonoActualizado = repository.save(telefono);
        return telefonoActualizado;
    }

    // Para ambos controladores
    public void eliminar (Long id){
        log.info("Eliminando telefono con id: {}", id);
        if (!repository.existsById(id)){
            throw new NoSuchElementException("Telefono con id" + id + " no encontrado(a).");
        }
        repository.deleteById(id);
    }
}