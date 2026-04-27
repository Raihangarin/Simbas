package sembako.view;

import sembako.dao.KategoriDAO;
import sembako.model.Kategori;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KategoriForm extends JPanel {

    private final KategoriDAO dao = new KategoriDAO();
    private JTable tbl;
    private DefaultTableModel model;
    private JTextField txtNama, txtKeterangan;
    private JButton btnTambah, btnUpdate, btnHapus, btnBatal;
    private int selectedId = -1;

    public KategoriForm() {
        setLayout(new BorderLayout());
        setBackground(new Color(240,244,240));
        initComponents();
        loadData();
    }

    private void initComponents() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,new Color(229,231,235)),
            BorderFactory.createEmptyBorder(16,20,16,20)
        ));
        JLabel judul = new JLabel("Manajemen Kategori");
        judul.setFont(new Font("Georgia", Font.BOLD, 20));
        judul.setForeground(new Color(27,67,50));
        header.add(judul, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(300);
        split.setBorder(null);
        split.setDividerSize(4);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20,20,20,16));

        JLabel lblForm = new JLabel(selectedId < 0 ? "Tambah Kategori Baru" : "Edit Kategori");
        lblForm.setFont(new Font("Georgia", Font.BOLD, 15));
        lblForm.setForeground(new Color(27,67,50));
        lblForm.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNama = buatField();
        txtKeterangan = buatField();

        btnTambah = buatBtn("+ Simpan Kategori", new Color(27,67,50), Color.WHITE);
        btnUpdate  = buatBtn("✎ Update", new Color(37,99,235), Color.WHITE);
        btnHapus   = buatBtn("Hapus", new Color(220,38,38), Color.WHITE);
        btnBatal   = buatBtn("Batal", new Color(243,244,246), new Color(75,85,99));

        btnUpdate.setVisible(false);
        btnHapus.setVisible(false);
        btnBatal.setVisible(false);

        btnTambah.addActionListener(e -> tambah());
        btnUpdate.addActionListener(e -> update());
        btnHapus.addActionListener(e -> hapus());
        btnBatal.addActionListener(e -> resetForm());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.add(btnTambah);
        btnRow.add(btnUpdate);
        btnRow.add(btnHapus);
        btnRow.add(btnBatal);

        form.add(lblForm);
        form.add(Box.createVerticalStrut(16));
        form.add(buatRow("Nama Kategori *", txtNama));
        form.add(Box.createVerticalStrut(12));
        form.add(buatRow("Keterangan", txtKeterangan));
        form.add(Box.createVerticalStrut(20));
        form.add(btnRow);

        JPanel tblPanel = new JPanel(new BorderLayout());
        tblPanel.setBackground(new Color(248,250,248));
        tblPanel.setBorder(BorderFactory.createEmptyBorder(16,16,16,20));

        JLabel lblTbl = new JLabel("Daftar Kategori");
        lblTbl.setFont(new Font("Georgia", Font.BOLD, 15));
        lblTbl.setForeground(new Color(27,67,50));
        lblTbl.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        String[] kol = {"ID","Nama Kategori","Keterangan"};
        model = new DefaultTableModel(kol, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tbl = new JTable(model);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tbl.setRowHeight(30);
        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tbl.getTableHeader().setBackground(new Color(248,250,248));
        tbl.setGridColor(new Color(229,231,235));
        tbl.getColumnModel().getColumn(0).setMaxWidth(40);

        tbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) isiFormDariTabel();
        });

        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(229,231,235)));

        tblPanel.add(lblTbl, BorderLayout.NORTH);
        tblPanel.add(scroll, BorderLayout.CENTER);

        split.setLeftComponent(form);
        split.setRightComponent(tblPanel);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buatRow(String label, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(0,4));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(27,67,50));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209,213,219)),
            BorderFactory.createEmptyBorder(6,10,6,10)
        ));
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JTextField buatField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return f;
    }

    private JButton buatBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBackground(bg); b.setForeground(fg);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void loadData() {
        model.setRowCount(0);
        for (Kategori k : dao.getAll()) {
            model.addRow(new Object[]{k.getIdKategori(), k.getNamaKategori(), k.getKeterangan()});
        }
    }

    private void isiFormDariTabel() {
        int row = tbl.getSelectedRow();
        if (row < 0) return;
        selectedId = (int) model.getValueAt(row, 0);
        txtNama.setText((String) model.getValueAt(row, 1));
        txtKeterangan.setText((String) model.getValueAt(row, 2));
        btnTambah.setVisible(false);
        btnUpdate.setVisible(true);
        btnHapus.setVisible(true);
        btnBatal.setVisible(true);
    }

    private void tambah() {
        String nama = txtNama.getText().trim();
        if (nama.isEmpty()) { JOptionPane.showMessageDialog(this, "Nama kategori wajib diisi!"); return; }
        Kategori k = new Kategori();
        k.setNamaKategori(nama);
        k.setKeterangan(txtKeterangan.getText().trim());
        if (dao.tambah(k)) { loadData(); resetForm(); JOptionPane.showMessageDialog(this, "Kategori berhasil ditambahkan!"); }
        else JOptionPane.showMessageDialog(this, "Gagal menambahkan kategori!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void update() {
        String nama = txtNama.getText().trim();
        if (nama.isEmpty()) { JOptionPane.showMessageDialog(this, "Nama kategori wajib diisi!"); return; }
        Kategori k = new Kategori();
        k.setIdKategori(selectedId);
        k.setNamaKategori(nama);
        k.setKeterangan(txtKeterangan.getText().trim());
        if (dao.update(k)) { loadData(); resetForm(); JOptionPane.showMessageDialog(this, "Kategori berhasil diperbarui!"); }
        else JOptionPane.showMessageDialog(this, "Gagal memperbarui!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void hapus() {
        int opt = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus kategori ini?\nBarang yang terkait mungkin terpengaruh.", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            if (dao.hapus(selectedId)) { loadData(); resetForm(); JOptionPane.showMessageDialog(this, "Kategori berhasil dihapus!"); }
            else JOptionPane.showMessageDialog(this, "Gagal menghapus! Kategori mungkin masih digunakan oleh barang.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        selectedId = -1;
        txtNama.setText(""); txtKeterangan.setText("");
        btnTambah.setVisible(true);
        btnUpdate.setVisible(false); btnHapus.setVisible(false); btnBatal.setVisible(false);
        tbl.clearSelection();
    }
}
