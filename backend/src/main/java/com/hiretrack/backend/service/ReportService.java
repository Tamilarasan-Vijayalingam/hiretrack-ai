package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Placement;
import com.hiretrack.backend.repository.PlacementRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final PlacementRepository placementRepository;

    public byte[] generatePlacementPdfReport() {
        try {
            List<Placement> placements = placementRepository.findAll();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();
            
            document.add(new Paragraph("HireTrack AI - Placement Report"));
            document.add(new Paragraph(" ")); 
            
            PdfPTable table = new PdfPTable(4);
            table.addCell("Student Name");
            table.addCell("Company");
            table.addCell("Job Role");
            table.addCell("Package (LPA)");
            
            for (Placement p : placements) {
                table.addCell(p.getStudent() != null ? p.getStudent().getName() : "N/A");
                table.addCell(p.getCompany() != null ? p.getCompany().getName() : "N/A");
                table.addCell(p.getJobRole() != null ? p.getJobRole() : "N/A");
                table.addCell(p.getPackageOffered() != null ? p.getPackageOffered().toString() : "N/A");
            }
            
            document.add(table);
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    public String generatePlacementCsvReport() {
        try {
            List<Placement> placements = placementRepository.findAll();
            StringWriter sw = new StringWriter();
            CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT.withHeader("Student Name", "Company", "Job Role", "Package"));
            
            for (Placement p : placements) {
                csvPrinter.printRecord(
                    p.getStudent() != null ? p.getStudent().getName() : "N/A",
                    p.getCompany() != null ? p.getCompany().getName() : "N/A",
                    p.getJobRole() != null ? p.getJobRole() : "N/A",
                    p.getPackageOffered() != null ? p.getPackageOffered().toString() : "N/A"
                );
            }
            csvPrinter.flush();
            return sw.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate CSV", e);
        }
    }
}
