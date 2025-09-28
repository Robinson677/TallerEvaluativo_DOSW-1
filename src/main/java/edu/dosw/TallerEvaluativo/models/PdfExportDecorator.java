package edu.dosw.TallerEvaluativo.models;

import edu.dosw.TallerEvaluativo.dtos.ReportResponseDTO;
import edu.dosw.TallerEvaluativo.dtos.TransactionDTO;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

public class PdfExportDecorator extends ReportDecorator {

    public PdfExportDecorator(ReportComponent component) {
        super(component);
    }

    @Override
    public ReportResponseDTO generate() {
        ReportResponseDTO response = super.generate();

        byte[] pdfContent = generatePdfContent(response);
        response.setExportedFile(pdfContent);
        response.setExportFormat("PDF");

        return response;
    }

    private byte[] generatePdfContent(ReportResponseDTO response) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            StringBuilder pdfText = new StringBuilder();

            pdfText.append("REPORTE FINANCIERO\n");
            pdfText.append("==================\n\n");
            pdfText.append("Título: ").append(response.getTitle()).append("\n");
            pdfText.append("Autor: ").append(response.getAuthor()).append("\n");
            pdfText.append("Fecha: ").append(response.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n\n");

            pdfText.append("CONTENIDO:\n");
            pdfText.append(response.getContent()).append("\n\n");

            if (response.getTransactions() != null && !response.getTransactions().isEmpty()) {
                pdfText.append("TRANSACCIONES:\n");
                pdfText.append("--------------\n");
                for (TransactionDTO transaction : response.getTransactions()) {
                    pdfText.append("• ").append(transaction.getDescription())
                            .append(" - $").append(transaction.getAmount()).append("\n");
                }
                pdfText.append("\n");
            }

            if (response.getHasStatistics() && response.getStatistics() != null) {
                pdfText.append("ESTADÍSTICAS:\n");
                pdfText.append("-------------\n");
                response.getStatistics().forEach((key, value) ->
                        pdfText.append(key).append(": ").append(value).append("\n"));
            }

            return pdfText.toString().getBytes(StandardCharsets.UTF_8);

        } catch (Exception e) {
            return new byte[0];
        }
    }
}
