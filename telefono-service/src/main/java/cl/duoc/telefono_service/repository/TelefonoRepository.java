package cl.duoc.telefono_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.telefono_service.model.Telefono;

public interface TelefonoRepository extends JpaRepository<Telefono, Long>{
}