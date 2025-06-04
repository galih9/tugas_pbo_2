package com.galih.tugas_pbo_2.ui;

import java.awt.Color;

public class StatusColorUtil {
    public static final Color[] STATUS_COLORS = {
        new Color(144, 238, 144), // Selesai - Light green
        new Color(255, 218, 185), // Menunggu dijemput - Peach
        new Color(176, 224, 230), // Sedang dijemput - Light blue
        new Color(230, 230, 250), // Di gudang - Lavender
        new Color(255, 182, 193), // Menuju Transit Hub - Light pink
        new Color(221, 160, 221), // Menunggu dikirim - Plum
        new Color(255, 255, 224)  // Sedang dikirim - Light yellow
    };

    public static Color getStatusColor(String status) {
        String[] labels = StatusConstants.STATUS_LABELS;
        for (int i = 0; i < labels.length; i++) {
            if (labels[i].equals(status)) {
                return STATUS_COLORS[i];
            }
        }
        return Color.WHITE;
    }
}
