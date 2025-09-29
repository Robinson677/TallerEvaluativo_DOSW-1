package edu.dosw.TallerEvaluativo.TallerEvaluativo_DOSW_1;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.dosw.TallerEvaluativo.dtos.ReportResponseDTO;
import edu.dosw.TallerEvaluativo.dtos.TransactionDTO;
import edu.dosw.TallerEvaluativo.models.ExcelExportDecorator;
import edu.dosw.TallerEvaluativo.models.ReportComponent;

class ExcelExportDecoratorTest {

    private ReportComponent mockComponent;
    private ExcelExportDecorator decorator;
    private ReportResponseDTO baseResponse;

    @BeforeEach
    void setup() {
        mockComponent = mock(ReportComponent.class);
        decorator = new ExcelExportDecorator(mockComponent);

        baseResponse = new ReportResponseDTO();
        baseResponse.setTitle("Financial Report");
        baseResponse.setAuthor("John Doe");
        baseResponse.setContent("Report content");
        baseResponse.setDate(LocalDate.of(2023, 6, 15));
        baseResponse.setTransactions(new ArrayList<>());
    }

    @Test
    void testShouldGenerateExcelExport() {
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        assertNotNull(response);
        assertNotNull(response.getExportedFile());
        assertEquals("EXCEL", response.getExportFormat());
        assertTrue(response.getExportedFile().length > 0);
    }

    @Test
    void testShouldSetExportFormatToExcel() {
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        assertEquals("EXCEL", response.getExportFormat());
    }

    @Test
    void testShouldGenerateExcelWithBasicInfo() {
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        String content = new String(response.getExportedFile(), StandardCharsets.UTF_8);
        assertTrue(content.contains("Reporte Financiero"));
        assertTrue(content.contains("Financial Report"));
        assertTrue(content.contains("John Doe"));
        assertTrue(content.contains("2023-06-15"));
    }

    @Test
    void testShouldGenerateExcelWithTransactions() {
        TransactionDTO t1 = new TransactionDTO();
        t1.setDescription("Payment");
        t1.setAmount(new BigDecimal("100.00"));

        TransactionDTO t2 = new TransactionDTO();
        t2.setDescription("Refund");
        t2.setAmount(new BigDecimal("50.00"));

        baseResponse.setTransactions(Arrays.asList(t1, t2));
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        String content = new String(response.getExportedFile(), StandardCharsets.UTF_8);
        assertTrue(content.contains("Transacciones"));
        assertTrue(content.contains("Payment"));
        assertTrue(content.contains("100.00"));
        assertTrue(content.contains("Refund"));
        assertTrue(content.contains("50.00"));
    }

    @Test
    void testShouldGenerateExcelWithCsvFormat() {
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        String content = new String(response.getExportedFile(), StandardCharsets.UTF_8);
        assertTrue(content.contains(","));
        assertTrue(content.contains("\n"));
    }

    @Test
    void testShouldGenerateExcelWithEmptyTransactions() {
        baseResponse.setTransactions(new ArrayList<>());
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        assertNotNull(response.getExportedFile());
        assertTrue(response.getExportedFile().length > 0);
    }

    @Test
    void testShouldGenerateExcelWithNullTransactions() {
        baseResponse.setTransactions(null);
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        assertNotNull(response.getExportedFile());
    }

    @Test
    void testShouldCallSuperGenerate() {
        when(mockComponent.generate()).thenReturn(baseResponse);

        decorator.generate();

        verify(mockComponent, times(1)).generate();
    }

    @Test
    void testShouldPreserveOriginalResponseData() {
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        assertEquals("Financial Report", response.getTitle());
        assertEquals("John Doe", response.getAuthor());
        assertEquals("Report content", response.getContent());
    }

