package com.galih.tugas_pbo_2.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.galih.tugas_pbo_2.db.Database;
import com.galih.tugas_pbo_2.model.Akun;

public class AkunServiceImpl implements AkunService {

    private String generateUniqueId(String tugas) {
        // Convert tugas to uppercase and generate random string
        String prefix = tugas.toUpperCase();
        String randomString = UUID.randomUUID().toString().substring(0, 8);
        return prefix + "_" + randomString;
    }

    @Override
    public Akun login(String email, String password) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM akun WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapToAkun(rs);
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean register(Akun akun) {
        try (Connection conn = Database.getConnection()) {
            // Check if email already exists
            if (findByEmail(akun.getEmail()) != null) {
                return false;
            }

            // Generate unique ID
            String uniqueId = generateUniqueId(akun.getTugas());
            
            String sql = "INSERT INTO akun (id, nama, email, password, tugas) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, uniqueId);
            stmt.setString(2, akun.getNama());
            stmt.setString(3, akun.getEmail());
            stmt.setString(4, akun.getPassword());
            stmt.setString(5, akun.getTugas());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error during registration: " + e.getMessage());
            return false;
        }
    }

    private Akun findByEmail(String email) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM akun WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapToAkun(rs);
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    private Akun mapToAkun(ResultSet rs) throws SQLException {
        Akun akun = new Akun();
        akun.setId(rs.getString("id")); // Changed from int to String
        akun.setNama(rs.getString("nama"));
        akun.setEmail(rs.getString("email"));
        akun.setPassword(rs.getString("password"));
        akun.setTugas(rs.getString("tugas"));
        return akun;
    }
}
