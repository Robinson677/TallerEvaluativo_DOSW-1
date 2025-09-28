package edu.dosw.TallerEvaluativo.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import edu.dosw.TallerEvaluativo.models.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByAmount(BigDecimal amount);
}
