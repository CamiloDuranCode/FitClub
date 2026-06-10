package fitclub.dao;

import fitclub.model.Maquina;
import fitclub.model.enums.EstadoMaquina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para operaciones CRUD de Maquina.
 *
 * @author Wilberto Ariza Zapata
 */
public class MaquinaDAO implements IMaquinaDAO {

    @Override
    public void insertar(Maquina maquina) {
        String sql = "INSERT INTO maquina (nombre, tipo, ubicacion, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, maquina.getNombre());
            ps.setString(2, maquina.getTipo());
            ps.setString(3, maquina.getUbicacion());
            ps.setObject(4, maquina.getEstado().name().toLowerCase(), Types.OTHER);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar máquina: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Maquina maquina) {
        String sql = "UPDATE maquina SET nombre = ?, tipo = ?, ubicacion = ?, estado = ? WHERE id_maquina = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, maquina.getNombre());
            ps.setString(2, maquina.getTipo());
            ps.setString(3, maquina.getUbicacion());
            ps.setObject(4, maquina.getEstado().name().toLowerCase(), Types.OTHER);
            ps.setInt(5, maquina.getIdMaquina());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar máquina: " + e.getMessage(), e);
        }
    }

    @Override
    public void desactivar(int idMaquina) {
        String sql = "UPDATE maquina SET activa = FALSE WHERE id_maquina = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idMaquina);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar máquina: " + e.getMessage(), e);
        }
    }

    @Override
    public Maquina buscarPorId(int idMaquina) {
        String sql = "SELECT * FROM maquina WHERE id_maquina = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idMaquina);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar máquina: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Maquina> listarTodas() {
        List<Maquina> lista = new ArrayList<>();
        String sql = "SELECT * FROM maquina ORDER BY nombre";
        try (Statement st = Conexion.getInstancia().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar máquinas: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Maquina> listarActivas() {
        List<Maquina> lista = new ArrayList<>();
        String sql = "SELECT * FROM maquina WHERE activa = TRUE ORDER BY nombre";
        try (Statement st = Conexion.getInstancia().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar máquinas activas: " + e.getMessage(), e);
        }
        return lista;
    }

    private Maquina mapear(ResultSet rs) throws SQLException {
        return new Maquina(
                rs.getInt("id_maquina"),
                rs.getString("nombre"),
                rs.getString("tipo"),
                rs.getString("ubicacion"),
                EstadoMaquina.valueOf(rs.getString("estado").toUpperCase()),
                rs.getBoolean("activa")
        );
    }
}