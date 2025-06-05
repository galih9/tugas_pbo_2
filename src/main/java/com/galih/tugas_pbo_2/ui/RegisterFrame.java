package com.galih.tugas_pbo_2.ui;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.galih.tugas_pbo_2.model.Akun;
import com.galih.tugas_pbo_2.service.AkunService;
import com.galih.tugas_pbo_2.service.AkunServiceImpl;

public class RegisterFrame extends JDialog {

    private final JTextField namaField;
    private final JComboBox<String> tugasComboBox;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton registerBtn;
    private final AkunService akunService;

    public RegisterFrame(JFrame parent) {
        super(parent, "Register", true);
        setSize(350, 250);
        setLocationRelativeTo(parent);
        akunService = new AkunServiceImpl();

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("Nama:"));
        namaField = new JTextField();
        panel.add(namaField);
        panel.add(new JLabel("Tugas:"));
        String[] roles = {"Admin", "Kurir", "Hub"};
        tugasComboBox = new JComboBox<>(roles);
        panel.add(tugasComboBox);
        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        registerBtn = new JButton("Register");
        panel.add(new JLabel());
        panel.add(registerBtn);

        add(panel);

        registerBtn.addActionListener(e -> register());
    }

    private void register() {
        String nama = namaField.getText();
        String tugas = (String) tugasComboBox.getSelectedItem();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }
        Akun akun = new Akun();
        akun.setNama(nama);
        akun.setTugas(tugas);
        akun.setEmail(email);
        akun.setPassword(password);
        if (akunService.register(akun)) {
            JOptionPane.showMessageDialog(this, "Registrasi berhasil!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registrasi gagal! Email sudah digunakan.");
        }
    }
}
