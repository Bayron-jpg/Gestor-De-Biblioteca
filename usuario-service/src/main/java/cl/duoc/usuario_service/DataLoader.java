package cl.duoc.usuario_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import cl.duoc.usuario_service.model.Usuario;
import cl.duoc.usuario_service.repository.UsuarioRepository;
import net.datafaker.Faker;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0) return;

        Faker faker = new Faker();

        for (int i = 0; i < 10; i++) {
            Usuario usuario = new Usuario();
            
            usuario.setNombre(faker.name().firstName());
            usuario.setApellido(faker.name().lastName());
            usuario.setGmail(faker.internet().emailAddress());
            usuario.setIdTelefono((long) faker.number().numberBetween(1, 10));
            usuario.setIdDireccion((long) faker.number().numberBetween(1, 10));
            
            repository.save(usuario);
        }
    }
}