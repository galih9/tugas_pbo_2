package com.galih.tugas_pbo_2.model;

import java.time.Instant;

public class Paket {
    private Integer id;
    private final String pengirim;
    private final String penerima;
    private final String jenisBarang;
    private final Pengiriman pengiriman;
    private String status;
    private Instant tanggalMasuk;
    private Instant tanggalKeluar;

    public Paket(String pengirim, String penerima, String jenisBarang, Pengiriman pengiriman) {
        this.pengirim = pengirim;
        this.penerima = penerima;
        this.jenisBarang = jenisBarang;
        this.pengiriman = pengiriman;
        this.status = "Menunggu dijemput";
        this.tanggalMasuk = Instant.now(); // Set current time as tanggalMasuk
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPengirim() { return pengirim; }
    public String getPenerima() { return penerima; }
    public String getJenisBarang() { return jenisBarang; }
    public Pengiriman getPengiriman() { return pengiriman; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getTanggalMasuk() { return tanggalMasuk; }
    public void setTanggalMasuk(Instant tanggalMasuk) { this.tanggalMasuk = tanggalMasuk; }

    public Instant getTanggalKeluar() { return tanggalKeluar; }
    public void setTanggalKeluar(Instant tanggalKeluar) { this.tanggalKeluar = tanggalKeluar; }
}
