package fitclub.dao;

import fitclub.model.Pago;

import java.util.List;


public interface PagoDAO {
    void insertar(Pago pago, int membresiaId);
    void actualizar(Pago pago);
    void eliminar(int idPago);
    Pago buscarPorId(int idPago);
    List<Pago> listarPorMembresia(int membresiaId);
}