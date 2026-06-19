package cl.duoc.bibliotecario_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import cl.duoc.bibliotecario_service.controller.BibliotecarioControllerV2;
import cl.duoc.bibliotecario_service.model.Bibliotecario;

@Component
public class BibliotecarioModelAssembler implements RepresentationModelAssembler<Bibliotecario, EntityModel<Bibliotecario>>{
    @Override
    public EntityModel<Bibliotecario> toModel(Bibliotecario bibliotecario) {
        return EntityModel.of(bibliotecario,
                linkTo(methodOn(BibliotecarioControllerV2.class).getBibliotecarioPorId(bibliotecario.getId())).withSelfRel(),
                linkTo(methodOn(BibliotecarioControllerV2.class).getAll()).withRel("bibliotecarios"));
    }
}