package fitclub.dao;

import fitclub.model.Pago;
import fitclub.model.enums.MetodoPago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO implements IPagoDAO {

    @Override
    public void insertar(Pago pago, int membresiaId) {
        String sql = "INSERT INTO pago (cedula, monto, metodo_pago, concepto) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, pago.getCedula());
            ps.setDouble(2, pago.getMonto());
            ps.setObject(3, pago.getMetodoPago().toSQL(), Types.OTHER);
            ps.setString(4, pago.getConcepto());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar pago: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Pago pago) {
        String sql = "UPDATE pago SET monto = ?, metodo_pago = ?, concepto = ? WHERE id_pago = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setDouble(1, pago.getMonto());
            ps.setObject(2, pago.getMetodoPago().toSQL(), Types.OTHER);
            ps.setString(3, pago.getConcepto());
            ps.setInt(4, pago.getIdPago());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar pago: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int idPago) {
        String sql = "DELETE FROM pago WHERE id_pago = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idPago);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar pago: " + e.getMessage(), e);
        }
    }

    @Override
    public Pago buscarPorId(int idPago) {
        String sql = "SELECT * FROM pago WHERE id_pago = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idPago);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar pago: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Pago> listarPorMembresia(int membresiaId) {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pago WHERE cedula = " +
                "(SELECT cedula FROM cliente_membresia WHERE id = ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, membresiaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pagos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Pago> listarTodos() {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pago ORDER BY fecha_pago DESC";
        try (Statement st = Conexion.getInstancia().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pagos: " + e.getMessage(), e);
        }
        return lista;
    }

    private Pago mapear(ResultSet rs) throws SQLException {
        return new Pago(
                rs.getInt("id_pago"),
                rs.getString("cedula"),
                rs.getDouble("monto"),
                rs.getTimestamp("fecha_pago").toLocalDateTime().toLocalDate(),
                MetodoPago.valueOf(rs.getString("metodo_pago").toUpperCase()),
                rs.getString("concepto")
        );
    }
}