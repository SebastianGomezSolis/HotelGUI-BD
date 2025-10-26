CREATE DATABASE IF NOT EXISTS hotel;

USE hotel;

-- TABLA: cliente
CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    primerApellido VARCHAR(80) NOT NULL,
    segundoApellido VARCHAR(80),
    identificacion VARCHAR(20) UNIQUE,
    fechaNacimiento DATE
);

-- TABLA: habitacion
CREATE TABLE habitacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL,
    tipo VARCHAR(80) NOT NULL,
    descripcionTipo VARCHAR(80),
    estado VARCHAR(80) NOT NULL,
    descripcionEstado VARCHAR(80),
    precio DOUBLE NOT NULL,
    capacidad INT NOT NULL
);

-- TABLA: reservacion
CREATE TABLE reservacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idHabitacion INT NOT NULL,
    idCliente INT NOT NULL,
    fechaReservacion DATE NOT NULL,
    fechaLlegada DATE NOT NULL,
    cantidadNoches INT NOT NULL,
    fechaSalida DATE,
    preciototal DOUBLE,
    descuento DOUBLE,

    CONSTRAINT fk_reservacion_cliente FOREIGN KEY (idCliente)
        REFERENCES cliente(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_reservacion_habitacion FOREIGN KEY (idHabitacion)
        REFERENCES habitacion(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);