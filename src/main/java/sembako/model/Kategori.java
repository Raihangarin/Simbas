package sembako.model;

public class Kategori {
    private int idKategori;
    private String namaKategori;
    private String keterangan;

    public Kategori() {}

    public Kategori(int idKategori, String namaKategori, String keterangan) {
        this.idKategori   = idKategori;
        this.namaKategori = namaKategori;
        this.keterangan   = keterangan;
    }

    public int    getIdKategori()             { return idKategori; }
    public void   setIdKategori(int v)        { this.idKategori = v; }
    public String getNamaKategori()           { return namaKategori; }
    public void   setNamaKategori(String v)   { this.namaKategori = v; }
    public String getKeterangan()             { return keterangan; }
    public void   setKeterangan(String v)     { this.keterangan = v; }

    @Override public String toString() { return namaKategori; }
}
