package cl.duoc.piso_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.piso_service.model.Piso;

public interface PisoRepository extends JpaRepository<Piso, Long> {
}