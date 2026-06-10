package fitclub.dao;

import fitclub.model.Entrenador;
import java.util.List;

public interface IEntrenadorDAO {
    void insertar(Entrenador entrenador);
    void actualizar(Entrenador entrenador);
    void desactivar(String cedula);
    void eliminar(String cedula);          // ← AÑADIDO
    Entrenador buscarPorCedula(String cedula);
    List<Entrenador> listarTodos();
    List<Entrenador> listarActivos();
}