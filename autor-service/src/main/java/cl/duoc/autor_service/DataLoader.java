package cl.duoc.autor_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import cl.duoc.autor_service.model.Autor;
import cl.duoc.autor_service.repository.AutorRepository;
import net.datafaker.Faker;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {
    @Autowired
    private AutorRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0) return;

        Faker faker = new Faker();

        for (int i = 0; i < 10; i++) {
            Autor autor = new Autor();
            autor.setNombre(faker.book().author());
            autor.setFechaNacimiento(faker.timeAndDate().birthday(18, 80));
            repository.save(autor);
        }
    }
}