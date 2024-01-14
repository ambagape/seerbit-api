package com.example.seerbitapi.services;

import com.example.seerbitapi.dtos.Statistics;
import com.example.seerbitapi.dtos.Transaction;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ambrose
 */
@Service
public class TransactionService implements ITransactionService {

    static final List<Transaction> transactionList = new LinkedList<>();
    
    final long VALIDITY_INTERVAL = 30 * 1000;

    @Override
    public void add(Transaction transaction) throws InvalidDateException{
        if(transaction.timestamp().getTime() < new Date().getTime() - VALIDITY_INTERVAL){
            throw new InvalidDateException();
        }
        transactionList.add(transaction);
    }

    @Override
    public void deleteAll() {
        transactionList.clear();
    }

    @Override
    public Statistics getStatistics() {
        this.removeOldTransactions();
        final var size = transactionList.size();
        final var amounts = transactionList.stream()
                .map(Transaction::amount)
                .collect(Collectors.toList());        
        final var sum = amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        final var avg = size == 0 || sum.equals(BigDecimal.ZERO) ? BigDecimal.ZERO: sum.divide(new BigDecimal(size));
        final var min = amounts.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        final var max = amounts.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        return new Statistics(sum, avg, max, min, size);
    }

    private List<Transaction> removeOldTransactions() {
        transactionList.removeIf((transaction) -> {
            return transaction.timestamp().getTime() + VALIDITY_INTERVAL < new Date().getTime();
        });
        return transactionList;
    }

}
