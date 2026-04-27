package sembako.view;

import sembako.dao.BarangDAO;
import sembako.dao.TransaksiDAO;
import sembako.model.Barang;
import sembako.model.DetailTransaksi;
import sembako.model.Transaksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class TransaksiForm extends JPanel {

    private final TransaksiDAO transaksiDAO = new TransaksiDAO();
    private final BarangDAO    barangDAO    = new BarangDAO();
    private static final NumberFormat FMT = NumberFormat.getCurrencyInstance(new Locale("id","ID"));

    private JComboBox<String>  cbJenis;
    private JComboBox<Object>  cbBarang;
    private JTextField         txtJumlah, txtHarga, txtKeterangan, txtNoTransaksi;
    private JLabel             lblTotal, lblSubtotal, lblStokInfo;
    private JTable             tblDetail;
    private DefaultTableModel  detailModel;
    private JButton            btnTambahItem, btnHapusItem, btnSimpan, btnBatal;

    private List<DetailTransaksi> detailList = new ArrayList<>();
    private List<Barang>          barangList;
    private int                   currentIdUser;
    private double                grandTotal = 0;

    public TransaksiForm(int idUser) {
        this.currentIdUser = idUser;
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(240, 244, 240));
        initComponents();
        loadBarang();
        updateNoTransaksi();
    }

    private void initComponents() {
            JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0, new Color(229,231,235)),
            BorderFactory.createEmptyBorder(16,20,16,20)
        ));
        JLabel lblJudul = new JLabel("Input Transaksi");
        lblJudul.setFont(new Font("Georgia", Font.BOLD, 20));
        lblJudul.setForeground(new Color(27,67,50));
        header.add(lblJudul, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(380);
        split.setBorder(null);
        split.setDividerSize(4);

        JPanel kiri = new JPanel();
        kiri.setLayout(new BoxLayout(kiri, BoxLayout.Y_AXIS));
        kiri.setBackground(Color.WHITE);
        kiri.setBorder(BorderFactory.createEmptyBorder(16,20,16,16));

        txtNoTransaksi = new JTextField();
        txtNoTransaksi.setEditable(false);
        txtNoTransaksi.setBackground(new Color(249,250,251));
        txtNoTransaksi.setFont(new Font("Courier New", Font.BOLD, 13));

        cbJenis = new JComboBox<>(new String[]{"keluar", "masuk"});
        cbJenis.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbJenis.addActionListener(e -> updateNoTransaksi());

        cbBarang = new JComboBox<>();
        cbBarang.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbBarang.addActionListener(e -> onBarangSelected());

        lblStokInfo = new JLabel(" ");
        lblStokInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStokInfo.setForeground(new Color(107,114,128));

        txtHarga = new JTextField("0");
        txtHarga.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtHarga.setEditable(false);
        txtHarga.setBackground(new Color(249,250,251));

        txtJumlah = new JTextField("1");
        txtJumlah.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtJumlah.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { hitungSubtotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { hitungSubtotal(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });

        lblSubtotal = new JLabel("Subtotal: Rp 0");
        lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSubtotal.setForeground(new Color(27,67,50));

        txtKeterangan = new JTextField();
        txtKeterangan.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        btnTambahItem = buatTombol("+ Tambah ke Daftar", new Color(27,67,50), Color.WHITE);
        btnTambahItem.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnTambahItem.addActionListener(e -> tambahItem());

        kiri.add(buatFormRow("No. Transaksi", txtNoTransaksi));
        kiri.add(Box.createVerticalStrut(10));
        kiri.add(buatFormRowCombo("Jenis Transaksi", cbJenis));
        kiri.add(Box.createVerticalStrut(10));
        kiri.add(buatFormRowCombo("Nama Barang", cbBarang));
        kiri.add(lblStokInfo);
        kiri.add(Box.createVerticalStrut(8));
        kiri.add(buatFormRow("Harga Satuan (Rp)", txtHarga));
        kiri.add(Box.createVerticalStrut(10));
        kiri.add(buatFormRow("Jumlah", txtJumlah));
        kiri.add(Box.createVerticalStrut(6));
        kiri.add(lblSubtotal);
        kiri.add(Box.createVerticalStrut(10));
        kiri.add(buatFormRow("Keterangan", txtKeterangan));
        kiri.add(Box.createVerticalStrut(16));
        kiri.add(btnTambahItem);

        JPanel kanan = new JPanel(new BorderLayout());
        kanan.setBackground(new Color(248,250,248));
        kanan.setBorder(BorderFactory.createEmptyBorder(16,16,16,20));

        JLabel lblDaftar = new JLabel("Daftar Item Transaksi");
        lblDaftar.setFont(new Font("Georgia", Font.BOLD, 15));
        lblDaftar.setForeground(new Color(27,67,50));
        lblDaftar.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        String[] kolom = {"Kode", "Nama Barang", "Jumlah", "Harga Satuan", "Subtotal"};
        detailModel = new DefaultTableModel(kolom, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblDetail = new JTable(detailModel);
        tblDetail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblDetail.setRowHeight(30);
        tblDetail.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblDetail.getTableHeader().setBackground(new Color(248,250,248));
        tblDetail.setGridColor(new Color(229,231,235));

        JScrollPane scroll = new JScrollPane(tblDetail);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(229,231,235)));

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(12,0,0,0));

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        totalPanel.setOpaque(false);
        lblTotal = new JLabel("TOTAL: Rp 0");
        lblTotal.setFont(new Font("Georgia", Font.BOLD, 18));
        lblTotal.setForeground(new Color(27,67,50));
        totalPanel.add(lblTotal);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);
        btnHapusItem = buatTombol("Hapus Item", new Color(254,242,242), new Color(220,38,38));
        btnHapusItem.setBorder(BorderFactory.createLineBorder(new Color(252,165,165)));
        btnBatal   = buatTombol("Batal / Reset", new Color(243,244,246), new Color(75,85,99));
        btnSimpan  = buatTombol("Simpan Transaksi", new Color(27,67,50), Color.WHITE);

        btnHapusItem.addActionListener(e -> hapusItem());
        btnBatal.addActionListener(e -> resetForm());
        btnSimpan.addActionListener(e -> simpanTransaksi());

        btnPanel.add(btnHapusItem);
        btnPanel.add(btnBatal);
        btnPanel.add(btnSimpan);

        footer.add(totalPanel, BorderLayout.WEST);
        footer.add(btnPanel,   BorderLayout.EAST);

        kanan.add(lblDaftar, BorderLayout.NORTH);
        kanan.add(scroll,    BorderLayout.CENTER);
        kanan.add(footer,    BorderLayout.SOUTH);

        split.setLeftComponent(kiri);
        split.setRightComponent(kanan);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buatFormRow(String label, JTextField field) {
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

    private JPanel buatFormRowCombo(String label, JComboBox<?> combo) {
        JPanel p = new JPanel(new BorderLayout(0,4));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(27,67,50));
        p.add(lbl, BorderLayout.NORTH);
        p.add(combo, BorderLayout.CENTER);
        return p;
    }

    private JButton buatTombol(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 34));
        return btn;
    }

    private void loadBarang() {
        barangList = barangDAO.getAllBarang();
        cbBarang.removeAllItems();
        for (Barang b : barangList) {
            cbBarang.addItem(b.getKodeBarang() + " - " + b.getNamaBarang());
        }
        onBarangSelected();
    }

    private void onBarangSelected() {
        int idx = cbBarang.getSelectedIndex();
        if (idx < 0 || barangList == null || idx >= barangList.size()) return;
        Barang b = barangList.get(idx);
        String jenis = (String) cbJenis.getSelectedItem();
        double harga = jenis.equals("masuk") ? b.getHargaBeli() : b.getHargaJual();
        txtHarga.setText(String.valueOf((long) harga));
        lblStokInfo.setText("Stok tersedia: " + b.getStokAkhir() + " " + b.getSatuan()
            + "  |  Status: " + b.getStatusStok());
        hitungSubtotal();
    }

    private void hitungSubtotal() {
        try {
            double harga = Double.parseDouble(txtHarga.getText().trim());
            int jumlah   = Integer.parseInt(txtJumlah.getText().trim());
            double sub   = harga * jumlah;
            lblSubtotal.setText("Subtotal: " + FMT.format(sub));
        } catch (NumberFormatException ex) {
            lblSubtotal.setText("Subtotal: -");
        }
    }

    private void tambahItem() {
        int idx = cbBarang.getSelectedIndex();
        if (idx < 0) return;
        Barang b = barangList.get(idx);

        try {
            int jumlah       = Integer.parseInt(txtJumlah.getText().trim());
            double harga     = Double.parseDouble(txtHarga.getText().trim());
            String jenis     = (String) cbJenis.getSelectedItem();

            if (jumlah <= 0) { JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0!"); return; }
            if (jenis.equals("keluar") && jumlah > b.getStokAkhir()) {
                JOptionPane.showMessageDialog(this, "Stok tidak mencukupi!\nStok tersedia: " + b.getStokAkhir());
                return;
            }

            double subtotal = harga * jumlah;
            DetailTransaksi d = new DetailTransaksi();
            d.setIdBarang(b.getIdBarang());
            d.setKodeBarang(b.getKodeBarang());
            d.setNamaBarang(b.getNamaBarang());
            d.setJumlah(jumlah);
            d.setHargaSatuan(harga);
            d.setSubtotal(subtotal);
            detailList.add(d);

            detailModel.addRow(new Object[]{
                b.getKodeBarang(), b.getNamaBarang(), jumlah,
                FMT.format(harga), FMT.format(subtotal)
            });

            grandTotal += subtotal;
            lblTotal.setText("TOTAL: " + FMT.format(grandTotal));
            txtJumlah.setText("1");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka!");
        }
    }

    private void hapusItem() {
        int row = tblDetail.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih item yang ingin dihapus!"); return; }
        grandTotal -= detailList.get(row).getSubtotal();
        detailList.remove(row);
        detailModel.removeRow(row);
        lblTotal.setText("TOTAL: " + FMT.format(grandTotal));
    }

    private void resetForm() {
        detailList.clear();
        detailModel.setRowCount(0);
        grandTotal = 0;
        lblTotal.setText("TOTAL: Rp 0");
        txtJumlah.setText("1");
        txtKeterangan.setText("");
        updateNoTransaksi();
    }

    private void updateNoTransaksi() {
        String jenis = cbJenis.getSelectedItem() != null ? (String)cbJenis.getSelectedItem() : "keluar";
        txtNoTransaksi.setText(transaksiDAO.generateNoTransaksi(jenis));
        onBarangSelected();
    }

    private void simpanTransaksi() {
        if (detailList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tambahkan minimal 1 item transaksi!");
            return;
        }
        Transaksi t = new Transaksi();
        t.setNoTransaksi(txtNoTransaksi.getText());
        t.setJenis((String) cbJenis.getSelectedItem());
        t.setTanggal(new java.util.Date());
        t.setTotalHarga(grandTotal);
        t.setKeterangan(txtKeterangan.getText().trim());
        t.setDetails(detailList);

        boolean ok = transaksiDAO.simpan(t, currentIdUser);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                "Transaksi berhasil disimpan!\nNo: " + t.getNoTransaksi(),
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
            resetForm();
            loadBarang(); // refresh stok
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
