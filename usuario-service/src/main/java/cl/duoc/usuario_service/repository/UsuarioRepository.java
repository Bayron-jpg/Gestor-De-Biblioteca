package cl.duoc.usuario_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.usuario_service.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
}