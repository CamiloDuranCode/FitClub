package fitclub.view;

import fitclub.dao.MembresiaDAO;
import fitclub.dao.PagoDAO;
import fitclub.service.ReporteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Panel de reportes de ingresos del gimnasio Fit Club.
 * Permite filtrar por período y visualizar ingresos totales,
 * por mes y por tipo de membresía.
 *
 * @author Camilo Andrés Durán Baquero
 */
public class ReportePanel extends JPanel {

    private final ReporteService reporteService;

    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JLabel lblTotal;
    private JTable tablaPagos;
    private JTable tablaAgrupado;
    private DefaultTableModel modeloPagos;
    private DefaultTableModel modeloAgrupado;

    public ReportePanel() {
        this.reporteService = new ReporteService(new PagoDAO(), new MembresiaDAO());
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(242, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== TÍTULO =====
        JLabel titulo = new JLabel("📊  Reporte de Ingresos");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(31, 56, 100));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        // ===== PANEL DE FILTROS =====
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelFiltros.setBackground(Color.WHITE);
        panelFiltros.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        panelFiltros.add(new JLabel("Fecha inicio (yyyy-MM-dd):"));
        txtFechaInicio = new JTextField(12);
        txtFechaInicio.setText(LocalDate.now().withDayOfMonth(1).toString());
        panelFiltros.add(txtFechaInicio);

        panelFiltros.add(new JLabel("Fecha fin (yyyy-MM-dd):"));
        txtFechaFin = new JTextField(12);
        txtFechaFin.setText(LocalDate.now().toString());
        panelFiltros.add(txtFechaFin);

        JButton btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setBackground(new Color(46, 95, 163));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setFont(new Font("Arial", Font.BOLD, 13));
        btnGenerar.setFocusPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.addActionListener(e -> generarReporte());
        panelFiltros.add(btnGenerar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBackground(new Color(242, 245, 250));
        btnLimpiar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(e -> limpiar());
        panelFiltros.add(btnLimpiar);

        // ===== LABEL TOTAL =====
        lblTotal = new JLabel("Total de ingresos: —");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 15));
        lblTotal.setForeground(new Color(55, 86, 35));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        panelFiltros.add(lblTotal);

        add(panelFiltros, BorderLayout.NORTH);

        // ===== TABLAS =====
        JPanel panelTablas = new JPanel(new GridLayout(1, 2, 15, 0));
        panelTablas.setBackground(new Color(242, 245, 250));

        // Tabla de pagos del período
        modeloPagos = new DefaultTableModel(
                new String[]{"ID Pago", "Fecha", "Monto ($)", "Método"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaPagos = new JTable(modeloPagos);
        tablaPagos.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaPagos.setRowHeight(28);
        tablaPagos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaPagos.getTableHeader().setBackground(new Color(46, 95, 163));
        tablaPagos.getTableHeader().setForeground(Color.WHITE);

        JPanel panelPagos = new JPanel(new BorderLayout());
        panelPagos.setBackground(Color.WHITE);
        panelPagos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247)),
                "Pagos del período",
                0, 0,
                new Font("Arial", Font.BOLD, 13),
                new Color(31, 56, 100)
        ));
        panelPagos.add(new JScrollPane(tablaPagos), BorderLayout.CENTER);
        panelTablas.add(panelPagos);

        // Tabla agrupada por mes y tipo
        modeloAgrupado = new DefaultTableModel(
                new String[]{"Agrupación", "Total ($)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaAgrupado = new JTable(modeloAgrupado);
        tablaAgrupado.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaAgrupado.setRowHeight(28);
        tablaAgrupado.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaAgrupado.getTableHeader().setBackground(new Color(46, 95, 163));
        tablaAgrupado.getTableHeader().setForeground(Color.WHITE);

        JPanel panelAgrupado = new JPanel(new BorderLayout());
        panelAgrupado.setBackground(Color.WHITE);
        panelAgrupado.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247)),
                "Ingresos por mes y tipo de membresía",
                0, 0,
                new Font("Arial", Font.BOLD, 13),
                new Color(31, 56, 100)
        ));
        panelAgrupado.add(new JScrollPane(tablaAgrupado), BorderLayout.CENTER);
        panelTablas.add(panelAgrupado);

        add(panelTablas, BorderLayout.CENTER);
    }

    /**
     * Genera el reporte consultando el servicio con las fechas ingresadas
     * y carga los resultados en las tablas.
     */
    private void generarReporte() {
        try {
            LocalDate inicio = LocalDate.parse(txtFechaInicio.getText().trim());
            LocalDate fin = LocalDate.parse(txtFechaFin.getText().trim());

            // Limpiar tablas
            modeloPagos.setRowCount(0);
            modeloAgrupado.setRowCount(0);

            // Cargar pagos del período
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            reporteService.obtenerPagosPorPeriodo(inicio, fin).forEach(p ->
                    modeloPagos.addRow(new Object[]{
                            p.getIdPago(),
                            p.getFechaPago().format(fmt),
                            String.format("$ %,.2f", p.getMonto()),
                            p.getMetodoPago()
                    })
            );

            // Cargar ingresos por mes
            modeloAgrupado.addRow(new Object[]{"── POR MES ──", ""});
            for (Map.Entry<String, Double> entry : reporteService.ingresosPorMes(inicio, fin).entrySet()) {
                modeloAgrupado.addRow(new Object[]{
                        entry.getKey(),
                        String.format("$ %,.2f", entry.getValue())
                });
            }

            // Cargar ingresos por tipo de membresía
            modeloAgrupado.addRow(new Object[]{"── POR TIPO ──", ""});
            for (Map.Entry<String, Double> entry : reporteService.ingresosPorTipoMembresia(inicio, fin).entrySet()) {
                modeloAgrupado.addRow(new Object[]{
                        entry.getKey().toUpperCase(),
                        String.format("$ %,.2f", entry.getValue())
                });
            }

            // Mostrar total
            double total = reporteService.calcularTotalIngresos(inicio, fin);
            lblTotal.setText(String.format("Total de ingresos: $ %,.2f", total));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error al generar reporte",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Limpia los filtros y las tablas del panel.
     */
    private void limpiar() {
        txtFechaInicio.setText(LocalDate.now().withDayOfMonth(1).toString());
        txtFechaFin.setText(LocalDate.now().toString());
        modeloPagos.setRowCount(0);
        modeloAgrupado.setRowCount(0);
        lblTotal.setText("Total de ingresos: —");
    }
}