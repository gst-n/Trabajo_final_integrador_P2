/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Models.CodigoBarras;
import Models.Producto;
import Service.ProductoServiceImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author gast-n
 */
public class MenuHandlerMain {
    private final Scanner scanner;
    private final ProductoServiceImpl productoServiceImpl;

    public MenuHandlerMain(Scanner scanner, ProductoServiceImpl productoServiceImpl) {
        if (scanner == null){
            throw new IllegalArgumentException();
        }
        if (productoServiceImpl == null){
            throw new IllegalArgumentException();
        }
        this.scanner = scanner;
        this.productoServiceImpl = productoServiceImpl;
    }

    
    /** fin handler
     * @throws java.lang.Exception **/
        
    public void crearProducto() throws Exception {
        try {
            System.out.println("Nombre: ");
            String nombre = scanner.nextLine().trim();
            System.out.println("Marca: ");
            String marca = scanner.nextLine().trim();
            System.out.println("Categoria: ");
            String categoria = scanner.nextLine().trim();
            System.out.println("Precio: ");
            double precio = Integer.parseInt(scanner.nextLine().trim());

            CodigoBarras codigoBarras = null;
            System.out.println("¿Quiere ingresar un codigo de barras? ingrese s para ingresar uno, o n para continuar: ");
            if (scanner.nextLine().equalsIgnoreCase("s")){
                codigoBarras = crearCodigoBarras();
            }

            Producto producto = new Producto(nombre, marca, categoria, precio);
            producto.setCodigoBarras(codigoBarras);
            productoServiceImpl.insertar(producto);
            System.out.println("Producto creado correctamente");
        }
        catch (Exception e){
            System.err.println("Error al crear el producto");
        }
    }
    private CodigoBarras crearCodigoBarras() throws ParseException {
        System.out.println("Tipo: ");
        String tipo = scanner.nextLine();
        System.out.println("Fecha: ");
        String fecha = scanner.nextLine();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaString = formato.parse(fecha);
        System.out.println("Observacion: ");
        String observacion = scanner.nextLine();
        return new CodigoBarras(tipo, fechaString, observacion);
    }
    
    public void listarProductos(){
        try{
            System.out.println("Presione 1 para listar todos los productos, o (2) para buscar un producto por nombre/marca: ");
            int subOpcion = Integer.parseInt(scanner.nextLine());
            List<Producto> productos;
            if (subOpcion == 1){
                productos = productoServiceImpl.getAll();
            } else if (subOpcion == 2){
                System.out.println("Ingrese Nombre o marca del producto");
                String filtro = scanner.nextLine().trim();
                productos = productoServiceImpl.buscarPorNombreOMarca(filtro);
            } else {
                System.out.println("Opcion invalida");
                return;
            }
            if (productos.isEmpty()){
                System.out.println("No se encontraron productos con la referencia ingresada");
                return;
            }
            
            for (Producto producto : productos) {
                System.out.println("ID: "+ producto.getId()+" Nombre: "+
                        producto.getNombre() + " Marca: "+producto.getMarca() +
                        " Categoria: "+producto.getCategoria() + " Precio: "
                        +producto.getPrecio());
                
                if (producto.getCodigoBarras() != null){
                    System.out.println(" Codigo de Barras: " + producto.getCodigoBarras());
                    
                }
            }
        }
        catch (Exception e){
            System.err.println("Error al listar el/los producto(s) " + e.getMessage());
            
        }
    }
    
    public void actualizarProducto(){
        try {
            System.out.println("ID del producto que desea actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            Producto productoActualizado = productoServiceImpl.getById(id);
            
            if(productoActualizado == null){
                System.out.println("Producto no encontrado");
                return;
            }
            
            System.out.println("Nuevo nombre (nombre actual: "+ productoActualizado.getNombre()+ ", Enter para mantenerlo): " );
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()){
                productoActualizado.setNombre(nombre);
            }
            
            System.out.println("Nueva Marca (marca actual: "+ productoActualizado.getMarca()+ ", Enter para mantenerlo): " );
            String marca = scanner.nextLine().trim();
            if (!marca.isEmpty()){
                productoActualizado.setMarca(marca);
            }
            
            System.out.println("Nueva categoria (categoria actual: "+ productoActualizado.getCategoria()+ ", Enter para mantenerlo): " );
            String categoria = scanner.nextLine().trim();
            if (!marca.isEmpty()){
                productoActualizado.setMarca(categoria);
            }
            
            System.out.println("Nuevo precio (precio actual: "+ productoActualizado.getPrecio()+ ", Enter para mantenerlo): " );
            double precio = Double.parseDouble(scanner.nextLine().trim());
            if (!marca.isEmpty()){
                productoActualizado.setPrecio(precio);
            }
            
            
        }
        catch (Exception e){
            System.err.println("Error al actualizar el producto. " + e.getMessage());
        }
    }
    
