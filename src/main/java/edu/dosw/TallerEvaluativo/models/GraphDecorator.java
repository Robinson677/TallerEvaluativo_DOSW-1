package edu.dosw.TallerEvaluativo.models;

import edu.dosw.TallerEvaluativo.dtos.ReportResponseDTO;
import edu.dosw.TallerEvaluativo.dtos.TransactionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphDecorator extends ReportDecorator {

    private final ObjectMapper objectMapper;

    public GraphDecorator(ReportComponent component, ObjectMapper objectMapper) {
        super(component);
        this.objectMapper = objectMapper;
    }

    @Override
    public ReportResponseDTO generate() {
        ReportResponseDTO response = super.generate();

        if (response.getTransactions() != null && !response.getTransactions().isEmpty()) {
            String chartData = generateChartData(response.getTransactions());
            response.setChartData(chartData);
            response.setHasCharts(true);
        }

        return response;
    }

    private String generateChartData(List<TransactionDTO> transactions) {
        try {
            Map<String, Object> chartData = new HashMap<>();

            List<Map<String, Object>> barData = transactions.stream()
                    .map(transaction -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("description", transaction.getDescription());
                        item.put("amount", transaction.getAmount());
                        return item;
                    })
                    .collect(Collectors.toList());

            Map<String, Long> pieData = transactions.stream()
                    .collect(Collectors.groupingBy(
                            t -> t.getAmount().compareTo(BigDecimal.ZERO) >= 0 ? "Ingreso" : "Gasto",
                            Collectors.counting()
                    ));

            chartData.put("barChart", barData);
            chartData.put("pieChart", pieData);

            return objectMapper.writeValueAsString(chartData);
        } catch (Exception e) {
            return "{}";
        }
    }
}