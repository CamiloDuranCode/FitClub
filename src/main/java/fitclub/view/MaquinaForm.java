package fitclub.view;

import fitclub.dao.MaquinaDAO;
import fitclub.dao.UsoMaquinaDAO;
import fitclub.model.Maquina;
import fitclub.model.UsoMaquina;
import fitclub.model.enums.EstadoMaquina;
import fitclub.service.MaquinaService;
import fitclub.service.UsoMaquinaService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel de gestión de máquinas del gimnasio Fit Club.
 * Permite CRUD de máquinas y gestión de uso actual por cliente.
 * El estado se colorea: verde = disponible, naranja = en uso, rojo = mantenimiento.
 *
 * @author Wilberto Ariza Zapata
 */
public class MaquinaForm extends JPanel {

    private JTextField txtIdMaquina;
    private JTextField txtNombre;
    private JTextField txtTipo;
    private JTextField txtUbicacion;
    private JTextField txtCedulaUso;
    private JTextField txtIdUso;
    private JTable tablaMaquinas;
    private JTable tablaUsos;
    private DefaultTableModel modeloMaquinas;
    private DefaultTableModel modeloUsos;

    private final MaquinaService maquinaService;
    private final UsoMaquinaService usoMaquinaService;

    public MaquinaForm() {
        MaquinaDAO maquinaDAO = new MaquinaDAO();
        UsoMaquinaDAO usoMaquinaDAO = new UsoMaquinaDAO();
        this.maquinaService = new MaquinaService(maquinaDAO);
        this.usoMaquinaService = new UsoMaquinaService(usoMaquinaDAO, maquinaService);
        initComponents();
        cargarTablas();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(242, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== TÍTULO =====
        JLabel titulo = new JLabel("🏋️  Gestión de Máquinas");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(31, 56, 100));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        // ===== PANEL IZQUIERDO (formularios) =====
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(new Color(242, 245, 250));

        // --- Formulario CRUD ---
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBackground(Color.WHITE);
        panelCampos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(214, 228, 247)),
                        "Datos de la Máquina", 0, 0,
                        new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panelCampos.add(new JLabel("ID (solo para buscar/desactivar):"), gbc);
        gbc.gridx = 1; txtIdMaquina = new JTextField(15);
        panelCampos.add(txtIdMaquina, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelCampos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; txtNombre = new JTextField(15);
        panelCampos.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelCampos.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; txtTipo = new JTextField(15);
        panelCampos.add(txtTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelCampos.add(new JLabel("Ubicación:"), gbc);
        gbc.gridx = 1; txtUbicacion = new JTextField(15);
        panelCampos.add(txtUbicacion, gbc);

        JPanel panelBotonesCRUD = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelBotonesCRUD.setBackground(Color.WHITE);
        JButton btnGuardar    = crearBoton("Guardar",     new Color(46, 95, 163));
        JButton btnDesactivar = crearBoton("Desactivar",  new Color(180, 50, 50));
        JButton btnLimpiar    = crearBoton("Limpiar",     new Color(100, 100, 100));
        panelBotonesCRUD.add(btnGuardar);
        panelBotonesCRUD.add(btnDesactivar);
        panelBotonesCRUD.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panelCampos.add(panelBotonesCRUD, gbc);

        // --- Formulario USO ACTUAL ---
        JPanel panelUso = new JPanel(new GridBagLayout());
        panelUso.setBackground(Color.WHITE);
        panelUso.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(214, 228, 247)),
                        "Uso Actual", 0, 0,
                        new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);
        gbc2.anchor = GridBagConstraints.WEST;

