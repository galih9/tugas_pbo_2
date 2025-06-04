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

    public PaketForm() {
        initComponents();
    }

    public PaketForm(Paket paket) {
        initComponents();
        editId = paket.getId();
        tfPengirim.setText(paket.getPengirim());
        tfPenerima.setText(paket.getPenerima());
        jenisBarangCombo.setSelectedItem(paket.getJenisBarang());
        if (paket.getPengiriman() instanceof ExpressPengiriman) {
            expressBtn.setSelected(true);
        } else {
            regulerBtn.setSelected(true);
        }
        setTitle("Edit Paket");
    }

    private void initComponents() {
        setTitle("Input Paket");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        tfPengirim = new JTextField();
        tfPenerima = new JTextField();
        jenisBarangCombo = new JComboBox<>(new String[]{"Dokumen", "Elektronik", "Pakaian"});

        regulerBtn = new JRadioButton("Reguler");
        expressBtn = new JRadioButton("Express");
        ButtonGroup metodeGroup = new ButtonGroup();
        metodeGroup.add(regulerBtn);
        metodeGroup.add(expressBtn);
        regulerBtn.setSelected(true);

        simpanBtn = new JButton("Simpan Paket");
        simpanBtn.addActionListener(e -> simpanPaket());

        add(new JLabel("Nama Pengirim:"));
        add(tfPengirim);
        add(new JLabel("Nama Penerima:"));
        add(tfPenerima);
        add(new JLabel("Jenis Barang:"));
        add(jenisBarangCombo);
        add(regulerBtn);
        add(expressBtn);
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
        }
        PaketService service = new PaketServiceImpl();

        try {
            service.simpanPaket(paket);
            JOptionPane.showMessageDialog(this, "Paket berhasil disimpan!");
            
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan paket: " + ex.getMessage());
        }
    }
}
