package edu.dosw.TallerEvaluativo.services;

import edu.dosw.TallerEvaluativo.models.*;
import edu.dosw.TallerEvaluativo.dtos.ReportDTO;
import edu.dosw.TallerEvaluativo.enums.DecoratorType;
import edu.dosw.TallerEvaluativo.models.Report;
import edu.dosw.TallerEvaluativo.models.TransactionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ReportDecoratorFactory {

    private final ObjectMapper objectMapper;
    private final TransactionMapper transactionMapper;

    public ReportDecoratorFactory(ObjectMapper objectMapper, TransactionMapper transactionMapper) {
        this.objectMapper = objectMapper;
        this.transactionMapper = transactionMapper;
    }

    public ReportComponent createDecoratedReport(Report report, ReportDTO request) {
        ReportComponent component = new BaseReportComponent(report, transactionMapper);

        if (request.getDecorators() != null) {
            for (DecoratorType decoratorType : request.getDecorators()) {
                component = applyDecorator(component, decoratorType, request);
            }
        }

        return component;
    }

    private ReportComponent applyDecorator(ReportComponent component,
                                           DecoratorType type,
                                           ReportDTO request) {
        return switch (type) {
            case CHARTS -> new GraphDecorator(component, objectMapper);
            case WATERMARK -> new WatermarkSecurityDecorator(component, request.getWatermarkText());
            case STATISTICS -> new SummaryDecorator(component);
            case EXPORT_PDF -> new PdfExportDecorator(component);
            case EXPORT_EXCEL -> new ExcelExportDecorator(component);
        };
    }
}