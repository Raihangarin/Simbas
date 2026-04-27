package sembako.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Transaksi {
    private int    idTransaksi;
    private String noTransaksi;
    private String jenis;       
    private Date   tanggal;
    private double totalHarga;
    private String keterangan;
    private int    idUser;
    private List<DetailTransaksi> details = new ArrayList<>();

    public Transaksi() {}

    public int    getIdTransaksi()           { return idTransaksi; }
    public void   setIdTransaksi(int v)      { this.idTransaksi = v; }
    public String getNoTransaksi()           { return noTransaksi; }
    public void   setNoTransaksi(String v)   { this.noTransaksi = v; }
    public String getJenis()                 { return jenis; }
    public void   setJenis(String v)         { this.jenis = v; }
    public Date   getTanggal()               { return tanggal; }
    public void   setTanggal(Date v)         { this.tanggal = v; }
    public double getTotalHarga()            { return totalHarga; }
    public void   setTotalHarga(double v)    { this.totalHarga = v; }
    public String getKeterangan()            { return keterangan; }
    public void   setKeterangan(String v)    { this.keterangan = v; }
    public int    getIdUser()                { return idUser; }
    public void   setIdUser(int v)           { this.idUser = v; }
    public List<DetailTransaksi> getDetails(){ return details; }
    public void   setDetails(List<DetailTransaksi> v){ this.details = v; }
}
