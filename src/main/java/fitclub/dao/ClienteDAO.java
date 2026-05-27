package fitclub.dao;

import fitclub.model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements IClienteDAO {

    private Connection con;

    public ClienteDAO() {
        this.con = Conexion.getInstancia();
    }

    @Override
    public void insertar(Cliente cliente) {
        String sqlPersona = "INSERT INTO persona (cedula, nombre, telefono) VALUES (?, ?, ?)";
        String sqlCliente = "INSERT INTO cliente (cedula, fecha_nacimiento, direccion) VALUES (?, ?, ?)";
        try {
            con.setAutoCommit(false);
            try (PreparedStatement psPersona = con.prepareStatement(sqlPersona);
                 PreparedStatement psCliente = con.prepareStatement(sqlCliente)) {

                psPersona.setString(1, cliente.getCedula());
                psPersona.setString(2, cliente.getNombre());
                psPersona.setString(3, cliente.getTelefono());
                psPersona.executeUpdate();

                psCliente.setString(1, cliente.getCedula());
                psCliente.setDate(2, Date.valueOf(cliente.getFechaNacimiento()));
                psCliente.setString(3, cliente.getDireccion());
                psCliente.executeUpdate();

                con.commit();
                System.out.println("Cliente insertado correctamente.");
            }
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { System.err.println("Error al revertir: " + ex.getMessage()); }
            System.err.println("Error al insertar cliente: " + e.getMessage());
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { System.err.println("Error al restaurar autocommit: " + e.getMessage()); }
        }
    }

    @Override
    public void actualizar(Cliente cliente) {
        String sqlPersona = "UPDATE persona SET nombre = ?, telefono = ? WHERE cedula = ?";
        String sqlCliente = "UPDATE cliente SET fecha_nacimiento = ?, direccion = ? WHERE cedula = ?";
        try {
            con.setAutoCommit(false);
            try (PreparedStatement psPersona = con.prepareStatement(sqlPersona);
                 PreparedStatement psCliente = con.prepareStatement(sqlCliente)) {

                psPersona.setString(1, cliente.getNombre());
                psPersona.setString(2, cliente.getTelefono());
                psPersona.setString(3, cliente.getCedula());
                psPersona.executeUpdate();

                psCliente.setDate(1, Date.valueOf(cliente.getFechaNacimiento()));
                psCliente.setString(2, cliente.getDireccion());
                psCliente.setString(3, cliente.getCedula());
                psCliente.executeUpdate();

                con.commit();
                System.out.println("Cliente actualizado correctamente.");
            }
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { System.err.println("Error al revertir: " + ex.getMessage()); }
            System.err.println("Error al actualizar cliente: " + e.getMessage());
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { System.err.println("Error al restaurar autocommit: " + e.getMessage()); }
        }
    }

    @Override
    public void eliminar(String cedula) {
        String sqlCliente = "DELETE FROM cliente WHERE cedula = ?";
        String sqlPersona = "DELETE FROM persona WHERE cedula = ?";
        try {
            con.setAutoCommit(false);
            try (PreparedStatement psCliente = con.prepareStatement(sqlCliente);
                 PreparedStatement psPersona = con.prepareStatement(sqlPersona)) {

                psCliente.setString(1, cedula);
                psCliente.executeUpdate();

                psPersona.setString(1, cedula);
                psPersona.executeUpdate();

                con.commit();
                System.out.println("Cliente eliminado correctamente.");
            }
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { System.err.println("Error al revertir: " + ex.getMessage()); }
            System.err.println("Error al eliminar cliente: " + e.getMessage());
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { System.err.println("Error al restaurar autocommit: " + e.getMessage()); }
        }
    }

    @Override
    public Cliente buscarPorCedula(String cedula) {
        String sql = "SELECT p.cedula, p.nombre, p.telefono, c.fecha_nacimiento, c.direccion " +
                "FROM persona p JOIN cliente c ON p.cedula = c.cedula " +
                "WHERE p.cedula = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getString("cedula"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getDate("fecha_nacimiento").toLocalDate(),
                            rs.getString("direccion")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT p.cedula, p.nombre, p.telefono, c.fecha_nacimiento, c.direccion " +
                "FROM persona p JOIN cliente c ON p.cedula = c.cedula";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getString("direccion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }
}