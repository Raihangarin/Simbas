package sembako.dao;

import sembako.Koneksi;
import sembako.model.Kategori;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KategoriDAO {

    public List<Kategori> getAll() {
        List<Kategori> list = new ArrayList<>();
        String sql = "SELECT * FROM kategori ORDER BY nama_kategori";
        try (Connection c = Koneksi.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Kategori k = new Kategori();
                k.setIdKategori(rs.getInt("id_kategori"));
                k.setNamaKategori(rs.getString("nama_kategori"));
                k.setKeterangan(rs.getString("keterangan"));
                list.add(k);
            }
        } catch (SQLException e) { System.err.println("getAll Kategori: " + e.getMessage()); }
        return list;
    }

    public boolean tambah(Kategori k) {
        String sql = "INSERT INTO kategori (nama_kategori, keterangan) VALUES (?,?)";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, k.getNamaKategori());
            ps.setString(2, k.getKeterangan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("tambah Kategori: " + e.getMessage()); return false; }
    }

    public boolean update(Kategori k) {
        String sql = "UPDATE kategori SET nama_kategori=?, keterangan=? WHERE id_kategori=?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, k.getNamaKategori());
            ps.setString(2, k.getKeterangan());
            ps.setInt(3, k.getIdKategori());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("update Kategori: " + e.getMessage()); return false; }
    }

    public boolean hapus(int id) {
        String sql = "DELETE FROM kategori WHERE id_kategori=?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("hapus Kategori: " + e.getMessage()); return false; }
    }
}
