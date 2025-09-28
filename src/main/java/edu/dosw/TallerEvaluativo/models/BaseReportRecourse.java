package edu.dosw.TallerEvaluativo.models;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Permite renderizar un reporte
 */
public class BaseReportRecourse implements IReportRecourse {

    private final Report report;

    public BaseReportRecourse(Report report) {
        this.report = Objects.requireNonNull(report, "report no puede ser null");
    }

    public Report getReport() {
        return report;
    }

    @Override
    public String renderHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h1>").append(report.getTitle() == null ? "" : report.getTitle()).append("</h1>");
        sb.append("<p><strong>Autor:</strong> ").append(report.getAuthor() == null ? "" : report.getAuthor()).append("</p>");
        sb.append("<p><strong>Fecha:</strong> ").append(report.getDate() == null ? "" : report.getDate().toString()).append("</p>");
        sb.append("<h3>Transacciones</h3>");
        sb.append("<ul>");
        if (report.getTransactions() != null) {
            report.getTransactions().forEach(t -> sb.append("<li>").append(t == null ? "null" : t.toString()).append("</li>"));
        }
        sb.append("</ul>");
        if (report.getContent() != null) {
            sb.append("<div>").append(report.getContent()).append("</div>");
        }
        sb.append("</body></html>");
        return sb.toString();
    }

    @Override
    public byte[] renderPdf() {
        return renderHtml().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] renderExcel() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reporte: ").append(report.getTitle() == null ? "" : report.getTitle()).append("\n");
        sb.append("Autor: ").append(report.getAuthor() == null ? "" : report.getAuthor()).append("\n");
        sb.append("Transacciones: ").append(report.getTransactions() == null ? 0 : report.getTransactions().size()).append("\n");
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
