package fitclub.view;

import fitclub.dao.TurnoDAO;
import fitclub.model.Turno;
import fitclub.model.enums.DiaSemana;
import fitclub.service.TurnoService;
import fitclub.service.SeccionActual;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Panel de gestión de turnos de entrenadores del gimnasio Fit Club.
 *
 * @author Wilberto Ariza Zapata
 */
public class TurnoForm extends JPanel {

    private JTextField txtCedulaEntrenador;
    private JComboBox<DiaSemana> cmbDia;
    private JTextField txtHoraInicio;
    private JTextField txtHoraFin;
    private JTable tablaTurnos;
    private DefaultTableModel modeloTabla;
    private int idSeleccionado = -1;

    private final TurnoService turnoService;

    public TurnoForm() {
        this.turnoService = new TurnoService(new TurnoDAO());
        initComponents();
        cargarTablaInicial();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(242, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("🕐  Gestión de Turnos");
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
        panelCampos.add(new JLabel("Cédula Entrenador:"), gbc);
        gbc.gridx = 1; txtCedulaEntrenador = new JTextField(15);
        panelCampos.add(txtCedulaEntrenador, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelCampos.add(new JLabel("Día:"), gbc);
        gbc.gridx = 1; cmbDia = new JComboBox<>(DiaSemana.values());
        panelCampos.add(cmbDia, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelCampos.add(new JLabel("Hora Inicio (HH:mm):"), gbc);
        gbc.gridx = 1; txtHoraInicio = new JTextField(10);
        panelCampos.add(txtHoraInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelCampos.add(new JLabel("Hora Fin (HH:mm):"), gbc);
        gbc.gridx = 1; txtHoraFin = new JTextField(10);
        panelCampos.add(txtHoraFin, gbc);

        // -----------------------------------------------------------------
        // Botones
        // -----------------------------------------------------------------
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        JButton btnGuardar    = crearBoton("Guardar",    new Color(46, 95, 163));
        JButton btnActualizar = crearBoton("Actualizar", new Color(46, 95, 163));
        JButton btnEliminar   = crearBoton("Eliminar",   new Color(180, 50, 50));
        JButton btnBuscar     = crearBoton("Buscar",     new Color(46, 95, 163));
        JButton btnLimpiar    = crearBoton("Limpiar",    new Color(100, 100, 100));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);
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
                new String[]{"ID", "Cédula Entrenador", "Día", "Hora Inicio", "Hora Fin"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaTurnos = new JTable(modeloTabla);
        tablaTurnos.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaTurnos.setRowHeight(28);
        tablaTurnos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaTurnos.getTableHeader().setBackground(new Color(46, 95, 163));
        tablaTurnos.getTableHeader().setForeground(Color.WHITE);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247)),
                "Turnos registrados", 0, 0,
                new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)));
        panelTabla.add(new JScrollPane(tablaTurnos), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        // -----------------------------------------------------------------
        // Listeners
        // -----------------------------------------------------------------
        btnGuardar.addActionListener(e -> guardarTurno());
        btnActualizar.addActionListener(e -> actualizarTurno());
        btnEliminar.addActionListener(e -> eliminarTurno());
        btnBuscar.addActionListener(e -> buscarTurnos());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaTurnos.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaTurnos.getSelectedRow();
            if (fila >= 0) {
                idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                txtCedulaEntrenador.setText((String) modeloTabla.getValueAt(fila, 1));
                cmbDia.setSelectedItem(DiaSemana.valueOf(modeloTabla.getValueAt(fila, 2).toString().toUpperCase()));
                txtHoraInicio.setText((String) modeloTabla.getValueAt(fila, 3));
                txtHoraFin.setText((String) modeloTabla.getValueAt(fila, 4));
            }
        });
    }

    // -------------------------------------------------------------------------
    // Acciones
    // -------------------------------------------------------------------------

    private void guardarTurno() {
        try {
            Turno turno = construirTurno(0);
            turnoService.registrarTurno(turno);
            JOptionPane.showMessageDialog(this, "Turno registrado correctamente.");
            cargarTablaInicial();
            limpiarCampos();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTurno() {
        if (idSeleccionado < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un turno de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Turno turno = construirTurno(idSeleccionado);
            turnoService.actualizarTurno(turno);
            JOptionPane.showMessageDialog(this, "Turno actualizado correctamente.");
            cargarTablaInicial();
            limpiarCampos();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarTurno() {
        if (idSeleccionado < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un turno de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este turno?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                turnoService.eliminarTurno(idSeleccionado);
                JOptionPane.showMessageDialog(this, "Turno eliminado correctamente.");
                cargarTablaInicial();
                limpiarCampos();
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarTurnos() {
        String cedula = txtCedulaEntrenador.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la cédula del entrenador.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        modeloTabla.setRowCount(0);
        try {
            turnoService.listarPorEntrenador(cedula).forEach(t ->
                    modeloTabla.addRow(new Object[]{
                            t.getIdTurno(),
                            t.getCedulaEntrenador(),
                            t.getDia().name(),
                            t.getHoraInicio().toString(),
                            t.getHoraFin().toString()
                    }));
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTablaInicial() {
        modeloTabla.setRowCount(0);
        try {
            turnoService.listarTodos().forEach(t ->
                    modeloTabla.addRow(new Object[]{
                            t.getIdTurno(),
                            t.getCedulaEntrenador(),
                            t.getDia().name(),
                            t.getHoraInicio().toString(),
                            t.getHoraFin().toString()
                    }));
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Turno construirTurno(int id) {
        String cedula = txtCedulaEntrenador.getText().trim();
        if (cedula.isEmpty()) throw new IllegalArgumentException("La cédula del entrenador es obligatoria.");
        try {
            LocalTime inicio = LocalTime.parse(txtHoraInicio.getText().trim());
            LocalTime fin    = LocalTime.parse(txtHoraFin.getText().trim());
            return new Turno(id, cedula, (DiaSemana) cmbDia.getSelectedItem(), inicio, fin);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de hora inválido. Use HH:mm");
        }
    }

    private void limpiarCampos() {
        txtCedulaEntrenador.setText("");
        cmbDia.setSelectedIndex(0);
        txtHoraInicio.setText("");
        txtHoraFin.setText("");
        idSeleccionado = -1;
        tablaTurnos.clearSelection();
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