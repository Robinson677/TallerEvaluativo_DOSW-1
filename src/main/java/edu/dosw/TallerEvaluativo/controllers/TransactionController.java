package edu.dosw.TallerEvaluativo.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import edu.dosw.TallerEvaluativo.dtos.TransactionDTO;
import edu.dosw.TallerEvaluativo.services.TransactionService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public TransactionDTO createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        return transactionService.createTransaction(transactionDTO);
    }

    @GetMapping
    public List<TransactionDTO> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public TransactionDTO getTransactionById(@PathVariable String id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/by-date")
    public List<TransactionDTO> getTransactionByDate(@RequestParam LocalDate date) {
        return transactionService.getTransactionByDate(date);
    }

    @GetMapping("/by-amount")
    public List<TransactionDTO> getTransactionByAmount(@RequestParam BigDecimal amount) {
        return transactionService.getTransactionByAmount(amount);
    }

    @PutMapping("/{id}")
    public TransactionDTO updateTransaction(@PathVariable String id, @Valid @RequestBody TransactionDTO dto) {
        return transactionService.updateTransaction(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable String id) {
        transactionService.deleteTransaction(id);
    }
}

