package edu.dosw.TallerEvaluativo.models;

/**
 * Interfaz para los recurso de los reportes
 */
public interface IReportRecourse {
    String renderHtml();
    byte[] renderPdf();
    byte[] renderExcel();
}