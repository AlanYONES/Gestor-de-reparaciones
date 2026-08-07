CREATE DATABASE gestor_reparaciones;
USE gestor_reparaciones;

CREATE TABLE clientes (
	id INT AUTO_INCREMENT,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    tipo_documento ENUM('DNI', 'CUIT', 'PASAPORTE'),
    numero_documento VARCHAR(20),
    telefono VARCHAR(20),
    correo VARCHAR(100),
    en_lista_negra BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id)
    );
    
CREATE TABLE empleados (
	id INT AUTO_INCREMENT,
    nombre VARCHAR(50),
    cuit VARCHAR(15),
    rol_empleado ENUM('TECNICO', 'RECEPCIONISTA', 'ADMINISTRADOR'),
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id)
    );
    
CREATE TABLE plantilla_diagnostico (
	id INT AUTO_INCREMENT,
    nombre VARCHAR(50),
    descripcion TEXT,
    dias_estimados INT,
    PRIMARY KEY (id)
    );

CREATE TABLE registro_lista_negra (
	id INT AUTO_INCREMENT,
    cliente_id INT,
	motivo VARCHAR(100),
	fecha DATE,
    empleado_id INT,
	PRIMARY KEY (id),
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (empleado_id) REFERENCES empleados(id)
	);
	
CREATE TABLE dispositivos (
	id INT AUTO_INCREMENT,
    cliente_id INT,
    tipo_equipo ENUM('CELULAR', 'TABLET', 'PARLANTE', 'OTROS'),
    marca VARCHAR(15),
    modelo VARCHAR(20),
    imei CHAR(15),
    numero_serie VARCHAR(20),
    PRIMARY KEY (id),
    FOREIGN KEY (cliente_id) REFERENCES clientes(id)
    );
    
CREATE TABLE accesorios (
	id INT AUTO_INCREMENT,
    dispositivo_id INT,
    descripcion VARCHAR(50),
    PRIMARY KEY (id),
    FOREIGN KEY (dispositivo_id) REFERENCES dispositivos(id)
    );
    
CREATE TABLE reparaciones (
	id INT AUTO_INCREMENT,
    dispositivo_id INT,
    empleado_id INT,
    estado_reparacion ENUM('RECIBIDO', 'EN_REPARACION', 'ENTREGADO', 'CANCELADA'),
    falla_declarada TEXT,
    estado_fisico_al_recibir TEXT,
    observaciones TEXT,
    reparacion_realizada TEXT,
    fecha_entrada DATETIME,
    fecha_entrega_estimada DATE,
    fecha_entrega_final DATETIME,
    presupuesto DOUBLE,
    pin_desbloqueo VARCHAR(50),
    tiene_garantia BOOLEAN,
    dias_garantia INT,
    fecha_vencimiento_garantia DATE,
    cancelada_con_cargo BOOLEAN,
    cargo_revision DOUBLE,
    PRIMARY KEY (id),
    FOREIGN KEY (dispositivo_id) REFERENCES dispositivos(id),
    FOREIGN KEY (empleado_id) REFERENCES empleados(id)
    );
    
CREATE TABLE patron_desbloqueo (
	id INT AUTO_INCREMENT,
    reparacion_id INT,
    posicion INT,
    orden INT,
    PRIMARY KEY (id),
    FOREIGN KEY (reparacion_id) REFERENCES reparaciones(id)
    );
    
CREATE TABLE foto_reparacion (
	id INT AUTO_INCREMENT,
    reparacion_id INT,
    ruta VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (reparacion_id) REFERENCES reparaciones(id)
    );
    
CREATE TABLE pagos (
	id INT AUTO_INCREMENT,
    reparacion_id INT,
    monto DOUBLE,
    fecha DATE,
    forma_pago ENUM('EFECTIVO', 'TRANSFERENCIA', 'TARJETA'),
    tipo_pago ENUM('SENA', 'PAGO_TOTAL', 'SALDO'),
    recargo_porcentaje DOUBLE,
    anulado BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id),
    FOREIGN KEY (reparacion_id) REFERENCES reparaciones(id)
    );

CREATE TABLE historial_estados (
	id INT AUTO_INCREMENT,
    reparacion_id INT,
    estado_reparacion ENUM('RECIBIDO', 'EN_REPARACION', 'ENTREGADO', 'CANCELADA'),
    fecha DATETIME,
    empleado_id INT,
    PRIMARY KEY (id),
    FOREIGN KEY (reparacion_id) REFERENCES reparaciones(id),
    FOREIGN KEY (empleado_id) REFERENCES empleados(id)
    );

-- TESTEOS

INSERT INTO clientes (nombre, apellido, tipo_documento, numero_documento, telefono, correo)
VALUES ('Juan', 'Perez', 'DNI', '30111222', '1122334455', 'juanpe@mail.com');

INSERT INTO empleados (nombre, cuit, rol_empleado, activo)
VALUES ('Ana Garcia', '20304050607', 'TECNICO', TRUE);

INSERT INTO dispositivos (cliente_id, tipo_equipo, marca, modelo, imei, numero_serie)
VALUES (1, 'CELULAR', 'Samsung', 'A22 4G', '123456789012345', 'A225M');

INSERT INTO reparaciones (dispositivo_id, empleado_id, estado_reparacion, falla_declarada, presupuesto, fecha_entrada)
VALUES (1, 1, "RECIBIDO", "No enciende", 50000.0, NOW());

INSERT INTO pagos (reparacion_id, monto, fecha, forma_pago, tipo_pago, recargo_porcentaje)
VALUES(1, 20000.0, CURDATE(), 'EFECTIVO', 'SENA', 0.0);

SELECT * FROM reparaciones;
SELECT * FROM pagos;

SELECT c.nombre, c.apellido, d.marca, d.modelo, r.falla_declarada, r.estado_reparacion
FROM clientes c
JOIN dispositivos d ON d.cliente_id = c.id
JOIN reparaciones r ON r.dispositivo_id = d.id;

-- Para crear el usuario de la aplicación (reemplazar la contraseña):
-- CREATE USER 'gestor_app'@'localhost' IDENTIFIED BY 'tu_contraseña_aqui';
-- GRANT ALL PRIVILEGES ON gestor_reparaciones.* TO 'gestor_app'@'localhost';
-- FLUSH PRIVILEGES;