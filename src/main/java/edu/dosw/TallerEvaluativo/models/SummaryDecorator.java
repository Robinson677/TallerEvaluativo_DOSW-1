package edu.dosw.TallerEvaluativo.models;

import edu.dosw.TallerEvaluativo.dtos.ReportResponseDTO;
import edu.dosw.TallerEvaluativo.dtos.TransactionDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryDecorator extends ReportDecorator {

    public SummaryDecorator(ReportComponent component) {
        super(component);
    }

    @Override
    public ReportResponseDTO generate() {
        ReportResponseDTO response = super.generate();

        if (response.getTransactions() != null && !response.getTransactions().isEmpty()) {
            Map<String, Object> statistics = calculateStatistics(response.getTransactions());
            response.setStatistics(statistics);
            response.setHasStatistics(true);
        }

        return response;
    }

    private Map<String, Object> calculateStatistics(List<TransactionDTO> transactions) {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalTransactions", transactions.size());

        BigDecimal total = transactions.stream()
                .map(TransactionDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalAmount", total);

        BigDecimal average = total.divide(
                new BigDecimal(transactions.size()),
                2,
                RoundingMode.HALF_UP
        );
        stats.put("averageAmount", average);

        BigDecimal max = transactions.stream()
                .map(TransactionDTO::getAmount)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        stats.put("maxAmount", max);

        BigDecimal min = transactions.stream()
                .map(TransactionDTO::getAmount)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        stats.put("minAmount", min);

        long positiveTransactions = transactions.stream()
                .mapToLong(t -> t.getAmount().compareTo(BigDecimal.ZERO) >= 0 ? 1 : 0)
                .sum();
        stats.put("positiveTransactions", positiveTransactions);
        stats.put("negativeTransactions", transactions.size() - positiveTransactions);

        return stats;
    }
}
