package edu.dosw.TallerEvaluativo.TallerEvaluativo_DOSW_1;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import edu.dosw.TallerEvaluativo.dtos.ReportDTO;
import edu.dosw.TallerEvaluativo.dtos.ReportResponseDTO;
import edu.dosw.TallerEvaluativo.models.Report;
import edu.dosw.TallerEvaluativo.models.ReportMapper;
import edu.dosw.TallerEvaluativo.models.Transaction;
import edu.dosw.TallerEvaluativo.repositories.ReportRepository;
import edu.dosw.TallerEvaluativo.repositories.TransactionRepository;
import edu.dosw.TallerEvaluativo.services.ReportDecoratorFactory;
import edu.dosw.TallerEvaluativo.services.ReportService;
import edu.dosw.TallerEvaluativo.models.ReportComponent;

class ReportServiceTest {

    private ReportRepository reportRepository;
    private TransactionRepository transactionRepository;
    private ReportMapper reportMapper;
    private ReportDecoratorFactory decoratorFactory;
    private ReportService reportService;

    @BeforeEach
    void setup() {
        reportRepository = mock(ReportRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        reportMapper = mock(ReportMapper.class);
        decoratorFactory = mock(ReportDecoratorFactory.class);
        reportService = new ReportService(reportRepository, transactionRepository, reportMapper, decoratorFactory);
    }

    @Test
    void testShouldCreateReport() {
        ReportDTO dto = new ReportDTO();
        dto.setTitle("Test Report");
        dto.setAuthor("Test Author");
        dto.setContent("Test Content");

        Report report = new Report();
        report.setTitle("Test Report");
        report.setAuthor("Test Author");
        report.setContent("Test Content");

        Report savedReport = new Report();
        savedReport.setId("1");
        savedReport.setTitle("Test Report");
        savedReport.setAuthor("Test Author");
        savedReport.setContent("Test Content");
        savedReport.setDate(LocalDate.now());

        ReportDTO expectedDTO = new ReportDTO();
        expectedDTO.setTitle("Test Report");
        expectedDTO.setAuthor("Test Author");
        expectedDTO.setContent("Test Content");

        when(reportMapper.toEntity(dto)).thenReturn(report);
        when(reportRepository.save(any(Report.class))).thenReturn(savedReport);
        when(reportMapper.toDTO(savedReport)).thenReturn(expectedDTO);

        ReportDTO result = reportService.createReport(dto);

        assertNotNull(result);
        assertEquals("Test Report", result.getTitle());
        assertEquals("Test Author", result.getAuthor());
        verify(reportRepository).save(any(Report.class));
    }

    @Test
    void testShouldCreateDecoratedReport() {
        ReportDTO request = new ReportDTO();
        request.setTitle("Decorated Report");
        request.setAuthor("Test Author");
        request.setContent("Test Content");

        Transaction transaction = new Transaction();
        List<Transaction> transactions = List.of(transaction);

        Report savedReport = new Report();
        savedReport.setId("1");
        savedReport.setTitle("Decorated Report");

        ReportComponent mockComponent = mock(ReportComponent.class);
        ReportResponseDTO expectedResponse = new ReportResponseDTO();
        expectedResponse.setTitle("Decorated Report");

        when(transactionRepository.findAll()).thenReturn(transactions);
        when(reportRepository.save(any(Report.class))).thenReturn(savedReport);
        when(decoratorFactory.createDecoratedReport(savedReport, request)).thenReturn(mockComponent);
        when(mockComponent.generate()).thenReturn(expectedResponse);

        ReportResponseDTO result = reportService.createDecoratedReport(request);

        assertNotNull(result);
        assertEquals("Decorated Report", result.getTitle());
        verify(reportRepository).save(any(Report.class));
    }

    @Test
    void testShouldGetAllReports() {
        Report r1 = new Report();
        r1.setId("1");
        r1.setTitle("Report 1");

        Report r2 = new Report();
        r2.setId("2");
        r2.setTitle("Report 2");

        ReportDTO dto1 = new ReportDTO();
        dto1.setTitle("Report 1");

        ReportDTO dto2 = new ReportDTO();
        dto2.setTitle("Report 2");

        when(reportRepository.findAll()).thenReturn(List.of(r1, r2));
        when(reportMapper.toDTO(r1)).thenReturn(dto1);
        when(reportMapper.toDTO(r2)).thenReturn(dto2);

        List<ReportDTO> result = reportService.getAllReports();

        assertEquals(2, result.size());
        assertEquals("Report 1", result.get(0).getTitle());
        assertEquals("Report 2", result.get(1).getTitle());
    }

    @Test
    void testShouldThrowExceptionWhenNoReportsFound() {
        when(reportRepository.findAll()).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, () -> reportService.getAllReports());
    }

