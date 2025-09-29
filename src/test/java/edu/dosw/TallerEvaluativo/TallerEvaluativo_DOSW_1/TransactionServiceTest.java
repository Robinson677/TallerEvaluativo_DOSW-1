package edu.dosw.TallerEvaluativo.TallerEvaluativo_DOSW_1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import edu.dosw.TallerEvaluativo.dtos.TransactionDTO;
import edu.dosw.TallerEvaluativo.models.Transaction;
import edu.dosw.TallerEvaluativo.models.TransactionMapper;
import edu.dosw.TallerEvaluativo.repositories.TransactionRepository;
import edu.dosw.TallerEvaluativo.services.TransactionService;

class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private TransactionMapper transactionMapper;
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        transactionRepository = mock(TransactionRepository.class);
        transactionMapper = mock(TransactionMapper.class);
        transactionService = new TransactionService(transactionRepository, transactionMapper);
    }

    @Test
    void testShouldCreateTransaction() {
        TransactionDTO dto = new TransactionDTO();
        dto.setDescription("Test Transaction");
        dto.setAmount(new BigDecimal("100.00"));

        Transaction transaction = new Transaction();
        transaction.setDescription("Test Transaction");
        transaction.setAmount(new BigDecimal("100.00"));

        Transaction savedTransaction = new Transaction();
        savedTransaction.setId("1");
        savedTransaction.setDescription("Test Transaction");
        savedTransaction.setAmount(new BigDecimal("100.00"));
        savedTransaction.setDate(LocalDate.now());

        TransactionDTO expectedDTO = new TransactionDTO();
        expectedDTO.setDescription("Test Transaction");
        expectedDTO.setAmount(new BigDecimal("100.00"));

        when(transactionMapper.toEntity(dto)).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);
        when(transactionMapper.toDTO(savedTransaction)).thenReturn(expectedDTO);

        TransactionDTO result = transactionService.createTransaction(dto);

        assertNotNull(result);
        assertEquals("Test Transaction", result.getDescription());
        assertEquals(new BigDecimal("100.00"), result.getAmount());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testShouldGetAllTransactions() {
        Transaction t1 = new Transaction();
        t1.setId("1");
        t1.setDescription("Transaction 1");
        t1.setAmount(new BigDecimal("50.00"));

        Transaction t2 = new Transaction();
        t2.setId("2");
        t2.setDescription("Transaction 2");
        t2.setAmount(new BigDecimal("75.00"));

        TransactionDTO dto1 = new TransactionDTO();
        dto1.setDescription("Transaction 1");
        dto1.setAmount(new BigDecimal("50.00"));

        TransactionDTO dto2 = new TransactionDTO();
        dto2.setDescription("Transaction 2");
        dto2.setAmount(new BigDecimal("75.00"));

        when(transactionRepository.findAll()).thenReturn(List.of(t1, t2));
        when(transactionMapper.toDTO(t1)).thenReturn(dto1);
        when(transactionMapper.toDTO(t2)).thenReturn(dto2);

        List<TransactionDTO> result = transactionService.getAllTransactions();

        assertEquals(2, result.size());
        assertEquals("Transaction 1", result.get(0).getDescription());
        assertEquals("Transaction 2", result.get(1).getDescription());
    }

    @Test
    void testShouldThrowExceptionWhenNoTransactionsFound() {
        when(transactionRepository.findAll()).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, () -> transactionService.getAllTransactions());
    }

    @Test
    void testShouldGetTransactionById() {
        Transaction transaction = new Transaction();
        transaction.setId("1");
        transaction.setDescription("Test Transaction");
        transaction.setAmount(new BigDecimal("100.00"));

        TransactionDTO dto = new TransactionDTO();
        dto.setDescription("Test Transaction");
        dto.setAmount(new BigDecimal("100.00"));

        when(transactionRepository.findById("1")).thenReturn(Optional.of(transaction));
        when(transactionMapper.toDTO(transaction)).thenReturn(dto);

        TransactionDTO result = transactionService.getTransactionById("1");

        assertNotNull(result);
        assertEquals("Test Transaction", result.getDescription());
        assertEquals(new BigDecimal("100.00"), result.getAmount());
    }

    @Test
    void testShouldThrowExceptionWhenTransactionNotFound() {
        when(transactionRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> transactionService.getTransactionById("99"));
    }

    @Test
    void testShouldGetTransactionByDate() {
        LocalDate testDate = LocalDate.of(2023, 1, 1);
        Transaction transaction = new Transaction();
        transaction.setId("1");
        transaction.setDescription("Test Transaction");
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDate(testDate);

        TransactionDTO dto = new TransactionDTO();
        dto.setDescription("Test Transaction");
        dto.setAmount(new BigDecimal("100.00"));

        when(transactionRepository.findAll()).thenReturn(List.of(transaction));
        when(transactionMapper.toDTO(transaction)).thenReturn(dto);

        List<TransactionDTO> result = transactionService.getTransactionByDate(testDate);

        assertEquals(1, result.size());
        assertEquals("Test Transaction", result.get(0).getDescription());
    }

    @Test
    void testShouldThrowExceptionWhenNoTransactionsFoundByDate() {
        LocalDate testDate = LocalDate.of(2023, 1, 1);
        when(transactionRepository.findAll()).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, () -> transactionService.getTransactionByDate(testDate));
    }

    @Test
    void testShouldGetTransactionByAmount() {
        BigDecimal testAmount = new BigDecimal("100.00");
        Transaction transaction = new Transaction();
        transaction.setId("1");
        transaction.setDescription("Test Transaction");
        transaction.setAmount(testAmount);

        TransactionDTO dto = new TransactionDTO();
        dto.setDescription("Test Transaction");
        dto.setAmount(testAmount);

        when(transactionRepository.findAll()).thenReturn(List.of(transaction));
        when(transactionMapper.toDTO(transaction)).thenReturn(dto);

        List<TransactionDTO> result = transactionService.getTransactionByAmount(testAmount);

        assertEquals(1, result.size());
        assertEquals("Test Transaction", result.get(0).getDescription());
        assertEquals(testAmount, result.get(0).getAmount());
    }

    @Test
    void testShouldThrowExceptionWhenNoTransactionsFoundByAmount() {
        BigDecimal testAmount = new BigDecimal("999.99");
        when(transactionRepository.findAll()).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, () -> transactionService.getTransactionByAmount(testAmount));
    }

    @Test
    void testShouldUpdateTransaction() {
        Transaction existing = new Transaction();
        existing.setId("1");
        existing.setDescription("Old Description");
        existing.setAmount(new BigDecimal("50.00"));

        TransactionDTO dto = new TransactionDTO();
        dto.setDescription("New Description");
        dto.setAmount(new BigDecimal("75.00"));

        Transaction updated = new Transaction();
        updated.setId("1");
        updated.setDescription("New Description");
        updated.setAmount(new BigDecimal("75.00"));

        TransactionDTO updatedDTO = new TransactionDTO();
        updatedDTO.setDescription("New Description");
        updatedDTO.setAmount(new BigDecimal("75.00"));

        when(transactionRepository.findById("1")).thenReturn(Optional.of(existing));
        when(transactionRepository.save(existing)).thenReturn(updated);
        when(transactionMapper.toDTO(updated)).thenReturn(updatedDTO);

        TransactionDTO result = transactionService.updateTransaction("1", dto);

        assertNotNull(result);
        assertEquals("New Description", result.getDescription());
        assertEquals(new BigDecimal("75.00"), result.getAmount());
        verify(transactionMapper).updateEntity(existing, dto);
    }

    @Test
    void testShouldThrowExceptionWhenUpdatingNonExistentTransaction() {
        TransactionDTO dto = new TransactionDTO();
        when(transactionRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> transactionService.updateTransaction("99", dto));
    }

    @Test
    void testShouldDeleteTransaction() {
        when(transactionRepository.existsById("1")).thenReturn(true);
        doNothing().when(transactionRepository).deleteById("1");

        assertDoesNotThrow(() -> transactionService.deleteTransaction("1"));
        verify(transactionRepository).deleteById("1");
    }

    @Test
    void testShouldThrowExceptionWhenDeletingNonExistentTransaction() {
        when(transactionRepository.existsById("99")).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> transactionService.deleteTransaction("99"));
    }
}