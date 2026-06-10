package fitclub.view;

import fitclub.dao.UsuarioDAO;
import fitclub.model.Usuario;
import fitclub.model.enums.RolUsuario;
import fitclub.service.UsuarioService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de administración de usuarios del sistema Fit Club.
 * Solo accesible para usuarios con rol ADMIN.
 *
 * @author Wilberto Ariza Zapata
 */
public class UsuarioAdminPanel extends JPanel {

    private JTextField txtUsername;
    private JTextField txtNombre;
    private JPasswordField txtPassword;
    private JComboBox<RolUsuario> cmbRol;
    private JTextField txtCedulaEntrenador;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    private final UsuarioService usuarioService;

    public UsuarioAdminPanel() {
        this.usuarioService = new UsuarioService(new UsuarioDAO());
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(242, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("🔑  Administración de Usuarios");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(31, 56, 100));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBackground(Color.WHITE);
        panelCampos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panelCampos.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; txtUsername = new JTextField(15);
        panelCampos.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelCampos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; txtNombre = new JTextField(15);
        panelCampos.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelCampos.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; txtPassword = new JPasswordField(15);
        panelCampos.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelCampos.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1; cmbRol = new JComboBox<>(RolUsuario.values());
        panelCampos.add(cmbRol, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelCampos.add(new JLabel("Cédula Entrenador:"), gbc);
        gbc.gridx = 1; txtCedulaEntrenador = new JTextField(15);
        txtCedulaEntrenador.setToolTipText("Solo requerido si el rol es ENTRENADOR");
        panelCampos.add(txtCedulaEntrenador, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        JButton btnRegistrar = crearBoton("Registrar",         new Color(46, 95, 163));
        JButton btnToggle    = crearBoton("Activar/Desactivar", new Color(180, 130, 30));
        JButton btnLimpiar   = crearBoton("Limpiar",            new Color(100, 100, 100));
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnToggle);
        panelBotones.add(btnLimpiar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(242, 245, 250));
        panelSuperior.add(panelCampos, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.WEST);

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Username", "Nombre", "Rol", "Cédula Entrenador"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaUsuarios.setRowHeight(28);
        tablaUsuarios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaUsuarios.getTableHeader().setBackground(new Color(46, 95, 163));
        tablaUsuarios.getTableHeader().setForeground(Color.WHITE);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247)),
                "Usuarios registrados", 0, 0,
                new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)));
        panelTabla.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrarUsuario());
        btnToggle.addActionListener(e -> toggleUsuario());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila >= 0) {
                txtUsername.setText((String) modeloTabla.getValueAt(fila, 1));
                txtNombre.setText((String) modeloTabla.getValueAt(fila, 2));
                cmbRol.setSelectedItem(RolUsuario.valueOf(
                        modeloTabla.getValueAt(fila, 3).toString().toUpperCase()));
                String cedula = (String) modeloTabla.getValueAt(fila, 4);
                txtCedulaEntrenador.setText(cedula != null ? cedula : "");
            }
        });
    }

    private void registrarUsuario() {
        String username = txtUsername.getText().trim();
        String nombre   = txtNombre.getText().trim();
        String password = new String(txtPassword.getPassword());
        RolUsuario rol  = (RolUsuario) cmbRol.getSelectedItem();
        String cedula   = txtCedulaEntrenador.getText().trim();

        if (username.isEmpty() || nombre.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username, nombre y contraseña son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (rol == RolUsuario.ENTRENADOR && cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Un usuario con rol ENTRENADOR debe tener cédula de entrenador.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Usuario nuevo = new Usuario(0, username, null, rol, nombre, true,
                    cedula.isEmpty() ? null : cedula);
            usuarioService.registrar(nuevo, password);
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
            cargarTabla();
            limpiarCampos();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void toggleUsuario() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un usuario de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idUsuario = (int) modeloTabla.getValueAt(fila, 0);
        try {
            usuarioService.toggleActivo(idUsuario);
            JOptionPane.showMessageDialog(this, "Estado del usuario actualizado.");
            cargarTabla();
            limpiarCampos();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            for (Usuario u : usuarios) {
                modeloTabla.addRow(new Object[]{
                        u.getIdUsuario(),
                        u.getUsername(),
                        u.getNombre(),
                        u.getRol().name(),
                        u.getCedulaEntrenador()
                });
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtUsername.setText("");
        txtNombre.setText("");
        txtPassword.setText("");
        cmbRol.setSelectedIndex(0);
        txtCedulaEntrenador.setText("");
        tablaUsuarios.clearSelection();
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
}