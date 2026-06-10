package fitclub.view;

import fitclub.dao.EjercicioDAO;
import fitclub.dao.MaquinaDAO;
import fitclub.dao.RutinaDAO;
import fitclub.model.Ejercicio;
import fitclub.model.Maquina;
import fitclub.model.Rutina;
import fitclub.service.EjercicioService;
import fitclub.service.MaquinaService;
import fitclub.service.RutinaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Panel de gestión de rutinas y ejercicios del gimnasio Fit Club.
 * Al seleccionar una rutina se carga su lista de ejercicios.
 * Los ejercicios pueden asociarse opcionalmente a una máquina disponible.
 *
 * @author Wilberto Ariza Zapata
 */
public class RutinaForm extends JPanel {

    // Campos rutina
    private JTextField txtIdRutina;
    private JTextField txtClienteCedula;
    private JTextField txtEntrenadorCedula;
    private JTextField txtNombre;
    private JTextField txtObjetivo;
    private JTextField txtDescripcion;

    // Campos ejercicio
    private JTextField txtIdEjercicio;
    private JTextField txtNombreEjercicio;
    private JTextField txtSeries;
    private JTextField txtRepeticiones;
    private JTextField txtDescripcionEjercicio;
    private JComboBox<String> cmbMaquinas;

    // Tablas
    private JTable tablaRutinas;
    private JTable tablaEjercicios;
    private DefaultTableModel modeloRutinas;
    private DefaultTableModel modeloEjercicios;

    private final RutinaService rutinaService;
    private final EjercicioService ejercicioService;
    private final MaquinaService maquinaService;

    // Máquinas cargadas para el combo
    private List<Maquina> maquinasDisponibles;

