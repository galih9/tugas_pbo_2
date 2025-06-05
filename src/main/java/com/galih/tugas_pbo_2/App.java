package com.galih.tugas_pbo_2;

import javax.swing.SwingUtilities;

import com.galih.tugas_pbo_2.ui.LoginFrame;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
