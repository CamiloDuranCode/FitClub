package fitclub.dao;

import fitclub.model.Membresia;
import fitclub.model.enums.TipoMembresia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para operaciones CRUD de Membresia.
 *
 * @author Wilberto Ariza Zapata
 */
public class MembresiaDAO implements IMembresiaDAO {

    @Override
    public void insertar(Membresia membresia, String clienteCedula) {
        String sql = "INSERT INTO cliente_membresia (cedula, id_membresia, fecha_inicio, fecha_fin) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, clienteCedula);
            ps.setInt(2, membresia.getIdMembresia());
            ps.setDate(3, Date.valueOf(membresia.getFechaInicio()));
            ps.setDate(4, Date.valueOf(membresia.getFechaVencimiento()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar membresía: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Membresia membresia) {
        String sql = "UPDATE cliente_membresia SET fecha_inicio = ?, fecha_fin = ? WHERE id = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(membresia.getFechaInicio()));
            ps.setDate(2, Date.valueOf(membresia.getFechaVencimiento()));
            ps.setInt(3, membresia.getIdMembresia());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar membresía: " + e.getMessage(), e);
        }
    }

    @Override
    public void cancelar(int idMembresia) {
        String sql = "UPDATE cliente_membresia SET estado = 'cancelada' WHERE id = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idMembresia);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al cancelar membresía: " + e.getMessage(), e);
        }
    }

    @Override
    public Membresia buscarPorId(int idMembresia) {
        String sql = "SELECT cm.id, m.tipo, cm.fecha_inicio, cm.fecha_fin " +
                "FROM cliente_membresia cm " +
                "JOIN membresia m ON m.id_membresia = cm.id_membresia " +
                "WHERE cm.id = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idMembresia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar membresía: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Membresia> listarPorCliente(String clienteCedula) {
        List<Membresia> lista = new ArrayList<>();
        String sql = "SELECT cm.id, m.tipo, cm.fecha_inicio, cm.fecha_fin " +
                "FROM cliente_membresia cm " +
                "JOIN membresia m ON m.id_membresia = cm.id_membresia " +
                "WHERE cm.cedula = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, clienteCedula);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar membresías: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Membresia> listarActivasPorCliente(String clienteCedula) {
        List<Membresia> lista = new ArrayList<>();
        String sql = "SELECT cm.id, m.tipo, cm.fecha_inicio, cm.fecha_fin " +
                "FROM cliente_membresia cm " +
                "JOIN membresia m ON m.id_membresia = cm.id_membresia " +
                "WHERE cm.cedula = ? AND cm.estado = 'activa'";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, clienteCedula);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar membresías activas: " + e.getMessage(), e);
        }
        return lista;
    }

    private Membresia mapear(ResultSet rs) throws SQLException {
        return new Membresia(
                rs.getInt("id"),
                TipoMembresia.valueOf(rs.getString("tipo").toUpperCase()),
                rs.getDate("fecha_inicio").toLocalDate(),
                rs.getDate("fecha_fin").toLocalDate()
        );
    }
}