package com.galih.tugas_pbo_2.ui;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.galih.tugas_pbo_2.model.ExpressPengiriman;
import com.galih.tugas_pbo_2.model.Paket;
import com.galih.tugas_pbo_2.model.Pengiriman;
import com.galih.tugas_pbo_2.model.RegulerPengiriman;
import com.galih.tugas_pbo_2.service.PaketService;
import com.galih.tugas_pbo_2.service.PaketServiceImpl;

public class PaketForm extends JFrame {

    private JTextField tfPengirim, tfPenerima;
    private JComboBox<String> jenisBarangCombo;
    private JRadioButton regulerBtn, expressBtn;
    private JButton simpanBtn;
    private Integer editId = null;
    private final Dashboard dashboard;
    private JComboBox<String> statusCombo;
    private final String[] STATUSES = {
        "Selesai", "Menunggu dijemput", "Sedang dijemput",
        "Di gudang", "Menuju Transit Hub", "Menunggu dikirim",
        "Sedang dikirim"
    };

    public PaketForm(Dashboard dashboard) {
        this.dashboard = dashboard;
        initComponents();
    }

    public PaketForm(Dashboard dashboard, Paket paket) {
        this.dashboard = dashboard;
        this.editId = paket.getId();
        initComponents();

        tfPengirim.setText(paket.getPengirim());
        tfPenerima.setText(paket.getPenerima());
        jenisBarangCombo.setSelectedItem(paket.getJenisBarang());
        statusCombo.setSelectedItem(paket.getStatus());
        if (paket.getPengiriman() instanceof ExpressPengiriman) {
            expressBtn.setSelected(true);
        } else {
            regulerBtn.setSelected(true);
        }
        setTitle("Edit Paket");
    }

    private void initComponents() {

        tfPengirim = new JTextField();
        tfPenerima = new JTextField();
        jenisBarangCombo = new JComboBox<>(new String[]{"Dokumen", "Elektronik", "Pakaian"});
        statusCombo = new JComboBox<>(STATUSES);

        regulerBtn = new JRadioButton("Reguler");
        expressBtn = new JRadioButton("Express");
        ButtonGroup metodeGroup = new ButtonGroup();
        metodeGroup.add(regulerBtn);
        metodeGroup.add(expressBtn);
        regulerBtn.setSelected(true);

        simpanBtn = new JButton("Simpan Paket");
        simpanBtn.addActionListener(e -> simpanPaket());

        setTitle("Input Paket");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        int rows = (editId != null) ? 7 : 6;
        setLayout(new GridLayout(rows, 2));

        add(new JLabel("Nama Pengirim:"));
        add(tfPengirim);
        add(new JLabel("Nama Penerima:"));
        add(tfPenerima);
        add(new JLabel("Jenis Barang:"));
        add(jenisBarangCombo);
        add(regulerBtn);
        add(expressBtn);

        if (editId != null) {
            add(new JLabel("Status:"));
            add(statusCombo);
        }

        add(new JLabel(""));
        add(simpanBtn);
    }

    private void simpanPaket() {
        String pengirim = tfPengirim.getText();
        String penerima = tfPenerima.getText();
        String jenisBarang = (String) jenisBarangCombo.getSelectedItem();
        Pengiriman pengiriman = regulerBtn.isSelected() ? new RegulerPengiriman() : new ExpressPengiriman();

        Paket paket = new Paket(pengirim, penerima, jenisBarang, pengiriman);
        if (editId != null) {
            paket.setId(editId);
            paket.setStatus((String) statusCombo.getSelectedItem());
        }
        PaketService service = new PaketServiceImpl();

        try {
            service.simpanPaket(paket);
            JOptionPane.showMessageDialog(this, "Paket berhasil disimpan!");
            dashboard.refreshData();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan paket: " + ex.getMessage());
        }
    }
}
