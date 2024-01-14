package com.example.seerbitapi.services;

import com.example.seerbitapi.dtos.Statistics;
import com.example.seerbitapi.dtos.Transaction;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public interface ITransactionService {
    void add(Transaction transaction) throws InvalidDateException;
    Statistics getStatistics();
    void deleteAll();
}
