package edu.dosw.TallerEvaluativo.models;

import edu.dosw.TallerEvaluativo.dtos.ReportResponseDTO;
import edu.dosw.TallerEvaluativo.dtos.TransactionDTO;

import java.nio.charset.StandardCharsets;

public class ExcelExportDecorator extends ReportDecorator {

    public ExcelExportDecorator(ReportComponent component) {
        super(component);
    }

    @Override
    public ReportResponseDTO generate() {
        ReportResponseDTO response = super.generate();

        byte[] excelContent = generateExcelContent(response);
        response.setExportedFile(excelContent);
        response.setExportFormat("EXCEL");

        return response;
    }

    private byte[] generateExcelContent(ReportResponseDTO response) {
        try {
            StringBuilder csvContent = new StringBuilder();

            csvContent.append("Reporte Financiero\n");
            csvContent.append("Título,").append(response.getTitle()).append("\n");
            csvContent.append("Autor,").append(response.getAuthor()).append("\n");
            csvContent.append("Fecha,").append(response.getDate()).append("\n\n");

            if (response.getTransactions() != null && !response.getTransactions().isEmpty()) {
                csvContent.append("Transacciones\n");
                csvContent.append("Descripción,Monto\n");

                for (TransactionDTO transaction : response.getTransactions()) {
                    csvContent.append(transaction.getDescription())
                            .append(",")
                            .append(transaction.getAmount())
                            .append("\n");
                }
            }

            return csvContent.toString().getBytes(StandardCharsets.UTF_8);

        } catch (Exception e) {
            return new byte[0];
        }
    }
}
