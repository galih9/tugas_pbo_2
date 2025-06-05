package com.galih.tugas_pbo_2.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.galih.tugas_pbo_2.model.Akun;
import com.galih.tugas_pbo_2.service.AkunService;
import com.galih.tugas_pbo_2.service.AkunServiceImpl;

public class LoginFrame extends JFrame {

    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginBtn, registerBtn;
    private final AkunService akunService;

    public LoginFrame() {
        setTitle("Login");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        akunService = new AkunServiceImpl();

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginBtn = new JButton("Login");
        registerBtn = new JButton("Register");
        JPanel btnPanel = new JPanel();
        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);

        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> register());
    }

    private void login() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        Akun akun = akunService.login(email, password);
        if (akun != null) {
            SwingUtilities.invokeLater(() -> {
                Dashboard dashboard = new Dashboard(akun);
                dashboard.setExtendedState(JFrame.MAXIMIZED_BOTH);
                dashboard.setVisible(true);
                dispose();
            });
        } else {
            JOptionPane.showMessageDialog(this, "Email atau password salah!");
        }
    }

    private void register() {
        RegisterFrame reg = new RegisterFrame(this);
        reg.setLocationRelativeTo(this);
        reg.setVisible(true);
    }
}
