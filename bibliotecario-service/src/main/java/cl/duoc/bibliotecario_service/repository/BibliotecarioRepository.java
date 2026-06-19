package cl.duoc.bibliotecario_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.bibliotecario_service.model.Bibliotecario;

public interface BibliotecarioRepository extends JpaRepository<Bibliotecario, Long>{
}