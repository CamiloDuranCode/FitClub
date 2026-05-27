package fitclub.dao;

import java.util.List;


public interface AsistenciaDAO {
    void insertar(Asistencia asistencia, String clienteCedula);
    void eliminar(int idAsistencia);
    Asistencia buscarPorId(int idAsistencia);
    List<Asistencia> listarPorCliente(String clienteCedula);
}