package fitclub.dao;

import fitclub.model.Asistencia;
import java.util.List;

public interface IAsistenciaDAO {
    void insertar(Asistencia asistencia, String clienteCedula);
    void eliminar(int idAsistencia);
    Asistencia buscarPorId(int idAsistencia);
    List<Asistencia> listarPorCliente(String clienteCedula);
}