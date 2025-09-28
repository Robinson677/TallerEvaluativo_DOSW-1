package edu.dosw.TallerEvaluativo.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    private String id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
}

