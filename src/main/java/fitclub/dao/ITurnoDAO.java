package fitclub.dao;

import fitclub.model.Turno;
import java.util.List;

public interface ITurnoDAO {
    void insertar(Turno turno);
    void actualizar(Turno turno);
    void eliminar(int idTurno);
    Turno buscarPorId(int idTurno);
    List<Turno> listarPorEntrenador(String cedulaEntrenador);
    List<Turno> listarTodos();
}