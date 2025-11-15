/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author gast-n
 */
public class CodigoBarras extends Base {
    private String tipo;
    private Date fechaAsignacion = new Date();
    private String observaciones;

    public CodigoBarras(String tipo, String observaciones, int id, boolean eliminado) {
        super(id, eliminado);
        this.tipo = tipo;
        this.observaciones = observaciones;
    }

    public CodigoBarras(String tipo, String observaciones) {
        super();
    }

    public CodigoBarras() {
    }
    
    

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "CodigoBarras{" + "tipo=" + tipo + 
                ", fechaAsignacion=" + fechaAsignacion +
                ", observaciones=" + observaciones + '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodigoBarras codigoBarras  = (CodigoBarras) o;
        return Objects.equals(id, codigoBarras.id);
    }
    
        @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
}
