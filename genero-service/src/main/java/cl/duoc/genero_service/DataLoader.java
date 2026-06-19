package cl.duoc.genero_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import cl.duoc.genero_service.model.Genero;
import cl.duoc.genero_service.repository.GeneroRepository;
import net.datafaker.Faker;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner{
    @Autowired
    private GeneroRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0) return;

        Faker faker = new Faker();

        for (int i = 0; i < 10; i++) {
            Genero genero = new Genero();
            genero.setNombre(faker.book().genre());
            genero.setDescripcion(faker.lorem().sentence());
            repository.save(genero);
        }
    }
}