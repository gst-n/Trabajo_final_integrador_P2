/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Dao.ProductoDAO;
import Models.Producto;
import java.util.List;

/**
 *
 * @author gast-n
 */
public class ProductoServiceImpl implements GenericService<Producto>{
    
    private final CodigoBarrasServiceImpl codigoBarrasServiceImpl;
    private final ProductoDAO productoDAO;

    public ProductoServiceImpl(ProductoDAO productoDAO, CodigoBarrasServiceImpl codigoBarrasServiceImpl) {
        if (productoDAO == null){
            throw new IllegalArgumentException("ProductoDAO no puede ser NULL");
        }
        this.productoDAO = productoDAO;
        this.codigoBarrasServiceImpl = codigoBarrasServiceImpl;
    }

    @Override
    public void insertar(Producto producto) throws Exception {
        validateProducto(producto);
        validateId(producto.getId());
        
        if (producto.getCodigoBarras()!= null){
            if(producto.getCodigoBarras().getId() == 0){
                codigoBarrasServiceImpl.insertar(producto.getCodigoBarras());
            } else {
                codigoBarrasServiceImpl.actualizar(producto.getCodigoBarras());
            }
        }
        productoDAO.insertar(producto);
    }

    @Override
    public void actualizar(Producto producto) throws Exception {
        validateProducto(producto);
        if (producto.getId() <= 0){
            throw new IllegalArgumentException("El id debe ser mayor a 0 para actualizar el producto"); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
        validateId(producto.getId());
        productoDAO.actualizar(producto);
    }

    @Override
    public void eliminar(int id) throws Exception {
        if(id <=0){
            throw new IllegalArgumentException("El id debe ser ayor a 0");
        }
        productoDAO.eliminar(id);
    }

    @Override
    public Producto getById(int id) throws Exception {
        if (id <= 0){
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
  
        }
        return productoDAO.getById(id);
    }

    @Override
    public List<Producto> getAll() throws Exception {
        return productoDAO.getAll();
    }
    
    private void validateProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (producto.getCategoria() == null || producto.getCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("La categoria no puede estar vacía");
        }
        if (producto.getPrecio() == 0) {
            throw new IllegalArgumentException("El DNI no puede estar vacío");
        }
    }
    
    private void validateId (int idProducto) throws Exception{
        Producto prodExistente = productoDAO.getById(idProducto);
        if(prodExistente != null){
            //existe un producto con este id
            if (prodExistente.getId() != idProducto ){
                throw new IllegalArgumentException("Ya existe un producto con este ID");
            }
        }
    }
    
    public CodigoBarrasServiceImpl getCodigoBarrasService(){
        return this.codigoBarrasServiceImpl;
    }
    
    public List<Producto> buscarPorNombreOMarca(String filtro) throws Exception {
    if (filtro == null || filtro.trim().isEmpty()) {
        throw new IllegalArgumentException("El filtro de búsqueda no puede estar vacío");
    }
    return productoDAO.buscarPorNombreOMarca(filtro);
    }
    
    public Producto buscarPorId(String id) throws Exception{
        if(id == null || id.trim().isEmpty()){
            throw new IllegalArgumentException("EL id ingresado no puede ser null");
        }
        return productoDAO.getById(Integer.parseInt(id));
    }
    
    public void eliminarCodigoBarrasProducto(int idProducto, int idCodigoBarras) throws Exception{
        if(idProducto <= 0 || idCodigoBarras <=0){
            throw new IllegalArgumentException("El id no puede ser menor a 0");
        }
        Producto producto = productoDAO.getById(idProducto);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrada con ID: " + idProducto);
        }
        
        if (producto.getCodigoBarras() == null || producto.getCodigoBarras().getId() != idCodigoBarras) {
            throw new IllegalArgumentException("El codigo de barras no pertenece a este producto");
        }
        producto.setCodigoBarras(null);
        productoDAO.actualizar(producto);
        codigoBarrasServiceImpl.eliminar(idProducto);
        
    }
    
}
