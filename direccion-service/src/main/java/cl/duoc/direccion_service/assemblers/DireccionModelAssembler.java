package cl.duoc.direccion_service.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import cl.duoc.direccion_service.controller.DireccionControllerV2;
import cl.duoc.direccion_service.model.Direccion;

@Component
public class DireccionModelAssembler implements RepresentationModelAssembler<Direccion, EntityModel<Direccion>> {
    @Override
    public EntityModel<Direccion> toModel(Direccion direccion) {
        return EntityModel.of(direccion,
                linkTo(methodOn(DireccionControllerV2.class).getDireccionPorId(direccion.getId())).withSelfRel(),
                linkTo(methodOn(DireccionControllerV2.class).getAll()).withRel("direcciones"));
    }
}
