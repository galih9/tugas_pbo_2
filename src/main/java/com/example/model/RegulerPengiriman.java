package com.example.model;

public class RegulerPengiriman extends Pengiriman {
    @Override
    public String getMetode() {
        return "Reguler";
    }

    @Override
    public double getBiaya() {
        return 10000;
    }
}
