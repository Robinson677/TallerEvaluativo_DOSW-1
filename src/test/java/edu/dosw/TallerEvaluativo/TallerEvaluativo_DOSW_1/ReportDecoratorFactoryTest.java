package edu.dosw.TallerEvaluativo.TallerEvaluativo_DOSW_1;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dosw.TallerEvaluativo.dtos.ReportDTO;
import edu.dosw.TallerEvaluativo.enums.DecoratorType;
import edu.dosw.TallerEvaluativo.models.*;
import edu.dosw.TallerEvaluativo.services.ReportDecoratorFactory;

class ReportDecoratorFactoryTest {

    private ObjectMapper objectMapper;
    private TransactionMapper transactionMapper;
    private ReportDecoratorFactory factory;
    private Report report;
    private ReportDTO requestDTO;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        transactionMapper = mock(TransactionMapper.class);
        factory = new ReportDecoratorFactory(objectMapper, transactionMapper);
        
        report = new Report();
        report.setId("1");
        report.setTitle("Test Report");
        report.setAuthor("Test Author");
        report.setContent("Test Content");
        report.setTransactions(new ArrayList<>());
        
        requestDTO = new ReportDTO();
        requestDTO.setDecorators(new HashSet<>());
    }

    @Test
    void testShouldCreateBaseReportComponentWithoutDecorators() {
        requestDTO.setDecorators(null);

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof BaseReportComponent);
    }

    @Test
    void testShouldCreateBaseReportComponentWithEmptyDecorators() {
        requestDTO.setDecorators(new HashSet<>());

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof BaseReportComponent);
    }

    @Test
    void testShouldApplyChartsDecorator() {
        requestDTO.getDecorators().add(DecoratorType.CHARTS);

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof GraphDecorator);
    }

    @Test
    void testShouldApplyWatermarkDecorator() {
        requestDTO.getDecorators().add(DecoratorType.WATERMARK);
        requestDTO.setWatermarkText("CONFIDENTIAL");

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof WatermarkSecurityDecorator);
    }

    @Test
    void testShouldApplyStatisticsDecorator() {
        requestDTO.getDecorators().add(DecoratorType.STATISTICS);

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof SummaryDecorator);
    }

    @Test
    void testShouldApplyPdfExportDecorator() {
        requestDTO.getDecorators().add(DecoratorType.EXPORT_PDF);

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof PdfExportDecorator);
    }

    @Test
    void testShouldApplyExcelExportDecorator() {
        requestDTO.getDecorators().add(DecoratorType.EXPORT_EXCEL);

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof ExcelExportDecorator);
    }



    @Test
    void testShouldApplyAllDecoratorsInOrder() {
        requestDTO.getDecorators().add(DecoratorType.STATISTICS);
        requestDTO.getDecorators().add(DecoratorType.CHARTS);
        requestDTO.getDecorators().add(DecoratorType.WATERMARK);
        requestDTO.setWatermarkText("TOP SECRET");
        requestDTO.getDecorators().add(DecoratorType.EXPORT_PDF);

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof PdfExportDecorator);
    }

    @Test
    void testShouldApplyChartsAndExportPdf() {
        requestDTO.getDecorators().add(DecoratorType.CHARTS);
        requestDTO.getDecorators().add(DecoratorType.EXPORT_PDF);

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof PdfExportDecorator);
    }


    @Test
    void testShouldApplyWatermarkWithText() {
        requestDTO.getDecorators().add(DecoratorType.WATERMARK);
        requestDTO.setWatermarkText("DRAFT");

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof WatermarkSecurityDecorator);
    }

    @Test
    void testShouldApplyWatermarkWithNullText() {
        requestDTO.getDecorators().add(DecoratorType.WATERMARK);
        requestDTO.setWatermarkText(null);

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof WatermarkSecurityDecorator);
    }

    @Test
    void testShouldCreateDecoratedReportWithComplexScenario() {
        requestDTO.getDecorators().add(DecoratorType.STATISTICS);
        requestDTO.getDecorators().add(DecoratorType.CHARTS);
        requestDTO.getDecorators().add(DecoratorType.WATERMARK);
        requestDTO.setWatermarkText("CONFIDENTIAL - DO NOT DISTRIBUTE");
        requestDTO.getDecorators().add(DecoratorType.EXPORT_PDF);

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof PdfExportDecorator);
    }

    @Test
    void testShouldHandleReportWithNullTransactions() {
        report.setTransactions(null);
        requestDTO.getDecorators().add(DecoratorType.STATISTICS);

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof SummaryDecorator);
    }

    @Test
    void testShouldApplyDecoratorsWithDifferentReportData() {
        Report customReport = new Report();
        customReport.setId("2");
        customReport.setTitle("Custom Report");
        customReport.setAuthor("Custom Author");
        customReport.setContent("Custom Content");
        customReport.setTransactions(new ArrayList<>());

        requestDTO.getDecorators().add(DecoratorType.CHARTS);
        requestDTO.getDecorators().add(DecoratorType.STATISTICS);

        ReportComponent component = factory.createDecoratedReport(customReport, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof SummaryDecorator);
    }

    @Test
    void testShouldApplySingleChartDecorator() {
        requestDTO.getDecorators().add(DecoratorType.CHARTS);

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof GraphDecorator);
    }

    @Test
    void testShouldApplySingleStatisticsDecorator() {
        requestDTO.getDecorators().add(DecoratorType.STATISTICS);

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof SummaryDecorator);
    }

    @Test
    void testShouldApplyExportDecoratorsSequentially() {
        requestDTO.getDecorators().add(DecoratorType.EXPORT_EXCEL);
        requestDTO.getDecorators().add(DecoratorType.EXPORT_PDF);

        ReportComponent component = factory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof PdfExportDecorator);
    }

    @Test
    void testShouldVerifyObjectMapperIsUsedForChartsDecorator() {
        ObjectMapper customMapper = mock(ObjectMapper.class);
        ReportDecoratorFactory customFactory = new ReportDecoratorFactory(customMapper, transactionMapper);
        
        requestDTO.getDecorators().add(DecoratorType.CHARTS);

        ReportComponent component = customFactory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof GraphDecorator);
    }

    @Test
    void testShouldVerifyTransactionMapperIsUsedForBaseComponent() {
        TransactionMapper customMapper = mock(TransactionMapper.class);
        ReportDecoratorFactory customFactory = new ReportDecoratorFactory(objectMapper, customMapper);
        
        ReportComponent component = customFactory.createDecoratedReport(report, requestDTO);

        assertNotNull(component);
        assertTrue(component instanceof BaseReportComponent);
    }
}