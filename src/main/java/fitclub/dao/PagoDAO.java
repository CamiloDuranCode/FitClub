package fitclub.dao;

import fitclub.model.Pago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para operaciones CRUD de Pago.
 *
 * @author Wilberto Ariza Zapata
 */
public class PagoDAO implements IPagoDAO {

    private Connection con;

    public PagoDAO() {
        this.con = Conexion.getInstancia();
    }

    @Override
    public void insertar(Pago pago, int membresiaId) {
        String sql = "INSERT INTO pago (membresia_id, monto, fecha_pago, metodo_pago) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, membresiaId);
            ps.setDouble(2, pago.getMonto());
            ps.setDate(3, Date.valueOf(pago.getFechaPago()));
            ps.setString(4, pago.getMetodoPago());
            ps.executeUpdate();
            System.out.println("Pago insertado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar pago: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Pago pago) {
        String sql = "UPDATE pago SET monto = ?, fecha_pago = ?, metodo_pago = ? WHERE id_pago = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, pago.getMonto());
            ps.setDate(2, Date.valueOf(pago.getFechaPago()));
            ps.setString(3, pago.getMetodoPago());
            ps.setInt(4, pago.getIdPago());
            ps.executeUpdate();
            System.out.println("Pago actualizado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar pago: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int idPago) {
        String sql = "DELETE FROM pago WHERE id_pago = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPago);
            ps.executeUpdate();
            System.out.println("Pago eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar pago: " + e.getMessage());
        }
    }

    @Override
    public Pago buscarPorId(int idPago) {
        String sql = "SELECT * FROM pago WHERE id_pago = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPago);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Pago(
                            rs.getInt("id_pago"),
                            rs.getDouble("monto"),
                            rs.getDate("fecha_pago").toLocalDate(),
                            rs.getString("metodo_pago")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar pago: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Pago> listarPorMembresia(int membresiaId) {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pago WHERE membresia_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, membresiaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Pago(
                            rs.getInt("id_pago"),
                            rs.getDouble("monto"),
                            rs.getDate("fecha_pago").toLocalDate(),
                            rs.getString("metodo_pago")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pagos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Pago> listarTodos() {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pago";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Pago(
                        rs.getInt("id_pago"),
                        rs.getDouble("monto"),
                        rs.getDate("fecha_pago").toLocalDate(),
                        rs.getString("metodo_pago")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pagos: " + e.getMessage());
        }
        return lista;
    }
}