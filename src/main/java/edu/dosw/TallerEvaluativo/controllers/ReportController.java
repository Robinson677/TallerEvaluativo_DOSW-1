package edu.dosw.TallerEvaluativo.controllers;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.dosw.TallerEvaluativo.dtos.ReportDTO;
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
}

