package com.kerbino.bcpredict.controller.report;

import com.kerbino.bcpredict.services.report.CSVReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class CSVReportController {

    private final CSVReportService csvReportService;

    @GetMapping("/coins")
    public ResponseEntity<byte[]> generateCoinReport() {
        byte[] csvData = csvReportService.generateCSVReport();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=coins_report.csv");
        headers.setContentType(MediaType.TEXT_PLAIN);
        return ResponseEntity.ok().headers(headers).body(csvData);
    }
} 