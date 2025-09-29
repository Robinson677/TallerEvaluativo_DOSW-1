package edu.dosw.TallerEvaluativo.TallerEvaluativo_DOSW_1;


import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.dosw.TallerEvaluativo.models.BaseReportRecourse;
import edu.dosw.TallerEvaluativo.models.Report;
import edu.dosw.TallerEvaluativo.models.Transaction;

class BaseReportRecourseTest {

    private Report report;
    private BaseReportRecourse recourse;

    @BeforeEach
    void setup() {
        report = new Report();
        report.setId("1");
        report.setTitle("Financial Report");
        report.setAuthor("John Doe");
        report.setContent("This is the content of the report");
        report.setDate(LocalDate.of(2023, 6, 15));
        report.setTransactions(new ArrayList<>());
    }

    @Test
    void testShouldCreateBaseReportRecourse() {
        recourse = new BaseReportRecourse(report);

        assertNotNull(recourse);
        assertEquals(report, recourse.getReport());
    }

    @Test
    void testShouldThrowExceptionWhenReportIsNull() {
        assertThrows(NullPointerException.class, () -> new BaseReportRecourse(null));
    }

    @Test
    void testShouldRenderHtmlWithBasicReport() {
        recourse = new BaseReportRecourse(report);

        String html = recourse.renderHtml();

        assertNotNull(html);
        assertTrue(html.contains("<html><body>"));
        assertTrue(html.contains("<h1>Financial Report</h1>"));
        assertTrue(html.contains("<strong>Autor:</strong> John Doe"));
        assertTrue(html.contains("<strong>Fecha:</strong> 2023-06-15"));
        assertTrue(html.contains("</body></html>"));
    }

    @Test
    void testShouldRenderHtmlWithTransactions() {
        Transaction t1 = new Transaction();
        t1.setId("t1");
        t1.setDescription("Payment");
        t1.setAmount(new BigDecimal("100.00"));

        Transaction t2 = new Transaction();
        t2.setId("t2");
        t2.setDescription("Refund");
        t2.setAmount(new BigDecimal("50.00"));

        report.setTransactions(Arrays.asList(t1, t2));
        recourse = new BaseReportRecourse(report);

        String html = recourse.renderHtml();

        assertTrue(html.contains("<h3>Transacciones</h3>"));
        assertTrue(html.contains("<ul>"));
        assertTrue(html.contains("<li>"));
        assertTrue(html.contains("</ul>"));
    }

    @Test
    void testShouldRenderHtmlWithNullTitle() {
        report.setTitle(null);
        recourse = new BaseReportRecourse(report);

        String html = recourse.renderHtml();

        assertTrue(html.contains("<h1></h1>"));
    }

    @Test
    void testShouldRenderHtmlWithNullAuthor() {
        report.setAuthor(null);
        recourse = new BaseReportRecourse(report);

        String html = recourse.renderHtml();

        assertTrue(html.contains("<strong>Autor:</strong> </p>"));
    }

    @Test
    void testShouldRenderHtmlWithNullDate() {
        report.setDate(null);
        recourse = new BaseReportRecourse(report);

        String html = recourse.renderHtml();

        assertTrue(html.contains("<strong>Fecha:</strong> </p>"));
    }

    @Test
    void testShouldRenderHtmlWithNullContent() {
        report.setContent(null);
        recourse = new BaseReportRecourse(report);

        String html = recourse.renderHtml();

        assertNotNull(html);
        assertFalse(html.contains("<div>"));
    }

    @Test
    void testShouldRenderHtmlWithNullTransactions() {
        report.setTransactions(null);
        recourse = new BaseReportRecourse(report);

        String html = recourse.renderHtml();

        assertTrue(html.contains("<h3>Transacciones</h3>"));
        assertTrue(html.contains("<ul></ul>"));
    }

    @Test
    void testShouldRenderHtmlWithEmptyTransactions() {
        report.setTransactions(new ArrayList<>());
        recourse = new BaseReportRecourse(report);

        String html = recourse.renderHtml();

        assertTrue(html.contains("<ul></ul>"));
    }

    @Test
    void testShouldRenderPdfAsBytes() {
        recourse = new BaseReportRecourse(report);

        byte[] pdf = recourse.renderPdf();

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }

    @Test
    void testShouldRenderPdfContainsHtmlContent() {
        recourse = new BaseReportRecourse(report);

        byte[] pdf = recourse.renderPdf();
        String pdfContent = new String(pdf);

        assertTrue(pdfContent.contains("Financial Report"));
        assertTrue(pdfContent.contains("John Doe"));
    }

    @Test
    void testShouldRenderExcelAsBytes() {
        recourse = new BaseReportRecourse(report);

        byte[] excel = recourse.renderExcel();

        assertNotNull(excel);
        assertTrue(excel.length > 0);
    }

    @Test
    void testShouldRenderExcelWithReportInfo() {
        recourse = new BaseReportRecourse(report);

        byte[] excel = recourse.renderExcel();
        String content = new String(excel);

        assertTrue(content.contains("Report: Financial Report"));
        assertTrue(content.contains("Author: John Doe"));
        assertTrue(content.contains("Transactions: 0"));
    }

    @Test
    void testShouldRenderExcelWithTransactionCount() {
        Transaction t1 = new Transaction();
        t1.setId("t1");
        t1.setDescription("Payment");

        report.setTransactions(Arrays.asList(t1));
        recourse = new BaseReportRecourse(report);

        byte[] excel = recourse.renderExcel();
        String content = new String(excel);

        assertTrue(content.contains("Transactions: 1"));
    }

    @Test
    void testShouldRenderExcelWithNullTitle() {
        report.setTitle(null);
        recourse = new BaseReportRecourse(report);

        byte[] excel = recourse.renderExcel();
        String content = new String(excel);

        assertTrue(content.contains("Report: "));
    }

    @Test
    void testShouldRenderExcelWithNullAuthor() {
        report.setAuthor(null);
        recourse = new BaseReportRecourse(report);

        byte[] excel = recourse.renderExcel();
        String content = new String(excel);

        assertTrue(content.contains("Author: "));
    }

    @Test
    void testShouldRenderExcelWithNullTransactions() {
        report.setTransactions(null);
        recourse = new BaseReportRecourse(report);

        byte[] excel = recourse.renderExcel();
        String content = new String(excel);

        assertTrue(content.contains("Transactions: 0"));
    }

    @Test
    void testShouldGetReport() {
        recourse = new BaseReportRecourse(report);

        Report retrievedReport = recourse.getReport();

        assertNotNull(retrievedReport);
        assertEquals(report, retrievedReport);
        assertEquals("Financial Report", retrievedReport.getTitle());
        assertEquals("John Doe", retrievedReport.getAuthor());
    }

    @Test
    void testShouldRenderHtmlWithContent() {
        report.setContent("Detailed financial analysis");
        recourse = new BaseReportRecourse(report);

        String html = recourse.renderHtml();

        assertTrue(html.contains("<div>Detailed financial analysis</div>"));
    }

    @Test
    void testShouldRenderCompleteHtmlStructure() {
        Transaction t1 = new Transaction();
        t1.setId("t1");
        t1.setDescription("Payment");
        report.setTransactions(Arrays.asList(t1));
        recourse = new BaseReportRecourse(report);

        String html = recourse.renderHtml();

        assertTrue(html.startsWith("<html><body>"));
        assertTrue(html.endsWith("</body></html>"));
        assertTrue(html.contains("<h1>"));
        assertTrue(html.contains("<h3>Transacciones</h3>"));
    }
}