package cl.duoc.usuario_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.usuario_service.assemblers.UsuarioModelAssembler;
import cl.duoc.usuario_service.dto.UsuarioRequest;
import cl.duoc.usuario_service.model.Usuario;
import cl.duoc.usuario_service.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "UsuariosV2", description = "Operaciones relacionadas con los usuarios.")
@RequestMapping("/api/v2/usuarios")
@RestController
@Slf4j
public class UsuarioControllerV2 {
    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioModelAssembler assembler;

    @GetMapping(produces = "application/json")
    @Operation(summary = "Obtener todos los usuarios", description = "Obtiene los usuarios existentes.")
    @ApiResponse(responseCode = "200", description = "Usuarios obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class)))
    public CollectionModel<EntityModel<Usuario>> getAll() {
        log.info("V2/GET/usuarios");
        List<EntityModel<Usuario>> usuarios = service.getAllEntidad().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioControllerV2.class).getAll()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Obtener usuario por su ID", description = "Obtiene un usuario por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    })
    public EntityModel<Usuario> getUsuarioPorId(@PathVariable Long id) {
        log.info("V2/GET/usuarios/{}", id);
        Usuario usuario = service.findByIdEntidad(id);
        return assembler.toModel(usuario);
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario.")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class)))
    public ResponseEntity<EntityModel<Usuario>> crearUsuario(@Valid @RequestBody UsuarioRequest request) {
        log.info("V2/POST/usuarios");
        Usuario nuevoUsuario = service.crearEntidad(request);

        return ResponseEntity.created(
                linkTo(methodOn(UsuarioControllerV2.class)
                        .getUsuarioPorId(nuevoUsuario.getId()))
                        .toUri())
                .body(assembler.toModel(nuevoUsuario));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Modificar usuario por su ID", description = "Modifica un usuario existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario modificado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    })
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(@PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request) {
        log.info("V2/PUT/usuarios/{}", id);
        Usuario usuarioActualizado = service.actualizarEntidad(id, request);
        return ResponseEntity.ok(assembler.toModel(usuarioActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Eliminar usuario por ID", description = "Elimina un usuario existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado.")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id) {
        log.info("V2/DELETE/usuarios/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}