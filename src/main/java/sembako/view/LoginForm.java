package sembako.view;

import sembako.Koneksi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblStatus;

    public LoginForm() {
        setTitle("Simbas - Login");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(27, 67, 50));

        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(320, 500));
        leftPanel.setBackground(new Color(13, 43, 30));
        leftPanel.setLayout(new GridBagLayout());

        JPanel leftContent = new JPanel();
        leftContent.setOpaque(false);
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));

        JLabel lblIcon = new JLabel("🌾", JLabel.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAppName = new JLabel("<html><center>Simbas</center></html>", JLabel.CENTER);
        lblAppName.setFont(new Font("Georgia", Font.BOLD, 28));
        lblAppName.setForeground(Color.WHITE);
        lblAppName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><center>Sistem Manajemen Barang &<br>Stok Penjualan Sembako<br>Kota Malang</center></html>", JLabel.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(82, 183, 136));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] fiturList = {
            "✓  Pencatatan stok otomatis",
            "✓  Hitung selisih stok real-time",
            "✓  Riwayat transaksi lengkap",
            "✓  Akses aman hanya pemilik"
        };

        JPanel fiturPanel = new JPanel();
        fiturPanel.setOpaque(false);
        fiturPanel.setLayout(new BoxLayout(fiturPanel, BoxLayout.Y_AXIS));
        fiturPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        for (String f : fiturList) {
            JLabel lf = new JLabel(f);
            lf.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lf.setForeground(new Color(200, 200, 200));
            lf.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
            fiturPanel.add(lf);
        }

        leftContent.add(Box.createVerticalStrut(10));
        leftContent.add(lblIcon);
        leftContent.add(Box.createVerticalStrut(12));
        leftContent.add(lblAppName);
        leftContent.add(Box.createVerticalStrut(10));
        leftContent.add(lblDesc);
        leftContent.add(Box.createVerticalStrut(20));
        leftContent.add(fiturPanel);

        leftPanel.add(leftContent);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(253, 246, 227));
        rightPanel.setLayout(new GridBagLayout());

        JPanel formContent = new JPanel();
        formContent.setOpaque(false);
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setPreferredSize(new Dimension(340, 350));

        JLabel lblWelcome = new JLabel("Selamat Datang 👋");
        lblWelcome.setFont(new Font("Georgia", Font.BOLD, 26));
        lblWelcome.setForeground(new Color(27, 67, 50));
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubWelcome = new JLabel("Masuk untuk mengelola stok sembako Anda");
        lblSubWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubWelcome.setForeground(new Color(107, 114, 128));
        lblSubWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblUser = new JLabel("USERNAME");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblUser.setForeground(new Color(27, 67, 50));
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtUsername.setPreferredSize(new Dimension(300, 40));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(82, 183, 136), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPass = new JLabel("PASSWORD");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblPass.setForeground(new Color(27, 67, 50));
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPassword.setPreferredSize(new Dimension(300, 40));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnLogin = new JButton("🔑  Masuk ke Sistem");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(27, 67, 50));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnLogin.setPreferredSize(new Dimension(300, 44));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnLogin.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(45, 106, 79));
            }
            @Override public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(27, 67, 50));
            }
        });

        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(220, 38, 38));
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);

        formContent.add(lblWelcome);
        formContent.add(Box.createVerticalStrut(4));
        formContent.add(lblSubWelcome);
        formContent.add(Box.createVerticalStrut(24));
        formContent.add(lblUser);
        formContent.add(Box.createVerticalStrut(6));
        formContent.add(txtUsername);
        formContent.add(Box.createVerticalStrut(14));
        formContent.add(lblPass);
        formContent.add(Box.createVerticalStrut(6));
        formContent.add(txtPassword);
        formContent.add(Box.createVerticalStrut(6));
        formContent.add(lblStatus);
        formContent.add(Box.createVerticalStrut(12));
        formContent.add(btnLogin);

        rightPanel.add(formContent);

        btnLogin.addActionListener(e -> doLogin());
        txtPassword.addActionListener(e -> doLogin());
        txtUsername.addActionListener(e -> txtPassword.requestFocus());

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("⚠ Username dan password tidak boleh kosong!");
            return;
        }

        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String namaLengkap = rs.getString("nama_lengkap");
                lblStatus.setForeground(new Color(5, 150, 105));
                lblStatus.setText("✓ Login berhasil! Selamat datang, " + namaLengkap);

                SwingUtilities.invokeLater(() -> {
                    DashboardForm dashboard = new DashboardForm(namaLengkap);
                    dashboard.setVisible(true);
                    this.dispose();
                });
            } else {
                lblStatus.setForeground(new Color(220, 38, 38));
                lblStatus.setText("✗ Username atau password salah!");
                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        } catch (SQLException e) {
            lblStatus.setText("✗ Gagal terhubung ke database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

        }

        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
