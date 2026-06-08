package fitclub.dao;

import fitclub.model.Usuario;
import fitclub.model.enums.RolUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para operaciones de Usuario.
 * Login via SELECT * FROM fn_login(?, ?) con PreparedStatement.
 *
 * @author Wilberto Ariza Zapata
 */
public class UsuarioDAO implements IUsuarioDAO {

    @Override
    public Usuario login(String username, String password) {
        String sql = "SELECT * FROM fn_login(?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("username"),
                            null, // password no se retorna en fn_login
                            RolUsuario.valueOf(rs.getString("rol").toUpperCase()),
                            rs.getString("nombre"),
                            true, // si retorna fn_login, está activo
                            rs.getString("cedula_entrenador")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al iniciar sesión: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void insertar(Usuario usuario, String password) {
        String sql = "{call sp_registrar_usuario(?, ?, ?, ?, ?)}";
        try (CallableStatement cs = Conexion.getInstancia().prepareCall(sql)) {
            cs.setString(1, usuario.getUsername());
            cs.setString(2, password);
            cs.setObject(3, usuario.getRol().name().toLowerCase(), Types.OTHER);
            cs.setString(4, usuario.getNombre());
            cs.setString(5, usuario.getCedulaEntrenador());
            cs.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public void toggleActivo(int idUsuario) {
        String sql = "{call sp_toggle_usuario(?)}";
        try (CallableStatement cs = Conexion.getInstancia().prepareCall(sql)) {
            cs.setInt(1, idUsuario);
            cs.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al cambiar estado del usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM v_usuarios_activos";
        try (Statement st = Conexion.getInstancia().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        null,
                        RolUsuario.valueOf(rs.getString("rol").toUpperCase()),
                        rs.getString("nombre"),
                        true,
                        rs.getString("cedula_entrenador")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public Usuario buscarPorId(int idUsuario) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("username"),
                            null,
                            RolUsuario.valueOf(rs.getString("rol").toUpperCase()),
                            rs.getString("nombre"),
                            rs.getBoolean("activo"),
                            rs.getString("cedula_entrenador")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }
        return null;
    }
}