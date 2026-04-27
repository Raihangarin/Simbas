package sembako.view;

import sembako.dao.BarangDAO;
import sembako.model.Barang;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DashboardForm extends JFrame {

    private JTable tabelBarang;
    private DefaultTableModel tableModel;
    private JTextField txtCari;
    private JComboBox<String> cbFilterKategori;
    private JLabel lblTotalBarang, lblStokMenipis, lblSelisih;

    private final BarangDAO barangDAO = new BarangDAO();
    private final String namaPemilik;
    private int idUser;
    private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance(new Locale("id","ID"));

    private TransaksiForm  transaksiForm;
    private LaporanForm    laporanForm;
    private HargaForm      hargaForm;
    private KategoriForm   kategoriForm;
    private PengaturanForm pengaturanForm;

    private JPanel    contentArea;
    private CardLayout cardLayout;
    private JPanel[]  sidebarItems;
    private String    activeMenu = "stok";

    public DashboardForm(String namaPemilik, int idUser) {
        this.namaPemilik = namaPemilik;
        this.idUser      = idUser;
        setTitle("Simbas - Dashboard Manajemen Stok");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        refreshData();
    }

    public DashboardForm(String namaPemilik) { this(namaPemilik, 1); }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240,244,240));

        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(new Color(27,67,50));
        topbar.setPreferredSize(new Dimension(0,56));
        topbar.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));
        JLabel lblBrand = new JLabel("\uD83C\uDF3E  Simbas");
        lblBrand.setFont(new Font("Georgia", Font.BOLD, 18));
        lblBrand.setForeground(Color.WHITE);
        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));
        topRight.setOpaque(false);
        JLabel lblUser = new JLabel("\uD83D\uDC64  " + namaPemilik);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUser.setForeground(Color.WHITE);
        JButton btnLogout = new JButton("\u23FB  Keluar");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnLogout.setForeground(new Color(252,165,165));
        btnLogout.setBackground(new Color(100,30,30));
        btnLogout.setBorderPainted(false); btnLogout.setFocusPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            int opt = JOptionPane.showConfirmDialog(this,"Yakin ingin keluar?","Logout",JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) { new LoginForm().setVisible(true); dispose(); }
        });
        topRight.add(lblUser); topRight.add(btnLogout);
        topbar.add(lblBrand, BorderLayout.WEST);
        topbar.add(topRight, BorderLayout.EAST);

        String[][] menus = {
            {"\uD83D\uDCE6","Stok Barang","stok"},
            {"\uD83D\uDED2","Transaksi","transaksi"},
            {"\uD83D\uDCC8","Laporan","laporan"},
            {"\uD83C\uDFF7\uFE0F","Harga","harga"},
            {"\uD83D\uDCC2","Kategori","kategori"},
            {"\uD83D\uDD27","Pengaturan","pengaturan"}
        };
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(185,0));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(BorderFactory.createMatteBorder(0,0,0,1,new Color(229,231,235)));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.add(Box.createVerticalStrut(14));

        sidebarItems = new JPanel[menus.length];
        for (int i = 0; i < menus.length; i++) {
            final String key = menus[i][2];
            final int    idx = i;
            JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT,12,0));
            item.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
            item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            item.setOpaque(true);
            boolean first = key.equals("stok");
            item.setBackground(first ? new Color(240,251,244) : Color.WHITE);
            item.setBorder(BorderFactory.createMatteBorder(0,first?3:0,0,0,
                first ? new Color(45,106,79) : Color.WHITE));
            JLabel lbl = new JLabel(menus[i][0] + "  " + menus[i][1]);
            lbl.setFont(new Font("Segoe UI", first ? Font.BOLD : Font.PLAIN, 13));
            lbl.setForeground(first ? new Color(27,67,50) : new Color(75,85,99));
            item.add(lbl);
            item.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) { switchMenu(key, idx, menus); }
                public void mouseEntered(MouseEvent e) { if (!key.equals(activeMenu)) item.setBackground(new Color(249,250,251)); }
                public void mouseExited(MouseEvent e)  { if (!key.equals(activeMenu)) item.setBackground(Color.WHITE); }
            });
            sidebarItems[i] = item;
            sidebar.add(item);
            sidebar.add(Box.createVerticalStrut(2));
        }

        cardLayout   = new CardLayout();
        contentArea  = new JPanel(cardLayout);
        contentArea.setBackground(new Color(240,244,240));

        transaksiForm  = new TransaksiForm(idUser);
        laporanForm    = new LaporanForm();
        hargaForm      = new HargaForm();
        kategoriForm   = new KategoriForm();
        pengaturanForm = new PengaturanForm(idUser, namaPemilik);

        contentArea.add(buatStokPanel(),  "stok");
        contentArea.add(transaksiForm,    "transaksi");
        contentArea.add(laporanForm,      "laporan");
        contentArea.add(hargaForm,        "harga");
        contentArea.add(kategoriForm,     "kategori");
        contentArea.add(pengaturanForm,   "pengaturan");

        JPanel body = new JPanel(new BorderLayout());
        body.add(sidebar,     BorderLayout.WEST);
        body.add(contentArea, BorderLayout.CENTER);

        add(topbar, BorderLayout.NORTH);
        add(body,   BorderLayout.CENTER);
    }

    private void switchMenu(String key, int idx, String[][] menus) {
        activeMenu = key;
        cardLayout.show(contentArea, key);
        if (key.equals("laporan")) laporanForm.loadData();
        if (key.equals("harga"))   hargaForm.loadData();
        if (key.equals("stok"))    refreshData();
        for (int i = 0; i < sidebarItems.length; i++) {
            boolean aktif = menus[i][2].equals(key);
            sidebarItems[i].setBackground(aktif ? new Color(240,251,244) : Color.WHITE);
            sidebarItems[i].setBorder(BorderFactory.createMatteBorder(0,aktif?3:0,0,0,
                aktif ? new Color(45,106,79) : Color.WHITE));
            JLabel l = (JLabel) sidebarItems[i].getComponent(0);
            l.setFont(new Font("Segoe UI", aktif ? Font.BOLD : Font.PLAIN, 13));
            l.setForeground(aktif ? new Color(27,67,50) : new Color(75,85,99));
        }
    }

    private JPanel buatStokPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240,244,240));
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(240,244,240));
        main.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JPanel statRow = new JPanel(new GridLayout(1,4,14,0));
        statRow.setOpaque(false);
        lblTotalBarang = new JLabel("0"); lblStokMenipis = new JLabel("0"); lblSelisih = new JLabel("0");
        statRow.add(buildStat("Total Jenis Barang", lblTotalBarang, "5 kategori", new Color(27,67,50), Color.WHITE));
        statRow.add(buildStat("Stok Menipis",       lblStokMenipis, "Perlu restock", Color.WHITE, new Color(217,119,6)));
        statRow.add(buildStat("Transaksi Hari Ini", new JLabel("Rp 2,4 Jt"), "Real-time", Color.WHITE, new Color(27,67,50)));
        statRow.add(buildStat("Selisih Stok",       lblSelisih, "Item perlu dicek", Color.WHITE, new Color(220,38,38)));

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);
        toolbar.setBorder(BorderFactory.createEmptyBorder(16,0,10,0));
        JLabel lblJ = new JLabel("Data Stok Barang Sembako");
        lblJ.setFont(new Font("Georgia", Font.BOLD, 18));
        lblJ.setForeground(new Color(27,67,50));

        JPanel tr = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        tr.setOpaque(false);
        cbFilterKategori = new JComboBox<>(new String[]{"Semua","Beras","Minyak","Gula","Garam","Tepung","Lainnya"});
        cbFilterKategori.setPreferredSize(new Dimension(120,32));
        cbFilterKategori.addActionListener(e -> filterData());
        txtCari = new JTextField();
        txtCari.setPreferredSize(new Dimension(180,32));
        txtCari.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209,213,219)),
            BorderFactory.createEmptyBorder(4,8,4,8)));
        txtCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filterData(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filterData(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });
        JButton btnT = miniBtn("+ Tambah", new Color(27,67,50), Color.WHITE);
        btnT.addActionListener(e -> { new FormBarang(this, null).setVisible(true); refreshData(); });
        JButton btnR = miniBtn("Refresh",  new Color(249,246,227), new Color(27,67,50));
        btnR.addActionListener(e -> refreshData());
        tr.add(new JLabel("Kategori:")); tr.add(cbFilterKategori);
        tr.add(new JLabel("Cari:")); tr.add(txtCari); tr.add(btnR); tr.add(btnT);
        toolbar.add(lblJ, BorderLayout.WEST); toolbar.add(tr, BorderLayout.EAST);

        String[] kol = {"No","Kode Barang","Nama Barang","Kategori","Stok Awal","Stok Akhir","Selisih","Satuan","Tgl Masuk","Harga Beli","Harga Jual","Status","Aksi"};
        tableModel = new DefaultTableModel(kol, 0) { public boolean isCellEditable(int r,int c){return c==12;} };
        tabelBarang = new JTable(tableModel);
        tabelBarang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelBarang.setRowHeight(34);
        tabelBarang.setGridColor(new Color(243,244,246));
        tabelBarang.setSelectionBackground(new Color(220,252,231));
        JTableHeader h = tabelBarang.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 11));
        h.setBackground(new Color(248,250,248));
        h.setPreferredSize(new Dimension(0,36));
        tabelBarang.getColumnModel().getColumn(6).setCellRenderer(new SelisihRenderer());
        tabelBarang.getColumnModel().getColumn(11).setCellRenderer(new StatusRenderer());
        tabelBarang.getColumnModel().getColumn(12).setCellRenderer(new ButtonRenderer());
        tabelBarang.getColumnModel().getColumn(12).setCellEditor(new ButtonEditor(tabelBarang));
        int[] ws = {35,90,160,90,75,75,65,65,90,110,110,75,120};
        for (int i=0;i<ws.length;i++) tabelBarang.getColumnModel().getColumn(i).setPreferredWidth(ws[i]);

        JScrollPane sp = new JScrollPane(tabelBarang);
        sp.setBorder(BorderFactory.createLineBorder(new Color(229,231,235)));
        sp.getViewport().setBackground(Color.WHITE);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(statRow, BorderLayout.NORTH);
        wrap.add(toolbar, BorderLayout.CENTER);
        JPanel tw = new JPanel(new BorderLayout());
        tw.setBackground(Color.WHITE);
        tw.setBorder(BorderFactory.createLineBorder(new Color(229,231,235)));
        tw.add(sp, BorderLayout.CENTER);
        main.add(wrap, BorderLayout.NORTH);
        main.add(tw,   BorderLayout.CENTER);
        panel.add(main, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildStat(String lbl, JLabel val, String sub, Color bg, Color valFg) {
        JPanel c = new JPanel();
        c.setBackground(bg);
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229,231,235)),
            BorderFactory.createEmptyBorder(14,18,14,18)));
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        JLabel l = new JLabel(lbl.toUpperCase());
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(bg.equals(new Color(27,67,50)) ? new Color(180,220,200) : new Color(107,114,128));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        val.setFont(new Font("Georgia", Font.BOLD, 24));
        val.setForeground(valFg);
        val.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel s = new JLabel(sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s.setForeground(bg.equals(new Color(27,67,50)) ? new Color(150,200,180) : new Color(156,163,175));
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.add(l); c.add(Box.createVerticalStrut(6));
        c.add(val); c.add(Box.createVerticalStrut(3)); c.add(s);
        return c;
    }

    private JButton miniBtn(String t, Color bg, Color fg) {
        JButton b = new JButton(t);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBackground(bg); b.setForeground(fg);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0,32));
        return b;
    }

    public void loadData(List<Barang> list) {
        tableModel.setRowCount(0);
        int no = 1;
        for (Barang b : list) {
            String sel = (b.getSelisih()>=0?"+":"") + b.getSelisih();
            tableModel.addRow(new Object[]{no++, b.getKodeBarang(), b.getNamaBarang(), b.getNamaKategori(),
                b.getStokAwal(), b.getStokAkhir(), sel, b.getSatuan(),
                b.getTanggalMasuk()!=null ? b.getTanggalMasuk().toString() : "-",
                CURRENCY.format(b.getHargaBeli()), CURRENCY.format(b.getHargaJual()),
                b.getStatusStok(), b.getIdBarang()});
        }
    }

    private void filterData() {
        String kw  = txtCari.getText().trim();
        String kat = (String) cbFilterKategori.getSelectedItem();
        List<Barang> l = kw.isEmpty() ? barangDAO.getAllBarang() : barangDAO.cariBarang(kw);
        if (kat!=null && !kat.equals("Semua")) l.removeIf(b -> !b.getNamaKategori().equalsIgnoreCase(kat));
        loadData(l); updateStatistik();
    }

    public void refreshData() { loadData(barangDAO.getAllBarang()); updateStatistik(); }

    private void updateStatistik() {
        List<Barang> all = barangDAO.getAllBarang();
        lblTotalBarang.setText(String.valueOf(all.size()));
        lblStokMenipis.setText(String.valueOf(all.stream().filter(b->!b.getStatusStok().equals("Aman")).count()));
        lblSelisih.setText(String.valueOf(-(int)all.stream().filter(b->b.getSelisih()<0).count()));
    }

    static class SelisihRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){
            super.getTableCellRendererComponent(t,v,s,f,r,c);
            setHorizontalAlignment(JLabel.CENTER); setFont(new Font("Segoe UI",Font.BOLD,12));
            String str=v!=null?v.toString():"0";
            if(str.startsWith("+")) setForeground(new Color(5,150,105));
            else if(str.startsWith("-")) setForeground(new Color(220,38,38));
            else setForeground(new Color(107,114,128));
            return this;
        }
    }
    static class StatusRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){
            super.getTableCellRendererComponent(t,v,s,f,r,c);
            setHorizontalAlignment(JLabel.CENTER); setFont(new Font("Segoe UI",Font.BOLD,11));
            String str=v!=null?v.toString():"";
            switch(str){
                case "Aman":    setForeground(new Color(5,150,105));  break;
                case "Menipis": setForeground(new Color(217,119,6));  break;
                case "Kritis":  setForeground(new Color(220,38,38));  break;
                default:        setForeground(Color.BLACK);
            }
            return this;
        }
    }
    static class ButtonRenderer extends JPanel implements TableCellRenderer {
        ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER,4,4)); setOpaque(true);
            JButton e=new JButton("Edit"); e.setFont(new Font("Segoe UI",Font.BOLD,10));
            e.setBackground(new Color(239,246,255)); e.setForeground(new Color(37,99,235)); e.setFocusPainted(false);
            JButton d=new JButton("Hapus"); d.setFont(new Font("Segoe UI",Font.BOLD,10));
            d.setBackground(new Color(254,242,242)); d.setForeground(new Color(220,38,38)); d.setFocusPainted(false);
            add(e); add(d);
        }
        public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){
            setBackground(s?new Color(220,252,231):Color.WHITE); return this;
        }
    }
    class ButtonEditor extends DefaultCellEditor {
        private final JPanel  panel = new JPanel(new FlowLayout(FlowLayout.CENTER,4,4));
        private final JButton edit  = new JButton("Edit");
        private final JButton del   = new JButton("Hapus");
        private int cid;
        ButtonEditor(JTable t) {
            super(new JCheckBox()); panel.setBackground(Color.WHITE);
            edit.setFont(new Font("Segoe UI",Font.BOLD,10)); edit.setBackground(new Color(239,246,255)); edit.setForeground(new Color(37,99,235)); edit.setFocusPainted(false);
            del.setFont(new Font("Segoe UI",Font.BOLD,10));  del.setBackground(new Color(254,242,242));  del.setForeground(new Color(220,38,38));  del.setFocusPainted(false);
            panel.add(edit); panel.add(del);
            edit.addActionListener(e->{fireEditingStopped(); final int id=cid; barangDAO.getAllBarang().stream().filter(b->b.getIdBarang()==id).findFirst().ifPresent(b->{new FormBarang(DashboardForm.this,b).setVisible(true); refreshData();});});
            del.addActionListener(e->{fireEditingStopped(); if(JOptionPane.showConfirmDialog(DashboardForm.this,"Hapus barang ini?","Konfirmasi",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){if(barangDAO.hapusBarang(cid)){JOptionPane.showMessageDialog(DashboardForm.this,"Berhasil dihapus!"); refreshData();}else JOptionPane.showMessageDialog(DashboardForm.this,"Gagal menghapus!","Error",JOptionPane.ERROR_MESSAGE);}});
        }
        public Component getTableCellEditorComponent(JTable t,Object v,boolean s,int r,int c){cid=(int)t.getValueAt(r,12); return panel;}
        public Object getCellEditorValue(){return cid;}
    }
}
