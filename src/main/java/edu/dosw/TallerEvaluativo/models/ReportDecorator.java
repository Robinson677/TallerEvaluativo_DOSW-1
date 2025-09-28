package edu.dosw.TallerEvaluativo.models;

import edu.dosw.TallerEvaluativo.dtos.ReportResponseDTO;
import edu.dosw.TallerEvaluativo.enums.DecoratorType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class ReportDecorator implements ReportComponent {

    protected ReportComponent component;

    @Override
    public ReportResponseDTO generate() {
        return component.generate();
    }
}
