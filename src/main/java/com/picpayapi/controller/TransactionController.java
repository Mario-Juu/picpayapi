package com.picpayapi.controller;

import com.picpayapi.domain.transaction.Transaction;
import com.picpayapi.dto.TransactionalDTO;
import com.picpayapi.service.TransactionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.ServiceUnavailableException;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionalService transactionalService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionalDTO transaction) throws ServiceUnavailableException {
        Transaction newTransaction = transactionalService.createTransaction(transaction);
        return ResponseEntity.ok(newTransaction);
    }
}
