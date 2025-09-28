package edu.dosw.TallerEvaluativo.services;

import edu.dosw.TallerEvaluativo.models.ReportComponent;
import edu.dosw.TallerEvaluativo.dtos.ReportDTO;
import edu.dosw.TallerEvaluativo.dtos.ReportResponseDTO;
import edu.dosw.TallerEvaluativo.models.Report;
import edu.dosw.TallerEvaluativo.models.ReportMapper;
import edu.dosw.TallerEvaluativo.models.Transaction;
import edu.dosw.TallerEvaluativo.repositories.ReportRepository;
import edu.dosw.TallerEvaluativo.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final TransactionRepository transactionRepository;
    private final ReportMapper reportMapper;
    private final ReportDecoratorFactory decoratorFactory;

    public ReportService(ReportRepository reportRepository,
                                 TransactionRepository transactionRepository,
                                 ReportMapper reportMapper,
                                 ReportDecoratorFactory decoratorFactory) {
        this.reportRepository = reportRepository;
        this.transactionRepository = transactionRepository;
        this.reportMapper = reportMapper;
        this.decoratorFactory = decoratorFactory;
    }

    public ReportDTO createReport(ReportDTO dto) {
        Report report = reportMapper.toEntity(dto);
        report.setId(UUID.randomUUID().toString());
        report.setDate(LocalDate.now());
        report.setTransactions(List.of());
        Report saved = reportRepository.save(report);
        return reportMapper.toDTO(saved);
    }

    // Nuevo método para crear reportes con decoradores
    public ReportResponseDTO createDecoratedReport(ReportDTO request) {
        // Crear reporte base
        Report report = new Report();
        report.setId(UUID.randomUUID().toString());
        report.setTitle(request.getTitle());
        report.setAuthor(request.getAuthor());
        report.setContent(request.getContent());
        report.setDate(LocalDate.now());

        // Obtener todas las transacciones (o filtrar según necesidades)
        List<Transaction> transactions = transactionRepository.findAll();
        report.setTransactions(transactions);

        // Guardar reporte
        Report saved = reportRepository.save(report);

        // Aplicar decoradores y generar respuesta
        ReportComponent decoratedReport = decoratorFactory.createDecoratedReport(saved, request);
        return decoratedReport.generate();
    }

    // Método para obtener reporte decorado por ID
    public ReportResponseDTO getDecoratedReportById(String id, ReportDTO decoratorConfig) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reporte no encontrado"));

        // Aplicar decoradores
        ReportComponent decoratedReport = decoratorFactory.createDecoratedReport(report, decoratorConfig);
        return decoratedReport.generate();
    }

    // Métodos existentes mantienen su funcionalidad
    public List<ReportDTO> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        if (reports.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron reportes");
        }
        return reports.stream().map(reportMapper::toDTO).toList();
    }

    public ReportDTO getReportById(String id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reporte no encontrado"));
        return reportMapper.toDTO(report);
    }

    public List<ReportDTO> getReportsByDate(LocalDate date) {
        List<Report> reports = reportRepository.findAll()
                .stream()
                .filter(r -> r.getDate().equals(date))
                .toList();
        if (reports.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron reportes para la fecha: " + date);
        }
        return reports.stream().map(reportMapper::toDTO).toList();
    }

    public List<ReportDTO> getReportsByAuthor(String author) {
        List<Report> reports = reportRepository.findAll()
                .stream()
                .filter(r -> r.getAuthor().equalsIgnoreCase(author))
                .toList();
        if (reports.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron reportes del autor: " + author);
        }
        return reports.stream().map(reportMapper::toDTO).toList();
    }

    public ReportDTO updateReport(String id, ReportDTO dto) {
        Report existing = reportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reporte no encontrado"));
        reportMapper.updateEntity(existing, dto);
        Report updated = reportRepository.save(existing);
        return reportMapper.toDTO(updated);
    }

    public void deleteReport(String id) {
        boolean exists = reportRepository.existsById(id);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reporte no encontrado");
        }
        reportRepository.deleteById(id);
    }
}