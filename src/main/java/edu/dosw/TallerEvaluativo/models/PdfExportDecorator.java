package edu.dosw.TallerEvaluativo.models;

import edu.dosw.TallerEvaluativo.dtos.ReportResponseDTO;
import edu.dosw.TallerEvaluativo.dtos.TransactionDTO;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

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
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("REPORTE FINANCIERO"));
            document.add(new Paragraph("==================\n"));
            document.add(new Paragraph("Título: " + response.getTitle()));
            document.add(new Paragraph("Autor: " + response.getAuthor()));
            document.add(new Paragraph("Fecha: " +
                    response.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            document.add(new Paragraph("\nContenido:\n" + response.getContent()));

            if (response.getTransactions() != null && !response.getTransactions().isEmpty()) {
                document.add(new Paragraph("\nTransacciones:"));
                for (TransactionDTO t : response.getTransactions()) {
                    document.add(new Paragraph("• " + t.getDescription() + " - $" + t.getAmount()));
                }
            }

            if (response.getHasStatistics() && response.getStatistics() != null) {
                document.add(new Paragraph("\nEstadísticas:"));
                response.getStatistics().forEach((k, v) ->
                        document.add(new Paragraph(k + ": " + v))
                );
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
