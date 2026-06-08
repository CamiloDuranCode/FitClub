package fitclub.dao;

import fitclub.model.Maquina;
import java.util.List;

public interface IMaquinaDAO {
    void insertar(Maquina maquina);
    void actualizar(Maquina maquina);
    void desactivar(int idMaquina);
    Maquina buscarPorId(int idMaquina);
    List<Maquina> listarTodas();
    List<Maquina> listarActivas();
}