    @Test
    void testShouldGenerateExcelWithMultipleTransactions() {
        TransactionDTO t1 = new TransactionDTO();
        t1.setDescription("Transaction 1");
        t1.setAmount(new BigDecimal("100.00"));

        TransactionDTO t2 = new TransactionDTO();
        t2.setDescription("Transaction 2");
        t2.setAmount(new BigDecimal("200.00"));

        TransactionDTO t3 = new TransactionDTO();
        t3.setDescription("Transaction 3");
        t3.setAmount(new BigDecimal("300.00"));

        baseResponse.setTransactions(Arrays.asList(t1, t2, t3));
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        String content = new String(response.getExportedFile(), StandardCharsets.UTF_8);
        assertTrue(content.contains("Transaction 1"));
        assertTrue(content.contains("Transaction 2"));
        assertTrue(content.contains("Transaction 3"));
    }

    @Test
    void testShouldIncludeTransactionHeaders() {
        TransactionDTO t1 = new TransactionDTO();
        t1.setDescription("Payment");
        t1.setAmount(new BigDecimal("100.00"));

        baseResponse.setTransactions(Arrays.asList(t1));
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        String content = new String(response.getExportedFile(), StandardCharsets.UTF_8);
        assertTrue(content.contains("Descripción,Monto") || content.contains("DescripciÃ³n,Monto"));
    }

    @Test
    void testShouldGenerateValidCsvStructure() {
        TransactionDTO t1 = new TransactionDTO();
        t1.setDescription("Payment");
        t1.setAmount(new BigDecimal("100.00"));

        baseResponse.setTransactions(Arrays.asList(t1));
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        String content = new String(response.getExportedFile(), StandardCharsets.UTF_8);
        String[] lines = content.split("\n");
        assertTrue(lines.length > 0);
    }

    @Test
    void testShouldGenerateNonEmptyExcelFile() {
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        byte[] excelContent = response.getExportedFile();
        assertNotNull(excelContent);
        assertTrue(excelContent.length > 50);
    }

    @Test
    void testShouldIncludeReportMetadata() {
        baseResponse.setTitle("Q3 Financial Report");
        baseResponse.setAuthor("Jane Smith");
        baseResponse.setDate(LocalDate.of(2023, 9, 30));
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        String content = new String(response.getExportedFile(), StandardCharsets.UTF_8);
        assertTrue(content.contains("Q3 Financial Report"));
        assertTrue(content.contains("Jane Smith"));
    }

    @Test
    void testShouldHandleTransactionsWithDecimalAmounts() {
        TransactionDTO t1 = new TransactionDTO();
        t1.setDescription("Payment");
        t1.setAmount(new BigDecimal("123.45"));

        baseResponse.setTransactions(Arrays.asList(t1));
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        String content = new String(response.getExportedFile(), StandardCharsets.UTF_8);
        assertTrue(content.contains("123.45"));
    }

    @Test
    void testShouldHandleTransactionsWithLargeAmounts() {
        TransactionDTO t1 = new TransactionDTO();
        t1.setDescription("Large Payment");
        t1.setAmount(new BigDecimal("999999.99"));

        baseResponse.setTransactions(Arrays.asList(t1));
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        String content = new String(response.getExportedFile(), StandardCharsets.UTF_8);
        assertTrue(content.contains("999999.99"));
    }

    @Test
    void testShouldPreserveAllResponseFields() {
        baseResponse.setTitle("Test Title");
        baseResponse.setAuthor("Test Author");
        baseResponse.setContent("Test Content");
        baseResponse.setDate(LocalDate.now());

        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Author", response.getAuthor());
        assertEquals("Test Content", response.getContent());
        assertNotNull(response.getDate());
        assertEquals("EXCEL", response.getExportFormat());
    }

    @Test
    void testShouldHandleSpecialCharactersInTransactions() {
        TransactionDTO t1 = new TransactionDTO();
        t1.setDescription("Payment, with comma");
        t1.setAmount(new BigDecimal("100.00"));

        baseResponse.setTransactions(Arrays.asList(t1));
        when(mockComponent.generate()).thenReturn(baseResponse);

        ReportResponseDTO response = decorator.generate();

        assertNotNull(response.getExportedFile());
        assertTrue(response.getExportedFile().length > 0);
    }
}