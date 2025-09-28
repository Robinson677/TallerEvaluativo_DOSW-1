package edu.dosw.TallerEvaluativo.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ReportBuilder {
    private final List<Consumer<Report>> report = new ArrayList<>();


    public ReportBuilder id(String id) {
        report.add(r -> r.setId(id));
        return this;
    }

    public ReportBuilder title(String title) {
        report.add(r -> r.setTitle(title));
        return this;
    }

    public ReportBuilder date(LocalDate date) {
        report.add(r -> r.setDate(date));
        return this;
    }

    public ReportBuilder author(String author) {
        report.add(r -> r.setAuthor(author));
        return this;
    }

    public ReportBuilder transactions(List<Transaction> transactions) {
        report.add(r -> r.setTransactions(Optional.ofNullable(transactions).orElse(List.of())));
        return this;
    }

    public ReportBuilder content(String content) {
        report.add(r -> r.setContent(content));
        return this;
    }

    public Report build() {
        Report r = new Report();
        report.stream().forEach(step -> step.accept(r));
        return r;
    }
}