package fitclub.view;

import fitclub.dao.ClienteDAO;
import fitclub.model.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Panel de gestión de clientes del gimnasio Fit Club.
 *
 * @author Wilberto Ariza Zapata
 */
public class ClienteForm extends JPanel {

    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtFechaNacimiento;
    private JTextField txtDireccion;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    private ClienteDAO clienteDAO;

    public ClienteForm() {
        clienteDAO = new ClienteDAO();
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(242, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== TÍTULO =====
        JLabel titulo = new JLabel("👥  Gestión de Clientes");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(31, 56, 100));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        // ===== PANEL DE CAMPOS =====
        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 10, 10));
        panelCampos.setBackground(Color.WHITE);
        panelCampos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        panelCampos.add(new JLabel("Cédula:"));
        txtCedula = new JTextField();
        panelCampos.add(txtCedula);

        panelCampos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelCampos.add(txtNombre);

        panelCampos.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelCampos.add(txtTelefono);

        panelCampos.add(new JLabel("Fecha Nacimiento (YYYY-MM-DD):"));
        txtFechaNacimiento = new JTextField();
        panelCampos.add(txtFechaNacimiento);

        panelCampos.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panelCampos.add(txtDireccion);

        // ===== PANEL DE BOTONES =====
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(Color.WHITE);

        JButton btnGuardar = crearBoton("Guardar", new Color(46, 95, 163));
        JButton btnBuscar = crearBoton("Buscar", new Color(46, 95, 163));
        JButton btnEliminar = crearBoton("Eliminar", new Color(180, 50, 50));
        JButton btnLimpiar = crearBoton("Limpiar", new Color(100, 100, 100));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(242, 245, 250));
        panelSuperior.add(panelCampos, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.WEST);

        // ===== TABLA =====
        modeloTabla = new DefaultTableModel(
                new String[]{"Cédula", "Nombre", "Teléfono", "Fecha Nac.", "Dirección"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaClientes.setRowHeight(28);
        tablaClientes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaClientes.getTableHeader().setBackground(new Color(46, 95, 163));
        tablaClientes.getTableHeader().setForeground(Color.WHITE);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247)),
                "Clientes registrados", 0, 0,
                new Font("Arial", Font.BOLD, 13),
                new Color(31, 56, 100)
        ));
        panelTabla.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        // ===== ACCIONES =====
        btnGuardar.addActionListener(e -> guardarCliente());
        btnBuscar.addActionListener(e -> buscarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaClientes.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaClientes.getSelectedRow();
            if (fila >= 0) {
                txtCedula.setText((String) modeloTabla.getValueAt(fila, 0));
                txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
                txtTelefono.setText((String) modeloTabla.getValueAt(fila, 2));
                txtFechaNacimiento.setText((String) modeloTabla.getValueAt(fila, 3));
                txtDireccion.setText((String) modeloTabla.getValueAt(fila, 4));
            }
        });
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        clienteDAO.listarTodos().forEach(c -> modeloTabla.addRow(new Object[]{
                c.getCedula(),
                c.getNombre(),
                c.getTelefono(),
                c.getFechaNacimiento().toString(),
                c.getDireccion()
        }));
    }

    private void guardarCliente() {
        if (txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cédula y nombre son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Cliente cliente = new Cliente(
                    txtCedula.getText(),
                    txtNombre.getText(),
                    txtTelefono.getText(),
                    LocalDate.parse(txtFechaNacimiento.getText()),
                    txtDireccion.getText()
            );
            clienteDAO.insertar(cliente);
            JOptionPane.showMessageDialog(this, "Cliente guardado correctamente.");
            limpiarCampos();
            cargarTabla();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarCliente() {
        if (txtCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cédula para buscar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Cliente c = clienteDAO.buscarPorCedula(txtCedula.getText());
        if (c != null) {
            txtNombre.setText(c.getNombre());
            txtTelefono.setText(c.getTelefono());
            txtFechaNacimiento.setText(c.getFechaNacimiento().toString());
            txtDireccion.setText(c.getDireccion());
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void eliminarCliente() {
        if (txtCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cédula para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            clienteDAO.eliminar(txtCedula.getText());
            JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.");
            limpiarCampos();
            cargarTabla();
        }
    }

    private void limpiarCampos() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtFechaNacimiento.setText("");
        txtDireccion.setText("");
    }
}