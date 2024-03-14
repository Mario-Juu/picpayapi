package com.picpayapi.repository;

import com.picpayapi.domain.user.User;
import com.picpayapi.dto.UserDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Should get user succesfully from DB")
    void findUserByDocumentSuccess() {
        String document = "9999";
        UserDTO data = new UserDTO("Teste", "Teste", document, new BigDecimal(10), "teste@gmail", "44444");
        this.createUser(data);

        Optional<User> foundedUser = this.userRepository.findUserByDocument("9999");
        assertThat(foundedUser.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should don't get User from DB")
    void findUserByDocumentFail() {
        String document = "9999";

        Optional<User> foundedUser = this.userRepository.findUserByDocument("9999");
        assertThat(foundedUser.isEmpty()).isTrue();
    }

    private User createUser(UserDTO dto){
        User newUser = new User(dto);
        this.entityManager.persist(newUser);
        return newUser;
    }
}