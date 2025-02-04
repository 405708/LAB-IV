CREATE TABLE IF NOT EXISTS reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    id_hotel BIGINT NOT NULL,
    tipo_habitacion VARCHAR(50) NOT NULL,
    fecha_ingreso DATETIME NOT NULL,
    fecha_salida DATETIME NOT NULL,
    estado_reserva VARCHAR(50) NOT NULL,
    medio_pago VARCHAR(50) NOT NULL,
    precio DECIMAL(10,2) NOT NULL
    );

INSERT INTO reservas (id_cliente, id_hotel, tipo_habitacion, fecha_ingreso, fecha_salida, estado_reserva, medio_pago, precio)
VALUES
    (1, 101, 'SIMPLE', '2024-09-01 15:00:00', '2024-09-05 12:00:00', 'CONFIRMADA', 'TARJETA', 250.00),
    (2, 102, 'DOBLE', '2024-09-10 14:00:00', '2024-09-15 11:00:00', 'PENDIENTE', 'EFECTIVO', 450.50),
    (3, 103, 'SUITE', '2024-10-01 16:00:00', '2024-10-07 10:00:00', 'CANCELADA', 'TRANSFERENCIA', 800.75);
