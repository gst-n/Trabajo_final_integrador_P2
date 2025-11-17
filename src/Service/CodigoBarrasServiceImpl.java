/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Dao.GenericDAO;
import Models.CodigoBarras;
import java.util.List;

/**
 *
 * @author gast-n
 */
public class CodigoBarrasServiceImpl implements GenericService<CodigoBarras> {
    
    private final GenericDAO<CodigoBarras> codigoBarrasDAO;
    
    public CodigoBarrasServiceImpl(GenericDAO<CodigoBarras> codigoBarrasDAO) {
        if (codigoBarrasDAO == null) {
            throw new IllegalArgumentException("CodigoBarras no puede ser null");
        }
        this.codigoBarrasDAO = codigoBarrasDAO;
    }
    

    @Override
    public void insertar(CodigoBarras codigoBarras) throws Exception {
        validateCodigoBarras(codigoBarras);
        codigoBarrasDAO.insertar(codigoBarras);
    }

    @Override
    public void actualizar(CodigoBarras codigo) throws Exception {
        validateCodigoBarras(codigo);
        if (codigo.getId()<=0){
            throw new UnsupportedOperationException("Not supported yet."); 
        }
        codigoBarrasDAO.actualizar(codigo);
    }

    @Override
    public void eliminar(int id) throws Exception {
        if(id<=0){
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
        codigoBarrasDAO.eliminar(id);
    }

    @Override
    public CodigoBarras getById(int id) throws Exception {
        if(id<=0){
            throw new UnsupportedOperationException("Not supported yet."); // 
        }
        return codigoBarrasDAO.getById(id);
    }

    @Override
    public List<CodigoBarras> getAll() throws Exception {
        return codigoBarrasDAO.getAll();
    }
    
    private void validateCodigoBarras(CodigoBarras codigoB) {
        if (codigoB == null) {
            throw new IllegalArgumentException("El codigo no puede ser null");
        }
        if (codigoB.getFechaAsignacion() == null) {
            throw new IllegalArgumentException("La fecha no puede estar vacía");
        }
        if (codigoB.getTipo()== null) {
            throw new IllegalArgumentException("El tipo de codigo no puede estar vacío");
        }
    }
    
    public void insertTx(CodigoBarras codigoBarras, java.sql.Connection conn) throws Exception {
    validateCodigoBarras(codigoBarras);
    codigoBarrasDAO.insertTx(codigoBarras, conn); 
}
    
}
