package cl.duoc.genero_service.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import cl.duoc.genero_service.controller.GeneroControllerV2;
import cl.duoc.genero_service.model.Genero;

@Component
public class GeneroModelAssembler implements RepresentationModelAssembler<Genero, EntityModel<Genero>> {
    @Override
    public EntityModel<Genero> toModel(Genero genero) {
        return EntityModel.of(genero,
                linkTo(methodOn(GeneroControllerV2.class).getGeneroPorId(genero.getId())).withSelfRel(),
                linkTo(methodOn(GeneroControllerV2.class).getAll()).withRel("generos"));
    }
}