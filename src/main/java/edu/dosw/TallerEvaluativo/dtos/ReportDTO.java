package edu.dosw.TallerEvaluativo.dtos;

import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportDTO {
    
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    private String content;
}