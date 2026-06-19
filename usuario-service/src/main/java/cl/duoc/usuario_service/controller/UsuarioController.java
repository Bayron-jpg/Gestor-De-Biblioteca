package cl.duoc.usuario_service.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cl.duoc.usuario_service.dto.UsuarioRequest;
import cl.duoc.usuario_service.dto.UsuarioResponse;
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

@Tag(name = "UsuariosV1", description = "Operaciones relacionadas con los usuarios.")
@RequestMapping("/api/v1/usuarios")
@RestController
@Slf4j
public class UsuarioController {
    @Autowired
    private UsuarioService service;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Obtiene los usuarios existentes.")
    @ApiResponse(responseCode = "200", description = "Usuarios obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class)))
    public ResponseEntity<List<UsuarioResponse>> getUsuarios() {
        log.info("V1/GET/usuarios");
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Obtiene un usuario por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    })
    public ResponseEntity<UsuarioResponse> getUsuarioPorId(@PathVariable Long id) {
        log.info("V1/GET/usuarios/{}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario.")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class)))
    public ResponseEntity<UsuarioResponse> postUsuario(@Valid @RequestBody UsuarioRequest request) {
        log.info("V1/POST/usuarios");
        UsuarioResponse usuarioCreado = service.crear(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuarioCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(usuarioCreado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un usuario por su ID", description = "Modifica un usuario existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario modificado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    })
    public ResponseEntity<UsuarioResponse> putUsuario(@PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request) {
        log.info("V1/PUT/usuarios/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario por su ID", description = "Elimina un usuario existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado.")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        log.info("V1/DELETE/usuarios/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}