    @Test
    void testShouldGetReportById() {
        Report report = new Report();
        report.setId("1");
        report.setTitle("Test Report");

        ReportDTO dto = new ReportDTO();
        dto.setTitle("Test Report");

        when(reportRepository.findById("1")).thenReturn(Optional.of(report));
        when(reportMapper.toDTO(report)).thenReturn(dto);

        ReportDTO result = reportService.getReportById("1");

        assertNotNull(result);
        assertEquals("Test Report", result.getTitle());
    }

    @Test
    void testShouldThrowExceptionWhenReportNotFound() {
        when(reportRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> reportService.getReportById("99"));
    }

    @Test
    void testShouldGetDecoratedReportById() {
        Report report = new Report();
        report.setId("1");
        report.setTitle("Test Report");

        ReportDTO decoratorConfig = new ReportDTO();
        ReportComponent mockComponent = mock(ReportComponent.class);
        ReportResponseDTO expectedResponse = new ReportResponseDTO();
        expectedResponse.setTitle("Test Report");

        when(reportRepository.findById("1")).thenReturn(Optional.of(report));
        when(decoratorFactory.createDecoratedReport(report, decoratorConfig)).thenReturn(mockComponent);
        when(mockComponent.generate()).thenReturn(expectedResponse);

        ReportResponseDTO result = reportService.getDecoratedReportById("1", decoratorConfig);

        assertNotNull(result);
        assertEquals("Test Report", result.getTitle());
    }

    @Test
    void testShouldGetReportsByDate() {
        LocalDate testDate = LocalDate.of(2023, 1, 1);
        Report report = new Report();
        report.setId("1");
        report.setTitle("Test Report");
        report.setDate(testDate);

        ReportDTO dto = new ReportDTO();
        dto.setTitle("Test Report");

        when(reportRepository.findAll()).thenReturn(List.of(report));
        when(reportMapper.toDTO(report)).thenReturn(dto);

        List<ReportDTO> result = reportService.getReportsByDate(testDate);

        assertEquals(1, result.size());
        assertEquals("Test Report", result.get(0).getTitle());
    }

    @Test
    void testShouldGetReportsByAuthor() {
        Report report = new Report();
        report.setId("1");
        report.setTitle("Test Report");
        report.setAuthor("Test Author");

        ReportDTO dto = new ReportDTO();
        dto.setTitle("Test Report");

        when(reportRepository.findAll()).thenReturn(List.of(report));
        when(reportMapper.toDTO(report)).thenReturn(dto);

        List<ReportDTO> result = reportService.getReportsByAuthor("Test Author");

        assertEquals(1, result.size());
        assertEquals("Test Report", result.get(0).getTitle());
    }

    @Test
    void testShouldUpdateReport() {
        Report existing = new Report();
        existing.setId("1");
        existing.setTitle("Old Title");

        ReportDTO dto = new ReportDTO();
        dto.setTitle("New Title");

        Report updated = new Report();
        updated.setId("1");
        updated.setTitle("New Title");

        ReportDTO updatedDTO = new ReportDTO();
        updatedDTO.setTitle("New Title");

        when(reportRepository.findById("1")).thenReturn(Optional.of(existing));
        when(reportRepository.save(existing)).thenReturn(updated);
        when(reportMapper.toDTO(updated)).thenReturn(updatedDTO);

        ReportDTO result = reportService.updateReport("1", dto);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        verify(reportMapper).updateEntity(existing, dto);
    }

    @Test
    void testShouldThrowExceptionWhenUpdatingNonExistentReport() {
        ReportDTO dto = new ReportDTO();
        when(reportRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> reportService.updateReport("99", dto));
    }

    @Test
    void testShouldDeleteReport() {
        when(reportRepository.existsById("1")).thenReturn(true);
        doNothing().when(reportRepository).deleteById("1");

        assertDoesNotThrow(() -> reportService.deleteReport("1"));
        verify(reportRepository).deleteById("1");
    }

    @Test
    void testShouldThrowExceptionWhenDeletingNonExistentReport() {
        when(reportRepository.existsById("99")).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> reportService.deleteReport("99"));
    }
}