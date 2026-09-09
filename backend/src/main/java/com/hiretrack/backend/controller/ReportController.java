package com.hiretrack.backend.controller;

import com.hiretrack.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/placements/pdf")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER', 'HOD')")
    public ResponseEntity<byte[]> downloadPlacementPdf() {
        byte[] pdf = reportService.generatePlacementPdfReport();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "placement_report.pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }

    @GetMapping("/placements/csv")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER', 'HOD')")
    public ResponseEntity<String> downloadPlacementCsv() {
        String csv = reportService.generatePlacementCsvReport();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("text/csv"));
        headers.setContentDispositionFormData("attachment", "placement_report.csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csv);
    }
}
