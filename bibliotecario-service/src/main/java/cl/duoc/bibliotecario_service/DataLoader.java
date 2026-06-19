package cl.duoc.bibliotecario_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import cl.duoc.bibliotecario_service.model.Bibliotecario;
import cl.duoc.bibliotecario_service.repository.BibliotecarioRepository;
import net.datafaker.Faker;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private BibliotecarioRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0) return;

        Faker faker = new Faker();

        for (int i = 0; i < 10; i++) {
            Bibliotecario bibliotecario = new Bibliotecario();
            bibliotecario.setNombre(faker.name().firstName());
            bibliotecario.setApellido(faker.name().lastName());
            bibliotecario.setEdad(faker.number().numberBetween(19, 65));
            repository.save(bibliotecario);
        }
    }
}