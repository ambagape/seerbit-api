package com.example.seerbitapi.services;

import com.example.seerbitapi.services.exceptions.InvalidDateException;
import com.example.seerbitapi.dtos.Statistics;
import com.example.seerbitapi.dtos.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Queue;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ambrose
 */
@Service
public class TransactionService implements ITransactionService {

    static final Queue<Transaction> transactionList = new PriorityQueue<>(Transaction::compareTo);

    static final long POLLING_INTERVAL_RATE_MILLIS = 200;
    static final long VALIDITY_INTERVAL = 30 * 1000;
    static BigDecimal sumOfAmounts = BigDecimal.ZERO;
    static BigDecimal avg = BigDecimal.ZERO;
    static BigDecimal min = BigDecimal.ZERO;
    static BigDecimal max = BigDecimal.ZERO;

    
    /*
    * New transactions are added here. To achieve O(1) complexity, I use running aggregates
    */
    @Override
    public void add(Transaction transaction) throws InvalidDateException {
        synchronized (this) {
            if (transaction.timestamp().getTime() < new Date().getTime() - VALIDITY_INTERVAL) {
                throw new InvalidDateException();
            }
            if (transactionList.isEmpty() && !transaction.amount().equals(BigDecimal.ZERO)) {
                min = transaction.amount();
                max = transaction.amount();
            } else {
                min = transaction.amount().min(min);
                max = transaction.amount().max(max);
            }
            transactionList.add(transaction);
            sumOfAmounts = sumOfAmounts.add(transaction.amount());
        }
    }

    @Override
    public synchronized void deleteAll() {
        synchronized (this) {
            transactionList.clear();
            resetStatistics();
        }
    }

    /*
    * This method returns the statistics of the transactions in the last 30 seconds
    */
    @Override
    public synchronized Statistics getStatistics() {
        avg = !transactionList.isEmpty() ? sumOfAmounts
                .divide(new BigDecimal(transactionList.size()), RoundingMode.HALF_UP) : BigDecimal.ZERO;
        return new Statistics(sumOfAmounts, avg, max, min, transactionList.size());
    }

    /*
    * Scheduled job to run maintenance on the background
    */
    @Scheduled(fixedRate = POLLING_INTERVAL_RATE_MILLIS)
    public Queue<Transaction> removeOldTransactions() {
       final long CUTOFF_TIME = new Date().getTime() - VALIDITY_INTERVAL;
       return removeOldTransactions(CUTOFF_TIME);        
    }
    
    Queue<Transaction> removeOldTransactions(long cutoffTime) {
        synchronized (this) {
            boolean isMinChanged = false;
            boolean isMaxChanged = false;
            while (!transactionList.isEmpty() && transactionList.peek().timestamp().getTime() < cutoffTime) {
                final var transaction = transactionList.poll();
                sumOfAmounts = sumOfAmounts.subtract(transaction.amount());
                if (min == transaction.amount()) {
                    isMinChanged = true;
                }
                if (max == transaction.amount()) {
                    isMaxChanged = true;
                }
            }
            if (isMinChanged) {
                min = transactionList.stream().map(Transaction::amount).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            }
            if (isMaxChanged) {
                max = transactionList.stream().map(Transaction::amount).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            }
            return transactionList;
        }
    }

    private void resetStatistics() {
        sumOfAmounts = BigDecimal.ZERO;
        avg = BigDecimal.ZERO;
        min = BigDecimal.ZERO;
        max = BigDecimal.ZERO;
    }
}
