package fitclub.model;

import fitclub.model.enums.TipoMembresia;

import java.time.LocalDate;

/**
 * Representa la membresía de un cliente en el gimnasio Fit Club.
 * Contiene el tipo, las fechas de vigencia y expone lógica
 * para verificar si la membresía se encuentra activa.
 * Implementa {@link ICalculable} para formalizar el contrato
 * de cálculo de valor y vigencia.
 */
public class Membresia implements ICalculable {

    private static final double VALOR_BASE_MENSUAL = 80000.0;

    private int idMembresia;
    private TipoMembresia tipo;
    private LocalDate fechaInicio;
    private LocalDate fechaVencimiento;

    /**
     * Constructor de Membresia.
     *
     * @param idMembresia      Identificador único de la membresía.
     * @param tipo             Tipo de membresía (mensual, trimestral, semestral, anual).
     * @param fechaInicio      Fecha de inicio de la membresía.
     * @param fechaVencimiento Fecha de vencimiento de la membresía.
     */

    public Membresia(int idMembresia, TipoMembresia tipo, LocalDate fechaInicio, LocalDate fechaVencimiento) {
        this.idMembresia = idMembresia;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * Calcula el valor total de la membresía según su tipo.
     * mensual = valor base, trimestral = base x 3,
     * semestral = base x 6, anual = base x 12.
     *
     * @return total a pagar como {@code double}.
     */
    @Override
    public double calcularTotal() {
        return switch (tipo) {
            case TRIMESTRAL -> VALOR_BASE_MENSUAL * 3;
            case SEMESTRAL  -> VALOR_BASE_MENSUAL * 6;
            case ANUAL      -> VALOR_BASE_MENSUAL * 12;
            default           -> VALOR_BASE_MENSUAL; // mensual
        };
    }

    /**
     * Verifica si la membresía se encuentra vigente a la fecha actual.
     *
     * @return {@code true} si la fecha actual es anterior o igual al vencimiento.
     */
    @Override
    public boolean estaVigente() {
        return !LocalDate.now().isAfter(fechaVencimiento);
    }

    public int getIdMembresia() {
        return idMembresia;
    }

    public void setIdMembresia(int idMembresia) {
        if (idMembresia <= 0) throw new IllegalArgumentException("El ID de membresía debe ser mayor a cero.");
        this.idMembresia = idMembresia;
    }

    public TipoMembresia getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de membresía.
     *
     * @param tipo Debe ser mensual, trimestral, semestral o anual.
     */
    public void setTipo(TipoMembresia tipo) {
        if (tipo == null) throw new IllegalArgumentException("El tipo de membresía no puede estar vacío.");
        this.tipo = tipo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        if (fechaInicio == null) throw new IllegalArgumentException("La fecha de inicio no puede estar vacío.");
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
        if (fechaVencimiento == null) throw new IllegalArgumentException("La fecha de vencimiento no puede ser nula.");
        if (fechaInicio != null && fechaVencimiento.isBefore(fechaInicio)) throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a la fecha de inicio.");
        this.fechaVencimiento = fechaVencimiento;
    }
}