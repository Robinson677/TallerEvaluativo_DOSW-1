package edu.dosw.TallerEvaluativo.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.dosw.TallerEvaluativo.models.Report;

public interface ReportRepository extends MongoRepository<Report, String> {
    List<Report> findByDate(LocalDate date);
    List<Report> findByAuthor(String author);
}
