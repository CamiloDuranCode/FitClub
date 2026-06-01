package fitclub.view;

import fitclub.dao.MembresiaDAO;
import fitclub.dao.PagoDAO;
import fitclub.service.ReporteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        JLabel titulo = new JLabel("📊  Reporte de Ingresos");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(31, 56, 100));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel panelFiltros = new JPanel(new GridLayout(2, 1, 0, 5));
        panelFiltros.setBackground(Color.WHITE);
        panelFiltros.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 228, 247), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JPanel filaFechas = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filaFechas.setBackground(Color.WHITE);
        JPanel filaTotal = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        filaTotal.setBackground(Color.WHITE);

        filaFechas.add(new JLabel("Fecha inicio (yyyy-MM-dd):"));
        txtFechaInicio = new JTextField(12);
        txtFechaInicio.setText(LocalDate.now().withDayOfMonth(1).toString());
        filaFechas.add(txtFechaInicio);

        filaFechas.add(new JLabel("Fecha fin (yyyy-MM-dd):"));
        txtFechaFin = new JTextField(12);
        txtFechaFin.setText(LocalDate.now().toString());
        filaFechas.add(txtFechaFin);

        JButton btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setBackground(new Color(46, 95, 163));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setFont(new Font("Arial", Font.BOLD, 13));
        btnGenerar.setFocusPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.addActionListener(e -> generarReporte());
        filaFechas.add(btnGenerar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBackground(new Color(242, 245, 250));
        btnLimpiar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(e -> limpiar());
        filaFechas.add(btnLimpiar);

        panelFiltros.add(filaFechas);
        panelFiltros.add(filaTotal);

        lblTotal = new JLabel("Total de ingresos: —");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 15));
        lblTotal.setForeground(new Color(55, 86, 35));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        filaTotal.add(lblTotal);

        add(panelFiltros, BorderLayout.NORTH);

        JPanel panelTablas = new JPanel(new GridLayout(1, 2, 15, 0));
        panelTablas.setBackground(new Color(242, 245, 250));

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
                "Pagos del período", 0, 0,
                new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)));
        panelPagos.add(new JScrollPane(tablaPagos), BorderLayout.CENTER);
        panelTablas.add(panelPagos);

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
                "Ingresos por mes y tipo de membresía", 0, 0,
                new Font("Arial", Font.BOLD, 13), new Color(31, 56, 100)));
        panelAgrupado.add(new JScrollPane(tablaAgrupado), BorderLayout.CENTER);
        panelTablas.add(panelAgrupado);

        add(panelTablas, BorderLayout.CENTER);
    }

    private void generarReporte() {
        try {
            LocalDate inicio = LocalDate.parse(txtFechaInicio.getText().trim());
            LocalDate fin = LocalDate.parse(txtFechaFin.getText().trim());

            modeloPagos.setRowCount(0);
            modeloAgrupado.setRowCount(0);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            reporteService.obtenerPagosPorPeriodo(inicio, fin).forEach(p ->
                    modeloPagos.addRow(new Object[]{
                            p.getIdPago(),
                            p.getFechaPago().format(fmt),
                            String.format("$ %,.2f", p.getMonto()),
                            p.getMetodoPago()
                    }));

            modeloAgrupado.addRow(new Object[]{"── POR MES ──", ""});
            for (Map.Entry<String, Double> entry : reporteService.ingresosPorMes(inicio, fin).entrySet()) {
                modeloAgrupado.addRow(new Object[]{entry.getKey(), String.format("$ %,.2f", entry.getValue())});
            }

            modeloAgrupado.addRow(new Object[]{"── POR TIPO ──", ""});
            for (Map.Entry<String, Double> entry : reporteService.ingresosPorTipoMembresia(inicio, fin).entrySet()) {
                modeloAgrupado.addRow(new Object[]{entry.getKey().toUpperCase(), String.format("$ %,.2f", entry.getValue())});
            }

            lblTotal.setText(String.format("Total de ingresos: $ %,.2f", reporteService.calcularTotalIngresos(inicio, fin)));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error al generar reporte", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtFechaInicio.setText(LocalDate.now().withDayOfMonth(1).toString());
        txtFechaFin.setText(LocalDate.now().toString());
        modeloPagos.setRowCount(0);
        modeloAgrupado.setRowCount(0);
        lblTotal.setText("Total de ingresos: —");
    }
}