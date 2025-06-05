package com.galih.tugas_pbo_2.model;

public class Akun {
    private String id;      // Changed from int to String
    private String nama;
    private String tugas;
    private String email;
    private String password;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getTugas() { return tugas; }
    public void setTugas(String tugas) { this.tugas = tugas; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
