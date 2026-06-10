package fitclub.dao;

import fitclub.model.Membresia;
import fitclub.model.enums.TipoMembresia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para operaciones CRUD de Membresia.
 *
 *
 *
 * @author Wilberto Ariza Zapata
 */
public class MembresiaDAO implements IMembresiaDAO {



    private static final String SELECT_BASE =
            "SELECT cm.id            AS id_cliente_membresia, " +
                    "       m.id_membresia,                           " +
                    "       m.tipo,                                   " +
                    "       cm.fecha_inicio,                          " +
                    "       cm.fecha_fin                              " +
                    "FROM   cliente_membresia cm                      " +
                    "JOIN   membresia m ON m.id_membresia = cm.id_membresia ";

    @Override
    public void insertar(Membresia membresia, String clienteCedula) {
        String sql = "INSERT INTO cliente_membresia (cedula, id_membresia, fecha_inicio, fecha_fin) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, clienteCedula);
            ps.setInt(2, membresia.getIdMembresia());          // debe ser > 0
            ps.setDate(3, Date.valueOf(membresia.getFechaInicio()));
            ps.setDate(4, Date.valueOf(membresia.getFechaVencimiento()));
            ps.executeUpdate();
        } catch (SQLException e) {
            // Log de diagnóstico conservado para facilitar depuración
            System.err.println("[MembresiaDAO] Error al insertar membresía");
            System.err.println("  cedula       = " + clienteCedula);
            System.err.println("  id_membresia = " + membresia.getIdMembresia());
            System.err.println("  tipo         = " + membresia.getTipo());
            throw new RuntimeException("Error al insertar membresía: " + e.getMessage(), e);
        }
    }


    @Override
    public void actualizar(Membresia membresia) {
        // NOTA: la clave del WHERE es cm.id (PK de cliente_membresia),
        // NO id_membresia del catálogo.  Para eso usamos getIdClienteMembresia()
        // si lo tuvieras, o pasas el id de registro como parámetro extra.
        // Por ahora se actualiza por id_membresia del catálogo (según diseño original).
        String sql = "UPDATE cliente_membresia " +
                "SET    fecha_inicio = ?, fecha_fin = ? " +
                "WHERE  id = ?";
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
    public void cancelar(int idRegistroMembresia) {
        String sql = "UPDATE cliente_membresia SET estado = 'cancelada' WHERE id = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idRegistroMembresia);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al cancelar membresía: " + e.getMessage(), e);
        }
    }

    @Override
    public Membresia buscarPorId(int idRegistroMembresia) {
        String sql = SELECT_BASE + "WHERE cm.id = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idRegistroMembresia);
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
        String sql = SELECT_BASE + "WHERE cm.cedula = ?";
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
        String sql = SELECT_BASE + "WHERE cm.cedula = ? AND cm.estado = 'activa'";
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
                rs.getInt("id_cliente_membresia"),                            // ✅ cm.id — para cancelar/actualizar
                TipoMembresia.valueOf(rs.getString("tipo").toUpperCase()),
                rs.getDate("fecha_inicio").toLocalDate(),
                rs.getDate("fecha_fin").toLocalDate()
        );
    }
}