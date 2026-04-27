package sembako.view;

import sembako.dao.BarangDAO;
import sembako.Koneksi;
import sembako.model.Barang;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HargaForm extends JPanel {

    private final BarangDAO dao = new BarangDAO();
    private static final NumberFormat FMT = NumberFormat.getCurrencyInstance(new Locale("id","ID"));

    private JTable tbl;
    private DefaultTableModel model;
    private JTextField txtHargaBeli, txtHargaJual;
    private JLabel lblNamaBarang, lblKodeBarang;
    private JButton btnUpdate;
    private int selectedIdBarang = -1;

    public HargaForm() {
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
        JLabel judul = new JLabel("Manajemen Harga Barang");
        judul.setFont(new Font("Georgia", Font.BOLD, 20));
        judul.setForeground(new Color(27,67,50));
        JLabel sub = new JLabel("Klik baris barang untuk mengubah harga");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(107,114,128));
        JPanel hdrLeft = new JPanel();
        hdrLeft.setOpaque(false);
        hdrLeft.setLayout(new BoxLayout(hdrLeft, BoxLayout.Y_AXIS));
        hdrLeft.add(judul); hdrLeft.add(sub);
        header.add(hdrLeft, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(320); split.setBorder(null); split.setDividerSize(4);

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        editPanel.setBackground(Color.WHITE);
        editPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,16));

        JLabel lblJudul2 = new JLabel("Update Harga");
        lblJudul2.setFont(new Font("Georgia", Font.BOLD, 15));
        lblJudul2.setForeground(new Color(27,67,50));
        lblJudul2.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblKodeBarang = new JLabel("— Belum dipilih —");
        lblKodeBarang.setFont(new Font("Courier New", Font.BOLD, 12));
        lblKodeBarang.setForeground(new Color(37,99,235));
        lblKodeBarang.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblNamaBarang = new JLabel(" ");
        lblNamaBarang.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblNamaBarang.setForeground(new Color(55,65,81));
        lblNamaBarang.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtHargaBeli = buatField();
        txtHargaJual = buatField();

        btnUpdate = new JButton("✎  Update Harga");
        btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnUpdate.setBackground(new Color(27,67,50));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        btnUpdate.setBorderPainted(false);
        btnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnUpdate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnUpdate.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnUpdate.setEnabled(false);
        btnUpdate.addActionListener(e -> updateHarga());

        JPanel infoMargin = new JPanel();
        infoMargin.setOpaque(false);
        infoMargin.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoMargin.setLayout(new BoxLayout(infoMargin, BoxLayout.X_AXIS));
        JLabel lblMarginInfo = new JLabel("Margin keuntungan akan dihitung otomatis");
        lblMarginInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMarginInfo.setForeground(new Color(107,114,128));
        infoMargin.add(lblMarginInfo);

        editPanel.add(lblJudul2);
        editPanel.add(Box.createVerticalStrut(16));
        editPanel.add(lblKodeBarang);
        editPanel.add(Box.createVerticalStrut(4));
        editPanel.add(lblNamaBarang);
        editPanel.add(Box.createVerticalStrut(16));
        editPanel.add(buatRow("Harga Beli Baru (Rp)", txtHargaBeli));
        editPanel.add(Box.createVerticalStrut(12));
        editPanel.add(buatRow("Harga Jual Baru (Rp)", txtHargaJual));
        editPanel.add(Box.createVerticalStrut(6));
        editPanel.add(infoMargin);
        editPanel.add(Box.createVerticalStrut(20));
        editPanel.add(btnUpdate);

        JPanel tblPanel = new JPanel(new BorderLayout());
        tblPanel.setBackground(new Color(248,250,248));
        tblPanel.setBorder(BorderFactory.createEmptyBorder(16,16,16,20));

        JLabel lblTbl = new JLabel("Daftar Harga Semua Barang");
        lblTbl.setFont(new Font("Georgia", Font.BOLD, 15));
        lblTbl.setForeground(new Color(27,67,50));
        lblTbl.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        String[] kol = {"Kode","Nama Barang","Kategori","Satuan","Harga Beli","Harga Jual","Margin"};
        model = new DefaultTableModel(kol, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tbl = new JTable(model);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tbl.setRowHeight(30);
        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tbl.getTableHeader().setBackground(new Color(248,250,248));
        tbl.setGridColor(new Color(229,231,235));
        tbl.getColumnModel().getColumn(6).setCellRenderer(new MarginRenderer());

        tbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) isiFormDariTabel();
        });

        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(229,231,235)));

        tblPanel.add(lblTbl, BorderLayout.NORTH);
        tblPanel.add(scroll, BorderLayout.CENTER);

        split.setLeftComponent(editPanel);
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

    public void loadData() {
        model.setRowCount(0);
        for (Barang b : dao.getAllBarang()) {
            double margin = b.getHargaBeli() > 0
                ? ((b.getHargaJual() - b.getHargaBeli()) / b.getHargaBeli()) * 100
                : 0;
            model.addRow(new Object[]{
                b.getKodeBarang(), b.getNamaBarang(), b.getNamaKategori(), b.getSatuan(),
                FMT.format(b.getHargaBeli()), FMT.format(b.getHargaJual()),
                String.format("%.1f%%", margin)
            });
        }
    }

    private void isiFormDariTabel() {
        int row = tbl.getSelectedRow();
        if (row < 0) return;
        List<Barang> list = dao.getAllBarang();
        if (row >= list.size()) return;
        Barang b = list.get(row);
        selectedIdBarang = b.getIdBarang();
        lblKodeBarang.setText(b.getKodeBarang());
        lblNamaBarang.setText(b.getNamaBarang() + " (" + b.getSatuan() + ")");
        txtHargaBeli.setText(String.valueOf((long) b.getHargaBeli()));
        txtHargaJual.setText(String.valueOf((long) b.getHargaJual()));
        btnUpdate.setEnabled(true);
    }

    private void updateHarga() {
        if (selectedIdBarang < 0) return;
        try {
            double beli = Double.parseDouble(txtHargaBeli.getText().trim().replace(",",""));
            double jual = Double.parseDouble(txtHargaJual.getText().trim().replace(",",""));
            if (jual < beli) {
                int opt = JOptionPane.showConfirmDialog(this,
                    "Harga jual lebih kecil dari harga beli — akan rugi!\nLanjutkan?", "Peringatan",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (opt != JOptionPane.YES_OPTION) return;
            }
            String sql = "UPDATE barang SET harga_beli=?, harga_jual=? WHERE id_barang=?";
            try (Connection c = Koneksi.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setDouble(1, beli);
                ps.setDouble(2, jual);
                ps.setInt(3, selectedIdBarang);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Harga berhasil diperbarui!");
                    loadData();
                    selectedIdBarang = -1;
                    lblKodeBarang.setText("— Belum dipilih —");
                    lblNamaBarang.setText(" ");
                    txtHargaBeli.setText(""); txtHargaJual.setText("");
                    btnUpdate.setEnabled(false);
                    tbl.clearSelection();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Format harga tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static class MarginRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, v, sel, foc, r, c);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            String s = v != null ? v.toString() : "0%";
            try {
                double val = Double.parseDouble(s.replace("%",""));
                if (val >= 20) setForeground(new Color(5,150,105));
                else if (val >= 10) setForeground(new Color(217,119,6));
                else setForeground(new Color(220,38,38));
            } catch (Exception e) { setForeground(Color.BLACK); }
            return this;
        }
    }
}
