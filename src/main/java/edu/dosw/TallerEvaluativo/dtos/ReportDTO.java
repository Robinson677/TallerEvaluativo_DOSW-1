package edu.dosw.TallerEvaluativo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import edu.dosw.TallerEvaluativo.enums.DecoratorType;
import java.util.Set;
import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {

    @NotBlank
    private String title;
    @NotBlank
    private String author;
    private String content;

    private Set<DecoratorType> decorators = new HashSet<>();

    private String watermarkText;
    private String exportFormat;
    private Boolean includeCharts = false;
    private Boolean includeStatistics = false;
}