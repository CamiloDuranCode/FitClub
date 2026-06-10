package fitclub.model;

import fitclub.model.enums.MetodoPago;
import java.time.LocalDate;

/**
 * Representa un pago realizado por un cliente en el gimnasio Fit Club.
 */
public class Pago implements ICalculable {

    private int idPago;
    private String cedula;        // ← AÑADIDO: faltaba este campo
    private double monto;
    private LocalDate fechaPago;
    private MetodoPago metodoPago;
    private String concepto;      // ← AÑADIDO: faltaba este campo

    /**
     * Constructor de Pago.
     */
    public Pago(int idPago, String cedula, double monto,
                LocalDate fechaPago, MetodoPago metodoPago, String concepto) {
        this.idPago      = idPago;
        this.cedula      = cedula;
        this.monto       = monto;
        this.fechaPago   = fechaPago;
        this.metodoPago  = metodoPago;
        this.concepto    = concepto;
    }

    @Override
    public double calcularTotal() {
        return this.monto;
    }

    @Override
    public boolean estaVigente() {
        return !LocalDate.now().isBefore(fechaPago);
    }

    public int getIdPago() { return idPago; }

    public void setIdPago(int idPago) {
        if (idPago <= 0) throw new IllegalArgumentException("El ID de pago debe ser mayor a cero.");
        this.idPago = idPago;
    }

    public String getCedula() { return cedula; }

    public void setCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty())
            throw new IllegalArgumentException("La cédula no puede estar vacía.");
        this.cedula = cedula;
    }

    public double getMonto() { return monto; }

    public void setMonto(double monto) {
        if (monto <= 0) throw new IllegalArgumentException("El monto del pago debe ser mayor a cero.");
        this.monto = monto;
    }

    public LocalDate getFechaPago() { return fechaPago; }

    public void setFechaPago(LocalDate fechaPago) {
        if (fechaPago == null) throw new IllegalArgumentException("La fecha de pago no puede estar vacía.");
        this.fechaPago = fechaPago;
    }

    public MetodoPago getMetodoPago() { return metodoPago; }

    public void setMetodoPago(MetodoPago metodoPago) {
        if (metodoPago == null) throw new IllegalArgumentException("El método de pago no puede estar vacío.");
        this.metodoPago = metodoPago;
    }

    public String getConcepto() { return concepto; }

    public void setConcepto(String concepto) {
        this.concepto = (concepto == null) ? "" : concepto;
    }
}