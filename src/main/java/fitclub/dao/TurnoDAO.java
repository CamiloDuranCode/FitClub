package fitclub.dao;

import fitclub.model.Turno;
import fitclub.model.enums.DiaSemana;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para operaciones CRUD de TurnoEntrenador.
 *
 * @author Wilberto Ariza Zapata
 */
public class TurnoDAO implements ITurnoDAO {

    @Override
    public void insertar(Turno turno) {
        String sql = "INSERT INTO turno_entrenador (cedula_entrenador, dia, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, turno.getCedulaEntrenador());
            ps.setObject(2, turno.getDia().name().toLowerCase(), Types.OTHER);
            ps.setTime(3, Time.valueOf(turno.getHoraInicio()));
            ps.setTime(4, Time.valueOf(turno.getHoraFin()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar turno: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Turno turno) {
        String sql = "UPDATE turno_entrenador SET dia = ?, hora_inicio = ?, hora_fin = ? WHERE id_turno = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setObject(1, turno.getDia().name().toLowerCase(), Types.OTHER);
            ps.setTime(2, Time.valueOf(turno.getHoraInicio()));
            ps.setTime(3, Time.valueOf(turno.getHoraFin()));
            ps.setInt(4, turno.getIdTurno());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar turno: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int idTurno) {
        String sql = "DELETE FROM turno_entrenador WHERE id_turno = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idTurno);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar turno: " + e.getMessage(), e);
        }
    }

    @Override
    public Turno buscarPorId(int idTurno) {
        String sql = "SELECT * FROM turno_entrenador WHERE id_turno = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idTurno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar turno: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Turno> listarPorEntrenador(String cedulaEntrenador) {
        List<Turno> lista = new ArrayList<>();
        String sql = "SELECT * FROM turno_entrenador WHERE cedula_entrenador = ? ORDER BY dia";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, cedulaEntrenador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar turnos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Turno> listarTodos() {
        List<Turno> lista = new ArrayList<>();
        String sql = "SELECT * FROM turno_entrenador ORDER BY cedula_entrenador, dia";
        try (Statement st = Conexion.getInstancia().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar turnos: " + e.getMessage(), e);
        }
        return lista;
    }

    private Turno mapear(ResultSet rs) throws SQLException {
        return new Turno(
                rs.getInt("id_turno"),
                rs.getString("cedula_entrenador"),
                DiaSemana.valueOf(rs.getString("dia").toUpperCase()),
                rs.getTime("hora_inicio").toLocalTime(),
                rs.getTime("hora_fin").toLocalTime()
        );
    }
}