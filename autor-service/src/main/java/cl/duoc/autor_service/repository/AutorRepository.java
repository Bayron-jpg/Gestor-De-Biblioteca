package cl.duoc.autor_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.autor_service.model.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long>{
}