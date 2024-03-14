package com.picpayapi.service;

import com.picpayapi.domain.transaction.Transaction;
import com.picpayapi.domain.user.User;
import com.picpayapi.dto.TransactionalDTO;
import com.picpayapi.repository.TransactionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.ServiceUnavailableException;
import java.time.LocalDateTime;

@Service
public class TransactionalService {

    @Autowired
    UserService userService;

    @Autowired
    private TransactionalRepository repository;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionalDTO transaction) throws ServiceUnavailableException {
        User sender = userService.findUserById(transaction.senderId());
        User receiver = userService.findUserById(transaction.receiverId());
        userService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = this.authorizationService.authorizeTransaction(sender, transaction.value());
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

        this.notificationService.sendNotification(sender, "Transação realizada com sucesso");

        this.notificationService.sendNotification(receiver, "Transação recebida com sucesso");

        return transactionEntity;
    }


}
