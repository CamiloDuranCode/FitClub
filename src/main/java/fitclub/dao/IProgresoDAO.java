package fitclub.dao;

import fitclub.model.Progreso;
import java.util.List;


public interface IProgresoDAO {
    void insertar(Progreso progreso, String clienteCedula, int rutinaId);
    void actualizar(Progreso progreso);
    void eliminar(int idProgreso);
    Progreso buscarPorId(int idProgreso);
    List<Progreso> listarPorCliente(String clienteCedula);
}