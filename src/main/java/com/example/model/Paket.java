package com.example.model;

public class Paket {
    private Integer id;
    private final String pengirim;
    private final String penerima;
    private final String jenisBarang;
    private final Pengiriman pengiriman;
    private String status;

    public Paket(String pengirim, String penerima, String jenisBarang, Pengiriman pengiriman) {
        this.pengirim = pengirim;
        this.penerima = penerima;
        this.jenisBarang = jenisBarang;
        this.pengiriman = pengiriman;
        this.status = "Menunggu dijemput";
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPengirim() { return pengirim; }
    public String getPenerima() { return penerima; }
    public String getJenisBarang() { return jenisBarang; }
    public Pengiriman getPengiriman() { return pengiriman; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }
}
