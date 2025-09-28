package edu.dosw.TallerEvaluativo.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDTO {

    private String description;
    private BigDecimal amount;    
}
