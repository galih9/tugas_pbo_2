package com.galih.tugas_pbo_2.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.galih.tugas_pbo_2.model.Paket;
import java.io.FileOutputStream;
import java.util.List;

public class ExcelExporter {
    public static void exportToExcel(List<Paket> pakets, String filePath) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Paket Data");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Pengirim", "Penerima", "Jenis Barang", "Metode", "Status", "Tanggal Masuk", "Tanggal Keluar"};
            
            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Create header cells
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Create data rows
            int rowNum = 1;
            for (Paket paket : pakets) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(paket.getId());
                row.createCell(1).setCellValue(paket.getPengirim());
                row.createCell(2).setCellValue(paket.getPenerima());
                row.createCell(3).setCellValue(paket.getJenisBarang());
                row.createCell(4).setCellValue(paket.getPengiriman().getMetode());
                row.createCell(5).setCellValue(paket.getStatus());
                row.createCell(6).setCellValue(paket.getTanggalMasuk() != null ? paket.getTanggalMasuk().toString() : "");
                row.createCell(7).setCellValue(paket.getTanggalKeluar() != null ? paket.getTanggalKeluar().toString() : "");
            }

            // Autosize columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }
}
