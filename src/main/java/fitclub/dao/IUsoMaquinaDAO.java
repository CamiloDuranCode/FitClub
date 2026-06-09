package fitclub.dao;

import fitclub.model.UsoMaquina;
import java.util.List;

public interface IUsoMaquinaDAO {
    void iniciarUso(String clienteCedula, int idMaquina);
    void finalizarUso(int idUso);
    UsoMaquina buscarPorId(int idUso);
    List<UsoMaquina> listarPorCliente(String clienteCedula);
    List<UsoMaquina> listarPorMaquina(int idMaquina);
    List<UsoMaquina> listarUsosActivos();
}