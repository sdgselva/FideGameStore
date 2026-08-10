/*
    Este Script funciona para crear la base de datos de FideGameStore
*/

-- Sección de administración 
drop database if exists fidegamestore;
drop user if exists usuario_admin;
drop user if exists usuario_reportes;

-- Creación del esquema
CREATE database fidegamestore
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- Creación de usuarios con contraseñas
create user 'usuario_admin'@'%' identified by 'Admin123.';
create user 'usuario_reportes'@'%' identified by 'Reportes123.';

-- Asignación de permisos
-- Se otorgan permisos específicos en lugar de todos los permisos a todas las tablas futuras
grant select, insert, update, delete on fidegamestore.* to 'usuario_admin'@'%';
grant select on fidegamestore.* to 'usuario_reportes'@'%';
flush privileges;

use fidegamestore;

-- --- Sección de Creación de Tablas ---

-- Tabla de categorías
create table categoria (
  id_categoria INT NOT NULL AUTO_INCREMENT,
  categoria VARCHAR(50) NOT NULL,
  activo boolean,
  PRIMARY KEY (id_categoria),
  unique (categoria),
  index ndx_categoria (categoria))
  ENGINE = InnoDB;

-- Tabla de roles
create table rol (
  id_rol INT NOT NULL AUTO_INCREMENT,
  rol VARCHAR(50) NOT NULL,
  activo boolean,
  PRIMARY KEY (id_rol),
  unique (rol),
  index ndx_rol (rol))
  ENGINE = InnoDB;

-- Tabla de plataformas
create table plataforma (
  id_plataforma INT NOT NULL AUTO_INCREMENT,
  plataforma VARCHAR(50) NOT NULL,
  activo boolean,
  PRIMARY KEY (id_plataforma),
  unique (plataforma),
  index ndx_plataforma (plataforma))
  ENGINE = InnoDB;

-- Tabla de regiones
create table region (
  id_region INT NOT NULL AUTO_INCREMENT,
  region VARCHAR(50) NOT NULL,
  activo boolean,
  PRIMARY KEY (id_region),
  unique (region),
  index ndx_region (region))
  ENGINE = InnoDB;

-- Tabla de estatus de tickets
create table estado_ticket (
  id_estado_ticket INT NOT NULL AUTO_INCREMENT,
  estado_ticket VARCHAR(50) NOT NULL,
  activo boolean,
  PRIMARY KEY (id_estado_ticket),
  unique (estado_ticket),
  index ndx_estado_ticket (estado_ticket))
  ENGINE = InnoDB;

-- Tabla de estado de ordenes
create table estado_orden (
  id_estado_orden INT NOT NULL AUTO_INCREMENT,
  estado_orden VARCHAR(50) NOT NULL,
  activo boolean,
  PRIMARY KEY (id_estado_orden),
  unique (estado_orden),
  index ndx_estado_orden (estado_orden))
  ENGINE = InnoDB;

-- Tabla de usuarios
CREATE TABLE usuario (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  username varchar(30) NOT NULL UNIQUE,
  password varchar(512) NOT NULL,
  correo VARCHAR(75) NULL UNIQUE,
  ruta_imagen varchar(1024),
  activo boolean,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`),
  CHECK (correo REGEXP '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$'),
  index ndx_username (username))
  ENGINE = InnoDB;

-- Tabla de productos
create table producto (
  id_producto INT NOT NULL AUTO_INCREMENT,
  id_categoria INT NOT NULL,
  nombre VARCHAR(50) NOT NULL,  
  ruta_imagen varchar(1024),
  activo boolean,
  PRIMARY KEY (id_producto),
  unique (nombre),
  index ndx_nombre (nombre),
  foreign key fk_producto_categoria (id_categoria) references categoria(id_categoria))
  ENGINE = InnoDB;

-- Tabla de variantes de productos
create table variante_producto (
  id_variante_producto INT NOT NULL AUTO_INCREMENT,
  id_producto INT NOT NULL,
  id_region INT NOT NULL,
  id_plataforma INT NOT NULL,
  PRIMARY KEY (id_variante_producto),
  foreign key fk_variante_producto_producto (id_producto) references producto(id_producto),
  foreign key fk_variante_producto_region (id_region) references region(id_region),
  foreign key fk_variante_producto_plataforma (id_plataforma) references plataforma(id_plataforma))
  ENGINE = InnoDB;

-- Tabla de llaves
create table llave (
  id_llave INT NOT NULL AUTO_INCREMENT,
  llave VARCHAR(15) NOT NULL,  
  activo boolean,
  id_variante_producto INT NOT NULL,
  PRIMARY KEY (id_llave),
  unique (llave),
  foreign key fk_llave_variante_producto (id_variante_producto) references variante_producto(id_variante_producto))
  ENGINE = InnoDB;

-- Tabla de anuncios
CREATE TABLE anuncio (
  id_anuncio INT NOT NULL AUTO_INCREMENT,
  id_variante_producto INT NOT NULL,
  precio decimal(12,2) check (precio>= 0),
  activo boolean,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_anuncio`),
  foreign key fk_anuncio_variante_producto (id_variante_producto) references variante_producto(id_variante_producto))
  ENGINE = InnoDB;

