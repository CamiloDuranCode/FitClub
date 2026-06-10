package fitclub.model;

import java.time.LocalDate;

/**
 * Representa una rutina de entrenamiento asignada a un cliente en el gimnasio Fit Club.
 * Contiene nombre, objetivo, descripción y fecha de creación.
 *
 * @author Juan Camilo Rangel Osias
 */
public class Rutina {

    private int idRutina;
    private String nombre;
    private String objetivo;
    private String descripcion;
    private LocalDate fechaAsignacion;

    /**
     * Constructor de Rutina.
     *
     * @param idRutina        Identificador único de la rutina.
     * @param nombre          Nombre de la rutina de entrenamiento.
     * @param objetivo        Objetivo principal de la rutina (puede ser nulo).
     * @param descripcion     Descripción detallada del plan de entrenamiento.
     * @param fechaAsignacion Fecha en que se asignó la rutina al cliente.
     */
    public Rutina(int idRutina, String nombre, String objetivo,
                  String descripcion, LocalDate fechaAsignacion) {
        this.idRutina = idRutina;
        this.nombre = nombre;
        this.objetivo = objetivo;
        this.descripcion = descripcion;
        this.fechaAsignacion = fechaAsignacion;
    }

    public int getIdRutina() { return idRutina; }

    public void setIdRutina(int idRutina) {
        if (idRutina <= 0) throw new IllegalArgumentException("El ID de rutina debe ser mayor a cero.");
        this.idRutina = idRutina;
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre de la rutina no puede estar vacío.");
        this.nombre = nombre;
    }

    public String getObjetivo() { return objetivo; }

    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty())
            throw new IllegalArgumentException("La descripción de la rutina no puede estar vacía.");
        this.descripcion = descripcion;
    }

    public LocalDate getFechaAsignacion() { return fechaAsignacion; }

    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        if (fechaAsignacion == null)
            throw new IllegalArgumentException("La fecha de asignación no puede estar vacía.");
        this.fechaAsignacion = fechaAsignacion;
    }
}