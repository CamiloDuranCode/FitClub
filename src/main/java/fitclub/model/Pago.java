package fitclub.model;

import java.time.LocalDate;

/**
 * Representa un pago realizado por un cliente en el gimnasio Fit Club.
 * Registra el monto, la fecha y el método de pago utilizado.
 */

public class Pago {

    private int idPago;
    private double monto;
    private LocalDate fechaPago;
    private String metodoPago;

    /**
     * Constructor de Pago.
     *
     * @param idPago      Identificador único del pago.
     * @param monto       Monto pagado. Debe ser mayor a cero.
     * @param fechaPago   Fecha en que se realizó el pago.
     * @param metodoPago  Método de pago utilizado (efectivo, transferencia, etc).
     */

    public Pago(int idPago, double monto, LocalDate fechaPago, String metodoPago) {
        this.idPago = idPago;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.metodoPago = metodoPago;
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

        if (fechaPago == null) throw new IllegalArgumentException("La fecha de pago no puede estar vacio");
        this.fechaPago = fechaPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {

        if (metodoPago == null || metodoPago.trim().isEmpty()) throw new IllegalArgumentException("El metodo de pago no puede estar vacio");
        this.metodoPago = metodoPago;
    }
}
