package com.picpayapi.service;

import com.picpayapi.domain.user.User;
import com.picpayapi.domain.user.UserType;
import com.picpayapi.dto.TransactionalDTO;
import com.picpayapi.repository.TransactionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import javax.naming.ServiceUnavailableException;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestComponent
class TransactionalServiceTest {

    @Mock
    UserService userService;

    @Mock
    private TransactionalRepository repository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private NotificationService notificationService;

    @Autowired
    @InjectMocks
    private TransactionalService transactionalService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create transaction successfully")
    void createTransactionSucesss() throws ServiceUnavailableException {
        User sender = new User(1L, "Maria", "Souza", "999901", "maria@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(1L, "Joao", "Souza", "999902", "joao@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);
        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);
        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(true);

        TransactionalDTO request = new TransactionalDTO(new BigDecimal(5), 1L, 2L);
        transactionalService.createTransaction(request);

        verify(repository, times(1)).save(any());

        sender.setBalance(sender.getBalance().subtract(request.value()));
        verify(userService, times(1)).saveUser(sender);

        receiver.setBalance(receiver.getBalance().add(request.value()));
        verify(userService, times(1)).saveUser(receiver);

        verify(notificationService, times(1)).sendNotification(sender, "Transação realizada com sucesso");
        verify(notificationService, times(1)).sendNotification(receiver, "Transação recebida com sucesso");
    }

    @Test
    @DisplayName("Should not create transaction successfully")
    void createTransactionFail() {
    }
}