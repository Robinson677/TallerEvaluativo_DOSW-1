package edu.dosw.TallerEvaluativo.TallerEvaluativo_DOSW_1;


import edu.dosw.TallerEvaluativo.controllers.TransactionController;
import edu.dosw.TallerEvaluativo.dtos.TransactionDTO;
import edu.dosw.TallerEvaluativo.services.TransactionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private TransactionDTO transactionDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        transactionDTO = new TransactionDTO();
        transactionDTO.setDescription("Test Transaction");
        transactionDTO.setAmount(new BigDecimal("100.00"));
    }

    @Test
    void testCreateTransaction() {
        when(transactionService.createTransaction(transactionDTO)).thenReturn(transactionDTO);

        TransactionDTO result = transactionController.createTransaction(transactionDTO);

        assertNotNull(result);
        assertEquals("Test Transaction", result.getDescription());
        assertEquals(new BigDecimal("100.00"), result.getAmount());
        verify(transactionService).createTransaction(transactionDTO);
    }

    @Test
    void testGetAllTransactions() {
        TransactionDTO transaction2 = new TransactionDTO();
        transaction2.setDescription("Second Transaction");
        transaction2.setAmount(new BigDecimal("200.00"));

        when(transactionService.getAllTransactions()).thenReturn(Arrays.asList(transactionDTO, transaction2));

        List<TransactionDTO> result = transactionController.getAllTransactions();

        assertEquals(2, result.size());
        assertEquals("Test Transaction", result.get(0).getDescription());
        assertEquals(new BigDecimal("100.00"), result.get(0).getAmount());
        assertEquals("Second Transaction", result.get(1).getDescription());
        assertEquals(new BigDecimal("200.00"), result.get(1).getAmount());
    }

    @Test
    void testGetTransactionById() {
        when(transactionService.getTransactionById("1")).thenReturn(transactionDTO);

        TransactionDTO result = transactionController.getTransactionById("1");

        assertNotNull(result);
        assertEquals("Test Transaction", result.getDescription());
        assertEquals(new BigDecimal("100.00"), result.getAmount());
        verify(transactionService).getTransactionById("1");
    }

    @Test
    void testGetTransactionByDate() {
        LocalDate testDate = LocalDate.of(2023, 1, 15);

        when(transactionService.getTransactionByDate(testDate)).thenReturn(Arrays.asList(transactionDTO));

        List<TransactionDTO> result = transactionController.getTransactionByDate(testDate);

        assertEquals(1, result.size());
        assertEquals("Test Transaction", result.get(0).getDescription());
        assertEquals(new BigDecimal("100.00"), result.get(0).getAmount());
        verify(transactionService).getTransactionByDate(testDate);
    }

    @Test
    void testGetTransactionByAmount() {
        BigDecimal testAmount = new BigDecimal("100.00");

        when(transactionService.getTransactionByAmount(testAmount)).thenReturn(Arrays.asList(transactionDTO));

        List<TransactionDTO> result = transactionController.getTransactionByAmount(testAmount);

        assertEquals(1, result.size());
        assertEquals("Test Transaction", result.get(0).getDescription());
        assertEquals(testAmount, result.get(0).getAmount());
        verify(transactionService).getTransactionByAmount(testAmount);
    }

    @Test
    void testGetTransactionByAmountMultipleResults() {
        BigDecimal testAmount = new BigDecimal("100.00");

        TransactionDTO transaction2 = new TransactionDTO();
        transaction2.setDescription("Another Transaction");
        transaction2.setAmount(new BigDecimal("100.00"));

        when(transactionService.getTransactionByAmount(testAmount))
                .thenReturn(Arrays.asList(transactionDTO, transaction2));

        List<TransactionDTO> result = transactionController.getTransactionByAmount(testAmount);

        assertEquals(2, result.size());
        assertEquals("Test Transaction", result.get(0).getDescription());
        assertEquals("Another Transaction", result.get(1).getDescription());
        assertEquals(testAmount, result.get(0).getAmount());
        assertEquals(testAmount, result.get(1).getAmount());
    }

    @Test
    void testUpdateTransaction() {
        TransactionDTO updatedDTO = new TransactionDTO();
        updatedDTO.setDescription("Updated Transaction");
        updatedDTO.setAmount(new BigDecimal("150.00"));

        when(transactionService.updateTransaction("1", transactionDTO)).thenReturn(updatedDTO);

        TransactionDTO result = transactionController.updateTransaction("1", transactionDTO);

        assertNotNull(result);
        assertEquals("Updated Transaction", result.getDescription());
        assertEquals(new BigDecimal("150.00"), result.getAmount());
        verify(transactionService).updateTransaction("1", transactionDTO);
    }

    @Test
    void testDeleteTransaction() {
        doNothing().when(transactionService).deleteTransaction("1");

        transactionController.deleteTransaction("1");

        verify(transactionService).deleteTransaction("1");
    }

    @Test
    void testGetTransactionByDateNoResults() {
        LocalDate testDate = LocalDate.of(2023, 12, 31);

        when(transactionService.getTransactionByDate(testDate)).thenReturn(Arrays.asList());

        List<TransactionDTO> result = transactionController.getTransactionByDate(testDate);

        assertEquals(0, result.size());
        verify(transactionService).getTransactionByDate(testDate);
    }

    @Test
    void testGetTransactionByAmountNoResults() {
        BigDecimal testAmount = new BigDecimal("999.99");

        when(transactionService.getTransactionByAmount(testAmount)).thenReturn(Arrays.asList());

        List<TransactionDTO> result = transactionController.getTransactionByAmount(testAmount);

        assertEquals(0, result.size());
        verify(transactionService).getTransactionByAmount(testAmount);
    }
}
