/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Config.DatabaseConnection;
import Models.CodigoBarras;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gast-n
 */
public class CodigoBarrasDAO implements GenericDAO<CodigoBarras> {
    
    // --- Queries SQL de CodigoBarras ---

    /**
     * Query de inserción de código de barras.
     * Inserta tipo, fecha_asignacion y observaciones.
     */
    private static final String INSERT_SQL = "INSERT INTO codigos_barras (tipo, fecha_asignacion, observaciones) VALUES (?, ?, ?)";

    /**
     * Query de actualización de código de barras por id.
     * Actualiza tipo, fecha_asignacion y observaciones.
     */
    private static final String UPDATE_SQL = "UPDATE codigos_barras SET tipo = ?, fecha_asignacion = ?, observaciones = ? WHERE id = ?";

    /**
     * Query de soft delete.
     * Marca eliminado=TRUE sin borrar físicamente la fila.
     */
    private static final String DELETE_SQL = "UPDATE codigos_barras SET eliminado = TRUE WHERE id = ?";

    /**
     * Query para obtener código de barras por ID.
     * Solo retorna códigos activos (eliminado=FALSE).
     */
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM codigos_barras WHERE id = ? AND eliminado = FALSE";

    /**
     * Query para obtener todos los códigos de barras activos.
     */
    private static final String SELECT_ALL_SQL = "SELECT * FROM codigos_barras WHERE eliminado = FALSE";

    // --- Implementación de GenericDAO<CodigoBarras> ---

    @Override
    public void insertar(CodigoBarras codigoBarras) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setCodigoBarrasParameters(stmt, codigoBarras);
            stmt.executeUpdate();

            setGeneratedId(stmt, codigoBarras);
        }
    }

    @Override
    public void insertTx(CodigoBarras codigoBarras, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setCodigoBarrasParameters(stmt, codigoBarras);
            stmt.executeUpdate();
            setGeneratedId(stmt, codigoBarras);
        }
    }

    @Override
    public void actualizar(CodigoBarras codigoBarras) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            setCodigoBarrasParameters(stmt, codigoBarras);
            stmt.setInt(4, codigoBarras.getId()); // El ID es el 4to parámetro en UPDATE

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo actualizar el código de barras con ID: " + codigoBarras.getId());
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
                throw new SQLException("No se encontró código de barras con ID: " + id);
            }
        }
    }

    @Override
    public CodigoBarras getById(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCodigoBarras(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<CodigoBarras> getAll() throws Exception {
        List<CodigoBarras> codigos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                codigos.add(mapResultSetToCodigoBarras(rs));
            }
        }
        return codigos;
    }

    // --- Métodos Auxiliares Privados ---

    /**
     * Setea los parámetros de CodigoBarras en un PreparedStatement.
     * Adaptado para INSERT y la primera parte de UPDATE.
     */
private void setCodigoBarrasParameters(PreparedStatement stmt, CodigoBarras codigoBarras) throws SQLException {
    stmt.setString(1, codigoBarras.getTipo());
    
    // Mapeo de java.util.Date a java.sql.Timestamp para la BD
    if (codigoBarras.getFechaAsignacion() != null) {
        // Uso de java.sql.Timestamp para evitar el error de sombreado.
        stmt.setTimestamp(2, new java.sql.Timestamp(codigoBarras.getFechaAsignacion().getTime()));
    } else {
        // Uso de java.sql.Timestamp para evitar el error de sombreado.
        stmt.setTimestamp(2, new java.sql.Timestamp(new java.util.Date().getTime()));
        
        // 💡 Alternativa limpia si la columna fecha_asignacion es NULLABLE:
        // stmt.setNull(2, java.sql.Types.TIMESTAMP);
    }
    
    stmt.setString(3, codigoBarras.getObservaciones());
}

    /**
     * Obtiene el ID autogenerado por la BD después de un INSERT.
     */
    private void setGeneratedId(PreparedStatement stmt, CodigoBarras codigoBarras) throws SQLException {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                codigoBarras.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("La inserción del código de barras falló, no se obtuvo ID generado");
            }
        }
    }

    /**
     * Mapea un ResultSet a un objeto CodigoBarras.
     * Usa el constructor que se ajusta a la entidad proporcionada.
     */
    private CodigoBarras mapResultSetToCodigoBarras(ResultSet rs) throws SQLException {
        // Se utiliza el constructor de CodigoBarras(String tipo, String observaciones, int id, boolean eliminado)
        // proporcionado en CodigoBarras.java.
        // Se asume que la BD tiene una columna 'eliminado' (BOOLEAN) y 'fecha_asignacion' (TIMESTAMP/DATETIME).
        return new CodigoBarras(
            rs.getString("tipo"),
            rs.getString("observaciones"),
            rs.getInt("id"),
            rs.getBoolean("eliminado")
        );
    }
}
