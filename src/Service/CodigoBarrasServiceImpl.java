/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Dao.GenericDAO;
import Models.CodigoBarras;
import Models.Producto;
import java.util.List;

/**
 *
 * @author gast-n
 */
class CodigoBarrasServiceImpl implements GenericService<CodigoBarras> {
    private final GenericDAO<CodigoBarras> codigoBarrasDAO;
    public CodigoBarrasServiceImpl(GenericDAO<CodigoBarras> codigoBarrasDAO) {
        if (codigoBarrasDAO == null) {
            throw new IllegalArgumentException("CodigoBarras no puede ser null");
        }
        this.codigoBarrasDAO = codigoBarrasDAO;
    }
    

    @Override
    public void insertar(CodigoBarras entidad) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizar(CodigoBarras entidad) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminar(int id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public CodigoBarras getById(int id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CodigoBarras> getAll() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
