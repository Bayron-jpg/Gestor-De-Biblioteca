CREATE TABLE libro (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(60) NOT NULL,
    isbn VARCHAR(50) NOT NULL,
    fecha_publicacion DATE NOT NULL,
    id_autor BIGINT NOT NULL,
    id_genero BIGINT NOT NULL
);