/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.Objects;

/**
 *
 * @author gast-n
 */

public class Producto extends Base {
/** Nombre del producto. Requerido. */
    private String nombre;

    /** Marca del producto. Requerido. */
    private String marca;

    /** Categoría del producto. Requerido. */
    private String categoria;

    /** Precio del producto. Requerido. */
    private double precio;

    private CodigoBarras codigoBarras;

    /**
     * Constructor completo para reconstruir un Producto desde la BD.
     * Usado por ProductoDAO al mapear ResultSet.
     * @param id
     * @param nombre
     * @param marca
     * @param categoria
     * @param precio
     */
    public Producto(int id, String nombre, String marca, String categoria, double precio) {
        super(id, false);
        this.nombre = nombre;
        this.marca = marca;
        this.categoria = categoria;
        this.precio = precio;
    }

    /** Constructor por defecto para crear un producto nuevo sin ID. */
    public Producto() {
        super();
    }

    public Producto(String nombre, String marca, String categoria, double precio) {
        this.nombre = nombre;
        this.marca = marca;
        this.categoria = categoria;
        this.precio = precio;
    }
    
    

    // --- Getters y Setters ---

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el código de barras asociado. (Soluciona el error en ProductoDAO)
     * @return 
     */
    public CodigoBarras getCodigoBarras() {
        return codigoBarras;
    }

    /**
     * Asocia o desasocia un código de barras al producto.
     * @param codigoBarras
     */
    public void setCodigoBarras(CodigoBarras codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + getId() +
                ", nombre='" + nombre + '\'' +
                ", marca='" + marca + '\'' +
                ", categoria='" + categoria + '\'' +
                ", precio=" + precio +
                ", codigoBarras=" + codigoBarras +
                ", eliminado=" + isEliminado() +
                '}';
    }

    /**
     * Se elimina la lógica de equals y hashCode basada en DNI,
     * ya que la clase Producto no tiene un campo 'natural' único
     * como lo era el DNI. Se usa la implementación por defecto o
     * la heredada de Base2.
     */

}
