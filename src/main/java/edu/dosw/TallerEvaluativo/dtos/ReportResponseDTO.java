package edu.dosw.TallerEvaluativo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDTO {

    private String id;
    private String title;
    private String author;
    private String content;
    private LocalDate date;
    private List<TransactionDTO> transactions;

    private Boolean hasCharts;
    private Boolean hasWatermark;
    private Boolean hasStatistics;
    private String exportFormat;
    private String chartData;
    private Map<String, Object> statistics;
    private String watermarkText;
    private byte[] exportedFile;
}