package fitclub.view;

import fitclub.dao.MaquinaDAO;
import fitclub.model.Maquina;
import fitclub.model.enums.EstadoMaquina;
import fitclub.service.MaquinaService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de gestión de máquinas del gimnasio Fit Club.
 *
 * @author Wilberto Ariza Zapata
 */
public class MaquinaForm extends JPanel {

    private JTextField txtNombre;
    private JTextField txtTipo;
    private JTextField txtUbicacion;
    private JComboBox<EstadoMaquina> cmbEstado;
    private JTable tablaMaquinas;
    private DefaultTableModel modeloTabla;
    private int idSeleccionado = -1;

    private final MaquinaService maquinaService;

    public MaquinaForm() {
        this.maquinaService = new MaquinaService(new MaquinaDAO());
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(242, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("🏋️  Gestión de Máquinas");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(31, 56, 100));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        // -----------------------------------------------------------------
        // Panel de campos
        // -----------------------------------------------------------------
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBackground(Color.WHITE);
        panelCampos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panelCampos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; txtNombre = new JTextField(15);
        panelCampos.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelCampos.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; txtTipo = new JTextField(15);
        panelCampos.add(txtTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelCampos.add(new JLabel("Ubicación:"), gbc);
        gbc.gridx = 1; txtUbicacion = new JTextField(15);
        panelCampos.add(txtUbicacion, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelCampos.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; cmbEstado = new JComboBox<>(EstadoMaquina.values());
        panelCampos.add(cmbEstado, gbc);

        // -----------------------------------------------------------------
        // Botones
        // -----------------------------------------------------------------
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        JButton btnGuardar    = crearBoton("Guardar",     new Color(46, 95, 163));
        JButton btnActualizar = crearBoton("Actualizar",  new Color(46, 95, 163));
        JButton btnDesactivar = crearBoton("Desactivar",  new Color(180, 50, 50));
        JButton btnLimpiar    = crearBoton("Limpiar",     new Color(100, 100, 100));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnDesactivar);
        panelBotones.add(btnLimpiar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(242, 245, 250));
        panelSuperior.add(panelCampos, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.WEST);

        // -----------------------------------------------------------------
        // Tabla
        // -----------------------------------------------------------------
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Tipo", "Ubicación", "Estado", "Activa"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaMaquinas = new JTable(modeloTabla);
        tablaMaquinas.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaMaquinas.setRowHeight(28);
        tablaMaquinas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaMaquinas.getTableHeader().setBackground(new Color(46, 95, 163));
        tablaMaquinas.getTableHeader().setForeground(Color.WHITE);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247)),
                "Máquinas registradas", 0, 0,
                new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)));
        panelTabla.add(new JScrollPane(tablaMaquinas), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        // -----------------------------------------------------------------
        // Listeners
        // -----------------------------------------------------------------
        btnGuardar.addActionListener(e -> guardarMaquina());
        btnActualizar.addActionListener(e -> actualizarMaquina());
        btnDesactivar.addActionListener(e -> desactivarMaquina());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaMaquinas.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaMaquinas.getSelectedRow();
            if (fila >= 0) {
                idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
                txtTipo.setText((String) modeloTabla.getValueAt(fila, 2));
                txtUbicacion.setText((String) modeloTabla.getValueAt(fila, 3));
                cmbEstado.setSelectedItem(EstadoMaquina.valueOf(
                        modeloTabla.getValueAt(fila, 4).toString().toUpperCase()));
            }
        });
    }

    // -------------------------------------------------------------------------
    // Acciones
    // -------------------------------------------------------------------------

    private void guardarMaquina() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Maquina maquina = new Maquina(
                    0,
                    txtNombre.getText().trim(),
                    txtTipo.getText().trim(),
                    txtUbicacion.getText().trim(),
                    (EstadoMaquina) cmbEstado.getSelectedItem(),
                    true
            );
            maquinaService.registrarMaquina(maquina);
            JOptionPane.showMessageDialog(this, "Máquina registrada correctamente.");
            cargarTabla();
            limpiarCampos();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarMaquina() {
        if (idSeleccionado < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una máquina de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Maquina maquina = new Maquina(
                    idSeleccionado,
                    txtNombre.getText().trim(),
                    txtTipo.getText().trim(),
                    txtUbicacion.getText().trim(),
                    (EstadoMaquina) cmbEstado.getSelectedItem(),
                    true
            );
            maquinaService.actualizarMaquina(maquina);
            JOptionPane.showMessageDialog(this, "Máquina actualizada correctamente.");
            cargarTabla();
            limpiarCampos();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void desactivarMaquina() {
        if (idSeleccionado < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una máquina de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Desactivar esta máquina?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                maquinaService.desactivarMaquina(idSeleccionado);
                JOptionPane.showMessageDialog(this, "Máquina desactivada correctamente.");
                cargarTabla();
                limpiarCampos();
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            maquinaService.listarTodas().forEach(m ->
                    modeloTabla.addRow(new Object[]{
                            m.getIdMaquina(),
                            m.getNombre(),
                            m.getTipo(),
                            m.getUbicacion(),
                            m.getEstado().name(),
                            m.isActiva() ? "✅" : "❌"
                    }));
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtTipo.setText("");
        txtUbicacion.setText("");
        cmbEstado.setSelectedIndex(0);
        idSeleccionado = -1;
        tablaMaquinas.clearSelection();
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