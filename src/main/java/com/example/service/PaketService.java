package com.example.service;

import java.util.List;

import com.example.model.Paket;

public interface PaketService {
    void simpanPaket(Paket paket) throws Exception;
    void updateStatus(int id, String status) throws Exception;
    List<Paket> getAllPakets() throws Exception;
    void deletePaket(int id) throws Exception;
    Paket getPaketById(int id) throws Exception;
}
