package cl.duoc.libro_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import cl.duoc.libro_service.model.Libro;
import cl.duoc.libro_service.repository.LibroRepository;
import net.datafaker.Faker;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private LibroRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0) return;

        Faker faker = new Faker();

        for (int i = 0; i < 10; i++) {
            Libro libro = new Libro();
            libro.setTitulo(faker.book().title());
            libro.setIsbn(faker.code().isbn10());
            libro.setFechaPublicacion(faker.timeAndDate().birthday(1, 50));
            libro.setIdAutor((long) faker.number().numberBetween(1, 10));
            libro.setIdGenero((long) faker.number().numberBetween(1, 10));
            repository.save(libro);
        }
    }
}