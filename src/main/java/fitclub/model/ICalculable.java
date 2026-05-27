package fitclub.model;

/**
 * Contrato que deben cumplir las entidades del sistema
 * que involucran cálculos financieros y vigencia temporal.
 * Implementado por {@link Membresia} y {@link Pago}.
 */
public interface ICalculable {

    /**
     * Calcula el valor total asociado a la entidad.
     *
     * @return total como {@code double}.
     */
    double calcularTotal();

    /**
     * Verifica si la entidad se encuentra vigente a la fecha actual.
     *
     * @return {@code true} si está vigente, {@code false} en caso contrario.
     */
    boolean estaVigente();
}