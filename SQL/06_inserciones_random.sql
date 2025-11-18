-- ingreso de datos random mediante DELIMITER CREATE PROCEDURE
DELIMITER $$
CREATE PROCEDURE InsertarDatosAleatorios(IN num_registros INT)
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE random_id_producto INT;
    DECLARE random_nombre VARCHAR(120);
    DECLARE random_categoria VARCHAR(80);
    DECLARE random_marca VARCHAR(80);
    DECLARE random_precio DOUBLE;

    -- Tablas de valores predefinidos (para mayor realismo)
    -- Generar IDs aleatorios para estos arrays
    
    SET @NOMBRES = 'Laptop,Smartphone,Mouse,Teclado,Monitor,Cafetera,Tostadora,Licuadora,Libro,Cuaderno';
    SET @MARCAS = 'Samsung,Apple,Logitech,Dell,HP,Oster,Philips,Bosch,RandomHouse,3M';
    SET @CATEGORIAS = 'Electrónica,Hogar,Oficina,Alimentos,Libros';

    WHILE i < num_registros DO
        -- 1. Generar datos aleatorios
        SET random_nombre = SUBSTRING_INDEX(SUBSTRING_INDEX(@NOMBRES, ',', FLOOR(1 + (RAND() * (LENGTH(@NOMBRES) - LENGTH(REPLACE(@NOMBRES, ',', '')) + 1)))), ',', -1);
        SET random_marca = SUBSTRING_INDEX(SUBSTRING_INDEX(@MARCAS, ',', FLOOR(1 + (RAND() * (LENGTH(@MARCAS) - LENGTH(REPLACE(@MARCAS, ',', '')) + 1)))), ',', -1);
        SET random_categoria = SUBSTRING_INDEX(SUBSTRING_INDEX(@CATEGORIAS, ',', FLOOR(1 + (RAND() * (LENGTH(@CATEGORIAS) - LENGTH(REPLACE(@CATEGORIAS, ',', '')) + 1)))), ',', -1);
        -- Precio aleatorio entre 1 y 500 (con dos decimales)
        SET random_precio = ROUND(1 + (RAND() * 1499), 2);

        -- 2. Insertar en Producto
        -- El argumento 'id'  no se añade ya que se auto asigna mediante AUTO-INCREMENT o LAST_INSERT_ID
        INSERT INTO Producto (eliminado, nombre, marca, categoria, precio) VALUES
        (
            0,
            CONCAT(random_nombre, ' - ', i), -- Añadimos 'i' para garantizar nombres únicos
            random_marca,
            random_categoria,
            random_precio
        );

        -- 3. Obtener el ID recién insertado (Clave Primaria)
        SET random_id_producto = LAST_INSERT_ID();

        -- 4. Insertar en CodigoBarras usando el ID (Clave Foránea)
        INSERT INTO CodigoBarras (eliminado, tipo, fechaAsignacion, observaciones, producto_id) VALUES
        -- producto_id, tipo, fechaAsignacion, observaciones, eliminado
        (
            0,
            'EAN-13-Producto-General', -- Tipo fijo para simplicidad
            NOW() - INTERVAL FLOOR(RAND() * 365) DAY, -- Fecha aleatoria en el último año
            CONCAT('Lote numero- ', random_id_producto), -- Observacion que indica el numero de lote y se concatena con el id del producto.
            random_id_producto
        );
        
        SET i = i + 1;
    END WHILE;
END$$

DELIMITER ;

-- DROP PROCEDURE InsertarDatosAleatorios;
-- tuvimos problemas con la primera creacion de la sentencia PROCEDURE, por ello utilizamos DROP y creamos nuevamente.
CALL InsertarDatosAleatorios(1000);


-- Consultas realizadas para la demostración.
-- buscamos codigos de barras con id = null
SELECT
    COUNT(C.id) AS CodigosBarrasHuerfanos
FROM
    CodigoBarras C
LEFT JOIN
    Producto P ON C.producto_id = P.id
WHERE
    P.id IS NULL;
    
    -- validamos cardinalidades
-- Busca productos que no tienen ninguna entrada en CodigoBarras
SELECT
    COUNT(P.id) AS ProductosSinCodigos
FROM
    Producto P
LEFT JOIN
    CodigoBarras C ON P.id = C.producto_id
WHERE
    C.id IS NULL;

-- Conteo y validacion de Dominio
SELECT
    COUNT(id) AS TotalProductos,
    MIN(precio) AS PrecioMinimo,
    MAX(precio) AS PrecioMaximo
FROM
    Producto;
    -- verifica el conteo total de registros para confirmar el volumen (cercano a 1000) y,
    -- simultáneamente, comprueba 
    -- si el rango de precios aleatorios generado cumple con la restricción del procedimiento 
    -- (precios entre 1 y 1499).
