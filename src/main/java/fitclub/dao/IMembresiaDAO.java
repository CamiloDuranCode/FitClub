package fitclub.dao;

import fitclub.model.Membresia;
import java.util.List;

public interface IMembresiaDAO {
    void insertar(Membresia membresia, String clienteCedula);
    void actualizar(Membresia membresia);
    void cancelar(int idMembresia);
    Membresia buscarPorId(int idMembresia);
    List<Membresia> listarPorCliente(String clienteCedula);
    List<Membresia> listarActivasPorCliente(String clienteCedula);
}