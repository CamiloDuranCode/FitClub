package fitclub.view;

import fitclub.dao.MembresiaDAO;
import fitclub.model.Membresia;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import fitclub.dao.PagoDAO;
import fitclub.model.Pago;
import java.util.List;

/**
 * Panel de gestión de membresías del gimnasio Fit Club.
 *
 * @author Wilberto Ariza Zapata
 */
public class MembresiaForm extends JPanel {

    private JTextField txtIdMembresia;
    private JTextField txtClienteCedula;
    private JComboBox<String> cmbTipo;
    private JTable tablaMembresias;
    private DefaultTableModel modeloTabla;

    private MembresiaDAO membresiaDAO;
    private PagoDAO pagoDAO;

    public MembresiaForm() {
        membresiaDAO = new MembresiaDAO();
        pagoDAO = new PagoDAO();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(242, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== TÍTULO =====
        JLabel titulo = new JLabel("💳  Gestión de Membresías");
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
        panelCampos.add(new JLabel("ID Membresía (solo para buscar/eliminar):"), gbc);
        gbc.gridx = 1; txtIdMembresia = new JTextField(15);
        panelCampos.add(txtIdMembresia, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelCampos.add(new JLabel("Cédula Cliente:"), gbc);
        gbc.gridx = 1; txtClienteCedula = new JTextField(15);
        panelCampos.add(txtClienteCedula, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelCampos.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; cmbTipo = new JComboBox<>(new String[]{"mensual", "trimestral", "anual"});
        panelCampos.add(cmbTipo, gbc);

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
                new String[]{"ID", "Tipo", "Fecha Inicio", "Fecha Vencimiento", "Vigente"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaMembresias = new JTable(modeloTabla);
        tablaMembresias.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaMembresias.setRowHeight(28);
        tablaMembresias.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaMembresias.getTableHeader().setBackground(new Color(46, 95, 163));
        tablaMembresias.getTableHeader().setForeground(Color.WHITE);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247)),
                "Membresías registradas", 0, 0,
                new Font("Arial", Font.BOLD, 13),
                new Color(31, 56, 100)
        ));
        panelTabla.add(new JScrollPane(tablaMembresias), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        // ===== ACCIONES =====
        btnGuardar.addActionListener(e -> guardarMembresia());
        btnBuscar.addActionListener(e -> buscarMembresia());
        btnEliminar.addActionListener(e -> eliminarMembresia());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaMembresias.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaMembresias.getSelectedRow();
            if (fila >= 0) {
                txtIdMembresia.setText(String.valueOf(modeloTabla.getValueAt(fila, 0)));
                cmbTipo.setSelectedItem(modeloTabla.getValueAt(fila, 1));
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

    private LocalDate calcularFechaVencimiento(String tipo, LocalDate inicio) {
        return switch (tipo) {
            case "mensual"     -> inicio.plusMonths(1);
            case "trimestral"  -> inicio.plusMonths(3);
            case "anual"       -> inicio.plusYears(1);
            default            -> inicio.plusMonths(1);
        };
    }

    private void guardarMembresia() {
        if (txtClienteCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La cédula del cliente es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String tipo = (String) cmbTipo.getSelectedItem();
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaVencimiento = calcularFechaVencimiento(tipo, fechaInicio);

        Membresia membresia = new Membresia(tipo, 0, fechaInicio, fechaVencimiento);
        membresiaDAO.insertar(membresia, txtClienteCedula.getText());

        // Calcular monto según tipo
        double monto = switch (tipo) {
            case "mensual"    -> 50000;
            case "trimestral" -> 130000;
            case "anual"      -> 480000;
            default           -> 50000;
        };

        // Registrar pago automáticamente
        List<Membresia> membresias = membresiaDAO.listarPorCliente(txtClienteCedula.getText());
        if (!membresias.isEmpty()) {
            Membresia ultima = membresias.get(membresias.size() - 1);
            Pago pago = new Pago(0, monto, LocalDate.now(), "efectivo");
            pagoDAO.insertar(pago, ultima.getIdMembresia());
        }

        JOptionPane.showMessageDialog(this,
                "Membresía guardada.\nInicio: " + fechaInicio + "\nVencimiento: " + fechaVencimiento);
        cargarTabla();
        limpiarCampos();
    }

    private void buscarMembresia() {
        if (txtClienteCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la cédula del cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        cargarTabla();
    }

    private void eliminarMembresia() {
        if (txtIdMembresia.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int confirmar = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar esta membresía?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                membresiaDAO.eliminar(Integer.parseInt(txtIdMembresia.getText()));
                JOptionPane.showMessageDialog(this, "Membresía eliminada correctamente.");
                cargarTabla();
                limpiarCampos();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        membresiaDAO.listarPorCliente(txtClienteCedula.getText()).forEach(m ->
                modeloTabla.addRow(new Object[]{
                        m.getIdMembresia(),
                        m.getTipo(),
                        m.getFechaInicio().toString(),
                        m.getFechaVencimiento().toString(),
                        m.estaVigente() ? "✅ Sí" : "❌ No"
                })
        );
    }

    private void limpiarCampos() {
        txtIdMembresia.setText("");
        txtClienteCedula.setText("");
        cmbTipo.setSelectedIndex(0);
    }
}