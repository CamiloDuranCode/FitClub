package fitclub.dao;

import fitclub.model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements IClienteDAO {

    @Override
    public void insertar(Cliente cliente) {
        String sqlPersona = "INSERT INTO persona (cedula, nombre, telefono) VALUES (?, ?, ?)";
        String sqlCliente = "INSERT INTO cliente (cedula, fecha_nacimiento, direccion) VALUES (?, ?, ?)";
        Connection con = Conexion.getInstancia();
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
            }
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Error al insertar cliente: " + e.getMessage(), e);
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void actualizar(Cliente cliente) {
        String sqlPersona = "UPDATE persona SET nombre = ?, telefono = ? WHERE cedula = ?";
        String sqlCliente = "UPDATE cliente SET fecha_nacimiento = ?, direccion = ? WHERE cedula = ?";
        Connection con = Conexion.getInstancia();
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
            }
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Error al actualizar cliente: " + e.getMessage(), e);
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void desactivar(String cedula) {
        String sql = "UPDATE cliente SET activo = false WHERE cedula = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, cedula);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public Cliente buscarPorCedula(String cedula) {
        String sql = "SELECT p.cedula, p.nombre, p.telefono, c.fecha_nacimiento, c.direccion " +
                "FROM persona p JOIN cliente c ON p.cedula = c.cedula WHERE p.cedula = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
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
            throw new RuntimeException("Error al buscar cliente: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT p.cedula, p.nombre, p.telefono, c.fecha_nacimiento, c.direccion " +
                "FROM persona p JOIN cliente c ON p.cedula = c.cedula";
        try (Statement st = Conexion.getInstancia().createStatement();
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
            throw new RuntimeException("Error al listar clientes: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Cliente> listarActivos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT p.cedula, p.nombre, p.telefono, c.fecha_nacimiento, c.direccion " +
                "FROM persona p JOIN cliente c ON p.cedula = c.cedula " +
                "WHERE c.activo = true";
        try (Statement st = Conexion.getInstancia().createStatement();
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
            throw new RuntimeException("Error al listar clientes activos: " + e.getMessage(), e);
        }
        return lista;
    }
}