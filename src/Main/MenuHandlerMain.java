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
import java.util.ArrayList;
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

//            CodigoBarras codigoBarras = null;
//            System.out.println("¿Quiere ingresar un codigo de barras? ingrese s para ingresar uno, o n para continuar: ");
//            if (scanner.nextLine().equalsIgnoreCase("s")){
//                codigoBarras = crearCodigoBarras();
//            }

            Producto producto = new Producto(nombre, marca, categoria, precio);
//            producto.setCodigoBarras(codigoBarras);
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
            System.out.println("Presione 1 para listar todos los productos, o (2) para buscar un producto por nombre/marca, o (3) para buscar por ID: ");
            int subOpcion = Integer.parseInt(scanner.nextLine());
            List<Producto> productos;
            switch (subOpcion) {
                case 1:
                    productos = productoServiceImpl.getAll();
                    break;
                case 2:
                    System.out.println("Ingrese Nombre o marca del producto");
                    String filtro = scanner.nextLine().trim();
                    productos = productoServiceImpl.buscarPorNombreOMarca(filtro);
                    break;
                case 3: 
                    System.out.println("Ingrese el Id: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    Producto productoEncontrado = productoServiceImpl.getById(id);
                    productos = new ArrayList<>(); 
                    // Si se encontró el producto, agregarlo a la lista
                    if (productoEncontrado != null) {
                        productos.add(productoEncontrado);
                    }
                    break;
                default:
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
    
    public void actualizarProducto() {
    try {
        System.out.println("ID del producto que desea actualizar: ");
        // Usamos Integer.parseInt() y manejamos el error de formato en el catch
        int id = Integer.parseInt(scanner.nextLine()); 
        
        // 1. Obtener el producto existente
        Producto productoActualizado = productoServiceImpl.getById(id);
        
        if (productoActualizado == null) {
            System.out.println("Producto no encontrado con ID: " + id);
            return;
        }

        // --- 2. Solicitar y actualizar campos (Manejo de entrada) ---
        
        // Nombre
        System.out.println("Nuevo nombre (nombre actual: " + productoActualizado.getNombre() + ", Enter para mantenerlo): ");
        String nombre = scanner.nextLine().trim();
        if (!nombre.isEmpty()) {
            productoActualizado.setNombre(nombre);
        }

        // Marca
        System.out.println("Nueva Marca (marca actual: " + productoActualizado.getMarca() + ", Enter para mantenerlo): ");
        String marca = scanner.nextLine().trim();
        if (!marca.isEmpty()) {
            productoActualizado.setMarca(marca);
        }

        // Categoria
        System.out.println("Nueva categoria (categoria actual: " + productoActualizado.getCategoria() + ", Enter para mantenerlo): ");
        String categoria = scanner.nextLine().trim();

        if (!categoria.isEmpty()) { 
            productoActualizado.setCategoria(categoria);
        }

        // Precio
        System.out.println("Nuevo precio (precio actual: " + productoActualizado.getPrecio() + ", Enter para mantenerlo, 0 para no cambiar): ");
        String precioInput = scanner.nextLine().trim();
        
        // CORRECCIÓN: Evitar la conversión automática. Solo actualiza si el usuario ingresa un valor.
        if (!precioInput.isEmpty()) {
            try {
                double nuevoPrecio = Double.parseDouble(precioInput);
                // ERROR LÓGICO CORREGIDO: Establecer el precio sin usar una condición de marca.
                productoActualizado.setPrecio(nuevoPrecio); 
            } catch (NumberFormatException nfe) {
                System.out.println("Advertencia: El precio ingresado no es válido. Se mantendrá el precio actual.");
            }
        }
        
        // 3. Persistir los cambios en la base de datos (Llamada Faltante)
        productoServiceImpl.actualizar(productoActualizado);
        
        System.out.println("✅ Producto con ID " + id + " actualizado correctamente.");


    } catch (NumberFormatException nfe) {
        System.err.println("Error: El ID o el Precio ingresado no es un número válido.");
    } catch (Exception e) {
        System.err.println("Error al actualizar el producto: " + e.getMessage());
    }
}
    
    //corregido
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
    
//    public void crearCodigoBarrasIndependiente() {
//        try{
//            CodigoBarras codigoBarras = crearCodigoBarras();
//            productoServiceImpl.getCodigoBarrasService().insertar(codigoBarras);
//            System.out.println("Codigo de barras creado correctamente.");
//        }   
//        catch(Exception e){
//            System.err.println("Error al crear el codigo de barras." +e.getMessage());
//        }
//    }
    
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
        try {
                System.out.println("ID del Codigo de Barras que desea actualizar: ");
                int id = Integer.parseInt(scanner.nextLine());

                CodigoBarras codigoActualizado = productoServiceImpl.getCodigoBarrasService().getById(id);

                if (codigoActualizado == null) {
                    System.out.println("Codigo de Barras con ID " + id + " NO encontrado.");
                    System.out.println("¿Desea crear un NUEVO Codigo de Barras y asociarlo a un Producto? (S/N)");
                    String opcion = scanner.nextLine().trim().toUpperCase();

                    if (opcion.equals("S")) {
                        // Proceder a la creación y asociación
                        crearYAsociarCodigoBarras();
                    }
                    return;
                }

                // --- SOLICITAR Y APLICAR NUEVOS VALORES (Si el objeto existe) ---

                System.out.println("--- Actualizando Codigo de Barras (ID: " + codigoActualizado.getId() + ") ---");

                // Tipo
                System.out.println("Nuevo Tipo (actual: " + codigoActualizado.getTipo() + ", Enter para mantenerlo): ");
                String tipo = scanner.nextLine().trim();
                if (!tipo.isEmpty()) {
                    codigoActualizado.setTipo(tipo);
                }

                // Observaciones
                System.out.println("Nueva Observación (actual: " + codigoActualizado.getObservaciones() + ", Enter para mantenerla): ");
                String observacion = scanner.nextLine().trim();
                if (!observacion.isEmpty()) {
                    codigoActualizado.setObservaciones(observacion);
                }

                // Fecha de Asignación (Manejo de formato)
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                String fechaActual = codigoActualizado.getFechaAsignacion() != null ? formato.format(codigoActualizado.getFechaAsignacion()) : "N/A";

                System.out.println("Nueva Fecha (actual: " + fechaActual + ", Formato dd/MM/yyyy, Enter para mantenerla): ");
                String fechaInput = scanner.nextLine().trim();

                if (!fechaInput.isEmpty()) {
                    try {
                        Date nuevaFecha = formato.parse(fechaInput);
                        codigoActualizado.setFechaAsignacion(nuevaFecha);
                    } catch (ParseException pe) {
                        System.out.println("Advertencia: El formato de fecha es incorrecto. Se mantiene la fecha actual.");
                    }
                }

                // PERSISTIR LOS CAMBIOS
                productoServiceImpl.getCodigoBarrasService().actualizar(codigoActualizado);

                System.out.println("✅ Codigo de Barras actualizado correctamente.");

            } catch (NumberFormatException nfe) {
                System.err.println("Error: El ID ingresado no es un número válido.");
            } catch (Exception e) {
                System.err.println("Error al actualizar codigo de barras. " + e.getMessage());
            }
    }
    
    private void crearYAsociarCodigoBarras() {
        try {
            System.out.println("\n--- Creación de Nuevo Codigo de Barras ---");

            // 1. Obtener ID del Producto para la FK
            System.out.println("Ingrese el ID del Producto al que se asociará este código: ");
            int idProducto = Integer.parseInt(scanner.nextLine());

            Producto productoExistente = productoServiceImpl.getById(idProducto);

            if (productoExistente == null) {
                System.out.println("❌ Error: Producto con ID " + idProducto + " no encontrado. No se puede crear el código.");
                return;
            }

            // 2. Crear el objeto CodigoBarras (reutilizando lógica de entrada)
            CodigoBarras nuevoCodigo = crearCodigoBarras(); // Reutiliza tu método auxiliar de entrada
            nuevoCodigo.setProductoId(idProducto); // Seteamos la clave foránea requerida

            // 3. Persistir (Usamos el método del servicio)
            // Nota: Asumimos que el servicio maneja la inserción correctamente.
            productoServiceImpl.getCodigoBarrasService().insertar(nuevoCodigo);

            System.out.println("✅ Codigo de Barras creado y asociado al Producto " + idProducto + " con éxito.");

        } catch (NumberFormatException nfe) {
            System.err.println("Error: El ID del producto ingresado no es un número válido.");
        } catch (Exception e) {
            System.err.println("Error durante la creación y asociación del código de barras. " + e.getMessage());
        }
    }

    public void eliminarCodigoBarrasPorId(){
//        try{
//            System.out.println("ID del codigo de barras que desea eliminar: ");
//            int id = Integer.parseInt(scanner.nextLine());
//            productoServiceImpl.getCodigoBarrasService().eliminar(id);
//            System.out.println("Codigo de barras eliminado correctamente.");
//            
//        }
//        catch(Exception e){
//            System.out.println("Error al eliminar el codigo de barras. "+e.getMessage());
//            
//        }
              try{
            System.out.println("ID del codigo de barras que desea eliminar: ");
            int idCodigoBarras = Integer.parseInt(scanner.nextLine());

            // 1. Obtener el CodigoBarras para saber a qué Producto está asociado
            CodigoBarras codigoABorrar = productoServiceImpl.getCodigoBarrasService().getById(idCodigoBarras);

            if (codigoABorrar == null) {
                System.out.println("Error: Codigo de barras con ID " + idCodigoBarras + " no encontrado.");
                return;
            }

            int idProducto = codigoABorrar.getProductoId();

            // 2. Usar el método transaccional del servicio para eliminar y desvincular
            // Este método realiza la desvinculación (Producto.setCodigoBarras(null)) y luego el soft delete del CodigoBarras.
            productoServiceImpl.eliminarCodigoBarrasProducto(idProducto, idCodigoBarras);

            System.out.println("Codigo de barras eliminado y desvinculado del Producto " + idProducto + " correctamente.");

        } catch (NumberFormatException nfe) {
            System.out.println("Error: El ID ingresado no es un número válido.");
        } catch(Exception e){
            // Capturará errores si el código de barras no pertenece al producto (validación en el servicio)
            System.err.println("Error al eliminar el codigo de barras: "+e.getMessage());
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
    



    