package cl.duoc.sala_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import cl.duoc.sala_service.model.Sala;
import cl.duoc.sala_service.repository.SalaRepository;
import net.datafaker.Faker;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private SalaRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0) return;

        Faker faker = new Faker();

        for (int i = 0; i < 10; i++) {
            Sala sala = new Sala();
            sala.setNombre("Sala " + faker.letterify("?").toUpperCase() + faker.number().numberBetween(100, 500));
            sala.setCapacidad(faker.number().numberBetween(5, 100));
            sala.setIdPiso((long) faker.number().numberBetween(1, 10));
            repository.save(sala);
        }
    }
}