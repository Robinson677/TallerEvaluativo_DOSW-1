package edu.dosw.TallerEvaluativo.TallerEvaluativo_DOSW_1;


import edu.dosw.TallerEvaluativo.controllers.ReportController;
import edu.dosw.TallerEvaluativo.dtos.ReportDTO;
import edu.dosw.TallerEvaluativo.dtos.ReportResponseDTO;
import edu.dosw.TallerEvaluativo.services.ReportService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    private ReportDTO reportDTO;
    private ReportResponseDTO reportResponseDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        reportDTO = new ReportDTO();
        reportDTO.setTitle("Test Report");
        reportDTO.setAuthor("Test Author");
        reportDTO.setContent("Test Content");

        reportResponseDTO = new ReportResponseDTO();
        reportResponseDTO.setTitle("Test Report");
        reportResponseDTO.setAuthor("Test Author");
        reportResponseDTO.setContent("Test Content");
    }

    @Test
    void testCreateReport() {
        when(reportService.createReport(reportDTO)).thenReturn(reportDTO);

        ReportDTO result = reportController.createReport(reportDTO);

        assertNotNull(result);
        assertEquals("Test Report", result.getTitle());
        assertEquals("Test Author", result.getAuthor());
        verify(reportService).createReport(reportDTO);
    }

    @Test
    void testGetAllReports() {
        ReportDTO report2 = new ReportDTO();
        report2.setTitle("Second Report");
        report2.setAuthor("Second Author");

        when(reportService.getAllReports()).thenReturn(Arrays.asList(reportDTO, report2));

        List<ReportDTO> result = reportController.getAllReports();

        assertEquals(2, result.size());
        assertEquals("Test Report", result.get(0).getTitle());
        assertEquals("Second Report", result.get(1).getTitle());
    }

    @Test
    void testGetReportById() {
        when(reportService.getReportById("1")).thenReturn(reportDTO);

        ReportDTO result = reportController.getReportById("1");

        assertNotNull(result);
        assertEquals("Test Report", result.getTitle());
        assertEquals("Test Author", result.getAuthor());
    }

    @Test
    void testGetReportsByDate() {
        LocalDate testDate = LocalDate.of(2023, 1, 1);
        when(reportService.getReportsByDate(testDate)).thenReturn(Arrays.asList(reportDTO));

        List<ReportDTO> result = reportController.getReportsByDate(testDate);

        assertEquals(1, result.size());
        assertEquals("Test Report", result.get(0).getTitle());
    }

    @Test
    void testGetReportsByAuthor() {
        when(reportService.getReportsByAuthor("Test Author")).thenReturn(Arrays.asList(reportDTO));

        List<ReportDTO> result = reportController.getReportsByAuthor("Test Author");

        assertEquals(1, result.size());
        assertEquals("Test Report", result.get(0).getTitle());
        assertEquals("Test Author", result.get(0).getAuthor());
    }

    @Test
    void testUpdateReport() {
        ReportDTO updatedDTO = new ReportDTO();
        updatedDTO.setTitle("Updated Report");
        updatedDTO.setAuthor("Test Author");

        when(reportService.updateReport("1", reportDTO)).thenReturn(updatedDTO);

        ReportDTO result = reportController.updateReport("1", reportDTO);

        assertNotNull(result);
        assertEquals("Updated Report", result.getTitle());
        assertEquals("Test Author", result.getAuthor());
    }

    @Test
    void testDeleteReport() {
        doNothing().when(reportService).deleteReport("1");

        reportController.deleteReport("1");

        verify(reportService).deleteReport("1");
    }

    @Test
    void testCreateDecoratedReport() {
        when(reportService.createDecoratedReport(reportDTO)).thenReturn(reportResponseDTO);

        ReportResponseDTO result = reportController.createDecoratedReport(reportDTO);

        assertNotNull(result);
        assertEquals("Test Report", result.getTitle());
        assertEquals("Test Author", result.getAuthor());
        verify(reportService).createDecoratedReport(reportDTO);
    }

    @Test
    void testGetDecoratedReport() {
        ReportDTO decoratorConfig = new ReportDTO();
        when(reportService.getDecoratedReportById("1", decoratorConfig)).thenReturn(reportResponseDTO);

        ReportResponseDTO result = reportController.getDecoratedReport("1", decoratorConfig);

        assertNotNull(result);
        assertEquals("Test Report", result.getTitle());
        assertEquals("Test Author", result.getAuthor());
        verify(reportService).getDecoratedReportById("1", decoratorConfig);
    }

    @Test
    void testExportReportWithPDF() {
        ReportDTO exportConfig = new ReportDTO();
        ReportResponseDTO response = new ReportResponseDTO();
        response.setTitle("Test Report");
        response.setExportFormat("PDF");
        response.setExportedFile("test pdf content".getBytes());

        when(reportService.getDecoratedReportById("1", exportConfig)).thenReturn(response);

        ResponseEntity<byte[]> result = reportController.exportReport("1", exportConfig);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("application/pdf", result.getHeaders().getContentType().toString());
    }

    @Test
    void testExportReportWithExcel() {
        ReportDTO exportConfig = new ReportDTO();
        ReportResponseDTO response = new ReportResponseDTO();
        response.setTitle("Test Report");
        response.setExportFormat("EXCEL");
        response.setExportedFile("test excel content".getBytes());

        when(reportService.getDecoratedReportById("1", exportConfig)).thenReturn(response);

        ResponseEntity<byte[]> result = reportController.exportReport("1", exportConfig);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("application/vnd.ms-excel", result.getHeaders().getContentType().toString());
    }

    @Test
    void testExportReportNotFound() {
        ReportDTO exportConfig = new ReportDTO();
        ReportResponseDTO response = new ReportResponseDTO();
        response.setTitle("Test Report");
        response.setExportedFile(null);

        when(reportService.getDecoratedReportById("1", exportConfig)).thenReturn(response);

        ResponseEntity<byte[]> result = reportController.exportReport("1", exportConfig);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }



}