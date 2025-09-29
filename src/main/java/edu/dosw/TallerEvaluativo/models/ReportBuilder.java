package edu.dosw.TallerEvaluativo.models;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ReportBuilder {
    private Report report;

    public ReportBuilder title(String title) {
        report.setTitle(title); 
        return this; 
    }

    public ReportBuilder date(LocalDate date) {
        report.setDate(date); 
        return this; 
    }

    public ReportBuilder author(String author) {
        report.setAuthor(author);
        return this; 
    }

    public ReportBuilder transactions(List<Transaction> transactions) {
        report.setTransactions(transactions);
        return this; 
    }

    public ReportBuilder content(String content) {
        report.setContent(content); 
        return this; 
    }

    public Report build() { 
        return report; 
    }
}