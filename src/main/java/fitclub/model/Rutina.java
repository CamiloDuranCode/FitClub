package fitclub.model;

import java.time.LocalDate;

/**
 * Representa una rutina de entrenamiento asignada a un cliente en el gimnasio Fit Club.
 * Incluye una descripción del plan de ejercicios y la fecha en que fue asignada.
 */

public class Rutina {

    private int idRutina;
    private String descripcion;
    private LocalDate fechaAsignacion;

    /**
     * Constructor de Rutina.
     *
     * @param idRutina        Identificador único de la rutina.
     * @param descripcion     Descripción del plan de entrenamiento.
     * @param fechaAsignacion Fecha en que se asignó la rutina al cliente.
     */

    public Rutina(int idRutina, String descripcion, LocalDate fechaAsignacion) {
        this.idRutina = idRutina;
        this.descripcion = descripcion;
        this.fechaAsignacion = fechaAsignacion;
    }

    public int getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(int idRutina) {

        if (idRutina <= 0) throw new IllegalArgumentException("El ID de rutina debe ser mayor a cero");
        this.idRutina = idRutina;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {

        if (descripcion == null || descripcion.trim().isEmpty())
            throw new IllegalArgumentException("La descripcion de la rutina no puede estar vacia");
        this.descripcion = descripcion;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDate fechaAsignacion) {

        if (fechaAsignacion == null) throw new IllegalArgumentException("La fecha de asignacion no puede estar vacia");
        this.fechaAsignacion = fechaAsignacion;
    }
}
