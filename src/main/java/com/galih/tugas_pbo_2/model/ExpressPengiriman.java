package com.galih.tugas_pbo_2.model;

public class ExpressPengiriman extends Pengiriman {
    @Override
    public String getMetode() {
        return "Express";
    }

    @Override
    public double getBiaya() {
        return 20000;
    }
}
