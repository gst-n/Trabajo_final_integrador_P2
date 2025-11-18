CREATE DATABASE IF NOT EXISTS TPFI_BaseDeDatos;

-- Seleccionams la base de datos para usarla
USE TPFI_BaseDeDatos;

-- tabla Producto
CREATE TABLE IF NOT EXISTS Producto (
    -- Clave Primaria: Identificador único para el producto
    id INT NOT NULL AUTO_INCREMENT,
    -- Borrado Lógico: 0 para activo, 1 para eliminado
    eliminado TINYINT(1) NOT NULL DEFAULT 0,
    nombre VARCHAR(120) NOT NULL,
    marca VARCHAR(80),
    categoria VARCHAR(80),
    precio DOUBLE,
    -- Restricción de Clave Primaria
    PRIMARY KEY (id),
    -- Índice para búsqueda rápida por nombre
    INDEX idx_nombre (nombre)
);

-- tabla CodigoBarras
CREATE TABLE IF NOT EXISTS CodigoBarras (
    -- Clave Primaria para el código de barras
    id INT NOT NULL AUTO_INCREMENT,
    -- Borrado Lógico: 0 para activo, 1 para eliminado
    eliminado TINYINT(1) NOT NULL DEFAULT 0,
    tipo VARCHAR(45) NOT NULL,
    -- Fecha y hora de asignación del código al producto
    fechaAsignacion DATETIME NOT NULL,
    observaciones VARCHAR(255),
    -- Clave Foránea que referencia el ID del producto
    producto_id INT NOT NULL,
    -- Restricción de Clave Primaria
    PRIMARY KEY (id),
    -- Restricción de Clave Foránea (Foreign Key)
    -- Establece la relación uno a muchos: CodigoBarras pertenece a Producto
    CONSTRAINT fk_codigoBarras_producto
        FOREIGN KEY (producto_id)
        REFERENCES Producto (id)
        -- Reglas de integridad referencial:
        -- ON DELETE RESTRICT: No permite borrar un producto si tiene códigos de barras asociados.
        -- ON UPDATE CASCADE: Si el ID del producto cambia, el producto_id en CodigoBarras también se actualiza.
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);