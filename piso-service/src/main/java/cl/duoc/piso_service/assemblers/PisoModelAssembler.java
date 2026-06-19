package cl.duoc.piso_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import cl.duoc.piso_service.controller.PisoControllerV2;
import cl.duoc.piso_service.model.Piso;

@Component
public class PisoModelAssembler implements RepresentationModelAssembler<Piso, EntityModel<Piso>> {
    @Override
    public EntityModel<Piso> toModel(Piso piso) {
        return EntityModel.of(piso,
                linkTo(methodOn(PisoControllerV2.class).getPisoPorId(piso.getId())).withSelfRel(),
                linkTo(methodOn(PisoControllerV2.class).getAll()).withRel("pisos"));
    }
}