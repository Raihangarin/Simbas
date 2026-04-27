package sembako.view;

import sembako.dao.BarangDAO;
import sembako.model.Barang;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FormBarang extends JDialog {

    private JTextField txtKode, txtNama, txtSatuan, txtHargaBeli, txtHargaJual;
    private JTextField txtStokAwal, txtStokAkhir, txtTanggal, txtKeterangan;
    private JComboBox<String> cbKategori;
    private JButton btnSimpan, btnBatal;

    private final BarangDAO barangDAO = new BarangDAO();
    private final DashboardForm parent;
    private final Barang barangEdit; 
    private int[] idKategoriList;

    public FormBarang(DashboardForm parent, Barang barang) {
        super(parent, barang == null ? "Tambah Barang Baru" : "Edit Barang", true);
        this.parent = parent;
        this.barangEdit = barang;

        setSize(480, 560);
        setLocationRelativeTo(parent);
        setResizable(false);

        initComponents();

        if (barang != null) isiDataEdit(barang);
    }

    private void initComponents() {
        JPanel main = new JPanel();
        main.setBackground(new Color(253, 246, 227));
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JLabel lblJudul = new JLabel(barangEdit == null ? "➕  Tambah Barang Baru" : "✏️  Edit Barang");
        lblJudul.setFont(new Font("Georgia", Font.BOLD, 20));
        lblJudul.setForeground(new Color(27, 67, 50));
        lblJudul.setAlignmentX(Component.LEFT_ALIGNMENT);
        main.add(lblJudul);
        main.add(Box.createVerticalStrut(20));

        List<String[]> katList = barangDAO.getAllKategori();
        idKategoriList = new int[katList.size()];
        String[] katNames = new String[katList.size()];
        for (int i = 0; i < katList.size(); i++) {
            idKategoriList[i] = Integer.parseInt(katList.get(i)[0]);
            katNames[i] = katList.get(i)[1];
        }
        cbKategori = new JComboBox<>(katNames);

        txtKode       = buatField();
        txtNama       = buatField();
        txtSatuan     = buatField();
        txtHargaBeli  = buatField();
        txtHargaJual  = buatField();
        txtStokAwal   = buatField();
        txtStokAkhir  = buatField();
        txtTanggal    = buatField();
        txtTanggal.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        txtKeterangan = buatField();

        main.add(buatRow("Kode Barang *", txtKode));
        main.add(Box.createVerticalStrut(10));
        main.add(buatRow("Nama Barang *", txtNama));
        main.add(Box.createVerticalStrut(10));
        main.add(buatRowKategori("Kategori *", cbKategori));
        main.add(Box.createVerticalStrut(10));
        main.add(buatRow("Satuan (Kg/Liter/Pcs/dll) *", txtSatuan));
        main.add(Box.createVerticalStrut(10));

        JPanel hargaRow = new JPanel(new GridLayout(1, 2, 10, 0));
        hargaRow.setOpaque(false);
        hargaRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        hargaRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        hargaRow.add(buatColField("Harga Beli (Rp) *", txtHargaBeli));
        hargaRow.add(buatColField("Harga Jual (Rp) *", txtHargaJual));
        main.add(hargaRow);
        main.add(Box.createVerticalStrut(10));

        JPanel stokRow = new JPanel(new GridLayout(1, 2, 10, 0));
        stokRow.setOpaque(false);
        stokRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        stokRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        stokRow.add(buatColField("Stok Awal *", txtStokAwal));
        stokRow.add(buatColField("Stok Akhir *", txtStokAkhir));
        main.add(stokRow);
        main.add(Box.createVerticalStrut(10));

        main.add(buatRow("Tanggal Masuk (yyyy-MM-dd) *", txtTanggal));
        main.add(Box.createVerticalStrut(10));
        main.add(buatRow("Keterangan", txtKeterangan));
        main.add(Box.createVerticalStrut(24));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnBatal = new JButton("Batal");
        btnBatal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnBatal.setBackground(new Color(243, 244, 246));
        btnBatal.setForeground(new Color(75, 85, 99));
        btnBatal.setFocusPainted(false);
        btnBatal.setBorderPainted(false);
        btnBatal.setPreferredSize(new Dimension(100, 36));
        btnBatal.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBatal.addActionListener(e -> dispose());

        btnSimpan = new JButton(barangEdit == null ? "💾  Simpan" : "💾  Update");
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSimpan.setBackground(new Color(27, 67, 50));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setBorderPainted(false);
        btnSimpan.setPreferredSize(new Dimension(120, 36));
        btnSimpan.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSimpan.addActionListener(e -> simpanData());

        btnPanel.add(btnBatal);
        btnPanel.add(btnSimpan);
        main.add(btnPanel);

        if (barangEdit != null) txtKode.setEnabled(false);

        JScrollPane scroll = new JScrollPane(main);
        scroll.setBorder(null);
        setContentPane(scroll);
    }

    private JTextField buatField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return f;
    }

    private JPanel buatRow(String label, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(27, 67, 50));
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JPanel buatRowKategori(String label, JComboBox<String> cb) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(27, 67, 50));
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        p.add(lbl, BorderLayout.NORTH);
        p.add(cb, BorderLayout.CENTER);
        return p;
    }

    private JPanel buatColField(String label, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(27, 67, 50));
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private void isiDataEdit(Barang b) {
        txtKode.setText(b.getKodeBarang());
        txtNama.setText(b.getNamaBarang());
        txtSatuan.setText(b.getSatuan());
        txtHargaBeli.setText(String.valueOf((long)b.getHargaBeli()));
        txtHargaJual.setText(String.valueOf((long)b.getHargaJual()));
        txtStokAwal.setText(String.valueOf(b.getStokAwal()));
        txtStokAkhir.setText(String.valueOf(b.getStokAkhir()));
        if (b.getTanggalMasuk() != null)
            txtTanggal.setText(new SimpleDateFormat("yyyy-MM-dd").format(b.getTanggalMasuk()));
        txtKeterangan.setText(b.getKeterangan());

        List<String[]> katList = barangDAO.getAllKategori();
        for (int i = 0; i < katList.size(); i++) {
            if (katList.get(i)[1].equalsIgnoreCase(b.getNamaKategori())) {
                cbKategori.setSelectedIndex(i);
                break;
            }
        }
    }

    private void simpanData() {
        if (txtKode.getText().trim().isEmpty() || txtNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode dan Nama barang wajib diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Barang b = new Barang();
            b.setKodeBarang(txtKode.getText().trim().toUpperCase());
            b.setNamaBarang(txtNama.getText().trim());
            b.setIdKategori(idKategoriList[cbKategori.getSelectedIndex()]);
            b.setSatuan(txtSatuan.getText().trim());
            b.setHargaBeli(Double.parseDouble(txtHargaBeli.getText().trim().replace(",", "")));
            b.setHargaJual(Double.parseDouble(txtHargaJual.getText().trim().replace(",", "")));
            b.setStokAwal(Integer.parseInt(txtStokAwal.getText().trim()));
            b.setStokAkhir(Integer.parseInt(txtStokAkhir.getText().trim()));
            b.setTanggalMasuk(new SimpleDateFormat("yyyy-MM-dd").parse(txtTanggal.getText().trim()));
            b.setKeterangan(txtKeterangan.getText().trim());

            boolean ok;
            if (barangEdit == null) {
                if (barangDAO.isKodeExist(b.getKodeBarang())) {
                    JOptionPane.showMessageDialog(this, "Kode barang sudah ada!", "Duplikat", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ok = barangDAO.tambahBarang(b);
            } else {
                b.setIdBarang(barangEdit.getIdBarang());
                ok = barangDAO.updateBarang(b);
            }

            if (ok) {
                JOptionPane.showMessageDialog(this,
                    barangEdit == null ? "Barang berhasil ditambahkan!" : "Barang berhasil diperbarui!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Format angka tidak valid! Pastikan harga dan stok berupa angka.", "Error Format", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Format tanggal salah! Gunakan: yyyy-MM-dd (contoh: 2026-04-10)", "Error Tanggal", JOptionPane.ERROR_MESSAGE);
        }
    }
}
