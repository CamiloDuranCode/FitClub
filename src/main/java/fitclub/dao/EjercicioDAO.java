package fitclub.dao;

import fitclub.model.Ejercicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para operaciones CRUD de Ejercicio.
 *
 * @author Juan Camilo Rangel Osias
 */
public class EjercicioDAO implements IEjercicioDAO {

    @Override
    public void insertar(Ejercicio ejercicio) {
        String sql = "INSERT INTO ejercicio (id_rutina, nombre, series, repeticiones, descripcion, id_maquina) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, ejercicio.getIdRutina());
            ps.setString(2, ejercicio.getNombre());
            ps.setInt(3, ejercicio.getSeries());
            ps.setInt(4, ejercicio.getRepeticiones());
            ps.setString(5, ejercicio.getDescripcion());
            if (ejercicio.tieneMaquina()) {
                ps.setInt(6, ejercicio.getIdMaquina());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar ejercicio: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Ejercicio ejercicio) {
        String sql = "UPDATE ejercicio SET nombre = ?, series = ?, repeticiones = ?, " +
                "descripcion = ?, id_maquina = ? WHERE id_ejercicio = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, ejercicio.getNombre());
            ps.setInt(2, ejercicio.getSeries());
            ps.setInt(3, ejercicio.getRepeticiones());
            ps.setString(4, ejercicio.getDescripcion());
            if (ejercicio.tieneMaquina()) {
                ps.setInt(5, ejercicio.getIdMaquina());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setInt(6, ejercicio.getIdEjercicio());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar ejercicio: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int idEjercicio) {
        String sql = "DELETE FROM ejercicio WHERE id_ejercicio = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idEjercicio);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar ejercicio: " + e.getMessage(), e);
        }
    }

    @Override
    public Ejercicio buscarPorId(int idEjercicio) {
        String sql = "SELECT * FROM ejercicio WHERE id_ejercicio = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idEjercicio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ejercicio: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Ejercicio> listarPorRutina(int idRutina) {
        List<Ejercicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM ejercicio WHERE id_rutina = ? ORDER BY id_ejercicio";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idRutina);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar ejercicios: " + e.getMessage(), e);
        }
        return lista;
    }

    private Ejercicio mapear(ResultSet rs) throws SQLException {
        int idMaquina = rs.getInt("id_maquina");
        return new Ejercicio(
                rs.getInt("id_ejercicio"),
                rs.getInt("id_rutina"),
                rs.getString("nombre"),
                rs.getInt("series"),
                rs.getInt("repeticiones"),
                rs.getString("descripcion"),
                rs.wasNull() ? null : idMaquina
        );
    }
}