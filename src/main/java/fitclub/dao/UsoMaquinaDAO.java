package fitclub.dao;

import fitclub.model.UsoMaquina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para operaciones de UsoMaquina.
 * Usa sp_iniciar_uso_maquina y sp_finalizar_uso_maquina.
 *
 * CORRECCIÓN: PostgreSQL JDBC traduce {call ...} a SELECT internamente,
 * lo que falla con PROCEDURE. Se usa CALL directo con prepareStatement.
 *
 * @author Wilberto Ariza Zapata
 */
public class UsoMaquinaDAO implements IUsoMaquinaDAO {

    @Override
    public void iniciarUso(String clienteCedula, int idMaquina) {
        // ✅ CALL directo — {call ...} se traduce a SELECT y falla con PROCEDURE
        String sql = "CALL sp_iniciar_uso_maquina(?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, clienteCedula);
            ps.setInt(2, idMaquina);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al iniciar uso de máquina: " + e.getMessage(), e);
        }
    }

    @Override
    public void finalizarUso(int idUso) {
        // ✅ Mismo fix
        String sql = "CALL sp_finalizar_uso_maquina(?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idUso);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al finalizar uso de máquina: " + e.getMessage(), e);
        }
    }

    @Override
    public UsoMaquina buscarPorId(int idUso) {
        String sql = "SELECT * FROM uso_maquina WHERE id_uso = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idUso);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar uso: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<UsoMaquina> listarPorCliente(String clienteCedula) {
        List<UsoMaquina> lista = new ArrayList<>();
        String sql = "SELECT * FROM uso_maquina WHERE cedula = ? ORDER BY fecha_hora_inicio DESC";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, clienteCedula);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usos por cliente: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<UsoMaquina> listarPorMaquina(int idMaquina) {
        List<UsoMaquina> lista = new ArrayList<>();
        String sql = "SELECT * FROM uso_maquina WHERE id_maquina = ? ORDER BY fecha_hora_inicio DESC";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idMaquina);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usos por máquina: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<UsoMaquina> listarUsosActivos() {
        List<UsoMaquina> lista = new ArrayList<>();
        String sql = "SELECT * FROM uso_maquina WHERE fecha_hora_fin IS NULL ORDER BY fecha_hora_inicio DESC";
        try (Statement st = Conexion.getInstancia().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usos activos: " + e.getMessage(), e);
        }
        return lista;
    }

    private UsoMaquina mapear(ResultSet rs) throws SQLException {
        Timestamp fin = rs.getTimestamp("fecha_hora_fin");
        return new UsoMaquina(
                rs.getInt("id_uso"),
                rs.getInt("id_maquina"),
                rs.getTimestamp("fecha_hora_inicio").toLocalDateTime(),
                rs.getString("cedula"),
                fin != null ? fin.toLocalDateTime() : null
        );
    }
}