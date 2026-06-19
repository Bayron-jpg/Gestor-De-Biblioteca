package cl.duoc.direccion_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import cl.duoc.direccion_service.model.Direccion;
import cl.duoc.direccion_service.repository.DireccionRepository;
import net.datafaker.Faker;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {
    @Autowired
    private DireccionRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0)
            return;

        Faker faker = new Faker();

        for (int i = 0; i < 10; i++) {
            Direccion direccion = new Direccion();
            direccion.setDireccion(faker.address().streetAddress());
            direccion.setComuna(faker.address().city());
            direccion.setRegion(faker.address().state());
            repository.save(direccion);
        }
    }
}
