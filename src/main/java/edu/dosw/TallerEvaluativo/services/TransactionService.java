package edu.dosw.TallerEvaluativo.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.dosw.TallerEvaluativo.dtos.TransactionDTO;
import edu.dosw.TallerEvaluativo.models.Transaction;
import edu.dosw.TallerEvaluativo.models.TransactionMapper;
import edu.dosw.TallerEvaluativo.repositories.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    public TransactionDTO createTransaction(TransactionDTO dto) {
        Transaction transaction = transactionMapper.toEntity(dto);
        transaction.setId(UUID.randomUUID().toString());
        transaction.setDate(LocalDate.now());
        Transaction saved = transactionRepository.save(transaction);
        return transactionMapper.toDTO(saved);
    }

    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron transacciones");
        }
        return transactions.stream().map(transactionMapper::toDTO).toList();
    }

    public TransactionDTO getTransactionById(String id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transacción no encontrada"));
        return transactionMapper.toDTO(transaction);
    }

    public List<TransactionDTO> getTransactionByDate(LocalDate date) {
        List<Transaction> transactions = transactionRepository.findAll()
                .stream()
                .filter(t -> t.getDate().equals(date))
                .toList();
        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron transacciones para la fecha: " + date);
        }
        return transactions.stream().map(transactionMapper::toDTO).toList();
    }

    public List<TransactionDTO> getTransactionByAmount(BigDecimal amount) {
        List<Transaction> transactions = transactionRepository.findAll()
                .stream()
                .filter(t -> t.getAmount().compareTo(amount) == 0)
                .toList();
        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron transacciones por ese monto: " + amount);
        }
        return transactions.stream().map(transactionMapper::toDTO).toList();
    }

    public TransactionDTO updateTransaction(String id, TransactionDTO dto) {
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transacción no encontrada"));
        transactionMapper.updateEntity(existing, dto);
        Transaction updated = transactionRepository.save(existing);
        return transactionMapper.toDTO(updated);
    }

    public void deleteTransaction(String id) {
        boolean exists = transactionRepository.existsById(id);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transacción no encontrada");
        }
        transactionRepository.deleteById(id);
    }
}
