package fitclub.view;

import fitclub.dao.EntrenadorDAO;
import fitclub.model.Entrenador;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Panel de gestión de entrenadores del gimnasio Fit Club.
 *
 * @author Wilberto Ariza Zapata
 */
public class EntrenadorForm extends JPanel {

    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtEspecialidad;
    private JTextField txtHorario;
    private JTable tablaEntrenadores;
    private DefaultTableModel modeloTabla;

    private EntrenadorDAO entrenadorDAO;

    public EntrenadorForm() {
        entrenadorDAO = new EntrenadorDAO();
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(242, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== TÍTULO =====
        JLabel titulo = new JLabel("💪  Gestión de Entrenadores");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(31, 56, 100));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        // ===== PANEL DE CAMPOS =====
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBackground(Color.WHITE);
        panelCampos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panelCampos.add(new JLabel("Cédula:"), gbc);
        gbc.gridx = 1; txtCedula = new JTextField(15);
        panelCampos.add(txtCedula, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelCampos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; txtNombre = new JTextField(15);
        panelCampos.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelCampos.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; txtTelefono = new JTextField(15);
        panelCampos.add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelCampos.add(new JLabel("Especialidad:"), gbc);
        gbc.gridx = 1; txtEspecialidad = new JTextField(15);
        panelCampos.add(txtEspecialidad, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelCampos.add(new JLabel("Horario:"), gbc);
        gbc.gridx = 1; txtHorario = new JTextField(15);
        panelCampos.add(txtHorario, gbc);

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
                new String[]{"Cédula", "Nombre", "Teléfono", "Especialidad", "Horario"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaEntrenadores = new JTable(modeloTabla);
        tablaEntrenadores.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaEntrenadores.setRowHeight(28);
        tablaEntrenadores.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaEntrenadores.getTableHeader().setBackground(new Color(46, 95, 163));
        tablaEntrenadores.getTableHeader().setForeground(Color.WHITE);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247)),
                "Entrenadores registrados", 0, 0,
                new Font("Arial", Font.BOLD, 13),
                new Color(31, 56, 100)
        ));
        panelTabla.add(new JScrollPane(tablaEntrenadores), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        // ===== ACCIONES =====
        btnGuardar.addActionListener(e -> guardarEntrenador());
        btnBuscar.addActionListener(e -> buscarEntrenador());
        btnEliminar.addActionListener(e -> eliminarEntrenador());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaEntrenadores.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaEntrenadores.getSelectedRow();
            if (fila >= 0) {
                txtCedula.setText((String) modeloTabla.getValueAt(fila, 0));
                txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
                txtTelefono.setText((String) modeloTabla.getValueAt(fila, 2));
                txtEspecialidad.setText((String) modeloTabla.getValueAt(fila, 3));
                txtHorario.setText((String) modeloTabla.getValueAt(fila, 4));
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
        entrenadorDAO.listarTodos().forEach(e -> modeloTabla.addRow(new Object[]{
                e.getCedula(),
                e.getNombre(),
                e.getTelefono(),
                e.getEspecialidad(),
                e.getHorario()
        }));
    }

    private void guardarEntrenador() {
        if (txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cédula y nombre son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Entrenador entrenador = new Entrenador(
                txtCedula.getText(),
                txtNombre.getText(),
                txtTelefono.getText(),
                txtEspecialidad.getText(),
                txtHorario.getText()
        );
        entrenadorDAO.insertar(entrenador);
        JOptionPane.showMessageDialog(this, "Entrenador guardado correctamente.");
        limpiarCampos();
        cargarTabla();
    }

    private void buscarEntrenador() {
        if (txtCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cédula para buscar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Entrenador e = entrenadorDAO.buscarPorCedula(txtCedula.getText());
        if (e != null) {
            txtNombre.setText(e.getNombre());
            txtTelefono.setText(e.getTelefono());
            txtEspecialidad.setText(e.getEspecialidad());
            txtHorario.setText(e.getHorario());
        } else {
            JOptionPane.showMessageDialog(this, "Entrenador no encontrado.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void eliminarEntrenador() {
        if (txtCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cédula para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este entrenador?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            entrenadorDAO.eliminar(txtCedula.getText());
            JOptionPane.showMessageDialog(this, "Entrenador eliminado correctamente.");
            limpiarCampos();
            cargarTabla();
        }
    }

    private void limpiarCampos() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEspecialidad.setText("");
        txtHorario.setText("");
    }
}