package cl.duoc.usuario_service.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.usuario_service.controller.UsuarioControllerV2;
import cl.duoc.usuario_service.model.Usuario;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>>{
    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioControllerV2.class).getUsuarioPorId(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioControllerV2.class).getAll()).withRel("Usuarios"));
    }
}