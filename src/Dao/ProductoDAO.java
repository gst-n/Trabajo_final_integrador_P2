/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Config.DatabaseConnection;
import Models.CodigoBarras;
import Models.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gast-n
 */
public class ProductoDAO implements GenericDAO<Producto> {

    private static final String INSERT_SQL = "INSERT INTO Producto (nombre, marca, categoria, precio) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE Producto SET nombre = ?, marca = ?, categoria = ?, precio = ? WHERE id = ?";
    private static final String DELETE_SQL = "UPDATE Producto SET eliminado = TRUE WHERE id = ?";
    
    //revisar sintaxis SQL "SELECT " + SELECT_BASE_FIELDS 
    
private static final String SELECT_BASE_FIELDS = 
    "p.id, p.nombre, p.marca, p.categoria, p.precio, p.eliminado, " +
    "cb.id AS cb_id, cb.tipo, cb.fechaAsignacion, cb.observaciones ";

private static final String SELECT_BY_ID_SQL = "SELECT " + SELECT_BASE_FIELDS +
    " FROM Producto p LEFT JOIN CodigoBarras cb ON p.id = cb.producto_id " +
    " WHERE p.id = ? AND p.eliminado = FALSE";

private static final String SELECT_ALL_SQL = "SELECT " + SELECT_BASE_FIELDS +
    " FROM Producto p LEFT JOIN CodigoBarras cb ON p.id = cb.producto_id " +
    " WHERE p.eliminado = FALSE";

    // Búsqueda flexible por nombre o marca (LIKE)
private static final String SEARCH_BY_NAME_MARK_SQL = "SELECT " + SELECT_BASE_FIELDS +
    " FROM Producto p LEFT JOIN CodigoBarras cb ON p.id = cb.producto_id " +
    " WHERE p.eliminado = FALSE AND (p.nombre LIKE ? OR p.marca LIKE ?)";

    // --- Dependencias (similar a DomicilioDAO) ---

    private final CodigoBarrasDAO codigoBarrasDAO;

    public ProductoDAO(CodigoBarrasDAO codigoBarrasDAO) {
        if (codigoBarrasDAO == null) {
            throw new IllegalArgumentException("CodigoBarrasDAO no puede ser null");
        }
        this.codigoBarrasDAO = codigoBarrasDAO;
    }

    // --- Implementación de GenericDAO<Producto> ---

    @Override
    public void insertar(Producto producto) throws Exception {
    try (Connection conn = DatabaseConnection.getConnection();
         // No se pasa Statement.RETURN_GENERATED_KEYS ya que es el default en algunos drivers
         PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

        setProductoParameters(stmt, producto); // 4 parámetros
        stmt.executeUpdate();
        setGeneratedId(stmt, producto);

        // Si el producto tiene un CodigoBarras, se debe insertar AHORA con el nuevo ID del Producto.
        if (producto.getCodigoBarras() != null) {
            producto.getCodigoBarras().setProductoId(producto.getId());
            codigoBarrasDAO.insertTx(producto.getCodigoBarras(), conn); // Usar insertTx si estamos en transacción
        }
    } // Nota: El código anterior estaba fuera de una transacción. Si requiere transacciones, debe usarse un TransactionManager.
}

@Override
public void insertTx(Producto producto, Connection conn) throws Exception {
    try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
        setProductoParameters(stmt, producto); // 4 parámetros
        stmt.executeUpdate();
        setGeneratedId(stmt, producto);

        // Si el producto tiene un CodigoBarras, se debe insertar AHORA con el nuevo ID del Producto.
        if (producto.getCodigoBarras() != null) {
            producto.getCodigoBarras().setProductoId(producto.getId());
            codigoBarrasDAO.insertTx(producto.getCodigoBarras(), conn);
        }
    }
}

//    @Override
//    public void actualizar(Producto producto) throws Exception {
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
//
//            stmt.setString(1, producto.getNombre());
//            stmt.setString(2, producto.getMarca());
//            stmt.setString(3, producto.getCategoria());
//            stmt.setDouble(4, producto.getPrecio());
//            setCodigoBarrasId(stmt, 5, producto.getCodigoBarras()); // Setea FK
//            stmt.setInt(6, producto.getId());
//
//            int rowsAffected = stmt.executeUpdate();
//            if (rowsAffected == 0) {
//                throw new SQLException("No se pudo actualizar el producto con ID: " + producto.getId());
//            }
//        }
//    }
// ProductoDAO.java