        gbc2.gridx = 0; gbc2.gridy = 0;
        panelUso.add(new JLabel("Cédula Cliente:"), gbc2);
        gbc2.gridx = 1; txtCedulaUso = new JTextField(15);
        panelUso.add(txtCedulaUso, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 1;
        panelUso.add(new JLabel("ID Uso (para finalizar):"), gbc2);
        gbc2.gridx = 1; txtIdUso = new JTextField(15);
        panelUso.add(txtIdUso, gbc2);

        JPanel panelBotonesUso = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelBotonesUso.setBackground(Color.WHITE);
        JButton btnIniciarUso   = crearBoton("Iniciar Uso",   new Color(46, 163, 95));
        JButton btnFinalizarUso = crearBoton("Finalizar Uso", new Color(163, 95, 46));
        panelBotonesUso.add(btnIniciarUso);
        panelBotonesUso.add(btnFinalizarUso);

        gbc2.gridx = 0; gbc2.gridy = 2; gbc2.gridwidth = 2;
        panelUso.add(panelBotonesUso, gbc2);

        panelIzquierdo.add(panelCampos);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(panelUso);
        add(panelIzquierdo, BorderLayout.WEST);

        // ===== PANEL DERECHO (tablas) =====
        JPanel panelTablas = new JPanel(new GridLayout(2, 1, 0, 10));
        panelTablas.setBackground(new Color(242, 245, 250));

        // Tabla máquinas
        modeloMaquinas = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Tipo", "Ubicación", "Estado", "Activa"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaMaquinas = new JTable(modeloMaquinas);
        tablaMaquinas.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaMaquinas.setRowHeight(28);
        tablaMaquinas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaMaquinas.getTableHeader().setBackground(new Color(46, 95, 163));
        tablaMaquinas.getTableHeader().setForeground(Color.WHITE);
        tablaMaquinas.getColumnModel().getColumn(4).setCellRenderer(new EstadoColorRenderer());

        JPanel panelTablaMaquinas = new JPanel(new BorderLayout());
        panelTablaMaquinas.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247)),
                "Máquinas registradas", 0, 0,
                new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)));
        panelTablaMaquinas.add(new JScrollPane(tablaMaquinas), BorderLayout.CENTER);

        // Tabla usos activos
        modeloUsos = new DefaultTableModel(
                new String[]{"ID Uso", "ID Máquina", "Cédula Cliente", "Inicio"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaUsos = new JTable(modeloUsos);
        tablaUsos.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaUsos.setRowHeight(28);
        tablaUsos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaUsos.getTableHeader().setBackground(new Color(46, 95, 163));
        tablaUsos.getTableHeader().setForeground(Color.WHITE);

        JPanel panelTablaUsos = new JPanel(new BorderLayout());
        panelTablaUsos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247)),
                "Usos activos", 0, 0,
                new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)));
        panelTablaUsos.add(new JScrollPane(tablaUsos), BorderLayout.CENTER);

        panelTablas.add(panelTablaMaquinas);
        panelTablas.add(panelTablaUsos);
        add(panelTablas, BorderLayout.CENTER);

        // ===== LISTENERS =====
        btnGuardar.addActionListener(e -> guardarMaquina());
        btnDesactivar.addActionListener(e -> desactivarMaquina());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnIniciarUso.addActionListener(e -> iniciarUso());
        btnFinalizarUso.addActionListener(e -> finalizarUso());

        tablaMaquinas.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaMaquinas.getSelectedRow();
            if (fila >= 0) {
                txtIdMaquina.setText(String.valueOf(modeloMaquinas.getValueAt(fila, 0)));
                txtNombre.setText((String) modeloMaquinas.getValueAt(fila, 1));
                txtTipo.setText((String) modeloMaquinas.getValueAt(fila, 2));
                txtUbicacion.setText((String) modeloMaquinas.getValueAt(fila, 3));
            }
        });

        tablaUsos.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaUsos.getSelectedRow();
            if (fila >= 0)
                txtIdUso.setText(String.valueOf(modeloUsos.getValueAt(fila, 0)));
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

    private void cargarTablas() {
        cargarTablaMaquinas();
        cargarTablaUsos();
    }

    private void cargarTablaMaquinas() {
        modeloMaquinas.setRowCount(0);
        maquinaService.listarTodas().forEach(m -> modeloMaquinas.addRow(new Object[]{
                m.getIdMaquina(),
                m.getNombre(),
                m.getTipo(),
                m.getUbicacion(),
                m.getEstado().name(),
                m.isActiva() ? "✅" : "❌"
        }));
    }

    private void cargarTablaUsos() {
        modeloUsos.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        usoMaquinaService.listarUsosActivos().forEach(u -> modeloUsos.addRow(new Object[]{
                u.getIdUso(),
                u.getIdMaquina(),
                u.getCedulaCliente(),
                u.getFechaHoraInicio().format(fmt)
        }));
    }

    private void guardarMaquina() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Maquina maquina = new Maquina(0, txtNombre.getText().trim(),
                    txtTipo.getText().trim(), txtUbicacion.getText().trim(),
                    EstadoMaquina.DISPONIBLE, true);
            maquinaService.registrarMaquina(maquina);
            JOptionPane.showMessageDialog(this, "Máquina registrada correctamente.");
            limpiarCampos();
            cargarTablaMaquinas();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void desactivarMaquina() {
        if (txtIdMaquina.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una máquina de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int confirmar = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de desactivar esta máquina?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                maquinaService.desactivarMaquina(Integer.parseInt(txtIdMaquina.getText()));
                JOptionPane.showMessageDialog(this, "Máquina desactivada correctamente.");
                limpiarCampos();
                cargarTablaMaquinas();
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void iniciarUso() {
        if (txtIdMaquina.getText().trim().isEmpty() || txtCedulaUso.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una máquina e ingrese la cédula del cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            usoMaquinaService.iniciarUso(txtCedulaUso.getText().trim(),
                    Integer.parseInt(txtIdMaquina.getText().trim()));
            JOptionPane.showMessageDialog(this, "Uso iniciado correctamente.");
            txtCedulaUso.setText("");
            cargarTablas();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finalizarUso() {
        if (txtIdUso.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un uso de la tabla para finalizar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int confirmar = JOptionPane.showConfirmDialog(this,
                    "¿Finalizar el uso seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                usoMaquinaService.finalizarUso(Integer.parseInt(txtIdUso.getText().trim()));
                JOptionPane.showMessageDialog(this, "Uso finalizado correctamente.");
                txtIdUso.setText("");
                cargarTablas();
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtIdMaquina.setText("");
        txtNombre.setText("");
        txtTipo.setText("");
        txtUbicacion.setText("");
        txtCedulaUso.setText("");
        txtIdUso.setText("");
    }

    /**
     * Renderer que colorea la celda de estado según su valor.
     * Verde = DISPONIBLE, Naranja = EN_USO, Rojo = MANTENIMIENTO.
     */
    private static class EstadoColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.CENTER);
            if (!isSelected) {
                String estado = value != null ? value.toString() : "";
                switch (estado) {
                    case "DISPONIBLE"   -> setBackground(new Color(198, 239, 206));
                    case "EN_USO"       -> setBackground(new Color(255, 235, 156));
                    case "MANTENIMIENTO"-> setBackground(new Color(255, 199, 206));
                    default             -> setBackground(Color.WHITE);
                }
            }
            return this;
        }
    }
}