    public RutinaForm() {
        MaquinaDAO maquinaDAO = new MaquinaDAO();
        this.maquinaService   = new MaquinaService(maquinaDAO);
        this.rutinaService    = new RutinaService(new RutinaDAO());
        this.ejercicioService = new EjercicioService(new EjercicioDAO(), maquinaService);
        initComponents();
        cargarComboMaquinas();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(242, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== TÍTULO =====
        JLabel titulo = new JLabel("🏃  Gestión de Rutinas");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(31, 56, 100));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        // ===== PANEL IZQUIERDO =====
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(new Color(242, 245, 250));

        // --- Formulario Rutina ---
        JPanel panelRutina = new JPanel(new GridBagLayout());
        panelRutina.setBackground(Color.WHITE);
        panelRutina.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(214, 228, 247)),
                        "Datos de la Rutina", 0, 0,
                        new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panelRutina.add(new JLabel("ID (buscar/eliminar):"), gbc);
        gbc.gridx = 1; txtIdRutina = new JTextField(15);
        panelRutina.add(txtIdRutina, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelRutina.add(new JLabel("Cédula Cliente:"), gbc);
        gbc.gridx = 1; txtClienteCedula = new JTextField(15);
        panelRutina.add(txtClienteCedula, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelRutina.add(new JLabel("Cédula Entrenador:"), gbc);
        gbc.gridx = 1; txtEntrenadorCedula = new JTextField(15);
        panelRutina.add(txtEntrenadorCedula, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelRutina.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; txtNombre = new JTextField(15);
        panelRutina.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelRutina.add(new JLabel("Objetivo:"), gbc);
        gbc.gridx = 1; txtObjetivo = new JTextField(15);
        panelRutina.add(txtObjetivo, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panelRutina.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; txtDescripcion = new JTextField(15);
        panelRutina.add(txtDescripcion, gbc);

        JPanel botonesRutina = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        botonesRutina.setBackground(Color.WHITE);
        JButton btnGuardar  = crearBoton("Guardar",  new Color(46, 95, 163));
        JButton btnBuscar   = crearBoton("Buscar",   new Color(46, 95, 163));
        JButton btnEliminar = crearBoton("Eliminar", new Color(180, 50, 50));
        JButton btnLimpiar  = crearBoton("Limpiar",  new Color(100, 100, 100));
        botonesRutina.add(btnGuardar);
        botonesRutina.add(btnBuscar);
        botonesRutina.add(btnEliminar);
        botonesRutina.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panelRutina.add(botonesRutina, gbc);

        // --- Formulario Ejercicio ---
        JPanel panelEjercicio = new JPanel(new GridBagLayout());
        panelEjercicio.setBackground(Color.WHITE);
        panelEjercicio.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(214, 228, 247)),
                        "Ejercicios de la Rutina", 0, 0,
                        new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(4, 5, 4, 5);
        gbc2.anchor = GridBagConstraints.WEST;

        gbc2.gridx = 0; gbc2.gridy = 0;
        panelEjercicio.add(new JLabel("ID Ejercicio:"), gbc2);
        gbc2.gridx = 1; txtIdEjercicio = new JTextField(12);
        panelEjercicio.add(txtIdEjercicio, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 1;
        panelEjercicio.add(new JLabel("Nombre:"), gbc2);
        gbc2.gridx = 1; txtNombreEjercicio = new JTextField(12);
        panelEjercicio.add(txtNombreEjercicio, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 2;
        panelEjercicio.add(new JLabel("Series:"), gbc2);
        gbc2.gridx = 1; txtSeries = new JTextField(12);
        panelEjercicio.add(txtSeries, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 3;
        panelEjercicio.add(new JLabel("Repeticiones:"), gbc2);
        gbc2.gridx = 1; txtRepeticiones = new JTextField(12);
        panelEjercicio.add(txtRepeticiones, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 4;
        panelEjercicio.add(new JLabel("Descripción:"), gbc2);
        gbc2.gridx = 1; txtDescripcionEjercicio = new JTextField(12);
        panelEjercicio.add(txtDescripcionEjercicio, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 5;
        panelEjercicio.add(new JLabel("Máquina (opcional):"), gbc2);
        gbc2.gridx = 1; cmbMaquinas = new JComboBox<>();
        panelEjercicio.add(cmbMaquinas, gbc2);

        JPanel botonesEjercicio = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        botonesEjercicio.setBackground(Color.WHITE);
        JButton btnAgregarEjercicio  = crearBoton("Agregar",  new Color(46, 163, 95));
        JButton btnEliminarEjercicio = crearBoton("Eliminar", new Color(180, 50, 50));
        botonesEjercicio.add(btnAgregarEjercicio);
        botonesEjercicio.add(btnEliminarEjercicio);

        gbc2.gridx = 0; gbc2.gridy = 6; gbc2.gridwidth = 2;
        panelEjercicio.add(botonesEjercicio, gbc2);

        panelIzquierdo.add(panelRutina);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(panelEjercicio);
        add(panelIzquierdo, BorderLayout.WEST);

        // ===== PANEL DERECHO (tablas) =====
        JPanel panelTablas = new JPanel(new GridLayout(2, 1, 0, 10));
        panelTablas.setBackground(new Color(242, 245, 250));

        modeloRutinas = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Objetivo", "Fecha"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaRutinas = new JTable(modeloRutinas);
        tablaRutinas.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaRutinas.setRowHeight(28);
        tablaRutinas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaRutinas.getTableHeader().setBackground(new Color(46, 95, 163));
        tablaRutinas.getTableHeader().setForeground(Color.WHITE);

        JPanel panelTablaRutinas = new JPanel(new BorderLayout());
        panelTablaRutinas.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247)),
                "Rutinas del cliente", 0, 0,
                new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)));
        panelTablaRutinas.add(new JScrollPane(tablaRutinas), BorderLayout.CENTER);

        modeloEjercicios = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Series", "Reps", "Descripción", "Máquina"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaEjercicios = new JTable(modeloEjercicios);
        tablaEjercicios.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaEjercicios.setRowHeight(28);
        tablaEjercicios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaEjercicios.getTableHeader().setBackground(new Color(46, 95, 163));
        tablaEjercicios.getTableHeader().setForeground(Color.WHITE);

        JPanel panelTablaEjercicios = new JPanel(new BorderLayout());
        panelTablaEjercicios.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247)),
                "Ejercicios de la rutina seleccionada", 0, 0,
                new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)));
        panelTablaEjercicios.add(new JScrollPane(tablaEjercicios), BorderLayout.CENTER);

        panelTablas.add(panelTablaRutinas);
        panelTablas.add(panelTablaEjercicios);
        add(panelTablas, BorderLayout.CENTER);

        // ===== LISTENERS =====
        btnGuardar.addActionListener(e -> guardarRutina());
        btnBuscar.addActionListener(e -> buscarRutinas());
        btnEliminar.addActionListener(e -> eliminarRutina());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnAgregarEjercicio.addActionListener(e -> agregarEjercicio());
        btnEliminarEjercicio.addActionListener(e -> eliminarEjercicio());

        tablaRutinas.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaRutinas.getSelectedRow();
            if (fila >= 0) {
                txtIdRutina.setText(String.valueOf(modeloRutinas.getValueAt(fila, 0)));
                txtNombre.setText((String) modeloRutinas.getValueAt(fila, 1));
                txtObjetivo.setText((String) modeloRutinas.getValueAt(fila, 2));
                cargarEjercicios(Integer.parseInt(txtIdRutina.getText()));
            }
        });

        tablaEjercicios.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaEjercicios.getSelectedRow();
            if (fila >= 0) {
                txtIdEjercicio.setText(String.valueOf(modeloEjercicios.getValueAt(fila, 0)));
                txtNombreEjercicio.setText((String) modeloEjercicios.getValueAt(fila, 1));
                txtSeries.setText(String.valueOf(modeloEjercicios.getValueAt(fila, 2)));
                txtRepeticiones.setText(String.valueOf(modeloEjercicios.getValueAt(fila, 3)));
                txtDescripcionEjercicio.setText((String) modeloEjercicios.getValueAt(fila, 4));
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

    private void cargarComboMaquinas() {
        cmbMaquinas.removeAllItems();
        cmbMaquinas.addItem("— Sin máquina —");
        maquinasDisponibles = maquinaService.listarActivas();
        maquinasDisponibles.forEach(m -> cmbMaquinas.addItem(m.getNombre()));
    }

    private void buscarRutinas() {
        if (txtClienteCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la cédula del cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        modeloRutinas.setRowCount(0);
        modeloEjercicios.setRowCount(0);
        rutinaService.consultarRutinasCliente(txtClienteCedula.getText().trim()).forEach(r ->
                modeloRutinas.addRow(new Object[]{
                        r.getIdRutina(),
                        r.getNombre(),
                        r.getObjetivo(),
                        r.getFechaAsignacion().toString()
                }));
    }

    private void cargarEjercicios(int idRutina) {
        modeloEjercicios.setRowCount(0);
        ejercicioService.listarPorRutina(idRutina).forEach(ej -> {
            String maquinaNombre = "—";
            if (ej.tieneMaquina()) {
                maquinaNombre = maquinasDisponibles.stream()
                        .filter(m -> m.getIdMaquina() == ej.getIdMaquina())
                        .map(Maquina::getNombre)
                        .findFirst()
                        .orElse("ID " + ej.getIdMaquina());
            }
            modeloEjercicios.addRow(new Object[]{
                    ej.getIdEjercicio(),
                    ej.getNombre(),
                    ej.getSeries(),
                    ej.getRepeticiones(),
                    ej.getDescripcion(),
                    maquinaNombre
            });
        });
    }

    private void guardarRutina() {
        if (txtClienteCedula.getText().trim().isEmpty() || txtEntrenadorCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cédula de cliente y entrenador son obligatorias.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre de la rutina es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Rutina rutina = new Rutina(0,
                    txtNombre.getText().trim(),
                    txtObjetivo.getText().trim(),
                    txtDescripcion.getText().trim(),
                    LocalDate.now());
            rutinaService.asignarRutina(rutina,
                    txtClienteCedula.getText().trim(),
                    txtEntrenadorCedula.getText().trim());
            JOptionPane.showMessageDialog(this, "Rutina asignada correctamente.");
            buscarRutinas();
            limpiarCamposRutina();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarEjercicio() {
        if (txtIdRutina.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una rutina de la tabla primero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtNombreEjercicio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del ejercicio es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int series = Integer.parseInt(txtSeries.getText().trim());
            int reps   = Integer.parseInt(txtRepeticiones.getText().trim());
            int idRutina = Integer.parseInt(txtIdRutina.getText().trim());

            Integer idMaquina = null;
            int idxMaquina = cmbMaquinas.getSelectedIndex();
            if (idxMaquina > 0 && maquinasDisponibles != null && idxMaquina - 1 < maquinasDisponibles.size()) {
                idMaquina = maquinasDisponibles.get(idxMaquina - 1).getIdMaquina();
            }

            Ejercicio ejercicio = new Ejercicio(idRutina,
                    txtNombreEjercicio.getText().trim(),
                    series, reps,
                    txtDescripcionEjercicio.getText().trim(),
                    idMaquina);
            ejercicioService.agregarEjercicio(ejercicio);
            JOptionPane.showMessageDialog(this, "Ejercicio agregado correctamente.");
            cargarEjercicios(idRutina);
            limpiarCamposEjercicio();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Series y repeticiones deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRutina() {
        if (txtIdRutina.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una rutina de la tabla para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int confirmar = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar esta rutina? También se eliminarán todos sus ejercicios.",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                rutinaService.eliminarRutina(Integer.parseInt(txtIdRutina.getText().trim()));
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

    private void eliminarEjercicio() {
        if (txtIdEjercicio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un ejercicio de la tabla para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int confirmar = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar este ejercicio?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                int idRutina = Integer.parseInt(txtIdRutina.getText().trim());
                ejercicioService.eliminarEjercicio(Integer.parseInt(txtIdEjercicio.getText().trim()));
                JOptionPane.showMessageDialog(this, "Ejercicio eliminado correctamente.");
                cargarEjercicios(idRutina);
                limpiarCamposEjercicio();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        limpiarCamposRutina();
        limpiarCamposEjercicio();
        modeloRutinas.setRowCount(0);
        modeloEjercicios.setRowCount(0);
    }

    private void limpiarCamposRutina() {
        txtIdRutina.setText("");
        txtClienteCedula.setText("");
        txtEntrenadorCedula.setText("");
        txtNombre.setText("");
        txtObjetivo.setText("");
        txtDescripcion.setText("");
    }

    private void limpiarCamposEjercicio() {
        txtIdEjercicio.setText("");
        txtNombreEjercicio.setText("");
        txtSeries.setText("");
        txtRepeticiones.setText("");
        txtDescripcionEjercicio.setText("");
        cmbMaquinas.setSelectedIndex(0);
    }
}