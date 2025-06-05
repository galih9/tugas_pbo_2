package com.galih.tugas_pbo_2.ui;

import java.awt.BorderLayout;
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
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.galih.tugas_pbo_2.model.Akun;
import com.galih.tugas_pbo_2.model.Paket;
import com.galih.tugas_pbo_2.service.PaketService;
import com.galih.tugas_pbo_2.service.PaketServiceImpl;

import jcharts.JpieCharts;

public final class Dashboard extends JFrame {
    private final Akun loggedInUser;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final PaketService paketService;
    private JButton refreshBtn;
    private JButton tambahBtn;
    private final JpieCharts pie;
    private final DashboardBottomPanel bottomPanel;

    public Dashboard(Akun user) {
        this.loggedInUser = user;
        setTitle("Dashboard Paket - " + user.getTugas());
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        paketService = new PaketServiceImpl();

        tableModel = createTableModel();
        table = createTable();
        pie = createPieChart();
        bottomPanel = new DashboardBottomPanel(pie);

        add(createTablePanel(), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(createButtonPanel(), BorderLayout.NORTH);

        refreshData();
    }

    private DefaultTableModel createTableModel() {
        String[] columns = {"ID", "Pengirim", "Penerima", "Jenis Barang", "Metode", "Status", "Tanggal Masuk", "Tanggal Keluar", "Actions"};
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
            }
        };
    }

    private JTable createTable() {
        JTable localTable = new JTable(tableModel);
        localTable.setRowHeight(40);
        localTable.getColumnModel().getColumn(0).setMaxWidth(50);
        localTable.getColumnModel().getColumn(8).setMinWidth(150);
        localTable.getColumnModel().getColumn(5).setCellRenderer(createStatusRenderer());
        TableColumn actionColumn = localTable.getColumnModel().getColumn(8);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor());
        return localTable;
    }

    private TableCellRenderer createStatusRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(StatusColorUtil.getStatusColor((String) value));
                return c;
            }
        };
    }

    private JpieCharts createPieChart() {
        return new JpieCharts(
            new int[]{190, 320},
            StatusConstants.STATUS_LABELS,
            new int[StatusConstants.STATUS_LABELS.length],
            StatusColorUtil.STATUS_COLORS
        );
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        bottomPanel.setUserInfo(loggedInUser);  // Add this line
        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        
        // Left side buttons
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        refreshBtn = new JButton("Refresh");
        tambahBtn = new JButton("Tambah Paket");
        refreshBtn.addActionListener(e -> refreshData());
        tambahBtn.addActionListener(e -> showPaketForm());
        leftButtons.add(refreshBtn);
        leftButtons.add(tambahBtn);
        
        // Right side logout button
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> logout());
        rightButtons.add(logoutBtn);
        
        buttonPanel.add(leftButtons, BorderLayout.WEST);
        buttonPanel.add(rightButtons, BorderLayout.EAST);
        return buttonPanel;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah anda yakin ingin logout?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION);
                
        if (confirm == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            });
        }
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        try {
            List<Paket> pakets = paketService.getAllPakets();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(java.time.ZoneId.systemDefault());
            int totalProfit = 0;
            int totalPaket = pakets.size();
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
                // Calculate profit
                if ("Reguler".equals(paket.getPengiriman().getMetode())) {
                    totalProfit += 10000;
                } else if ("Express".equals(paket.getPengiriman().getMetode())) {
                    totalProfit += 20000;
                }
            }
            pie.updateData(countPackagesByStatus());
            // Update info panel
            bottomPanel.updateInfo(tableModel.getRowCount(), totalPaket, totalProfit);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private int[] countPackagesByStatus() {
        int[] counts = new int[StatusConstants.STATUS_LABELS.length];
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String status = (String) tableModel.getValueAt(i, 5);
            for (int j = 0; j < StatusConstants.STATUS_LABELS.length; j++) {
                if (StatusConstants.STATUS_LABELS[j].equals(status)) {
                    counts[j]++;
                    break;
                }
            }
        }
        return counts;
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
