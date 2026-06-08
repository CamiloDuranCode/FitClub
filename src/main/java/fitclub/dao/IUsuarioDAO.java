package fitclub.dao;

import fitclub.model.Usuario;
import java.util.List;

public interface IUsuarioDAO {
    Usuario login(String username, String password);
    void insertar(Usuario usuario, String password);
    void toggleActivo(int idUsuario);
    Usuario buscarPorId(int idUsuario);
    List<Usuario> listarTodos();
}