@Override
public void actualizar(Producto producto) throws Exception {
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

        // 1. Actualizar la tabla Producto (solo 5 parámetros)
        stmt.setString(1, producto.getNombre());
        stmt.setString(2, producto.getMarca());
        stmt.setString(3, producto.getCategoria());
        stmt.setDouble(4, producto.getPrecio());
        stmt.setInt(5, producto.getId()); // ID al final para la cláusula WHERE

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("No se pudo actualizar el producto con ID: " + producto.getId());
        }

        // 2. Actualizar/Insertar el CodigoBarras si está presente
        if (producto.getCodigoBarras() != null) {
            producto.getCodigoBarras().setProductoId(producto.getId());
            // Si tiene ID > 0, se actualiza; si es nuevo (ID=0), se inserta (se maneja en el servicio)
            if (producto.getCodigoBarras().getId() > 0) {
                codigoBarrasDAO.actualizar(producto.getCodigoBarras());
            } else {
                codigoBarrasDAO.insertar(producto.getCodigoBarras());
            }
        }
    }
}

    @Override
    public void eliminar(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("No se encontró producto con ID: " + id);
            }
        }
    }

    @Override
    public Producto getById(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProducto(rs);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener producto por ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Producto> getAll() throws Exception {
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener todos los productos: " + e.getMessage(), e);
        }
        return productos;
    }

    // --- Métodos de Búsqueda Específica ---

    /**
     * Busca productos por nombre o marca con búsqueda flexible (LIKE '%filtro%').
     * @param filtro Texto a buscar (nombre o marca)
     * @return Lista de productos que coinciden
     * @throws SQLException Si hay error de BD
     */
    public List<Producto> buscarPorNombreOMarca(String filtro) throws SQLException {
        if (filtro == null || filtro.trim().isEmpty()) {
            throw new IllegalArgumentException("El filtro de búsqueda no puede estar vacío");
        }

        List<Producto> productos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_NAME_MARK_SQL)) {

            String searchPattern = "%" + filtro + "%";
            stmt.setString(1, searchPattern); // p.nombre LIKE ?
            stmt.setString(2, searchPattern); // p.marca LIKE ?

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapResultSetToProducto(rs));
                }
            }
        }
        return productos;
    }

    // --- Métodos Auxiliares Privados ---

private void setProductoParameters(PreparedStatement stmt, Producto producto) throws SQLException {
    stmt.setString(1, producto.getNombre());
    stmt.setString(2, producto.getMarca());
    stmt.setString(3, producto.getCategoria());
    stmt.setDouble(4, producto.getPrecio());
    // Se elimina setCodigoBarrasId
}

    /**
     * Setea la FK codigo_barras_id. Maneja correctamente el caso NULL.
     */
//metodo en deshuso probablemente
//    private void setCodigoBarrasId(PreparedStatement stmt, int parameterIndex, CodigoBarras codigoBarras) throws SQLException {
//        if (codigoBarras != null && codigoBarras.getId() > 0) {
//            stmt.setInt(parameterIndex, codigoBarras.getId());
//        } else {
//            stmt.setNull(parameterIndex, Types.INTEGER);
//        }
//    }

    /**
     * Obtiene el ID autogenerado por la BD después de un INSERT.
     */
    private void setGeneratedId(PreparedStatement stmt, Producto producto) throws SQLException {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                producto.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("La inserción del producto falló, no se obtuvo ID generado");
            }
        }
    }

    /**
     * Mapea un ResultSet a un objeto Producto.
     * Reconstruye la relación con CodigoBarras usando LEFT JOIN.
     */
//    private Producto mapResultSetToProducto(ResultSet rs) throws SQLException {
//        Producto producto = new Producto();
//        producto.setId(rs.getInt("id"));
//        producto.setNombre(rs.getString("nombre"));
//        producto.setMarca(rs.getString("marca"));
//        producto.setCategoria(rs.getString("categoria"));
//        producto.setPrecio(rs.getDouble("precio"));
//
//        // Manejo correcto de LEFT JOIN: verificar si codigo_barras_id es NULL
//        int codigoBarrasId = rs.getInt("codigo_barras_id");
//        if (codigoBarrasId > 0 && !rs.wasNull()) {
//            CodigoBarras cb = new CodigoBarras();
//            // cb.id se lee como 'cb_id' gracias al alias en la query
//            cb.setId(rs.getInt("cb_id"));
//            cb.setTipo(rs.getString("tipo"));
//            cb.setFechaAsignacion(rs.getTimestamp("fecha_asignacion"));
//            cb.setObservaciones(rs.getString("observaciones"));
//            producto.setCodigoBarras(cb);
//        }
//
//        return producto;
//    }
    // ProductoDAO.java

/**
 * Mapea un ResultSet a un objeto Producto.
 * Reconstruye la relación con CodigoBarras usando LEFT JOIN.
 */
private Producto mapResultSetToProducto(ResultSet rs) throws SQLException {
    // Usar el constructor de Base(int id, boolean eliminado) y el constructor de Producto
    Producto producto = new Producto(
        rs.getInt("id"),
        rs.getString("nombre"),
        rs.getString("marca"),
        rs.getString("categoria"),
        rs.getDouble("precio")
    );

    // Mapeo del CodigoBarras
    int codigoBarrasId = rs.getInt("cb_id");
    if (codigoBarrasId > 0 && !rs.wasNull()) {
        CodigoBarras cb = new CodigoBarras(
            rs.getString("tipo"),
            rs.getString("observaciones"),
            codigoBarrasId, // cb.id (alias cb_id)
            false // Asumiendo que solo se traen códigos no eliminados
        );
        cb.setFechaAsignacion(rs.getTimestamp("fechaAsignacion")); // Se usa getTimestamp para mapear
        cb.setProductoId(producto.getId()); // Se setea la FK en el objeto CodigoBarras
        producto.setCodigoBarras(cb);
    }
    return producto;
    }
}
