package fitclub.view;

import fitclub.dao.RutinaDAO;
import fitclub.model.Rutina;
import fitclub.service.RutinaService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

/**
 * Panel de gestión de rutinas del gimnasio Fit Club.
 *
 * @author Wilberto Ariza Zapata
 */
public class RutinaForm extends JPanel {

    private JTextField txtIdRutina;
    private JTextField txtClienteCedula;
    private JTextField txtEntrenadorCedula;
    private JTextField txtDescripcion;
    private JTable tablaRutinas;
    private DefaultTableModel modeloTabla;

    private final RutinaService rutinaService;

    public RutinaForm() {
        this.rutinaService = new RutinaService(new RutinaDAO());
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(242, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("🏃  Gestión de Rutinas");
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
        panelCampos.add(new JLabel("ID Rutina (solo para buscar/eliminar):"), gbc);
        gbc.gridx = 1; txtIdRutina = new JTextField(15);
        panelCampos.add(txtIdRutina, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelCampos.add(new JLabel("Cédula Cliente:"), gbc);
        gbc.gridx = 1; txtClienteCedula = new JTextField(15);
        panelCampos.add(txtClienteCedula, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelCampos.add(new JLabel("Cédula Entrenador:"), gbc);
        gbc.gridx = 1; txtEntrenadorCedula = new JTextField(15);
        panelCampos.add(txtEntrenadorCedula, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelCampos.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; txtDescripcion = new JTextField(15);
        panelCampos.add(txtDescripcion, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        JButton btnGuardar  = crearBoton("Guardar",  new Color(46, 95, 163));
        JButton btnBuscar   = crearBoton("Buscar",   new Color(46, 95, 163));
        JButton btnEliminar = crearBoton("Eliminar", new Color(180, 50, 50));
        JButton btnLimpiar  = crearBoton("Limpiar",  new Color(100, 100, 100));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(242, 245, 250));
        panelSuperior.add(panelCampos, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.WEST);

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Descripción", "Fecha Asignación"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaRutinas = new JTable(modeloTabla);
        tablaRutinas.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaRutinas.setRowHeight(28);
        tablaRutinas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaRutinas.getTableHeader().setBackground(new Color(46, 95, 163));
        tablaRutinas.getTableHeader().setForeground(Color.WHITE);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247)),
                "Rutinas del cliente", 0, 0,
                new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)));
        panelTabla.add(new JScrollPane(tablaRutinas), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarRutina());
        btnBuscar.addActionListener(e -> buscarRutinas());
        btnEliminar.addActionListener(e -> eliminarRutina());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaRutinas.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaRutinas.getSelectedRow();
            if (fila >= 0) {
                txtIdRutina.setText(String.valueOf(modeloTabla.getValueAt(fila, 0)));
                txtDescripcion.setText((String) modeloTabla.getValueAt(fila, 1));
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

    private void guardarRutina() {
        if (txtClienteCedula.getText().isEmpty() || txtEntrenadorCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cédula de cliente y entrenador son obligatorias.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtDescripcion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Rutina rutina = new Rutina(0, txtDescripcion.getText(), LocalDate.now());
            rutinaService.asignarRutina(rutina, txtClienteCedula.getText(), txtEntrenadorCedula.getText());
            JOptionPane.showMessageDialog(this, "Rutina asignada correctamente.");
            buscarRutinas();
            limpiarCampos();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarRutinas() {
        if (txtClienteCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la cédula del cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        modeloTabla.setRowCount(0);
        rutinaService.consultarRutinasCliente(txtClienteCedula.getText()).forEach(r ->
                modeloTabla.addRow(new Object[]{
                        r.getIdRutina(), r.getDescripcion(),
                        r.getFechaAsignacion().toString()
                }));
    }

    private void eliminarRutina() {
        if (txtIdRutina.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una rutina de la tabla para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int confirmar = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar esta rutina?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                rutinaService.eliminarRutina(Integer.parseInt(txtIdRutina.getText()));
                JOptionPane.showMessageDialog(this, "Rutina eliminada correctamente.");
                buscarRutinas();
                limpiarCampos();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtIdRutina.setText("");
        txtClienteCedula.setText("");
        txtEntrenadorCedula.setText("");
        txtDescripcion.setText("");
        modeloTabla.setRowCount(0);
    }
}