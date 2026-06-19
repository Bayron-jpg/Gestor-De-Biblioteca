package cl.duoc.sala_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.sala_service.model.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long>{
}