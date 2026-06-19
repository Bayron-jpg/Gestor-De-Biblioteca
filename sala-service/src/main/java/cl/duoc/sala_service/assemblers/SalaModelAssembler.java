package cl.duoc.sala_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import cl.duoc.sala_service.controller.SalaControllerV2;
import cl.duoc.sala_service.model.Sala;

@Component
public class SalaModelAssembler implements RepresentationModelAssembler<Sala, EntityModel<Sala>> {
    @Override
    public EntityModel<Sala> toModel(Sala sala) {
        return EntityModel.of(sala,
                linkTo(methodOn(SalaControllerV2.class).getSalaPorId(sala.getId())).withSelfRel(),
                linkTo(methodOn(SalaControllerV2.class).getAll()).withRel("salas"));
    }
}