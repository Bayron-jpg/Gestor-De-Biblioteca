package cl.duoc.turno_service.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import cl.duoc.turno_service.controller.TurnoControllerV2;
import cl.duoc.turno_service.model.Turno;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TurnoModelAssembler implements RepresentationModelAssembler<Turno, EntityModel<Turno>>{
    @Override
    public EntityModel<Turno> toModel(Turno turno) {
        return EntityModel.of(turno,
                linkTo(methodOn(TurnoControllerV2.class).getTurnoPorId(turno.getId())).withSelfRel(),
                linkTo(methodOn(TurnoControllerV2.class).getAll()).withRel("turnos"));
    }
}
