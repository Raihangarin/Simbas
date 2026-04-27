package sembako.dao;

import sembako.Koneksi;
import sembako.model.DetailTransaksi;
import sembako.model.Transaksi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaksiDAO {

    public String generateNoTransaksi(String jenis) {
        String prefix = jenis.equals("masuk") ? "MSK" : "KLR";
        String today = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        String sql = "SELECT COUNT(*) FROM transaksi WHERE DATE(tanggal)=CURDATE() AND jenis=?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, jenis);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int urut = rs.getInt(1) + 1;
                return prefix + "-" + today + "-" + String.format("%03d", urut);
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return prefix + "-" + today + "-001";
    }

    public boolean simpan(Transaksi t, int idUser) {
        String sqlT = "INSERT INTO transaksi (no_transaksi, jenis, tanggal, total_harga, keterangan, id_user) VALUES (?,?,?,?,?,?)";
        String sqlD = "INSERT INTO detail_transaksi (id_transaksi, id_barang, jumlah, harga_satuan, subtotal) VALUES (?,?,?,?,?)";
        String sqlStokMasuk  = "CALL sp_stok_masuk(?,?,?)";
        String sqlStokKeluar = "CALL sp_stok_keluar(?,?,?)";

        try (Connection c = Koneksi.getConnection()) {
            c.setAutoCommit(false);

            PreparedStatement psT = c.prepareStatement(sqlT, Statement.RETURN_GENERATED_KEYS);
            psT.setString(1, t.getNoTransaksi());
            psT.setString(2, t.getJenis());
            psT.setDate(3, new java.sql.Date(t.getTanggal().getTime()));
            psT.setDouble(4, t.getTotalHarga());
            psT.setString(5, t.getKeterangan());
            psT.setInt(6, idUser);
            psT.executeUpdate();

            ResultSet keys = psT.getGeneratedKeys();
            if (!keys.next()) { c.rollback(); return false; }
            int idTransaksi = keys.getInt(1);

            for (DetailTransaksi d : t.getDetails()) {
                PreparedStatement psD = c.prepareStatement(sqlD);
                psD.setInt(1, idTransaksi);
                psD.setInt(2, d.getIdBarang());
                psD.setInt(3, d.getJumlah());
                psD.setDouble(4, d.getHargaSatuan());
                psD.setDouble(5, d.getSubtotal());
                psD.executeUpdate();

                String sqlStok = t.getJenis().equals("masuk") ? sqlStokMasuk : sqlStokKeluar;
                PreparedStatement psS = c.prepareStatement(sqlStok);
                psS.setInt(1, d.getIdBarang());
                psS.setInt(2, d.getJumlah());
                psS.setString(3, t.getNoTransaksi());
                psS.execute();
            }

            c.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("simpan Transaksi: " + e.getMessage());
            return false;
        }
    }

    public List<Object[]> getAllTransaksiRows() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT t.no_transaksi, t.jenis, t.tanggal, " +
                     "COUNT(dt.id_detail) as jml_item, t.total_harga, t.keterangan " +
                     "FROM transaksi t LEFT JOIN detail_transaksi dt ON t.id_transaksi=dt.id_transaksi " +
                     "GROUP BY t.id_transaksi ORDER BY t.tanggal DESC";
        try (Connection c = Koneksi.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("no_transaksi"),
                    rs.getString("jenis").equals("masuk") ? "Stok Masuk" : "Penjualan",
                    rs.getDate("tanggal").toString(),
                    rs.getInt("jml_item"),
                    rs.getDouble("total_harga"),
                    rs.getString("keterangan")
                });
            }
        } catch (SQLException e) { System.err.println("getAllTransaksi: " + e.getMessage()); }
        return list;
    }

    public double getTotalPenjualanHariIni() {
        String sql = "SELECT IFNULL(SUM(total_harga),0) FROM transaksi WHERE DATE(tanggal)=CURDATE() AND jenis='keluar'";
        try (Connection c = Koneksi.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return 0;
    }

    public double getTotalPemasukanHariIni() {
        String sql = "SELECT IFNULL(SUM(total_harga),0) FROM transaksi WHERE DATE(tanggal)=CURDATE() AND jenis='masuk'";
        try (Connection c = Koneksi.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return 0;
    }

    public int getJumlahTransaksiHariIni() {
        String sql = "SELECT COUNT(*) FROM transaksi WHERE DATE(tanggal)=CURDATE()";
        try (Connection c = Koneksi.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return 0;
    }
}
