package com.galih.tugas_pbo_2.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import jcharts.JpieCharts;

public class DashboardBottomPanel extends JPanel {

    private final JLabel totalDataLabel;
    private final JLabel totalPaketLabel;
    private final JLabel totalProfitLabel;

    public DashboardBottomPanel(JpieCharts pieChart) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 300));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Pie chart panel with fixed width, border, and right padding
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(220, 300));
        leftPanel.setBorder(new CompoundBorder(
                new LineBorder(java.awt.Color.GRAY, 2, true),
                new EmptyBorder(0, 0, 0, 0)
        ));
        leftPanel.add(pieChart, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new CompoundBorder(
            new TitledBorder(new LineBorder(new Color(0x1976D2), 2, true), "Ringkasan Informasi", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), new Color(0x1976D2)),
            new EmptyBorder(20, 30, 20, 10)
        ));
        rightPanel.setBackground(Color.WHITE);

        // Info row: icon + label + value
        totalDataLabel = createInfoLabel("Total Data: 0", "/icons/database.png");
        totalPaketLabel = createInfoLabel("Total Paket: 0", "/icons/box.png");
        totalProfitLabel = createInfoLabel("Total Profit: Rp 0", "/icons/money.png");

        rightPanel.add(totalDataLabel);
        rightPanel.add(Box.createVerticalStrut(18));
        rightPanel.add(totalPaketLabel);
        rightPanel.add(Box.createVerticalStrut(18));
        rightPanel.add(totalProfitLabel);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private JLabel createInfoLabel(String text, String iconPath) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        label.setForeground(new Color(0x333333));
        // Try to load icon, fallback if not found
        try {
            label.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        } catch (Exception e) {
            // No icon, ignore
        }
        label.setIconTextGap(16);
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    public void updateInfo(int totalData, int totalPaket, int totalProfit) {
        totalDataLabel.setText("  Total Data: " + totalData);
        totalPaketLabel.setText("  Total Paket: " + totalPaket);
        totalProfitLabel.setText("  Total Profit: Rp " + totalProfit);
    }
}
