package fitclub.service;

import fitclub.dao.IPagoDAO;
import fitclub.model.Pago;

import java.util.List;

/**
 * Capa de servicios para la gestión de pagos del gimnasio Fit Club.
 * Contiene la lógica de negocio para registrar, actualizar y consultar
 * los pagos asociados a las membresías de los clientes.
 */

public class PagoService {

    private final IPagoDAO pagoDAO;

    public PagoService(IPagoDAO pagoDAO) {
        this.pagoDAO = pagoDAO;
    }

    /**
     * Registra un nuevo pago asociado a una membresía.
     * Valida que el pago no sea nulo y que el ID de membresía sea válido.
     *
     * @param pago        Pago a registrar.
     * @param membresiaId ID de la membresía a la que pertenece el pago.
     * @throws IllegalArgumentException si el pago es nulo o el ID de membresía es inválido.
     */

    public void registrarPago(Pago pago, int membresiaId) {
        if (pago == null) {
            throw new IllegalArgumentException("El pago no puede ser nulo.");
        }
        if (membresiaId <= 0) {
            throw new IllegalArgumentException("El ID de membresía debe ser mayor a cero.");
        }
        if (pago.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero.");
        }
        pagoDAO.insertar(pago, membresiaId);
    }

    /**
     * Actualiza los datos de un pago existente.
     *
     * @param pago Pago con los datos actualizados.
     * @throws IllegalArgumentException si el pago es nulo o no existe.
     */

    public void actualizarPago(Pago pago) {
        if (pago == null) {
            throw new IllegalArgumentException("El pago no puede ser nulo.");
        }
        if (pagoDAO.buscarPorId(pago.getIdPago()) == null) {
            throw new IllegalArgumentException("No existe un pago con el ID: " + pago.getIdPago());
        }
        pagoDAO.actualizar(pago);
    }

    /**
     * Busca un pago por su ID.
     *
     * @param idPago ID del pago a buscar.
     * @return El pago encontrado, o {@code null} si no existe.
     * @throws IllegalArgumentException si el ID es menor o igual a cero.
     */

    public Pago buscarPorId(int idPago) {
        if (idPago <= 0) {
            throw new IllegalArgumentException("El ID de pago debe ser mayor a cero.");
        }
        return pagoDAO.buscarPorId(idPago);
    }

    /**
     * Consulta todos los pagos asociados a una membresía.
     *
     * @param membresiaId ID de la membresía a consultar.
     * @return Lista de pagos de la membresía. Puede estar vacía si no hay registros.
     * @throws IllegalArgumentException si el ID de membresía es inválido.
     */

    public List<Pago> consultarPagosPorMembresia(int membresiaId) {
        if (membresiaId <= 0) {
            throw new IllegalArgumentException("El ID de membresía debe ser mayor a cero.");
        }
        return pagoDAO.listarPorMembresia(membresiaId);
    }

    /**
     * Calcula el total pagado por una membresía sumando todos sus pagos.
     *
     * @param membresiaId ID de la membresía a consultar.
     * @return Total pagado como double.
     */

    public double calcularTotalPagado(int membresiaId) {
        if (membresiaId <= 0) {
            throw new IllegalArgumentException("El ID de membresía debe ser mayor a cero.");
        }
        return pagoDAO.listarPorMembresia(membresiaId)
                .stream()
                .mapToDouble(Pago::getMonto)
                .sum();
    }

    /**
     * Elimina un pago por su ID.
     * Valida que el pago exista antes de eliminar.
     *
     * @param idPago ID del pago a eliminar.
     * @throws IllegalArgumentException si el pago no existe.
     */

    public void eliminarPago(int idPago) {
        if (pagoDAO.buscarPorId(idPago) == null) {
            throw new IllegalArgumentException("No existe un pago con el ID: " + idPago);
        }
        pagoDAO.eliminar(idPago);
    }
}
