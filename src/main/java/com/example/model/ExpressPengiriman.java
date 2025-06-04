package com.example.model;

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
