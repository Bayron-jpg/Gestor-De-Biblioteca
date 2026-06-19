CREATE TABLE turno (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    turno VARCHAR(40) NOT NULL,
    hora_entrada TIME NOT NULL,
    hora_salida TIME NOT NULL,
    id_bibliotecario BIGINT NOT NULL
);