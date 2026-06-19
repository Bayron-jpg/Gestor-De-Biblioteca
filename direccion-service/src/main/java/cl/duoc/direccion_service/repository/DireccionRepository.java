package cl.duoc.direccion_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.direccion_service.model.Direccion;

public interface DireccionRepository extends JpaRepository<Direccion, Long>{
}