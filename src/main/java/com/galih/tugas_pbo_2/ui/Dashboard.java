package com.galih.tugas_pbo_2.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
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

public class Dashboard extends JFrame {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final PaketService paketService;
    private final JButton refreshBtn;
    private final JButton tambahBtn;

    public Dashboard() {
        setTitle("Dashboard Paket");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        paketService = new PaketServiceImpl();

        // Create table with custom model
        String[] columns = {"ID", "Pengirim", "Penerima", "Jenis Barang", "Metode", "Status", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only allow editing of action column
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(40); // Set row height to accommodate buttons better
        table.getColumnModel().getColumn(0).setMaxWidth(50); // ID column
        table.getColumnModel().getColumn(6).setMinWidth(150); // Actions column
        
        // Add button column
        TableColumn actionColumn = table.getColumnModel().getColumn(6);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor());
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Create panel for table with margin
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add 10px margin on all sides
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);

        // Create button panel
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

    private void refreshData() {
        tableModel.setRowCount(0);
        try {
            List<Paket> pakets = paketService.getAllPakets();
            for (Paket paket : pakets) {
                tableModel.addRow(new Object[]{
                    paket.getId(),
                    paket.getPengirim(),
                    paket.getPenerima(),
                    paket.getJenisBarang(),
                    paket.getPengiriman().getMetode(),
                    paket.getStatus(),
                    "actions" // This cell will be replaced with buttons
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void showPaketForm() {
        PaketForm form = new PaketForm();
        form.setLocationRelativeTo(this);
        form.setVisible(true);
    }

    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton editBtn = new JButton("Edit");
        private final JButton deleteBtn = new JButton("Delete");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(editBtn);
            add(deleteBtn);
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

        public ButtonEditor() {
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.add(editBtn);
            panel.add(deleteBtn);

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

    private void editPaket(int id) {
        try {
            Paket paket = paketService.getPaketById(id);
            if (paket != null) {
                PaketForm form = new PaketForm(paket);
                form.setLocationRelativeTo(this);
                form.setVisible(true);
                refreshData(); // Refresh after edit
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error editing paket: " + e.getMessage());
        }
    }

    private void deletePaket(int id) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this package?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                paketService.deletePaket(id);
                refreshData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting paket: " + e.getMessage());
            }
        }
    }
}
