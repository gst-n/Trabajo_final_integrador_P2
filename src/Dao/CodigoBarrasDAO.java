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

    private static final String INSERT_SQL = "INSERT INTO CodigoBarras (tipo, fechaAsignacion, observaciones, producto_id) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE CodigoBarras SET tipo = ?, fechaAsignacion = ?, observaciones = ?, producto_id = ? WHERE id = ?";
    private static final String DELETE_SQL = "UPDATE codigosBarras SET eliminado = TRUE WHERE id = ?";

    private static final String SELECT_BY_ID_SQL = "SELECT * FROM codigos_barras WHERE id = ? AND eliminado = FALSE";

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
            stmt.setInt(5, codigoBarras.getId());
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
    stmt.setTimestamp(2, new java.sql.Timestamp(codigoBarras.getFechaAsignacion().getTime()));
    stmt.setString(3, codigoBarras.getObservaciones());
    stmt.setInt(4, codigoBarras.getProductoId());
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
