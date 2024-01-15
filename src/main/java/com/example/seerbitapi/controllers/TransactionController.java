package com.example.seerbitapi.controllers;

import com.example.seerbitapi.dtos.Transaction;
import com.example.seerbitapi.services.ITransactionService;
import com.example.seerbitapi.services.exceptions.InvalidDateException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@RestController
public class TransactionController {    
    
    final ITransactionService service;
    
    @Autowired
    public TransactionController(ITransactionService service) {
        this.service = service;
    }
    
    @PostMapping("transactions")
    public ResponseEntity createTransaction(@RequestBody @Valid Transaction transaction) throws InvalidDateException{           
        this.service.add(transaction);
        return status(201).build();
    }
    
    @GetMapping("/statistics")
    public ResponseEntity createTransaction(){
        final var stats = this.service.getStatistics();
        return ok(stats);
    }
    
    @DeleteMapping("/transactions")
    public ResponseEntity deleteAll(){
        this.service.deleteAll();
        return status(204).build();
    }
}