-- Tabla de ordenes
CREATE TABLE orden (
  id_orden INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  id_estado_orden INT NOT NULL,
  precio_total decimal(12,2) check (precio_total>= 0),
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_orden`),
  foreign key fk_orden_usuario (id_usuario) references usuario(id_usuario),
  foreign key fk_orden_estado_orden (id_estado_orden) references estado_orden(id_estado_orden))
  ENGINE = InnoDB;

-- Tabla de llaves por orden
CREATE TABLE orden_llave (
  id_orden_llave INT NOT NULL AUTO_INCREMENT,
  id_orden INT NOT NULL,
  id_llave INT NOT NULL,
  precio_pagado decimal(12,2) check (precio_pagado>= 0),
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_orden_llave`),
  foreign key fk_orden_llave_orden (id_orden) references orden(id_orden),
  foreign key fk_orden_llave_llave (id_llave) references llave(id_llave))
  ENGINE = InnoDB;

-- Tabla de tickets
CREATE TABLE ticket (
  id_ticket INT NOT NULL AUTO_INCREMENT,
  id_orden INT NOT NULL,
  id_estado_ticket INT NOT NULL,
  descripcion VARCHAR(300) NOT NULL,  
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_ticket`),
  foreign key fk_ticket_orden (id_orden) references orden(id_orden),
  foreign key fk_ticket_estado_ticket (id_estado_ticket) references estado_ticket(id_estado_ticket))
  ENGINE = InnoDB;


-- --- Sección de Inserción de Datos ---


INSERT INTO categoria (categoria, activo) VALUES 
('Juego',true),
('Tarjeta de Regalo',true);

INSERT INTO rol (rol, activo) VALUES 
('Administrador',true),
('Soporte',true),
('Usuario',true);

INSERT INTO plataforma (plataforma, activo) VALUES 
('Todas las plataformas',true),
('Nintendo',true),
('Steam',true),
('PlayStation',true),
('Xbox',true);

INSERT INTO region (region, activo) VALUES 
('Global',true),
('Norteamerica',true),
('Suramerica',true),
('Europa',true),
('Asia',true);

INSERT INTO estado_ticket (estado_ticket, activo) VALUES 
('Abierto',true),
('Cerrado',true);

INSERT INTO usuario (username, password, correo, ruta_imagen, activo) VALUES 
('Admin', '123', 'admin@fidegamestore.com', 'imagen', true),
('Soporte', '123', 'soporte@fidegamestore.com', 'imagen', true),
('usuario1', '123', 'usuario@gmail.com', 'imagen', true);

INSERT INTO producto (id_categoria, nombre, ruta_imagen, activo) VALUES
(1, 'Minecraft', 'imagen', true),
(2, 'PSN $10 Gift Card', 'imagen', true),
(1, 'Stardew Valley', 'imagen', true),
(1, 'Doom Eternal', 'imagen', true),
(2, 'Steam Wallet $50 Gift Card', 'imagen', true);

INSERT INTO variante_producto (id_producto, id_region, id_plataforma) VALUES
(1, 1, 5),
(3, 2, 2),
(2, 2, 1),
(4, 3, 2),
(2, 2, 1);

INSERT INTO llave (llave, id_variante_producto, activo) VALUES
('AAAA-AAAA-AAAA', 1, true),
('BBBB-BBBB-BBBB', 2, true),
('CCCC-CCCC-CCCC', 3, true),
('DDDD-DDDD-DDDD', 4, false),
('EEEE-EEEE-EEEE', 5, false);

INSERT INTO anuncio (id_variante_producto, precio, activo) VALUES
(1, 55.50, true),
(2, 10.00, true),
(3, 30.00, true),
(4, 59.90, true);

INSERT INTO estado_orden (estado_orden, activo) VALUES
('Completada', true),
('En disputa', true),
('Reembolsada', true);

INSERT INTO orden (id_usuario, id_estado_orden, precio_total) VALUES
(3, 1, 60.00),
(3, 2, 10.00);

INSERT INTO orden_llave (id_orden, id_llave, precio_pagado) VALUES
(2, 5, 60.00);

INSERT INTO ticket (id_orden, id_estado_ticket, descripcion) VALUES
(2, 1, 'Intente usar la tarjeta de regalo en mi consola de Play Station pero no funciono');
