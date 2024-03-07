package com.picpayapi.service;

import com.picpayapi.domain.transaction.Transaction;
import com.picpayapi.domain.user.User;
import com.picpayapi.dto.TransactionalDTO;
import com.picpayapi.repository.TransactionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionalService {

    @Autowired
    UserService userService;

    @Autowired
    private TransactionalRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    public void createTransaction(TransactionalDTO transaction){
        User sender = userService.findUserById(transaction.senderId());
        User receiver = userService.findUserById(transaction.receiverId());
        userService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
        if(!isAuthorized){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction not authorized");
        }

        Transaction transactionEntity = new Transaction();
        transactionEntity.setAmount(transaction.value());
        transactionEntity.setSender(sender);
        transactionEntity.setReceiver(receiver);
        transactionEntity.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        repository.save(new Transaction());
        userService.saveUser(sender);
        userService.saveUser(receiver);
    }

    public boolean authorizeTransaction(User sender, BigDecimal value){
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc", Map.class);
        return authorizationResponse.getStatusCode() == HttpStatus.OK && authorizationResponse.getBody().get("message").equals("Autorizado");
    }
}
