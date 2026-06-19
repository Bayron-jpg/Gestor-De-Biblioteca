package cl.duoc.usuario_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.usuario_service.client.TelefonoClient;
import cl.duoc.usuario_service.client.DireccionClient;
import cl.duoc.usuario_service.dto.TelefonoResponse;
import cl.duoc.usuario_service.dto.DireccionResponse;
import cl.duoc.usuario_service.dto.UsuarioRequest;
import cl.duoc.usuario_service.dto.UsuarioResponse;
import cl.duoc.usuario_service.mapper.UsuarioMapper;
import cl.duoc.usuario_service.model.Usuario;
import cl.duoc.usuario_service.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TelefonoClient telefonoClient;

    @Autowired
    private DireccionClient direccionClient;

    @Autowired
    private UsuarioMapper mapper;

    // Controller V1
    public List<UsuarioResponse> getAll() {
        log.info("Obteniendo usuarios...");
        return repository.findAll().stream()
                .map(usuario -> {
                    TelefonoResponse telefono = telefonoClient.buscarPorId(usuario.getIdTelefono());
                    DireccionResponse direccion = direccionClient.buscarPorId(usuario.getIdDireccion());
                    return mapper.toResponse(usuario, telefono, direccion);
                }).toList();
    }

    public UsuarioResponse findById(Long id) {
        log.info("Buscando usuario con id: {}", id);
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró usuario con ID: " + id));

        TelefonoResponse telefono = telefonoClient.buscarPorId(usuario.getIdTelefono());
        DireccionResponse direccion = direccionClient.buscarPorId(usuario.getIdDireccion());
        return mapper.toResponse(usuario, telefono, direccion);
    }

    public UsuarioResponse crear(UsuarioRequest request) {
        log.info("Creando usuario: {}", request.getNombre());

        TelefonoResponse telefono = telefonoClient.buscarPorId(request.getIdTelefono());
        if (telefono == null) {
            log.warn("Telefono con id: {} no encontrado", request.getIdTelefono());
            throw new NoSuchElementException("Telefono con id " + request.getIdTelefono() + " no encontrado");
        }

        DireccionResponse direccion = direccionClient.buscarPorId(request.getIdDireccion());
        if (direccion == null) {
            log.warn("Direccion con id: {} no encontrada", request.getIdDireccion());
            throw new NoSuchElementException("Direccion con id " + request.getIdDireccion() + " no encontrada");
        }

        Usuario usuario = repository.save(mapper.toEntity(request));
        return mapper.toResponse(usuario, telefono, direccion);
    }

    public UsuarioResponse actualizar(Long id, UsuarioRequest request) {
        log.info("Actualizando usuario: {}", request.getNombre());

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro usuario con id " + id));

        TelefonoResponse telefono = telefonoClient.buscarPorId(request.getIdTelefono());
        if (telefono == null) {
            log.warn("Telefono con id: {} no encontrado", request.getIdTelefono());
            throw new NoSuchElementException("Telefono con id " + request.getIdTelefono() + " no encontrado");
        }

        DireccionResponse direccion = direccionClient.buscarPorId(request.getIdDireccion());
        if (direccion == null) {
            log.warn("Direccion con id: {} no encontrada", request.getIdDireccion());
            throw new NoSuchElementException("Direccion con id " + request.getIdDireccion() + " no encontrada");
        }

        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setGmail(request.getGmail());
        usuario.setIdTelefono(request.getIdTelefono());
        usuario.setIdDireccion(request.getIdDireccion());

        Usuario usuarioActualizado = repository.save(usuario);
        return mapper.toResponse(usuarioActualizado, telefono, direccion);
    }

    // Controller V2
    public List<Usuario> getAllEntidad() {
        log.info("Obteniendo usuarios...");
        return repository.findAll();
    }

    public Usuario findByIdEntidad(Long id) {
        log.info("Buscando usuario con id: {}", id);
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró usuario con ID: " + id));
        return usuario;
    }

    public Usuario crearEntidad(UsuarioRequest request) {
        log.info("Creando usuario: {}", request.getNombre());

        TelefonoResponse telefono = telefonoClient.buscarPorId(request.getIdTelefono());
        if (telefono == null) {
            throw new NoSuchElementException("Telefono con id " + request.getIdTelefono() + " no encontrado");
        }

        DireccionResponse direccion = direccionClient.buscarPorId(request.getIdDireccion());
        if (direccion == null) {
            throw new NoSuchElementException("Direccion con id " + request.getIdDireccion() + " no encontrada");
        }

        Usuario nuevoUsuario = mapper.toEntity(request);
        return repository.save(nuevoUsuario);
    }

    public Usuario actualizarEntidad(Long id, UsuarioRequest request) {
        log.info("Actualizando usuario: {}", request.getNombre());

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro usuario con id " + id));

        TelefonoResponse telefono = telefonoClient.buscarPorId(request.getIdTelefono());
        if (telefono == null) {
            throw new NoSuchElementException("Telefono con id " + request.getIdTelefono() + " no encontrado");
        }

        DireccionResponse direccion = direccionClient.buscarPorId(request.getIdDireccion());
        if (direccion == null) {
            throw new NoSuchElementException("Direccion con id " + request.getIdDireccion() + " no encontrada");
        }

        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setGmail(request.getGmail());
        usuario.setIdTelefono(request.getIdTelefono());
        usuario.setIdDireccion(request.getIdDireccion());

        return repository.save(usuario);
    }

    // Para ambos controladores
    public void eliminar(Long id) {
        log.info("Eliminando usuario con id: {}", id);
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Usuario con id " + id + " no encontrado.");
        }
        repository.deleteById(id);
    }
}