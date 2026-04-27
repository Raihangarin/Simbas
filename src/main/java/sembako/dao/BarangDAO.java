package sembako.dao;

import sembako.Koneksi;
import sembako.model.Barang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BarangDAO extends BaseDAO<Barang> {
    public BarangDAO() {
        super();
    }

    @Override
    public List<Barang> getAll() {
        return getAllBarang();
    }

    public List<Barang> getAllBarang() {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT * FROM v_stok_barang";
        try (Connection conn = Koneksi.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Barang b = new Barang();
                b.setIdBarang(rs.getInt("id_barang"));
                b.setKodeBarang(rs.getString("kode_barang"));
                b.setNamaBarang(rs.getString("nama_barang"));
                b.setNamaKategori(rs.getString("kategori"));
                b.setSatuan(rs.getString("satuan"));
                b.setStokAwal(rs.getInt("stok_awal"));
                b.setStokAkhir(rs.getInt("stok_akhir"));
                b.setTanggalMasuk(rs.getDate("tanggal_masuk"));
                b.setHargaBeli(rs.getDouble("harga_beli"));
                b.setHargaJual(rs.getDouble("harga_jual"));
                list.add(b);
            }
        } catch (SQLException e) {
            logError("getAllBarang", e.getMessage());
        }
        return list;
    }

    @Override
    public List<Barang> search(String keyword) {
        return cariBarang(keyword);
    }

    public List<Barang> cariBarang(String keyword) {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT * FROM v_stok_barang WHERE kode_barang LIKE ? OR nama_barang LIKE ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Barang b = new Barang();
                b.setIdBarang(rs.getInt("id_barang"));
                b.setKodeBarang(rs.getString("kode_barang"));
                b.setNamaBarang(rs.getString("nama_barang"));
                b.setNamaKategori(rs.getString("kategori"));
                b.setSatuan(rs.getString("satuan"));
                b.setStokAwal(rs.getInt("stok_awal"));
                b.setStokAkhir(rs.getInt("stok_akhir"));
                b.setTanggalMasuk(rs.getDate("tanggal_masuk"));
                b.setHargaBeli(rs.getDouble("harga_beli"));
                b.setHargaJual(rs.getDouble("harga_jual"));
                list.add(b);
            }
        } catch (SQLException e) {
            logError("cariBarang", e.getMessage());
        }
        return list;
    }

    @Override
    public boolean create(Barang barang) {
        return tambahBarang(barang);
    }

    public boolean tambahBarang(Barang b) {
        String sql = "INSERT INTO barang (kode_barang, nama_barang, id_kategori, satuan, " +
                     "harga_beli, harga_jual, stok_awal, stok_akhir, tanggal_masuk, keterangan) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, b.getKodeBarang());
            ps.setString(2, b.getNamaBarang());
            ps.setInt(3, b.getIdKategori());
            ps.setString(4, b.getSatuan());
            ps.setDouble(5, b.getHargaBeli());
            ps.setDouble(6, b.getHargaJual());
            ps.setInt(7, b.getStokAwal());
            ps.setInt(8, b.getStokAkhir());
            ps.setDate(9, new java.sql.Date(b.getTanggalMasuk().getTime()));
            ps.setString(10, b.getKeterangan());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logError("tambahBarang", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Barang barang) {
        return updateBarang(barang);
    }

    public boolean updateBarang(Barang b) {
        String sql = "UPDATE barang SET nama_barang=?, id_kategori=?, satuan=?, " +
                     "harga_beli=?, harga_jual=?, stok_awal=?, stok_akhir=?, " +
                     "tanggal_masuk=?, keterangan=? WHERE id_barang=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, b.getNamaBarang());
            ps.setInt(2, b.getIdKategori());
            ps.setString(3, b.getSatuan());
            ps.setDouble(4, b.getHargaBeli());
            ps.setDouble(5, b.getHargaJual());
            ps.setInt(6, b.getStokAwal());
            ps.setInt(7, b.getStokAkhir());
            ps.setDate(8, new java.sql.Date(b.getTanggalMasuk().getTime()));
            ps.setString(9, b.getKeterangan());
            ps.setInt(10, b.getIdBarang());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logError("updateBarang", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int idBarang) {
        return hapusBarang(idBarang);
    }

    public boolean hapusBarang(int idBarang) {
        String sql = "DELETE FROM barang WHERE id_barang=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idBarang);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logError("hapusBarang", e.getMessage());
            return false;
        }
    }

    public List<String[]> getAllKategori() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT id_kategori, nama_kategori FROM kategori ORDER BY nama_kategori";
        try (Connection conn = Koneksi.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("id_kategori"),
                    rs.getString("nama_kategori")
                });
            }
        } catch (SQLException e) {
            logError("getAllKategori", e.getMessage());
        }
        return list;
    }

    public boolean isKodeExist(String kode) {
        String sql = "SELECT COUNT(*) FROM barang WHERE kode_barang=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            logError("isKodeExist", e.getMessage());
        }
        return false;
    }
}
