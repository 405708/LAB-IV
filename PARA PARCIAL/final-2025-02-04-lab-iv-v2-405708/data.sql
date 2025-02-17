-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS telemetry_db;
USE telemetry_db;

-- Crear la tabla DEVICE
CREATE TABLE DEVICE (
                        HOSTNAME VARCHAR(255) PRIMARY KEY,
                        createdDate DATETIME,
                        os VARCHAR(100),
                        macAddress VARCHAR(255) UNIQUE,
                        TYPE ENUM('LAPTOP', 'DESKTOP', 'TABLET', 'PHONE')
);

-- Crear la tabla TELEMETRY
CREATE TABLE TELEMETRY (
                           ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                           ip VARCHAR(50),
                           dataDate DATETIME,
                           hostDiskFree DOUBLE,
                           cpuUsage DOUBLE,
                           microphoneState VARCHAR(255),
                           screenCaptureAllowed BOOLEAN,
                           audioCaptureAllowed BOOLEAN,
                           hostname VARCHAR(255),
                           CONSTRAINT fk_telemetry_device FOREIGN KEY (hostname) REFERENCES DEVICE(HOSTNAME) ON DELETE CASCADE
);