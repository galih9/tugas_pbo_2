package com.galih.tugas_pbo_2.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.galih.tugas_pbo_2.model.Paket;
import com.galih.tugas_pbo_2.service.PaketService;
import com.galih.tugas_pbo_2.service.PaketServiceImpl;

import jcharts.JpieCharts;

public final class Dashboard extends JFrame {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final PaketService paketService;
    private final JButton refreshBtn;
    private final JButton tambahBtn;
    int width = 220;
    int height = 300;
    private final JpieCharts pie; // Add this field

    public Dashboard() {
        setTitle("Dashboard Paket");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        paketService = new PaketServiceImpl();

        String[] columns = {"ID", "Pengirim", "Penerima", "Jenis Barang", "Metode", "Status", "Tanggal Masuk", "Tanggal Keluar", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(8).setMinWidth(150);

        table.getColumnModel().getColumn(5).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;
                if (status == null) {
                    c.setBackground(table.getBackground());
                } else {
                    switch (status) {
                        case "Selesai":
                            c.setBackground(new java.awt.Color(144, 238, 144));
                            break;
                        case "Sedang dikirim":
                            c.setBackground(new java.awt.Color(255, 255, 224));
                            break;
                        case "Menunggu dijemput":
                            c.setBackground(new java.awt.Color(255, 218, 185));
                            break;
                        case "Sedang dijemput":
                            c.setBackground(new java.awt.Color(176, 224, 230));
                            break;
                        case "Di gudang":
                            c.setBackground(new java.awt.Color(230, 230, 250));
                            break;
                        case "Menuju Transit Hub":
                            c.setBackground(new java.awt.Color(255, 182, 193));
                            break;
                        case "Menunggu dikirim":
                            c.setBackground(new java.awt.Color(221, 160, 221));
                            break;
                        default:
                            c.setBackground(table.getBackground());
                            break;
                    }
                }
                return c;
            }
        });

        TableColumn actionColumn = table.getColumnModel().getColumn(8);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor());
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add empty panel under the table with text
        JPanel emptyPanel = new JPanel(new BorderLayout());
        emptyPanel.setPreferredSize(new java.awt.Dimension(800, 300));
        emptyPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // create your pie with matching colors
        Color[] statusColors = new Color[]{
            new Color(144, 238, 144), // Selesai - Light green
            new Color(255, 218, 185), // Menunggu dijemput - Peach
            new Color(176, 224, 230), // Sedang dijemput - Light blue
            new Color(230, 230, 250), // Di gudang - Lavender
            new Color(255, 182, 193), // Menuju Transit Hub - Light pink
            new Color(221, 160, 221), // Menunggu dikirim - Plum
            new Color(255, 255, 224)  // Sedang dikirim - Light yellow
        };

        pie = new JpieCharts(
            new int[]{width, height},
            new String[]{
                "Selesai", "Menunggu dijemput", "Sedang dijemput",
                "Di gudang", "Menuju Transit Hub", "Menunggu dikirim",
                "Sedang dikirim"
            },
            new int[7], // Initially empty data
            statusColors // Add the colors
        );

        // a JPanel container
        JPanel p = new JPanel();
        p.setSize(width, height);

        emptyPanel.add(pie, BorderLayout.NORTH);

        // Create a container panel for table and empty panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        contentPanel.add(emptyPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        refreshBtn = new JButton("Refresh");
        tambahBtn = new JButton("Tambah Paket");

        refreshBtn.addActionListener(e -> refreshData());
        tambahBtn.addActionListener(e -> showPaketForm());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(tambahBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshData();
    }

    // Add method to count packages by status
    private int[] countPackagesByStatus() {
        int[] counts = new int[7]; // One count for each status
        String[] statuses = {
            "Selesai", "Menunggu dijemput", "Sedang dijemput",
            "Di gudang", "Menuju Transit Hub", "Menunggu dikirim",
            "Sedang dikirim"
        };
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String status = (String) tableModel.getValueAt(i, 5); // Status is in column 5
            for (int j = 0; j < statuses.length; j++) {
                if (statuses[j].equals(status)) {
                    counts[j]++;
                    break;
                }
            }
        }
        return counts;
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        try {
            List<Paket> pakets = paketService.getAllPakets();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(java.time.ZoneId.systemDefault());
            for (Paket paket : pakets) {
                tableModel.addRow(new Object[]{
                    paket.getId(),
                    paket.getPengirim(),
                    paket.getPenerima(),
                    paket.getJenisBarang(),
                    paket.getPengiriman().getMetode(),
                    paket.getStatus(),
                    paket.getTanggalMasuk() != null ? formatter.format(paket.getTanggalMasuk()) : "",
                    paket.getTanggalKeluar() != null ? formatter.format(paket.getTanggalKeluar()) : "",
                    "actions"
                });
            }

            // Update pie chart with new data
            int[] statusCounts = countPackagesByStatus();
            pie.updateData(statusCounts);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void showPaketForm() {
        PaketForm form = new PaketForm(this);
        form.setLocationRelativeTo(this);
        form.setVisible(true);
    }

    private void editPaket(int id) {
        try {
            Paket paket = paketService.getPaketById(id);
            if (paket != null) {
                PaketForm form = new PaketForm(this, paket);
                form.setLocationRelativeTo(this);
                form.setVisible(true);
                refreshData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error editing paket: " + e.getMessage());
        }
    }

    private void deletePaket(int id) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah anda yakin ingin menghapus paket ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                paketService.deletePaket(id);
                refreshData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting paket: " + e.getMessage());
            }
        }
    }

    private void selesaiPaket(int id) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah anda yakin ingin menyelesaikan paket ini?",
                "Konfirmasi Selesai",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Paket paket = paketService.getPaketById(id);
                if (paket != null) {
                    paket.setStatus("Selesai");
                    paket.setTanggalKeluar(Instant.now());
                    paketService.updatePaket(paket);
                    refreshData();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error completing paket: " + e.getMessage());
            }
        }
    }

    class ButtonRenderer extends JPanel implements TableCellRenderer {

        private final JButton editBtn = new JButton("Edit");
        private final JButton deleteBtn = new JButton("Delete");
        private final JButton selesaiBtn = new JButton("Selesai");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(editBtn);
            add(deleteBtn);
            add(selesaiBtn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

        private final JPanel panel = new JPanel();
        private final JButton editBtn = new JButton("Edit");
        private final JButton deleteBtn = new JButton("Delete");
        private final JButton selesaiBtn = new JButton("Selesai");

        public ButtonEditor() {
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.add(editBtn);
            panel.add(deleteBtn);
            panel.add(selesaiBtn);

            editBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                int id = (Integer) table.getValueAt(row, 0);
                editPaket(id);
            });

            deleteBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                int id = (Integer) table.getValueAt(row, 0);
                deletePaket(id);
            });

            selesaiBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                int id = (Integer) table.getValueAt(row, 0);
                selesaiPaket(id);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "actions";
        }
    }
}
