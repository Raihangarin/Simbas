package sembako.view;

import sembako.dao.BarangDAO;
import sembako.dao.TransaksiDAO;
import sembako.model.Barang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class LaporanForm extends JPanel {

    private final TransaksiDAO transaksiDAO = new TransaksiDAO();
    private final BarangDAO    barangDAO    = new BarangDAO();
    private static final NumberFormat FMT = NumberFormat.getCurrencyInstance(new Locale("id","ID"));

    private JTable         tblTransaksi, tblStokRendah;
    private DefaultTableModel tblModelTrx, tblModelStok;
    private JLabel         lblPenjualanHariIni, lblPemasukanHariIni, lblJumlahTrx;

    public LaporanForm() {
        setLayout(new BorderLayout(0,0));
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
        JLabel lblJudul = new JLabel("Laporan & Statistik");
        lblJudul.setFont(new Font("Georgia", Font.BOLD, 20));
        lblJudul.setForeground(new Color(27,67,50));

        JButton btnRefresh = new JButton("↻ Refresh");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRefresh.setBackground(new Color(240,244,240));
        btnRefresh.setForeground(new Color(27,67,50));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadData());

        header.add(lblJudul,   BorderLayout.WEST);
        header.add(btnRefresh, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(240,244,240));
        content.setBorder(BorderFactory.createEmptyBorder(16,20,20,20));

        JPanel statRow = new JPanel(new GridLayout(1,3,14,0));
        statRow.setOpaque(false);
        statRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        statRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblPenjualanHariIni  = new JLabel("Rp 0");
        lblPemasukanHariIni  = new JLabel("Rp 0");
        lblJumlahTrx         = new JLabel("0");

        statRow.add(buatKartu("Penjualan Hari Ini", lblPenjualanHariIni, "Total keluar hari ini",
            new Color(27,67,50), Color.WHITE, new Color(82,183,136)));
        statRow.add(buatKartu("Stok Masuk Hari Ini", lblPemasukanHariIni, "Total pembelian stok",
            Color.WHITE, new Color(27,67,50), new Color(229,231,235)));
        statRow.add(buatKartu("Jumlah Transaksi", lblJumlahTrx, "Transaksi hari ini",
            Color.WHITE, new Color(37,99,235), new Color(229,231,235)));

        content.add(statRow);
        content.add(Box.createVerticalStrut(20));

        JLabel lblTrx = new JLabel("Riwayat Semua Transaksi");
        lblTrx.setFont(new Font("Georgia", Font.BOLD, 16));
        lblTrx.setForeground(new Color(27,67,50));
        lblTrx.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblTrx);
        content.add(Box.createVerticalStrut(8));

        String[] kolTrx = {"No. Transaksi","Jenis","Tanggal","Jml Item","Total Harga","Keterangan"};
        tblModelTrx = new DefaultTableModel(kolTrx, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblTransaksi = new JTable(tblModelTrx);
        tblTransaksi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblTransaksi.setRowHeight(30);
        tblTransaksi.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblTransaksi.getTableHeader().setBackground(new Color(248,250,248));
        tblTransaksi.setGridColor(new Color(243,244,246));
        tblTransaksi.getColumnModel().getColumn(1).setCellRenderer(new JenisRenderer());

        JScrollPane scrollTrx = new JScrollPane(tblTransaksi);
        scrollTrx.setPreferredSize(new Dimension(0, 220));
        scrollTrx.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        scrollTrx.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollTrx.setBorder(BorderFactory.createLineBorder(new Color(229,231,235)));

        content.add(scrollTrx);
        content.add(Box.createVerticalStrut(20));

        JLabel lblStok = new JLabel("Peringatan Stok Menipis / Kritis");
        lblStok.setFont(new Font("Georgia", Font.BOLD, 16));
        lblStok.setForeground(new Color(27,67,50));
        lblStok.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblStok);
        content.add(Box.createVerticalStrut(8));

        String[] kolStok = {"Kode","Nama Barang","Kategori","Stok Akhir","Satuan","Status"};
        tblModelStok = new DefaultTableModel(kolStok, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblStokRendah = new JTable(tblModelStok);
        tblStokRendah.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblStokRendah.setRowHeight(30);
        tblStokRendah.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblStokRendah.getTableHeader().setBackground(new Color(248,250,248));
        tblStokRendah.setGridColor(new Color(243,244,246));

        JScrollPane scrollStok = new JScrollPane(tblStokRendah);
        scrollStok.setPreferredSize(new Dimension(0, 160));
        scrollStok.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        scrollStok.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollStok.setBorder(BorderFactory.createLineBorder(new Color(229,231,235)));

        content.add(scrollStok);

        JScrollPane mainScroll = new JScrollPane(content);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll, BorderLayout.CENTER);
    }

    private JPanel buatKartu(String label, JLabel valueLabel, String sub, Color bg, Color valueFg, Color border) {
        JPanel card = new JPanel();
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(border),
            BorderFactory.createEmptyBorder(14,18,14,18)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(bg.equals(new Color(27,67,50)) ? new Color(200,230,210) : new Color(107,114,128));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        valueLabel.setForeground(valueFg);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subLbl = new JLabel(sub);
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        subLbl.setForeground(bg.equals(new Color(27,67,50)) ? new Color(150,210,180) : new Color(156,163,175));
        subLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lbl);
        card.add(Box.createVerticalStrut(6));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(3));
        card.add(subLbl);
        return card;
    }

    public void loadData() {
        double penjualan = transaksiDAO.getTotalPenjualanHariIni();
        double pemasukan = transaksiDAO.getTotalPemasukanHariIni();
        int jumlahTrx    = transaksiDAO.getJumlahTransaksiHariIni();
        lblPenjualanHariIni.setText(FMT.format(penjualan));
        lblPemasukanHariIni.setText(FMT.format(pemasukan));
        lblJumlahTrx.setText(String.valueOf(jumlahTrx));

        tblModelTrx.setRowCount(0);
        for (Object[] row : transaksiDAO.getAllTransaksiRows()) {
            row[4] = FMT.format((double) row[4]);
            tblModelTrx.addRow(row);
        }

        tblModelStok.setRowCount(0);
        List<Barang> all = barangDAO.getAllBarang();
        for (Barang b : all) {
            if (b.getStatusStok().equals("Menipis") || b.getStatusStok().equals("Kritis")) {
                tblModelStok.addRow(new Object[]{
                    b.getKodeBarang(), b.getNamaBarang(), b.getNamaKategori(),
                    b.getStokAkhir(), b.getSatuan(), b.getStatusStok()
                });
            }
        }
    }

    static class JenisRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, v, sel, foc, r, c);
            setHorizontalAlignment(JLabel.CENTER);
            String s = v != null ? v.toString() : "";
            if (s.equals("Stok Masuk")) {
                setForeground(new Color(5,150,105));
                setFont(new Font("Segoe UI", Font.BOLD, 11));
            } else {
                setForeground(new Color(37,99,235));
                setFont(new Font("Segoe UI", Font.BOLD, 11));
            }
            return this;
        }
    }
}
