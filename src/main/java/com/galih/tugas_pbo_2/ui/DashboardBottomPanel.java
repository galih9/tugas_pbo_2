package com.galih.tugas_pbo_2.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import jcharts.JpieCharts;
import jcharts.JtimeSeries;

public class DashboardBottomPanel extends JPanel {

    public DashboardBottomPanel(JpieCharts pieChart) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 300));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Pie chart panel with fixed width
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(220, 300));
        leftPanel.add(pieChart, BorderLayout.CENTER);

        // Time series chart panel fills the rest
        int graphWidth = 700;
        int graphHeight = 320;
        int widthForYLabels = 40;
        int heightForXLabels = 40;
        int widthForItemLabel = 100;

        JtimeSeries timeChart = new JtimeSeries(
            new int[]{graphWidth, graphHeight},
            new int[]{widthForYLabels, heightForXLabels, widthForItemLabel},
            new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"},
            new String[]{"Burger", "Fried Chicken", "Fries"},
            new int[][]{
                {67, 10, 20, 30, 50, 80, 130},
                {23, 100, 75, 200, 50, 180, 90},
                {20, 40, 60, 80, 100, 120, 140}
            }
        );
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(timeChart, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }
}
