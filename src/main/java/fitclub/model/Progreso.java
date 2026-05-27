package fitclub.model;

import java.time.LocalDate;

/**
 * Representa un registro de progreso físico de un cliente en el gimnasio Fit Club.
 * Almacena medidas corporales como peso y talla en una fecha determinada.
 */

public class Progreso {

    private int idProgreso;
    private LocalDate fechaRegistro;
    private double peso;
    private double talla;
    private String observaciones;

    /**
     * Constructor de Progreso.
     *
     * @param idProgreso    Identificador único del registro de progreso.
     * @param fechaRegistro Fecha en que se tomaron las medidas.
     * @param peso          Peso del cliente en kilogramos. Debe ser mayor a cero.
     * @param talla         Talla del cliente en metros. Debe estar entre 0.5 y 2.5.
     * @param observaciones Observaciones adicionales sobre el progreso.
     */

    public Progreso(int idProgreso, LocalDate fechaRegistro, double peso, double talla, String observaciones) {
        this.idProgreso = idProgreso;
        this.fechaRegistro = fechaRegistro;
        this.peso = peso;
        this.talla = talla;
        this.observaciones = observaciones;
    }

    public int getIdProgreso() {
        return idProgreso;
    }

    public void setIdProgreso(int idProgreso) {

        if (idProgreso <= 0) throw new IllegalArgumentException("El ID del progreso debe ser mayor a cero");
        this.idProgreso = idProgreso;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {

        if (fechaRegistro == null) throw new IllegalArgumentException("La fecha de registro no puede estar vacia");
        this.fechaRegistro = fechaRegistro;
    }

    public double getPeso() {
        return peso;
    }

    /**
     * Establece el peso del cliente en kilogramos.
     *
     * @param peso Debe ser un valor positivo mayor a cero.
     */

    public void setPeso(double peso) {

        if (peso <= 0) throw new IllegalArgumentException("El peso debe ser mayor a cero");
        this.peso = peso;
    }

    public double getTalla() {
        return talla;
    }

    /**
     * Establece la talla del cliente en metros.
     *
     * @param talla Debe estar en el rango de 0.5 a 2.5 metros.
     */

    public void setTalla(double talla) {

        if (talla < 0.5 || talla > 2.5) throw new IllegalArgumentException("La talla debe estar entre 0.5 y 2.5 metros");
        this.talla = talla;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = (observaciones == null) ? "" : observaciones;
    }
}
