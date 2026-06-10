package fitclub.dao;

import fitclub.model.Usuario;
import java.util.List;

public interface IUsuarioDAO {
    Usuario login(String username, String passwordHash);
    void insertar(Usuario usuario, String passwordHash);
    void cambiarPassword(int idUsuario, String passwordHash);
    void toggleActivo(int idUsuario);
    Usuario buscarPorId(int idUsuario);
    List<Usuario> listarTodos();
}