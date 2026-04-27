package sembako.view;

import sembako.Koneksi;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PengaturanForm extends JPanel {

    private JTextField   txtUsername, txtNamaLengkap;
    private JPasswordField txtPasswordLama, txtPasswordBaru, txtKonfirmasi;
    private JButton      btnSimpanProfile, btnGantiPassword;
    private JLabel       lblStatusProfile, lblStatusPassword;
    private int          currentIdUser;
    private String       currentUsername;

    public PengaturanForm(int idUser, String username) {
        this.currentIdUser  = idUser;
        this.currentUsername = username;
        setLayout(new BorderLayout());
        setBackground(new Color(240,244,240));
        initComponents();
        loadProfile();
    }

    private void initComponents() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,new Color(229,231,235)),
            BorderFactory.createEmptyBorder(16,20,16,20)
        ));
        JLabel judul = new JLabel("Pengaturan Akun");
        judul.setFont(new Font("Georgia", Font.BOLD, 20));
        judul.setForeground(new Color(27,67,50));
        header.add(judul, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(240,244,240));
        content.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JPanel kartuProfil = buatKartu("Informasi Profil", "Ubah nama dan username akun Anda");

        txtUsername    = buatField();
        txtNamaLengkap = buatField();
        lblStatusProfile = new JLabel(" ");
        lblStatusProfile.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        btnSimpanProfile = buatTombol("Simpan Profil", new Color(27,67,50), Color.WHITE);
        btnSimpanProfile.addActionListener(e -> simpanProfil());

        kartuProfil.add(buatRow("Username", txtUsername));
        kartuProfil.add(Box.createVerticalStrut(12));
        kartuProfil.add(buatRow("Nama Lengkap", txtNamaLengkap));
        kartuProfil.add(Box.createVerticalStrut(6));
        kartuProfil.add(lblStatusProfile);
        kartuProfil.add(Box.createVerticalStrut(14));
        kartuProfil.add(btnSimpanProfile);

        JPanel kartuPass = buatKartu("Ganti Password", "Pastikan password baru minimal 6 karakter");

        txtPasswordLama = buatPass();
        txtPasswordBaru = buatPass();
        txtKonfirmasi   = buatPass();
        lblStatusPassword = new JLabel(" ");
        lblStatusPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        btnGantiPassword = buatTombol("Ganti Password", new Color(37,99,235), Color.WHITE);
        btnGantiPassword.addActionListener(e -> gantiPassword());

        kartuPass.add(buatRowPass("Password Lama", txtPasswordLama));
        kartuPass.add(Box.createVerticalStrut(12));
        kartuPass.add(buatRowPass("Password Baru", txtPasswordBaru));
        kartuPass.add(Box.createVerticalStrut(12));
        kartuPass.add(buatRowPass("Konfirmasi Password Baru", txtKonfirmasi));
        kartuPass.add(Box.createVerticalStrut(6));
        kartuPass.add(lblStatusPassword);
        kartuPass.add(Box.createVerticalStrut(14));
        kartuPass.add(btnGantiPassword);

        JPanel kartuInfo = buatKartu("Informasi Aplikasi", "");
        String[][] infoItems = {
            {"Nama Aplikasi", "Simbas"},
            {"Versi",         "1.0.0"},
            {"Database",      "MySQL via JDBC"},
            {"Framework",     "Java Swing"},
            {"Dibuat untuk",  "Tugas Akhir"}
        };
        for (String[] item : infoItems) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
            JLabel lKey = new JLabel(item[0]);
            lKey.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lKey.setForeground(new Color(107,114,128));
            lKey.setPreferredSize(new Dimension(160, 20));
            JLabel lVal = new JLabel(item[1]);
            lVal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lVal.setForeground(new Color(31,41,55));
            row.add(lKey, BorderLayout.WEST);
            row.add(lVal, BorderLayout.CENTER);
            kartuInfo.add(row);
            kartuInfo.add(Box.createVerticalStrut(4));
        }

        content.add(kartuProfil);
        content.add(Box.createVerticalStrut(16));
        content.add(kartuPass);
        content.add(Box.createVerticalStrut(16));
        content.add(kartuInfo);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buatKartu(String judul, String sub) {
        JPanel kartu = new JPanel();
        kartu.setLayout(new BoxLayout(kartu, BoxLayout.Y_AXIS));
        kartu.setBackground(Color.WHITE);
        kartu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229,231,235)),
            BorderFactory.createEmptyBorder(20,24,20,24)
        ));
        kartu.setAlignmentX(Component.LEFT_ALIGNMENT);
        kartu.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel lblJudul = new JLabel(judul);
        lblJudul.setFont(new Font("Georgia", Font.BOLD, 16));
        lblJudul.setForeground(new Color(27,67,50));
        lblJudul.setAlignmentX(Component.LEFT_ALIGNMENT);
        kartu.add(lblJudul);

        if (!sub.isEmpty()) {
            JLabel lblSub = new JLabel(sub);
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblSub.setForeground(new Color(107,114,128));
            lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
            kartu.add(Box.createVerticalStrut(4));
            kartu.add(lblSub);
        }

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(229,231,235));
        kartu.add(Box.createVerticalStrut(14));
        kartu.add(sep);
        kartu.add(Box.createVerticalStrut(14));

        return kartu;
    }

    private JPanel buatRow(String label, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(0,4));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(500, 56));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(27,67,50));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209,213,219)),
            BorderFactory.createEmptyBorder(7,10,7,10)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JPanel buatRowPass(String label, JPasswordField field) {
        JPanel p = new JPanel(new BorderLayout(0,4));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(500, 56));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(27,67,50));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209,213,219)),
            BorderFactory.createEmptyBorder(7,10,7,10)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JTextField buatField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return f;
    }

    private JPasswordField buatPass() {
        JPasswordField f = new JPasswordField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return f;
    }

    private JButton buatTombol(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(bg); b.setForeground(fg);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(160, 36));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        return b;
    }

    private void loadProfile() {
        String sql = "SELECT username, nama_lengkap FROM users WHERE id_user=?";
        try (Connection c = Koneksi.getConnection();
             java.sql.PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, currentIdUser);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtUsername.setText(rs.getString("username"));
                txtNamaLengkap.setText(rs.getString("nama_lengkap"));
            }
        } catch (Exception e) { System.err.println(e.getMessage()); }
    }

    private void simpanProfil() {
        String username = txtUsername.getText().trim();
        String nama     = txtNamaLengkap.getText().trim();
        if (username.isEmpty() || nama.isEmpty()) {
            setStatus(lblStatusProfile, "Username dan nama lengkap wajib diisi!", false);
            return;
        }
        String sql = "UPDATE users SET username=?, nama_lengkap=? WHERE id_user=?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username); ps.setString(2, nama); ps.setInt(3, currentIdUser);
            if (ps.executeUpdate() > 0) setStatus(lblStatusProfile, "✓ Profil berhasil disimpan!", true);
            else setStatus(lblStatusProfile, "Gagal menyimpan profil.", false);
        } catch (Exception e) { setStatus(lblStatusProfile, "Error: " + e.getMessage(), false); }
    }

    private void gantiPassword() {
        String lama  = new String(txtPasswordLama.getPassword()).trim();
        String baru  = new String(txtPasswordBaru.getPassword()).trim();
        String konf  = new String(txtKonfirmasi.getPassword()).trim();

        if (lama.isEmpty() || baru.isEmpty() || konf.isEmpty()) {
            setStatus(lblStatusPassword, "Semua field password wajib diisi!", false); return;
        }
        if (!baru.equals(konf)) {
            setStatus(lblStatusPassword, "Password baru dan konfirmasi tidak cocok!", false); return;
        }
        if (baru.length() < 6) {
            setStatus(lblStatusPassword, "Password baru minimal 6 karakter!", false); return;
        }

        String cekSql = "SELECT COUNT(*) FROM users WHERE id_user=? AND password=?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(cekSql)) {
            ps.setInt(1, currentIdUser); ps.setString(2, lama);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                setStatus(lblStatusPassword, "Password lama salah!", false); return;
            }
        } catch (Exception e) { setStatus(lblStatusPassword, "Error: " + e.getMessage(), false); return; }

        String updSql = "UPDATE users SET password=? WHERE id_user=?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(updSql)) {
            ps.setString(1, baru); ps.setInt(2, currentIdUser);
            if (ps.executeUpdate() > 0) {
                setStatus(lblStatusPassword, "✓ Password berhasil diubah!", true);
                txtPasswordLama.setText(""); txtPasswordBaru.setText(""); txtKonfirmasi.setText("");
            }
        } catch (Exception e) { setStatus(lblStatusPassword, "Gagal mengubah password.", false); }
    }

    private void setStatus(JLabel lbl, String msg, boolean ok) {
        lbl.setText(msg);
        lbl.setForeground(ok ? new Color(5,150,105) : new Color(220,38,38));
    }
}
