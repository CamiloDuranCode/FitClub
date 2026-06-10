package fitclub.dao;

import fitclub.model.Usuario;
import fitclub.model.enums.RolUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para operaciones de Usuario.
 *
 * @author Wilberto Ariza Zapata
 */
public class UsuarioDAO implements IUsuarioDAO {

    @Override
    public Usuario login(String username, String passwordHash) {
        String sql = "SELECT * FROM fn_login(?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearSinHash(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al iniciar sesión: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void insertar(Usuario usuario, String passwordHash) {
        String sql = "{call sp_registrar_usuario(?, ?, ?, ?, ?)}";
        try (CallableStatement cs = Conexion.getInstancia().prepareCall(sql)) {
            cs.setString(1, usuario.getUsername());
            cs.setString(2, passwordHash);
            cs.setObject(3, usuario.getRol().toSQL(), Types.OTHER);
            cs.setString(4, usuario.getNombre());
            cs.setString(5, usuario.getCedulaEntrenador());
            cs.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public void cambiarPassword(int idUsuario, String passwordHash) {
        String sql = "{call sp_cambiar_password(?, ?)}";
        try (CallableStatement cs = Conexion.getInstancia().prepareCall(sql)) {
            cs.setInt(1, idUsuario);
            cs.setString(2, passwordHash);
            cs.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al cambiar contraseña: " + e.getMessage(), e);
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
    public Usuario buscarPorId(int idUsuario) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearSinHash(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM v_usuarios_activos";
        try (Statement st = Conexion.getInstancia().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearSinHash(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Mapea un ResultSet a Usuario sin incluir el hash de contraseña.
     * Usado en consultas de lectura donde el hash no se retorna.
     */
    private Usuario mapearSinHash(ResultSet rs) throws SQLException {
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