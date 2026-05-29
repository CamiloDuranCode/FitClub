package fitclub.dao;

import fitclub.model.Rutina;
import java.util.List;

public interface IRutinaDAO {
    void insertar(Rutina rutina, String clienteCedula, String entrenadorCedula);
    void actualizar(Rutina rutina);
    void eliminar(int idRutina);
    Rutina buscarPorId(int idRutina);
    List<Rutina> listarPorCliente(String clienteCedula);
}