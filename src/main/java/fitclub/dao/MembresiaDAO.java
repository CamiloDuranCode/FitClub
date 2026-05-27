package fitclub.dao;

import fitclub.model.Membresia;

import java.util.List;

public interface MembresiaDAO {
    void insertar(Membresia membresia, String clienteCedula);
    void actualizar(Membresia membresia);
    void eliminar(int idMembresia);
    Membresia buscarPorId(int idMembresia);
    List<Membresia> listarPorCliente(String clienteCedula);
}