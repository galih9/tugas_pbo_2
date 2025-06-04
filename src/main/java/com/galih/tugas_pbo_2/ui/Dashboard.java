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

public final class Dashboard extends JFrame {
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

        
        String[] columns = {"ID", "Pengirim", "Penerima", "Jenis Barang", "Metode", "Status", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; 
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(40); 
        table.getColumnModel().getColumn(0).setMaxWidth(50); 
        table.getColumnModel().getColumn(6).setMinWidth(150); 
        
        
        TableColumn actionColumn = table.getColumnModel().getColumn(6);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor());
        JScrollPane scrollPane = new JScrollPane(table);
        
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10)); 
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);

        
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

    public void refreshData() {
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
                    "actions" 
                });
            }
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
            "Are you sure you want to delete this package?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                paketService.deletePaket(id);
                refreshData(); // Refresh the table after deletion
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting paket: " + e.getMessage());
            }
        }
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
}
