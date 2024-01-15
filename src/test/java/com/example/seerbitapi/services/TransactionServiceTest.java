package com.example.seerbitapi.services;

import com.example.seerbitapi.dtos.Statistics;
import com.example.seerbitapi.dtos.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.LongStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        transactionService.deleteAll();
        Transaction transaction = new Transaction(new BigDecimal("100.50"), new Date());
        transactionService.add(transaction);
        assertEquals(1, TransactionService.transactionList.size());
        assertEquals(transaction, TransactionService.transactionList.peek());
    }

    @Test
    void deleteAllTransactionsShouldClearTransactionList() {
        Transaction transaction = new Transaction(new BigDecimal("100.50"), new Date());
        TransactionService.transactionList.add(transaction);
        transactionService.deleteAll();
        assertEquals(0, TransactionService.transactionList.size());
    }

    @Test
    void getStatisticsShouldCalculateCorrectStatistics() throws InvalidDateException {
        transactionService.deleteAll();
        Transaction recentTransaction = new Transaction(new BigDecimal("100.50"), new Date());
        transactionService.add(recentTransaction);
        Transaction oldTransaction = new Transaction(new BigDecimal("50.25"), new Date(System.currentTimeMillis() - 31 * 1000));
        try {
            transactionService.add(oldTransaction);
            assertTrue(false, "old transaction was added to set");
        } catch (InvalidDateException e) {
            assertTrue(true);
        }
        Transaction anotherRecentTransaction = new Transaction(new BigDecimal("200.50"), new Date());
        transactionService.add(anotherRecentTransaction);

        Statistics statistics = transactionService.getStatistics();

        assertEquals(new BigDecimal("301.00"), statistics.sum());
        assertEquals(new BigDecimal("150.50"), statistics.avg());
        assertEquals(new BigDecimal("200.50"), statistics.max());
        assertEquals(new BigDecimal("100.50"), statistics.min());
        assertEquals(2l, statistics.count());
    }

    @Test
    public void getStatisticsWithValidTimestampMultipleThreadSuccess() throws Exception {
        transactionService.deleteAll();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        BigDecimal amount = BigDecimal.ONE;
        int count = 100;
        long timestamp = new Date().getTime();
        final long CUTOFF_TIME = timestamp + 70000;
        for(int i = 0; i < 100 ; i++ ) {
            long timestep = timestamp + i * 1000;
            final BigDecimal finalAmount = amount;
            executorService.submit(() -> {
                try {
                    transactionService.add(new Transaction(finalAmount, new Date(timestep)));
                } catch (InvalidDateException ex) {
                    Logger.getLogger(TransactionServiceTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            });            
            amount = amount.add(BigDecimal.ONE);            
        }       
        executorService.shutdown();
        Thread.sleep(1000);
        ((TransactionService)transactionService).removeOldTransactions(CUTOFF_TIME);
        Statistics response = transactionService.getStatistics();
        assertEquals(LongStream.range(71, 101).sum(), response.sum().longValue());
        assertEquals(30, response.count());
        assertEquals(new BigDecimal(100), response.max());
        assertEquals(new BigDecimal(71), response.min());
        assertEquals(new BigDecimal(LongStream.range(71, 101).sum()).divide(new BigDecimal(30), RoundingMode.HALF_UP), response.avg());
    }
}
