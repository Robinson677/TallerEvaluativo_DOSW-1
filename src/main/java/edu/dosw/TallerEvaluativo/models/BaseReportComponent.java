package edu.dosw.TallerEvaluativo.models;

import edu.dosw.TallerEvaluativo.dtos.ReportResponseDTO;
import edu.dosw.TallerEvaluativo.dtos.TransactionDTO;
import edu.dosw.TallerEvaluativo.models.Report;
import edu.dosw.TallerEvaluativo.models.TransactionMapper;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
                    .collect(Collectors.toList());
            response.setTransactions(transactionDTOs);
        }

        response.setHasCharts(false);
        response.setHasWatermark(false);
        response.setHasStatistics(false);

        return response;
    }
}