package fitclub.dao;

import fitclub.model.Usuario;
import fitclub.model.enums.RolUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para operaciones de Usuario.
 *
 * CORRECCIONES:
 *  - listarTodos() usaba v_usuarios_activos que no expone la columna "activo"
 *    → ahora consulta la tabla usuario directamente con WHERE activo = TRUE
 *  - insertar/cambiarPassword/toggleActivo usaban {call ...} con prepareCall()
 *    → cambiado a CALL directo con prepareStatement() para PostgreSQL
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
        // ✅ CALL directo — {call ...} falla con PROCEDURE en PostgreSQL
        String sql = "CALL sp_registrar_usuario(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, passwordHash);
            ps.setObject(3, usuario.getRol().toSQL(), Types.OTHER);
            ps.setString(4, usuario.getNombre());
            ps.setString(5, usuario.getCedulaEntrenador());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public void cambiarPassword(int idUsuario, String passwordHash) {
        // ✅ CALL directo
        String sql = "CALL sp_cambiar_password(?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, passwordHash);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al cambiar contraseña: " + e.getMessage(), e);
        }
    }

    @Override
    public void toggleActivo(int idUsuario) {
        // ✅ CALL directo
        String sql = "CALL sp_toggle_usuario(?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.execute();
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
        // ✅ Consulta la tabla directamente — v_usuarios_activos no expone la columna "activo"
        String sql = "SELECT * FROM usuario WHERE activo = TRUE ORDER BY id_usuario";
        try (Statement st = Conexion.getInstancia().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearSinHash(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios: " + e.getMessage(), e);
        }
        return lista;
    }

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