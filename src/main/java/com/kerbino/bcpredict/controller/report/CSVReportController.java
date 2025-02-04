package com.kerbino.bcpredict.controller.report;

import com.kerbino.bcpredict.services.report.CSVReportService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class CSVReportController {

    private final CSVReportService csvReportService;
    private static final Logger logger = LoggerFactory.getLogger(CSVReportController.class);

    /**
     * Rota para download direto do relatório CSV.
     */
    @GetMapping("/coins")
    public ResponseEntity<byte[]> generateCoinReport() {
        byte[] csvData = csvReportService.generateCSVReport();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=coins_report.csv");
        headers.setContentType(MediaType.TEXT_PLAIN);
        return ResponseEntity.ok().headers(headers).body(csvData);
    }

    /**
     * Rota que gera o relatório CSV e o salva na pasta "savedData" com um timestamp no nome do arquivo.
     *
     * @return Mensagem com o caminho completo do arquivo salvo.
     */
    @GetMapping("/coins/save")
    public ResponseEntity<String> saveCSVReport() {
        try {
            byte[] csvData = csvReportService.generateCSVReport();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            Path dirPath = Paths.get("src/main/java/com/kerbino/bcpredict/savedData");
            
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            
            String fileName = "coins_report_" + timestamp + ".csv";
            Path filePath = dirPath.resolve(fileName);
            Files.write(filePath, csvData, StandardOpenOption.CREATE);

            logger.info("CSV report saved at {}", filePath.toAbsolutePath());

            return ResponseEntity.ok("CSV report saved successfully: " + filePath.toAbsolutePath());
        } catch (IOException ex) {
            logger.error("Error saving CSV report", ex);
            throw new RuntimeException("Error saving CSV report: " + ex.getMessage());
        }
    }
} 