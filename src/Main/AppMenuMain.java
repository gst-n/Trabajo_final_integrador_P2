/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Dao.CodigoBarrasDAO;
import Dao.ProductoDAO;
import Service.CodigoBarrasServiceImpl;
import Service.ProductoServiceImpl;
import java.util.Scanner;

/**
 *
 * @author gast-n
 */
public class AppMenuMain {
    private final Scanner scanner;
    private final MenuHandlerMain menuHandler;
    private boolean running;
    
    
    
    public AppMenuMain() {
        this.scanner = new Scanner(System.in);
        ProductoServiceImpl productoServiceImpl = createProductoService();
        this.menuHandler = new MenuHandlerMain(scanner, productoServiceImpl);
        this.running = true;
    }
    
    
    public static void main(String[] args){
        AppMenuMain app = new AppMenuMain();
        app.run();
    }
    
    
    
    
    

    private ProductoServiceImpl createProductoService() {
        CodigoBarrasDAO codigoBarrasDAO = new CodigoBarrasDAO();
        ProductoDAO productoDAO = new ProductoDAO(codigoBarrasDAO);
        CodigoBarrasServiceImpl codigoBarrasService = new CodigoBarrasServiceImpl(codigoBarrasDAO);
        return new ProductoServiceImpl(productoDAO, codigoBarrasService);

    }

    private void run() {
        try (scanner) {
            while(running){
                try{
                    Display.mostrarMenuPrincipal();
                    int opcion = Integer.parseInt(scanner.nextLine());
                    processOption(opcion);
                }
                catch(NumberFormatException e){
                    System.out.println("Error en la entrada "+e.getMessage());
                } catch (Exception ex) {
                    System.getLogger(AppMenuMain.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
        }
    }

    private void processOption(int opcion) throws Exception {
        switch (opcion) {
            case 1 -> menuHandler.crearProducto();
            case 2 -> menuHandler.listarProductos();
            case 3 -> menuHandler.actualizarProducto();
            case 4 -> menuHandler.eliminarProducto();
            case 5 -> menuHandler.crearCodigoBarrasIndependiente();
            case 6 -> menuHandler.listarCodigoBarras();
            case 7 -> menuHandler.actualizarCodigoPorId();
            case 8 -> menuHandler.eliminarCodigoBarrasPorId();
            case 9 -> menuHandler.actualizarCodigoBarrasPorProducto();
            case 10 -> menuHandler.eliminarCodigoBarrasPorProducto();
            case 0 -> {
                System.out.println("Saliendo...");
                running = false;
            }
            default -> System.out.println("Opcion no valida.");
        }
    }
}
