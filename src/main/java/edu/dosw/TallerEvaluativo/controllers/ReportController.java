package edu.dosw.TallerEvaluativo.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import edu.dosw.TallerEvaluativo.dtos.ReportDTO;
import edu.dosw.TallerEvaluativo.dtos.ReportResponseDTO;
import edu.dosw.TallerEvaluativo.enums.DecoratorType;
import edu.dosw.TallerEvaluativo.services.ReportService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Endpoints existentes
    @PostMapping
    public ReportDTO createReport(@Valid @RequestBody ReportDTO reportDTO) {
        return reportService.createReport(reportDTO);
    }

    @GetMapping
    public List<ReportDTO> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/{id}")
    public ReportDTO getReportById(@PathVariable String id) {
        return reportService.getReportById(id);
    }

    @GetMapping("/by-date")
    public List<ReportDTO> getReportsByDate(@RequestParam LocalDate date) {
        return reportService.getReportsByDate(date);
    }

    @GetMapping("/by-author")
    public List<ReportDTO> getReportsByAuthor(@RequestParam String author) {
        return reportService.getReportsByAuthor(author);
    }

    @PutMapping("/{id}")
    public ReportDTO updateReport(@PathVariable String id, @Valid @RequestBody ReportDTO dto) {
        return reportService.updateReport(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable String id) {
        reportService.deleteReport(id);
    }

    // Nuevos endpoints para reportes decorados
    @PostMapping("/decorated")
    public ReportResponseDTO createDecoratedReport(@Valid @RequestBody ReportDTO request) {
        return reportService.createDecoratedReport(request);
    }

    @PostMapping("/{id}/decorated")
    public ReportResponseDTO getDecoratedReport(@PathVariable String id,
                                                @RequestBody ReportDTO decoratorConfig) {
        return reportService.getDecoratedReportById(id, decoratorConfig);
    }

    // Endpoint para descargar archivo exportado
    @PostMapping("/{id}/export")
    public ResponseEntity<byte[]> exportReport(@PathVariable String id,
                                               @RequestBody ReportDTO exportConfig) {
        ReportResponseDTO response = reportService.getDecoratedReportById(id, exportConfig);

        if (response.getExportedFile() != null && response.getExportedFile().length > 0) {
            HttpHeaders headers = new HttpHeaders();

            if ("PDF".equals(response.getExportFormat())) {
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment",
                        response.getTitle().replaceAll("\\s+", "_") + ".pdf");
            } else if ("EXCEL".equals(response.getExportFormat())) {
                headers.setContentType(MediaType.valueOf("application/vnd.ms-excel"));
                headers.setContentDispositionFormData("attachment",
                        response.getTitle().replaceAll("\\s+", "_") + ".csv");
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response.getExportedFile());
        }

        return ResponseEntity.notFound().build();
    }

    // Endpoint para obtener solo los datos de gráficas
    @GetMapping("/{id}/charts")
    public ResponseEntity<String> getReportCharts(@PathVariable String id) {
        ReportDTO chartRequest = new ReportDTO();
        chartRequest.getDecorators().add(DecoratorType.CHARTS);

        ReportResponseDTO response = reportService.getDecoratedReportById(id, chartRequest);

        if (response.getHasCharts() && response.getChartData() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.getChartData());
        }

        return ResponseEntity.notFound().build();
    }

    // Endpoint para obtener solo las estadísticas
    @GetMapping("/{id}/statistics")
    public ResponseEntity<Object> getReportStatistics(@PathVariable String id) {
        ReportDTO statsRequest = new ReportDTO();
        statsRequest.getDecorators().add(DecoratorType.STATISTICS);

        ReportResponseDTO response = reportService.getDecoratedReportById(id, statsRequest);

        if (response.getHasStatistics() && response.getStatistics() != null) {
            return ResponseEntity.ok(response.getStatistics());
        }

        return ResponseEntity.notFound().build();
    }
}