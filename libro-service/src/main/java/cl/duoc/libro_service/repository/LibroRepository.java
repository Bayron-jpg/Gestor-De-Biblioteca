package cl.duoc.libro_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.libro_service.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long>{
}