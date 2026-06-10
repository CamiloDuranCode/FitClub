package fitclub.view;

import fitclub.dao.AsistenciaDAO;
import fitclub.model.Asistencia;
import fitclub.model.enums.TipoAsistencia;
import fitclub.service.AsistenciaService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Panel de registro de asistencia del gimnasio Fit Club.
 *
 * @author Wilberto Ariza Zapata
 */
public class AsistenciaForm extends JPanel {

    private JTextField txtIdEliminar;
    private JTextField txtClienteCedula;
    private JTextField txtObservacion;
    private JTable tablaAsistencia;
    private DefaultTableModel modeloTabla;

    private final AsistenciaService asistenciaService;

    public AsistenciaForm() {
        this.asistenciaService = new AsistenciaService(new AsistenciaDAO());
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(242, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("📋  Registro de Asistencia");
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
        panelCampos.add(new JLabel("Cédula Cliente:"), gbc);
        gbc.gridx = 1; txtClienteCedula = new JTextField(15);
        panelCampos.add(txtClienteCedula, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelCampos.add(new JLabel("Observación:"), gbc);
        gbc.gridx = 1; txtObservacion = new JTextField(15);
        panelCampos.add(txtObservacion, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelCampos.add(new JLabel("ID (solo para eliminar):"), gbc);
        gbc.gridx = 1; txtIdEliminar = new JTextField(15);
        panelCampos.add(txtIdEliminar, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        JButton btnRegistrar = crearBoton("Registrar", new Color(46, 95, 163));
        JButton btnBuscar    = crearBoton("Buscar",    new Color(46, 95, 163));
        JButton btnEliminar  = crearBoton("Eliminar",  new Color(180, 50, 50));
        JButton btnLimpiar   = crearBoton("Limpiar",   new Color(100, 100, 100));
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(242, 245, 250));
        panelSuperior.add(panelCampos, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.WEST);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Fecha y Hora", "Observación"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaAsistencia = new JTable(modeloTabla);
        tablaAsistencia.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaAsistencia.setRowHeight(28);
        tablaAsistencia.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaAsistencia.getTableHeader().setBackground(new Color(46, 95, 163));
        tablaAsistencia.getTableHeader().setForeground(Color.WHITE);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247)),
                "Historial de asistencia", 0, 0,
                new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)));
        panelTabla.add(new JScrollPane(tablaAsistencia), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrarAsistencia());
        btnBuscar.addActionListener(e -> buscarPorCliente());
        btnEliminar.addActionListener(e -> eliminarAsistencia());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaAsistencia.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaAsistencia.getSelectedRow();
            if (fila >= 0) txtIdEliminar.setText(String.valueOf(modeloTabla.getValueAt(fila, 0)));
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

    // REEMPLAZA el método registrarAsistencia() por este:
    private void registrarAsistencia() {
        if (txtClienteCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La cédula del cliente es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Asistencia asistencia = new Asistencia(
                    0,
                    LocalDateTime.now(),
                    fitclub.model.enums.TipoAsistencia.ENTRADA,   // ← tipo añadido
                    txtObservacion.getText()
            );
            asistenciaService.registrarAsistencia(asistencia, txtClienteCedula.getText());
            JOptionPane.showMessageDialog(this, "Asistencia registrada correctamente.");
            buscarPorCliente();
            txtObservacion.setText("");
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPorCliente() {
        if (txtClienteCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cédula para buscar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        modeloTabla.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        asistenciaService.consultarHistorial(txtClienteCedula.getText()).forEach(a ->
                modeloTabla.addRow(new Object[]{
                        a.getIdAsistencia(),
                        a.getFechaHora().format(fmt),
                        a.getObservacion()
                }));
    }

    private void eliminarAsistencia() {
        if (txtIdEliminar.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int confirmar = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                asistenciaService.eliminarAsistencia(Integer.parseInt(txtIdEliminar.getText()));
                JOptionPane.showMessageDialog(this, "Asistencia eliminada correctamente.");
                buscarPorCliente();
                txtIdEliminar.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtIdEliminar.setText("");
        txtClienteCedula.setText("");
        txtObservacion.setText("");
        modeloTabla.setRowCount(0);
    }
}