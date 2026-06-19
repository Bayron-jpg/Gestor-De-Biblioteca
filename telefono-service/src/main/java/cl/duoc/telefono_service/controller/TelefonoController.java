package cl.duoc.telefono_service.controller;

import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cl.duoc.telefono_service.dto.TelefonoRequest;
import cl.duoc.telefono_service.dto.TelefonoResponse;
import cl.duoc.telefono_service.model.Telefono;
import cl.duoc.telefono_service.service.TelefonoService;

@Tag(name = "TeléfonosV1", description = "Operaciones relacionadas con los teléfonos.")
@RequestMapping("/api/v1/telefonos")
@RestController
@Slf4j
public class TelefonoController {

    @Autowired
    private TelefonoService service;

    @GetMapping
    @Operation(summary = "Obtener todos los teléfonos", description = "Obtiene los teléfonos existentes.")
    @ApiResponse(responseCode = "200", description = "Teléfonos obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Telefono.class)))
    public ResponseEntity<List<TelefonoResponse>> getTelefonos() {
        log.info("V1/GET/telefonos");
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener teléfono por ID", description = "Obtiene un teléfono por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teléfono obtenido exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Telefono.class))),
            @ApiResponse(responseCode = "404", description = "Teléfono no encontrado.")
    })
    public ResponseEntity<TelefonoResponse> getTelefonoPorId(@PathVariable Long id) {
        log.info("V1/GET/telefonos/{}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear teléfono", description = "Crea un nuevo teléfono.")
    @ApiResponse(responseCode = "201", description = "Teléfono creado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Telefono.class)))
    public ResponseEntity<TelefonoResponse> postTelefono(@Valid @RequestBody TelefonoRequest request) {
        log.info("V1/POST/telefonos");
        TelefonoResponse telefonoCreado = service.crear(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(telefonoCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(telefonoCreado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un teléfono por su ID", description = "Modifica un autor existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teléfono modificado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Telefono.class))),
            @ApiResponse(responseCode = "404", description = "Teléfono no encontrado.")
    })
    public ResponseEntity<TelefonoResponse> putTelefono(@PathVariable Long id,
            @Valid @RequestBody TelefonoRequest request) {
        log.info("V1/PUT/telefonos/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar teléfono por su ID", description = "Elimina un teléfono existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Teléfono eliminado.")
    public ResponseEntity<Void> deleteTelefono(@PathVariable Long id) {
        log.info("V1/DELETE/telefonos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
