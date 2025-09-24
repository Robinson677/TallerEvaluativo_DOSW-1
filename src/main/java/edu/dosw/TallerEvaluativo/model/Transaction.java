package edu.dosw.TallerEvaluativo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {
	private String id;
	private String description;
	private BigDecimal amount; 
	private LocalDate date;
}
