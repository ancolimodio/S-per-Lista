CREATE DATABASE compras;

USE compras;

CREATE TABLE lista_compra (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL
);

CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    cantidad INT NOT NULL,
    categoria VARCHAR(255) NOT NULL,
    lista_compra_id INT,
    comprado boolean,
    FOREIGN KEY (lista_compra_id) REFERENCES lista_compra(id)
);
