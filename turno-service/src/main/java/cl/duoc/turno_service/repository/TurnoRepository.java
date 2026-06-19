package cl.duoc.turno_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.turno_service.model.Turno;

public interface TurnoRepository extends JpaRepository<Turno, Long>{
}