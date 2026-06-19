package cl.duoc.autor_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import cl.duoc.autor_service.controller.AutorControllerV2;
import cl.duoc.autor_service.model.Autor;

@Component
public class AutorModelAssembler implements RepresentationModelAssembler<Autor, EntityModel<Autor>> {
    @Override
    public EntityModel<Autor> toModel(Autor autor) {
        return EntityModel.of(autor,
                linkTo(methodOn(AutorControllerV2.class).getAutorPorId(autor.getId())).withSelfRel(),
                linkTo(methodOn(AutorControllerV2.class).getAll()).withRel("autores"));
    }
}