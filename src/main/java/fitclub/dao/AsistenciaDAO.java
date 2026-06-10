package fitclub.dao;

import fitclub.model.Asistencia;
import fitclub.model.enums.TipoAsistencia;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaDAO implements IAsistenciaDAO {

    @Override
    public void insertar(Asistencia asistencia, String clienteCedula) {
        String sql = "INSERT INTO asistencia (cedula, fecha_hora, tipo, observacion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, clienteCedula);
            ps.setTimestamp(2, Timestamp.valueOf(asistencia.getFechaHora()));
            ps.setObject(3, asistencia.getTipo().name().toLowerCase(), Types.OTHER);
            ps.setString(4, asistencia.getObservacion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar asistencia: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int idAsistencia) {
        String sql = "DELETE FROM asistencia WHERE id_asistencia = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idAsistencia);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar asistencia: " + e.getMessage(), e);
        }
    }

    @Override
    public Asistencia buscarPorId(int idAsistencia) {
        String sql = "SELECT * FROM asistencia WHERE id_asistencia = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idAsistencia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar asistencia: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Asistencia> listarPorCliente(String clienteCedula) {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia WHERE cedula = ? ORDER BY fecha_hora DESC";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, clienteCedula);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar asistencias: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Asistencia> listarPorFecha(String clienteCedula, LocalDate inicio, LocalDate fin) {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia WHERE cedula = ? " +
                "AND fecha_hora >= ? AND fecha_hora < ? ORDER BY fecha_hora DESC";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, clienteCedula);
            ps.setTimestamp(2, Timestamp.valueOf(inicio.atStartOfDay()));
            ps.setTimestamp(3, Timestamp.valueOf(fin.plusDays(1).atStartOfDay()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar asistencias por fecha: " + e.getMessage(), e);
        }
        return lista;
    }

    private Asistencia mapear(ResultSet rs) throws SQLException {
        return new Asistencia(
                rs.getInt("id_asistencia"),
                rs.getTimestamp("fecha_hora").toLocalDateTime(),
                TipoAsistencia.valueOf(rs.getString("tipo").toUpperCase()),
                rs.getString("observacion")
        );
    }
}