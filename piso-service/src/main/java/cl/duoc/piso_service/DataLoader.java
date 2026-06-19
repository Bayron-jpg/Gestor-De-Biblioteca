package cl.duoc.piso_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import cl.duoc.piso_service.model.Piso;
import cl.duoc.piso_service.repository.PisoRepository;
import net.datafaker.Faker;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {
    @Autowired
    private PisoRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0) return;

        Faker faker = new Faker();

        for (int i = 0; i < 10; i++) {
            Piso piso = new Piso();
            piso.setNumero(String.valueOf(i + 1));
            piso.setDescripcion(faker.lorem().sentence());
            repository.save(piso);
        }
    }
}