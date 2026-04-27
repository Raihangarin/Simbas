package sembako.model;

import java.util.Date;

public class Barang {
    private int idBarang;
    private String kodeBarang;
    private String namaBarang;
    private int idKategori;
    private String namaKategori;
    private String satuan;
    private double hargaBeli;
    private double hargaJual;
    private int stokAwal;
    private int stokAkhir;
    private Date tanggalMasuk;
    private String keterangan;

    public Barang() {}

    public Barang(int idBarang, String kodeBarang, String namaBarang,
                  int idKategori, String namaKategori, String satuan,
                  double hargaBeli, double hargaJual,
                  int stokAwal, int stokAkhir, Date tanggalMasuk, String keterangan) {
        this.idBarang     = idBarang;
        this.kodeBarang   = kodeBarang;
        this.namaBarang   = namaBarang;
        this.idKategori   = idKategori;
        this.namaKategori = namaKategori;
        this.satuan       = satuan;
        this.hargaBeli    = hargaBeli;
        this.hargaJual    = hargaJual;
        this.stokAwal     = stokAwal;
        this.stokAkhir    = stokAkhir;
        this.tanggalMasuk = tanggalMasuk;
        this.keterangan   = keterangan;
    }

    public int getSelisih() {
        return stokAkhir - stokAwal;
    }

    public String getStatusStok() {
        if (stokAkhir <= 5)  return "Kritis";
        if (stokAkhir <= 15) return "Menipis";
        return "Aman";
    }

    public int getIdBarang()           { return idBarang; }
    public void setIdBarang(int v)     { this.idBarang = v; }

    public String getKodeBarang()      { return kodeBarang; }
    public void setKodeBarang(String v){ this.kodeBarang = v; }

    public String getNamaBarang()      { return namaBarang; }
    public void setNamaBarang(String v){ this.namaBarang = v; }

    public int getIdKategori()         { return idKategori; }
    public void setIdKategori(int v)   { this.idKategori = v; }

    public String getNamaKategori()    { return namaKategori; }
    public void setNamaKategori(String v){ this.namaKategori = v; }

    public String getSatuan()          { return satuan; }
    public void setSatuan(String v)    { this.satuan = v; }

    public double getHargaBeli()       { return hargaBeli; }
    public void setHargaBeli(double v) { this.hargaBeli = v; }

    public double getHargaJual()       { return hargaJual; }
    public void setHargaJual(double v) { this.hargaJual = v; }

    public int getStokAwal()           { return stokAwal; }
    public void setStokAwal(int v)     { this.stokAwal = v; }

    public int getStokAkhir()          { return stokAkhir; }
    public void setStokAkhir(int v)    { this.stokAkhir = v; }

    public Date getTanggalMasuk()      { return tanggalMasuk; }
    public void setTanggalMasuk(Date v){ this.tanggalMasuk = v; }

    public String getKeterangan()      { return keterangan; }
    public void setKeterangan(String v){ this.keterangan = v; }
}
