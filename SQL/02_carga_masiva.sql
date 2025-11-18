-- Insercion de datos en la tabla Producto;
INSERT INTO Producto (nombre, marca, categoria, precio) VALUES
('Notebook ', 'Asus', 'Electrónica', 149999.99);

SELECT * FROM Producto;

-- Insercion de datos en la tabla CodigoBarras;
INSERT INTO CodigoBarras (producto_id, tipo, fechaAsignacion, observaciones) VALUES
(1, 'EAN-13', NOW(), 'Producto destacado');

SELECT * FROM CodigoBarras;

INSERT INTO Producto (nombre, marca, categoria, precio) VALUES
('Café Molido', 'NEscafe', 'Alimentos', 18500.00);
INSERT INTO CodigoBarras (producto_id, tipo, fechaAsignacion, observaciones) VALUES
(2, 'UPC-A', '2025-10-15 10:30:00', 'Para venta minorista en Argentina');
INSERT INTO Producto (nombre, marca, categoria, precio) VALUES
('Cien Años de Soledad', 'DeBolsillo', 'Libros', 15255.0);
INSERT INTO CodigoBarras (producto_id, tipo, fechaAsignacion, observaciones) VALUES
(last_insert_id(), 'ISBN-13', NOW(), 'Libro del autor ganador de un premio Nobel');

-- para insertar datos masivos desde un archivo CSV primero crearemos una tabla temporal 

CREATE TEMPORARY TABLE IF NOT EXISTS tmp_carga_datos (
    CodigoBarras_id INT,
    nombre VARCHAR(120),
    categoria VARCHAR(80),
    precio DOUBLE,
    marca VARCHAR(80)
);
-- insertamos los datos haciendo click derecho sobre la base de datos TPFI_BaseDeDatos e importando el archivo
-- hacemos la insercion de los datos de una tabla a la otra

INSERT INTO Producto (nombre, marca, categoria, precio)
SELECT nombre, marca, categoria, precio
FROM tmp_carga_datos;


-- ahora realizamos el ingreso de los codigos de barras respectvos
INSERT INTO CodigoBarras (producto_id, eliminado, tipo, fechaAsignacion, observaciones)
SELECT
    p.id AS producto_id,
    0,
    'Producto variado',          
    NOW(),               
    NULL                 
FROM
    tmp_carga_datos t
JOIN
    Producto p ON
        t.nombre = p.nombre AND
        t.categoria = p.categoria AND
        t.marca = p.marca;
        
        
-- Una vez que cargamos todos los datos en las respectivas tablas, eliminamos la tabla temporal tmp_carga_datos
DROP TABLE tmp_carga_datos;

