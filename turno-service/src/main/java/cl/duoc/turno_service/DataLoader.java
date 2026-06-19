package cl.duoc.turno_service;

import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import cl.duoc.turno_service.model.Turno;
import cl.duoc.turno_service.repository.TurnoRepository;
import net.datafaker.Faker;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private TurnoRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0) return;

        Faker faker = new Faker();

        for (int i = 0; i < 10; i++) {
            Turno turno = new Turno();
            
            String[] opcionesTurno = {"Mañana", "Tarde", "Noche", "Vespertino"};
            turno.setTurno("Turno " + opcionesTurno[faker.number().numberBetween(0, opcionesTurno.length)]);
            
            int horaEntradaAleatoria = faker.number().numberBetween(7, 15); // Entre las 07:00 y las 15:00
            LocalTime horaEntrada = LocalTime.of(horaEntradaAleatoria, 0, 0);
            LocalTime horaSalida = horaEntrada.plusHours(8); // Turnos de 8 horas

            turno.setHoraEntrada(horaEntrada);
            turno.setHoraSalida(horaSalida);
            
            turno.setIdBibliotecario((long) faker.number().numberBetween(1, 10));
            
            repository.save(turno);
        }
    }
}