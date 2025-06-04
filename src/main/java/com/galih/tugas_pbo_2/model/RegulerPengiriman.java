package com.galih.tugas_pbo_2.model;

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
