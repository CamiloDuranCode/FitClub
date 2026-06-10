package fitclub.view;

import fitclub.dao.UsuarioDAO;
import fitclub.model.Usuario;
import fitclub.service.SesionActual;
import fitclub.service.UsuarioService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Formulario de inicio de sesión del sistema Fit Club.
 * Valida credenciales via fn_login y carga MainFrame según el rol.
 *
 * @author Wilberto Ariza Zapata
 */
public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblError;

    private final UsuarioService usuarioService;

    public LoginForm() {
        this.usuarioService = new UsuarioService(new UsuarioDAO());
        initComponents();
    }

    private void initComponents() {
        setTitle("Fit Club — Iniciar Sesión");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(242, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        JLabel titulo = new JLabel("🏋️  FIT CLUB", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(new Color(31, 56, 100));
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(titulo, gbc);

        JLabel subtitulo = new JLabel("Sistema de Gestión", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(100, 100, 100));
        gbc.gridy = 1;
        panel.add(subtitulo, gbc);

        gbc.gridy = 2;
        panel.add(Box.createVerticalStrut(10), gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        txtUsername = new JTextField(15);
        panel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(15);
        panel.add(txtPassword, gbc);

        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 5;
        lblError = new JLabel(" ", SwingConstants.CENTER);
        lblError.setForeground(new Color(180, 50, 50));
        lblError.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblError, gbc);

        gbc.gridy = 6;
        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setBackground(new Color(46, 95, 163));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 14));
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setPreferredSize(new Dimension(200, 38));
        panel.add(btnIngresar, gbc);

        btnIngresar.addActionListener(e -> intentarLogin());
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) intentarLogin();
            }
        });

        add(panel);
        setVisible(true);
    }

    private void intentarLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Ingrese usuario y contraseña.");
            return;
        }

        try {
            Usuario usuario = usuarioService.login(username, password);
            if (usuario == null) {
                lblError.setText("Usuario o contraseña incorrectos.");
                txtPassword.setText("");
                return;
            }
            SesionActual.getInstancia().iniciarSesion(usuario);
            dispose();
            SwingUtilities.invokeLater(MainFrame::new);
        } catch (RuntimeException ex) {
            lblError.setText("Error de conexión: " + ex.getMessage());
        }
    }
}