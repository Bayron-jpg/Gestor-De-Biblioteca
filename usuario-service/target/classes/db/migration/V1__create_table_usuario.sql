CREATE TABLE usuario (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(40) NOT NULL,
    apellido VARCHAR(40) NOT NULL,
    gmail VARCHAR(60) NOT NULL,
    id_telefono BIGINT NOT NULL,
    id_direccion BIGINT NOT NULL
);