    public void eliminarProducto(){
        try{
            System.out.println("ID del producto que desea eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());
            productoServiceImpl.eliminar(id);
            System.out.println("Producto eliminado correctamente.");
        }
        catch(Exception e){
            System.err.println("Error al eliminar el producto");
        }
    }
    
    public void crearCodigoBarrasIndependiente() {
        try{
            CodigoBarras codigoBarras = crearCodigoBarras();
            productoServiceImpl.getCodigoBarrasService().insertar(codigoBarras);
            System.out.println("Codigo de barras creado correctamente.");
        }   
        catch(Exception e){
            System.err.println("Error al crear el codigo de barras." +e.getMessage());
        }
    }
    
    public void listarCodigoBarras(){
        try{
            List<CodigoBarras> codigos = productoServiceImpl.getCodigoBarrasService().getAll();
            if (codigos.isEmpty()){
                System.out.println("No se encontraron codigos de barra asociados");
                return;
            }
            for (CodigoBarras d : codigos){
                System.out.println("ID: "+d.getId() + " ,"+d.getObservaciones());
            }
        }
        catch(Exception e){
            System.out.println("Error al listar los codigos de Barras");
        }
    }
    
    public void actualizarCodigoPorId() throws Exception{
        try{
            System.out.println("ID del Codigo de barras a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            CodigoBarras codigo = productoServiceImpl.getCodigoBarrasService().getById(id);
            
            if (codigo == null){
                System.out.println("Codigo de barras no debe ser nulo");
                return;
            }
            
            System.out.println("Nuevo tipo de codigo (actual: "+ codigo.getTipo() +", Enter para mantener): ");
            String tipo = scanner.nextLine().trim();
            if(!tipo.isEmpty()){
                codigo.setTipo(tipo);
            }
            productoServiceImpl.getCodigoBarrasService().actualizar(codigo);
            System.out.println("Codigo de barras actualizado correctamente.");
            
        }
        catch (Exception e){
            System.err.println("Error al actualizar codigo de barras." + e.getMessage());
        }
    }
    
    public void eliminarCodigoBarrasPorId(){
        try{
            System.out.println("ID del codigo de barras que desea eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());
            productoServiceImpl.getCodigoBarrasService().eliminar(id);
            System.out.println("Codigo de barras eliminado correctamente.");
            
        }
        catch(Exception e){
            System.out.println("Error al eliminar el codigo de barras. "+e.getMessage());
            
        }
    }
    
    public void actualizarCodigoBarrasPorProducto(){
        try{
            System.out.println("ID del codigo de barras que desea actualizar: ");
            int productoId = Integer.parseInt(scanner.nextLine());
            Producto p = productoServiceImpl.getById(productoId);
            
            if (p == null){
                System.out.println("Producto no encontrado");
                return;
                
            }
            
            if (p.getCodigoBarras() == null){
                System.out.println("El producto no tiene codigo de barras asociado");
                return;
            }
            CodigoBarras codigo = p.getCodigoBarras();
            System.out.println("Nuevo tipo de codigo de barras: ");
            String tipo = scanner.nextLine();
            if(!tipo.isEmpty()){
                codigo.setTipo(tipo);
            }
            
            System.out.println("Nueva fecha de codigo de barras: ");
            String fecha = scanner.nextLine();
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    
            if(!fecha.isEmpty()){
                codigo.setFechaAsignacion(formato.parse(fecha));
            }
            
            productoServiceImpl.getCodigoBarrasService().actualizar(codigo);
            System.out.println("Codigo de barras del producto: "+p+" actualizado.");
            
        }
        catch (Exception e){
            System.err.println("Error al actualziar el producto"+e.getMessage());
            
        }
    }
    
    public void eliminarCodigoBarrasPorProducto(){
       try{
            System.out.println("ID del codigo de barras que desea eliminar: ");
            int productoId = Integer.parseInt(scanner.nextLine());
            Producto producto = productoServiceImpl.getById(productoId);
            
            if(producto == null){
                System.out.println("Producto no encontrado");
                return;
            }
            
            if(producto.getCodigoBarras() == null){
                System.out.println("El producto no tiene codigo de barras asociado");
                return;
            }
            
            int codigoId = producto.getCodigoBarras().getId();
            productoServiceImpl.eliminarCodigoBarrasProducto(productoId, codigoId);
            System.out.println("Codigo de barras eliminado correctamente. ");
            
       }
       catch(Exception e){
           System.err.println("Error al eliminar el codigo de Barras"+e.getMessage());
       }
    }
    
// Fin MAIN HANDLER;;;
}
    



    