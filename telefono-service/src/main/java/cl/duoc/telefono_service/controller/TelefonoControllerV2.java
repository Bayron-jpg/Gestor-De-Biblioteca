package cl.duoc.telefono_service.controller;

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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import cl.duoc.telefono_service.assemblers.TelefonoModelAssembler;
import cl.duoc.telefono_service.dto.TelefonoRequest;
import cl.duoc.telefono_service.model.Telefono;
import cl.duoc.telefono_service.service.TelefonoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "TeléfonosV2", description = "Operaciones relacionadas con los teléfonos.")
@RequestMapping("/api/v2/telefonos")
@RestController
@Slf4j
public class TelefonoControllerV2 {
    @Autowired
    private TelefonoService service;

    @Autowired
    private TelefonoModelAssembler assembler;

    @GetMapping(produces = "application/json")
    @Operation(summary = "Obtener todos los teléfonos", description = "Obtiene los teléfonos existentes.")
    @ApiResponse(responseCode = "200", description = "Teléfonos obtenidos exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Telefono.class)))
    public CollectionModel<EntityModel<Telefono>> getAll() {
        log.info("V2/GET/telefonos");
        List<EntityModel<Telefono>> telefonos = service.getAllEntidad().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(telefonos,
                linkTo(methodOn(TelefonoControllerV2.class).getAll()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Obtener telefono por su ID", description = "Obtiene un telefono por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telefono obtenido exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Telefono.class))),
            @ApiResponse(responseCode = "404", description = "Telefono no encontrado.")
    })
    public EntityModel<Telefono> getTelefonoPorId(@PathVariable Long id) {
        log.info("V2/GET/telefonos/{}", id);
        Telefono telefono = service.findByIdEntidad(id);
        return assembler.toModel(telefono);
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Crear teléfono", description = "Crea un nuevo telefono.")
    @ApiResponse(responseCode = "201", description = "Telefono creado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Telefono.class)))
    public ResponseEntity<EntityModel<Telefono>> crearTelefono(@Valid @RequestBody TelefonoRequest request) {
        log.info("V2/POST/telefonos");
        Telefono nuevoTelefono = service.crearEntidad(request);

        return ResponseEntity.created(
                linkTo(methodOn(TelefonoControllerV2.class)
                        .getTelefonoPorId(nuevoTelefono.getId()))
                        .toUri())
                .body(assembler.toModel(nuevoTelefono));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Modificar telefono por su ID", description = "Modifica un teléfono existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telefono modificado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Telefono.class))),
            @ApiResponse(responseCode = "404", description = "Telefono no encontrado.")
    })
    public ResponseEntity<EntityModel<Telefono>> actualizarTelefono(@PathVariable Long id,
            @Valid @RequestBody TelefonoRequest request) {
        log.info("V2/PUT/telefonos/{}", id);
        Telefono telefonoActualizado = service.actualizarEntidad(id, request);
        return ResponseEntity.ok(assembler.toModel(telefonoActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Eliminar teléfono por ID", description = "Elimina un teléfono existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Telefono eliminado.")
    public ResponseEntity<?> deleteTelefono(@PathVariable Long id) {
        log.info("V2/DELETE/telefonos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
