package com.picpayapi.service;


import com.picpayapi.domain.user.User;
import com.picpayapi.domain.user.UserType;
import com.picpayapi.dto.UserDTO;
import com.picpayapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount){
        if(sender.getUserType() != UserType.COMMON){
            throw new IllegalArgumentException("Only common users can make transactions");
        }

        if(sender.getBalance().compareTo(amount) < 0){
            throw new IllegalArgumentException("Insufficient balance");
        }
    }

    public User findUserById(Long id){
        return repository.findUserById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void saveUser(User user){
        this.repository.save(user);
    }

    public User createUser(UserDTO data){
        User user = new User(data);
        this.repository.save(user);
        return user;
    }

    public List<User> getAllUsers(){
        return repository.findAll();
    }
}
