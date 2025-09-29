package edu.dosw.TallerEvaluativo.models;

import edu.dosw.TallerEvaluativo.dtos.ReportResponseDTO;

public class WatermarkSecurityDecorator extends ReportDecorator {

    private String watermarkText;

    public WatermarkSecurityDecorator(ReportComponent component, String watermarkText) {
        super(component);
        this.watermarkText = watermarkText != null ? watermarkText : "CONFIDENCIAL";
    }

    @Override
    public ReportResponseDTO generate() {
        ReportResponseDTO response = super.generate();

        String originalContent = response.getContent();
        String watermarkedContent = addWatermark(originalContent);

        response.setContent(watermarkedContent);
        response.setWatermarkText(watermarkText);
        response.setHasWatermark(true);

        return response;
    }

    private String addWatermark(String content) {
        StringBuilder watermarkedContent = new StringBuilder();
        watermarkedContent.append("=== ").append(watermarkText).append(" ===\n\n");
        watermarkedContent.append(content);
        watermarkedContent.append("\n\n=== ").append(watermarkText).append(" ===");

        return watermarkedContent.toString();
    }
}