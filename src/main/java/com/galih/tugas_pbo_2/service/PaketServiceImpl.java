package com.galih.tugas_pbo_2.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.galih.tugas_pbo_2.db.Database;
import com.galih.tugas_pbo_2.model.ExpressPengiriman;
import com.galih.tugas_pbo_2.model.Paket;
import com.galih.tugas_pbo_2.model.RegulerPengiriman;

public class PaketServiceImpl implements PaketService {

    @Override
    public void simpanPaket(Paket paket) throws Exception {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO paket (pengirim, penerima, jenis_barang, metode_pengiriman, status, tanggal_masuk) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, paket.getPengirim());
            stmt.setString(2, paket.getPenerima());
            stmt.setString(3, paket.getJenisBarang());
            stmt.setString(4, paket.getPengiriman().getMetode());
            stmt.setString(5, paket.getStatus());
            stmt.setTimestamp(6, java.sql.Timestamp.from(paket.getTanggalMasuk())); // Insert tanggal_masuk
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateStatus(int id, String status) throws Exception {
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE paket SET status=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Paket> getAllPakets() throws Exception {
        List<Paket> pakets = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM paket ORDER BY id DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Paket paket = new Paket(
                    rs.getString("pengirim"),
                    rs.getString("penerima"),
                    rs.getString("jenis_barang"),
                    "Express".equals(rs.getString("metode_pengiriman")) ? 
                        new ExpressPengiriman() : new RegulerPengiriman()
                );
                paket.setId(rs.getInt("id"));
                paket.setStatus(rs.getString("status"));
                
                // Handle timestamps without throwing exceptions
                java.sql.Timestamp tanggalMasuk = rs.getTimestamp("tanggal_masuk");
                java.sql.Timestamp tanggalKeluar = rs.getTimestamp("tanggal_keluar");
                
                // Set the timestamps, null is allowed
                paket.setTanggalMasuk(tanggalMasuk != null ? tanggalMasuk.toInstant() : null);
                paket.setTanggalKeluar(tanggalKeluar != null ? tanggalKeluar.toInstant() : null);
                
                pakets.add(paket);
            }
        } catch (SQLException e) {
            throw new Exception("Error loading data: " + e.getMessage());
        }
        return pakets;
    }

    @Override
    public void deletePaket(int id) throws Exception {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM paket WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Paket getPaketById(int id) throws Exception {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM paket WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Paket paket = new Paket(
                    rs.getString("pengirim"),
                    rs.getString("penerima"),
                    rs.getString("jenis_barang"),
                    "Express".equals(rs.getString("metode_pengiriman")) ? 
                        new ExpressPengiriman() : new RegulerPengiriman()
                );
                paket.setId(rs.getInt("id"));
                paket.setStatus(rs.getString("status"));
                return paket;
            }
            return null;
        }
    }

    @Override
    public void updatePaket(Paket paket) throws Exception {
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE paket SET pengirim=?, penerima=?, jenis_barang=?, metode_pengiriman=?, status=?, tanggal_masuk=?, tanggal_keluar=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, paket.getPengirim());
            stmt.setString(2, paket.getPenerima());
            stmt.setString(3, paket.getJenisBarang());
            stmt.setString(4, paket.getPengiriman().getMetode());
            stmt.setString(5, paket.getStatus());
            stmt.setTimestamp(6, java.sql.Timestamp.from(paket.getTanggalMasuk()));
            stmt.setTimestamp(7, paket.getTanggalKeluar() != null ? java.sql.Timestamp.from(paket.getTanggalKeluar()) : null);
            stmt.setInt(8, paket.getId());
            stmt.executeUpdate();
        }
    }
}
