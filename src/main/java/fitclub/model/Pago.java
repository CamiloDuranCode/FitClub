package fitclub.model;

import fitclub.model.enums.MetodoPago;

import java.time.LocalDate;

/**
 * Representa un pago realizado por un cliente en el gimnasio Fit Club.
 * Registra el monto, la fecha y el método de pago utilizado.
 * Implementa {@link ICalculable} para formalizar el contrato
 * de cálculo de valor y vigencia del pago.
 */
public class Pago implements ICalculable {

    private int idPago;
    private double monto;
    private LocalDate fechaPago;
    private MetodoPago metodoPago;

    /**
     * Constructor de Pago.
     *
     * @param idPago     Identificador único del pago.
     * @param monto      Monto pagado. Debe ser mayor a cero.
     * @param fechaPago  Fecha en que se realizó el pago.
     * @param metodoPago Método de pago utilizado (efectivo, transferencia, etc).
     */
    public Pago(int idPago, double monto, LocalDate fechaPago, MetodoPago metodoPago) {
        this.idPago = idPago;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.metodoPago = metodoPago;
    }

    /**
     * Retorna el monto total del pago.
     *
     * @return monto como {@code double}.
     */
    @Override
    public double calcularTotal() {
        return this.monto;
    }

    /**
     * Verifica si el pago ya fue efectuado a la fecha actual.
     * Un pago con fecha futura no se considera vigente aún.
     *
     * @return {@code true} si la fecha de pago es hoy o anterior.
     */
    @Override
    public boolean estaVigente() {
        return !LocalDate.now().isBefore(fechaPago);
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        if (idPago <= 0) throw new IllegalArgumentException("El ID de pago debe ser mayor a cero.");
        this.idPago = idPago;
    }

    public double getMonto() {
        return monto;
    }

    /**
     * Establece el monto del pago.
     * No se permiten valores negativos ni iguales a cero.
     *
     * @param monto Nuevo monto del pago.
     */
    public void setMonto(double monto) {
        if (monto <= 0) throw new IllegalArgumentException("El monto del pago debe ser mayor a cero.");
        this.monto = monto;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        if (fechaPago == null) throw new IllegalArgumentException("La fecha de pago no puede estar vacía.");
        this.fechaPago = fechaPago;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        if (metodoPago == null) throw new IllegalArgumentException("El método de pago no puede estar vacío.");
        this.metodoPago = metodoPago;
    }
}