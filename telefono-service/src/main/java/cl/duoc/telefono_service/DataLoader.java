package cl.duoc.telefono_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import cl.duoc.telefono_service.model.Telefono;
import cl.duoc.telefono_service.repository.TelefonoRepository;
import net.datafaker.Faker;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {
    @Autowired
    private TelefonoRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0)
            return;

        Faker faker = new Faker();

        for (int i = 0; i < 10; i++) {
            Telefono telefono = new Telefono();
            telefono.setNumero(faker.number().numberBetween(100000000, 999999999));
            telefono.setTipo(faker.options().option("Móvil", "Fijo", "Trabajo", "Casa"));
            repository.save(telefono);
        }
    }
}