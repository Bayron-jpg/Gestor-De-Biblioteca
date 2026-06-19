package cl.duoc.telefono_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import cl.duoc.telefono_service.controller.TelefonoControllerV2;
import cl.duoc.telefono_service.model.Telefono;

@Component
public class TelefonoModelAssembler implements RepresentationModelAssembler<Telefono, EntityModel<Telefono>> {
    @Override
    public EntityModel<Telefono> toModel(Telefono telefono) {
        return EntityModel.of(telefono,
                linkTo(methodOn(TelefonoControllerV2.class).getTelefonoPorId(telefono.getId())).withSelfRel(),
                linkTo(methodOn(TelefonoControllerV2.class).getAll()).withRel("telefonos"));
    }
}
