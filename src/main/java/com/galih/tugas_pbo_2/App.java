package com.galih.tugas_pbo_2;

import javax.swing.JFrame;

import com.galih.tugas_pbo_2.ui.Dashboard;

public class App {

    public static void main(String[] args) {
        JFrame frame = new Dashboard();
        // set maksimum window size
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
