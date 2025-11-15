/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author gast-n
 */
public class Base {

    int id;

    private boolean eliminado;

    /**
     * Constructor completo con todos los campos.
     * Usado por los DAOs al reconstruir entidades desde la base de datos.
     *
     * @param id Identificador de la entidad
     * @param eliminado Estado de eliminación
     */
    protected Base(int id, boolean eliminado) {
        this.id = id;
        this.eliminado = eliminado;
    }

    /**
     * Constructor por defecto.
     * Inicializa una entidad nueva sin ID (será asignado por la BD).
     * Por defecto, las entidades nuevas NO están eliminadas.
     */
    protected Base() {
        this.eliminado = false;
    }

    /**
     * Obtiene el ID de la entidad.
     * @return ID de la entidad, 0 si aún no ha sido persistida
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Verifica si la entidad está marcada como eliminada.
     * @return true si está eliminada, false si está activa
     */
    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
}
