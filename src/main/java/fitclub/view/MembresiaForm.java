package fitclub.view;

import fitclub.dao.Conexion;
import fitclub.dao.MembresiaDAO;
import fitclub.dao.PagoDAO;
import fitclub.model.Membresia;
import fitclub.model.Pago;
import fitclub.service.MembresiaService;
import fitclub.service.PagoService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel de gestión de membresías del gimnasio Fit Club.
 *
 * CORRECCIONES APLICADAS:
 *  - guardarMembresia() ya NO crea Membresia(0, ...).
 *    Ahora consulta la tabla «membresia» y obtiene el id_membresia real
 *    antes de hacer el INSERT en cliente_membresia.
 *  - Se agrega obtenerIdMembresiaCatalogo() para la consulta al catálogo.
 *  - El combo cmbTipo sigue mostrando strings pero el ID se resuelve internamente.
 *
 * @author Wilberto Ariza Zapata
 */
public class MembresiaForm extends JPanel {

    private JTextField txtIdMembresia;
    private JTextField txtClienteCedula;
    private JComboBox<String> cmbTipo;
    private JTable tablaMembresias;
    private DefaultTableModel modeloTabla;

    private final MembresiaService membresiaService;
    private final PagoService pagoService;

    public MembresiaForm() {
        this.membresiaService = new MembresiaService(new MembresiaDAO());
        this.pagoService      = new PagoService(new PagoDAO());
        initComponents();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CORRECCIÓN PRINCIPAL
    // Consulta la tabla «membresia» y devuelve el id_membresia que corresponde
    // al tipo seleccionado. Lanza RuntimeException si no existe en el catálogo.
    // ─────────────────────────────────────────────────────────────────────────
    private int obtenerIdMembresiaCatalogo(String tipo) {
        String sql = "SELECT id_membresia FROM membresia WHERE tipo = ?::tipo_membresia AND activa = TRUE LIMIT 1";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, tipo.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_membresia");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar catálogo de membresías: " + e.getMessage(), e);
        }
        throw new RuntimeException(
                "No existe una membresía activa de tipo «" + tipo + "» en el catálogo.\n" +
                        "Verifique que la tabla «membresia» tenga registros activos.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UI
    // ─────────────────────────────────────────────────────────────────────────
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(242, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("💳  Gestión de Membresías");
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
        panelCampos.add(new JLabel("ID Membresía (solo para buscar/cancelar):"), gbc);
        gbc.gridx = 1; txtIdMembresia = new JTextField(15);
        panelCampos.add(txtIdMembresia, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelCampos.add(new JLabel("Cédula Cliente:"), gbc);
        gbc.gridx = 1; txtClienteCedula = new JTextField(15);
        panelCampos.add(txtClienteCedula, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelCampos.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        cmbTipo = new JComboBox<>(new String[]{"mensual", "trimestral", "semestral", "anual"});
        panelCampos.add(cmbTipo, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        JButton btnGuardar  = crearBoton("Guardar",  new Color(46, 95, 163));
        JButton btnBuscar   = crearBoton("Buscar",   new Color(46, 95, 163));
        JButton btnCancelar = crearBoton("Cancelar", new Color(180, 50, 50));
        JButton btnLimpiar  = crearBoton("Limpiar",  new Color(100, 100, 100));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnLimpiar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(242, 245, 250));
        panelSuperior.add(panelCampos,  BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.WEST);

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Tipo", "Fecha Inicio", "Fecha Vencimiento", "Estado"}, 0) {
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
                new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)));
        panelTabla.add(new JScrollPane(tablaMembresias), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        btnGuardar.addActionListener(e  -> guardarMembresia());
        btnBuscar.addActionListener(e   -> buscarMembresia());
        btnCancelar.addActionListener(e -> cancelarMembresia());
        btnLimpiar.addActionListener(e  -> limpiarCampos());

        tablaMembresias.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaMembresias.getSelectedRow();
            if (fila >= 0) {
                txtIdMembresia.setText(String.valueOf(modeloTabla.getValueAt(fila, 0)));
                cmbTipo.setSelectedItem(modeloTabla.getValueAt(fila, 1).toString().toLowerCase());
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
            case "trimestral" -> inicio.plusMonths(3);
            case "semestral"  -> inicio.plusMonths(6);
            case "anual"      -> inicio.plusYears(1);
            default           -> inicio.plusMonths(1);
        };
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GUARDAR — BUG CORREGIDO
    // ─────────────────────────────────────────────────────────────────────────
    private void guardarMembresia() {
        if (txtClienteCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La cédula del cliente es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String tipoStr = (String) cmbTipo.getSelectedItem();
            fitclub.model.enums.TipoMembresia tipo =
                    fitclub.model.enums.TipoMembresia.valueOf(tipoStr.toUpperCase());

            // ✅ CORRECCIÓN: obtener el id_membresia real del catálogo
            int idMembresiaCatalogo = obtenerIdMembresiaCatalogo(tipoStr);

            LocalDate fechaInicio      = LocalDate.now();
            LocalDate fechaVencimiento = calcularFechaVencimiento(tipoStr, fechaInicio);

            // ✅ Antes era new Membresia(0, ...) → ahora usa el ID real
            Membresia membresia = new Membresia(idMembresiaCatalogo, tipo, fechaInicio, fechaVencimiento);

            String cedula = txtClienteCedula.getText().trim();
            membresiaService.registrarMembresia(membresia, cedula);

            // Registrar pago automático
            List<Membresia> membresias = membresiaService.listarMembresiasCliente(cedula);
            if (!membresias.isEmpty()) {
                Membresia ultima = membresias.get(membresias.size() - 1);
                Pago pago = new Pago(
                        0,
                        cedula,
                        ultima.calcularTotal(),
                        LocalDate.now(),
                        fitclub.model.enums.MetodoPago.EFECTIVO,
                        "Membresía " + tipoStr
                );
                pagoService.registrarPago(pago, ultima.getIdMembresia());
            }

            JOptionPane.showMessageDialog(this,
                    "Membresía guardada.\nInicio: " + fechaInicio + "\nVencimiento: " + fechaVencimiento);
            cargarTabla();
            limpiarCampos();

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarMembresia() {
        if (txtClienteCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese la cédula del cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        cargarTabla();
    }

    private void cancelarMembresia() {
        if (txtIdMembresia.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un ID para cancelar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int confirmar = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de cancelar esta membresía?\nEl historial de pagos se conservará.",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                membresiaService.cancelarMembresia(Integer.parseInt(txtIdMembresia.getText().trim()));
                JOptionPane.showMessageDialog(this, "Membresía cancelada correctamente.");
                cargarTabla();
                limpiarCampos();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        String cedula = txtClienteCedula.getText().trim();
        if (cedula.isEmpty()) return;

        // Consulta el estado real (activa/cancelada/vencida) directamente de la BD
        Map<Integer, String> estados = obtenerEstadosPorCliente(cedula);

        membresiaService.listarMembresiasCliente(cedula).forEach(m -> {
            String estadoBD    = estados.getOrDefault(m.getIdMembresia(), "activa");
            String estadoTexto = switch (estadoBD) {
                case "cancelada" -> "\uD83D\uDEAB Cancelada";
                case "vencida"   -> "❌ Vencida";
                default          -> m.estaVigente() ? "✅ Vigente" : "❌ Vencida";
            };
            modeloTabla.addRow(new Object[]{
                    m.getIdMembresia(),
                    m.getTipo(),
                    m.getFechaInicio().toString(),
                    m.getFechaVencimiento().toString(),
                    estadoTexto
            });
        });
    }

    /** Devuelve un mapa cm.id → estado para todos los registros del cliente. */
    private Map<Integer, String> obtenerEstadosPorCliente(String cedula) {
        Map<Integer, String> map = new LinkedHashMap<>();
        String sql = "SELECT id, estado FROM cliente_membresia WHERE cedula = ?";
        try (java.sql.PreparedStatement ps = fitclub.dao.Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) map.put(rs.getInt("id"), rs.getString("estado"));
            }
        } catch (java.sql.SQLException e) {
            System.err.println("[MembresiaForm] No se pudo obtener estados: " + e.getMessage());
        }
        return map;
    }

    private void limpiarCampos() {
        txtIdMembresia.setText("");
        txtClienteCedula.setText("");
        cmbTipo.setSelectedIndex(0);
    }
}