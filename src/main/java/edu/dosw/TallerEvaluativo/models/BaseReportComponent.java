package edu.dosw.TallerEvaluativo.models;

import edu.dosw.TallerEvaluativo.dtos.ReportResponseDTO;
import edu.dosw.TallerEvaluativo.dtos.TransactionDTO;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BaseReportComponent implements ReportComponent {

    private Report report;
    private TransactionMapper transactionMapper;

    @Override
    public ReportResponseDTO generate() {
        ReportResponseDTO response = new ReportResponseDTO();
        response.setId(report.getId());
        response.setTitle(report.getTitle());
        response.setAuthor(report.getAuthor());
        response.setContent(report.getContent());
        response.setDate(report.getDate());

        if (report.getTransactions() != null) {
            List<TransactionDTO> transactionDTOs = report.getTransactions().stream()
                    .map(transactionMapper::toDTO)
                    .toList();
            response.setTransactions(transactionDTOs);
        }

        response.setHasCharts(false);
        response.setHasWatermark(false);
        response.setHasStatistics(false);

        return response;
    }
}