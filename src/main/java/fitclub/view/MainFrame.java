package fitclub.view;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del Sistema de Gestión Fit Club.
 * Contiene un menú lateral (sidebar) desde el cual se navega
 * entre los diferentes módulos del sistema.
 *
 * @author Camilo Andrés Durán Baquero
 */
public class MainFrame extends JFrame {

    private JPanel sidebar;
    private JPanel contenedor;

    public MainFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Fit Club — Sistema de Gestión");
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        header.setBackground(new Color(31, 56, 100));
        header.setPreferredSize(new Dimension(getWidth(), 55));
        JLabel titulo = new JLabel("🏋️  FIT CLUB — Sistema de Gestión");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        header.add(titulo);
        add(header, BorderLayout.NORTH);

        // ===== SIDEBAR =====
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(46, 95, 163));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel menuLabel = new JLabel("MENÚ PRINCIPAL");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 12));
        menuLabel.setForeground(new Color(214, 228, 247));
        menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        sidebar.add(menuLabel);

        sidebar.add(crearBotonMenu("👥  Clientes", "Clientes"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(crearBotonMenu("💪  Entrenadores", "Entrenadores"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(crearBotonMenu("💳  Membresías", "Membresias"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(crearBotonMenu("📋  Asistencia", "Asistencia"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(crearBotonMenu("🏃  Rutinas", "Rutinas"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(crearBotonMenu("📊  Reportes", "Reportes"));
        sidebar.add(Box.createVerticalGlue());

        JLabel version = new JLabel("v1.0.0 — 2025");
        version.setFont(new Font("Arial", Font.PLAIN, 10));
        version.setForeground(new Color(180, 200, 230));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(version);
        add(sidebar, BorderLayout.WEST);

        // ===== CONTENEDOR PRINCIPAL =====
        contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(new Color(242, 245, 250));

        JPanel bienvenida = new JPanel(new GridBagLayout());
        bienvenida.setBackground(new Color(242, 245, 250));
        JLabel lblBienvenida = new JLabel("Selecciona un módulo del menú lateral");
        lblBienvenida.setFont(new Font("Arial", Font.PLAIN, 16));
        lblBienvenida.setForeground(new Color(100, 100, 100));
        bienvenida.add(lblBienvenida);
        contenedor.add(bienvenida, BorderLayout.CENTER);
        add(contenedor, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton crearBotonMenu(String texto, String modulo) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(46, 95, 163));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(new Color(31, 56, 100)); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(new Color(46, 95, 163)); }
        });

        btn.addActionListener(e -> cargarModulo(modulo));
        return btn;
    }

    /**
     * Carga el panel correspondiente al módulo seleccionado
     * en el contenedor principal.
     *
     * @param modulo Nombre del módulo a cargar.
     */
    private void cargarModulo(String modulo) {
        contenedor.removeAll();
        switch (modulo) {
            case "Clientes"     -> contenedor.add(new ClienteForm(), BorderLayout.CENTER);
            case "Entrenadores" -> contenedor.add(new EntrenadorForm(), BorderLayout.CENTER);
            case "Membresias"   -> contenedor.add(new MembresiaForm(), BorderLayout.CENTER);
            case "Asistencia"   -> contenedor.add(new AsistenciaForm(), BorderLayout.CENTER);
            case "Rutinas"      -> contenedor.add(new RutinaForm(), BorderLayout.CENTER);
            case "Reportes"     -> contenedor.add(new ReportePanel(), BorderLayout.CENTER);
            default -> {
                JLabel proximamente = new JLabel("Módulo en desarrollo...", SwingConstants.CENTER);
                proximamente.setFont(new Font("Arial", Font.PLAIN, 15));
                proximamente.setForeground(new Color(150, 150, 150));
                contenedor.add(proximamente, BorderLayout.CENTER);
            }
        }
        contenedor.revalidate();
        contenedor.repaint();
    }
}