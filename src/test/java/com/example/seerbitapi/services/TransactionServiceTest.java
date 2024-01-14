package com.example.seerbitapi.services;

import com.example.seerbitapi.dtos.Statistics;
import com.example.seerbitapi.dtos.Transaction;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author HP
 */
public class TransactionServiceTest {
    private ITransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService();
    }

    @Test
    void addTransactionShouldAddToTransactionList() throws InvalidDateException {
        Transaction transaction = new Transaction(new BigDecimal("100.50"), new Date());
        transactionService.add(transaction);
        assertEquals(1, TransactionService.transactionList.size());
        assertEquals(transaction, TransactionService.transactionList.get(0));
    }

    @Test
    void deleteAllTransactionsShouldClearTransactionList() {
        Transaction transaction = new Transaction( new BigDecimal("100.50"), new Date());
        TransactionService.transactionList.add(transaction);
        transactionService.deleteAll();
        assertEquals(0, TransactionService.transactionList.size());
    }

    @Test
    void getStatisticsShouldCalculateCorrectStatistics() {        
        Transaction recentTransaction = new Transaction(new BigDecimal("100.50"), new Date());
        Transaction oldTransaction = new Transaction(new BigDecimal("50.25"), new Date(System.currentTimeMillis() - 31 * 1000));
        Transaction anotherRecentTransaction = new Transaction( new BigDecimal("200.50"), new Date());
        TransactionService.transactionList.addAll(List.of(recentTransaction, oldTransaction, anotherRecentTransaction));
        
        Statistics statistics = transactionService.getStatistics();
        
        assertEquals(new BigDecimal("301.00"), statistics.sum());
        assertEquals(new BigDecimal("150.50"), statistics.avg());
        assertEquals(new BigDecimal("200.50"), statistics.max());
        assertEquals(new BigDecimal("100.50"), statistics.min());
        assertEquals(2l, statistics.count());
    }
}
