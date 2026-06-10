package fitclub.dao;

import fitclub.model.Ejercicio;
import java.util.List;

public interface IEjercicioDAO {
    void insertar(Ejercicio ejercicio);
    void actualizar(Ejercicio ejercicio);
    void eliminar(int idEjercicio);
    Ejercicio buscarPorId(int idEjercicio);
    List<Ejercicio> listarPorRutina(int idRutina);
}