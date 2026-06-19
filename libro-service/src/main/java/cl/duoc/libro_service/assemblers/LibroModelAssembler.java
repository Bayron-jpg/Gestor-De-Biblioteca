package cl.duoc.libro_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import cl.duoc.libro_service.controller.LibroControllerV2;
import cl.duoc.libro_service.model.Libro;

@Component
public class LibroModelAssembler implements RepresentationModelAssembler<Libro, EntityModel<Libro>> {
    @Override
    public EntityModel<Libro> toModel(Libro libro) {
        return EntityModel.of(libro,
                linkTo(methodOn(LibroControllerV2.class).getLibroPorId(libro.getId())).withSelfRel(),
                linkTo(methodOn(LibroControllerV2.class).getAll()).withRel("libros"));
    }
}