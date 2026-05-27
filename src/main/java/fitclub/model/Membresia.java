package fitclub.model;

import java.time.LocalDate;

/**
 * Representa la membresía de un cliente en el gimnasio Fit Club.
 * Contiene el tipo, las fechas de vigencia y expone lógica
 * para verificar si la membresía se encuentra activa.
 */

public class Membresia {

    private int idMembresia;
    private String tipo;
    private LocalDate fechaInicio;
    private LocalDate fechaVencimiento;

    /**
     * Constructor de Membresia.
     *
     * @param idMembresia      Identificador único de la membresía.
     * @param tipo             Tipo de membresía (mensual, trimestral, anual).
     * @param fechaInicio      Fecha de inicio de la membresía.
     * @param fechaVencimiento Fecha de vencimiento de la membresía.
     */

    public Membresia(String tipo, int idMembresia, LocalDate fechaInicio, LocalDate fechaVencimiento) {
        this.tipo = tipo;
        this.idMembresia = idMembresia;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * Verifica si la membresía se encuentra vigente a la fecha actual.
     *
     * @return {@code true} si la fecha actual es anterior o igual al vencimiento.
     */

    public boolean estaVigente(){
        return !LocalDate.now().isAfter(fechaVencimiento);
    }

    public int getIdMembresia() {
        return idMembresia;
    }

    public void setIdMembresia(int idMembresia) {

        if (idMembresia <= 0) throw new IllegalArgumentException("El ID de membresía debe ser mayor a cero.");
        this.idMembresia = idMembresia;
    }


    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de membresía.
     *
     * @param tipo Debe ser mensual, trimestral o anual.
     */

    public void setTipo(String tipo) {

        if (tipo == null || tipo.trim().isEmpty()) throw new IllegalArgumentException("El tipo de membresía no puede estar vacío.");
        this.tipo = tipo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {

        if (fechaInicio == null) throw new IllegalArgumentException("La fecha de inicio no puede estar vacio");
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * Establece la fecha de vencimiento.
     * Debe ser posterior a la fecha de inicio.
     *
     * @param fechaVencimiento Nueva fecha de vencimiento.
     */

    public void setFechaVencimiento(LocalDate fechaVencimiento) {

        if (fechaVencimiento == null) throw new IllegalArgumentException("La fecha de vencimiento no puede ser nulo");
        if (fechaInicio != null && fechaVencimiento.isBefore(fechaInicio)) throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a la fecha de inicio");
        this.fechaVencimiento = fechaVencimiento;
    }
}
