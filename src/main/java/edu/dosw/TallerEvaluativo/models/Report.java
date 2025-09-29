package edu.dosw.TallerEvaluativo.models;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "reportes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    private String id;
    private String title;
    private LocalDate date;
    private String author;
    private List<Transaction> transactions;
    private String content;
}