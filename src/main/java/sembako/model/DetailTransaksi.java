package sembako.model;

public class DetailTransaksi {
    private int    idDetail;
    private int    idTransaksi;
    private int    idBarang;
    private String namaBarang;
    private String kodeBarang;
    private int    jumlah;
    private double hargaSatuan;
    private double subtotal;

    public DetailTransaksi() {}

    public int    getIdDetail()           { return idDetail; }
    public void   setIdDetail(int v)      { this.idDetail = v; }
    public int    getIdTransaksi()        { return idTransaksi; }
    public void   setIdTransaksi(int v)   { this.idTransaksi = v; }
    public int    getIdBarang()           { return idBarang; }
    public void   setIdBarang(int v)      { this.idBarang = v; }
    public String getNamaBarang()         { return namaBarang; }
    public void   setNamaBarang(String v) { this.namaBarang = v; }
    public String getKodeBarang()         { return kodeBarang; }
    public void   setKodeBarang(String v) { this.kodeBarang = v; }
    public int    getJumlah()             { return jumlah; }
    public void   setJumlah(int v)        { this.jumlah = v; }
    public double getHargaSatuan()        { return hargaSatuan; }
    public void   setHargaSatuan(double v){ this.hargaSatuan = v; }
    public double getSubtotal()           { return subtotal; }
    public void   setSubtotal(double v)   { this.subtotal = v; }
}
